package com.lotus.flatmate.websocket.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

	private final SimpMessageSendingOperations messageTemplate;

	@EventListener
	public void handleWebSocketDisconnectEventListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String message = messageFormatter(headerAccessor, "disconnected");
		Map<String, Object> payload = new HashMap<>();
		payload.put("message", message);
		messageTemplate.convertAndSend("/topic/hello", payload);
		log.info(message);
	}

	@EventListener
	public void handleWebSocketConnectEventListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String message = messageFormatter(headerAccessor, "connected");
		Map<String, Object> payload = new HashMap<>();
		payload.put("message", message);
		messageTemplate.convertAndSend("/topic/hello", payload);
		log.info(message);
	}

	@EventListener
	public void handleWebSocketSubscribeEventListener(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String message = messageFormatter(headerAccessor, "subscribed");
		Map<String, Object> payload = new HashMap<>();
		payload.put("message", message);
		messageTemplate.convertAndSend("/topic/hello", payload);
		log.info(message);
	}

	private String messageFormatter(StompHeaderAccessor stompHeaderAccessor, String event) {
		String username = stompHeaderAccessor.getSessionAttributes() == null ? "User"
				: (String) stompHeaderAccessor.getSessionAttributes().get("username");
		return String.format("%s %s.", username, event);
	}
}
