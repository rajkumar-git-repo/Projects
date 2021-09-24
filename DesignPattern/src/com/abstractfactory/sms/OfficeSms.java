package com.abstractfactory.sms;

import com.abstractfactory.factory.Notification;

public class OfficeSms implements Notification{

	@Override
	public void getNotification() {
		System.out.println("OFFICE SMS NOTIFICATION");
		
	}
}
