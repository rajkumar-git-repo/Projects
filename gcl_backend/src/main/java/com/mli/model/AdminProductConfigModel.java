package com.mli.model;

public class AdminProductConfigModel {

	private String bankName;
	private String isRiderEnable;
	private String isCovidEnable;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIsRiderEnable() {
		return isRiderEnable;
	}

	public void setIsRiderEnable(String isRiderEnable) {
		this.isRiderEnable = isRiderEnable;
	}

	public String getIsCovidEnable() {
		return isCovidEnable;
	}

	public void setIsCovidEnable(String isCovidEnable) {
		this.isCovidEnable = isCovidEnable;
	}

	@Override
	public String toString() {
		return "AdminProductConfigModel [bankName=" + bankName + ", isRiderEnable=" + isRiderEnable + ", isCovidEnable="
				+ isCovidEnable + "]";
	}

}
