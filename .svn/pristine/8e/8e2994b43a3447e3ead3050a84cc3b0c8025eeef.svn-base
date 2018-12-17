package com.eazytec.bpm.admin.notification.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.notification.dao.NotificationDao;
import com.eazytec.bpm.admin.notification.service.NotificationService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.mail.service.MailService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Notification;
import com.eazytec.service.MailEngine;
import com.eazytec.service.UserManager;
import com.eazytec.service.impl.GenericManagerImpl;

/**
 * Implementation of NotificationService interface
 * 
 * @author Ramachandran
 * @author Mathi
 */
@Service("notificationService")
public class NotificationServiceImpl extends GenericManagerImpl<Notification, String> implements NotificationService{
	
	private NotificationDao notificationDao;
	
	private UserManager userManager;

	private RoleService roleService;
	
	private GroupService groupService;
	
	private DepartmentService departmentService;

	private MailSender mailSender;

	private MailEngine mailEngine;
	
	private MailService mailService;
	
	@Autowired
	public void setNotificationDao(NotificationDao notificationDao){
		this.notificationDao = notificationDao;
	}
	
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

    @Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
    
	@Autowired
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Autowired
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}
	
	@Autowired
	public void setMailService(MailService mailService) {
	    this.mailService = mailService;
	}
	 
	/**
     * {@inheritDoc}
     */
	public boolean sendNotifications(String notificationDate){
		try {
			List<Notification> notifications = getNotificationOnDate(notificationDate);
			if(!notifications.isEmpty()){
				for(Notification notification : notifications){
					Map<String, Object> resultMap = new HashMap<String, Object>();
					if (!StringUtil.isEmptyString(notification.getNotificationType()) && notification.getType().equalsIgnoreCase("ROLE")) {
						resultMap = sendNotificationByRole(resultMap, notification);
					} else if(!StringUtil.isEmptyString(notification.getNotificationType()) && notification.getType().equalsIgnoreCase("GROUP")){
						resultMap = sendNotificationByGroup(resultMap, notification);
					} else if (!StringUtil.isEmptyString(notification.getNotificationType()) && notification.getType().equalsIgnoreCase("DEPARTMENT")) {
						resultMap = sendNotificationByDepartment(resultMap, notification);
					} else if(!StringUtil.isEmptyString(notification.getNotificationType()) && notification.getType().equalsIgnoreCase("user")){
						resultMap = sendNotificationByUser(resultMap, notification);
					}
					if(!resultMap.isEmpty()){
					    if(new Boolean(resultMap.get("sent").toString())){
					    	notification.setStatus("sent");
					    	notification.setStatusMessage(resultMap.get("successMessage").toString());
					    } else {
					    	notification.setStatus("failed");
					    	notification.setStatusMessage(resultMap.get("errorMessage").toString());
					    }
					    notification = saveOrUpdateNotification(notification);
				    }
				}
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * send notification to users by role
	 * 
	 * @param resultMap
	 * @param notification
	 * @return
	 */
	private Map<String, Object> sendNotificationByRole(Map<String, Object> resultMap, Notification notification){
		if(!StringUtil.isEmptyString(notification.getTypeId())){
			List<String> recipients = new ArrayList<String>();
			if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_SMS_TYPE)){
				recipients = userManager.getUsersMobileNoByRole(notification.getTypeId());
	    	} else if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_EMAIL_TYPE)){
	    		recipients = userManager.getUsersEmailIdByRole(notification.getTypeId());
	    	} else {
	    		recipients = userManager.getUsersInternalMessageIdByRole(notification.getTypeId(), CommonUtil.getInternalMessageDomain());
	    	}
			resultMap = sendNotification(resultMap, notification.getNotificationType(), recipients, notification.getSubject(), notification.getMessage());
		}
		return resultMap;
	}
	
	/**
	 * send notification to users by group
	 * 
	 * @param resultMap
	 * @param notification
	 * @return
	 */
	private Map<String, Object> sendNotificationByGroup(Map<String, Object> resultMap, Notification notification){
		if(!StringUtil.isEmptyString(notification.getTypeId())){
			List<String> recipients = new ArrayList<String>();
			if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_SMS_TYPE)){
				recipients = userManager.getUsersMobileNoByGroup(notification.getTypeId());
	    	} else if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_EMAIL_TYPE)){
	    		recipients = userManager.getUsersEmailIdByGroup(notification.getTypeId());
	    	} else {
	    		recipients = userManager.getUsersInternalMessageIdByGroup(notification.getTypeId(), CommonUtil.getInternalMessageDomain());
	    	}
			resultMap = sendNotification(resultMap, notification.getNotificationType(), recipients, notification.getSubject(), notification.getMessage());
		}
		return resultMap;
	}
	
	/**
	 * send notification to users by department
	 * 
	 * @param resultMap
	 * @param notification
	 * @return
	 */
	private Map<String, Object> sendNotificationByDepartment(Map<String, Object> resultMap, Notification notification){
		if(!StringUtil.isEmptyString(notification.getTypeId())){
			List<String> recipients = new ArrayList<String>();
			if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_SMS_TYPE)){
				recipients = userManager.getUsersMobileNoByDepartment(notification.getTypeId());
	    	} else if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_EMAIL_TYPE)){
	    		recipients = userManager.getUsersEmailIdByDepartment(notification.getTypeId());
	    	} else {
	    		recipients = userManager.getUsersInternalMessageIdByDepartment(notification.getTypeId(), CommonUtil.getInternalMessageDomain());
	    	}
			resultMap = sendNotification(resultMap, notification.getNotificationType(), recipients, notification.getSubject(), notification.getMessage());
		}
		return resultMap;
	}
	
	/**
	 * send notification to user
	 * 
	 * @param resultMap
	 * @param notification
	 * @return
	 */
	private Map<String, Object> sendNotificationByUser(Map<String, Object> resultMap, Notification notification){
		List<String> recipients = new ArrayList<String>();
		if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_SMS_TYPE)){
			if(StringUtil.isEmptyString(notification.getTypeId()) && !StringUtil.isEmptyString(notification.getUserId())){
				recipients = userManager.getUsersMobileNoByUser(notification.getUserId());	
		    } else if(!StringUtil.isEmptyString(notification.getTypeId())){
		    	recipients.add(notification.getTypeId());
		    }
    	} else if(notification.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_EMAIL_TYPE)){
    		if(StringUtil.isEmptyString(notification.getTypeId()) && !StringUtil.isEmptyString(notification.getUserId())){
		    	String email = getUserMailIdByUser(notification.getUserId());	
		    	recipients.add(email);
		    } else if(!StringUtil.isEmptyString(notification.getTypeId())){
		    	recipients.add(notification.getTypeId());
		    }
    	} else {
    		if(StringUtil.isEmptyString(notification.getTypeId()) && !StringUtil.isEmptyString(notification.getUserId())){
    			recipients.add(CommonUtil.getInternalMessageId(notification.getUserId()));
		    } else if(!StringUtil.isEmptyString(notification.getTypeId())){
		    	recipients.add(CommonUtil.getInternalMessageId(notification.getTypeId()));
		    }
    	}
    	resultMap = sendNotification(resultMap, notification.getNotificationType(), recipients, notification.getSubject(), notification.getMessage());
		return resultMap;
	}
	
	/**
	 * send notification to user 
	 * 
	 * @param resultMap
	 * @param notificationType
	 * @param recipients
	 * @param subject
	 * @param body
	 * @return
	 */
	private Map<String, Object> sendNotification(Map<String, Object> resultMap, String notificationType, List<String> recipients, String subject, String body){
		if(!recipients.isEmpty()){
			if(notificationType.equalsIgnoreCase(Constants.NOTIFICATION_SMS_TYPE)){
	    		
	    	} else if(notificationType.equalsIgnoreCase(Constants.NOTIFICATION_EMAIL_TYPE)){
	    		resultMap = mailEngine.sendEmail(recipients.toArray(new String[recipients.size()]), subject, body);
	    	} else {
	    		//TODO 发送内部消息
	    	}
		}
		return resultMap;
	}
	
	
	/**
	  * {@inheritDoc}
	  */
	public List<Notification> getNotificationOnDate(String messageSendOn){
		return notificationDao.getNotificationOnDate(messageSendOn);
	}
	
	
	public Notification saveOrUpdateNotification(Notification notification){
		return notificationDao.saveOrUpdateNotification(notification);
	}
	
	public String getUserMailIdByUser(String userId) throws EazyBpmException{
		return userManager.getUserMailIdByUser(userId);
	}
		 
}