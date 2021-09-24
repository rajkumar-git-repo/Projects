package com.abstractfactory.email;

import com.abstractfactory.factory.Notification;
import com.abstractfactory.factory.NotificationType;

public class EmailFactory implements NotificationType{

	@Override
	public Notification getEmail(String type) {
		if(type==null) {
			return null;
		}else if(type.equals("personal")) {
			return new PersonalEmail();
		}else if(type.equals("office")) {
			return new OfficeEmail();
		}
		return null;
	}

	@Override
	public Notification getSms(String type) {
		// TODO Auto-generated method stub
		return null;
	}

}
