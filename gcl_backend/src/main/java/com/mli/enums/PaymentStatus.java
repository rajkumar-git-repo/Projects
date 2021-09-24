package com.mli.enums;

public enum PaymentStatus {
	
	SUCCESS("0300","SUCCESS" , "Successful Transaction"),
	FAILED("0399", "FAILED", "Failed Resposne received from bank"),
	CANCELLED("0392", "CANCELLED", "Transaction cancelled by user either in Bank Page or in PG Card /PG Bank  selection"),
	AWAITED("0396", "AWAITED", "Transaction response not received from Bank, Status Check on same Day"),
	ABORTED("0397", "ABORTED", "Transaction Response not received from Bank. Status Check on next Day"),
	REFUND_SUCCESS("0400", "REFUND_SUCCESS", "Refund Initiated Successfully"),
	REFUND_INPROGRESS("0401", "REFUND_INPROGRESS", "Refund in Progress"),
	REFUND_INITIATED("0402", "REFUND_INITIATED", "Instant Refund Initiated Successfully"),
	REFUND_FAILED("0499", "REFUND_FAILED", "Refund initiation failed"),
	TRANSACTION_NOT_FOUND("9999", "TRANSACTION_NOT_FOUND", "Transaction not found in PG");
	
	private String id;
	private String label;
	private String description;
	
	
	public static PaymentStatus getPaymentStatus(String id) {
		if (id == null) {
			return null;
		}
		if (SUCCESS.getId().equals(id)) {
			return SUCCESS;
		} else if (FAILED.getId().equals(id)) {
			return FAILED;
		} else if (CANCELLED.getId().equals(id)) {
			return CANCELLED;
		} else if (AWAITED.getId().equals(id)) {
			return AWAITED;
		} else if (ABORTED.getId().equals(id)) {
			return ABORTED;
		} else if (REFUND_SUCCESS.getId().equals(id)) {
			return REFUND_SUCCESS;
		} else if (REFUND_INPROGRESS.getId().equals(id)) {
			return REFUND_INPROGRESS;
		} else if (REFUND_INITIATED.getId().equals(id)) {
			return REFUND_INITIATED;
		} else if (REFUND_FAILED.getId().equals(id)) {
			return REFUND_FAILED;
		} else if (TRANSACTION_NOT_FOUND.getId().equals(id)) {
			return TRANSACTION_NOT_FOUND;
		}
		return null;
	}
	
	PaymentStatus(String id, String label, String description){
		this.id = id;
		this.label = label;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
