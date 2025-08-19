package Talk.server.dao;

import java.sql.*;
import java.util.*;

public class ChatDAO {

    public static class UserRow { public int userId; public String name; }

    public List<UserRow> listPartners(int myId, String myRole) throws Exception {
        List<UserRow> list = new ArrayList<>();
        try (Connection c = Talk.common.DB.get()) {
            String sql;
            boolean bindId = false;
            if ("ADMIN".equalsIgnoreCase(myRole)) {
                sql = "SELECT user_id,name FROM users WHERE role='USER' AND user_id<>? ORDER BY user_id";
                bindId = true;
            } else {
                sql = "SELECT user_id,name FROM users WHERE role='ADMIN' ORDER BY user_id";
            }
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                if (bindId) ps.setInt(1, myId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        UserRow u = new UserRow();
                        u.userId = rs.getInt(1);
                        u.name   = rs.getString(2);
                        list.add(u);
                    }
                }
            }
        }
        return list;
    }

    public String getUserName(int uid) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement("SELECT name FROM users WHERE user_id=?")) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : ("user"+uid);
            }
        }
    }

    public long ensureDmRoom(int a, int b) throws Exception {
        if (a==b) throw new IllegalArgumentException("self DM not allowed");
        String find = "SELECT r.room_id FROM chatting_room r " +
                "JOIN chatting_room_member x ON r.room_id=x.room_id AND x.user_id=? " +
                "JOIN chatting_room_member y ON r.room_id=y.room_id AND y.user_id=? " +
                "WHERE r.type='DM' LIMIT 1";
        try (Connection c = Talk.common.DB.get()) {
            try (PreparedStatement ps = c.prepareStatement(find)) {
                ps.setInt(1, a); ps.setInt(2, b);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                }
            }
            // create
            long roomId;
            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO chatting_room(type) VALUES('DM')", Statement.RETURN_GENERATED_KEYS)) {
                ins.executeUpdate();
                try (ResultSet k = ins.getGeneratedKeys()) { k.next(); roomId = k.getLong(1); }
            }
            try (PreparedStatement m = c.prepareStatement(
                    "INSERT INTO chatting_room_member(room_id,user_id,last_read_message_id) VALUES(?,?,NULL)")) {
                m.setLong(1, roomId); m.setInt(2, a); m.executeUpdate();
                m.setLong(1, roomId); m.setInt(2, b); m.executeUpdate();
            }
            try (PreparedStatement msg = c.prepareStatement(
                    "INSERT INTO chatting_message(room_id,sender_id,content,content_type) VALUES(?,?,?,'TEXT')")) {
                msg.setLong(1, roomId);
                msg.setInt(2, a);
                msg.setString(3, "안녕하세요! 테스트 DM입니다.");
                msg.executeUpdate();
            }
            return roomId;
        }
    }

    public static class MsgRow {
        public long messageId; public long roomId; public int senderId;
        public String content; public String contentType; public java.sql.Timestamp createdAt;
        public String filePath; public String originalName; public Integer sizeBytes;
    }

    public List<MsgRow> fetchHistory(long roomId, int limit) throws Exception {
        String sql = "SELECT m.message_id,m.room_id,m.sender_id,m.content,m.content_type,m.created_at," +
                "i.file_path,i.original_name,i.size_bytes " +
                "FROM chatting_message m LEFT JOIN chat_image i ON m.message_id=i.message_id " +
                "WHERE m.room_id=? ORDER BY m.message_id ASC LIMIT ?";
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<MsgRow> out = new ArrayList<>();
                while (rs.next()) {
                    MsgRow r = new MsgRow();
                    r.messageId = rs.getLong(1);
                    r.roomId = rs.getLong(2);
                    r.senderId = rs.getInt(3);
                    r.content = rs.getString(4);
                    r.contentType = rs.getString(5);
                    r.createdAt = rs.getTimestamp(6);
                    r.filePath = rs.getString(7);
                    r.originalName = rs.getString(8);
                    Object o = rs.getObject(9);
                    if (o instanceof Number) r.sizeBytes = ((Number)o).intValue(); else r.sizeBytes = null;
                    out.add(r);
                }
                return out;
            }
        }
    }

    public long getLastMessageId(long roomId) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement("SELECT COALESCE(MAX(message_id),0) FROM chatting_message WHERE room_id=?")) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getLong(1); }
        }
    }

    public List<Integer> getMembers(long roomId) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement("SELECT user_id FROM chatting_room_member WHERE room_id=?")) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> ids = new ArrayList<>();
                while (rs.next()) ids.add(rs.getInt(1));
                return ids;
            }
        }
    }

    public long insertText(long roomId, int senderId, String text) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO chatting_message(room_id,sender_id,content,content_type) VALUES(?,?,?,'TEXT')",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, roomId);
            ps.setInt(2, senderId);
            ps.setString(3, text);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public long insertImage(long roomId, int senderId, String path, String name, int size) throws Exception {
        try (Connection c = Talk.common.DB.get()) {
            long mid;
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO chatting_message(room_id,sender_id,content,content_type) VALUES(?,?,NULL,'IMAGE')",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, roomId);
                ps.setInt(2, senderId);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); mid = rs.getLong(1); }
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO chat_image(message_id,file_path,original_name,size_bytes) VALUES(?,?,?,?)")) {
                ps.setLong(1, mid);
                ps.setString(2, path);
                ps.setString(3, name);
                ps.setInt(4, size);
                ps.executeUpdate();
            }
            return mid;
        }
    }

    public void updateLastRead(long roomId, int userId, long lastId) throws Exception {
        try (Connection c = Talk.common.DB.get();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE chatting_room_member SET last_read_message_id=? WHERE room_id=? AND user_id=?")) {
            ps.setLong(1, lastId);
            ps.setLong(2, roomId);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }
}
