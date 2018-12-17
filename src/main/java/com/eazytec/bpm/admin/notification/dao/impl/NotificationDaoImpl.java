package com.eazytec.bpm.admin.notification.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.notification.dao.NotificationDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Notification;
import com.eazytec.model.Role;


/**
 *  This class interacts with Hibernate session to save/delete, retrieve Notification
 * objects.
 * @author Ramachandran
 *
 */
@Repository("notificationDao")
public class NotificationDaoImpl extends GenericDaoHibernate<Notification, String> implements NotificationDao{

	public NotificationDaoImpl() {
		super(Notification.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public  List<Notification> getNotificationOnDate(String messageSendOn) {
		List<Notification> notificationList = (List<Notification>) getSession().createQuery("from Notification as notification where notification.messageSendOn <='"+messageSendOn+"' AND notification.status='Active'").list();
		return notificationList;
	}
	
	@Override
	public Notification updateNotificationStatus(Notification notification){
		getSession().saveOrUpdate(notification);
		getSession().flush();
		return notification;
	}

	/**
	 * {@inheritDoc}
	 */

	public Notification saveOrUpdateNotification(Notification notification) {
        if (log.isDebugEnabled()) {
            log.debug("notification id: " + notification.getId());
        }
        getSession().saveOrUpdate(notification);
        getSession().flush();
        return notification;
    }
	
}
