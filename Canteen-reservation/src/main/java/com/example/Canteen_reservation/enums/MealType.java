package com.example.Canteen_reservation.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MealType {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner");
	
	private final String value;
	    
	MealType(String value) {
	   this.value = value;
	}
	    
	@JsonValue
	public String getValue() {
	   return value;
	}
}