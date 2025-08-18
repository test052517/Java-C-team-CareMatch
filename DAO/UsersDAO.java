package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.UsersDTO;
import DB.DBConnect;

public class UsersDAO {

	// 새로운 사용자 추가
    public boolean addUser(UsersDTO user) {
        String sql = "INSERT INTO Users (user_id, role, password, phone, email, user_createDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getRole());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getUserCreateDate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("사용자 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 사용자 정보 조회
    public UsersDTO findUserById(String userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        UsersDTO user = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new UsersDTO();
                    user.setUserId(rs.getString("user_id"));
                    user.setRole(rs.getString("role"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setEmail(rs.getString("email"));
                    user.setUserCreateDate(rs.getString("user_createDate"));
                }
            }
        } catch (SQLException e) {
            System.err.println("사용자 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    // 특정 사용자 정보 수정
    public boolean updateUser(UsersDTO user) {
        String sql = "UPDATE Users SET role = ?, password = ?, phone = ?, email = ? WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getRole());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("사용자 정보 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 사용자 삭제
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("사용자 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}