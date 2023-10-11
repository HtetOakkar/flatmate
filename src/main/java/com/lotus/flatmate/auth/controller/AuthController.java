package com.lotus.flatmate.auth.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lotus.flatmate.auth.request.LoginRequest;
import com.lotus.flatmate.auth.request.RegistrationRequest;
import com.lotus.flatmate.auth.request.VerificationRequest;
import com.lotus.flatmate.auth.response.ApiResponse;
import com.lotus.flatmate.auth.response.CodeVerificationResponse;
import com.lotus.flatmate.auth.response.JwtAuthenticationResponse;
import com.lotus.flatmate.auth.response.VerificationResponse;
import com.lotus.flatmate.auth.service.AuthService;
import com.lotus.flatmate.model.exception.PasswordMismatchException;
import com.lotus.flatmate.refreshToken.dto.RefreshTokenDto;
import com.lotus.flatmate.refreshToken.service.RefreshTokenService;
import com.lotus.flatmate.security.JwtTokenProvider;
import com.lotus.flatmate.security.UserPrincipal;
import com.lotus.flatmate.user.dto.UserDto;
import com.lotus.flatmate.user.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserMapper userMapper;

	private final AuthService authService;

	private final AuthenticationManager authenticationManager;

	private final RefreshTokenService refreshTokenService;

	private final JwtTokenProvider tokenProvider;

	@PostMapping("/user/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {
		UserDto userDto = userMapper.mapToUserDto(request);
		VerificationResponse response = authService.registerUser(userDto);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/user/verify")
	public ResponseEntity<?> verifyUser(@Valid @RequestBody VerificationRequest request) {
		CodeVerificationResponse response = authService.verifyUser(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest request) {
		try {
			Date expiration = new Date(System.currentTimeMillis() + 86400 * 1000);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			RefreshTokenDto newRefreshToken = refreshTokenService.generateRefreshToken(userPrincipal.getId(), request.getDeviceId());
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwtToken = tokenProvider.generateJwtToken(authentication);
				return ResponseEntity
						.ok(new JwtAuthenticationResponse(jwtToken, expiration, newRefreshToken.getRefreshToken()));
			}
		} catch (BadCredentialsException e) {
			throw new PasswordMismatchException("Incorrect password!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(false, "Username or password incorrect."));
	}

	@GetMapping("/refresh-token")
	public JwtAuthenticationResponse getRefreshToken(@Valid @RequestParam String token) {
		return authService.validateRefreshToken(token);
	}
}
