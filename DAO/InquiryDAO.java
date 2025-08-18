package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.InquiryDTO;
import DB.DBConnect;

public class InquiryDAO {

    // 문의 추가
    public boolean addInquiry(InquiryDTO inquiry) {
        String sql = "INSERT INTO Inquiry (inquiry_id, title, content, created_date, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inquiry.getInquiryId());
            pstmt.setString(2, inquiry.getTitle());
            pstmt.setString(3, inquiry.getContent());
            pstmt.setDate(4, inquiry.getCreatedDate());
            pstmt.setString(5, inquiry.getStatus());
            pstmt.setString(6, inquiry.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("문의 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //특정 문의 정보 조회
    public InquiryDTO findInquiryById(String inquiryId) {
        String sql = "SELECT * FROM Inquiry WHERE inquiry_id = ?";
        InquiryDTO inquiry = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inquiryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    inquiry = new InquiryDTO();
                    inquiry.setInquiryId(rs.getString("inquiry_id"));
                    inquiry.setTitle(rs.getString("title"));
                    inquiry.setContent(rs.getString("content"));
                    inquiry.setCreatedDate(rs.getDate("created_date"));
                    inquiry.setStatus(rs.getString("status"));
                    inquiry.setUserId(rs.getString("user_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("문의 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return inquiry;
    }

    // 특정 사용자가 작성한 모든 문의 목록 조회
    public List<InquiryDTO> findInquiriesByUserId(String userId) {
        String sql = "SELECT * FROM Inquiry WHERE user_id = ? ORDER BY created_date DESC";
        List<InquiryDTO> inquiries = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InquiryDTO inquiry = new InquiryDTO();
                    inquiry.setInquiryId(rs.getString("inquiry_id"));
                    inquiry.setTitle(rs.getString("title"));
                    inquiry.setContent(rs.getString("content"));
                    inquiry.setCreatedDate(rs.getDate("created_date"));
                    inquiry.setStatus(rs.getString("status"));
                    inquiry.setUserId(rs.getString("user_id"));
                    inquiries.add(inquiry);
                }
            }
        } catch (SQLException e) {
            System.err.println("사용자 ID로 문의 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return inquiries;
    }
    
    // 기존 문의 상태 수정
    public boolean updateInquiryStatus(String inquiryId, String newStatus) {
        String sql = "UPDATE Inquiry SET status = ? WHERE inquiry_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, inquiryId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("문의 상태 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}