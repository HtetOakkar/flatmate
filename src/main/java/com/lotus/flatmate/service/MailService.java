package com.lotus.flatmate.service;

public interface MailService {
	void sendEmail(String to, String subject, String body);
}
