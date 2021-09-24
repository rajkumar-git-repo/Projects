package com.factory;

public class NotificationFactory {

	public static Notification getInstance(String type) {
		if(type.equals("email")) {
			return new EmailNotification();
		}else if(type.equals("sms")) {
			return new SmsNotification();
		}
		return null;
	}
}
