package com.example.Canteen_reservation.dtos;

import java.util.List;

public class CanteenDTO {
    
    private Integer id;
    
    private String name;
    
    private String location;
    
    private Integer capacity;
    
    private List<WorkingHoursDTO> workingHours;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public List<WorkingHoursDTO> getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(List<WorkingHoursDTO> workingHours) {
        this.workingHours = workingHours;
    }
}