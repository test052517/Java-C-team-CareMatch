package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.AdoptionDTO;
import DB.DBConnect;

public class AdoptionDAO {

    // 새로운 입양 신청 추가
    public boolean addAdoption(AdoptionDTO adoption) {
        String sql = "INSERT INTO Adoption (application_id, status, visit_date, submitted_date, user_id, animal_id, animal_reason) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, adoption.getApplicationId());
            pstmt.setString(2, adoption.getStatus());
            pstmt.setDate(3, adoption.getVisitDate());
            pstmt.setDate(4, adoption.getSubmittedDate());
            pstmt.setString(5, adoption.getUserId());
            pstmt.setString(6, adoption.getAnimalId());
            pstmt.setString(7, adoption.getAnimalReason());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("입양 신청 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //  특정 입양 신청 정보 조회
    public AdoptionDTO findAdoptionById(String applicationId) {
        String sql = "SELECT * FROM Adoption WHERE application_id = ?";
        AdoptionDTO adoption = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, applicationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    adoption = new AdoptionDTO();
                    adoption.setApplicationId(rs.getString("application_id"));
                    adoption.setStatus(rs.getString("status"));
                    adoption.setVisitDate(rs.getDate("visit_date"));
                    adoption.setSubmittedDate(rs.getDate("submitted_date"));
                    adoption.setUserId(rs.getString("user_id"));
                    adoption.setAnimalId(rs.getString("animal_id"));
                    adoption.setAnimalReason(rs.getString("animal_reason"));
                }
            }
        } catch (SQLException e) {
            System.err.println("입양 신청 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return adoption;
    }

    // 입양 신청의 상태 수정
    public boolean updateAdoptionStatus(String applicationId, String newStatus) {
        String sql = "UPDATE Adoption SET status = ? WHERE application_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setString(2, applicationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("입양 상태 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //  특정 입양 신청 삭제
    public boolean deleteAdoption(String applicationId) {
        String sql = "DELETE FROM Adoption WHERE application_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, applicationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("입양 신청 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}