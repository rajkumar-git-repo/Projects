package com.mli.model.sms;

public class SMSMliSmsServiceModel {

	private SMSRequestHeaderModel requestHeader;
	private SMSRequestBodyModel requestBody;

	public SMSRequestHeaderModel getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(SMSRequestHeaderModel requestHeader) {
		this.requestHeader = requestHeader;
	}

	public SMSRequestBodyModel getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(SMSRequestBodyModel requestBody) {
		this.requestBody = requestBody;
	}

}
