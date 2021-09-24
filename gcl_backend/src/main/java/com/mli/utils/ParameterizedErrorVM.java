package com.mli.utils;

import java.io.Serializable;
import java.util.List;

/**
 * View Model for sending a parameterized error message.
 */
public class ParameterizedErrorVM implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String exception;
	private final String exceptionMessage;
	private final List<String> errorCodeList;
	private final boolean success;

	public ParameterizedErrorVM(String exception, String message, List<String> errorCodeList) {
		this.success = false;
		this.exception = exception;
		this.exceptionMessage = message;
		this.errorCodeList = errorCodeList;
	}

	public String getException() {
		return exception;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public List<String> getErrorCodeList() {
		return errorCodeList;
	}

	public boolean isSuccess() {
		return success;
	}

}
