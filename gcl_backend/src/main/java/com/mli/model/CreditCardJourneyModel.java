package com.mli.model;



import com.mli.model.otp.CustOTPVerificationModel;

public class CreditCardJourneyModel {

	private Integer steps;
	private String proposalNumber;
	private Long sfvTimeStamp;
	private Long sellerContNumber;
	private CreditCardCustomerModel creditCardCustomerModel;
	private CreditCardNomineeModel creditCardNomineeModel;
	private CreditCardHealthModel creditCardHealthModel;
	private CreditCardMandatoryModel creditCardMandatoryModel;
	private CustOTPVerificationModel custOTPVerificationModel;

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public Long getSfvTimeStamp() {
		return sfvTimeStamp;
	}

	public void setSfvTimeStamp(Long sfvTimeStamp) {
		this.sfvTimeStamp = sfvTimeStamp;
	}

	public Long getSellerContNumber() {
		return sellerContNumber;
	}

	public void setSellerContNumber(Long sellerContNumber) {
		this.sellerContNumber = sellerContNumber;
	}

	public CreditCardCustomerModel getCreditCardCustomerModel() {
		return creditCardCustomerModel;
	}

	public void setCreditCardCustomerModel(CreditCardCustomerModel creditCardCustomerModel) {
		this.creditCardCustomerModel = creditCardCustomerModel;
	}

	public CreditCardNomineeModel getCreditCardNomineeModel() {
		return creditCardNomineeModel;
	}

	public void setCreditCardNomineeModel(CreditCardNomineeModel creditCardNomineeModel) {
		this.creditCardNomineeModel = creditCardNomineeModel;
	}

	public CreditCardHealthModel getCreditCardHealthModel() {
		return creditCardHealthModel;
	}

	public void setCreditCardHealthModel(CreditCardHealthModel creditCardHealthModel) {
		this.creditCardHealthModel = creditCardHealthModel;
	}

	public CreditCardMandatoryModel getCreditCardMandatoryModel() {
		return creditCardMandatoryModel;
	}

	public void setCreditCardMandatoryModel(CreditCardMandatoryModel creditCardMandatoryModel) {
		this.creditCardMandatoryModel = creditCardMandatoryModel;
	}

	public CustOTPVerificationModel getCustOTPVerificationModel() {
		return custOTPVerificationModel;
	}

	public void setCustOTPVerificationModel(CustOTPVerificationModel custOTPVerificationModel) {
		this.custOTPVerificationModel = custOTPVerificationModel;
	}

	@Override
	public String toString() {
		return "CreditCardJourneyModel [steps=" + steps + ", proposalNumber=" + proposalNumber + ", sfvTimeStamp="
				+ sfvTimeStamp + ", sellerContNumber=" + sellerContNumber + ", creditCardCustomerModel="
				+ creditCardCustomerModel + ", creditCardNomineeModel=" + creditCardNomineeModel
				+ ", creditCardHealthModel=" + creditCardHealthModel + ", creditCardMandatoryModel="
				+ creditCardMandatoryModel + ", custOTPVerificationModel=" + custOTPVerificationModel + "]";
	}
}
