package com.enterprise.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EOAddress {

	@Id
	private int primarykey;
	private String addressLine1;
	private String addressLine2;
	private String country;
	
	@OneToMany(mappedBy = "eoAddress")
	private List<EOCity> cities;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private EOPerson eoPerson;

	public int getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<EOCity> getCities() {
		return cities;
	}

	public void setCities(List<EOCity> cities) {
		this.cities = cities;
	}

	public EOPerson getEoPerson() {
		return eoPerson;
	}

	public void setEoPerson(EOPerson eoPerson) {
		this.eoPerson = eoPerson;
	}

	@Override
	public String toString() {
		return "EOAddress [primarykey=" + primarykey + ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", country=" + country + ", cities=" + cities + ", eoPerson=" + eoPerson + "]";
	}

}
