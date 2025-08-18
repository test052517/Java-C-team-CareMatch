package DTO;

import java.sql.Date;

public class AdoptionDTO {
    private String applicationId;
    private String status;
    private Date visitDate;
    private Date submittedDate;
    private String userId;
    private String animalId;
    private String animalReason;

    public AdoptionDTO() {}

    public AdoptionDTO(String applicationId, String status, Date visitDate, Date submittedDate, String userId, String animalId, String animalReason) {
        this.applicationId = applicationId;
        this.status = status;
        this.visitDate = visitDate;
        this.submittedDate = submittedDate;
        this.userId = userId;
        this.animalId = animalId;
        this.animalReason = animalReason;
    }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
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
    public String getAnimalReason() { return animalReason; }
    public void setAnimalReason(String animalReason) { this.animalReason = animalReason; }

    @Override
    public String toString() {
        return "AdoptionDTO{" +
                "applicationId='" + applicationId + '\'' +
                ", status='" + status + '\'' +
                ", visitDate=" + visitDate +
                ", submittedDate=" + submittedDate +
                ", userId='" + userId + '\'' +
                ", animalId='" + animalId + '\'' +
                ", animalReason='" + animalReason + '\'' +
                '}';
    }
}