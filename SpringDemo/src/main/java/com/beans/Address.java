package com.beans;

public class Address {

	private int id;
	private String state;
	private String country;
	
	public Address() {
         System.out.println("Address default constructor invoke....");
	}
	
	public Address(int id) {
		this.id = id;
		System.out.println("Address id constructor invoked.....");
	}
	
	public Address(String state) {
		this.state = state;
		System.out.println("Address state constructor invoked.....");
	}
	
	public Address(int id,String state) {
		this.id = id;
		this.state = state;
		System.out.println("Address id,state constructor invoked.....");
	}
	
	public Address(String state,String country) {
		this.state = state;
		this.country = country;
		System.out.println("Address state,country constructor invoked.....");
	}

	public Address(int id ,String state,String country) {
		this.id = id;
		this.state = state;
		this.country = country;
		System.out.println("Address id, state,country constructor invoked.....");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", state=" + state + ", country=" + country + "]";
	}
}
