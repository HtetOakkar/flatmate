package com.lotus.flatmate.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
	@MessageMapping("/hello")
	@SendTo("/topic/hello")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String greeting() {
		return "hello";
	}
}
