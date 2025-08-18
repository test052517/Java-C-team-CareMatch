package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import DTO.AnswerDTO;
import DB.DBConnect;

public class AnswerDAO {

    // 문의 답변 추가
    public boolean addAnswer(AnswerDTO answer) {
        String sql = "INSERT INTO Answer (answer_id, title, content, created_date, inquiry_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, answer.getAnswerId());
            pstmt.setString(2, answer.getTitle());
            pstmt.setString(3, answer.getContent());
            pstmt.setTimestamp(4, answer.getCreatedDate());
            pstmt.setString(5, answer.getInquiryId());
            pstmt.setString(6, answer.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("답변 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 문의 ID로 특정 답변 조회
    public AnswerDTO findAnswerByInquiryId(String inquiryId) {
        String sql = "SELECT * FROM Answer WHERE inquiry_id = ?";
        AnswerDTO answer = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inquiryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    answer = new AnswerDTO();
                    answer.setAnswerId(rs.getString("answer_id"));
                    answer.setTitle(rs.getString("title"));
                    answer.setContent(rs.getString("content"));
                    answer.setCreatedDate(rs.getTimestamp("created_date"));
                    answer.setInquiryId(rs.getString("inquiry_id"));
                    answer.setUserId(rs.getString("user_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("문의 ID로 답변 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return answer;
    }

    // 답변 내용 수정
    public boolean updateAnswer(AnswerDTO answer) {
        String sql = "UPDATE Answer SET title = ?, content = ? WHERE answer_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, answer.getTitle());
            pstmt.setString(2, answer.getContent());
            pstmt.setString(3, answer.getAnswerId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("답변 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 답변 삭제
    public boolean deleteAnswer(String answerId) {
        String sql = "DELETE FROM Answer WHERE answer_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, answerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("답변 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}