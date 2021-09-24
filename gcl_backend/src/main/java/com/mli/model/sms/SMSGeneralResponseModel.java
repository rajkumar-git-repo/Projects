package com.mli.model.sms;

public class SMSGeneralResponseModel {

	private String correlationIdl;
	private String consumerId;
	private String appAccId;
	private String status;
	private String code;
	private String description;

	public String getCorrelationIdl() {
		return correlationIdl;
	}

	public void setCorrelationIdl(String correlationIdl) {
		this.correlationIdl = correlationIdl;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getAppAccId() {
		return appAccId;
	}

	public void setAppAccId(String appAccId) {
		this.appAccId = appAccId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
