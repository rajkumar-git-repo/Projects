package com.mli.model;

import java.io.Serializable;

public class PageInfoModel implements Serializable {

    private String totalPages;
	private String totalElements;
	private String size;
	private String number;
	private String numberOfElements;

	public String getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}

	public String getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(String totalElements) {
		this.totalElements = totalElements;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(String numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	@Override
	public String toString() {
		return "PageInfoVM [totalpages=" + totalPages + ", totalElements=" + totalElements + ", size=" + size
				+ ", number=" + number + ", numberOfElements=" + numberOfElements + "]";
	}

}
