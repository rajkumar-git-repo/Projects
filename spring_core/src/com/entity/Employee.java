package com.entity;

public class Employee{

	private int id;
	private String name;
	private Address address;
	
	public Employee() {
		System.out.println("Employee default constructor...");
	}
	
	public Employee(int id, String name, Address address) {
		super();
		System.out.println("Employee 3 constructor...");
		this.id = id;
		this.name = name;
		this.address = address;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", address=" + address + "]";
	}

}
