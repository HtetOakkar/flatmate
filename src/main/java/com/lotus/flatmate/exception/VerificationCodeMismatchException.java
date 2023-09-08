package com.lotus.flatmate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VerificationCodeMismatchException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VerificationCodeMismatchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VerificationCodeMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public VerificationCodeMismatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public VerificationCodeMismatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public VerificationCodeMismatchException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
