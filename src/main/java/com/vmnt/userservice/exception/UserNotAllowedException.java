/**
 * 
 */
package com.vmnt.userservice.exception;

/**
 * @author vmntruong
 *
 */
public class UserNotAllowedException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public UserNotAllowedException(String message) {
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
