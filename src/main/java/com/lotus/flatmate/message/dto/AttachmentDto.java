package com.lotus.flatmate.message.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class AttachmentDto {
	private Long id;
	private String fileName;
	private String fileType;
	private String fileBytes;
	private Instant createdAt;
	private Instant updatedAt;
}
