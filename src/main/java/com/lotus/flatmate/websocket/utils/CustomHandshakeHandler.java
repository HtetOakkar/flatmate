package com.lotus.flatmate.websocket.utils;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.lotus.flatmate.security.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomHandshakeHandler extends DefaultHandshakeHandler{
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		Principal principal = null;
		String token = request.getHeaders().getFirst("token");
		log.error("token : " + token);
		if (token != null) {
			token = token.replace("Bearer ", "");
			if (tokenProvider.validateJwtToken(token)) {
				String username = tokenProvider.getUserNameFromJwtToken(token);
				principal = new Principal() {
					
					@Override
					public String getName() {
						return username;
					}
				};
				
			}
		}
		
		
		return principal;
	}
}
