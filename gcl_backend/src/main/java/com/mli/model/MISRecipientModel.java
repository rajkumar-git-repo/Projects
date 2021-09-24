package com.mli.model;

import org.hibernate.validator.constraints.NotBlank;

public class MISRecipientModel {

	private String mailId;
	@NotBlank
	private String mphType;
	@NotBlank
	private String mailType;

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getMphType() {
		return mphType;
	}

	public void setMphType(String mphType) {
		this.mphType = mphType;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	@Override
	public String toString() {
		return "MISRecipientModel [mailId=" + mailId + ", mphType=" + mphType + ", mailType=" + mailType + "]";
	}

}
