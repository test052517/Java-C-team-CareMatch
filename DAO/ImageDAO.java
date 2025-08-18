package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.ImageDTO;
import DB.DBConnect;

public class ImageDAO {

    // 이미지 추가
    public boolean addImage(ImageDTO image) {
        String sql = "INSERT INTO Image (image_id, image_url, image_data) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, image.getImageId());
            pstmt.setString(2, image.getImageUrl());
            pstmt.setBytes(3, image.getImageData()); // byte[] 데이터는 setBytes()를 사용

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("이미지 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 이미지 정보 조회
    public ImageDTO findImageById(String imageId) {
        String sql = "SELECT * FROM Image WHERE image_id = ?";
        ImageDTO image = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imageId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    image = new ImageDTO();
                    image.setImageId(rs.getString("image_id"));
                    image.setImageUrl(rs.getString("image_url"));
                    image.setImageData(rs.getBytes("image_data"));
                }
            }
        } catch (SQLException e) {
            System.err.println("이미지 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    // 특정 이미지 삭제
    public boolean deleteImage(String imageId) {
        String sql = "DELETE FROM Image WHERE image_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, imageId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("이미지 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}