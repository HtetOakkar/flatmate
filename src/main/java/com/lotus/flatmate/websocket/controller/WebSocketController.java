package com.lotus.flatmate.websocket.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.lotus.flatmate.conversation.service.ConversationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

	private final ConversationService conversationService;
	
	@MessageMapping("/hello")
	public void greeting(@Payload String message, Principal principal) throws InterruptedException {
		Thread.sleep(1000);
		conversationService.handleMessage(principal, message);
	}
	
	@MessageMapping("/reply/{receiver}")
	public void reply(@Payload String message, Principal principal, @DestinationVariable String receiver) throws InterruptedException {
		Thread.sleep(1000);
		conversationService.handlePrivateMessage(principal , message, receiver);
	}
	
	
}
