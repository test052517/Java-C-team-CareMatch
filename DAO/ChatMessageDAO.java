package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.ChatMessageDTO;
import DB.DBConnect;

public class ChatMessageDAO {

    // 새로운 채팅 메시지 추가
    public boolean addMessage(ChatMessageDTO msg) {
        String sql = "INSERT INTO ChatMessage (message_id, room_id, user_id, message, sent_date, status, image_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, msg.getMessageId());
            pstmt.setInt(2, msg.getRoomId());
            pstmt.setString(3, msg.getUserId());
            pstmt.setString(4, msg.getMessage());
            pstmt.setDate(5, msg.getSentDate());
            pstmt.setBoolean(6, msg.isStatus());
            pstmt.setString(7, msg.getImageId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("채팅 메시지 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 채팅방의 모든 메시지를 시간순으로 조회
    public List<ChatMessageDTO> findMessagesByRoomId(int roomId) {
        String sql = "SELECT * FROM ChatMessage WHERE room_id = ? ORDER BY sent_date ASC";
        List<ChatMessageDTO> messages = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChatMessageDTO msg = new ChatMessageDTO();
                    msg.setMessageId(rs.getInt("message_id"));
                    msg.setRoomId(rs.getInt("room_id"));
                    msg.setUserId(rs.getString("user_id"));
                    msg.setMessage(rs.getString("message"));
                    msg.setSentDate(rs.getDate("sent_date"));
                    msg.setStatus(rs.getBoolean("status"));
                    msg.setImageId(rs.getString("image_id"));
                    messages.add(msg);
                }
            }
        } catch (SQLException e) {
            System.err.println("채팅방 메시지 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }

    // 특정 메시지 상태 수정
    public boolean updateMessageStatus(int messageId, boolean newStatus) {
        String sql = "UPDATE ChatMessage SET status = ? WHERE message_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, newStatus);
            pstmt.setInt(2, messageId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("메시지 상태 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}