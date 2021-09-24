package com.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.beans.Address;

public class Application {

	public static void main(String[] args) {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("resource/applicationContext.xml");
		Address address = (Address) applicationContext.getBean("address1");
		System.out.println(address);
		((AbstractApplicationContext) applicationContext).registerShutdownHook();
	}
}
