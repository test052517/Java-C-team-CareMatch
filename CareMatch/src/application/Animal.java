package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Animal {
    private String name;
    private String type;
    private int age;
    private double weight;
    private String gender;       // "남" 또는 "여"
    private boolean neutered;    // 중성화 여부 boolean
    private LocalDate admissionDate;
    private String status;

    // 생성자 - admissionDate는 문자열(예: "2023-08-08")로 받아서 LocalDate 변환
    public Animal(String name, String type, int age, double weight, String gender, boolean neutered, String admissionDateStr) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.neutered = neutered;
        this.status = status;

        // 문자열을 LocalDate로 변환 (날짜 포맷에 맞게 변경 가능)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.admissionDate = LocalDate.parse(admissionDateStr, formatter);
    }

    // Getter methods
    public String getName() { return name; }
    public String getType() { return type; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public String getGender() { return gender; }
    public boolean isNeutered() { return neutered; }
    public LocalDate getAdmissionDate() { return admissionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}