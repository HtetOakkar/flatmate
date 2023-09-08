package com.lotus.flatmate.util;

import org.springframework.stereotype.Component;

@Component
public class MailTemplate {

	public String verificationMailTemplate(String name, String code) {
		
		return "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "  <meta charset=\"UTF-8\">\r\n"
				+ "  <title>OTP Email</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "  <div style=\"background-color: #f6f6f6; padding: 20px;\">\r\n"
				+ "    <div style=\"background-color: #ffffff; padding: 20px; border-radius: 10px;\">\r\n"
				+ "      <h2>OTP Verification</h2>\r\n"
				+ "      <p>Dear "+ name + ",</p>\r\n"
				+ "      <p>Your One-Time Password (OTP) for verification is:</p>\r\n"
				+ "      <h3 style=\"background-color: #f5f5f5; padding: 10px; border-radius: 5px;\">" +code+"</h3>\r\n"
				+ "      <p>Please enter this OTP in the provided field to complete the verification process.</p>\r\n"
				+ "      <p>Thank you for using our service!</p>\r\n"
				+ "      <p>Best regards,</p>\r\n"
				+ "      <p>Flatmates team</p>\r\n"
				+ "    </div>\r\n"
				+ "  </div>\r\n"
				+ "</body>\r\n"
				+ "</html>";
	}
}
