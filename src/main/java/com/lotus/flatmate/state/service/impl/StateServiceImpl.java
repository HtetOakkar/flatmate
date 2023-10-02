package com.lotus.flatmate.state.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.model.exception.RecordNotFoundException;
import com.lotus.flatmate.state.dto.StateDto;
import com.lotus.flatmate.state.entity.State;
import com.lotus.flatmate.state.mapper.StateMapper;
import com.lotus.flatmate.state.repository.StateRepository;
import com.lotus.flatmate.state.service.StateService;
import com.lotus.flatmate.township.entity.Township;
import com.lotus.flatmate.township.repository.TownshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

	private final StateRepository stateRepository;

	private final TownshipRepository townshipRepository;
	
	private final StateMapper stateMapper;

	@Override
	public void addSate(MultipartFile dataFile) {
		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(dataFile.getInputStream(), StandardCharsets.UTF_8));
				CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true)
						.setIgnoreHeaderCase(true).setTrim(true).build().parse(bufferedReader)) {

			for (CSVRecord csvRecord : csvParser) {
				List<String> headers = csvParser.getHeaderNames();

				State state = stateRepository.findByName(csvRecord.get(headers.get(0)));
				if (state == null) {
					state = new State();
					state.setName(csvRecord.get(headers.get(0)));
					state = stateRepository.save(state);
				}

				Township township = townshipRepository.findByName(csvRecord.get(headers.get(1)));
				if (township == null) {
					township = new Township();
					township.setName(csvRecord.get(headers.get(1)));
					township.setState(state);
					township = townshipRepository.save(township);
				}

			}
		} catch (IOException e) {
			throw new RuntimeException("Error parsing in csv file.", e);
		}
	}

	@Override
	public List<StateDto> getAllState() {
		List<State> states = stateRepository.findAll();
		return states.stream().map(stateMapper::mapToDto).toList();
	}

	@Override
	public StateDto getStateById(Long id) {
		State state = stateRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("State not found with id : " + id));
		return stateMapper.mapToDto(state);
	}

}
