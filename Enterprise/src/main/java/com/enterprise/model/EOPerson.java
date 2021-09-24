package com.enterprise.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class EOPerson {

	@Id
	private int primarykey;
	private String firstName;
	private String lastName;
	@OneToMany(mappedBy = "eoPerson")
	private List<EOAddress> addresses;
	
	
	public int getPrimarykey() {
		return primarykey;
	}
	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<EOAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<EOAddress> addresses) {
		this.addresses = addresses;
	}
	
	@Override
	public String toString() {
		return "EOPerson [primarykey=" + primarykey + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", addresses=" + addresses + "]";
	} 

}
