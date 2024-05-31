package com.lotus.flatmate.conversation.service;

import java.security.Principal;

public interface ConversationService {
	void handleMessage(Principal principal, Object payload);

	void handlePrivateMessage(Principal principal, String message, String receiver);
}
