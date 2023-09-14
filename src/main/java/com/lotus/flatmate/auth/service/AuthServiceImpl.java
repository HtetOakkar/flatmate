package com.lotus.flatmate.auth.service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.CodeVerificationResponse;
import com.lotus.flatmate.auth.response.JwtAuthenticationResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.emailVerification.entity.EmailVerification;
import com.lotus.flatmate.emailVerification.repository.EmailVerificationRepository;
import com.lotus.flatmate.exception.AppException;
import com.lotus.flatmate.exception.RecordAlreadyExistException;
import com.lotus.flatmate.exception.RecordNotFoundException;
import com.lotus.flatmate.exception.VerificationCodeMismatchException;
import com.lotus.flatmate.refreshToken.entity.RefreshToken;
import com.lotus.flatmate.refreshToken.repository.RefreshTokenRepository;
import com.lotus.flatmate.role.entity.Role;
import com.lotus.flatmate.role.entity.RoleName;
import com.lotus.flatmate.role.repository.RoleRepository;
import com.lotus.flatmate.security.JwtTokenProvider;
import com.lotus.flatmate.service.MailService;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.entity.LoginProvider;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.repository.UserRepository;
import com.lotus.flatmate.util.MailTemplate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final MailService mailService;

	private final MailTemplate mailTemplate;

	private final EmailVerificationRepository emailVerificationRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtTokenProvider jwtTokenProvider;

	private String generateOpt() {
		return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
	}

	@Override
	public VerificationResponse registerUser(UserDto userDto) {
		String otp = generateOpt();
		String mailBody = mailTemplate.verificationMailTemplate(userDto.getUsername(), otp);
		VerificationResponse response = new VerificationResponse();
		Optional<EmailVerification> existingEmailVerificationOpt = emailVerificationRepository
				.findByEmail(userDto.getEmail());
		if (existingEmailVerificationOpt.isPresent()) {
			User existingUser = existingEmailVerificationOpt.get().getUser();
			existingUser.setUsername(userDto.getUsername());
			existingUser.setPassword(userDto.getPassword());
			existingUser.setMobileNumber(userDto.getMobileNumber());
			existingUser.getEmailVerification().setVerificationCode(otp);
			userRepository.save(existingUser);
		} else {
			Optional<User> existingUserOpt = userRepository.findByEmail(userDto.getEmail());

			if (existingUserOpt.isPresent()) {
				throw new RecordAlreadyExistException(
						"User Account with email '" + userDto.getEmail() + "' already exist!");
			} else {
				User user = new User();
				user.setUsername(userDto.getUsername());
				user.setPassword(userDto.getPassword());
				user.setMobileNumber(userDto.getMobileNumber());
				user.setLoginProvider(LoginProvider.EMAIL);
				EmailVerification emailVerification = new EmailVerification();
				emailVerification.setEmail(userDto.getEmail());
				emailVerification.setVerificationCode(otp);
				emailVerification.setUser(user);
				user.setEmailVerification(emailVerification);
				Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
						.orElseThrow(() -> new AppException("User Role not set."));
				user.setRoles(Collections.singletonList(userRole));
				userRepository.save(user);
			}
		}

		mailService.sendEmail(userDto.getEmail(), "Verification code", mailBody);
		response.setEmail(userDto.getEmail());
		response.setMessage("Verification code was sent to " + userDto.getEmail() + ".");
		response.setSuccess(true);
		return response;
	}

	@Override
	public CodeVerificationResponse verifyUser(@Valid VerificationRequest request) {
		EmailVerification emailVerification = emailVerificationRepository.findByEmail(request.getEmail()).orElseThrow(
				() -> new RecordNotFoundException("User account with email '" + request.getEmail() + "' not found."));
		if (!emailVerification.getVerificationCode().equals(request.getCode())) {
			throw new VerificationCodeMismatchException("Verification code mismatch.");
		}
		emailVerification.setVerified(true);
		emailVerification.setEmail(null);
		emailVerification.setVerificationCode(null);
		EmailVerification savedEmailVerification = emailVerificationRepository.save(emailVerification);
		User user = emailVerification.getUser();
		user.setEmail(request.getEmail());
		user.setEmailVerification(savedEmailVerification);
		user.setUpdatedAt(Instant.now());
		userRepository.save(user);
		return new CodeVerificationResponse(true, "User successfully registered!");
	}

	@Override
	public JwtAuthenticationResponse validateRefreshToken(@Valid String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RecordNotFoundException("Token does not exist or incorrect!"));
		User user = refreshToken.getUser();
		user.setUpdatedAt(Instant.now());
		userRepository.save(user);
		String jwtToken = jwtTokenProvider.generateRefreshToken(user);
		String newRefreshToken = UUID.randomUUID().toString();
		refreshToken.setRefreshToken(newRefreshToken);
		refreshTokenRepository.save(refreshToken);
		return new JwtAuthenticationResponse(jwtToken, new Date(new Date().getTime() + 86400000), newRefreshToken);
	}
}
