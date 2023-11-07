package com.lotus.flatmate.conversation.dto;

import java.time.Instant;
import java.util.List;

import com.lotus.flatmate.message.dto.MessageDto;
import com.lotus.flatmate.user.dto.UserDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationDto {
	private Long id;
	private UserDto senderDto;
	private UserDto receiverDto;
	private List<MessageDto> messageDtos;
	private Instant createdAt;
	private Instant updatedAt;
}
