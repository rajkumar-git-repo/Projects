package com.mli.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CIRiderOption {

	GOLD(1, "Gold"), SILVER(2, "Silver");

	private static final Map<Integer, String> lookup = new HashMap<>();
	private static final Map<String, CIRiderOption> labelTovalue = new HashMap<>();
	private static final Map<Integer, CIRiderOption> valueTolabel = new HashMap<>();

	static {
		for (CIRiderOption options : EnumSet.allOf(CIRiderOption.class)) {
			lookup.put(options.getValue(), options.getLabel());
		}

		for (CIRiderOption options : EnumSet.allOf(CIRiderOption.class)) {
			labelTovalue.put(options.getLabel(), options);
		}

		for (CIRiderOption options : EnumSet.allOf(CIRiderOption.class)) {
			valueTolabel.put(options.getValue(), options);
		}
	}

	private CIRiderOption(Integer value, String label) {
		this.label = label;
		this.value = value;
	}

	private String label;
	private Integer value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static String getLabel(Integer value) {
		return lookup.get(value);
	}

	public static CIRiderOption getRequestType(Integer value) {
		return valueTolabel.get(value);
	}

	public static CIRiderOption getRequestType(String label) {
		return labelTovalue.get(label);
	}

}
