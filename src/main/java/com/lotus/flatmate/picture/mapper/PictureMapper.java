package com.lotus.flatmate.picture.mapper;

import com.lotus.flatmate.picture.dto.AllPictureDto;
import com.lotus.flatmate.picture.dto.PictureDto;
import com.lotus.flatmate.picture.entity.Picture;
import com.lotus.flatmate.picture.response.PictureResponse;

public interface PictureMapper {
	
	Picture mapToEntity(PictureDto pictureDto);
	
	PictureDto mapToDto(Picture picture);
	
	PictureResponse mapToResponse(PictureDto pictureDto);
	
	PictureResponse mapToResponse(AllPictureDto picture);
}
