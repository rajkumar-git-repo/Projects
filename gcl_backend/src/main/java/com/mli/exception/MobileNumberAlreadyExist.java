package com.mli.exception;

public class MobileNumberAlreadyExist extends Exception {

	private static final long serialVersionUID = 1L;

	public MobileNumberAlreadyExist(String message) {
		super(message);
	}

	public MobileNumberAlreadyExist(String message, Throwable cause) {
		super(message, cause);
	}
}
