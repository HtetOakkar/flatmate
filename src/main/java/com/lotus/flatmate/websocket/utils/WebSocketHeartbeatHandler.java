package com.lotus.flatmate.websocket.utils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebSocketHeartbeatHandler extends WebSocketHandlerDecorator {
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	
	public WebSocketHeartbeatHandler(WebSocketHandler delegate) {
		super(delegate);
	}
	

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		scheduleHeartbeat(session);
	}

	private void scheduleHeartbeat(WebSocketSession session) {
		if (taskScheduler == null) {
            taskScheduler = new ThreadPoolTaskScheduler();
            taskScheduler.initialize();
        }
		taskScheduler.scheduleAtFixedRate(() -> {
			try {
				log.info("I'm in the scheduled task");
				session.sendMessage(new TextMessage(Instant.now().toString()));
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}, Instant.now(), Duration.ofMillis(5000));
	}


}
