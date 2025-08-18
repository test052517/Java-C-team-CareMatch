package DTO;

public class ShelterDTO {
    private String shelterId;
    private String shelterName;
    private String telephone;
    private String address;

    public ShelterDTO() {}

    public ShelterDTO(String shelterId, String shelterName, String telephone, String address) {
        this.shelterId = shelterId;
        this.shelterName = shelterName;
        this.telephone = telephone;
        this.address = address;
    }

    public String getShelterId() { return shelterId; }
    public void setShelterId(String shelterId) { this.shelterId = shelterId; }
    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "ShelterDTO{" +
                "shelterId='" + shelterId + '\'' +
                ", shelterName='" + shelterName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}