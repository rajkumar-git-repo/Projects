package com.factory;

public class EmailNotification implements Notification{

	@Override
	public void getNotification() {
        System.out.println("Email Notification Executed");		
	}

}
