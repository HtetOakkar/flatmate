package com.lotus.flatmate.state.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.state.dto.StateDto;
import com.lotus.flatmate.state.mapper.StateMapper;
import com.lotus.flatmate.state.response.StateResponse;
import com.lotus.flatmate.state.service.StateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/state")
@RequiredArgsConstructor
public class StateController {
	
	private final StateService stateService;
	
	private final StateMapper stateMapper;
	
	@PostMapping
	public ResponseEntity<?> addStateFromCsvFile(@RequestPart(value = "csv") MultipartFile dataFile) {
		if (dataFile.getContentType().equals("text/csv") || dataFile.getContentType().equals("application/vnd.ms-excel")) {
			try {
				stateService.addSate(dataFile);
				return ResponseEntity.ok("imported successfully.");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not import file.");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a csv or ms-excel file.");
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<StateResponse> getAllState() {
		List<StateDto> stateDtos = stateService.getAllState();
		return stateDtos.stream().map(stateMapper::mapToResponse).toList();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public StateResponse getState(@PathVariable Long id) {
		StateDto stateDto = stateService.getStateById(id);
		return stateMapper.mapToResponse(stateDto);
	}
}
