package com.mli.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.mli.enums.UserDesignation;

public class SellerDetailModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long sellerDtlId;

	private Long contactNo;

	private String sellerEmailId;

	private String sellerName;

	private UserDesignation connector;

	private String groupPolicyNumber;

	private Set<SellerBankModel> sellerBankDetails;

	private String sourceEmpCode;

	private String status;

	private String racLocationMapping;

	private String mliSalesManager;

	private String mliSMCode;

	private String mliRM;

	private String mliRMCode;

	private Long createdOn;

	private Long modifiedOn;

	private String lastLogIn;

	private String role;

	private Set<String> loanTypes;

	private String sellerBankName;

	private Set<LoanTypeSellerModal> LoanTypeSellerModal;

	private String mph;

	public Long getSellerDtlId() {
		return sellerDtlId;
	}

	public void setSellerDtlId(Long sellerDtlId) {
		this.sellerDtlId = sellerDtlId;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public String getSellerEmailId() {
		return sellerEmailId;
	}

	public void setSellerEmailId(String sellerEmailId) {
		this.sellerEmailId = sellerEmailId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public UserDesignation getConnector() {
		return connector;
	}

	public void setConnector(UserDesignation connector) {
		this.connector = connector;
	}

	public String getGroupPolicyNumber() {
		return groupPolicyNumber;
	}

	public void setGroupPolicyNumber(String groupPolicyNumber) {
		this.groupPolicyNumber = groupPolicyNumber;
	}

	public Set<SellerBankModel> getSellerBankDetails() {
		return sellerBankDetails;
	}

	public void setSellerBankDetails(Set<SellerBankModel> sellerBankDetails) {
		this.sellerBankDetails = sellerBankDetails;
	}

	public String getSourceEmpCode() {
		return sourceEmpCode;
	}

	public void setSourceEmpCode(String sourceEmpCode) {
		this.sourceEmpCode = sourceEmpCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRacLocationMapping() {
		return racLocationMapping;
	}

	public void setRacLocationMapping(String racLocationMapping) {
		this.racLocationMapping = racLocationMapping;
	}

	public String getMliSalesManager() {
		return mliSalesManager;
	}

	public void setMliSalesManager(String mliSalesManager) {
		this.mliSalesManager = mliSalesManager;
	}

	public String getMliSMCode() {
		return mliSMCode;
	}

	public void setMliSMCode(String mliSMCode) {
		this.mliSMCode = mliSMCode;
	}

	public String getMliRM() {
		return mliRM;
	}

	public void setMliRM(String mliRM) {
		this.mliRM = mliRM;
	}

	public String getMliRMCode() {
		return mliRMCode;
	}

	public void setMliRMCode(String mliRMCode) {
		this.mliRMCode = mliRMCode;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getLastLogIn() {
		return lastLogIn;
	}

	public void setLastLogIn(String lastLogIn) {
		this.lastLogIn = lastLogIn;
	}

	public Set<LoanTypeSellerModal> getLoanTypeSellerModal() {
		return LoanTypeSellerModal;
	}

	public void setLoanTypeSellerModal(Set<LoanTypeSellerModal> loanTypeSellerModal) {
		LoanTypeSellerModal = loanTypeSellerModal;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<String> getLoanTypes() {
		return loanTypes;
	}

	public void setLoanTypes(Set<String> loanTypes) {
		this.loanTypes = loanTypes;
	}

	public String getSellerBankName() {
		return sellerBankName;
	}

	public void setSellerBankName(String sellerBankName) {
		this.sellerBankName = sellerBankName;
	}

	public String getMph() {
		return mph;
	}

	public void setMph(String mph) {
		this.mph = mph;
	}

	@Override
	public String toString() {
		return "SellerDetailModel [sellerDtlId=" + sellerDtlId + ", contactNo=" + contactNo + ", sellerEmailId="
				+ sellerEmailId + ", sellerName=" + sellerName + ", connector=" + connector + ", groupPolicyNumber="
				+ groupPolicyNumber + ", sellerBankDetails=" + sellerBankDetails + ", sourceEmpCode=" + sourceEmpCode
				+ ", status=" + status + ", racLocationMapping=" + racLocationMapping + ", mliSalesManager="
				+ mliSalesManager + ", mliSMCode=" + mliSMCode + ", mliRM=" + mliRM + ", mliRMCode=" + mliRMCode
				+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", lastLogIn=" + lastLogIn + ", role="
				+ role + ", loanTypes=" + loanTypes + ", sellerBankName=" + sellerBankName + ", LoanTypeSellerModal="
				+ LoanTypeSellerModal + ", mph=" + mph + "]";
	}
}
