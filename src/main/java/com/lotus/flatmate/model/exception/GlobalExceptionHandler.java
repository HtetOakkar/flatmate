package com.lotus.flatmate.model.exception;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lotus.flatmate.model.payload.GlobalExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<?> handlePasswordMismatchException(PasswordMismatchException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(NonVerifiedException.class)
	public ResponseEntity<?> handleNonVerifiedException(NonVerifiedException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.FORBIDDEN, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> handleAppException(AppException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(RecordAlreadyExistException.class)
	public ResponseEntity<?> handleRecordAlreadyExistException(RecordAlreadyExistException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.CONFLICT, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<?> handleRecordNotFoundException(RecordNotFoundException e) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.NOT_FOUND);
		response.put("message", e.getMessage());
		response.put("timestamp", new Date(new Date().getTime()));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(VerificationCodeMismatchException.class)
	public ResponseEntity<?> handleVerificationCodeMismatchException(VerificationCodeMismatchException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(UnauthorizedActionException.class)
	public ResponseEntity<?> handleUnauthorizedActionException(UnauthorizedActionException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
		GlobalExceptionResponse response = new GlobalExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage(), Instant.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
