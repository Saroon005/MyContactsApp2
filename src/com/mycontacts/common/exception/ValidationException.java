package com.mycontacts.common.exception;

/**
 * Domain-level validation exception for UC-01.
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
