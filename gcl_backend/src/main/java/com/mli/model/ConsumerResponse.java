package com.mli.model;

public class ConsumerResponse {

	private String token;
	private String txnId;
	private String marchantId;
	private String salt;
	private String itemId;
	private String returnUrl;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getMarchantId() {
		return marchantId;
	}

	public void setMarchantId(String marchantId) {
		this.marchantId = marchantId;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	@Override
	public String toString() {
		return "ConsumerResponse [token=" + token + ", txnId=" + txnId + ", marchantId=" + marchantId + ", salt=" + salt
				+ ", itemId=" + itemId + ", returnUrl=" + returnUrl + "]";
	}
}
