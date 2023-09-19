package com.lotus.flatmate.aparment.dto;

import com.lotus.flatmate.aparment.entity.ApartmentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentDto {
	private Long id;
	private int floor;
	private double width;
	private double length;
	private ApartmentType apartmentType;
}
