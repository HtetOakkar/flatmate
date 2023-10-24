package com.lotus.flatmate.apartment.dto;

import com.lotus.flatmate.apartment.entity.ApartmentType;

public interface AllApartmentDto {
	public Long getId();
	public int getFloor();
	public double getWidth();
	public double getLength();
	public ApartmentType getApartmentType();
}
