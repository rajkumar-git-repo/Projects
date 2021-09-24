package com.mli.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum MasterPolicyHolderName {

	YES(1, "Yes", "Yes Bank Ltd"),
	AXIS(2, "Axis", "Axis Bank Ltd"),
	YESBANKCC(3, "YesBankCC", "Yes Bank Credit Card");

	MasterPolicyHolderName(Integer value, String label, String description) {
		this.value = value;
		this.label = label;
		this.description = description;
	}

	public static final Map<Integer, String> lookup = new HashMap<Integer, String>();
	static {
		for (MasterPolicyHolderName masterPolicyHolderName : EnumSet.allOf(MasterPolicyHolderName.class)) {
			lookup.put(masterPolicyHolderName.getValue(), masterPolicyHolderName.getLabel());
		}

	}

	public static final List<MasterPolicyHolderName> masterPolicyHolders = new ArrayList<MasterPolicyHolderName>();
	static {
		for (MasterPolicyHolderName masterPolicyHolderName : EnumSet.allOf(MasterPolicyHolderName.class)) {
			masterPolicyHolders.add(masterPolicyHolderName);
		}

	}

	private int value;
	private String label;
	private String description;

	public static MasterPolicyHolderName getMasterPolicyHolder(String label) {
		if (label == null) {
			return null;
		}
		if (YES.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YES;
		} else if (AXIS.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return AXIS;
		} else if (YESBANKCC.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YESBANKCC;
		}
		return null;
	}

	public static MasterPolicyHolderName getMasterPolicyHolder(Integer value) {
		if (value == null) {
			return null;
		}
		if (YES.value == value) {
			return YES;
		}else if (AXIS.value == value) {
			return AXIS;
		}else if (YESBANKCC.value == value) {
			return YESBANKCC;
		}
		return null;
	}

	public static String getDescription(String label) {
		if (label == null) {
			return null;
		}
		if (YES.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YES.getDescription();
		}else if (AXIS.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return AXIS.getDescription();
		}else if (YESBANKCC.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YESBANKCC.getDescription();
		}
		return null;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
