package com.enterprise.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class EnterpriseWeb implements ServletContextListener{

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("******************Context Initialized*******************");
		
	}
	
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("******************Context Destroyed*******************");
	}
}
