package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum SchemeType {

	LEVEL(1, "Level"), REDUCING(2, "Reducing");

	private int id;
	private String label;

	SchemeType(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static SchemeType getSchemeType(String label) {
		if (label == null) {
			return null;
		}

		if (LEVEL.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return LEVEL;
		} else if (REDUCING.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return REDUCING;
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
