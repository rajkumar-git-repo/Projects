package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "mis_recipient",uniqueConstraints= @UniqueConstraint(columnNames={"mail_type", "mph_type"}))
public class MISRecipientEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "mis_recipient_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long misRecipientId;
	
	
	@Column(name = "mail_id",length = 5000)
	private String mailId;
	@NotNull
	@Column(name = "mph_type")
	private String mphType;
	@NotNull
	@Column(name = "mail_type")
	private String mailType;
	
	public MISRecipientEntity() {
		super();
	}
	
	public MISRecipientEntity(String mailId, String mphType, String mailType) {
		super();
		this.mailId = mailId;
		this.mphType = mphType;
		this.mailType = mailType;
	}

	public Long getMisRecipientId() {
		return misRecipientId;
	}

	public void setMisRecipientId(Long misRecipientId) {
		this.misRecipientId = misRecipientId;
	}

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
		return "MISRecipientEntity [misRecipientId=" + misRecipientId + ", mailId=" + mailId + ", mphType=" + mphType
				+ ", mailType=" + mailType + "]";
	}
}
