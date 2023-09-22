package com.lotus.flatmate.township.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.township.entity.Township;

@Repository
public interface TownshipRepository extends JpaRepository<Township, Long>{

	Township findByName(String string);

}
