package com.singleton;

public class SingletonEager {

	private static Employee instance = new Employee();
	
	private SingletonEager() {
		
	}
	
	public static Employee getInstance() {
		return instance;
	}
}
