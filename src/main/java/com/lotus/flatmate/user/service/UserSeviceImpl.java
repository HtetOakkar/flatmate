package com.lotus.flatmate.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.exception.RecordNotFoundException;
import com.lotus.flatmate.exception.VerificationCodeMismatchException;
import com.lotus.flatmate.security.JwtTokenProvider;
import com.lotus.flatmate.service.MailService;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.mapper.UserMapper;
import com.lotus.flatmate.user.repository.UserRepository;
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
		
		user.getEmailVerification().setVerificationCode(otp);
		userRepository.save(user);
		mailService.sendEmail(email, "[" + otp + "] Verification code", mailBody);
		return new VerificationResponse(true, email, "Verification code sent to '" + email + "'.");
	}

	@Override
	public OtpVerificationResponse verifyOtp(@Valid VerificationRequest request) {
		
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RecordNotFoundException("User does not exist with email '" + request.getEmail() + "'."));
		if (!user.getEmailVerification().getVerificationCode().equals(request.getCode())) {
			throw new VerificationCodeMismatchException("Verificaiton code mismatch.");
		} 
		String token = tokenProvider.generateRefreshToken(user);
		return new OtpVerificationResponse(token, true);
	}

	@Override
	public void resetPassword(Long userId, String password) {
		User user = userRepository.findById(userId).get();
		user.setPassword(password);
		userRepository.save(user);
	}

}
