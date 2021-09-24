package com.mli.exception;

public class FileFormatIsNotCorrectException extends StorageException {

	private static final long serialVersionUID = 1L;

	public FileFormatIsNotCorrectException(String message) {
        super(message);
    }

    public FileFormatIsNotCorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}