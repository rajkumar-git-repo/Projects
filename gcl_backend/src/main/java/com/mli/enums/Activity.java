package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum Activity {

	REGENERATE_OTP(1, "RegenerateOTP"), GENERATE_OTP(2, "GenerateOTP"), UNSUCCESSFUL_LOGIN(3,
			"Unsuccessful login"), SUCCESSFUL_LOGIN(4, "Successful login");

	private int id;
	private String label;

	Activity(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static Activity getActivity(String label) {
		if (label == null) {
			return null;
		}

		if (REGENERATE_OTP.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return REGENERATE_OTP;
		} else if (GENERATE_OTP.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return GENERATE_OTP;
		} else if (UNSUCCESSFUL_LOGIN.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return UNSUCCESSFUL_LOGIN;
		} else if (SUCCESSFUL_LOGIN.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SUCCESSFUL_LOGIN;
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
