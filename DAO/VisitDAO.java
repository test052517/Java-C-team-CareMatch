package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.VisitDTO;
import DB.DBConnect;


public class VisitDAO {

    // 새로운 방문 예약을 추가
    public boolean addVisit(VisitDTO visit) {
        String sql = "INSERT INTO Visit (visit_id, purpose, status, visit_date, submitted_date, user_id, animal_id, application_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, visit.getVisitId());
            pstmt.setString(2, visit.getPurpose());
            pstmt.setString(3, visit.getStatus());
            pstmt.setDate(4, visit.getVisitDate());
            pstmt.setDate(5, visit.getSubmittedDate());
            pstmt.setString(6, visit.getUserId());
            pstmt.setString(7, visit.getAnimalId());
            pstmt.setString(8, visit.getApplicationId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("방문 예약 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 방문 예약 정보 조회
    public VisitDTO findVisitById(String visitId) {
        String sql = "SELECT * FROM Visit WHERE visit_id = ?";
        VisitDTO visit = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, visitId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    visit = new VisitDTO();
                    visit.setVisitId(rs.getString("visit_id"));
                    visit.setPurpose(rs.getString("purpose"));
                    visit.setStatus(rs.getString("status"));
                    visit.setVisitDate(rs.getDate("visit_date"));
                    visit.setSubmittedDate(rs.getDate("submitted_date"));
                    visit.setUserId(rs.getString("user_id"));
                    visit.setAnimalId(rs.getString("animal_id"));
                    visit.setApplicationId(rs.getString("application_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("방문 예약 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return visit;
    }

    // 특정 사용자의 모든 방문 예약 목록 조회
    public List<VisitDTO> findVisitsByUserId(String userId) {
        String sql = "SELECT * FROM Visit WHERE user_id = ? ORDER BY visit_date DESC";
        List<VisitDTO> visits = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    VisitDTO visit = new VisitDTO();
                    visit.setVisitId(rs.getString("visit_id"));
                    visit.setPurpose(rs.getString("purpose"));
                    visit.setStatus(rs.getString("status"));
                    visit.setVisitDate(rs.getDate("visit_date"));
                    visit.setSubmittedDate(rs.getDate("submitted_date"));
                    visit.setUserId(rs.getString("user_id"));
                    visit.setAnimalId(rs.getString("animal_id"));
                    visit.setApplicationId(rs.getString("application_id"));
                    visits.add(visit);
                }
            }
        } catch (SQLException e) {
            System.err.println("사용자 ID로 방문 예약 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return visits;
    }
    
    // 기존 방문 예약 상태 수정
    public boolean updateVisitStatus(String visitId, String newStatus) {
        String sql = "UPDATE Visit SET status = ? WHERE visit_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, visitId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("방문 예약 상태 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 방문 예약 삭제
    public boolean deleteVisit(String visitId) {
        String sql = "DELETE FROM Visit WHERE visit_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, visitId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("방문 예약 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}