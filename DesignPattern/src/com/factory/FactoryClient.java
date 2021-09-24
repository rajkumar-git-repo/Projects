package com.factory;

public class FactoryClient {

	public static void main(String[] args) {
		Notification  emailNotification  = NotificationFactory.getInstance("email");
		emailNotification.getNotification();
		
		Notification  smsNotification  = NotificationFactory.getInstance("sms");
		smsNotification.getNotification();
	}
}
