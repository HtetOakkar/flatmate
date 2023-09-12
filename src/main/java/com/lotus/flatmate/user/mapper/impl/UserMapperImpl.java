package com.lotus.flatmate.user.mapper.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lotus.flatmate.auth.request.RegistrationRequest;
import com.lotus.flatmate.socialContact.mapper.SocialContactMapper;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.response.UserProfileResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

	private final PasswordEncoder passwordEncoder;
	
	private final SocialContactMapper socialContactMapper;

	@Override
	public UserDto mapToUserDto(@Valid RegistrationRequest request) {
		UserDto userDto = new UserDto();
		userDto.setUsername(request.getUsername());
		userDto.setEmail(request.getEmail());
		userDto.setPassword(passwordEncoder.encode(request.getPassword()));
		userDto.setMobileNumber(request.getMobileNumber());
		return userDto;
	}

	@Override
	public UserDto mapToUserDto(User user) {
		if (user == null) {
			return null;
		}
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setMobileNumber(user.getMobileNumber());
		userDto.setPassword(user.getPassword());
		userDto.setLoginProvider(user.getLoginProvider());
		userDto.setCreatedAt(user.getCreatedAt());
		userDto.setUpdatedAt(user.getUpdatedAt());
		userDto.setProfileUrl(user.getProfileUrl());
		if (user.getSocialContacts().size() > 0) {
			userDto.setSocialContactDtos(user.getSocialContacts().stream().map(socialContactMapper::mapToSocialContactDto).toList());
		}
		return userDto;
	}

	@Override
	public UserProfileResponse mapToProfileResponse(UserDto userDto) {
		UserProfileResponse response = new UserProfileResponse();
		response.setId(userDto.getId());
		response.setUsername(userDto.getUsername());
		response.setEmail(userDto.getEmail());
		response.setMobileNumber(userDto.getMobileNumber());
		response.setProfileUrl(userDto.getProfileUrl());
		if (userDto.getSocialContactDtos() != null && !userDto.getSocialContactDtos().isEmpty()) {
			response.setSocialContacts(userDto.getSocialContactDtos().stream().map(socialContactMapper::mapToSocialContactResponse).toList());
		}
		return response;
	}

}
