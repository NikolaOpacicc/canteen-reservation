package com.example.Canteen_reservation.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Canteen_reservation.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@Query("SELECT r FROM Reservation r WHERE r.student.id = :studentId "
			+ "AND r.date = :date AND r.status = 'ACTIVE'")
	List<Reservation> findActiveReservationsByStudentAndDate(@Param("studentId") Integer studentId,
			@Param("date") LocalDate date);

	
	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.canteen.id = :canteenId "
			+ "AND r.date = :date AND r.time = :time AND r.duration = :duration " + "AND r.status = 'ACTIVE'")
	int countActiveReservationsByTimeSlot(@Param("canteenId") Integer canteenId, @Param("date") LocalDate date,
			@Param("time") LocalTime time, @Param("duration") Integer duration);
}
