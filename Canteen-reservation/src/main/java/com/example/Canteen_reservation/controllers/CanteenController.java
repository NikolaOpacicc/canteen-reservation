package com.example.Canteen_reservation.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Canteen_reservation.dtos.CanteenCapacityDTO;
import com.example.Canteen_reservation.dtos.CanteenDTO;
import com.example.Canteen_reservation.services.CanteenService;

@RestController
@RequestMapping("/canteens")
public class CanteenController {

	@Autowired
	CanteenService cs;

	@PostMapping
	public ResponseEntity<CanteenDTO> saveCanteen(@RequestHeader("studentId") int studentId,
			@RequestBody CanteenDTO canteen) {
		try {
			CanteenDTO savedCanteen = cs.saveCanteen(canteen, studentId);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCanteen);
		} catch (SecurityException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<CanteenDTO>> getAllCanteens() {
		return ResponseEntity.ok(cs.getAllCanteens());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CanteenDTO> getCanteenById(@PathVariable int id) {
		try {
			CanteenDTO canteen = cs.getCanteenById(id);
			return ResponseEntity.ok(canteen);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<CanteenDTO> updateCanteen(@PathVariable int id, @RequestHeader("studentId") int studentId,
			@RequestBody CanteenDTO canteenDTO) {
		try {
			CanteenDTO updatedCanteen = cs.updateCanteen(id, studentId, canteenDTO);
			return ResponseEntity.ok(updatedCanteen);

		} catch (SecurityException e) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("not found")) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCanteen(@PathVariable int id, @RequestHeader("studentId") int studentId) {
		try {
			cs.deleteCanteen(id, studentId);
			return ResponseEntity.noContent().build();
		} catch (SecurityException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("not found")) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/status")
	public ResponseEntity<List<CanteenCapacityDTO>> getAllCanteensCapacity(@RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String startTime, @RequestParam String endTime,
			@RequestParam Integer duration) {

		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);
			LocalTime timeStart = LocalTime.parse(startTime);
			LocalTime timeEnd = LocalTime.parse(endTime);

			List<CanteenCapacityDTO> capacities = cs.getAllCanteensCapacity(start, end, timeStart, timeEnd, duration);

			return ResponseEntity.ok(capacities);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}/status")
	public ResponseEntity<CanteenCapacityDTO> getCanteenCapacity(@PathVariable int id, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String startTime, @RequestParam String endTime,
			@RequestParam Integer duration) {

		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);
			LocalTime timeStart = LocalTime.parse(startTime);
			LocalTime timeEnd = LocalTime.parse(endTime);

			CanteenCapacityDTO capacity = cs.getCanteenCapacity(id, start, end, timeStart, timeEnd, duration);

			return ResponseEntity.ok(capacity);

		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("not found")) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
