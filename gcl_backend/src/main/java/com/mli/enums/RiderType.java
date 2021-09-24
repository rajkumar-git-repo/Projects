package com.mli.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nikhilesh.tiwari
 *
 */
public enum RiderType {

	BASE_RIDER(1, "Base Benefit"), BASE_CI_RIDER(2, "Base + Critical Illness (Accelerated Benefit) Rider");

	private static final Map<Integer, String> lookup = new HashMap<>();
	private static final Map<String, RiderType> labelToStatus = new HashMap<>();
	private static final Map<Integer, RiderType> valueToStatus = new HashMap<>();

	static {
		for (RiderType riderType : EnumSet.allOf(RiderType.class)) {
			lookup.put(riderType.getValue(), riderType.getLabel());
		}
		for (RiderType riderType : EnumSet.allOf(RiderType.class)) {
			labelToStatus.put(riderType.getLabel(), riderType);
		}
		for (RiderType riderType : EnumSet.allOf(RiderType.class)) {
			valueToStatus.put(riderType.getValue(), riderType);
		}
	}

	private Integer value;
	private String label;

	private RiderType(Integer valuesd, String label) {
		this.value = valuesd;
		this.label = label;
	}

	public Integer getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static String getLabel(Integer value) {
		return lookup.get(value);
	}

	public static RiderType getRequestType(Integer value) {
		return valueToStatus.get(value);
	}

	public static RiderType getRequestType(String label) {
		return labelToStatus.get(label);
	}

}
