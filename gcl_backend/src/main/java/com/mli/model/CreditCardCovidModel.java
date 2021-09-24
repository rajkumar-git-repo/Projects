package com.mli.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CreditCardCovidModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String covidDeclaration;
	private String comment;

	public String getCovidDeclaration() {
		return covidDeclaration;
	}

	public void setCovidDeclaration(String covidDeclaration) {
		this.covidDeclaration = covidDeclaration;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "CreditCardCovidModel [covidDeclaration=" + covidDeclaration + ", comment=" + comment + "]";
	}
}
