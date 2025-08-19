package Talk.client;

import Talk.common.Protocol;
import Talk.common.KV;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChatWindow extends JFrame {
    private final long roomId;
    private final int myId;
    private final String partnerName;
    private final PrintWriter out;

    private final JPanel messages = new JPanel();
    private final JScrollPane scroll;
    private final JTextField input = new JTextField();
    private final JButton sendBtn = new JButton("전송");
    private final JButton attachBtn = new JButton("사진");

    // 메시지ID -> 버블 컴포넌트 (내/상대 모두 저장; 내 버블에만 readBadge가 존재)
    private final Map<Long, JComponent> bubbleById = new LinkedHashMap<>();
    private int fixedContentWidth = 480;

    public ChatWindow(long roomId, int myId, String partnerName, PrintWriter out) {
        this.roomId = roomId;
        this.myId = myId;
        this.partnerName = partnerName;
        this.out = out;

        setTitle(partnerName + " (room " + roomId + ")");
        setSize(720, 840);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#F27D16"));
        JLabel title = new JLabel(partnerName, SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(8,8,8,8));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title, BorderLayout.CENTER);

        // messages area
        messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
        messages.setOpaque(true);
        messages.setBackground(Color.decode("#F6EFE6"));
        messages.setBorder(new EmptyBorder(8,8,8,8));

        scroll = new JScrollPane(messages);
        scroll.getViewport().setBackground(Color.decode("#F6EFE6"));
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        // input area
        JPanel south = new JPanel(new BorderLayout(6,6));
        south.setBackground(Color.decode("#F6EFE6"));
        styleButton(sendBtn);
        styleButton(attachBtn);
        south.add(attachBtn, BorderLayout.WEST);
        south.add(input, BorderLayout.CENTER);
        south.add(sendBtn, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        // actions
        sendBtn.addActionListener(this::onSend);
        input.addActionListener(this::onSend);
        attachBtn.addActionListener(this::onAttach);
    }

    private void styleButton(JButton b) {
        b.setBackground(Color.decode("#5A3222"));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(8,16,8,16));
    }

    public long getRoomId() { return roomId; }

    // ===== 수신 렌더링 =====
    public void addIncomingText(long messageId, int senderId, String text, long ts, boolean mine) {
        SwingUtilities.invokeLater(() -> {
            JPanel bubble = bubbleText(text);
            JComponent meta = sideMeta(ts, mine);
            addRowWithMeta(bubble, meta, mine);
            bubbleById.put(messageId, bubble);
            if (!mine) sendReadAckToServer(messageId); // 수신 즉시 읽음
            scrollToBottom();
        });
    }
    public void addIncomingText(long messageId, int senderId, String text, long ts, boolean mine, String tag) {
        addIncomingText(messageId, senderId, text, ts, mine);
    }

    public void addIncomingImage(long messageId, int senderId, String b64, String name, long ts, boolean mine) {
        SwingUtilities.invokeLater(() -> {
            JPanel bubble = bubbleImage(b64, name);
            JComponent meta = sideMeta(ts, mine);
            addRowWithMeta(bubble, meta, mine);
            bubbleById.put(messageId, bubble);
            if (!mine) sendReadAckToServer(messageId);
            scrollToBottom();
        });
    }
    public void addIncomingImage(long messageId, int senderId, String b64, String name, long ts, boolean mine, String tag) {
        addIncomingImage(messageId, senderId, b64, name, ts, mine);
    }

    // ===== 전송 (서버가 요구하는 KV 포맷) =====
    private void onSend(ActionEvent e) {
        String text = input.getText();
        if (text == null) text = "";
        if (text.trim().isEmpty()) return;

        Map<String,String> kv = new LinkedHashMap<>();
        kv.put("room_id", String.valueOf(roomId));
        kv.put("sender_id", String.valueOf(myId));
        kv.put("text", text);
        kv.put("ts", String.valueOf(System.currentTimeMillis()));
        kv.put("client_tag", genTag());
        out.println(Protocol.SAY + " " + KV.build(kv));
        out.flush(); // 중요: 즉시 전송

        input.setText("");
        input.requestFocusInWindow();
    }

    private void onAttach(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                byte[] bin = Files.readAllBytes(f.toPath());
                // null 방지 + 서버 호환키 모두 포함
                String b64 = java.util.Base64.getEncoder().encodeToString(bin);
                if (b64 == null) b64 = "";

                Map<String,String> kv = new LinkedHashMap<>();
                kv.put("room_id", String.valueOf(roomId));
                kv.put("sender_id", String.valueOf(myId));
                kv.put("name", f.getName());
                kv.put("b64", b64);   // 표준 키
                kv.put("data", b64);  // 서버 호환 키 (혹시 data를 읽는 서버용)
                kv.put("size", String.valueOf(bin.length));
                String mime = Files.probeContentType(f.toPath());
                if (mime != null) kv.put("mime", mime);
                kv.put("ts", String.valueOf(System.currentTimeMillis()));
                kv.put("client_tag", genTag());
                out.println(Protocol.SAY_IMAGE + " " + KV.build(kv)); // 서버 스펙 = SAY_IMAGE
                out.flush();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "이미지 전송 실패: " + ex.getMessage());
            }
        }
        input.requestFocusInWindow();
    }

    private void sendReadAckToServer(long messageId) {
        try {
            Map<String,String> kv = new LinkedHashMap<>();
            kv.put("room_id", String.valueOf(roomId));
            kv.put("message_id", String.valueOf(messageId));
            out.println(Protocol.READ + " " + KV.build(kv));
            out.flush();
        } catch (Exception ignore) {}
    }

    // ===== 읽음 처리 =====
    // 서버가 message_id=X 로 ACK을 보내면, X 이하의 내 메시지들의 '1'을 전부 제거(누락 방지).
    public void markRead(long messageId) {
        SwingUtilities.invokeLater(() -> {
            for (Map.Entry<Long,JComponent> e : bubbleById.entrySet()) {
                if (e.getKey() <= messageId) hideReadBadge(e.getValue());
            }
        });
    }
    private void hideReadBadge(JComponent bubble) {
        Object prop = bubble.getClientProperty("readBadge");
        if (prop instanceof JLabel) {
            JLabel lb = (JLabel) prop;
            if (lb.isVisible()) {
                lb.setVisible(false);
                Container parent = lb.getParent();
                if (parent != null) { parent.revalidate(); parent.repaint(); }
            }
            return;
        }
        JLabel lb = findReadBadge(bubble);
        if (lb != null && lb.isVisible()) {
            lb.setVisible(false);
            Container parent = lb.getParent();
            if (parent != null) { parent.revalidate(); parent.repaint(); }
        }
    }

    // ===== 버블/레이아웃 =====
    private JPanel bubbleText(String text) {
        JPanel p = styledPanel();
        JTextArea ta = new JTextArea(text);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setOpaque(false);
        ta.setBorder(null);
        p.add(ta, BorderLayout.CENTER);
        return p;
    }

    private JPanel bubbleImage(String b64, String name) {
        JPanel p = styledPanel();
        try {
            byte[] bin = (b64==null) ? new byte[0] : java.util.Base64.getDecoder().decode(b64);
            JLabel img = imageLabelScaled(bin, fixedContentWidth-20);
            p.add(img, BorderLayout.CENTER);
        } catch (Exception ex) {
            p.add(new JLabel("이미지를 표시할 수 없습니다"), BorderLayout.CENTER);
        }
        return p;
    }

    private JPanel styledPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(true);
        p.setBackground(Color.decode("#FFE38A"));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE6,0xC7,0x8A)),
                new EmptyBorder(10,12,10,12)
        ));
        return p;
    }

    private JLabel imageLabelScaled(byte[] bin, int maxW) {
        try {
            ImageIcon icon = new ImageIcon(bin);
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            if (w > maxW) {
                int nh = (int)((double)h * maxW / w);
                Image scaled = icon.getImage().getScaledInstance(maxW, nh, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);
            }
            return new JLabel(icon);
        } catch (Exception ex) {
            return new JLabel("[이미지 로드 실패]");
        }
    }

    // mine: 오른쪽 정렬(메타 ← 버블), opponent: 왼쪽 정렬(버블 → 메타)
    private void addRowWithMeta(JComponent bubble, JComponent meta, boolean mine) {
        Dimension pref = bubble.getPreferredSize();
        Dimension fixed = new Dimension(fixedContentWidth, pref.height);
        bubble.setPreferredSize(fixed);
        bubble.setMaximumSize(fixed);
        bubble.setMinimumSize(new Dimension(fixedContentWidth, 10));

        JLabel badge = findReadBadge(meta);
        if (badge != null) bubble.putClientProperty("readBadge", badge);

        JPanel row = new JPanel(new FlowLayout(mine ? FlowLayout.RIGHT : FlowLayout.LEFT, 4, 0));
        row.setOpaque(false);

        if (mine) { row.add(meta); row.add(bubble); }
        else      { row.add(bubble); row.add(meta); }

        messages.add(row);
        messages.add(Box.createVerticalStrut(6));
        messages.revalidate();
        messages.repaint();
        scrollToBottom();
    }

    // 내 메시지: 위=1, 아래=시간 / 상대: 시간만
    private JPanel sideMeta(long ts, boolean mine) {
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        if (mine) {
            JLabel unread = new JLabel("1");
            unread.setFont(unread.getFont().deriveFont(11f));
            unread.setName("readBadge");
            unread.setAlignmentX(0f);
            box.add(unread);
        }
        JLabel time = new JLabel(formatKakaoTime(ts));
        time.setFont(time.getFont().deriveFont(11f));
        time.setAlignmentX(0f);
        box.add(time);
        return box;
    }

    private JLabel findReadBadge(Component c) {
        if (c instanceof JLabel && "readBadge".equals(((JLabel) c).getName())) return (JLabel)c;
        if (c instanceof Container) {
            for (Component ch : ((Container)c).getComponents()) {
                JLabel r = findReadBadge(ch);
                if (r != null) return r;
            }
        }
        return null;
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar v = scroll.getVerticalScrollBar();
            v.setValue(v.getMaximum());
        });
    }

    private String formatKakaoTime(long ts) {
        Date d = new Date(ts);
        SimpleDateFormat ap = new SimpleDateFormat("a h:mm");
        String s = ap.format(d);
        s = s.replace("AM","오전").replace("PM","오후");
        return s;
    }

    private static String genTag() {
        return Long.toString(System.currentTimeMillis()) + "-" + Integer.toHexString((int)(Math.random()*0xFFFF));
    }
}
