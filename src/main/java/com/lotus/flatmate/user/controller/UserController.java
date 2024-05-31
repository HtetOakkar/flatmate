package com.lotus.flatmate.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.ApiResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.model.exception.PasswordMismatchException;
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
import com.lotus.flatmate.user.response.UserDetailsResponse;
import com.lotus.flatmate.user.response.UserProfileResponse;
import com.lotus.flatmate.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	private final SocialContactService socialContactService;

	private final ImageUploadService imageUploadService;

	// Forgot password first step
	@PostMapping("/email")
	public VerificationResponse checkMobileNumber(@Valid @RequestBody CheckEmailRequest request) {
		return userService.checkEmail(request.getEmail());
	}

	// Forgot password second step
	@PatchMapping("/verify")
	public OtpVerificationResponse verifyOtp(@Valid @RequestBody VerificationRequest request) {
		return userService.verifyOtp(request);
	}

	@PatchMapping("/password")
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

	@DeleteMapping("/me/social-contact/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse deleteSocialContact(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long id) {
		socialContactService.deleteSocialContact(id, userPrincipal.getId());
		UserDto userDto = userService.getById(userPrincipal.getId());

		return userMapper.mapToProfileResponse(userDto);
	}

	@PatchMapping("/me/password")
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

	@PatchMapping("/me/username")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse changeUsername(@CurrentUser UserPrincipal userPrincipal,
			@RequestBody ChangeUsernameRequest request) {
		UserDto userDto = userService.changeUsername(request.getUsername(), userPrincipal.getId());
		return userMapper.mapToProfileResponse(userDto);
	}

	@PatchMapping("/me/mobile-number")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse changeMobileNumber(@CurrentUser UserPrincipal userPrincipal,
			@RequestBody ChangeMobileNumberRequest request) {
		UserDto userDto = userService.changeMobileNumber(request.getMobileNumber(), userPrincipal.getId());
		return userMapper.mapToProfileResponse(userDto);
	}

	@PostMapping(path = "/me/profile", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse uploadUserProfilePhoto(@CurrentUser UserPrincipal userPrincipal,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "data") ProfileUploadRequest request) throws IOException, ImageReadException {
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

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserDetailsResponse getUserDetails(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		UserDto userDto = userService.getById(id);
		UserDto currentUserDto = userService.getById(currentUser.getId());
		return userMapper.mapToUserDetailsResponse(userDto, currentUserDto);
	}

	@GetMapping("/search")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<UserDetailsResponse> searchUsers(@RequestParam(value = "k") String key,
			@CurrentUser UserPrincipal currentUser) {
		List<UserDto> userDtos = userService.searchUsers(key, currentUser.getId());
		UserDto currentUserDto = userService.getById(currentUser.getId());
		return userDtos.stream().map(userDto -> userMapper.mapToUserDetailsResponse(userDto, currentUserDto)).toList();
	}

	@PatchMapping("/me/email")
	@PreAuthorize("hasRole('ROLE_USER')")
	public VerificationResponse checkEmail(@CurrentUser UserPrincipal currentUser,
			@RequestBody CheckEmailRequest request) {
		return userService.changeEmail(request.getEmail(), currentUser.getId());
	}

	@PatchMapping("/me/email/verify")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse changeEmail(@CurrentUser UserPrincipal currentUser,
			@RequestBody VerificationRequest request) {
		UserDto userDto = userService.verifyEmail(request, currentUser.getId());
		return userMapper.mapToProfileResponse(userDto);
	}
}
