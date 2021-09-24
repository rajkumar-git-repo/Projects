package com.singleton;

public class SingletonSynchronized {

	private static Employee instance;
	private SingletonSynchronized() {}
	
	public static synchronized Employee getInstance() {
		if(instance == null) {
			instance = new Employee();
		}
		return instance;
	}
}
