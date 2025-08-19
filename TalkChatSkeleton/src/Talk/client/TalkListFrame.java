
package Talk.client;

import Talk.common.KV;
import Talk.common.Protocol;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.List;

/**
 * TalkListFrame
 * - 창 자동 호출(수신 시 자동 오픈) 제거.
 * - 입장(ENTER) 시에만 창을 열고, 서버에서 내려주는 히스토리를 버퍼링 후 그린다.
 * - 실시간 수신(MSG/MSG_IMAGE)은 이미 열려있는 창에만 그린다.
 */
public class TalkListFrame extends JFrame {
    private final int myId;
    private final String myName;
    private final String myRole;
    private final BufferedReader in;
    private final PrintWriter out;

    private final DefaultListModel<Partner> model = new DefaultListModel<>();
    private final JList<Partner> list = new JList<>(model);
    private final JButton openBtn = new JButton("대화 시작하기");

    // 열린 채팅창
    private final Map<Long, ChatWindow> windows = new HashMap<>();
    // 히스토리 버퍼 (HISTORY_BEGIN ~ HISTORY_END 사이 라인 저장)
    private final Map<Long, java.util.List<Runnable>> histBuf = new HashMap<>();

    
    // ---- Safe parsers ----
    private static long parseLongSafe(String s, long defVal) {
        if (s == null) return defVal;
        s = s.trim();
        if (s.isEmpty()) return defVal;
        try { return Long.parseLong(s); } catch (Exception e) { return defVal; }
    }
    private static int parseIntSafe(String s, int defVal) {
        if (s == null) return defVal;
        s = s.trim();
        if (s.isEmpty()) return defVal;
        try { return Integer.parseInt(s); } catch (Exception e) { return defVal; }
    }
    public TalkListFrame(int myId, String myName, String myRole, BufferedReader in, PrintWriter out) {
        super("대화 목록");
        this.myId = myId;
        this.myName = myName;
        this.myRole = myRole;
        this.in = in;
        this.out = out;

        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JLabel head = new JLabel("대화 목록", SwingConstants.CENTER);
        head.setOpaque(true);
        head.setBackground(Color.decode("#F27D16"));
        head.setForeground(Color.WHITE);
        head.setFont(new Font("Dialog", Font.BOLD, 20));
        getContentPane().add(head, BorderLayout.NORTH);

        list.setBorder(new EmptyBorder(10, 10, 10, 10));
        list.setBackground(Color.decode("#F6EFE6"));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer((JList<? extends Partner> l, Partner val, int idx, boolean sel, boolean foc) -> {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(sel ? Color.decode("#FFD36B") : Color.decode("#FFE38A"));
            p.setBorder(BorderFactory.createCompoundBorder(
                    new EmptyBorder(6,12,6,12),
                    BorderFactory.createLineBorder(new Color(255, 210, 120))
            ));
            JLabel lb = new JLabel(val.name);
            lb.setFont(new Font("Dialog", Font.PLAIN, 16));
            p.add(lb, BorderLayout.CENTER);
            return p;
        });
        list.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openChat();
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        openBtn.setBackground(java.awt.Color.decode("#5A3222"));
        openBtn.setForeground(java.awt.Color.WHITE);
        openBtn.setFocusPainted(false);
        south.add(openBtn);
        getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);

        openBtn.addActionListener(e -> openChat());

