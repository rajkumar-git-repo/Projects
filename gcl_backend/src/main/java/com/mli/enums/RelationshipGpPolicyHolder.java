package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum RelationshipGpPolicyHolder {

	BORROWER(1, "Borrower"), CO_BORROWER(2, "CoBorrower");

	private int id;
	private String label;

	RelationshipGpPolicyHolder(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static RelationshipGpPolicyHolder getRelationshipGpPolicyHolder(String label) {
		if (label == null) {
			return null;
		}

		if (BORROWER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return BORROWER;
		} else if (CO_BORROWER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return CO_BORROWER;
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
