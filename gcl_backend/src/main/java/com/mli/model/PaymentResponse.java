package com.mli.model;

public class PaymentResponse {

	private String email;
	private String name;
	private String phone;
	private Double amount;
	private String paymentStatus;
	private Long paymentOn;
	private String txnId;
	private String txnDate;
	private String redirectUrl;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getPaymentOn() {
		return paymentOn;
	}

	public void setPaymentOn(Long paymentOn) {
		this.paymentOn = paymentOn;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	public String toString() {
		return "PaymentResponse [email=" + email + ", name=" + name + ", phone=" + phone + ", amount=" + amount
				+ ", paymentStatus=" + paymentStatus + ", paymentOn=" + paymentOn + ", txnId=" + txnId + ", txnDate="
				+ txnDate + ", redirectUrl=" + redirectUrl + "]";
	}
}
