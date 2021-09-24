
package com.mli.model.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class SaveUpdateResponse {
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	private String status;
	private String message;
	private String loanAppId;
	private Integer currentStep;
	private Integer code;
	private Map<String, Object> attributes;

	public SaveUpdateResponse() {
	}

	public SaveUpdateResponse(String status) {
		this.status = status;
	}

	public SaveUpdateResponse(String status, String message, String loanAppId) {
		this.status = status;
		this.message = message;
		this.loanAppId = loanAppId;
	}

	public SaveUpdateResponse(String status, String message, String loanAppId, Integer currentStep) {
		this.status = status;
		this.message = message;
		this.loanAppId = loanAppId;
		this.currentStep = currentStep;
	}

	public SaveUpdateResponse(String status, String message) {
		this.status = status;
		this.message = message;
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

	public String getLoanAppId() {
		return loanAppId;
	}

	public void setLoanAppId(String loanAppId) {
		this.loanAppId = loanAppId;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "SaveUpdateResponse [status=" + status + ", message=" + message + ", loanAppId=" + loanAppId
				+ ", currentStep=" + currentStep + ", code=" + code + "]";
	}

}
