package com.lotus.flatmate.refreshToken.mapper;

import org.springframework.stereotype.Component;

import com.lotus.flatmate.refreshToken.dto.RefreshTokenDto;
import com.lotus.flatmate.refreshToken.entity.RefreshToken;

@Component
public class RefreshTokenMapperImpl implements RefreshTokenMapper{

	@Override
	public RefreshTokenDto mapToRefreshTokenDto(RefreshToken refreshToken) {
		RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
		refreshTokenDto.setId(refreshToken.getId());
		refreshTokenDto.setRefreshToken(refreshToken.getRefreshToken());
		return refreshTokenDto;
	}

}
