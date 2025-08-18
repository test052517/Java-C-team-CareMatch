package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.ShelterDTO;
import DB.DBConnect;

public class ShelterDAO {

    // 새로운 보호소 추가
    public boolean addShelter(ShelterDTO shelter) {
        String sql = "INSERT INTO Shelter (shelter_id, shelter_name, telephone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shelter.getShelterId());
            pstmt.setString(2, shelter.getShelterName());
            pstmt.setString(3, shelter.getTelephone());
            pstmt.setString(4, shelter.getAddress());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("보호소 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 보호소 정보 조회
    public ShelterDTO findShelterById(String shelterId) {
        String sql = "SELECT * FROM Shelter WHERE shelter_id = ?";
        ShelterDTO shelter = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shelterId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    shelter = new ShelterDTO();
                    shelter.setShelterId(rs.getString("shelter_id"));
                    shelter.setShelterName(rs.getString("shelter_name"));
                    shelter.setTelephone(rs.getString("telephone"));
                    shelter.setAddress(rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            System.err.println("보호소 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return shelter;
    }

    // 모든 보호소 목록 조회
    public List<ShelterDTO> findAllShelters() {
        String sql = "SELECT * FROM Shelter";
        List<ShelterDTO> shelters = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ShelterDTO shelter = new ShelterDTO();
                shelter.setShelterId(rs.getString("shelter_id"));
                shelter.setShelterName(rs.getString("shelter_name"));
                shelter.setTelephone(rs.getString("telephone"));
                shelter.setAddress(rs.getString("address"));
                shelters.add(shelter);
            }
        } catch (SQLException e) {
            System.err.println("전체 보호소 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return shelters;
    }

    // 기존 보호소 정보 수정
    public boolean updateShelter(ShelterDTO shelter) {
        String sql = "UPDATE Shelter SET shelter_name = ?, telephone = ?, address = ? WHERE shelter_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shelter.getShelterName());
            pstmt.setString(2, shelter.getTelephone());
            pstmt.setString(3, shelter.getAddress());
            pstmt.setString(4, shelter.getShelterId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("보호소 정보 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //  특정 보호소 삭제
    public boolean deleteShelter(String shelterId) {
        String sql = "DELETE FROM Shelter WHERE shelter_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, shelterId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("보호소 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}