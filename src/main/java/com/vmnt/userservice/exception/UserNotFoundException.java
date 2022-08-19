package com.vmnt.userservice.exception;

/**
 * 
 * @author vmntruong
 *
 */
public class UserNotFoundException extends RuntimeException {
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public UserNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
