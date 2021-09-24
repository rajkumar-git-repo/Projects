package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Entity
@Table(name = "mli_email")
public class MliEmailEntity extends BaseEntity {

	private static final long serialVersionUID = -7924081902773358056L;

	@Id
	@Column(name = "mli_email_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long mliEmailId;

	public Long getMliEmailId() {
		return mliEmailId;
	}

	public void setMliEmailId(Long mliEmailId) {
		this.mliEmailId = mliEmailId;
	}

	@NotNull
	@Column(name = "mail_id_to")
	private String mailIdTo;

	public String getMailIdTo() {
		return mailIdTo;
	}

	public void setMailIdTo(String mailIdTo) {
		this.mailIdTo = mailIdTo;
	}
	
	@Version
	@Column(name = "version")
	private long version;

	
	/*@NotNull
	@Column(name = "mail_id_cc")
	private String mailIdCc;

	@NotNull
	@Column(name = "mail_id_bcc")
	private String mailIdBcc;

	@NotNull
	@Column(name = "mail_subject")
	private String mailSubject;

	@NotNull
	@Column(name = "from_name")
	private String fromName;

	@NotNull
	@Column(name = "from_email")
	private String fromEmail;

	@NotNull
	@Column(name = "mail_body")
	private String mailBody;

	@NotNull
	@Column(name = "is_file_attached")
	private String isFileAttached;

	@NotNull
	@Column(name = "is_consolidate")
	private String isConsolidate;

	@NotNull
	@Column(name = "to")
	private String to;

	@NotNull
	@Column(name = "body_format")
	private String bodyFormat;

	@NotNull
	@Column(name = "request_time_stamp")
	private String requestTimestamp;

	@NotNull
	@Column(name = "file_attached", columnDefinition = "boolean default false")
	private boolean fileAttached;

	@NotNull
	@Column(name = "file_path")
	private String filePath;

	public Long getMliEmailId() {
		return mliEmailId;
	}

	public void setMliEmailId(Long mliEmailId) {
		this.mliEmailId = mliEmailId;
	}

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

	public void setFileAttached(boolean fileAttached) {
		this.fileAttached = fileAttached;
	}

	public String getIsConsolidate() {
		return isConsolidate;
	}

	public void setIsConsolidate(String isConsolidate) {
		this.isConsolidate = isConsolidate;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}*/

	
}
