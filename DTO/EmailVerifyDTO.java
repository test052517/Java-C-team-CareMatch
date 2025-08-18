package DTO;

import java.sql.Date;

public class EmailVerifyDTO {
    private int verifyId;
    private String verificationCode;
    private String userId;
    private boolean verified;
    private Date createdAt;

    public EmailVerifyDTO() {}

    public EmailVerifyDTO(int verifyId, String verificationCode, String userId, boolean verified, Date createdAt) {
        this.verifyId = verifyId;
        this.verificationCode = verificationCode;
        this.userId = userId;
        this.verified = verified;
        this.createdAt = createdAt;
    }

    public int getVerifyId() { return verifyId; }
    public void setVerifyId(int verifyId) { this.verifyId = verifyId; }
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "EmailVerifyDTO{" +
                "verifyId=" + verifyId +
                ", verificationCode='" + verificationCode + '\'' +
                ", userId='" + userId + '\'' +
                ", verified=" + verified +
                ", createdAt=" + createdAt +
                '}';
    }
}