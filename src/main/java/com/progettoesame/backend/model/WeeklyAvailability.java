package com.progettoesame.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "weekly_availabilities")
@Data
public class WeeklyAvailability {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	private double monday;
	private double tuesday;
	private double wednesday;
	private double thursday;
	private double friday;
	private double saturday;
	private double sunday;
	
	public double getTotalWeeklyHours() {
		return monday + tuesday + wednesday + thursday + friday + saturday + sunday;
	}
}
