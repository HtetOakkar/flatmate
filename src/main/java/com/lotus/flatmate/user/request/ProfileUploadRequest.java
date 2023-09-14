package com.lotus.flatmate.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUploadRequest {
	@JsonProperty("image_url")
	private String imageUrl;
}
