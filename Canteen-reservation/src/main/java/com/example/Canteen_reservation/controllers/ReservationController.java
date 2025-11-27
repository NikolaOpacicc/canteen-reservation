package com.example.Canteen_reservation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Canteen_reservation.dtos.ReservationDTO;
import com.example.Canteen_reservation.services.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	ReservationService rs;

	@PostMapping
	public ResponseEntity<ReservationDTO> saveReservation(@RequestBody ReservationDTO reservationDTO) {
		try {
			ReservationDTO savedReservation = rs.saveReservation(reservationDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ReservationDTO> deleteReservation(@PathVariable int id,@RequestHeader("studentId")int studentId){
		 try {
		        ReservationDTO cancelledReservation = rs.deleteReservation(id, studentId);
		        return ResponseEntity.ok(cancelledReservation);
		        
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

}
