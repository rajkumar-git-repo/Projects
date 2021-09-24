package main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import business.BankBusiness;
import service.BankService;

public class TestAOP {

	static Logger log = Logger.getLogger(BankService.class.getName());

	public static void main(String[] args) {
		log.info("aaaaaaaaaaaaaaaaaaaaaaa");

		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		BankBusiness business = (BankBusiness) context.getBean("bank");
		business.msg();
		System.out.println(business.m());
		System.out.println(business.k());

	}
}
