package com.mli.enums;


public enum CronJobType {

	EXCEL_TO_BANK(1, "Excel To Bank");

	private int id;
	private String label;

	CronJobType(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public static CronJobType getCronJobType(String label) {
		if (label == null) {
			return null;
		}

		if (EXCEL_TO_BANK.getLabel().toUpperCase().equals(label.toUpperCase())) {
			return EXCEL_TO_BANK;
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