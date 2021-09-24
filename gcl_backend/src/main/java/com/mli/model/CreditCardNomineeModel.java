package com.mli.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class CreditCardNomineeModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String nomineeFirstName;
	@NotEmpty
	private String nomineeLastName;
	@NotEmpty
	private String nomineeDob;
	@NotEmpty
	private String nomineeGender;
	@NotEmpty
	private String relationshipWithAssured;
	private String appointeeFirstName;
	private String appointeeLastName;
	private String appointeeDob;
	private String appointeeGender;
	private String relationWithNominee;
	private boolean isAppointee;

	public String getNomineeFirstName() {
		return nomineeFirstName;
	}

	public void setNomineeFirstName(String nomineeFirstName) {
		this.nomineeFirstName = nomineeFirstName;
	}

	public String getNomineeLastName() {
		return nomineeLastName;
	}

	public void setNomineeLastName(String nomineeLastName) {
		this.nomineeLastName = nomineeLastName;
	}

	public String getNomineeDob() {
		return nomineeDob;
	}

	public void setNomineeDob(String nomineeDob) {
		this.nomineeDob = nomineeDob;
	}

	public String getNomineeGender() {
		return nomineeGender;
	}

	public void setNomineeGender(String nomineeGender) {
		this.nomineeGender = nomineeGender;
	}

	public String getRelationshipWithAssured() {
		return relationshipWithAssured;
	}

	public void setRelationshipWithAssured(String relationshipWithAssured) {
		this.relationshipWithAssured = relationshipWithAssured;
	}

	public String getAppointeeFirstName() {
		return appointeeFirstName;
	}

	public void setAppointeeFirstName(String appointeeFirstName) {
		this.appointeeFirstName = appointeeFirstName;
	}

	public String getAppointeeLastName() {
		return appointeeLastName;
	}

	public void setAppointeeLastName(String appointeeLastName) {
		this.appointeeLastName = appointeeLastName;
	}

	public String getAppointeeDob() {
		return appointeeDob;
	}

	public void setAppointeeDob(String appointeeDob) {
		this.appointeeDob = appointeeDob;
	}

	public String getAppointeeGender() {
		return appointeeGender;
	}

	public void setAppointeeGender(String appointeeGender) {
		this.appointeeGender = appointeeGender;
	}

	public String getRelationWithNominee() {
		return relationWithNominee;
	}

	public void setRelationWithNominee(String relationWithNominee) {
		this.relationWithNominee = relationWithNominee;
	}
	
	public boolean isAppointee() {
		return isAppointee;
	}

	public void setAppointee(boolean isAppointee) {
		this.isAppointee = isAppointee;
	}

	@Override
	public String toString() {
		return "CreditCardNomineeModel [nomineeFirstName=" + nomineeFirstName + ", nomineeLastName=" + nomineeLastName
				+ ", nomineeDob=" + nomineeDob + ", nomineeGender=" + nomineeGender + ", relationshipWithAssured="
				+ relationshipWithAssured + ", appointeeFirstName=" + appointeeFirstName + ", appointeeLastName="
				+ appointeeLastName + ", appointeeDob=" + appointeeDob + ", appointeeGender=" + appointeeGender
				+ ", relationWithNominee=" + relationWithNominee + ", isAppointee=" + isAppointee + "]";
	}
}
