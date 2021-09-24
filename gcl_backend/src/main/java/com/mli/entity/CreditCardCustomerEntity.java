package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.mli.enums.Status;

@Entity
@Table(name = "creditcard_customer")
public class CreditCardCustomerEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "creditcard_customer_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long creditCardCustomerId;
	@Version
	@Column(name = "version")
	private long version;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "phone")
	private Long phone;
	@Column(name = "email")
	private String email;
	@Column(name = "gender")
	private String gender;
	@Column(name = "dob")
	private String dob;
	@Column(name = "customer_id")
	private String customerId;
	@Column(name = "card_segment")
	private String cardSegment;
	@Column(name = "coverage")
	private String coverage;
	@Column(name = "premium")
	private String premium;
	@Column(name = "proposal_number")
	private String proposalNumber;
	@Column(name = "policy_holder_name")
	private String masterPolicyHolderName;
	@Column(name = "app_status")
	@Enumerated(EnumType.STRING)
	private Status appStatus;
	@Column(name = "paymnet_status")
	@Enumerated(EnumType.STRING)
	private Status paymentStatus;
	@Column(name = "app_step")
	@Enumerated(EnumType.STRING)
	private Status step;
	@Column(name = "cust_otp_verified_date")
	private Long custOtpVerifiedDate;
	@Column(name = "verification_timestamp")
	private Long sfvTimeStamp;
	@Column(name = "application_completion_date")
	private Long appCompletionDate;
	@Column(name = "verified_otp")
	private String verifiedOtp;
	@Column(name = "pdf_sent")
	private String pdfSent;
	@Column(name = "cust_first_declaration", length = 2000)
	private String custFirstDeclaration;
	@Column(name = "cust_second_declaration", length = 2000)
	private String custSecondDeclaration;
	@Column(name = "cust_first_declaration_value")
	private String custFirstDeclarationValue;
	@Column(name = "cust_second_declaration_value")
	private String custSecondDeclarationValue;
	@Column(name = "verification_reminder_count")
	private String verificationReminderCount;
	@Column(name = "payment_reminder_count")
	private String paymentReminderCount;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_detail_id")
	private SellerDetailEntity slrDtlId;

	public Long getCreditCardCustomerId() {
		return creditCardCustomerId;
	}

	public void setCreditCardCustomerId(Long creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCardSegment() {
		return cardSegment;
	}

	public void setCardSegment(String cardSegment) {
		this.cardSegment = cardSegment;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getMasterPolicyHolderName() {
		return masterPolicyHolderName;
	}

	public void setMasterPolicyHolderName(String masterPolicyHolderName) {
		this.masterPolicyHolderName = masterPolicyHolderName;
	}

	public Status getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(Status appStatus) {
		this.appStatus = appStatus;
	}

	public Status getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Status paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Status getStep() {
		return step;
	}

	public void setStep(Status step) {
		this.step = step;
	}
	
	public Long getCustOtpVerifiedDate() {
		return custOtpVerifiedDate;
	}

	public void setCustOtpVerifiedDate(Long custOtpVerifiedDate) {
		this.custOtpVerifiedDate = custOtpVerifiedDate;
	}
	

	public Long getSfvTimeStamp() {
		return sfvTimeStamp;
	}

	public void setSfvTimeStamp(Long sfvTimeStamp) {
		this.sfvTimeStamp = sfvTimeStamp;
	}

	public SellerDetailEntity getSlrDtlId() {
		return slrDtlId;
	}

	public void setSlrDtlId(SellerDetailEntity slrDtlId) {
		this.slrDtlId = slrDtlId;
	}
	
	public Long getAppCompletionDate() {
		return appCompletionDate;
	}

	public void setAppCompletionDate(Long appCompletionDate) {
		this.appCompletionDate = appCompletionDate;
	}

	public String getVerifiedOtp() {
		return verifiedOtp;
	}

	public void setVerifiedOtp(String verifiedOtp) {
		this.verifiedOtp = verifiedOtp;
	}

	public String getPdfSent() {
		return pdfSent;
	}

	public void setPdfSent(String pdfSent) {
		this.pdfSent = pdfSent;
	}

	public String getCustFirstDeclaration() {
		return custFirstDeclaration;
	}

	public void setCustFirstDeclaration(String custFirstDeclaration) {
		this.custFirstDeclaration = custFirstDeclaration;
	}

	public String getCustSecondDeclaration() {
		return custSecondDeclaration;
	}

	public void setCustSecondDeclaration(String custSecondDeclaration) {
		this.custSecondDeclaration = custSecondDeclaration;
	}

	public String getCustFirstDeclarationValue() {
		return custFirstDeclarationValue;
	}

	public void setCustFirstDeclarationValue(String custFirstDeclarationValue) {
		this.custFirstDeclarationValue = custFirstDeclarationValue;
	}

	public String getCustSecondDeclarationValue() {
		return custSecondDeclarationValue;
	}

	public void setCustSecondDeclarationValue(String custSecondDeclarationValue) {
		this.custSecondDeclarationValue = custSecondDeclarationValue;
	}

	public String getVerificationReminderCount() {
		return verificationReminderCount;
	}

	public void setVerificationReminderCount(String verificationReminderCount) {
		this.verificationReminderCount = verificationReminderCount;
	}

	public String getPaymentReminderCount() {
		return paymentReminderCount;
	}

	public void setPaymentReminderCount(String paymentReminderCount) {
		this.paymentReminderCount = paymentReminderCount;
	}

	@Override
	public String toString() {
		return "CreditCardCustomerEntity [creditCardCustomerId=" + creditCardCustomerId + ", version=" + version
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", email=" + email
				+ ", gender=" + gender + ", dob=" + dob + ", customerId=" + customerId + ", cardSegment=" + cardSegment
				+ ", coverage=" + coverage + ", premium=" + premium + ", proposalNumber=" + proposalNumber
				+ ", masterPolicyHolderName=" + masterPolicyHolderName + ", appStatus=" + appStatus + ", paymentStatus="
				+ paymentStatus + ", step=" + step + ", custOtpVerifiedDate=" + custOtpVerifiedDate + ", sfvTimeStamp="
				+ sfvTimeStamp + ", appCompletionDate=" + appCompletionDate + ", verifiedOtp=" + verifiedOtp
				+ ", pdfSent=" + pdfSent + ", custFirstDeclaration=" + custFirstDeclaration + ", custSecondDeclaration="
				+ custSecondDeclaration + ", custFirstDeclarationValue=" + custFirstDeclarationValue
				+ ", custSecondDeclarationValue=" + custSecondDeclarationValue + ", verificationReminderCount="
				+ verificationReminderCount + ", paymentReminderCount=" + paymentReminderCount + ", slrDtlId="
				+ slrDtlId + "]";
	}
}
