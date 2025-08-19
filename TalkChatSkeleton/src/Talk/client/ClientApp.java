package Talk.client;

import Talk.common.KV;
import Talk.common.Protocol;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ClientApp().start();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "시작 실패: " + e.getMessage());
            }
        });
    }

    private void start() throws Exception {
        String host = "127.0.0.1";
        int port = 9192;
        Socket sock = new Socket(host, port);
        sock.setTcpNoDelay(true);
        sock.setSoTimeout(7000);

        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"), true);

        JPanel p = new JPanel(new GridLayout(0,1,4,4));
        JTextField id = new JTextField();
        JPasswordField pw = new JPasswordField();
        p.add(new JLabel("아이디"));
        p.add(id);
        p.add(new JLabel("비밀번호"));
        p.add(pw);
        int r = JOptionPane.showConfirmDialog(null, p, "로그인", JOptionPane.OK_CANCEL_OPTION);
        if (r!=JOptionPane.OK_OPTION) { sock.close(); return; }

        Map<String,String> kv = new LinkedHashMap<>();
        kv.put("id", id.getText().trim());
        kv.put("pw", new String(pw.getPassword()));
        out.println(Protocol.LOGIN+" "+KV.build(kv));

        String line = safeReadLine(in);
        if (line==null || !line.startsWith(Protocol.LOGIN_OK)) {
            JOptionPane.showMessageDialog(null, "로그인 실패");
            sock.close(); return;
        }
        Map<String,String> m = KV.parse(line.substring(Protocol.LOGIN_OK.length()).trim());
        int myId = Integer.parseInt(m.get("user_id"));
        String myName = m.get("name");
        String myRole = m.get("role");

        TalkListFrame f = new TalkListFrame(myId, myName, myRole, in, out);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static String safeReadLine(BufferedReader in) throws IOException {
        while (true) {
            try {
                return in.readLine();
            } catch (java.net.SocketTimeoutException ignore) {
                // keep waiting
            }
        }
    }
}
