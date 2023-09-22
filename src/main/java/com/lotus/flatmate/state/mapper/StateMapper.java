package com.lotus.flatmate.state.mapper;

import com.lotus.flatmate.state.dto.StateDto;
import com.lotus.flatmate.state.entity.State;
import com.lotus.flatmate.state.response.StateResponse;

public interface StateMapper {
	
	StateDto mapToDto(State state);
	
	StateResponse mapToResponse(StateDto stateDto);
}
