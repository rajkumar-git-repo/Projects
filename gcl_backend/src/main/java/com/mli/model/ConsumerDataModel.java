package com.mli.model;

import org.hibernate.validator.constraints.NotEmpty;

public class ConsumerDataModel {

	private String txnId;
	private String totalamount;
	private String accountNo;
	@NotEmpty
	private String consumerId;
	@NotEmpty
	private String consumerMobileNo;
	@NotEmpty
	private String consumerEmailId;
	private String debitStartDate;
	private String debitEndDate;
	private String maxAmount;
	private String amountType;
	private String frequency;
	private String cardNumber;
	private String expMonth;
	private String expYear;
	private String cvvCode;
	@NotEmpty
	private String proposalNumber;

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getConsumerMobileNo() {
		return consumerMobileNo;
	}

	public void setConsumerMobileNo(String consumerMobileNo) {
		this.consumerMobileNo = consumerMobileNo;
	}

	public String getConsumerEmailId() {
		return consumerEmailId;
	}

	public void setConsumerEmailId(String consumerEmailId) {
		this.consumerEmailId = consumerEmailId;
	}

	public String getDebitStartDate() {
		return debitStartDate;
	}

	public void setDebitStartDate(String debitStartDate) {
		this.debitStartDate = debitStartDate;
	}

	public String getDebitEndDate() {
		return debitEndDate;
	}

	public void setDebitEndDate(String debitEndDate) {
		this.debitEndDate = debitEndDate;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public String getCvvCode() {
		return cvvCode;
	}

	public void setCvvCode(String cvvCode) {
		this.cvvCode = cvvCode;
	}
	
	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	@Override
	public String toString() {
		return "ConsumerDataModel [txnId=" + txnId + ", totalamount=" + totalamount + ", accountNo=" + accountNo
				+ ", consumerId=" + consumerId + ", consumerMobileNo=" + consumerMobileNo + ", consumerEmailId="
				+ consumerEmailId + ", debitStartDate=" + debitStartDate + ", debitEndDate=" + debitEndDate
				+ ", maxAmount=" + maxAmount + ", amountType=" + amountType + ", frequency=" + frequency
				+ ", cardNumber=" + cardNumber + ", expMonth=" + expMonth + ", expYear=" + expYear + ", cvvCode="
				+ cvvCode + ", proposalNumber=" + proposalNumber + "]";
	}
}
