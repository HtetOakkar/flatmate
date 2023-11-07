package com.lotus.flatmate.message.dto;

import java.time.Instant;

import com.lotus.flatmate.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
	private Long id;
	private String message;
	private UserDto senderDto;
	private Instant createdAt;
	private Instant updatedAt;
}
