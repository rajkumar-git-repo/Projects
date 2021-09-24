package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum RelationshipWithAssured {

	SPOUSE(1, "Spouse"), PARENTS(2, "Parents"), SIBLING(3, "Sibling"), DAUGHTER(4, "Daughter"), SON(5, "Son"),
	GRANDPARENT(6, "Grandparent"), AUNT(7, "Aunt"), UNCLE(8, "Uncle"), GRANDDAUGHTER(9, "Granddaughter"),
	GRANDSON(10, "Grandson"), BROTHER_IN_LAW(11, "Brother-In-Law"),

	DAUGHTER_IN_LAW(12, "Daughter-In-Law"), FATHER_IN_LAW(13, "Father-In-Law"), MOTHER_IN_LAW(14, "Mother-In-Law"),
	COUSIN(15, "Cousin"), NEPHEW(16, "Nephew"), NIECE(17, "Niece"), SISTER_IN_LAW(18, "Sister-In-Law"),
	SON_IN_LAW(19, "Son-in-law"),
	OTHERS(20, "Other"), PARENT(21, "Parent");

	private int id;
	private String label;

	RelationshipWithAssured(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static RelationshipWithAssured getRelationshipWithAssured(String label) {
		if (label == null) {
			return null;
		}
		if (SPOUSE.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SPOUSE;
		} else if (PARENTS.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return PARENTS;
		} else if (SIBLING.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SIBLING;
		} else if (DAUGHTER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return DAUGHTER;
		} else if (SON.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SON;
		}

		else if (GRANDPARENT.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return GRANDPARENT;
		} else if (AUNT.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return AUNT;
		} else if (UNCLE.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return UNCLE;
		}

		else if (GRANDDAUGHTER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return GRANDDAUGHTER;
		} else if (GRANDSON.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return GRANDSON;
		} else if (BROTHER_IN_LAW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return BROTHER_IN_LAW;
		}

		else if (DAUGHTER_IN_LAW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return DAUGHTER_IN_LAW;
		} else if (FATHER_IN_LAW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return FATHER_IN_LAW;
		} else if (MOTHER_IN_LAW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return MOTHER_IN_LAW;
		}

		else if (COUSIN.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return COUSIN;
		} else if (NEPHEW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return NEPHEW;
		} else if (NIECE.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return NIECE;
		}

		else if (SISTER_IN_LAW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SISTER_IN_LAW;
		} else if (SON_IN_LAW.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SON_IN_LAW;
		} else if (OTHERS.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return OTHERS;
		} else if (PARENT.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return PARENT;
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
