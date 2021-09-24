package com.mli.modal.email;

public class MliEmailServiceModel {

	private RequestHeaderModel requestHeader;
	private MailRequestBodyModel requestBody;

	public RequestHeaderModel getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(RequestHeaderModel requestHeader) {
		this.requestHeader = requestHeader;
	}

	public MailRequestBodyModel getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(MailRequestBodyModel requestBody) {
		this.requestBody = requestBody;
	}

	@Override
	public String toString() {
		return "MliEmailServiceModel [requestHeader=" + requestHeader + ", requestBody=" + requestBody + "]";
	}
}
