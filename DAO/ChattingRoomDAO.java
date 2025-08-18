package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.ChattingRoomDTO;
import DB.DBConnect;

public class ChattingRoomDAO {

    // 새로운 채팅방 생성
    public boolean createRoom(ChattingRoomDTO room) {
        String sql = "INSERT INTO ChattingRoom (room_id, user_id, created_date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, room.getRoomId());
            pstmt.setString(2, room.getUserId());
            pstmt.setDate(3, room.getCreatedDate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("채팅방 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 채팅방 정보 조회
    public ChattingRoomDTO findRoomById(int roomId) {
        String sql = "SELECT * FROM ChattingRoom WHERE room_id = ?";
        ChattingRoomDTO room = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    room = new ChattingRoomDTO();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setUserId(rs.getString("user_id"));
                    room.setCreatedDate(rs.getDate("created_date"));
                }
            }
        } catch (SQLException e) {
            System.err.println("채팅방 ID로 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return room;
    }

    // 특정 사용자가 참여하고 있는 모든 채팅방 목록 조회
    public List<ChattingRoomDTO> findRoomsByUserId(String userId) {
        String sql = "SELECT * FROM ChattingRoom WHERE user_id = ? ORDER BY created_date DESC";
        List<ChattingRoomDTO> rooms = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChattingRoomDTO room = new ChattingRoomDTO();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setUserId(rs.getString("user_id"));
                    room.setCreatedDate(rs.getDate("created_date"));
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            System.err.println("사용자 ID로 채팅방 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    // 채팅방 ID로 특정 채팅방 삭제
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM ChattingRoom WHERE room_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("채팅방 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}