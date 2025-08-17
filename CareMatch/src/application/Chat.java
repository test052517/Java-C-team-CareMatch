package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Chat {
    private String userId;
    private LocalDate createdDate;

    public Chat(String userId, String createdDateStr) {
        this.userId = userId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdDate = LocalDate.parse(createdDateStr, formatter);
    }

    public String getUserId() { return userId; }
    public LocalDate getCreatedDate() { return createdDate; }
}