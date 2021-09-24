package com.mli.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class NomineeDetailsModel extends BaseModel {

	private static final long serialVersionUID = -2990901813955920982L;

	private String nomineeFirstName;
	private String nomineeLastName;
	private String dateOfBirth;
	private String gender;
	private String relationWithAssured;
	private String relationWithNominee;
	private Boolean isAppointee;
	private AppointeeModel appointeeDetails;

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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRelationWitHAssured() {
		return relationWithAssured;
	}

	public void setRelationWitHAssured(String relationWitHAssured) {
		this.relationWithAssured = relationWitHAssured;
	}

	public String getRelationWithNominee() {
		return relationWithNominee;
	}

	public void setRelationWithNominee(String relationWithNominee) {
		this.relationWithNominee = relationWithNominee;
	}

	public Boolean getIsAppointee() {
		return isAppointee;
	}

	public void setIsAppointee(Boolean isAppointee) {
		this.isAppointee = isAppointee;
	}

	public AppointeeModel getAppointeeDetails() {
		return appointeeDetails;
	}

	public void setAppointeeDetails(AppointeeModel appointeeDetails) {
		this.appointeeDetails = appointeeDetails;
	}

}
