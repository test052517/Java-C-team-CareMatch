package DTO;

import java.sql.Date;

public class AnimalsDTO {
    private String animalId;
    private int age;
    private double weight;
    private String sex;
    private String neutered;
    private Date happenDate;
    private String kindId;
    private String shelterId;
    private String status;
    private String imageId;
    private String animalName;

    public AnimalsDTO() {}

    public AnimalsDTO(String animalId, int age, double weight, String sex, String neutered, Date happenDate, String kindId, String shelterId, String status, String imageId, String animalName) {
        this.animalId = animalId;
        this.age = age;
        this.weight = weight;
        this.sex = sex;
        this.neutered = neutered;
        this.happenDate = happenDate;
        this.kindId = kindId;
        this.shelterId = shelterId;
        this.status = status;
        this.imageId = imageId;
        this.animalName = animalName;
    }

    public String getAnimalId() { return animalId; }
    public void setAnimalId(String animalId) { this.animalId = animalId; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getNeutered() { return neutered; }
    public void setNeutered(String neutered) { this.neutered = neutered; }
    public Date getHappenDate() { return happenDate; }
    public void setHappenDate(Date happenDate) { this.happenDate = happenDate; }
    public String getKindId() { return kindId; }
    public void setKindId(String kindId) { this.kindId = kindId; }
    public String getShelterId() { return shelterId; }
    public void setShelterId(String shelterId) { this.shelterId = shelterId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }
    public String getAnimalName() { return animalName; }
    public void setAnimalName(String animalName) { this.animalName = animalName; }

    @Override
    public String toString() {
        return "AnimalsDTO{" +
                "animalId='" + animalId + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", sex='" + sex + '\'' +
                ", neutered='" + neutered + '\'' +
                ", happenDate=" + happenDate +
                ", kindId='" + kindId + '\'' +
                ", shelterId='" + shelterId + '\'' +
                ", status='" + status + '\'' +
                ", imageId='" + imageId + '\'' +
                ", animalName='" + animalName + '\'' +
                '}';
    }
}