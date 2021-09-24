package com.mli.model.sms;

public class SMSGeneralConsumerInformationModel {
	private String messageVersion;
	private String consumerId;
	private String correlationId;

	public String getMessageVersion() {
		return messageVersion;
	}

	public void setMessageVersion(String messageVersion) {
		this.messageVersion = messageVersion;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

}
