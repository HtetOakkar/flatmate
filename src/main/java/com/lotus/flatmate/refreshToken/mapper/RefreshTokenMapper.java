package com.lotus.flatmate.refreshToken.mapper;

import com.lotus.flatmate.refreshToken.dto.RefreshTokenDto;
import com.lotus.flatmate.refreshToken.entity.RefreshToken;

public interface RefreshTokenMapper {

	RefreshTokenDto mapToRefreshTokenDto(RefreshToken refreshToken);

}
