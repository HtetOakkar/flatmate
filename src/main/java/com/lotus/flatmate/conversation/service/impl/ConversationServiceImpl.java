package com.lotus.flatmate.conversation.service.impl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.lotus.flatmate.conversation.service.ConversationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationServiceImpl implements ConversationService {

	private final SimpMessageSendingOperations messageTemplate;

	@Override
	public void handleMessage(Principal principal, Object payload) {
		String username = principal.getName();
		Map<String, Object> messagePayload = new HashMap<>();
		messagePayload.put("sender", username);
		messagePayload.put("message", payload);
		messageTemplate.convertAndSend("/topic/hello", messagePayload);
	}

	@Override
	public void handlePrivateMessage(Principal principal, String message, String receiver) {
		String username = principal.getName();
		Map<String, Object> messagePayload = new HashMap<>();
		messagePayload.put("sender", username);
		messagePayload.put("message", message);
		String destination = String.format("/queue/%s/reply", receiver);
		try {
			log.info("sending message to {}", receiver);
			log.info("destination: {}",destination);
			messageTemplate.convertAndSend(destination, messagePayload);
		} catch (MessageDeliveryException e) {
			log.error(e.getMessage());
		}
	}

}
