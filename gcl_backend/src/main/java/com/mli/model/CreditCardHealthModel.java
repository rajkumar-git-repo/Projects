package com.mli.model;

import java.io.Serializable;

public class CreditCardHealthModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String healthFirstAnswer;
	private String healthSecondAnswer;
	private String healthThirdAnswer;
	private String isSmoker;
	private String smokePerDay;
	private String declaration;
	private String appNumber;
	private CreditCardCovidModel creditCardCovidModel;

	public String getHealthFirstAnswer() {
		return healthFirstAnswer;
	}

	public void setHealthFirstAnswer(String healthFirstAnswer) {
		this.healthFirstAnswer = healthFirstAnswer;
	}

	public String getHealthSecondAnswer() {
		return healthSecondAnswer;
	}

	public void setHealthSecondAnswer(String healthSecondAnswer) {
		this.healthSecondAnswer = healthSecondAnswer;
	}

	public String getHealthThirdAnswer() {
		return healthThirdAnswer;
	}

	public void setHealthThirdAnswer(String healthThirdAnswer) {
		this.healthThirdAnswer = healthThirdAnswer;
	}

	public String getIsSmoker() {
		return isSmoker;
	}

	public void setIsSmoker(String isSmoker) {
		this.isSmoker = isSmoker;
	}

	public String getSmokePerDay() {
		return smokePerDay;
	}

	public void setSmokePerDay(String smokePerDay) {
		this.smokePerDay = smokePerDay;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	public CreditCardCovidModel getCreditCardCovidModel() {
		return creditCardCovidModel;
	}

	public void setCreditCardCovidModel(CreditCardCovidModel creditCardCovidModel) {
		this.creditCardCovidModel = creditCardCovidModel;
	}

	@Override
	public String toString() {
		return "CreditCardHealthModel [healthFirstAnswer=" + healthFirstAnswer + ", healthSecondAnswer="
				+ healthSecondAnswer + ", healthThirdAnswer=" + healthThirdAnswer + ", isSmoker=" + isSmoker
				+ ", smokePerDay=" + smokePerDay + ", declaration=" + declaration + ", appNumber=" + appNumber
				+ ", creditCardCovidModel=" + creditCardCovidModel + "]";
	}
}