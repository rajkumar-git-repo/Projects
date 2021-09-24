package com.abstractfactory.sms;

import com.abstractfactory.factory.Notification;

public class PersonalSms implements Notification{

	@Override
	public void getNotification() {
		System.out.println("PERSONAL SMS NOTIFICATION");
		
	}
}
