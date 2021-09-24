package com.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.entity.Address;
import com.entity.Employee;
import com.entity.Person;

public class ApplicationTest {

	public static void main(String[] args) {

		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("ci_applicationContext.xml");
		Employee employee = (Employee) applicationContext.getBean("emp");
		System.out.println(employee);

		/*
		 * Address address = (Address) applicationContext.getBean("address");
		 * System.out.println(address);
		 * 
		 * Person person = (Person) applicationContext.getBean("person");
		 * System.out.println(person);
		 */

		applicationContext.registerShutdownHook();

	}
}
