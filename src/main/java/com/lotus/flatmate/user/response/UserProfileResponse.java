package com.lotus.flatmate.user.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lotus.flatmate.socialContact.response.SocialContactResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {
	private Long id;
	private String username;
	private String email;
	@JsonProperty("mobile_number")
	private String mobileNumber;
	@JsonProperty("profile_url")
	private String profileUrl;
	@JsonProperty("social_contacts")
	private List<SocialContactResponse> socialContacts;
}
