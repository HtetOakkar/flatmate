package com.lotus.flatmate.state.mapper.impl;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.state.dto.StateDto;
import com.lotus.flatmate.state.entity.State;
import com.lotus.flatmate.state.mapper.StateMapper;
import com.lotus.flatmate.state.response.StateResponse;
import com.lotus.flatmate.township.mapper.TownshipMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StateMapperImpl implements StateMapper {
	
	private final TownshipMapper townshipMapper;

	@Override
	public StateDto mapToDto(State state) {
		if (state == null) {
			return null;
		}
		StateDto stateDto = new StateDto();
		stateDto.setId(state.getId());
		stateDto.setName(state.getName());
		stateDto.setTownships(state.getTownships().stream().map(townshipMapper::mapToDto).toList());
		return stateDto;
	}

	@Override
	public StateResponse mapToResponse(StateDto stateDto) {
		StateResponse response = new StateResponse();
		response.setId(stateDto.getId());
		response.setName(stateDto.getName());
		response.setTownships(stateDto.getTownships().stream().map(townshipMapper::mapToResponse).toList());
		return response;
	}

}
