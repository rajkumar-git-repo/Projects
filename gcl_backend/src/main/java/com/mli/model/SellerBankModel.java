package com.mli.model;

public class SellerBankModel {

	private String bankName;

	private String bankNameDesc;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNameDesc() {
		return bankNameDesc;
	}

	public void setBankNameDesc(String bankNameDesc) {
		this.bankNameDesc = bankNameDesc;
	}

	@Override
	public int hashCode(){
		int hashcode = 0;
		hashcode += bankName.hashCode();
		return hashcode;
	}
	@Override
	public boolean equals(Object obj){
		if (obj instanceof SellerBankModel) {
			SellerBankModel sellerBankModel = (SellerBankModel) obj;
			return (sellerBankModel.getBankName().equals(this.bankName));
		} else {
			return false;
		}
	}

}
