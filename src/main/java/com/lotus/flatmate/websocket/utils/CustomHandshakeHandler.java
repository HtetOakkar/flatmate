package com.lotus.flatmate.websocket.utils;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomHandshakeHandler extends DefaultHandshakeHandler{
	
	
	
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		Principal principal = request.getPrincipal();
		if (principal == null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				principal = authentication;
				log.info("Principal: {}", principal.getName());
			} else {
				log.error("Authentication is null");
			}
		}
		return principal;
	}
}
