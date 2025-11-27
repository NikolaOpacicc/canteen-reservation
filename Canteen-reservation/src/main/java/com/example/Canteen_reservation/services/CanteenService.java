package com.example.Canteen_reservation.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Canteen_reservation.dtos.CanteenCapacityDTO;
import com.example.Canteen_reservation.dtos.CanteenDTO;
import com.example.Canteen_reservation.dtos.SlotDTO;
import com.example.Canteen_reservation.dtos.WorkingHoursDTO;
import com.example.Canteen_reservation.entities.Canteen;
import com.example.Canteen_reservation.entities.Reservation;
import com.example.Canteen_reservation.entities.Student;
import com.example.Canteen_reservation.entities.model.WorkingHours;
import com.example.Canteen_reservation.enums.MealType;
import com.example.Canteen_reservation.repositories.CanteenRepository;
import com.example.Canteen_reservation.repositories.ReservationRepository;
import com.example.Canteen_reservation.repositories.StudentRepository;

@Service
public class CanteenService {

	@Autowired
	CanteenRepository cr;

	@Autowired
	StudentRepository sr;

	@Autowired
	ReservationRepository rr;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	public CanteenDTO saveCanteen(CanteenDTO canteenDTO, int studentId) {

		Optional<Student> optional = sr.findById(studentId);

		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Student not found");
		}

		Student student = optional.get();

		if (!student.isAdmin()) {
			throw new SecurityException("Only admin students can create canteens");
		}

		validateCanteenDTO(canteenDTO);

		if (cr.existsByName(canteenDTO.getName())) {
			throw new IllegalArgumentException("Canteen with this name already exists");
		}

		Canteen canteen = new Canteen();
		canteen.setName(canteenDTO.getName().trim());
		canteen.setLocation(canteenDTO.getLocation().trim());
		canteen.setCapacity(canteenDTO.getCapacity());

		List<WorkingHours> workingHoursList = new ArrayList<>();
		for (WorkingHoursDTO dto : canteenDTO.getWorkingHours()) {
			WorkingHours wh = convertToWorkingHours(dto);
			workingHoursList.add(wh);
		}
		canteen.setWorkingHours(workingHoursList);

		Canteen savedCanteen = cr.save(canteen);

