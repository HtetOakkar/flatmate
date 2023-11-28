package com.lotus.flatmate.websocket.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.lotus.flatmate.security.JwtTokenProvider;
import com.lotus.flatmate.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpHandShakeInterceptor implements HandshakeInterceptor{

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			List<String> authHeaders = servletRequest.getHeaders().get("Authorization");
			if (authHeaders != null) {
				String accessToken = authHeaders.get(0).replace("Bearer ", "");
				if (accessToken != null) {
					if (tokenProvider.validateJwtToken(accessToken)) {
						String username = tokenProvider.getUserNameFromJwtToken(accessToken);
						Claims claims = tokenProvider.getClaims(accessToken);
						String roles = claims.get("roles", String.class);
						Long userId = Long.parseLong(claims.getId());
						List<String> authorityArray = Arrays.asList(roles.split(","));
						List<GrantedAuthority> authorities = authorityArray.stream()
								.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
						UserPrincipal userPrincipal = new UserPrincipal(userId, username, null, authorities);
						Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null,
								authorities);
						SecurityContextHolder.getContext().setAuthentication(authentication);
						attributes.put("username", username);
					}
				}
			}
			HttpSession session = servletRequest.getServletRequest().getSession();
			attributes.put("sessionId", session.getId());
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		log.info("Hello I'm in the after handshake interceptor.");

	}

}
