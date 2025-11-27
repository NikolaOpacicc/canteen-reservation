package com.example.Canteen_reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Canteen_reservation.entities.Canteen;

@Repository
public interface CanteenRepository extends JpaRepository<Canteen, Integer> {

	boolean existsByName(String name);
}
