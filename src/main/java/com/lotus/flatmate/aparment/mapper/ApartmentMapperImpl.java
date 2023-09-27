package com.lotus.flatmate.aparment.mapper;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.aparment.dto.AllApartmentDto;
import com.lotus.flatmate.aparment.dto.ApartmentDto;
import com.lotus.flatmate.aparment.entity.Apartment;
import com.lotus.flatmate.aparment.request.ApartmentRequest;
import com.lotus.flatmate.aparment.response.ApartmentResponse;

@Component
public class ApartmentMapperImpl implements ApartmentMapper{

	@Override
	public ApartmentDto mapToDto(ApartmentRequest request) {
		ApartmentDto apartmentDto = new ApartmentDto();
		apartmentDto.setFloor(request.getFloor());
		apartmentDto.setLength(request.getLength());
		apartmentDto.setWidth(request.getWidth());
		apartmentDto.setApartmentType(request.getApartmentType());
		return apartmentDto;
	}

	@Override
	public Apartment mapToEntity(ApartmentDto apartmentDto) {
		Apartment apartment = new Apartment();
		apartment.setId(apartmentDto.getId());
		apartment.setFloor(apartmentDto.getFloor());
		apartment.setLength(apartmentDto.getLength());
		apartment.setWidth(apartmentDto.getWidth());
		apartment.setApartmentType(apartmentDto.getApartmentType());
		return apartment;
	}

	@Override
	public ApartmentDto mapToDto(Apartment apartment) {
		if (apartment == null) {
			return null;
		}
		ApartmentDto apartmentDto = new ApartmentDto();
		apartmentDto.setId(apartment.getId());
		apartmentDto.setFloor(apartment.getFloor());
		apartmentDto.setLength(apartment.getLength());
		apartmentDto.setWidth(apartment.getWidth());
		apartmentDto.setApartmentType(apartment.getApartmentType());
		return apartmentDto;
	}

	@Override
	public ApartmentResponse mapToResponse(ApartmentDto apartmentDto) {
		ApartmentResponse response = new ApartmentResponse();
		response.setId(apartmentDto.getId());
		response.setFloor(apartmentDto.getFloor());
		response.setLength(apartmentDto.getLength());
		response.setWidth(apartmentDto.getWidth());
		response.setApartmentType(apartmentDto.getApartmentType());
		return response;
	}

	@Override
	public ApartmentResponse mapToResponse(AllApartmentDto apartment) {
		if (apartment == null) {
			return null;
		}
		ApartmentResponse response = new ApartmentResponse();
		response.setId(apartment.getId());
		response.setFloor(apartment.getFloor());
		response.setWidth(apartment.getWidth());
		response.setLength(apartment.getLength());
		response.setApartmentType(apartment.getApartmentType());
		return response;
	}

}
