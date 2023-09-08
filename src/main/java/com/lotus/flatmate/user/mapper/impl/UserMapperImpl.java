package com.lotus.flatmate.user.mapper.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lotus.flatmate.auth.request.RegistrationRequest;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDto mapToUserDto(@Valid RegistrationRequest request) {
		UserDto userDto = new UserDto();
		userDto.setUsername(request.getUsername());
		userDto.setEmail(request.getEmail());
		userDto.setPassword(passwordEncoder.encode(request.getPassword()));
		userDto.setMobileNumber(request.getMobileNumber());
		return userDto;
	}

}
