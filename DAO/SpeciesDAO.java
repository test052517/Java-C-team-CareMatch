package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DTO.SpeciesDTO;
import DB.DBConnect;

public class SpeciesDAO {

    // 새로운 품종 추가
    public boolean addSpecies(SpeciesDTO species) {
        String sql = "INSERT INTO Species (kind_id, kind_name, type_name, category) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, species.getKindId());
            pstmt.setString(2, species.getKindName());
            pstmt.setString(3, species.getTypeName());
            pstmt.setString(4, species.getCategory());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("품종 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 품종 정보 조회
    public SpeciesDTO findSpeciesById(String kindId) {
        String sql = "SELECT * FROM Species WHERE kind_id = ?";
        SpeciesDTO species = null;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kindId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    species = new SpeciesDTO();
                    species.setKindId(rs.getString("kind_id"));
                    species.setKindName(rs.getString("kind_name"));
                    species.setTypeName(rs.getString("type_name"));
                    species.setCategory(rs.getString("category"));
                }
            }
        } catch (SQLException e) {
            System.err.println("품종 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return species;
    }

    // 모든 품종 목록 조회
    public List<SpeciesDTO> findAllSpecies() {
        String sql = "SELECT * FROM Species ORDER BY kind_name ASC";
        List<SpeciesDTO> speciesList = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                SpeciesDTO species = new SpeciesDTO();
                species.setKindId(rs.getString("kind_id"));
                species.setKindName(rs.getString("kind_name"));
                species.setTypeName(rs.getString("type_name"));
                species.setCategory(rs.getString("category"));
                speciesList.add(species);
            }
        } catch (SQLException e) {
            System.err.println("전체 품종 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return speciesList;
    }

    // 품종 정보 수정
    public boolean updateSpecies(SpeciesDTO species) {
        String sql = "UPDATE Species SET kind_name = ?, type_name = ?, category = ? WHERE kind_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, species.getKindName());
            pstmt.setString(2, species.getTypeName());
            pstmt.setString(3, species.getCategory());
            pstmt.setString(4, species.getKindId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("품종 정보 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 특정 품종 삭제
    public boolean deleteSpecies(String kindId) {
        String sql = "DELETE FROM Species WHERE kind_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kindId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("품종 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}