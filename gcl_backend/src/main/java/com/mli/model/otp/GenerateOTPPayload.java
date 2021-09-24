package com.mli.model.otp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GenerateOTPPayload {

	private String stepId;
	private String unqTokenNo;
	private String otpCode;

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getUnqTokenNo() {
		return unqTokenNo;
	}

	public void setUnqTokenNo(String unqTokenNo) {
		this.unqTokenNo = unqTokenNo;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

}
