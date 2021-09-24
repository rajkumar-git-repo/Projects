package com.singleton;

public class SingletonDoubleChackSynchronized {

	private static Employee instance;
	private SingletonDoubleChackSynchronized() {}
	
	public static synchronized Employee getInstance() {
		if(instance == null) {
			synchronized (SingletonDoubleChackSynchronized.class) {
				if(instance == null) {
					instance = new Employee();
				}
			}
		}
		return instance;
	}
}
