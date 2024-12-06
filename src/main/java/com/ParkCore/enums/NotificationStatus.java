package com.ParkCore.enums;

public enum NotificationStatus {
    SENT("Sent"),
    PENDING("Pending"),
    DELIVERED("Delivered");

    private final String status;

    NotificationStatus(String status) {
        this.status = status;
    }

    public static NotificationStatus SENT(String s) {
        return null;
    }

    public String getStatus() {
        return status;
    }
}
