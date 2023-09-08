package com.lotus.flatmate.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpVerificationResponse {
	private String token;
	private boolean status;
}
