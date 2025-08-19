package Talk.client;

import Talk.client.ui.UIStyle;
import Talk.common.KV;
import Talk.common.Protocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TalkAWT extends JFrame {
    private JTextField tfId;
    private JPasswordField tfPw;
    private JButton btnLogin;
    private JLabel lblStatus;
    private Socket sock;
    private BufferedReader in;
    private PrintWriter out;

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 9192;

    public TalkAWT() {
        setTitle("관리자 문의 - 로그인");
        setSize(UIStyle.WIDTH, UIStyle.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG);

        JPanel header = new JPanel();
        header.setBackground(UIStyle.BRAND);
        header.setPreferredSize(new Dimension(UIStyle.WIDTH, 60));
        JLabel title = new JLabel("관리자 문의");
        title.setForeground(Color.WHITE);
        title.setFont(UIStyle.TITLE_FONT);
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setBackground(UIStyle.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lid = new JLabel("아이디");
        lid.setFont(UIStyle.BODY_FONT);
        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        card.add(lid, c);

        tfId = new JTextField();
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        card.add(tfId, c);

        JLabel lpw = new JLabel("비밀번호");
        lpw.setFont(UIStyle.BODY_FONT);
        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        card.add(lpw, c);

        tfPw = new JPasswordField();
        c.gridx = 1; c.gridy = 1; c.weightx = 1;
        card.add(tfPw, c);

        btnLogin = new JButton("로그인");
        btnLogin.setBackground(java.awt.Color.decode("#5A3222"));
        btnLogin.setForeground(java.awt.Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this::onLogin);
        c.gridx = 1; c.gridy = 2; c.weightx = 1;
        card.add(btnLogin, c);

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(new Color(0x555555));
        c.gridx = 1; c.gridy = 3; c.weightx = 1;
        card.add(lblStatus, c);

        JPanel centerWrap = new JPanel();
        centerWrap.setBackground(UIStyle.BG);
        centerWrap.add(card);

        root.add(centerWrap, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void onLogin(ActionEvent e) {
        try {
            if (sock == null || sock.isClosed()) {
                sock = new Socket(HOST, PORT);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
                out = new PrintWriter(sock.getOutputStream(), true);
            }
            String id = tfId.getText().trim();
            String pw = new String(tfPw.getPassword());
            Map<String,String> m = new HashMap<>();
            m.put("id", id);
            m.put("pw", pw);
            out.println(Protocol.LOGIN + " " + KV.build(m));

            new Thread(() -> {
                try {
                    sock.setSoTimeout(5000);
                    String line = in.readLine();
                    int sp = line.indexOf(' ');
                    String cmd = sp > 0 ? line.substring(0, sp) : line;
                    String payload = sp > 0 ? line.substring(sp+1) : "";
                    if (Protocol.LOGIN_OK.equals(cmd)) {
                    	// (로그인 OK를 받은 직후)

                    	// 1) 서버 응답에서 내 정보 파싱
                    	Map<String,String> p = KV.parse(payload);   // payload는 LOGIN_OK 뒤의 키=값들
                    	int    myId   = Integer.parseInt(p.get("user_id"));
                    	String myName = p.get("name");
                    	String myRole = p.get("role");

                    	// 2) 대화 목록 창 띄우기 (스트림도 그대로 넘김)
                    	SwingUtilities.invokeLater(() -> {
                    	    TalkListFrame f = new TalkListFrame(myId, myName, myRole, in, out);
                    	    f.setLocationRelativeTo(null);
                    	    f.setVisible(true);
                    	});

                    	// (선택) 현재 로그인 창 닫기
                    	this.dispose();

                    } else {
                        SwingUtilities.invokeLater(() -> {
                            lblStatus.setForeground(Color.RED);
                            lblStatus.setText("로그인 실패");
                        });
                    }
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setForeground(Color.RED);
                        lblStatus.setText("서버 응답 오류: " + ex.getMessage());
                    });
                }
            }, "login-waiter").start();

        } catch (Exception ex) {
            lblStatus.setForeground(Color.RED);
            lblStatus.setText("서버 연결 실패: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TalkAWT().setVisible(true));
    }
}
