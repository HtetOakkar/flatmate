package com.lotus.flatmate.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lotus.flatmate.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService{

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${mail.sender_name}")
	private String senderName;

	public MailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Async("TaskExecutor")
	@Override
	public void sendEmail(String to, String subject, String body) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(new InternetAddress(username, senderName));
			helper.setReplyTo(new InternetAddress(username, senderName));
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setPriority(1);
			helper.setText(body, true);
			log.info("Sending mail....");
			javaMailSender.send(mimeMessage);
			log.info("Mail sent.");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
