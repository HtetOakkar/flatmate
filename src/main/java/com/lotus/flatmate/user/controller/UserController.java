package com.lotus.flatmate.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.ApiResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.security.CurrentUser;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.request.CheckEmailRequest;
import com.lotus.flatmate.user.request.ResetPasswordRequest;
import com.lotus.flatmate.user.response.OtpVerificationResponse;
import com.lotus.flatmate.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	private final UserMapper userMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	//Forgot password first step
	@PostMapping("/check-email")
	public VerificationResponse checkMobileNumber(@Valid @RequestBody CheckEmailRequest request) {
		VerificationResponse response = userService.checkEmail(request.getEmail());
		return response;
	}
	
	//Forgot password second step
	@PutMapping("/verify-otp")
	public OtpVerificationResponse verifyOtp(@Valid @RequestBody VerificationRequest request) {
		OtpVerificationResponse response = userService.verifyOtp(request);
		return response;
	}
	
	@PutMapping("/reset-password")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ApiResponse resetPassword(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ResetPasswordRequest request) {
		userService.resetPassword(userPrincipal.getId(), passwordEncoder.encode(request.getPassword()));
		return new ApiResponse(true, "Your password has been successfully reset. You can now log in to your account using your new password.");
	}
	
	@GetMapping("/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getUserProfileDetail(@CurrentUser UserPrincipal userPrincipal) {
		return null;
	}
	
}
