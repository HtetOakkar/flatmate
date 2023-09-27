package com.lotus.flatmate.aparment.dto;

import com.lotus.flatmate.aparment.entity.ApartmentType;

public interface AllApartmentDto {
	public Long getId();
	public int getFloor();
	public double getWidth();
	public double getLength();
	public ApartmentType getApartmentType();
}
