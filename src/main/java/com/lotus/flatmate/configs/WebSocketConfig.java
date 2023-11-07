package com.lotus.flatmate.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Bean
	ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxBinaryMessageBufferSize(1024000);
		return container;
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new WebSocketHandler(), "/socket").setAllowedOrigins("localhost:8080");
	}
	
	
	

}
