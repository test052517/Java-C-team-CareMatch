package DTO;

import java.sql.Date;

/**
 * ChattingRoom 테이블의 데이터를 담는 모델 클래스 (DTO)
 */
public class ChattingRoomDTO {
    private int roomId;
    private String userId;
    private Date createdDate;

    // 기본 생성자
    public ChattingRoomDTO() {}

    // 모든 필드를 초기화하는 생성자
    public ChattingRoomDTO(int roomId, String userId, Date createdDate) {
        this.roomId = roomId;
        this.userId = userId;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    // toString() 메서드
    @Override
    public String toString() {
        return "ChattingRoomDTO{" +
                "roomId=" + roomId +
                ", userId='" + userId + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}