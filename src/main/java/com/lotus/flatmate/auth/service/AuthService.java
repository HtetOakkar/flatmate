package com.lotus.flatmate.auth.service;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.CodeVerificationResponse;
import com.lotus.flatmate.auth.response.JwtAuthenticationResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.user.dto.UserDto;

import jakarta.validation.Valid;

public interface AuthService {

	VerificationResponse registerUser(UserDto userDto);

	CodeVerificationResponse verifyUser(@Valid VerificationRequest request);

	JwtAuthenticationResponse validateRefreshToken(@Valid String token);

}
