package DTO;

import java.sql.Date;

public class VisitDTO {
    private String visitId;
    private String purpose;
    private String status;
    private Date visitDate;
    private Date submittedDate;
    private String userId;
    private String animalId;
    private String applicationId;

    public VisitDTO() {}

    public VisitDTO(String visitId, String purpose, String status, Date visitDate, Date submittedDate, String userId, String animalId, String applicationId) {
        this.visitId = visitId;
        this.purpose = purpose;
        this.status = status;
        this.visitDate = visitDate;
        this.submittedDate = submittedDate;
        this.userId = userId;
        this.animalId = animalId;
        this.applicationId = applicationId;
    }

    public String getVisitId() { return visitId; }
    public void setVisitId(String visitId) { this.visitId = visitId; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getVisitDate() { return visitDate; }
    public void setVisitDate(Date visitDate) { this.visitDate = visitDate; }
    public Date getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(Date submittedDate) { this.submittedDate = submittedDate; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getAnimalId() { return animalId; }
    public void setAnimalId(String animalId) { this.animalId = animalId; }
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    @Override
    public String toString() {
        return "VisitDTO{" +
                "visitId='" + visitId + '\'' +
                ", purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                ", visitDate=" + visitDate +
                ", submittedDate=" + submittedDate +
                ", userId='" + userId + '\'' +
                ", animalId='" + animalId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                '}';
    }
}