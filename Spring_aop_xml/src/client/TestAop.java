package client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import target.BankBusiness;

public class TestAop {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BankBusiness target = (BankBusiness)context.getBean("proxy");
		System.out.println(target.deposit(1000, "sbi123"));
		
	}
}
