package com.eazytec.bpm.admin.notification.service;

import java.util.List;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Notification;

/**
 * It interacts with NotificationDao to save delete retrieve Notification details
 * and send notification to users
 * 
 * @author Ramachandran
 * @author Mathi
 */
public interface NotificationService {
	
	
	/**
	 * Used to get the Notification for given messageSendOn
	 * @param messageSendOn
	 * @return
	 */
	List<Notification> getNotificationOnDate(String messageSendOn);
	
	/**
	 * Save or Update the notification entity
	 * @param notification
	 * @return
	 */
	Notification saveOrUpdateNotification(Notification notification);
	
	String getUserMailIdByUser(String userId) throws EazyBpmException;
	
	/**
	 * send notification to corresponding user on notitification date
	 * 
	 * @param notificationDate
	 * @return
	 */
	boolean sendNotifications(String notificationDate);
}