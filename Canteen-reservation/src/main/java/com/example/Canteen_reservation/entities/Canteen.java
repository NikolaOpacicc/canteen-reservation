package com.example.Canteen_reservation.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import com.example.Canteen_reservation.entities.model.WorkingHours;


@Entity
@Table(name = "canteen")
public class Canteen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private int capacity;
    
    @ElementCollection
    @CollectionTable(name = "canteen_working_hours", joinColumns = @JoinColumn(name = "canteen_id"))
    private List<WorkingHours> workingHours = new ArrayList<>();
    
    @OneToMany(mappedBy = "canteen")
    private List<Reservation> reservations = new ArrayList<>();
    
 
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
    
    public 	int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public List<WorkingHours> getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(List<WorkingHours> workingHours) {
        this.workingHours = workingHours;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setCanteen(this);
    }
    
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setCanteen(null);
    }
}
