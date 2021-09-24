package com.mli.enums;

public enum LoginType {

	OTP(1, "OTP"), PASSWORD_AND_OTP(2, "PASSWORDANDOTP");
	private int id;
	private String label;

	LoginType(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static LoginType getLoginType(String label) {
		if (label == null) {
			return null;
		}

		if (OTP.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return OTP;
		} else if (PASSWORD_AND_OTP.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return PASSWORD_AND_OTP;
		}
		return null;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
