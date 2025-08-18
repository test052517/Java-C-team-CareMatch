package DTO;

import java.sql.Date;
	
public class InquiryDTO {
    private String inquiryId;
    private String title;
    private String content;
    private Date createdDate;
    private String status;
    private String userId;

    public InquiryDTO() {}

    public InquiryDTO(String inquiryId, String title, String content, Date createdDate, String status, String userId) {
        this.inquiryId = inquiryId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.status = status;
        this.userId = userId;
    }

    public String getInquiryId() { return inquiryId; }
    public void setInquiryId(String inquiryId) { this.inquiryId = inquiryId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "InquiryDTO{" +
                "inquiryId='" + inquiryId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}