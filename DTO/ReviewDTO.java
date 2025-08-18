package DTO;

import java.sql.Date;

public class ReviewDTO {
    private String reviewId;
    private Date createdDate;
    private String title;
    private String content;
    private String imageId;
    private int likeCount; // SQL 예약어 'like' 대신 'likeCount' 사용
    private String userId;
    private String animalId;

    public ReviewDTO() {}

    public ReviewDTO(String reviewId, Date createdDate, String title, String content, String imageId, int likeCount, String userId, String animalId) {
        this.reviewId = reviewId;
        this.createdDate = createdDate;
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        this.likeCount = likeCount;
        this.userId = userId;
        this.animalId = animalId;
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getAnimalId() { return animalId; }
    public void setAnimalId(String animalId) { this.animalId = animalId; }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "reviewId='" + reviewId + '\'' +
                ", createdDate=" + createdDate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageId='" + imageId + '\'' +
                ", likeCount=" + likeCount +
                ", userId='" + userId + '\'' +
                ", animalId='" + animalId + '\'' +
                '}';
    }
}