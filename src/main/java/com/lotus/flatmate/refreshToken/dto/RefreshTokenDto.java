package com.lotus.flatmate.refreshToken.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {
	private Long id;
	private String refreshToken;
}
