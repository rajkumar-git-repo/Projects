package com.mli.model.otp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OTPModel {

	private Header header;
	private GenerateOTPPayload payload;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public GenerateOTPPayload getPayload() {
		return payload;
	}

	public void setPayload(GenerateOTPPayload payload) {
		this.payload = payload;
	}

}
