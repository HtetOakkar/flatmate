package com.lotus.flatmate.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.model.exception.RecordAlreadyExistException;
import com.lotus.flatmate.model.exception.RecordNotFoundException;
import com.lotus.flatmate.model.exception.VerificationCodeMismatchException;
import com.lotus.flatmate.security.JwtTokenProvider;
import com.lotus.flatmate.service.MailService;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.repository.UserRepository;
import com.lotus.flatmate.user.request.ProfileUploadRequest;
import com.lotus.flatmate.user.response.OtpVerificationResponse;
import com.lotus.flatmate.util.MailTemplate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSeviceImpl implements UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	private final MailService mailService;

	private final MailTemplate mailTemplate;

	private final JwtTokenProvider tokenProvider;

	private String generateOpt() {
		return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
	}

	@Override
	public VerificationResponse checkEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RecordNotFoundException("User does not exist with email '" + email + "'."));
		String otp = generateOpt();
		String mailBody = mailTemplate.verificationMailTemplate(user.getUsername(), otp);
		String subject = String.format("[%s] Verification code", otp);
		user.getEmailVerification().setVerificationCode(otp);
		userRepository.save(user);
		mailService.sendEmail(email, subject , mailBody);
		return new VerificationResponse(true, email, "Verification code sent to '" + email + "'.");
	}

	@Override
	public OtpVerificationResponse verifyOtp(@Valid VerificationRequest request) {

		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
				() -> new RecordNotFoundException("User does not exist with email '" + request.getEmail() + "'."));
		if (!user.getEmailVerification().getVerificationCode().equals(request.getCode())) {
			throw new VerificationCodeMismatchException("Verificaiton code mismatch.");
		}
		user.getEmailVerification().setVerificationCode(null);
		User verifiedUser = userRepository.save(user);
		String token = tokenProvider.generateRefreshToken(verifiedUser);
		return new OtpVerificationResponse(token, true);
	}

	@Override
	public void resetPassword(Long userId, String password) {
		User user = userRepository.findById(userId).get();
		user.setPassword(password);
		userRepository.save(user);
	}

	@Override
	public UserDto getProfileDetails(Long id) {
		User user = userRepository.findById(id).get();

		return userMapper.mapToUserDto(user);
	}

	@Override
	public UserDto getById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("User not found with id : " + id));
		return userMapper.mapToUserDto(user);
	}

	@Override
	public void changePassword(String newPassword, Long id) {
		User user = userRepository.findById(id).get();
		user.setPassword(newPassword);
		userRepository.save(user);
	}

	@Override
	public UserDto changeUsername(String username, Long id) {
		User user = userRepository.findById(id).get();
		user.setUsername(username);
		return userMapper.mapToUserDto(userRepository.save(user));
	}

	@Override
	public UserDto changeMobileNumber(String mobileNumber, Long id) {
		User user = userRepository.findById(id).get();
		user.setMobileNumber(mobileNumber);
		return userMapper.mapToUserDto(userRepository.save(user));
	}

	@Override
	public UserDto uploadProfilePhoto(ProfileUploadRequest request, Long id) {
		User user = userRepository.findById(id).get();
		user.setProfileUrl(request.getImageUrl());
		return userMapper.mapToUserDto(userRepository.save(user));
	}

	@Override
	public List<UserDto> searchUsers(String key, Long currentUserId) {
		List<User> users = userRepository.findByLike(key, currentUserId);
		return users.stream().map(userMapper::mapToUserDto).toList();
	}

	@Override
	public VerificationResponse changeEmail(String email, Long userId) {
		User user = userRepository.findById(userId).get();
		if (user.getEmail().equals(email)) {
			throw new RecordAlreadyExistException("This email is already linked with your account.");
		}
		boolean existByEmail = userRepository.existsByEmail(email);
		if (existByEmail) {
			throw new RecordAlreadyExistException("User account with email '" + email + "' already exists.");
		}

		String otp = generateOpt();
		String subject = String.format("[%s] Verification code", otp);
		String mailBody = mailTemplate.verificationMailTemplate(user.getUsername(), otp);
		user.getEmailVerification().setEmail(email);
		user.getEmailVerification().setVerificationCode(otp);
		user.getEmailVerification().setVerified(false);
		userRepository.save(user);
		mailService.sendEmail(email, subject, mailBody);
		return new VerificationResponse(true, email, "Verification code sent to '" + email + "'.");
	}

	@Override
	public UserDto verifyEmail(VerificationRequest request, Long userId) {
		User user = userRepository.findById(userId).get();
		if (!user.getEmailVerification().getEmail().equals(request.getEmail())) {
			throw new RecordNotFoundException("Email mismatch.");
		}
		if (!user.getEmailVerification().getVerificationCode().equals(request.getCode())) {
			throw new VerificationCodeMismatchException("Verification code mismatch.");
		}

		user.setEmail(request.getEmail());
		user.getEmailVerification().setEmail(null);
		user.getEmailVerification().setVerificationCode(null);
		user.getEmailVerification().setVerified(true);

		return userMapper.mapToUserDto(userRepository.save(user));
	}

}
