package com.lotus.flatmate.post.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserResponse {
	private Long id;
	private String username;
	@JsonProperty("mobile_number")
	private String mobileNumber;
	@JsonProperty("profile_url")
	private String profileUrl;
}
