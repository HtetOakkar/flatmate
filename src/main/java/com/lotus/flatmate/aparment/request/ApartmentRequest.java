package com.lotus.flatmate.aparment.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lotus.flatmate.aparment.entity.ApartmentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentRequest {
	private int floor;
	private double length;
	private double width;
	@JsonProperty("apartment_type")
	private ApartmentType apartmentType;
}
