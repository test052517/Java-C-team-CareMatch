package DTO;

public class UsersDTO {
    private String userId;
    private String role;
    private String password;
    private String phone;
    private String email;
    private String userCreateDate;

    public UsersDTO() {}

    public UsersDTO(String userId, String role, String password, String phone, String email, String userCreateDate) {
        this.userId = userId;
        this.role = role;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.userCreateDate = userCreateDate;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUserCreateDate() { return userCreateDate; }
    public void setUserCreateDate(String userCreateDate) { this.userCreateDate = userCreateDate; }

    // 객체의 상태 확인
    @Override
    public String toString() {
        return "UsersDTO{" +
                "userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", userCreateDate='" + userCreateDate + '\'' +
                '}';
    }
}