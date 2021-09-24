package com.abstractfactory.factory;

public interface NotificationType {

	public Notification getEmail(String type);
	public Notification getSms(String type);
}
