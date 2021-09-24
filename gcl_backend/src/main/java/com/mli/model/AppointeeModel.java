package com.mli.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class AppointeeModel {

	private String appointeeFirstName;
	private String appointeeLastName;
	private String dateOfBirth;
	private String gender;
	private String relationWithAssured;
	private String relationWithAppointee;

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

	public String getRelationWithAssured() {
		return relationWithAssured;
	}

	public void setRelationWithAssured(String relationWithAssured) {
		this.relationWithAssured = relationWithAssured;
	}

	public String getRelationWithAppointee() {
		return relationWithAppointee;
	}

	public void setRelationWithAppointee(String relationWithAppointee) {
		this.relationWithAppointee = relationWithAppointee;
	}

}
