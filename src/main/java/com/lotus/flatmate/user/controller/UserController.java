package com.lotus.flatmate.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.ApiResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.exception.PasswordMismatchException;
import com.lotus.flatmate.security.CurrentUser;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.service.ImageUploadService;
import com.lotus.flatmate.socialContact.dto.SocialContactDto;
import com.lotus.flatmate.socialContact.request.SocialContactRequest;
import com.lotus.flatmate.socialContact.service.SocialContactService;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.request.ChangeMobileNumberRequest;
import com.lotus.flatmate.user.request.ChangePasswordRequest;
import com.lotus.flatmate.user.request.ChangeUsernameRequest;
import com.lotus.flatmate.user.request.CheckEmailRequest;
import com.lotus.flatmate.user.request.ProfileUploadRequest;
import com.lotus.flatmate.user.request.ResetPasswordRequest;
import com.lotus.flatmate.user.response.OtpVerificationResponse;
import com.lotus.flatmate.user.response.UserProfileResponse;
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

	private final SocialContactService socialContactService;

	private final ImageUploadService imageUploadService;

	// Forgot password first step
	@PostMapping("/check-email")
	public VerificationResponse checkMobileNumber(@Valid @RequestBody CheckEmailRequest request) {
		VerificationResponse response = userService.checkEmail(request.getEmail());
		return response;
	}

	// Forgot password second step
	@PutMapping("/verify-otp")
	public OtpVerificationResponse verifyOtp(@Valid @RequestBody VerificationRequest request) {
		OtpVerificationResponse response = userService.verifyOtp(request);
		return response;
	}

	@PutMapping("/reset-password")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ApiResponse resetPassword(@CurrentUser UserPrincipal userPrincipal,
			@Valid @RequestBody ResetPasswordRequest request) {
		userService.resetPassword(userPrincipal.getId(), passwordEncoder.encode(request.getPassword()));
		return new ApiResponse(true,
				"Your password has been successfully reset. You can now log in to your account using your new password.");
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse getUserProfileDetail(@CurrentUser UserPrincipal userPrincipal) {
		UserDto userDto = userService.getProfileDetails(userPrincipal.getId());
		return userMapper.mapToProfileResponse(userDto);
	}

	@PostMapping("/me/social-contact")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> addSocialContacts(@CurrentUser UserPrincipal userPrincipal,
			@RequestBody SocialContactRequest request) {

		SocialContactDto socialContactDto = socialContactService.addSocialContacts(request, userPrincipal.getId());
		UserDto userDto = userService.getById(userPrincipal.getId());
		List<SocialContactDto> socialContactDtos = new ArrayList<>();
		if (userDto.getSocialContactDtos() != null) {
			for (SocialContactDto socialContactDto2 : userDto.getSocialContactDtos()) {
				if (!socialContactDto.getContactType().equals(socialContactDto2.getContactType())) {
					socialContactDtos.add(socialContactDto2);
				}
			}
		}

		socialContactDtos.add(socialContactDto);

		userDto.setSocialContactDtos(socialContactDtos);
		return ResponseEntity.ok(userMapper.mapToProfileResponse(userDto));
	}

	@PutMapping("/me/password")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ApiResponse changePassword(@CurrentUser UserPrincipal userPrincipal,
			@RequestBody ChangePasswordRequest request) {
		UserDto userDto = userService.getById(userPrincipal.getId());
		if (!passwordEncoder.matches(request.getCurrentPassword(), userDto.getPassword())) {
			throw new PasswordMismatchException("Current password is incorrect.");
		}
		userService.changePassword(passwordEncoder.encode(request.getNewPassword()), userPrincipal.getId());
		return new ApiResponse(true, "Password successfully changed.");
	}

	@PutMapping("/me/username")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse changeUsername(@CurrentUser UserPrincipal userPrincipal,
			@RequestBody ChangeUsernameRequest request) {
		UserDto userDto = userService.changeUsername(request.getUsername(), userPrincipal.getId());
		return userMapper.mapToProfileResponse(userDto);
	}

	@PutMapping("/me/mobile-number")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse changeMobileNumber(@CurrentUser UserPrincipal userPrincipal,
			@RequestBody ChangeMobileNumberRequest request) {
		UserDto userDto = userService.changeMobileNumber(request.getMobileNumber(), userPrincipal.getId());
		return userMapper.mapToProfileResponse(userDto);
	}

	@PostMapping("/me/profile")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse uploadUserProfilePhoto(@CurrentUser UserPrincipal userPrincipal,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "data") ProfileUploadRequest request) throws IOException {
		if (!image.isEmpty()) {
			String folderName = "profile_photos";
			if (request.getImageUrl() != null) {
				 String previousUrl = request.getImageUrl();
				 imageUploadService.deleteImage(previousUrl, folderName);
			}
			
			String profileUrl = imageUploadService.uploadImage(image, folderName);
			request.setImageUrl(profileUrl);

		}

		UserDto userDto = userService.uploadProfilePhoto(request, userPrincipal.getId());
		return userMapper.mapToProfileResponse(userDto);
	}
	
}
