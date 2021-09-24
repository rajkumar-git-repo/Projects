package com.factory;

public class SmsNotification implements Notification{

	@Override
	public void getNotification() {
        System.out.println("SMS Notification Executed");		
	}
}
