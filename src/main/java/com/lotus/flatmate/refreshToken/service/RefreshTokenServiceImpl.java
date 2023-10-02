package com.lotus.flatmate.refreshToken.service;

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
	public RefreshTokenDto generateRefreshToken(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RecordNotFoundException("User with id '" + "' not found!"));
		String token = UUID.randomUUID().toString();
		
		RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
		if (user.getRefreshToken() != null) {
			user.getRefreshToken().setRefreshToken(token);
			refreshTokenDto = refreshTokenMapper.mapToRefreshTokenDto(refreshTokenRepository.save(user.getRefreshToken()));
		} else {
			RefreshToken refreshToken = new RefreshToken();
			refreshToken.setRefreshToken(token);
			refreshToken.setUser(user);
			refreshTokenDto = refreshTokenMapper.mapToRefreshTokenDto(refreshTokenRepository.save(refreshToken));
		}
		return refreshTokenDto;
	}

}
