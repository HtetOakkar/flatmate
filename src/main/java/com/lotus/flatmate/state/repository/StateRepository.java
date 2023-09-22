package com.lotus.flatmate.state.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotus.flatmate.state.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long>{

	State findByName(String string);

}
