package com.mli.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class SFQHealthDeclarationModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty
	private int height;
	@NotEmpty
	private int weight;
	private String healthFirstAnswer;
	private String healthSecondAnswer;
	private String healthThirdAnswer;
	private String healthFourthAnswer;
	private String healthFifthAnswer;
	private String healthSixthAnswer;
	private boolean smoker;
	private int smokePerDay;
	private SFQCovidModel sfqCovidModel;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

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

	public String getHealthFourthAnswer() {
		return healthFourthAnswer;
	}

	public void setHealthFourthAnswer(String healthFourthAnswer) {
		this.healthFourthAnswer = healthFourthAnswer;
	}

	public String getHealthFifthAnswer() {
		return healthFifthAnswer;
	}

	public void setHealthFifthAnswer(String healthFifthAnswer) {
		this.healthFifthAnswer = healthFifthAnswer;
	}

	public String getHealthSixthAnswer() {
		return healthSixthAnswer;
	}

	public void setHealthSixthAnswer(String healthSixthAnswer) {
		this.healthSixthAnswer = healthSixthAnswer;
	}
	
	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public int getSmokePerDay() {
		return smokePerDay;
	}

	public void setSmokePerDay(int smokePerDay) {
		this.smokePerDay = smokePerDay;
	}

	public SFQCovidModel getSfqCovidModel() {
		return sfqCovidModel;
	}

	public void setSfqCovidModel(SFQCovidModel sfqCovidModel) {
		this.sfqCovidModel = sfqCovidModel;
	}

	@Override
	public String toString() {
		return "SFQHealthDeclarationModel [height=" + height + ", weight=" + weight + ", healthFirstAnswer="
				+ healthFirstAnswer + ", healthSecondAnswer=" + healthSecondAnswer + ", healthThirdAnswer="
				+ healthThirdAnswer + ", healthFourthAnswer=" + healthFourthAnswer + ", healthFifthAnswer="
				+ healthFifthAnswer + ", healthSixthAnswer=" + healthSixthAnswer + ", isSmoker=" + smoker
				+ ", smokePerDay=" + smokePerDay + ", sfqCovidModel=" + sfqCovidModel + "]";
	}
}
