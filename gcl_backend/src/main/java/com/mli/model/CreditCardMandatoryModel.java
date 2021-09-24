package com.mli.model;

public class CreditCardMandatoryModel {

	private String isMandatoryDeclaration;
	private String place;

	public String getIsMandatoryDeclaration() {
		return isMandatoryDeclaration;
	}

	public void setIsMandatoryDeclaration(String isMandatoryDeclaration) {
		this.isMandatoryDeclaration = isMandatoryDeclaration;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "CreditCardMandatoryModel [isMandatoryDeclaration=" + isMandatoryDeclaration + ", place=" + place + "]";
	}
}
