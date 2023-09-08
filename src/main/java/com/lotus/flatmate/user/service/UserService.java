package com.lotus.flatmate.user.service;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.user.response.OtpVerificationResponse;

import jakarta.validation.Valid;

public interface UserService {

	VerificationResponse checkEmail(String email);

	OtpVerificationResponse verifyOtp(@Valid VerificationRequest request);

	void resetPassword(Long userId, String password);

}
