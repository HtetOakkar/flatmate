package com.lotus.flatmate.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationResponse {
	@JsonProperty("status")
	private boolean status;
	@JsonProperty("sent_to")
	private String email;
	@JsonProperty("message")
	private String message;
	public VerificationResponse(boolean status, String email, String message) {
		super();
		this.status = status;
		this.email = email;
		this.message = message;
	}
	public VerificationResponse() {
		super();
	}
	
	
}
