package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.AnimalsDTO;
import DB.DBConnect;

public class FavAnimalDAO {

    // 관심 동물 추가
    public boolean addFavorite(String userId, String animalId) {
        String sql = "INSERT INTO fav_animal (user_id, animal_id) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, animalId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("관심 동물 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 등록한 모든 관심 동물의 상세 정보 목록 조회
    public List<AnimalsDTO> findFavoritesByUserId(String userId) {
        String sql = "SELECT a.* FROM Animals a JOIN fav_animal f ON a.animal_id = f.animal_id WHERE f.user_id = ?";
        List<AnimalsDTO> favoriteAnimals = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AnimalsDTO animal = new AnimalsDTO();
                    animal.setAnimalId(rs.getString("animal_id"));
                    animal.setAge(rs.getInt("age"));
                    animal.setWeight(rs.getDouble("weight"));
                    animal.setSex(rs.getString("sex"));
                    animal.setNeutered(rs.getString("neutered"));
                    animal.setHappenDate(rs.getDate("happenDate"));
                    animal.setKindId(rs.getString("kind_id"));
                    animal.setShelterId(rs.getString("shelter_id"));
                    animal.setStatus(rs.getString("status"));
                    animal.setImageId(rs.getString("image_id"));
                    animal.setAnimalName(rs.getString("animal_name"));
                    favoriteAnimals.add(animal);
                }
            }
        } catch (SQLException e) {
            System.err.println("관심 동물 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return favoriteAnimals;
    }

    // 관심 동물 삭제
    public boolean removeFavorite(String userId, String animalId) {
        String sql = "DELETE FROM fav_animal WHERE user_id = ? AND animal_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, animalId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("관심 동물 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}