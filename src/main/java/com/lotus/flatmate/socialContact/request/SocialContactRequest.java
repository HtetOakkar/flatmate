package com.lotus.flatmate.socialContact.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lotus.flatmate.socialContact.entity.SocialContactType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialContactRequest {
	private String username;
	private String url;
	@JsonProperty("contact_type")
	private SocialContactType contactType;
}
