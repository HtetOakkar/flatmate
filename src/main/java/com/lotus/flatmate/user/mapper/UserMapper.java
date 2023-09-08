package com.lotus.flatmate.user.mapper;

import com.lotus.flatmate.auth.request.RegistrationRequest;
import com.lotus.flatmate.user.dto.UserDto;

import jakarta.validation.Valid;

public interface UserMapper {

	UserDto mapToUserDto(@Valid RegistrationRequest request);

}
