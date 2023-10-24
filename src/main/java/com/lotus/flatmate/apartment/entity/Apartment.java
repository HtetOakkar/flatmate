package com.lotus.flatmate.apartment.entity;

import com.lotus.flatmate.post.entity.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "apartments")
@Getter
@Setter
public class Apartment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aprtment_id")
	private Long id;
	
	@Column(name = "floor")
	private int floor;
	
	@Column(name = "width")
	private double width;
	
	@Column(name = "length")
	private double length;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "apartment_type")
	private ApartmentType apartmentType;
	
	@OneToOne
	@JoinColumn(name = "post_id", referencedColumnName = "post_id")
	private Post post;
}
