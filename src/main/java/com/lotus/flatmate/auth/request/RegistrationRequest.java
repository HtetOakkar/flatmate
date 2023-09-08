package com.lotus.flatmate.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
	private String username;
	private String email;
	private String password;
	@JsonProperty("mobile_number")
	private String mobileNumber;
}
