package com.lotus.flatmate.conversation.service;

import java.security.Principal;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface ConversationService {
	void handleMessage(Principal principal, Object payload);

	void handlePrivateMessage(StompHeaderAccessor headerAccessor, String message, String receiver);
}
