package com.lotus.flatmate.picture.mapper;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.picture.dto.PictureDto;
import com.lotus.flatmate.picture.entity.Picture;
import com.lotus.flatmate.picture.response.PictureResponse;

@Component
public class PictureMapperImpl implements PictureMapper{

	@Override
	public Picture mapToEntity(PictureDto pictureDto) {
		Picture picture = new Picture();
		picture.setId(pictureDto.getId());
		picture.setUrl(pictureDto.getUrl());
		return picture;
	}

	@Override
	public PictureDto mapToDto(Picture picture) {
		if (picture == null) {
			return null;
		}
		PictureDto pictureDto = new PictureDto();
		pictureDto.setId(picture.getId());
		pictureDto.setUrl(picture.getUrl());
		return pictureDto;
	}

	@Override
	public PictureResponse mapToResponse(PictureDto pictureDto) {
		PictureResponse response = new PictureResponse();
		response.setId(pictureDto.getId());
		response.setUrl(pictureDto.getUrl());
		return response;
	}

}
