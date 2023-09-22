package com.lotus.flatmate.state.dto;

import java.util.List;

import com.lotus.flatmate.township.dto.TownshipDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateDto {
	private Long id;
	private String name;
	private List<TownshipDto> townships;
}
