package com.mli.model;

import java.io.Serializable;


public class SFQCovidVaccineModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean vaccinated;
	private String firstDoseDate;
	private String secondDoseDate;
	private String vaccineName;
	private String declaration;
	
	public boolean isVaccinated() {
		return vaccinated;
	}
	public void setVaccinated(boolean vaccinated) {
		this.vaccinated = vaccinated;
	}
	public String getFirstDoseDate() {
		return firstDoseDate;
	}
	public void setFirstDoseDate(String firstDoseDate) {
		this.firstDoseDate = firstDoseDate;
	}
	public String getSecondDoseDate() {
		return secondDoseDate;
	}
	public void setSecondDoseDate(String secondDoseDate) {
		this.secondDoseDate = secondDoseDate;
	}
	public String getVaccineName() {
		return vaccineName;
	}
	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	@Override
	public String toString() {
		return "SFQCovidVaccineModel [isVaccinated=" + vaccinated + ", firstDoseDate=" + firstDoseDate
				+ ", SecondDoseDate=" + secondDoseDate + ", vaccineName=" + vaccineName + ", declaration=" + declaration
				+ "]";
	}
}
