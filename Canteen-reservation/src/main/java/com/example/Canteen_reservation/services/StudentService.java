package com.example.Canteen_reservation.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Canteen_reservation.dtos.StudentDTO;
import com.example.Canteen_reservation.entities.Student;
import com.example.Canteen_reservation.repositories.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository sr;

	public StudentDTO saveStudent(StudentDTO studentDTO) {
		if (studentDTO == null) {
			throw new IllegalArgumentException("Student cannot be null");
		}

		String name = studentDTO.getName();
		String email = studentDTO.getEmail();

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}

		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		
		if(sr.existsByEmail(email)) {
			throw new IllegalArgumentException("Student with this email already exists");
		}

		Student newStudent = new Student();
		newStudent.setName(name.trim());
		newStudent.setEmail(email.trim());
		newStudent.setAdmin(studentDTO.getIsAdmin());

		Student savedStudent = sr.save(newStudent);

		return convertToDTO(savedStudent);
	}

	public StudentDTO getStudentById(int id) {
		Optional<Student> optional = sr.findById(id);
		
		if (optional.isEmpty()) {
			throw new IllegalArgumentException("Student not found.");
		}

		Student student = optional.get();
		
		return convertToDTO(student);
	}
	
	public StudentDTO convertToDTO(Student student) {
		StudentDTO dto=new StudentDTO();
		dto.setName(student.getName());
		dto.setId(student.getId());
		dto.setEmail(student.getEmail());
		dto.setIsAdmin(student.isAdmin());
		return dto;
	}
}
