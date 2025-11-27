package com.example.Canteen_reservation.entities.model;

import java.time.LocalTime;

import com.example.Canteen_reservation.enums.MealType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class WorkingHours {
    
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MealType meal;
	    
    
	@Column(nullable = false)
	private LocalTime fromTime;
	    
	@Column(nullable = false)
	private LocalTime toTime;
 
    public MealType getMeal() {
		return meal;
	}

	public void setMeal(MealType meal) {
		this.meal = meal;
	}

	public LocalTime getFromTime() {
		return fromTime;
	}

	public void setFromTime(LocalTime fromTime) {
		this.fromTime = fromTime;
	}

	public LocalTime getToTime() {
		return toTime;
	}

	public void setToTime(LocalTime toTime) {
		this.toTime = toTime;
	}
      
}
