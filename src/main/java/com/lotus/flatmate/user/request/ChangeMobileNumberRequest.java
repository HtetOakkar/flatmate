package com.lotus.flatmate.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeMobileNumberRequest {
	@JsonProperty("mobile_number")
	private String mobileNumber;
}
