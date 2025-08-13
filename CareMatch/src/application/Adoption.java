package application;

public class Adoption {
    private String animalName;
    private String applicantName;
    private String status;
    private String applyDate;  // yyyy-MM-dd 포맷의 날짜 문자열
    private String visitDate;

    public Adoption(String animalName, String applicantName, String status, String applyDate, String visitDate) {
        this.animalName = animalName;
        this.applicantName = applicantName;
        this.status = status;
        this.applyDate = applyDate;
        this.visitDate = visitDate;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getStatus() {
        return status;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public String getVisitDate() {
        return visitDate;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}