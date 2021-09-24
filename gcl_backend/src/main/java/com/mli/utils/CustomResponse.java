package com.mli.utils;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public final class CustomResponse {

	private String status;
	private String message;
	private Object data;

	public CustomResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public CustomResponse(String status, String message, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
