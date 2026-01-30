package org.bettercare.business.entities;

import org.bettercare.business.entities.enums.NOTIFICATION_LEVEL;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private UserAccount user;  // receiver
    private String message;
    private NOTIFICATION_LEVEL level;  // INFO / WARNING / DANGER
    private LocalDateTime createdAt;   // when created
    private boolean seen;

    public Notification() {
    }

    public Notification(String message,
                        NOTIFICATION_LEVEL level,
                        LocalDateTime createdAt) {
        this.message = message;
        this.level = level;
        this.createdAt = createdAt;
        this.seen = false;
    }

    public Notification(UserAccount user,
                        String message,
                        NOTIFICATION_LEVEL level,
                        LocalDateTime createdAt) {
        this.user = user;
        this.message = message;
        this.level = level;
        this.createdAt = createdAt;
        this.seen = false;
    }


    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NOTIFICATION_LEVEL getLevel() {
        return level;
    }

    public void setLevel(NOTIFICATION_LEVEL level) {
        this.level = level;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void markAsSeen() {
        this.seen = true;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + notificationId +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", seen=" + seen +
                '}';
    }
}