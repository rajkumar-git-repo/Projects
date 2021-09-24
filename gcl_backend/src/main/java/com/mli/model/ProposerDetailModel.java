package com.mli.model;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class ProposerDetailModel {

	private String proposerFirstName;
	private String proposerLastName;
	private String dateOfBirth;
	private String gender;
	private String relationWithAssured;
	private String relationWithProposer;

	public String getProposerFirstName() {
		return proposerFirstName;
	}

	public void setProposerFirstName(String proposerFirstName) {
		this.proposerFirstName = proposerFirstName;
	}

	public String getProposerLastName() {
		return proposerLastName;
	}

	public void setProposerLastName(String proposerLastName) {
		this.proposerLastName = proposerLastName;
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

	public String getRelationWithProposer() {
		return relationWithProposer;
	}

	public void setRelationWithProposer(String relationWithProposer) {
		this.relationWithProposer = relationWithProposer;
	}
}
