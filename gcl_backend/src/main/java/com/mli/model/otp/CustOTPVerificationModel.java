package com.mli.model.otp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mli.model.BaseModel;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class CustOTPVerificationModel extends BaseModel {

	private static final long serialVersionUID = 8868984507746848726L;

	private String Status;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
