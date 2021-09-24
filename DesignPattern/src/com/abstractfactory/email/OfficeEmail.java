package com.abstractfactory.email;

import com.abstractfactory.factory.Notification;

public class OfficeEmail implements Notification{

	@Override
	public void getNotification() {
		System.out.println("OFFICE EMAIL NOTIFICATION");
		
	}
}
