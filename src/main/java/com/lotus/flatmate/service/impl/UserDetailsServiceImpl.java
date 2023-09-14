package com.lotus.flatmate.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lotus.flatmate.exception.NonVerifiedException;
import com.lotus.flatmate.exception.RecordNotFoundException;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RecordNotFoundException("User account with email '" + email +"' not found."));
		if (!user.getEmailVerification().isVerified() && user.getEmailVerification() == null) {
			throw new NonVerifiedException("You email is not verified yet. Please, verify your email first.");
		}
		return UserPrincipal.build(user);
	}

}
