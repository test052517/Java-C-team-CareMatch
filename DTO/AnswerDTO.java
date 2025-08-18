package DTO;

import java.sql.Timestamp;

public class AnswerDTO {
    private String answerId;
    private String title;
    private String content;
    private Timestamp createdDate;
    private String inquiryId;
    private String userId;

    public AnswerDTO() {}

    public AnswerDTO(String answerId, String title, String content, Timestamp createdDate, String inquiryId, String userId) {
        this.answerId = answerId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.inquiryId = inquiryId;
        this.userId = userId;
    }

    public String getAnswerId() { return answerId; }
    public void setAnswerId(String answerId) { this.answerId = answerId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    public String getInquiryId() { return inquiryId; }
    public void setInquiryId(String inquiryId) { this.inquiryId = inquiryId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "answerId='" + answerId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", inquiryId='" + inquiryId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}