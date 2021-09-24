package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum Nationality {

	INDIAN(1, "Indian"), NRI(2, "NRI"), OTHERS(3, "Others");

	private int id;
	private String label;

	Nationality(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static Nationality getNationality(String label) {
		if (label == null) {
			return null;
		}

		if (INDIAN.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return INDIAN;
		} else if (NRI.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return NRI;
		} else if (OTHERS.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return OTHERS;
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
