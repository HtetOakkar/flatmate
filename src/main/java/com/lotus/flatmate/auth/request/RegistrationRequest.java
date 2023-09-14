package com.lotus.flatmate.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
	@NotNull
	@NotBlank
	private String username;
	@NotNull
	@NotBlank
	private String email;
	@NotNull
	@NotBlank
	private String password;
	@JsonProperty("mobile_number")
	private String mobileNumber;
}
