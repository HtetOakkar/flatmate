package com.lotus.flatmate.township.entity;

import com.lotus.flatmate.state.entity.State;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "townships")
@Getter
@Setter
public class Township {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "township_id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	private State state;
	
}
