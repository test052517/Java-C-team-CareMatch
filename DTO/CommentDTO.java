package DTO;

import java.sql.Date;

public class CommentDTO {
    private String commentId;
    private Date createdDate;
    private String content;
    private int likeCount; // SQL 예약어 'like' 대신 'likeCount' 사용
    private String reviewId;
    private String userId;

    public CommentDTO() {}

    public CommentDTO(String commentId, Date createdDate, String content, int likeCount, String reviewId, String userId) {
        this.commentId = commentId;
        this.createdDate = createdDate;
        this.content = content;
        this.likeCount = likeCount;
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "commentId='" + commentId + '\'' +
                ", createdDate=" + createdDate +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                ", reviewId='" + reviewId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}