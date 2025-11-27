package com.example.Canteen_reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Canteen_reservation.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	boolean existsByEmail(String email);
}
