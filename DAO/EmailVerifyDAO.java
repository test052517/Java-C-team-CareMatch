package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.EmailVerifyDTO;
import DB.DBConnect;

public class EmailVerifyDAO {

    // 새로운 이메일 인증 요청 생성
    public boolean createVerification(EmailVerifyDTO verification) {
        String sql = "INSERT INTO email_verify (fav_id, animal_id, user_id, verified, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, verification.getVerifyId());
            pstmt.setString(2, verification.getVerificationCode());
            pstmt.setString(3, verification.getUserId());
            pstmt.setBoolean(4, verification.isVerified());
            pstmt.setDate(5, verification.getCreatedAt());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("이메일 인증 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 인증 정보 조회
    public EmailVerifyDTO findVerificationByCode(String code) {
        String sql = "SELECT * FROM email_verify WHERE animal_id = ?";
        EmailVerifyDTO verification = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    verification = new EmailVerifyDTO();
                    verification.setVerifyId(rs.getInt("fav_id"));
                    verification.setVerificationCode(rs.getString("animal_id"));
                    verification.setUserId(rs.getString("user_id"));
                    verification.setVerified(rs.getBoolean("verified"));
                    verification.setCreatedAt(rs.getDate("created_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("인증 코드 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return verification;
    }

    // 인증 코드 상태를 true로 변경
    public boolean setVerified(String code) {
        String sql = "UPDATE email_verify SET verified = TRUE WHERE animal_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("인증 상태 업데이트 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}