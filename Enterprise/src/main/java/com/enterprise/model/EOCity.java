package com.enterprise.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EOCity {

	@Id
	private int primarykey;
	private String cityName;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private EOAddress eoAddress;
	
	@ManyToOne
	private EOState state;

	public int getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public EOAddress getEoAddress() {
		return eoAddress;
	}

	public void setEoAddress(EOAddress eoAddress) {
		this.eoAddress = eoAddress;
	}

	public EOState getState() {
		return state;
	}

	public void setState(EOState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "EOCity [primarykey=" + primarykey + ", cityName=" + cityName + ", eoAddress=" + eoAddress + ", state="
				+ state + "]";
	}

}
