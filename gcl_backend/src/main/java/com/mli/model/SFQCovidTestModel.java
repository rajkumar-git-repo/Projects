package com.mli.model;

import java.io.Serializable;

public class SFQCovidTestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String covidFirstAnswer;
	private String covidSecondAnswer;
	private String covidThirdAnswer;
	private String covidFourthAnswer;

	public String getCovidFirstAnswer() {
		return covidFirstAnswer;
	}

	public void setCovidFirstAnswer(String covidFirstAnswer) {
		this.covidFirstAnswer = covidFirstAnswer;
	}

	public String getCovidSecondAnswer() {
		return covidSecondAnswer;
	}

	public void setCovidSecondAnswer(String covidSecondAnswer) {
		this.covidSecondAnswer = covidSecondAnswer;
	}

	public String getCovidThirdAnswer() {
		return covidThirdAnswer;
	}

	public void setCovidThirdAnswer(String covidThirdAnswer) {
		this.covidThirdAnswer = covidThirdAnswer;
	}

	public String getCovidFourthAnswer() {
		return covidFourthAnswer;
	}

	public void setCovidFourthAnswer(String covidFourthAnswer) {
		this.covidFourthAnswer = covidFourthAnswer;
	}

	@Override
	public String toString() {
		return "SFQCovidTestModel [covidFirstAnswer=" + covidFirstAnswer + ", covidSecondAnswer=" + covidSecondAnswer
				+ ", covidThirdAnswer=" + covidThirdAnswer + ", covidFourthAnswer=" + covidFourthAnswer + "]";
	}

}
