package com.lotus.flatmate.refreshToken.service;

import com.lotus.flatmate.refreshToken.dto.RefreshTokenDto;

public interface RefreshTokenService {

	RefreshTokenDto generateRefreshToken(Long id);

}
