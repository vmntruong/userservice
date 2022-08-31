package com.vmnt.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author vmntruong
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InvalidInputException extends RuntimeException {
	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
}
