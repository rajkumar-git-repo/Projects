package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum UserDesignation {

	DST(1, "DST"), DSA(2, "DSA"),CONNECTOR(3,"CONNECTOR");

	private int id;
	private String label;

	UserDesignation(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static UserDesignation getConnector(String label) {
		if (label == null) {
			return null;
		}

		if (DST.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return DST;
		} else if (DSA.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return DSA;
		} else if (CONNECTOR.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return CONNECTOR;
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
