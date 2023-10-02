package com.lotus.flatmate.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NonVerifiedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonVerifiedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NonVerifiedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NonVerifiedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NonVerifiedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NonVerifiedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
