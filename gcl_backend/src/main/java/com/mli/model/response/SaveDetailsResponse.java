package com.mli.model.response;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public class SaveDetailsResponse {
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	private String status;
	private String message;

	private String path;

	public SaveDetailsResponse() {
	}

	public SaveDetailsResponse(String status, String message, String path) {
		this.status = status;
		this.message = message;
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

}
