package com.mli.modal.email;

import java.util.List;

import com.mli.model.AttchDetailsModel;

public class MailRequestBodyModel {

	private String mailIdTo;
	private String mailIdCc;
	private String mailIdBcc;
	private String mailSubject;
	private String fromName;
	private String fromEmail;
	private String mailBody;
	private String isFileAttached;
	private List<AttchDetailsModel> attchDetails;
	private List<String> embeddedAttachments;
	private String isConsolidate;
	private Boolean isZip;
	private List<String> reqParam1;
	private String reqParam2;
	private String reqParam3;
	private String appId;
	private String appName;
	private String userType;
	private String userName;
	private String authenticationToken;
	private String channel;
	private String to;
	private String bodyFormat;
	private String requestTimestamp;
	private Boolean fileAttached;
	private Boolean consolidate;
	private Boolean pdf;
	private Boolean zip;

	public String getMailIdTo() {
		return mailIdTo;
	}

	public void setMailIdTo(String mailIdTo) {
		this.mailIdTo = mailIdTo;
	}

	public String getMailIdCc() {
		return mailIdCc;
	}

	public void setMailIdCc(String mailIdCc) {
		this.mailIdCc = mailIdCc;
	}

	public String getMailIdBcc() {
		return mailIdBcc;
	}

	public void setMailIdBcc(String mailIdBcc) {
		this.mailIdBcc = mailIdBcc;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getIsFileAttached() {
		return isFileAttached;
	}

	public void setIsFileAttached(String isFileAttached) {
		this.isFileAttached = isFileAttached;
	}

	public List<AttchDetailsModel> getAttchDetails() {
		return attchDetails;
	}

	public void setAttchDetails(List<AttchDetailsModel> attchDetails) {
		this.attchDetails = attchDetails;
	}

	public List<String> getEmbeddedAttachments() {
		return embeddedAttachments;
	}

	public void setEmbeddedAttachments(List<String> embeddedAttachments) {
		this.embeddedAttachments = embeddedAttachments;
	}

	public String getIsConsolidate() {
		return isConsolidate;
	}

	public void setIsConsolidate(String isConsolidate) {
		this.isConsolidate = isConsolidate;
	}

	public Boolean getIsZip() {
		return isZip;
	}

	public void setIsZip(Boolean isZip) {
		this.isZip = isZip;
	}

	public List<String> getReqParam1() {
		return reqParam1;
	}

	public void setReqParam1(List<String> reqParam1) {
		this.reqParam1 = reqParam1;
	}

	public String getReqParam2() {
		return reqParam2;
	}

	public void setReqParam2(String reqParam2) {
		this.reqParam2 = reqParam2;
	}

	public String getReqParam3() {
		return reqParam3;
	}

	public void setReqParam3(String reqParam3) {
		this.reqParam3 = reqParam3;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getBodyFormat() {
		return bodyFormat;
	}

	public void setBodyFormat(String bodyFormat) {
		this.bodyFormat = bodyFormat;
	}

	public String getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public Boolean getFileAttached() {
		return fileAttached;
	}

	public void setFileAttached(Boolean fileAttached) {
		this.fileAttached = fileAttached;
	}

	public Boolean getConsolidate() {
		return consolidate;
	}

	public void setConsolidate(Boolean consolidate) {
		this.consolidate = consolidate;
	}

	public Boolean getPdf() {
		return pdf;
	}

	public void setPdf(Boolean pdf) {
		this.pdf = pdf;
	}

	public Boolean getZip() {
		return zip;
	}

	public void setZip(Boolean zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "MailRequestBodyModel [mailIdTo=" + mailIdTo + ", mailIdCc=" + mailIdCc + ", mailIdBcc=" + mailIdBcc
				+ ", mailSubject=" + mailSubject + ", fromName=" + fromName + ", fromEmail=" + fromEmail + ", mailBody="
				+ mailBody + ", isFileAttached=" + isFileAttached + ", attchDetails=" + attchDetails
				+ ", embeddedAttachments=" + embeddedAttachments + ", isConsolidate=" + isConsolidate + ", isZip="
				+ isZip + ", reqParam1=" + reqParam1 + ", reqParam2=" + reqParam2 + ", reqParam3=" + reqParam3
				+ ", appId=" + appId + ", appName=" + appName + ", userType=" + userType + ", userName=" + userName
				+ ", authenticationToken=" + authenticationToken + ", channel=" + channel + ", to=" + to
				+ ", bodyFormat=" + bodyFormat + ", requestTimestamp=" + requestTimestamp + ", fileAttached="
				+ fileAttached + ", consolidate=" + consolidate + ", pdf=" + pdf + ", zip=" + zip + "]";
	}
}
