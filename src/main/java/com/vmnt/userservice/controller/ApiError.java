package com.vmnt.userservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ApiError {
	private HttpStatus httpStatus;
	private LocalDateTime timestamp;
	private String message;
	
	public ApiError(HttpStatus httpStatus, String message) {
		super();
		this.httpStatus = httpStatus;
		this.timestamp = LocalDateTime.now();
		this.message = message;
	}
	
	private ApiError() {
		this.timestamp = LocalDateTime.now();
    }
    
    public ApiError(final HttpStatus status, final List<String> errors) {
        this();
        this.httpStatus = status;
        this.message = String.join("\n", errors);;
    }

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

}
