package com.mli.model;


import javax.persistence.Column;

import org.hibernate.validator.constraints.NotEmpty;

import com.mli.enums.Status;

public class CreditCardCustomerModel {

	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@NotEmpty
	private Long phone;
	@NotEmpty
	private String email;
	@NotEmpty
	private String gender;
	@NotEmpty
	private String dob;
	@NotEmpty
	private String customerId;
	@NotEmpty
	private String cardSegment;
	@NotEmpty
	private String coverage;
	@NotEmpty
	private String premium;
	
	private String proposalNumber;
	private String masterPolicyHolderName;
	private String appStatus;
	private String paymentStatus;
	private Status step;
	private String verificationReminderCount;
	private String paymentReminderCount; 

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

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Status getStep() {
		return step;
	}

	public void setStep(Status step) {
		this.step = step;
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
		return "CreditCardCustomerModel [firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone
				+ ", email=" + email + ", gender=" + gender + ", dob=" + dob + ", customerId=" + customerId
				+ ", cardSegment=" + cardSegment + ", coverage=" + coverage + ", premium=" + premium
				+ ", proposalNumber=" + proposalNumber + ", masterPolicyHolderName=" + masterPolicyHolderName
				+ ", appStatus=" + appStatus + ", paymentStatus=" + paymentStatus + ", step=" + step + "]";
	}
}
