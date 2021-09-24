package com.prototype;

import java.util.List;

public class Client {

	public static void main(String[] args) {
		Customer customer = new Customer();
		customer.getCustomer();
		System.out.println("BEFOR EDIT");
		System.out.println(customer.getNames());
		
		Customer cloneCust = (Customer)customer.clone();
		List<String> list = cloneCust.getNames();
		System.out.println(list);
		
		System.out.println("AFTER EDIT");
		list.add("E");
		list.add("F");
		System.out.println(list);
		list.remove("A");
		System.out.println(list);
		System.out.println(customer.getNames());
	}
}
