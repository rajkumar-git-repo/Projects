package com.mli.exception;

public class AgeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AgeException(String message) {
		super(message);
	}

	public AgeException(String message, Throwable cause) {
		super(message, cause);
	}
}