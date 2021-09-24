package com.mli.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum Status {

	STEP0(0, "Step0"), STEP1(1, "Step1"), STEP2(2, "Step2"), STEP3(3, "Step3"), STEP4(4, "Step4"), STEP5(5, "Step5"),

	CURRENT_OTP(6, "Current OTP"), USED_OTP(7, "OTP Used"), EXPIRED_USED_OTP(8, "OTP Expired"), NOT_USED_OTP(9,"OTP Not Used"),

	APP_PENDING(10, "App Pending"), ACTIVE(11, "Active"), IN_ACTIVE(12, "In Active"), APP_COMPLETE(13, "App Complete"),NRI(14, "NRI Form Submit"),
	
	APP_SENT(15, "App Sent"),APP_VERIFIED(16, "App Verified"),APP_NOT_INTERESTED(17,"Not Interested"),APP_INTERESTED(18,"Interested"),PHYSICAL_FORM_VERIFICATION(19,"Physical Form Verification")
	,PAYMENT_PENDING(20,"Payment pending"),PAYMENT_SUCCESS(21,"Paymnet success"),PAYMENT_FAILED(22,"Paymnet failed"),
	AFL_PHYSICAL_FORM_VERIFICATION(23,"AFL Physical Form Verification");

	private static final Map<Integer, String> lookup = new HashMap<Integer, String>();
	private static final Map<String, Status> labelToStatus = new HashMap<String, Status>();
	private static final Map<Integer, Status> valueToStatus = new HashMap<Integer, Status>();

	static {
		for (Status status : EnumSet.allOf(Status.class)) {
			lookup.put(status.getValue(), status.getLabel());
		}
		for (Status status : EnumSet.allOf(Status.class)) {
			labelToStatus.put(status.getLabel(), status);
		}
		for (Status status : EnumSet.allOf(Status.class)) {
			valueToStatus.put(status.getValue(), status);
		}
	}

	private Integer value;
	private String label;

	Status(Integer valuesd, String label) {
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

	public static Status getStatus(Integer value) {
		return valueToStatus.get(value);
	}

	public static Status getStatus(String label) {
		return (label == null) ? null : labelToStatus.get(label.toUpperCase());
	}

}
