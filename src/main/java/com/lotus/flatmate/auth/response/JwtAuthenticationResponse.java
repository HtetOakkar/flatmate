package com.lotus.flatmate.auth.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
	private String token;
	private String type = "Bearer";
	private Date expiration;
	@JsonProperty("refresh_token")
	private String refreshToken;
	public JwtAuthenticationResponse(String token, Date expiration, String refreshToken) {
		this.token = token;
		this.expiration = expiration;
		this.refreshToken = refreshToken;
	}
	
	
}
