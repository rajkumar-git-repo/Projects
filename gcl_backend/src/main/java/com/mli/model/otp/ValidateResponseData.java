package com.mli.model.otp;

public class ValidateResponseData {

	private String soaStatusCode;
	private String soaMessage;
	private ValidateOTPPayload payload;

	public String getSoaStatusCode() {
		return soaStatusCode;
	}

	public void setSoaStatusCode(String soaStatusCode) {
		this.soaStatusCode = soaStatusCode;
	}

	public String getSoaMessage() {
		return soaMessage;
	}

	public void setSoaMessage(String soaMessage) {
		this.soaMessage = soaMessage;
	}

	public ValidateOTPPayload getPayload() {
		return payload;
	}

	public void setPayload(ValidateOTPPayload payload) {
		this.payload = payload;
	}

}
