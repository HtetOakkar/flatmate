package com.lotus.flatmate.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	private String email;
	private String password;
	@JsonProperty("device_id")
	private String deviceId;
}
