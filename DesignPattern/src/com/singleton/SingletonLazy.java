package com.singleton;

public class SingletonLazy {

	private static Employee instance;
	
	private SingletonLazy() {}
	
	public static Employee getInstance() {
		if(instance == null) {
			instance = new Employee();
		}
		return instance;
	}
}
