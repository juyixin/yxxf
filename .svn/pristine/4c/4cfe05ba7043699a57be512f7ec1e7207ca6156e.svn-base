package com.eazytec.bpm.oa.mail.service;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eazytec.model.EmailConfiguration;
import com.eazytec.model.EmailContact;
import com.eazytec.model.User;
import com.eazytec.service.GenericManager;
import com.eazytec.service.UserExistsException;

import dtw.webmail.JwmaSession;
import dtw.webmail.model.JwmaException;

/**
 * Interface to provide mailing service 
 * 
 * @author mathi
 */

public interface MailService extends GenericManager<EmailContact, String> {
	
	/**
	 * get email authentication while login user
	 * 
	 * @param websession
	 * @param user
	 */
	void getEmailAuthentication(HttpSession websession, User user);
	
	/**
	 * save email contact 
	 *  
	 * @param contact
	 * @return
	 */
	EmailContact saveEmailContact(EmailContact contact);
	
	/**
	 * get user contacts
	 * 
	 * @param user
	 * @return
	 */
	List<EmailContact> getUserContacts(String user);
	
	/**
	 * save mail configuration
	 * 
	 * @param emailConfiguration
	 * @return
	 */
	EmailConfiguration saveEmailConfig(EmailConfiguration emailConfiguration);
	
	/**
	 * get email configuration 
	 * 
	 */
	EmailConfiguration getEmailConfig();
	
	/**
     * Saves a user's information.
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return user the updated user object
     */
    User saveEmailSetting(User user, HttpServletRequest request, HttpServletResponse response);
    
    /**
     * Removes a contacts from the database by their contactIds
     *
     * @param contactId the contact's id
     */
    void removeContacts(List<String> contactIds);
    
    /**
     * check the mail authentication
     *  
     * @param req
     * @param res
     * @return
     */
	JwmaSession checkMailAuthentication(HttpServletRequest req, HttpServletResponse res);
	
	/**
     * Get external Mail Authentication for user
     * 
     * @param req
     * @param res
     * @param user
     * @return
	 * @throws JwmaException 
     */
	JwmaSession getEmailAuthentication(HttpServletRequest req, HttpServletResponse res, User user , boolean isMailSettings) throws JwmaException;
		
    /**
	 * method to get inbox grid script
	 * 
	 * @param session
	 * @return
	 * @throws JwmaException 
	 * @throws MessagingException 
	 */
	 String getInboxMailGridScript(JwmaSession session) throws JwmaException, MessagingException;
	 
	/**
	 * method to get draft grid script
	 * 
	 * @param session
	 * @return
	 * @throws JwmaException 
	 * @throws MessagingException 
	 */
	 String getDraftMailGridScript(JwmaSession session) throws JwmaException, MessagingException;
	
	/**
	 * method to get sent mail grid script
	 * 
	 * @param session
	 * @return
	 * @throws JwmaException 
	 * @throws MessagingException 
	 */
	 String getSentMailGridScript(JwmaSession session) throws JwmaException, MessagingException;
	 
	 
	 Map<String, Object> checkRecentReceivedMail(HttpServletRequest req, HttpServletResponse res);
	 
}
