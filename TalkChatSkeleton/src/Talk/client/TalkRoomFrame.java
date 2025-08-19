package Talk.client;

import Talk.common.Protocol;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Base64;

public class TalkRoomFrame extends JFrame implements ActionListener, Runnable {
    private final Socket sock;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int roomId;

    private final DefaultListModel<JPanel> model = new DefaultListModel<>();
    private final JList<JPanel> list = new JList<>(model);
    private final JTextField input = new JTextField();
    private final JButton sendBtn = new JButton("전송");
    private final JButton imgBtn = new JButton("사진");

    public TalkRoomFrame(Socket s, BufferedReader in, PrintWriter out, int roomId) {
        this.sock = s; this.in = in; this.out = out; this.roomId = roomId;

        setTitle("room " + roomId);
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(Theme.BG);
        list.setBackground(Theme.BG);
        list.setFixedCellHeight(-1);
        list.setCellRenderer((lst, value, index, isSelected, cellHasFocus) -> {
            value.setBackground(Theme.BG);
            value.setOpaque(true);
            return value;
        });
        styleButton(sendBtn);
        styleButton(imgBtn);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.HEADER);
        JLabel title = new JLabel("room " + roomId, SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(new EmptyBorder(8,8,8,8));
        header.add(title, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(list);
        sp.getViewport().setBackground(Theme.BG);
        sp.setBorder(null);

        JPanel bottom = new JPanel(new BorderLayout(5,5));
        bottom.setBackground(Theme.BG);
        bottom.add(imgBtn, BorderLayout.WEST);
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        sendBtn.addActionListener(this);
        input.addActionListener(this);
        imgBtn.addActionListener(this);

        out.println(Protocol.ENTER + "|" + roomId);
        out.println(Protocol.HISTORY + "|" + roomId + "|0");

        new Thread(this, "receiver").start();
    }

    private static void styleButton(JButton b) {
        b.setBackground(Theme.BUTTON);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == sendBtn || src == input) {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                out.println(Protocol.SAY + "|" + roomId + "|" + text);
                input.setText("");
            }
        } else if (src == imgBtn) {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File f = fc.getSelectedFile();
                    byte[] bin = Files.readAllBytes(f.toPath());
                    String b64 = Base64.getEncoder().encodeToString(bin);
                    out.println(Protocol.IMAGE + "|" + roomId + "|" + f.getName() + "|" + b64);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "이미지 전송 실패: " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.startsWith(Protocol.MSG + "|")) continue;
                String[] p = line.split("\\|", -1);
                int r = Integer.parseInt(p[2]);
                String sender = p[4];
                String type = p[5];
                String payload = p[6];
                if (r != roomId) continue;

                SwingUtilities.invokeLater(() -> {
                    model.addElement(renderBubble(sender, type, payload));
                    list.ensureIndexIsVisible(model.size()-1);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel renderBubble(String name, String type, String payload) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(true);
        wrap.setBackground(Theme.BG);
        wrap.setBorder(new EmptyBorder(10,12,10,12));

        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setOpaque(true);
        bubble.setBackground(Theme.BUBBLE);
        bubble.setBorder(new EmptyBorder(10,12,10,12));

        JLabel who = new JLabel(name);
        who.setForeground(Theme.BUTTON);
        bubble.add(who, BorderLayout.NORTH);

        if ("IMAGE".equals(type)) {
            try {
                ImageIcon icon = new ImageIcon(payload);
                Image img = icon.getImage().getScaledInstance(320, -1, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                bubble.add(new JLabel(icon), BorderLayout.CENTER);
            } catch (Exception e) {
                bubble.add(new JLabel("[이미지 로드 실패: " + payload + "]"), BorderLayout.CENTER);
            }
        } else {
            JTextArea ta = new JTextArea(payload);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setEditable(false);
            ta.setOpaque(false);
            bubble.add(ta, BorderLayout.CENTER);
        }

        wrap.add(bubble, BorderLayout.CENTER);
        return wrap;
    }
}
