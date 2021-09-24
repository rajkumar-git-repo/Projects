package com.mli.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Covid_19Model {

	private String covidAnsOne;
	private String covidAnsTwo;
	private String covidAnsThree_a;
	private String covidAnsThree_b;
	private String covidAnsFour;
	private String covidAnsFive;
	private String covidDeclaration;

	public String getCovidAnsOne() {
		return covidAnsOne;
	}

	public void setCovidAnsOne(String covidAnsOne) {
		this.covidAnsOne = covidAnsOne;
	}

	public String getCovidAnsTwo() {
		return covidAnsTwo;
	}

	public void setCovidAnsTwo(String covidAnsTwo) {
		this.covidAnsTwo = covidAnsTwo;
	}

	public String getCovidAnsThree_a() {
		return covidAnsThree_a;
	}

	public void setCovidAnsThree_a(String covidAnsThree_a) {
		this.covidAnsThree_a = covidAnsThree_a;
	}

	public String getCovidAnsThree_b() {
		return covidAnsThree_b;
	}

	public void setCovidAnsThree_b(String covidAnsThree_b) {
		this.covidAnsThree_b = covidAnsThree_b;
	}

	public String getCovidAnsFour() {
		return covidAnsFour;
	}

	public void setCovidAnsFour(String covidAnsFour) {
		this.covidAnsFour = covidAnsFour;
	}

	public String getCovidAnsFive() {
		return covidAnsFive;
	}

	public void setCovidAnsFive(String covidAnsFive) {
		this.covidAnsFive = covidAnsFive;
	}

	public String getCovidDeclaration() {
		return covidDeclaration;
	}

	public void setCovidDeclaration(String covidDeclaration) {
		this.covidDeclaration = covidDeclaration;
	}

	@Override
	public String toString() {
		return "Covid_19Model [covidAnsOne=" + covidAnsOne + ", covidAnsTwo=" + covidAnsTwo + ", covidAnsThree_a="
				+ covidAnsThree_a + ", covidAnsThree_b=" + covidAnsThree_b + ", covidAnsFour=" + covidAnsFour
				+ ", covidAnsFive=" + covidAnsFive + ", covidDeclaration=" + covidDeclaration + "]";
	}
}