        // 네트워크 수신
        new Thread(this::netLoop, "list-net").start();
        // 초기 방 목록 요청
        out.println(Protocol.LIST_ROOMS);
    }

    private void openChat() {
        Partner p = list.getSelectedValue();
        if (p == null) return;
        Map<String,String> m = new LinkedHashMap<>();
        m.put("partner_id", String.valueOf(p.userId));
        out.println(Protocol.ENTER + " " + KV.build(m));
    }

    private void netLoop() {
        try {
            while (true) {
                String line = safeReadLine();
                if (line == null) break;
                final String msg = line;
                SwingUtilities.invokeLater(() -> handleLine(msg));
            }
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "서버 연결 오류: " + ex.getMessage()));
        }
    }

    private void handleLine(String line) {
        if (line.startsWith(Protocol.ROOMS)) {
            Map<String,String> m = KV.parse(line.substring(Protocol.ROOMS.length()).trim());
            String rooms = m.get("rooms");
            model.clear();
            if (rooms != null && !rooms.isEmpty()) {
                for (String token : rooms.split("\\|")) {
                    String[] a = token.split(":", 2);
                    model.addElement(new Partner(Integer.parseInt(a[0]), a.length>1 ? a[1] : "user"));
                }
            }
        } else if (line.startsWith(Protocol.HISTORY_BEGIN)) {
            Map<String,String> m = KV.parse(line.substring(Protocol.HISTORY_BEGIN.length()).trim());
            long rid = parseLongSafe(m.get("room_id"), -1);
            String partner = m.get("partner");
            // 버퍼 준비 + 창 열기 (입장시에만)
            histBuf.put(rid, new ArrayList<>());
            openOrGetWindow(rid, partner);
        } else if (line.startsWith(Protocol.MSG + " ")) {
            Map<String,String> m = KV.parse(line.substring(Protocol.MSG.length()).trim());
            long rid = parseLongSafe(m.get("room_id"), -1);
            if (rid < 0) { Long g = guessActiveRoomId(); if (g != null) rid = g; }
            final long rid0 = rid;

            long mid = parseLongSafe(m.get("message_id"), -1);
            int sender = parseIntSafe(m.get("sender_id"), -1);
            String text = m.get("text");
            long ts = parseLongSafe(m.get("ts"), System.currentTimeMillis());
            String tag = m.get("client_tag");

            ChatWindow w = windows.get(rid0);
            java.util.List<Runnable> buf = histBuf.get(rid0);
            if (buf != null) {
                buf.add(() -> {
                    ChatWindow w2 = windows.get(rid0);
                    if (w2 != null) {
                        if (tag != null) w2.addIncomingText(mid, sender, text, ts, sender==myId, tag);
                        else w2.addIncomingText(mid, sender, text, ts, sender==myId);
                    }
                });
            } else if (w != null) {
                if (tag != null) w.addIncomingText(mid, sender, text, ts, sender==myId, tag);
                else w.addIncomingText(mid, sender, text, ts, sender==myId);
            }
        } else if (line.startsWith(Protocol.MSG_IMAGE)) {
            Map<String,String> m = KV.parse(line.substring(Protocol.MSG_IMAGE.length()).trim());
            long rid = parseLongSafe(m.get("room_id"), -1);
            if (rid < 0) { Long g = guessActiveRoomId(); if (g != null) rid = g; }
            final long rid0 = rid;

            long mid = parseLongSafe(m.get("message_id"), -1);
            int sender = parseIntSafe(m.get("sender_id"), -1);
            String b64 = m.get("data");
            String name = m.get("name");
            long ts = parseLongSafe(m.get("ts"), System.currentTimeMillis());
            String tag = m.get("client_tag");

            ChatWindow w = windows.get(rid0);
            java.util.List<Runnable> buf = histBuf.get(rid0);
            if (buf != null) {
                buf.add(() -> {
                    ChatWindow w2 = windows.get(rid0);
                    if (w2 != null) {
                        if (tag != null) w2.addIncomingImage(mid, sender, b64, name, ts, sender==myId, tag);
                        else w2.addIncomingImage(mid, sender, b64, name, ts, sender==myId);
                    }
                });
            } else if (w != null) {
                if (tag != null) w.addIncomingImage(mid, sender, b64, name, ts, sender==myId, tag);
                else w.addIncomingImage(mid, sender, b64, name, ts, sender==myId);
            }
        } else if (line.startsWith(Protocol.READ_ACK)) {
            Map<String,String> m = KV.parse(line.substring(Protocol.READ_ACK.length()).trim());
            long rid = parseLongSafe(m.get("room_id"), -1);
            long mid = parseLongSafe(m.get("message_id"), -1);
            ChatWindow w = windows.get(rid);
            if (w != null) w.markRead(mid);
        } else if (line.startsWith(Protocol.HISTORY_END)) {
            Map<String,String> m = KV.parse(line.substring(Protocol.HISTORY_END.length()).trim());
            long rid = parseLongSafe(m.get("room_id"), -1);
            java.util.List<Runnable> buf = histBuf.remove(rid);
            if (buf != null) {
                ChatWindow w = windows.get(rid);
                if (w != null) {
                    for (Runnable r : buf) r.run();
                }
            }
        }
    }

    /** 입장 시에만 창 생성/가져오기 */
    
    private Long guessActiveRoomId() {
        try {
            if (windows.size() == 1) {
                return windows.values().iterator().next().getRoomId();
            }
        } catch (Exception ignore) {}
        return null;
    }
    private ChatWindow openOrGetWindow(long roomId, String partnerName) {
        ChatWindow w = windows.get(roomId);
        if (w == null) {
            w = new ChatWindow(roomId, myId, partnerName, out);
            windows.put(roomId, w);
            w.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e) {
                    windows.remove(roomId);
                }
            });
            w.setLocationRelativeTo(this);
            w.setVisible(true);
        } else {
            w.toFront();
        }
        return w;
    }

    private String safeReadLine() throws IOException {
        while (true) {
            try {
                return in.readLine();
            } catch (SocketTimeoutException ste) {
                // keep waiting
            }
        }
    }

    private static class Partner {
        final int userId; final String name;
        Partner(int id, String name) { this.userId = id; this.name = name; }
        @Override public String toString() { return name; }
    }
}
