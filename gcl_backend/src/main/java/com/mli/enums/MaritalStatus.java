package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum MaritalStatus {

	SINGLE(1, "Single"), MARRIED(2, "Married"), WIDOWED(3, "Widowed"), DIVORCED(4, "Divorced");

	private int id;
	private String label;

	MaritalStatus(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static MaritalStatus getMaritalStatus(String label) {
		if (label == null) {
			return null;
		}

		if (SINGLE.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SINGLE;
		} else if (MARRIED.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return MARRIED;
		} else if (WIDOWED.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return WIDOWED;
		} else if (DIVORCED.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return DIVORCED;
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
