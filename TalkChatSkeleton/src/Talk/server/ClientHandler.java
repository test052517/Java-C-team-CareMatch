package Talk.server;

import Talk.common.KV;
import Talk.common.Protocol;
import Talk.server.dao.ChatDAO;
import Talk.server.dao.UserDAO;

import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Base64;

public class ClientHandler extends Thread {
    private final Socket sock;
    private BufferedReader in;
    private PrintWriter out;

    public Integer userId = null;
    public String name = null;
    public String role = null;

    public ClientHandler(Socket s){ this.sock = s; }

    @Override public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"), true);
            String line;
            while ((line = in.readLine()) != null) {
                int sp = line.indexOf(' ');
                String cmd = sp>0?line.substring(0,sp):line;
                String payload = sp>0?line.substring(sp+1):"";
                try {
                    if (Protocol.LOGIN.equals(cmd))      handleLogin(payload);
                    else if (Protocol.LIST_ROOMS.equals(cmd)) handleListRooms();
                    else if (Protocol.ENTER.equals(cmd)) handleEnter(payload);
                    else if (Protocol.SAY.equals(cmd))   handleSay(payload);
                    else if (Protocol.SAY_IMAGE.equals(cmd)) handleSayImage(payload);
                    else if (Protocol.READ.equals(cmd))  handleRead(payload);
                    else out.println(Protocol.ERROR+" msg=unknown");
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println(Protocol.ERROR+" msg="+e.getMessage());
                }
            }
        } catch (Exception ignore) {
        } finally {
            if (userId != null) {
                TalkServer.SESSIONS.remove(userId);
                System.out.println("[LOGOUT] user="+userId);
            }
            try { sock.close(); } catch(Exception ignore2){}
        }
    }

    private void ensureLogin() { if (userId==null) throw new RuntimeException("not_logged_in"); }

    private void handleLogin(String payload) throws Exception {
        Map<String,String> p = KV.parse(payload);
        UserDAO dao = new UserDAO();
        UserDAO.User u = dao.login(p.get("id"), p.get("pw"));
        if (u == null) { out.println(Protocol.LOGIN_FAIL+" msg=bad_credential"); return; }
        this.userId = u.userId; this.name = u.name; this.role = u.role;
        TalkServer.SESSIONS.put(userId, this);
        Map<String,String> r = new LinkedHashMap<>();
        r.put("user_id", String.valueOf(u.userId));
        r.put("name", u.name);
        r.put("role", u.role);
        System.out.println("[LOGIN] user="+u.userId+" ("+u.name+","+u.role+") online="+TalkServer.SESSIONS.size());
        out.println(Protocol.LOGIN_OK+" "+KV.build(r));
    }

    private void handleListRooms() throws Exception {
        ensureLogin();
        ChatDAO dao = new ChatDAO();
        List<ChatDAO.UserRow> partners = dao.listPartners(userId, role);
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<partners.size();i++) {
            if (i>0) sb.append('|');
            sb.append(partners.get(i).userId).append(':').append(partners.get(i).name);
        }
        Map<String,String> m = new LinkedHashMap<>();
        m.put("rooms", sb.toString());
        System.out.println("[ROOMS] user="+userId+" partners="+partners.size());
        out.println(Protocol.ROOMS+" "+KV.build(m));
    }

    private void handleEnter(String payload) throws Exception {
        ensureLogin();
        Map<String,String> p = KV.parse(payload);
        int partnerId = Integer.parseInt(p.get("partner_id"));
        ChatDAO dao = new ChatDAO();
        long roomId = dao.ensureDmRoom(userId, partnerId);
        String partnerName = dao.getUserName(partnerId);

        Map<String,String> begin = new LinkedHashMap<>();
        begin.put("room_id", String.valueOf(roomId));
        begin.put("partner", partnerName);
        out.println(Protocol.HISTORY_BEGIN+" "+KV.build(begin));

        List<ChatDAO.MsgRow> hist = dao.fetchHistory(roomId, 2000);
        for (ChatDAO.MsgRow m : hist) {
            long ts = (m.createdAt != null) ? m.createdAt.getTime() : System.currentTimeMillis();
            if ("IMAGE".equals(m.contentType)) {
                byte[] bytes = Files.readAllBytes(Path.of(m.filePath));
                Map<String,String> pl = new LinkedHashMap<>();
                pl.put("room_id", String.valueOf(roomId));
                pl.put("message_id", String.valueOf(m.messageId));
                pl.put("sender_id", String.valueOf(m.senderId));
                pl.put("data", Base64.getEncoder().encodeToString(bytes));
                pl.put("name", m.originalName==null?"image":m.originalName);
                pl.put("ts", String.valueOf(ts));
                pl.put("sender_name", dao.getUserName(m.senderId));
                out.println(Protocol.MSG_IMAGE+" "+KV.build(pl));
            } else {
                Map<String,String> pl = new LinkedHashMap<>();
                pl.put("room_id", String.valueOf(roomId));
                pl.put("message_id", String.valueOf(m.messageId));
                pl.put("sender_id", String.valueOf(m.senderId));
                pl.put("text", m.content==null?"":m.content);
                pl.put("ts", String.valueOf(ts));
                pl.put("sender_name", dao.getUserName(m.senderId));
                out.println(Protocol.MSG+" "+KV.build(pl));
            }
        }
        dao.updateLastRead(roomId, userId, dao.getLastMessageId(roomId));
        Map<String,String> end = new LinkedHashMap<>();
        end.put("room_id", String.valueOf(roomId));
        out.println(Protocol.HISTORY_END+" "+KV.build(end));
        System.out.println("[ENTER] user="+userId+" room="+roomId+" partner="+partnerName+" hist="+hist.size());
    }

    
    private void broadcast(long roomId, String line) throws Exception {
        ChatDAO dao = new ChatDAO();
        java.util.LinkedHashSet<Integer> targets = new java.util.LinkedHashSet<>(dao.getMembers(roomId));
        boolean selfSent = false;
        // 1) 표준 대상 전송
        for (Integer uid : targets) {
            ClientHandler ch = TalkServer.SESSIONS.get(uid);
            if (ch != null) {
                ch.out.println(line);
                if (this.userId != null && this.userId.equals(uid)) selfSent = true;
            }
        }
        // 2) 방 멤버 조회가 한 명만 나오는 등 비정상일 때, DB로 직접 상대를 조회하여 보강 전송
        if (targets.size() <= 1) {
            try (java.sql.Connection c = Talk.common.DB.get();
                 java.sql.PreparedStatement ps = c.prepareStatement(
                     "SELECT user_id FROM chatting_room_member WHERE room_id=? AND user_id<>? LIMIT 1")) {
                ps.setLong(1, roomId);
                ps.setInt(2, (this.userId==null? -1 : this.userId));
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int partner = rs.getInt(1);
                        if (!targets.contains(partner)) {
                            ClientHandler ch2 = TalkServer.SESSIONS.get(partner);
                            if (ch2 != null) {
                                ch2.out.println(line);
                                targets.add(partner);
                            }
                        }
                    }
                }
            } catch (Exception ignore) {}
        }
        // 3) 최소한 본인에게는 1회 보장
        if (!selfSent) {
            out.println(line);
        }
        System.out.println("[BROADCAST] room=" + roomId + " -> " + targets);
    }


    private void handleSay(String payload) throws Exception {
        ensureLogin();
        Map<String,String> p = KV.parse(payload);
        long roomId = Long.parseLong(p.get("room_id"));
        String text = p.get("text");
        String clientTag = p.get("client_tag");
        ChatDAO dao = new ChatDAO();
        long mid = dao.insertText(roomId, userId, text);
        long ts = System.currentTimeMillis();
        String senderName = dao.getUserName(userId);

        Map<String,String> pl = new LinkedHashMap<>();
        pl.put("room_id", String.valueOf(roomId));
        pl.put("message_id", String.valueOf(mid));
        pl.put("sender_id", String.valueOf(userId));
        pl.put("text", text);
        pl.put("ts", String.valueOf(ts));
        if (clientTag!=null) pl.put("client_tag", clientTag);
        pl.put("sender_name", senderName);
        String line = Protocol.MSG+" "+KV.build(pl);

        broadcast(roomId, line);
        dao.updateLastRead(roomId, userId, mid);
        System.out.println("[SAY] user="+userId+" room="+roomId);
        System.out.println("[BROADCAST] room="+roomId+" -> " + TalkServer.SESSIONS.keySet());
    }

    private void handleSayImage(String payload) throws Exception {
        ensureLogin();
        Map<String,String> p = KV.parse(payload);
        long roomId = Long.parseLong(p.get("room_id"));
        String name = p.get("name");
        String b64 = p.get("data");
        String clientTag = p.get("client_tag");
        byte[] bytes = Base64.getDecoder().decode(b64);
        Path dir = Path.of("uploads"); Files.createDirectories(dir);
        Path path = dir.resolve(System.currentTimeMillis()+"_"+Math.abs(Arrays.hashCode(bytes))+"_"+name);
        Files.write(path, bytes);
        ChatDAO dao = new ChatDAO();
        long mid = dao.insertImage(roomId, userId, path.toString(), name, bytes.length);
        long ts = System.currentTimeMillis();
        String senderName = dao.getUserName(userId);

        Map<String,String> pl = new LinkedHashMap<>();
        pl.put("room_id", String.valueOf(roomId));
        pl.put("message_id", String.valueOf(mid));
        pl.put("sender_id", String.valueOf(userId));
        pl.put("data", Base64.getEncoder().encodeToString(bytes));
        pl.put("name", name);
        pl.put("ts", String.valueOf(ts));
        if (clientTag!=null) pl.put("client_tag", clientTag);
        pl.put("sender_name", senderName);
        String line = Protocol.MSG_IMAGE+" "+KV.build(pl);

        broadcast(roomId, line);
        dao.updateLastRead(roomId, userId, mid);
        System.out.println("[IMAGE] user="+userId+" room="+roomId+" bytes="+bytes.length);
    }

    private void handleRead(String payload) throws Exception {
        ensureLogin();
        Map<String,String> p = KV.parse(payload);
        long roomId = Long.parseLong(p.get("room_id"));
        long messageId = Long.parseLong(p.get("message_id"));
        ChatDAO dao = new ChatDAO();
        dao.updateLastRead(roomId, userId, messageId);

        Map<String,String> pl = new LinkedHashMap<>();
        pl.put("room_id", String.valueOf(roomId));
        pl.put("message_id", String.valueOf(messageId));
        String line = Protocol.READ_ACK+" "+KV.build(pl);
        broadcast(roomId, line);
    }
}
