package com.lotus.flatmate.state.response;

import java.util.List;

import com.lotus.flatmate.township.response.TownshipResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateResponse {
	private Long id;
	private String name;
	private List<TownshipResponse> townships;
}
