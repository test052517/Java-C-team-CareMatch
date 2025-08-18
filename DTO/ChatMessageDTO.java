package DTO;

import java.sql.Date;

public class ChatMessageDTO {
    private int messageId;
    private int roomId;
    private String userId;
    private String message;
    private Date sentDate;
    private boolean status;
    private String imageId;

    public ChatMessageDTO() {}

    public ChatMessageDTO(int messageId, int roomId, String userId, String message, Date sentDate, boolean status, String imageId) {
        this.messageId = messageId;
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
        this.sentDate = sentDate;
        this.status = status;
        this.imageId = imageId;
    }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Date getSentDate() { return sentDate; }
    public void setSentDate(Date sentDate) { this.sentDate = sentDate; }
    public boolean isStatus() { return status; } // boolean 타입은 get 대신 is를 사용하기도 합니다.
    public void setStatus(boolean status) { this.status = status; }
    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "messageId=" + messageId +
                ", roomId=" + roomId +
                ", userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", sentDate=" + sentDate +
                ", status=" + status +
                ", imageId='" + imageId + '\'' +
                '}';
    }
}