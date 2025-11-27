package com.example.Canteen_reservation.dtos;

public class SlotDTO {
	
	private String date;
	
    private String meal;
    
    private String startTime;
    
    private Integer remainingCapacity;
    
    public SlotDTO(String date, String meal, String startTime, Integer remainingCapacity) {
        this.date = date;
        this.meal = meal;
        this.startTime = startTime;
        this.remainingCapacity = remainingCapacity;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMeal() {
		return meal;
	}

	public void setMeal(String meal) {
		this.meal = meal;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getRemainingCapacity() {
		return remainingCapacity;
	}

	public void setRemainingCapacity(Integer remainingCapacity) {
		this.remainingCapacity = remainingCapacity;
	}
    
    
    
}
