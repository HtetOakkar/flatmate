package com.lotus.flatmate.state.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.state.dto.StateDto;

public interface StateService {

	void addSate(MultipartFile dataFile);

	List<StateDto> getAllState();

	StateDto getStateById(Long id);

}
