package com.lotus.flatmate.township.mapper.impl;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.township.dto.TownshipDto;
import com.lotus.flatmate.township.entity.Township;
import com.lotus.flatmate.township.mapper.TownshipMapper;
import com.lotus.flatmate.township.response.TownshipResponse;

@Component
public class TownshipMapperImpl implements TownshipMapper {

	@Override
	public TownshipDto mapToDto(Township township) {
		if (township == null) {
			return null;
		}
		TownshipDto townshipDto = new TownshipDto();
		townshipDto.setId(township.getId());
		townshipDto.setName(township.getName());
		return townshipDto;
	}

	@Override
	public TownshipResponse mapToResponse(TownshipDto townshipDto) {
		TownshipResponse response = new TownshipResponse();
		response.setId(townshipDto.getId());
		response.setName(townshipDto.getName());
		return response;
	}
	
}
