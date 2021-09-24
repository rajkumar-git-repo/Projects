package com.abstractfactory;

import com.abstractfactory.factory.AbstractNotificationFactroy;
import com.abstractfactory.factory.Notification;
import com.abstractfactory.factory.NotificationType;

public class AbstractFactotryClient {

	public static void main(String[] args) {
		NotificationType emailNotificationType = AbstractNotificationFactroy.getNotificationInstance("email");
		Notification personalEmailNotification = emailNotificationType.getEmail("personal");
		personalEmailNotification.getNotification();
		Notification officeEmailNotification = emailNotificationType.getEmail("office");
		officeEmailNotification.getNotification();
		
		
		NotificationType smsNotificationType = AbstractNotificationFactroy.getNotificationInstance("sms");
		Notification personalSmsNotification = smsNotificationType.getSms("personal");
		personalSmsNotification.getNotification();
		Notification officeSmsNotification = smsNotificationType.getSms("office");
		officeSmsNotification.getNotification();
	}
}
