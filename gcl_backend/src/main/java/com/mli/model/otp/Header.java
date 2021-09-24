package com.mli.model.otp;

public class Header {
	private String soaCorrelationId;
	private String soaMsgVersion;
	private String soaAppId;

	public String getSoaCorrelationId() {
		return soaCorrelationId;
	}

	public void setSoaCorrelationId(String soaCorrelationId) {
		this.soaCorrelationId = soaCorrelationId;
	}

	public String getSoaMsgVersion() {
		return soaMsgVersion;
	}

	public void setSoaMsgVersion(String soaMsgVersion) {
		this.soaMsgVersion = soaMsgVersion;
	}

	public String getSoaAppId() {
		return soaAppId;
	}

	public void setSoaAppId(String soaAppId) {
		this.soaAppId = soaAppId;
	}

}
