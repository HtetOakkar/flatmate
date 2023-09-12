package com.lotus.flatmate.user.mapper;

import com.lotus.flatmate.auth.request.RegistrationRequest;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.response.UserProfileResponse;

import jakarta.validation.Valid;

public interface UserMapper {

	UserDto mapToUserDto(@Valid RegistrationRequest request);

	UserDto mapToUserDto(User user);

	UserProfileResponse mapToProfileResponse(UserDto userDto);

}
