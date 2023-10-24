package com.lotus.flatmate.user.service;

import java.util.List;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.request.ProfileUploadRequest;
import com.lotus.flatmate.user.response.OtpVerificationResponse;

import jakarta.validation.Valid;

public interface UserService {

	VerificationResponse checkEmail(String email);

	OtpVerificationResponse verifyOtp(@Valid VerificationRequest request);

	void resetPassword(Long userId, String password);

	UserDto getProfileDetails(Long id);

	UserDto getById(Long id);

	void changePassword(String newPassword, Long id);

	UserDto changeUsername(String username, Long id);

	UserDto changeMobileNumber(String mobileNumber, Long id);

	UserDto uploadProfilePhoto(ProfileUploadRequest request, Long id);

	List<UserDto> searchUsers(String key, Long currentUserId);

	VerificationResponse changeEmail(String email, Long userId);

	UserDto verifyEmail(VerificationRequest request, Long userId);

}
