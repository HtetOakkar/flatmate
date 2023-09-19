package com.lotus.flatmate.aparment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.aparment.entity.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long>{

}
