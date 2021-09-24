package com.mli.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
	ROLE_ADMIN(1, "ADMIN"), ROLE_SELLER(2, "SELLER"), ROLE_CUSTOMER(3, "CUSTOMER");

	private static final Map<String, UserType> labelToType = new HashMap<String, UserType>();
	private static final Map<Integer, UserType> valueToUserType = new HashMap<Integer, UserType>();

	static {
		for (UserType userType : UserType.values()) {
			labelToType.put(userType.getLabel(), userType);
		}
		for (UserType userType : EnumSet.allOf(UserType.class)) {
			valueToUserType.put(userType.getValue(), userType);
		}
	}

	UserType(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

	private Integer value;
	private String label;

	@JsonValue
	public String toValue() {
		return this.label;
	}

	public Integer getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static UserType getUserType(String label) {
		return labelToType.get(label);
	}

	public static UserType getUserType(Integer value) {
		return valueToUserType.get(value);
	}
}
