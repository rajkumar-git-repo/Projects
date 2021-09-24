package com.abstractfactory.sms;

import com.abstractfactory.factory.Notification;
import com.abstractfactory.factory.NotificationType;

public class SmsFactory implements NotificationType{

	@Override
	public Notification getEmail(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification getSms(String type) {
		if(type==null) {
			return null;
		}else if(type.equals("personal")) {
			return new PersonalSms();
		}else if(type.equals("office")) {
			return new OfficeSms();
		}
		return null;
	}

}
