package com.eazytec.bpm.admin.notification.dao;

import java.util.List;
import java.util.Date;

import com.eazytec.dao.GenericDao;
import com.eazytec.model.Notification;


/**
 * Notification Data Access Object.
 *  
 * @author Ramachandran
 *
 */
public interface NotificationDao extends GenericDao<Notification, String>{
	
	
	  /**
	   * Get Notification List by given messageSendOn
	   * @param messageSendOn
	   * @return
	   */	  
	  List<Notification> getNotificationOnDate(String messageSendOn);
	 /**
	  * Save or Update Notification by notification
	  * @param notification
	  * @return 
	  */
	 Notification updateNotificationStatus(Notification notification);
	  /**
	   * save or update notification values into database
	   * @param notification
	   * @return
	   */
	  Notification saveOrUpdateNotification(Notification notification);
}
