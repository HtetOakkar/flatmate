package com.lotus.flatmate.auth.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVerificationResponse {
	private boolean success;
	private String message;
	public CodeVerificationResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
}
