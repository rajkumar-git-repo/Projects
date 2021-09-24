package com.abstractfactory.email;

import com.abstractfactory.factory.Notification;

public class PersonalEmail implements Notification{

	@Override
	public void getNotification() {
		System.out.println("PERSONAL EMAIL NOTIFICATION");
		
	}

}
