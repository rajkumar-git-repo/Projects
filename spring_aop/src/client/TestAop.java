package client;

import org.springframework.aop.framework.ProxyFactoryBean;

import business.BankBusiness;
import service.BankServiceARA;
import service.BankServiceMBA;
import service.BankServiceMI;
import service.BankServiceTA;

public class TestAop {

	public static void main(String[] args) {
		BankBusiness business = new BankBusiness();
		BankServiceMBA advice1 = new BankServiceMBA();
		BankServiceARA advice2 = new BankServiceARA();
		BankServiceTA advice3 = new BankServiceTA();
		BankServiceMI advice4 = new BankServiceMI();
		
		ProxyFactoryBean proxy = new ProxyFactoryBean();
		proxy.setTarget(business);
		//proxy.addAdvice(advice1);
		//proxy.addAdvice(advice2);
		//proxy.addAdvice(advice3);
		proxy.addAdvice(advice4);
		BankBusiness proxyBusiness = (BankBusiness)proxy.getObject();
		int amount = proxyBusiness.deposit(1000, "sbi123");
		System.out.println("Amount:"+amount);
	}
}
