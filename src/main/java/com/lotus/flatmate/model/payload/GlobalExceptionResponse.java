package com.lotus.flatmate.model.payload;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GlobalExceptionResponse {
	private HttpStatus status;
	private String message;
	private Instant timestamp;
}
