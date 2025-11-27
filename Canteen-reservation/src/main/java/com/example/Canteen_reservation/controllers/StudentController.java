package com.example.Canteen_reservation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Canteen_reservation.dtos.StudentDTO;
import com.example.Canteen_reservation.services.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	StudentService ss;

	@PostMapping
	public ResponseEntity<StudentDTO> saveStudent(@RequestBody StudentDTO studentDTO) {
		try {
			StudentDTO savedStudent = ss.saveStudent(studentDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<StudentDTO> getStudentById(@PathVariable int id) {
		try {
			StudentDTO student = ss.getStudentById(id);
			return ResponseEntity.ok(student);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
