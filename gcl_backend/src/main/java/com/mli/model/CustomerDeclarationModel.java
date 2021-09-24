package com.mli.model;

import org.hibernate.validator.constraints.NotEmpty;

public class CustomerDeclarationModel {

	@NotEmpty
	private String proposalNumber;
	@NotEmpty
	private String custFirstDeclaration;
	@NotEmpty
	private String custSecondDeclaration;
	@NotEmpty
	private String custFirstDeclarationValue;
	@NotEmpty
	private String custSecondDeclarationValue;
	
	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getCustFirstDeclaration() {
		return custFirstDeclaration;
	}

	public void setCustFirstDeclaration(String custFirstDeclaration) {
		this.custFirstDeclaration = custFirstDeclaration;
	}

	public String getCustSecondDeclaration() {
		return custSecondDeclaration;
	}

	public void setCustSecondDeclaration(String custSecondDeclaration) {
		this.custSecondDeclaration = custSecondDeclaration;
	}

	public String getCustFirstDeclarationValue() {
		return custFirstDeclarationValue;
	}

	public void setCustFirstDeclarationValue(String custFirstDeclarationValue) {
		this.custFirstDeclarationValue = custFirstDeclarationValue;
	}

	public String getCustSecondDeclarationValue() {
		return custSecondDeclarationValue;
	}

	public void setCustSecondDeclarationValue(String custSecondDeclarationValue) {
		this.custSecondDeclarationValue = custSecondDeclarationValue;
	}

	@Override
	public String toString() {
		return "CustomerDeclarationModel [proposalNumber=" + proposalNumber + ", custFirstDeclaration="
				+ custFirstDeclaration + ", custSecondDeclaration=" + custSecondDeclaration
				+ ", custFirstDeclarationValue=" + custFirstDeclarationValue + ", custSecondDeclarationValue="
				+ custSecondDeclarationValue + "]";
	}
}
