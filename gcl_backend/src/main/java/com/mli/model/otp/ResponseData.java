package com.mli.model.otp;

public class ResponseData {

	private String soaStatusCode;
	private String soaMessage;
	private GenerateOTPPayload payload;

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

	public GenerateOTPPayload getPayload() {
		return payload;
	}

	public void setPayload(GenerateOTPPayload payload) {
		this.payload = payload;
	}

}
