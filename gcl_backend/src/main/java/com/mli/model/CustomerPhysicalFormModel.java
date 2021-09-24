package com.mli.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.mli.enums.LoanType;

public class CustomerPhysicalFormModel {

	@NotBlank
	private String customerFirstName;
	
	@NotBlank
	private String customerLastName;
	
	@NotBlank
	private String loanAppNumber;

	@NotNull
	private Long sellerContactNo;

	@NotNull
	private Double sumAssured;

	@NotBlank
	private String dob;

	@NotBlank
	private String mph;

	@NotBlank
	private String loanType;
	
    private Double premiumAmount;
	
	private String proposalNumber;
	
	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getLoanAppNumber() {
		return loanAppNumber;
	}

	public void setLoanAppNumber(String loanAppNumber) {
		this.loanAppNumber = loanAppNumber;
	}

	public Long getSellerContactNo() {
		return sellerContactNo;
	}

	public void setSellerContactNo(Long sellerContactNo) {
		this.sellerContactNo = sellerContactNo;
	}

	public Double getSumAssured() {
		return sumAssured;
	}

	public void setSumAssured(Double sumAssured) {
		this.sumAssured = sumAssured;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMph() {
		return mph;
	}

	public void setMph(String mph) {
		this.mph = mph;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Double getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(Double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	
}
