package com.lotus.flatmate.aparment.mapper;

import com.lotus.flatmate.aparment.dto.AllApartmentDto;
import com.lotus.flatmate.aparment.dto.ApartmentDto;
import com.lotus.flatmate.aparment.entity.Apartment;
import com.lotus.flatmate.aparment.request.ApartmentRequest;
import com.lotus.flatmate.aparment.response.ApartmentResponse;

public interface ApartmentMapper {

	ApartmentDto mapToDto(ApartmentRequest request);

	Apartment mapToEntity(ApartmentDto apartmentDto);

	ApartmentDto mapToDto(Apartment apartment);

	ApartmentResponse mapToResponse(ApartmentDto apartmentDto);

	ApartmentResponse mapToResponse(AllApartmentDto apartment);

}
