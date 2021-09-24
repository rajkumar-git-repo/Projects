package com.mli.model;

import java.io.Serializable;

public class SFQCovidModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String covidFirstAnswer;
	private String covidSecondAnswer;
	private String covidThirdAnswer_a;
	private String covidThirdAnswer_b;

	private SFQCovidTestModel sfqCovidTestModel;
	private SFQCovidVaccineModel sfqCovidVaccineModel;

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

	public String getCovidThirdAnswer_a() {
		return covidThirdAnswer_a;
	}

	public void setCovidThirdAnswer_a(String covidThirdAnswer_a) {
		this.covidThirdAnswer_a = covidThirdAnswer_a;
	}

	public String getCovidThirdAnswer_b() {
		return covidThirdAnswer_b;
	}

	public void setCovidThirdAnswer_b(String covidThirdAnswer_b) {
		this.covidThirdAnswer_b = covidThirdAnswer_b;
	}

	public SFQCovidTestModel getSfqCovidTestModel() {
		return sfqCovidTestModel;
	}

	public void setSfqCovidTestModel(SFQCovidTestModel sfqCovidTestModel) {
		this.sfqCovidTestModel = sfqCovidTestModel;
	}

	public SFQCovidVaccineModel getSfqCovidVaccineModel() {
		return sfqCovidVaccineModel;
	}

	public void setSfqCovidVaccineModel(SFQCovidVaccineModel sfqCovidVaccineModel) {
		this.sfqCovidVaccineModel = sfqCovidVaccineModel;
	}

	@Override
	public String toString() {
		return "SFQCovidModel [covidFirstAnswer=" + covidFirstAnswer + ", covidSecondAnswer=" + covidSecondAnswer
				+ ", covidThirdAnswer_a=" + covidThirdAnswer_a + ", covidThirdAnswer_b=" + covidThirdAnswer_b
				+ ", sfqCovidTestModel=" + sfqCovidTestModel + ", sfqCovidVaccineModel=" + sfqCovidVaccineModel + "]";
	}

}
