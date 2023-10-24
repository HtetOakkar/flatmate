package com.lotus.flatmate.apartment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lotus.flatmate.apartment.entity.ApartmentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentResponse {
	private Long id;
	private int floor;
	private double length;
	private double width;
	@JsonProperty("apartment_type")
	private ApartmentType apartmentType;
}
