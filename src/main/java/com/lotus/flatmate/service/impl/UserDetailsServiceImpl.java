package com.lotus.flatmate.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lotus.flatmate.emailVerification.entity.EmailVerification;
import com.lotus.flatmate.emailVerification.repository.EmailVerificationRepository;
import com.lotus.flatmate.model.exception.NonVerifiedException;
import com.lotus.flatmate.model.exception.RecordNotFoundException;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final EmailVerificationRepository emailVerificationRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByEmail(email);

//		if (!user.getEmailVerification().isVerified() && user.getEmailVerification() == null) {
//			throw new NonVerifiedException("You email is not verified yet. Please, verify your email first.");
//		}
		if (userOptional.isEmpty()) {
			EmailVerification emailVerification = emailVerificationRepository.findByEmail(email).orElseThrow(
					() -> new RecordNotFoundException("User account with email '" + email + "' does not exist!"));
			if (!emailVerification.isVerified()) {
				throw new NonVerifiedException("Your email is not verified. Please, verify your email first!");
			}
		}
		
		User user = userOptional.get();

		return UserPrincipal.build(user);

	}

}
