package com.mli.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class CustomerUpdateModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String data;
	private String custEmail;
	private CreditCardHealthModel creditCardHealthModel;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public CreditCardHealthModel getCreditCardHealthModel() {
		return creditCardHealthModel;
	}

	public void setCreditCardHealthModel(CreditCardHealthModel creditCardHealthModel) {
		this.creditCardHealthModel = creditCardHealthModel;
	}

	@Override
	public String toString() {
		return "CustomerUpdateModel [data=" + data + ", custEmail=" + custEmail + ", creditCardHealthModel="
				+ creditCardHealthModel + "]";
	}

}
