package com.example.Canteen_reservation.dtos;

public class ReservationDTO {
    
    private Integer id;
    
    private String status;
    
    private Integer studentId;
    
    private Integer canteenId;
    
    private String date;
    
    private String time;
    
    private Integer duration;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public Integer getCanteenId() {
        return canteenId;
    }
    
    public void setCanteenId(int canteenId) {
        this.canteenId = canteenId;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
