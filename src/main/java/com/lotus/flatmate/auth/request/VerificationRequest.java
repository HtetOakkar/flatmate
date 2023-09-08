package com.lotus.flatmate.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
	@NotBlank
	private String code;
	@NotNull
	private String email;
}
