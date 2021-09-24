package com.abstractfactory.factory;

import com.abstractfactory.email.EmailFactory;
import com.abstractfactory.sms.SmsFactory;

public class AbstractNotificationFactroy {

	public static NotificationType getNotificationInstance(String type) {
		if(type.equals("email")) {
			return new EmailFactory();
		}else if(type.equals("sms")) {
			return new SmsFactory();
		}
		return null;
	}
}
