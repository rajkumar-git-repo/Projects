package com.mli.model.sms;

public class SMSRequestBodyModel {

	private String appAccId;
	private String appAccPass;
	private String appId;
	private String msgTo;
	private String msgText;

	public String getAppAccId() {
		return appAccId;
	}

	public void setAppAccId(String appAccId) {
		this.appAccId = appAccId;
	}

	public String getAppAccPass() {
		return appAccPass;
	}

	public void setAppAccPass(String appAccPass) {
		this.appAccPass = appAccPass;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
}
