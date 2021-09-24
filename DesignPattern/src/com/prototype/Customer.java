package com.prototype;

import java.util.ArrayList;
import java.util.List;

public class Customer implements Cloneable{

	private List<String> names;
	
	public Customer() {
		names = new ArrayList<String>();
	}
	
	public Customer(List<String> names) {
		this.names = names;
	}
	
	public void  getCustomer() {
		names.add("A");
		names.add("B");
		names.add("C");
		names.add("D");
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}
	
	@Override
	public Object clone() {
		List<String> list = new ArrayList<String>();
		for(String name: this.names) {
			list.add(name);
		}
		return new Customer(list);
	}
	
}
