package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.CommentDTO;
import DB.DBConnect;

public class CommentDAO {

    // 새로운 댓글 추가
    public boolean addComment(CommentDTO comment) {
        String sql = "INSERT INTO Comment (comment_id, created_date, content, `like`, review_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, comment.getCommentId());
            pstmt.setDate(2, comment.getCreatedDate());
            pstmt.setString(3, comment.getContent());
            pstmt.setInt(4, comment.getLikeCount());
            pstmt.setString(5, comment.getReviewId());
            pstmt.setString(6, comment.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("댓글 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 리뷰(review_id)에 달린 모든 댓글 시간순으로 조회
    public List<CommentDTO> findCommentsByReviewId(String reviewId) {
        String sql = "SELECT * FROM Comment WHERE review_id = ? ORDER BY created_date ASC";
        List<CommentDTO> comments = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, reviewId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CommentDTO comment = new CommentDTO();
                    comment.setCommentId(rs.getString("comment_id"));
                    comment.setCreatedDate(rs.getDate("created_date"));
                    comment.setContent(rs.getString("content"));
                    comment.setLikeCount(rs.getInt("like")); // DB에서는 'like' 컬럼을 조회
                    comment.setReviewId(rs.getString("review_id"));
                    comment.setUserId(rs.getString("user_id"));
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            System.err.println("리뷰 ID로 댓글 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return comments;
    }

    // 댓글 내용 수정
    public boolean updateCommentContent(String commentId, String newContent) {
        String sql = "UPDATE Comment SET content = ? WHERE comment_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newContent);
            pstmt.setString(2, commentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("댓글 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 댓글 삭제
    public boolean deleteComment(String commentId) {
        String sql = "DELETE FROM Comment WHERE comment_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, commentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("댓글 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}