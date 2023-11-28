package com.lotus.flatmate.configs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.lotus.flatmate.security.JwtTokenProvider;
import com.lotus.flatmate.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final ThreadLocal<Authentication> authenticationHolder = new ThreadLocal<>();
	private final JwtTokenProvider tokenProvider;

	public WebSocketConfig(JwtTokenProvider tokenProvider) {
		super();
		this.tokenProvider = tokenProvider;
	}

//	@Bean
//	AuthorizationManager<Message<?>> messageAuthorizationManager(
//			MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//
//		return messages.anyMessage().permitAll().build();
//	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket").setAllowedOriginPatterns("*").withSockJS();
		registry.addEndpoint("/socket").setAllowedOriginPatterns("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

				List<String> authorizations = accessor.getNativeHeader("Authorization");
				log.info("Authorizations: {}", authorizations);

				if (authorizations != null) {
					String accessToken = authorizations.get(0).replace("Bearer ", "");
					if (accessToken != null && tokenProvider.validateJwtToken(accessToken)) {
						String username = tokenProvider.getUserNameFromJwtToken(accessToken);
						Claims claims = tokenProvider.getClaims(accessToken);
						String roles = claims.get("roles", String.class);
						Long userId = Long.parseLong(claims.getId());
						List<String> authorityArray = Arrays.asList(roles.split(","));
						List<GrantedAuthority> authorities = authorityArray.stream()
								.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
						UserPrincipal userPrincipal = new UserPrincipal(userId, username, null, authorities);
						try {
							Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null,
									authorities);
							authenticationHolder.set(authentication);
							SecurityContextHolder.getContext().setAuthentication(authentication);
							log.info("Authenticated : {}", username);
							accessor.getSessionAttributes().put("username", username);
							accessor.setUser(authentication);
							accessor.setLeaveMutable(true);
							return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
						} catch (Exception e) {
							log.error("unauthorized...." + e.getMessage());
						}
					} else {
						log.error("Access denined");
					}
				}

				return message;
			}

		});
	}

}
