package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.mli.enums.OTPUserType;
import com.mli.enums.Status;

/**
 * @author Nikhilesh.Tiwari
 *
 */

@Entity
@Table(name = "otp_history")
public class OTPHistoryEntity extends BaseEntity {

	private static final long serialVersionUID = -8885439961409425122L;

	@Id
	@Column(name = "otp_history_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long otpId;

	@Column(name = "cont_no")
	private Long contNo;

	@Column(name = "otp")
	private Integer otp;

	@Column(name = "otp_status", length = 55)
	@Enumerated(EnumType.STRING)
	private Status otpStatus;

	@Column(name = "user_type")
	@Enumerated(EnumType.STRING)
	private OTPUserType otpUserType;

	@Column(name = "unique_token")
	private String uniqueToken;
	
	@Version
	@Column(name = "version")
	private long version;

	public Long getOtpId() {
		return otpId;
	}

	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}

	public Long getContNo() {
		return contNo;
	}

	public void setContNo(Long contNo) {
		this.contNo = contNo;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public String getUniqueToken() {
		return uniqueToken;
	}

	public void setUniqueToken(String uniqueToken) {
		this.uniqueToken = uniqueToken;
	}

	public Status getOtpStatus() {
		return otpStatus;
	}

	public void setOtpStatus(Status otpStatus) {
		this.otpStatus = otpStatus;
	}

	public OTPUserType getOtpUserType() {
		return otpUserType;
	}

	public void setOtpUserType(OTPUserType otpUserType) {
		this.otpUserType = otpUserType;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
