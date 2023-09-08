package com.lotus.flatmate.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEmailRequest {
	@NotNull
	private String email;
}
