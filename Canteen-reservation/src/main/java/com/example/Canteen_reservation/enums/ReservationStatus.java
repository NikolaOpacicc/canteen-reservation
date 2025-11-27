package com.example.Canteen_reservation.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    ACTIVE("Active"),
    CANCELLED("Cancelled");
    
    private final String value;
    
    ReservationStatus(String value) {
        this.value = value;
    }
    
    @JsonValue
    public String getValue() {
        return value;
    }
}
