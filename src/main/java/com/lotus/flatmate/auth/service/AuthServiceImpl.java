package com.lotus.flatmate.auth.service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.CodeVerificationResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.emailVerification.EmailVerification;
import com.lotus.flatmate.emailVerification.EmailVerificationRepository;
import com.lotus.flatmate.exception.AppException;
import com.lotus.flatmate.exception.RecordAlreadyExistException;
import com.lotus.flatmate.exception.RecordNotFoundException;
import com.lotus.flatmate.exception.VerificationCodeMismatchException;
import com.lotus.flatmate.role.entity.Role;
import com.lotus.flatmate.role.entity.RoleName;
import com.lotus.flatmate.role.repository.RoleRepository;
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
	
	private String generateOpt() {
		return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
	}
	
	@Override
	public VerificationResponse registerUser(UserDto userDto) {
		String otp = generateOpt();
		String mailBody = mailTemplate.verificationMailTemplate(userDto.getUsername(), otp);
		VerificationResponse response = new VerificationResponse();
		Optional<User> existingUserOpt = userRepository.findByEmail(userDto.getEmail());
		if (existingUserOpt.isPresent()) {
			User existingUser = existingUserOpt.get();
			if (existingUser.getEmailVerification().isVerified()) {
				throw new RecordAlreadyExistException("User Account with email '" + userDto.getEmail() + "' already exist!");
			} else {
				existingUser.getEmailVerification().setVerificationCode(otp);
				existingUser.setUsername(userDto.getUsername());
				existingUser.setPassword(userDto.getPassword());
				userRepository.save(existingUser);
			}
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
		
		mailService.sendEmail(userDto.getEmail(), "[" + otp + "] Verification code", mailBody);
		response.setEmail(userDto.getEmail());
		response.setMessage("Verification code was sent to " + userDto.getEmail() + ".");
		response.setStatus(true);
		return response;
	}
	@Override
	public CodeVerificationResponse verifyUser(@Valid VerificationRequest request) {
		EmailVerification emailVerification = emailVerificationRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RecordNotFoundException("User account with email '" + request.getEmail() + "' not found."));
		if (!emailVerification.getVerificationCode().equals(request.getCode())) {
			throw new VerificationCodeMismatchException("Verificaiton code mismatch.");
		}
		emailVerification.setVerified(true);
		emailVerification.setEmail(null);
		emailVerification.setVerificationCode(null);
		EmailVerification savedEmailVerification = emailVerificationRepository.save(emailVerification);
		User user = emailVerification.getUser();
		user.setEmail(request.getEmail());
		user.setEmailVerification(savedEmailVerification);
		userRepository.save(user);
		return new CodeVerificationResponse(true, "User successfully registered!");
	}
}
