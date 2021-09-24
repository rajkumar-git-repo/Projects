package com.singleton;

public class Client {

	public static void main(String[] args) {
		Employee emp1 = SingletonEager.getInstance();
		emp1.getMessage();
		
		Employee emp2 = SingletonLazy.getInstance();
		emp2.getMessage();
		
		Employee emp3 = SingletonSynchronized.getInstance();
		emp3.getMessage();
		
		Employee emp4 = SingletonDoubleChackSynchronized.getInstance();
		emp4.getMessage();
		
	}
}
