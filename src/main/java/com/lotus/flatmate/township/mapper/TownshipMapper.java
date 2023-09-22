package com.lotus.flatmate.township.mapper;

import com.lotus.flatmate.township.dto.TownshipDto;
import com.lotus.flatmate.township.entity.Township;
import com.lotus.flatmate.township.response.TownshipResponse;

public interface TownshipMapper {
	
	TownshipDto mapToDto(Township township);
	
	TownshipResponse mapToResponse(TownshipDto townshipDto);
}
