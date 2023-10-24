package com.lotus.flatmate.apartment.mapper;

import com.lotus.flatmate.apartment.dto.AllApartmentDto;
import com.lotus.flatmate.apartment.dto.ApartmentDto;
import com.lotus.flatmate.apartment.entity.Apartment;
import com.lotus.flatmate.apartment.request.ApartmentRequest;
import com.lotus.flatmate.apartment.response.ApartmentResponse;

public interface ApartmentMapper {

	ApartmentDto mapToDto(ApartmentRequest request);

	Apartment mapToEntity(ApartmentDto apartmentDto);

	ApartmentDto mapToDto(Apartment apartment);

	ApartmentResponse mapToResponse(ApartmentDto apartmentDto);

	ApartmentResponse mapToResponse(AllApartmentDto apartment);

}
