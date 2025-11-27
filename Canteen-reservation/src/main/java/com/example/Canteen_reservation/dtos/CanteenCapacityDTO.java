package com.example.Canteen_reservation.dtos;

import java.util.List;

public class CanteenCapacityDTO {
    
    private int canteenId;
    
    private List<SlotDTO> slots;
    
    public int getCanteenId() {
        return canteenId;
    }
    
    public void setCanteenId(int canteenId) {
        this.canteenId = canteenId;
    }
    
    public List<SlotDTO> getSlots() {
        return slots;
    }
    
    public void setSlots(List<SlotDTO> slots) {
        this.slots = slots;
    }
}
