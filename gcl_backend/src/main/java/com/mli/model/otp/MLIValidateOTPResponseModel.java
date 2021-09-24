package com.mli.model.otp;

public class MLIValidateOTPResponseModel {

	private Header header;
	private ValidateResponseData responseData;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public ValidateResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(ValidateResponseData responseData) {
		this.responseData = responseData;
	}

}
