package com.mli.model;

import java.io.Serializable;

public class Coverage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String coverage;
	private String premium;
	
	
	public Coverage(String coverage, String premium) {
		super();
		this.coverage = coverage;
		this.premium = premium;
	}


	public String getCoverage() {
		return coverage;
	}


	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}


	public String getPremium() {
		return premium;
	}


	public void setPremium(String premium) {
		this.premium = premium;
	}


	@Override
	public String toString() {
		return "Coverage [coverage=" + coverage + ", premium=" + premium + "]";
	}
}

