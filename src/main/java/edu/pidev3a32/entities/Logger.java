package edu.pidev3a32.entities;

import java.time.LocalDateTime;

public class Logger {
    private int id;
    private int userId;
    private String action;
    private String details;
    private LocalDateTime timestamp;

    public Logger(int id, int userId, String action, String details, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public Logger(int userId, String action, String details) {
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Logger{" +
                "id=" + id +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
