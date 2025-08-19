package Talk.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageBubble extends JPanel {
    private final JLabel meta;
    private final JTextArea text;
    private boolean mine;

    public MessageBubble(boolean mine, String body, long ts, boolean read) {
        this.mine = mine;
        setOpaque(false);
        setLayout(new BorderLayout(6, 2));

        // meta on left (time + read)
        meta = new JLabel(buildMeta(ts, read));
        meta.setFont(new Font("Dialog", Font.PLAIN, 11));
        meta.setForeground(new Color(130,130,130));

        // body
        text = new JTextArea(body == null ? "" : body);
        text.setEditable(false);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setOpaque(true);
        text.setBorder(new EmptyBorder(8, 10, 8, 10));
        text.setBackground(mine ? new Color(255, 229, 200) : Color.WHITE);
        text.setForeground(Color.DARK_GRAY);
        text.setFont(new Font("Dialog", Font.PLAIN, 14));

        JPanel bubbleHolder = new JPanel(new BorderLayout());
        bubbleHolder.setOpaque(false);
        bubbleHolder.add(text, BorderLayout.CENTER);

        if (mine) {
            add(meta, BorderLayout.WEST);
            add(bubbleHolder, BorderLayout.CENTER);
        } else {
            add(meta, BorderLayout.WEST);
            add(bubbleHolder, BorderLayout.CENTER);
        }
    }

    private String buildMeta(long ts, boolean read) {
        String time = new SimpleDateFormat("HH:mm").format(new Date(ts));
        // unread 표시: 읽지 않음이면 "1", 읽었으면 공백
        String badge = read ? " " : "1";
        return badge + "  " + time;
    }

    public void setRead(boolean read, long ts) {
        meta.setText(buildMeta(ts, read));
        revalidate();
        repaint();
    }
}