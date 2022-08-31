package com.vmnt.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vmnt.userservice.exception.InvalidInputException;
import com.vmnt.userservice.exception.UserNotAllowedException;
import com.vmnt.userservice.exception.UserNotFoundException;

/**
 * Manage controller exception
 * @author vmntruong
 *
 */
@RestControllerAdvice
public class UserControllerExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		final List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getHttpStatus(), request);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return handleExceptionInternal(ex, apiError, headers, apiError.getHttpStatus(), request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
									  "TypeMismatchException : " + ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	
	/**
	 * Handle UserNotAllowedException
	 * @param ex the exception
	 * @return a ResponseEntity instance
	 */
	@ExceptionHandler(value=UserNotAllowedException.class)
	public ResponseEntity<Object> handleUserNotAllowedException(final UserNotAllowedException ex) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
									  "UserNotAllowedException : " + ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	
	/**
	 * Handle UserNotFoundException
	 * @param ex the exception
	 * @return a ResponseEntity instance
	 */
	@ExceptionHandler(value=UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFoundException(final UserNotFoundException ex) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND,
									  "UserNotFoundException : " + ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(error);
	}
	
	/**
	 * Handle InvalidInputException
	 * @param ex the exception
	 * @return a ResponseEntity instance
	 */
	@ExceptionHandler(value=InvalidInputException.class)
	public ResponseEntity<Object> handleInvalidInputException(final InvalidInputException ex) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
									  "InvalidInputException : " + ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}
	
}
