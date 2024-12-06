package com.ParkCore.enums;

public enum EmployeeStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String status;

    EmployeeStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}
