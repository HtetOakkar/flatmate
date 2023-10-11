package com.lotus.flatmate.refreshToken.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lotus.flatmate.model.exception.RecordNotFoundException;
import com.lotus.flatmate.refreshToken.dto.RefreshTokenDto;
import com.lotus.flatmate.refreshToken.entity.RefreshToken;
import com.lotus.flatmate.refreshToken.mapper.RefreshTokenMapper;
import com.lotus.flatmate.refreshToken.repository.RefreshTokenRepository;
import com.lotus.flatmate.user.entity.User;
import com.lotus.flatmate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	public RefreshTokenDto generateRefreshToken(Long userId, String deviceId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RecordNotFoundException("User with id '" + "' not found!"));
		Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByDeviceId(deviceId);
		String token = UUID.randomUUID().toString();
		RefreshToken refreshToken = new RefreshToken();
		
		if (refreshTokenOptional.isPresent()) {
			refreshToken = refreshTokenOptional.get();
		} else {
			refreshToken.setDeviceId(deviceId);
		}
		
		refreshToken.setRefreshToken(token);
		refreshToken.setUser(user);
		return refreshTokenMapper.mapToRefreshTokenDto(refreshTokenRepository.save(refreshToken));
	}

}
