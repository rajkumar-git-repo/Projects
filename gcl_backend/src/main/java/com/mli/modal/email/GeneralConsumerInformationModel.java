package com.mli.modal.email;

public class GeneralConsumerInformationModel {

	private String messageVersion;
	private String appId;
	private String correlationId;

	public String getMessageVersion() {
		return messageVersion;
	}

	public void setMessageVersion(String messageVersion) {
		this.messageVersion = messageVersion;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String toString() {
		return "GeneralConsumerInformationModel [messageVersion=" + messageVersion + ", appId=" + appId
				+ ", correlationId=" + correlationId + "]";
	}
}
