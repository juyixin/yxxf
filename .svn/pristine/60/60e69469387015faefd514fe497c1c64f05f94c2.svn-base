package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.bpm.admin.notification.service.NotificationService;

/**
 * Does the notification related actions for 
 * sending notifications,alerts
 * @author Mathi
 */

@Controller
public class NotificationController extends BaseFormController{
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private SimpleMailMessage mailMessage;
    private NotificationService notificationService;
	public VelocityEngine velocityEngine;
    
	@Autowired
	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	@Autowired
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
    /**
     * method for send chat history to chating users
     * 
     * @param request
     * @param model
     * 
     */
    @RequestMapping(value = "bpm/notification/notifyChatHistory",method = RequestMethod.POST)
	public @ResponseBody void notifyChatHistory(HttpServletRequest request,ModelMap model) {
    	List<String> recipients = new ArrayList<String>();
    	String userName = request.getParameter("userName");
    	String userFullName = request.getParameter("userFullName");
    	String userEmailId = request.getParameter("userEmailId");
    	String peerUserName = request.getParameter("peerUserName");
    	String peerUserFullName = request.getParameter("peerUserFullName");
    	String peerUserEmailId = request.getParameter("peerUserEmailId");
    	String chatMessage = request.getParameter("chatMessage");
    	
    	if(getUserManager().getEmailNotificationEnabled(userName)){
    		recipients.add(userEmailId);
    	}
    	
    	if(getUserManager().getEmailNotificationEnabled(peerUserName)){
    		recipients.add(peerUserEmailId);
    	}
    	
    	try {
    		if(recipients.size() > 0 && chatMessage.length() > 0){
    			mailEngine.sendMessage(recipients.toArray(new String[recipients.size()]), chatMessage, "Chat History");
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	
}