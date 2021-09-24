package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum OTPUserType {

	CUSTOMER(1, "Customer"), SELLER(2, "Seller"), BANK(3,"Bank"),UWT(4,"Under Writing Team"),VERIFIED(5,"Verified"),YBLCCCUSTOMER(6,"YBLCCCustomer"),
	YBLCCVERIFIED(7,"YBLCCVerified"),YBLCCBANK(8,"YBLCCBank");

	private int id;
	private String label;

	OTPUserType(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static OTPUserType getUserType(String label) {
		if (label == null) {
			return null;
		}
		if (CUSTOMER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return CUSTOMER;
		} else if (SELLER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return SELLER;
		} else if (BANK.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return BANK;
		} else if (YBLCCCUSTOMER.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YBLCCCUSTOMER;
		} else if (YBLCCVERIFIED.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YBLCCVERIFIED;
		} else if (YBLCCBANK.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return YBLCCBANK;
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