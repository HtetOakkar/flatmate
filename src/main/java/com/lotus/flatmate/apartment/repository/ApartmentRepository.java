package com.lotus.flatmate.apartment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.apartment.entity.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long>{

}
