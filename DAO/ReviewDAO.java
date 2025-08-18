package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.ReviewDTO;
import DB.DBConnect;

public class ReviewDAO {

    // 새로운 리뷰 추가
    public boolean addReview(ReviewDTO review) {
        String sql = "INSERT INTO Review (review_id, created_date, title, content, image_id, `like`, user_id, animal_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, review.getReviewId());
            pstmt.setDate(2, review.getCreatedDate());
            pstmt.setString(3, review.getTitle());
            pstmt.setString(4, review.getContent());
            pstmt.setString(5, review.getImageId());
            pstmt.setInt(6, review.getLikeCount());
            pstmt.setString(7, review.getUserId());
            pstmt.setString(8, review.getAnimalId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("리뷰 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 리뷰 정보 조회
    public ReviewDTO findReviewById(String reviewId) {
        String sql = "SELECT * FROM Review WHERE review_id = ?";
        ReviewDTO review = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, reviewId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    review = new ReviewDTO();
                    review.setReviewId(rs.getString("review_id"));
                    review.setCreatedDate(rs.getDate("created_date"));
                    review.setTitle(rs.getString("title"));
                    review.setContent(rs.getString("content"));
                    review.setImageId(rs.getString("image_id"));
                    review.setLikeCount(rs.getInt("like"));
                    review.setUserId(rs.getString("user_id"));
                    review.setAnimalId(rs.getString("animal_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("리뷰 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return review;
    }

    // 모든 리뷰 목록 최신순으로 조회
    public List<ReviewDTO> findAllReviews() {
        String sql = "SELECT * FROM Review ORDER BY created_date DESC";
        List<ReviewDTO> reviews = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setReviewId(rs.getString("review_id"));
                review.setCreatedDate(rs.getDate("created_date"));
                review.setTitle(rs.getString("title"));
                review.setContent(rs.getString("content"));
                review.setImageId(rs.getString("image_id"));
                review.setLikeCount(rs.getInt("like"));
                review.setUserId(rs.getString("user_id"));
                review.setAnimalId(rs.getString("animal_id"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.err.println("전체 리뷰 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    // 특정 리뷰 삭제
    public boolean deleteReview(String reviewId) {
        String sql = "DELETE FROM Review WHERE review_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("리뷰 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}