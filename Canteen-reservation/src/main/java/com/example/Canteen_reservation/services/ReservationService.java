package com.example.Canteen_reservation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Canteen_reservation.dtos.ReservationDTO;
import com.example.Canteen_reservation.entities.Canteen;
import com.example.Canteen_reservation.entities.Reservation;
import com.example.Canteen_reservation.entities.Student;
import com.example.Canteen_reservation.entities.model.WorkingHours;
import com.example.Canteen_reservation.enums.ReservationStatus;
import com.example.Canteen_reservation.repositories.CanteenRepository;
import com.example.Canteen_reservation.repositories.ReservationRepository;
import com.example.Canteen_reservation.repositories.StudentRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository rr;

	@Autowired
	private StudentRepository sr;

	@Autowired
	private CanteenRepository cr;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	public ReservationDTO saveReservation(ReservationDTO reservationDTO) {

		validateReservationDTO(reservationDTO);

		int studentId = reservationDTO.getStudentId();
		int canteenId = reservationDTO.getCanteenId();
		LocalDate date = LocalDate.parse(reservationDTO.getDate());
		LocalTime time = LocalTime.parse(reservationDTO.getTime(), TIME_FORMATTER);
		Integer duration = reservationDTO.getDuration();

		Optional<Student> optStudent = sr.findById(studentId);
		if (optStudent.isEmpty()) {
			throw new IllegalArgumentException("Student not found");
		}

		Student student = optStudent.get();

		Optional<Canteen> optCanteen = cr.findById(canteenId);
		if (optCanteen.isEmpty()) {
			throw new IllegalArgumentException("Canteen not found");
		}

		Canteen canteen = optCanteen.get();

		if (date.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Cannot create reservation for past dates");
		}

		if (time.getMinute() != 0 && time.getMinute() != 30) {
			throw new IllegalArgumentException("Time must start on the hour or half hour");
		}

		if (duration != 30 && duration != 60) {
			throw new IllegalArgumentException("Duration must be 30 or 60 minutes");
		}

		LocalTime endTime = time.plusMinutes(duration);

		boolean isWithinWorkingHours = false;
		for (WorkingHours wh : canteen.getWorkingHours()) {
			if (!time.isBefore(wh.getFromTime()) && !endTime.isAfter(wh.getToTime())) {
				isWithinWorkingHours = true;
				break;
			}
		}

		if (!isWithinWorkingHours) {
			throw new IllegalArgumentException("Reservation time is outside canteen working hours");
		}

		List<Reservation> studentReservations = rr.findActiveReservationsByStudentAndDate(studentId, date);

		for (Reservation existingReservation : studentReservations) {
			LocalTime existingStart = existingReservation.getTime();
			LocalTime existingEnd = existingStart.plusMinutes(existingReservation.getDuration());

			if (time.isBefore(existingEnd) && existingStart.isBefore(endTime)) {
				throw new IllegalArgumentException("Student already has a reservation in this time period");
			}
		}

		int reservedCount = rr.countActiveReservationsByTimeSlot(canteenId, date, time, duration);

		if (reservedCount >= canteen.getCapacity()) {
			throw new IllegalArgumentException("Canteen is at full capacity for this time slot");
		}

		Reservation reservation = new Reservation();
		reservation.setStudent(student);
		reservation.setCanteen(canteen);
		reservation.setDate(date);
		reservation.setTime(time);
		reservation.setDuration(duration);
		reservation.setStatus(ReservationStatus.ACTIVE);

		Reservation savedReservation = rr.save(reservation);

		return convertToDTO(savedReservation);
	}

	public ReservationDTO deleteReservation(int id, int studentId) {
		Optional<Student> optStudent = sr.findById(studentId);
		if (optStudent.isEmpty()) {
			throw new IllegalArgumentException("Student not found");
		}

		Student student = optStudent.get();

		Optional<Reservation> optReservation = rr.findById(id);
		if (optReservation.isEmpty()) {
			throw new IllegalArgumentException("Reservation not found");
		}

		Reservation reservation = optReservation.get();

		if (reservation.getStudent().getId() != studentId) {
			throw new SecurityException("You can only cancel your own reservations");
		}

		if (reservation.getStatus() == ReservationStatus.CANCELLED) {
			throw new IllegalArgumentException("Reservation is already cancelled");
		}

		reservation.cancel();

		reservation = rr.save(reservation);

		return convertToDTO(reservation);
	}

	private void validateReservationDTO(ReservationDTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("Reservation data cannot be null");
		}

		if (dto.getStudentId() == null) {
			throw new IllegalArgumentException("Student ID is required");
		}

		if (dto.getCanteenId() == null) {
			throw new IllegalArgumentException("Canteen ID is required");
		}

		if (dto.getDate() == null || dto.getDate().trim().isEmpty()) {
			throw new IllegalArgumentException("Date is required");
		}

		if (dto.getTime() == null || dto.getTime().trim().isEmpty()) {
			throw new IllegalArgumentException("Time is required");
		}

		if (dto.getDuration() == null) {
			throw new IllegalArgumentException("Duration is required");
		}
	}

	private ReservationDTO convertToDTO(Reservation reservation) {
		ReservationDTO dto = new ReservationDTO();
		dto.setId(reservation.getId());
		dto.setStatus(reservation.getStatus().getValue());
		dto.setStudentId(reservation.getStudent().getId());
		dto.setCanteenId(reservation.getCanteen().getId());
		dto.setDate(reservation.getDate().toString());
		dto.setTime(reservation.getTime().format(TIME_FORMATTER));
		dto.setDuration(reservation.getDuration());
		return dto;
	}
}
