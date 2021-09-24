package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "payment_detail")
public class PaymentEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "payment_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long paymentId;

	@NotNull
	@Column(name = "email")
	private String email;

	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "phone")
	private String phone;

	@NotNull
	@Column(name = "amount")
	private Double amount;

	@Column(name = "bank_code")
	private String bankCode;

	@NotNull
	@Column(name = "payment_status")
	private String paymentStatus;

	@NotNull
	@Column(name = "transaction_id")
	private String txnId;

	@Column(name = "payment_on")
	private Long paymentOn;

	@Column(name = "transaction_date")
	private String txnDate;

	@Column(name = "request_token")
	private String requestToken;
	
	@Column(name = "alias_name")
	private String aliasName;
	
	@Column(name = "bank_txn_id")
	private String bankTxnId;

	@OneToOne
	@NotNull
	@JoinColumn(name = "creditcard_customer_id")
	private CreditCardCustomerEntity creditCardCustomerId;

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

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

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Long getPaymentOn() {
		return paymentOn;
	}

	public void setPaymentOn(Long paymentOn) {
		this.paymentOn = paymentOn;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public CreditCardCustomerEntity getCreditCardCustomerId() {
		return creditCardCustomerId;
	}

	public void setCreditCardCustomerId(CreditCardCustomerEntity creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
	}
	
	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getBankTxnId() {
		return bankTxnId;
	}

	public void setBankTxnId(String bankTxnId) {
		this.bankTxnId = bankTxnId;
	}

	@Override
	public String toString() {
		return "PaymentEntity [paymentId=" + paymentId + ", email=" + email + ", name=" + name + ", phone=" + phone
				+ ", amount=" + amount + ", bankCode=" + bankCode + ", paymentStatus=" + paymentStatus + ", txnId="
				+ txnId + ", paymentOn=" + paymentOn + ", txnDate=" + txnDate + ", requestToken=" + requestToken
				+ ", aliasName=" + aliasName + ", bankTxnId=" + bankTxnId + ", creditCardCustomerId="
				+ creditCardCustomerId + "]";
	}
}
