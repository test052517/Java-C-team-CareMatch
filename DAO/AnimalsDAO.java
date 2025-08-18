package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.AnimalsDTO;
import DB.DBConnect;

public class AnimalsDAO {

    // 동물 정보 추가
    public boolean addAnimal(AnimalsDTO animal) {
        String sql = "INSERT INTO Animals (animal_id, age, weight, sex, neutered, happenDate, kind_id, shelter_id, status, image_id, animal_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getAnimalId());
            pstmt.setInt(2, animal.getAge());
            pstmt.setDouble(3, animal.getWeight());
            pstmt.setString(4, animal.getSex());
            pstmt.setString(5, animal.getNeutered());
            pstmt.setDate(6, animal.getHappenDate());
            pstmt.setString(7, animal.getKindId());
            pstmt.setString(8, animal.getShelterId());
            pstmt.setString(9, animal.getStatus());
            pstmt.setString(10, animal.getImageId());
            pstmt.setString(11, animal.getAnimalName());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("동물 정보 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 동물 정보 조회
    public AnimalsDTO findAnimalById(String animalId) {
        String sql = "SELECT * FROM Animals WHERE animal_id = ?";
        AnimalsDTO animal = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animalId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    animal = new AnimalsDTO();
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
                }
            }
        } catch (SQLException e) {
            System.err.println("동물 정보 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return animal;
    }

    // 동물 정보 수정
    public boolean updateAnimal(AnimalsDTO animal) {
        String sql = "UPDATE Animals SET age = ?, weight = ?, sex = ?, neutered = ?, happenDate = ?, kind_id = ?, shelter_id = ?, status = ?, image_id = ?, animal_name = ? WHERE animal_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, animal.getAge());
            pstmt.setDouble(2, animal.getWeight());
            pstmt.setString(3, animal.getSex());
            pstmt.setString(4, animal.getNeutered());
            pstmt.setDate(5, animal.getHappenDate());
            pstmt.setString(6, animal.getKindId());
            pstmt.setString(7, animal.getShelterId());
            pstmt.setString(8, animal.getStatus());
            pstmt.setString(9, animal.getImageId());
            pstmt.setString(10, animal.getAnimalName());
            pstmt.setString(11, animal.getAnimalId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("동물 정보 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 동물 삭제
    public boolean deleteAnimal(String animalId) {
        String sql = "DELETE FROM Animals WHERE animal_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animalId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("동물 정보 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // 모든 동물 목록 조회
    public List<AnimalsDTO> findAllAnimals() {
        String sql = "SELECT * FROM Animals";
        List<AnimalsDTO> animalList = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
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
                animalList.add(animal);
            }
        } catch (SQLException e) {
            System.err.println("전체 동물 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return animalList;
    }
}