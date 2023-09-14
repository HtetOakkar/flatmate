package com.lotus.flatmate.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
	@JsonProperty("current_password")
	private String currentPassword;
	@JsonProperty("new_password")
	private String newPassword;
}
