package com.lotus.flatmate.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationResponse {
	@JsonProperty("success")
	private boolean success;
	@JsonProperty("sent_to")
	private String email;
	@JsonProperty("message")
	private String message;
	public VerificationResponse(boolean success, String email, String message) {
		super();
		this.success = success;
		this.email = email;
		this.message = message;
	}
	public VerificationResponse() {
		super();
	}
	
	
}