		return convertToDTO(savedCanteen);
	}

	public List<CanteenDTO> getAllCanteens() {
		List<CanteenDTO> canteensDTO = new LinkedList<CanteenDTO>();
		List<Canteen> canteens = cr.findAll();

		for (Canteen c : canteens) {
			canteensDTO.add(convertToDTO(c));
		}

		return canteensDTO;
	}

	public CanteenDTO getCanteenById(int id) {
		Optional<Canteen> optional = cr.findById(id);

		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Canteen not found");
		}

		return convertToDTO(optional.get());
	}

	public CanteenDTO updateCanteen(int id, int studentId, CanteenDTO canteenDTO) {
		Optional<Student> optional = sr.findById(studentId);

		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Student not found");
		}

		Student student = optional.get();

		if (!student.isAdmin()) {
			throw new SecurityException("Only admin students can create canteens");
		}

		Optional<Canteen> optCanteen = cr.findById(id);

		if (optCanteen.isEmpty()) {
			throw new IllegalArgumentException("Canteen not found");
		}

		Canteen canteen = optCanteen.get();

		if (canteenDTO.getName() != null && !canteenDTO.getName().trim().isEmpty()) {
		    String newName = canteenDTO.getName().trim();
		    if (!canteen.getName().equals(newName)) {
		        if (cr.existsByName(newName)) {
		            throw new IllegalArgumentException("Canteen with this name already exists");
		        }
		        canteen.setName(newName);
		    }
		}
		if (canteenDTO.getLocation() != null && !canteenDTO.getLocation().trim().isEmpty()) {
			canteen.setLocation(canteenDTO.getLocation());
		}

		if (canteenDTO.getCapacity() != null) {
			if (canteenDTO.getCapacity() <= 0) {
				throw new IllegalArgumentException("Canteen capacity must be greater than 0");
			}
			canteen.setCapacity(canteenDTO.getCapacity());
		}

		if (canteenDTO.getWorkingHours() != null && !canteenDTO.getWorkingHours().isEmpty()) {
			for (WorkingHoursDTO dto : canteenDTO.getWorkingHours()) {
				validateWorkingHours(dto);
			}

			List<WorkingHours> workingHoursList = new LinkedList<WorkingHours>();
			for (WorkingHoursDTO dto : canteenDTO.getWorkingHours()) {
				workingHoursList.add(convertToWorkingHours(dto));
			}
			canteen.setWorkingHours(workingHoursList);
		}

		Canteen updatedCanteen = cr.save(canteen);

		return convertToDTO(updatedCanteen);
	}

	public void deleteCanteen(int id, int studentId) {
		Optional<Student> optional = sr.findById(studentId);

		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Student not found");
		}

		Student student = optional.get();

		if (!student.isAdmin()) {
			throw new SecurityException("Only admin students can create canteens");
		}

		Optional<Canteen> optCanteen = cr.findById(id);

		if (optCanteen.isEmpty()) {
			throw new IllegalArgumentException("Canteen not found");
		}

		Canteen canteen = optCanteen.get();

		List<Reservation> reservations = canteen.getReservations();
		for (Reservation r : reservations) {
			r.cancel();
			rr.save(r);
		}

		cr.delete(canteen);
	}

	public List<CanteenCapacityDTO> getAllCanteensCapacity(LocalDate startDate, LocalDate endDate, LocalTime startTime,
			LocalTime endTime, Integer duration) {

		validateCapacityRequest(startDate, endDate, startTime, endTime, duration);

		List<Canteen> canteens = cr.findAll();
		List<CanteenCapacityDTO> result = new ArrayList<>();

		for (Canteen canteen : canteens) {
			CanteenCapacityDTO capacityDTO = calculateCanteenCapacity(canteen, startDate, endDate, startTime, endTime,
					duration);
			result.add(capacityDTO);
		}

		return result;
	}

	public CanteenCapacityDTO getCanteenCapacity(int canteenId, LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime, Integer duration) {

		validateCapacityRequest(startDate, endDate, startTime, endTime, duration);

		Optional<Canteen> optional = cr.findById(canteenId);
		
		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Canteen not found");
		}
		
		Canteen canteen = optional.get();
		
		return calculateCanteenCapacity(canteen, startDate, endDate, startTime, endTime, duration);
	}

	private void validateCapacityRequest(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
			Integer duration) {

		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException("Start date and end date are required");
		}

		if (startTime == null || endTime == null) {
			throw new IllegalArgumentException("Start time and end time are required");
		}

		if (duration == null || (duration != 30 && duration != 60)) {
			throw new IllegalArgumentException("Duration must be 30 or 60 minutes");
		}

		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("End date must be after start date");
		}

		if (!endTime.isAfter(startTime)) {
			throw new IllegalArgumentException("End time must be after start time");
		}
	}

	private CanteenCapacityDTO calculateCanteenCapacity(Canteen canteen, LocalDate startDate, LocalDate endDate,
			LocalTime startTime, LocalTime endTime, Integer duration) {

		List<SlotDTO> slots = new ArrayList<>();

		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {

			for (WorkingHours wh : canteen.getWorkingHours()) {

		
				LocalTime whStart = wh.getFromTime().isAfter(startTime) ? wh.getFromTime() : startTime;
				LocalTime whEnd = wh.getToTime().isBefore(endTime) ? wh.getToTime() : endTime;

				if (whStart.isBefore(whEnd)) {
					
					LocalTime slotTime = whStart;

					while (slotTime.plusMinutes(duration).isBefore(whEnd)
							|| slotTime.plusMinutes(duration).equals(whEnd)) {

						
						if (slotTime.getMinute() == 0 || slotTime.getMinute() == 30) {

							
							int reservedCount = rr.countActiveReservationsByTimeSlot(
									canteen.getId(), currentDate, slotTime, duration);

							int remainingCapacity = canteen.getCapacity() - (int) reservedCount;

							SlotDTO slot = new SlotDTO(currentDate.toString(), wh.getMeal().getValue(),
									slotTime.format(TIME_FORMATTER), remainingCapacity);

							slots.add(slot);
						}

						slotTime = slotTime.plusMinutes(duration);
					}
				}
			}

			currentDate = currentDate.plusDays(1);
		}
		CanteenCapacityDTO canteenCapacityDTO=new CanteenCapacityDTO();
		canteenCapacityDTO.setCanteenId(canteen.getId());
		canteenCapacityDTO.setSlots(slots);

		return canteenCapacityDTO;
	}

	private void validateCanteenDTO(CanteenDTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("Canteen data cannot be null");
		}

		if (dto.getName() == null || dto.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Canteen name cannot be empty");
		}

		if (dto.getLocation() == null || dto.getLocation().trim().isEmpty()) {
			throw new IllegalArgumentException("Canteen location cannot be empty");
		}

		if (dto.getCapacity() == null || dto.getCapacity() <= 0) {
			throw new IllegalArgumentException("Canteen capacity must be greater than 0");
		}

		if (dto.getWorkingHours() == null || dto.getWorkingHours().isEmpty()) {
			throw new IllegalArgumentException("Working hours cannot be empty");
		}

		for (WorkingHoursDTO wh : dto.getWorkingHours()) {
			validateWorkingHours(wh);
		}
	}

	private void validateWorkingHours(WorkingHoursDTO dto) {
		if (dto.getMeal() == null || dto.getMeal().trim().isEmpty()) {
			throw new IllegalArgumentException("Meal type cannot be empty");
		}

		try {
			MealType.valueOf(dto.getMeal().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid meal type: " + dto.getMeal());
		}

		if (dto.getFrom() == null || dto.getFrom().trim().isEmpty()) {
			throw new IllegalArgumentException("From time cannot be empty");
		}

		if (dto.getTo() == null || dto.getTo().trim().isEmpty()) {
			throw new IllegalArgumentException("To time cannot be empty");
		}

		try {
			LocalTime from = LocalTime.parse(dto.getFrom(), TIME_FORMATTER);
			LocalTime to = LocalTime.parse(dto.getTo(), TIME_FORMATTER);

			if (!to.isAfter(from)) {
				throw new IllegalArgumentException("To time must be after from time");
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid time format. Use HH:mm format (e.g., 08:00)");
		}
	}

	private WorkingHours convertToWorkingHours(WorkingHoursDTO dto) {
		WorkingHours wh = new WorkingHours();
		wh.setMeal(MealType.valueOf(dto.getMeal().toUpperCase()));
		wh.setFromTime(LocalTime.parse(dto.getFrom(), TIME_FORMATTER));
		wh.setToTime(LocalTime.parse(dto.getTo(), TIME_FORMATTER));
		return wh;
	}

	private CanteenDTO convertToDTO(Canteen canteen) {
		CanteenDTO dto = new CanteenDTO();
		dto.setId(canteen.getId());
		dto.setName(canteen.getName());
		dto.setLocation(canteen.getLocation());
		dto.setCapacity(canteen.getCapacity());

		List<WorkingHoursDTO> workingHoursDTOs = new ArrayList<>();
		for (WorkingHours wh : canteen.getWorkingHours()) {
			WorkingHoursDTO whDTO = new WorkingHoursDTO();
			whDTO.setMeal(wh.getMeal().getValue());
			whDTO.setFrom(wh.getFromTime().format(TIME_FORMATTER));
			whDTO.setTo(wh.getToTime().format(TIME_FORMATTER));
			workingHoursDTOs.add(whDTO);
		}
		dto.setWorkingHours(workingHoursDTOs);

		return dto;
	}
}
