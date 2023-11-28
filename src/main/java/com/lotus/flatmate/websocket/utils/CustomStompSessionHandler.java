package com.lotus.flatmate.websocket.utils;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class CustomStompSessionHandler implements StompSessionHandler{

	

	public CustomStompSessionHandler() {
		
	}


	@Override
	public Type getPayloadType(StompHeaders headers) {
		return headers.getContentType().getClass();
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		log.info("your are here in handle frame ....");
//		conversationService.handleMessage(headers, payload);
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		StompFrameHandler frameHandler = new StompFrameHandler() {
			
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
//				conversationService.handleMessage(headers, payload);
			}
			
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return headers.getContentType().getClass();
			}
		};
		log.info("connnected");
		session.subscribe("/topic/hello", frameHandler);
		session.send("/app/hello", "Connected");
		
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		session.disconnect(headers);
		log.info(exception.getMessage());
		
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		log.error(exception.getMessage());
		
	}

	

}
