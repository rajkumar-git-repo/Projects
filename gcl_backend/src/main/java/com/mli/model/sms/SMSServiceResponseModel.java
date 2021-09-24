package com.mli.model.sms;

public class SMSServiceResponseModel {

	private SMSResponseHeaderModel responseHeader;
	private SMSResponseBodyModel responseBody;

	public SMSResponseHeaderModel getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(SMSResponseHeaderModel responseHeader) {
		this.responseHeader = responseHeader;
	}

	public SMSResponseBodyModel getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(SMSResponseBodyModel responseBody) {
		this.responseBody = responseBody;
	}

}
