package com.example.Canteen_reservation.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.Canteen_reservation.enums.ReservationStatus;

@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private LocalTime time;
    
    @Column(nullable = false)
    private Integer duration;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.ACTIVE;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Canteen getCanteen() {
        return canteen;
    }
    
    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalTime getTime() {
        return time;
    }
    
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
   
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
    
    public boolean isActive() {
        return this.status == ReservationStatus.ACTIVE;
    }
    
    public LocalTime getEndTime() {
        return this.time.plusMinutes(this.duration);
    }
}
