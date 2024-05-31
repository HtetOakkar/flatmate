package com.lotus.flatmate.message.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AttachmentRequest {
	@JsonProperty(value = "file_type")
	private String fileType;
	@JsonProperty(value = "file_bytes")
	private String fileBytes;
	
}
