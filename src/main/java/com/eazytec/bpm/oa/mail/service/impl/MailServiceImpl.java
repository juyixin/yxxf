package com.eazytec.bpm.oa.mail.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.FlagTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.mail.dao.MailDao;
import com.eazytec.bpm.oa.mail.service.MailService;
import com.eazytec.bpm.oa.mail.thread.EmailAuthenticationThread;
import com.eazytec.bpm.oa.message.dao.MessageDao;
import com.eazytec.bpm.runtime.listView.service.ListViewService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CipherUtils;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.model.EmailConfiguration;
import com.eazytec.model.EmailContact;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.PropertyReader;

import dtw.webmail.JwmaKernel;
import dtw.webmail.JwmaSession;
import dtw.webmail.model.JwmaException;
import dtw.webmail.model.JwmaFolderImpl;
import dtw.webmail.model.JwmaStoreImpl;
import dtw.webmail.model.JwmaStoreInfo;
import dtw.webmail.util.config.JwmaConfiguration;

/**
 * Implementation of mailing interface.
 *
 * @author mathi
 */

@Service("mailService")
public class MailServiceImpl extends GenericManagerImpl<EmailContact, String> implements MailService{
	private MailDao mailDao;
	private UserManager userManager;
	private ListViewService listViewService;
	private TaskService taskService;
	
	private int inboxMailOldCount = 0;
	private int draftMailOldCount = 0;
	private int sentMailOldCount = 0;
	private Message[] inboxMailOldMessages;
	private Message[] draftMailOldMessages;
	private Message[] sentMailOldMessages;
	
	@Autowired
	private MessageDao messageDao;
	
    @Autowired
    public void setMailDao(MailDao mailDao) {
        this.dao = mailDao;
        this.mailDao = mailDao;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    @Autowired
    public void setListViewService(ListViewService listViewService) {
        this.listViewService = listViewService;
    }
 
    @Autowired
    public void setTaskService(TaskService taskService){
    	this.taskService = taskService;
    }
    /**
     * {@inheritDoc}
     */
    public EmailContact saveEmailContact(EmailContact contact){
    	return mailDao.save(contact);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<EmailContact> getUserContacts(String user){
    	return mailDao.getUserContacts(user);
    }
    
    /**
     * {@inheritDoc}
     */
    public void getEmailAuthentication(HttpSession websession, User user){
    	log.info("getting email authentication "+user.getFullName());
		Thread mailAuthenticationThread = new Thread(new EmailAuthenticationThread(websession, user));
		mailAuthenticationThread.start();
	}
    
    /**
     * {@inheritDoc}
     */
	public EmailConfiguration getEmailConfig(){
		return mailDao.getEmailConfig();
	}
	
    /**
     * {@inheritDoc}
     */
    public EmailConfiguration saveEmailConfig(EmailConfiguration emailConfig){
		return mailDao.saveEmailConfig(emailConfig);
	}
    
    /**
     * {@inheritDoc}
     */
    public User saveEmailSetting(User user, HttpServletRequest request, HttpServletResponse response){
    	return userManager.saveEmailSetting(user, request, response);
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeContacts(List<String> contactIds){
    	List<EmailContact> contactList=mailDao.getContactsByIds(contactIds);
    	for (EmailContact contact : contactList) {
			mailDao.remove(contact);
		}
    }
	
    /**
     * {@inheritDoc}
     */
	public JwmaSession checkMailAuthentication(HttpServletRequest req, HttpServletResponse res){
	      HttpSession websession = req.getSession(true);
	      JwmaSession session = null;
    	  Object o = websession.getValue("jwma.emailSession");
	      session = ((o == null)? new JwmaSession(websession):((JwmaSession) o));
	      session.setRequest(req);
	      session.setResponse(res);
	      session.setWebSession(websession);
	      return session;
	}
	
	/**
     * {@inheritDoc}
	 * @throws JwmaException 
     */
	public JwmaSession getEmailAuthentication(HttpServletRequest req, HttpServletResponse res, User user, boolean isMailSettings) throws JwmaException{
		HttpSession websession = req.getSession(true);
		Object o = websession.getValue("jwma.emailSession");
		//update session information
		JwmaSession session = ((o == null)? new JwmaSession(websession):((JwmaSession) o));
		if(isMailSettings){
			if(session != null && session.isAuthenticated()){
				session.endMailSession();
			}
		}
		
		session.setRequest(req);
		session.setResponse(res);
		session.setWebSession(websession);
		String password = "";
		if(!StringUtil.isEmptyString(user.getEmailPassword())){
			String currentEmailPassword = userManager.getUserEmailPassword(user.getId());
			if(null != currentEmailPassword && currentEmailPassword.equals(user.getEmailPassword())){
				password =CipherUtils.decrypt(user.getEmailPassword());
			} else {
				password = user.getEmailPassword();
			}
		} 
		return getAuthentication(session, user.getEmail(), password, "Default PostOffice");
	}
	
	/**
	 * get authentication for mail account
	 * 
	 * @param session
	 * @param username
	 * @param password
	 * @throws JwmaException 
	 */
	public JwmaSession getAuthentication(JwmaSession session, String username, String password, String postoffice) throws JwmaException{
		log.info("getting authentication of "+username);
		boolean newAccount = true;
	    JwmaKernel kernel = JwmaKernel.getReference();
		JwmaConfiguration config = kernel.getConfiguration();
		session.setPostOffice(config.getPostOfficeByName(postoffice));
		System.out.println("Mail Setting Save == "+username+" Password == "+password);
		session.authenticate(username, password, newAccount);
		JwmaStoreImpl store = session.getJwmaStore();
		session.storeBean("jwma.storeinfo", (JwmaStoreInfo) store);
		session.storeBean("jwma.inboxinfo", store.getInboxInfo());
		session.storeBean("jwma.trashinfo", store.getTrashInfo());
		session.storeBean("jwma.folder", store.getActualFolder());
		session.getJwmaStore().prepareRootFolder();
		session.getJwmaStore().prepareTrashFolder();
		//session.getJwmaStore().prepareSentLaterFolder();
		return session;
	}
    
    /**
     * {@inheritDoc}
     */
	 public String getInboxMailGridScript(JwmaSession session) throws JwmaException, MessagingException{
		 String script = "";
		 int startcount = 1;
		 Message[] msgs;
		 int emailLimit = Integer.parseInt(PropertyReader.getInstance()
					.getPropertyFromFile("Integer", "system.email.limit"));
		 JwmaFolderImpl jwmaFolder = session.getJwmaStore().getInboxFolder();
    	 if( jwmaFolder == null){
    		 session.getJwmaStore().prepareInboxFolder();
  	    	 jwmaFolder=session.getJwmaStore().getInboxFolder();
  	     } 
    	 Folder folder = jwmaFolder.getFolder();
    	 try {
    		 //for listing only
    		 if (!folder.isOpen()) {
    			 folder.open(Folder.READ_ONLY);
    		 }
    		 int messageCount = folder.getMessages().length;
    		 if(messageCount > emailLimit){
    			 startcount = messageCount - emailLimit;
    		 }
    		 if(inboxMailOldCount != messageCount || messageCount == 0){
    			 msgs = folder.getMessages(startcount, folder.getMessages().length);
    			 //fetch messages with a slim profile
        		 FetchProfile fp = new FetchProfile();
        		 fp.add(FetchProfile.Item.ENVELOPE);		//contains the headers
        		 fp.add(FetchProfile.Item.FLAGS);
        		 fp.add(FetchProfile.Item.CONTENT_INFO);//contains the flags
        		 folder.fetch(msgs, fp);
    			 inboxMailOldMessages = msgs;
    		 } else {
    			 msgs = inboxMailOldMessages;
    		 }
    		 script=listViewService.getListViewForInboxMail(msgs);
    	 } catch (Exception ex) {
    		 ex.printStackTrace();
	         throw new JwmaException("jwma.messagelist.failedcreation");
  	   	 } finally {
  	   		 try {
	         //close the folder
  	   			if (folder.isOpen()) {
  	   				folder.close(false);
  	   			}
  	   		 } catch (MessagingException mesx) {
  	   			//don't care, the specs say it IS closed anyway
  	   		 }
  	   	}
	 return script;
	 }
	 
	/**
     * {@inheritDoc}
     */
	 public String getDraftMailGridScript(JwmaSession session) throws JwmaException, MessagingException{
		 String script = "";
		 int startcount = 1;
		 Message[] msgs;
		 int emailLimit = Integer.parseInt(PropertyReader.getInstance()
					.getPropertyFromFile("Integer", "system.email.limit"));
		 JwmaFolderImpl jwmaFolder = session.getJwmaStore().getInboxDraftFolder();
    	 if( jwmaFolder == null){
    		session.getJwmaStore().prepareDraftFolder();
    		jwmaFolder=session.getJwmaStore().getInboxDraftFolder();
  	     } 
  	     Folder folder = jwmaFolder.getFolder();
  	     try {
  	    	 //for listing only
  	    	 if (!folder.isOpen()) {
  	    		 folder.open(Folder.READ_ONLY);
  	    	 }
  	    	 int messageCount = folder.getMessages().length;
  	    	 if(messageCount > emailLimit){
  	    		 startcount = messageCount - emailLimit;
  	    	 }
  	    	 if(draftMailOldCount != messageCount || messageCount == 0){
  	    		 msgs = folder.getMessages(startcount, folder.getMessages().length);
  	    		 //fetch messages with a slim profile
  	    		 FetchProfile fp = new FetchProfile();
  	    		 fp.add(FetchProfile.Item.ENVELOPE);		//contains the headers
  	    		 fp.add(FetchProfile.Item.FLAGS); //contains the flags
  	    		 fp.add(FetchProfile.Item.CONTENT_INFO);//contains the content info
  	    		 folder.fetch(msgs, fp);
  	    		 draftMailOldMessages = msgs;
  	    	 } else {
  	    		 msgs = draftMailOldMessages;
  	    	 }
  	    	 script=listViewService.getListViewForDraftMail(msgs);
  	     } catch (Exception ex) {
  	    	 ex.printStackTrace();
  	     } finally {
  	    	 try {
  	    		 //close the folder
  	    		 if (folder.isOpen()) {
  	    			 folder.close(false);
  	    		 }
  	    	 } catch (MessagingException mesx) {
  	    		 //don't care, the specs say it IS closed anyway
  	    	 }
  	     }
  	     return script;
	 }
	 
	 /**
      * {@inheritDoc}
      */
	 public String getSentMailGridScript(JwmaSession session) throws JwmaException, MessagingException{
		 String script = "";
		 int startcount = 1;
		 Message[] msgs;
		 int emailLimit = Integer.parseInt(PropertyReader.getInstance()
					.getPropertyFromFile("Integer", "system.email.limit"));
		 JwmaFolderImpl jwmaFolder=session.getJwmaStore().getSentFolder();
		 if( jwmaFolder == null){
			 session.getJwmaStore().prepareSentFolder();
			 jwmaFolder=session.getJwmaStore().getSentFolder();
  	     } 
		 Folder folder = jwmaFolder.getFolder();
		 try {
  	    	 //for listing only
  	    	 if (!folder.isOpen()) {
  	    		 folder.open(Folder.READ_ONLY);
  	    	 }
  	    	 int messageCount = folder.getMessages().length;
  	    	 if(messageCount > emailLimit){
  	    		 startcount = messageCount - emailLimit;
  	    	 }
  	    	 if(sentMailOldCount != messageCount || messageCount == 0){
  	    		 msgs = folder.getMessages(startcount, folder.getMessages().length);
  	    		 //fetch messages with a slim profile
  	    		 FetchProfile fp = new FetchProfile();
  	    		 fp.add(FetchProfile.Item.ENVELOPE);		//contains the headers
  	    		 fp.add(FetchProfile.Item.FLAGS);
  	    		 fp.add(FetchProfile.Item.CONTENT_INFO);//contains the flags
  	    		 folder.fetch(msgs, fp);
  	    		 sentMailOldMessages = msgs;
  	    	 } else {
  	    		 msgs = sentMailOldMessages;
  	    	 }
  	    	 script=listViewService.getListViewForSentMail(msgs);
  	     } catch (Exception ex) {
  	    	 throw new JwmaException("jwma.messagelist.failedcreation");
  	     } finally {
  	    	 try {
  	    		 //close the folder
  	    		 if (folder.isOpen()) {
  	    			 folder.close(false);
  	    		 }
  	    	 } catch (MessagingException mesx) {
  	    		 //don't care, the specs say it IS closed anyway
  	    	 }
  	     }
	 return script;
	 }
	 
	 /**
      * {@inheritDoc}
      */
	public Map<String, Object> checkRecentReceivedMail(HttpServletRequest req,	HttpServletResponse res) {
		
		Map<String, Object> mailCount = new HashMap<String, Object>();
		JwmaSession session = null;
		HttpSession websession = req.getSession(true);
		
		User loggedInUser = CommonUtil.getLoggedInUser();
		
		int unreadEmailCount = 0;
		int unreadInternalMessageCount = messageDao.getAllUnReadMessageCount(loggedInUser.getId());
		//try {
			/*if (session == null || !session.isValidWebSession()
					|| !session.isAuthenticated()) {
				session = getInternalMessageAuthentication(req, res, loggedInUser);
			}
			if (null == session.getJwmaStore().getInboxFolder()) {
				
					session.getJwmaStore().prepareInboxFolder();
			}*/
			
		  /*  Object o = websession.getValue("jwma.internalMessageSession");
			session = ((o == null)? new JwmaSession(websession, "internalMessage"):((JwmaSession) o));
		    //session = checkMailAuthentication(req, res, "internalMessage");
			if (session !=null && session.isAuthenticated()) {
				 JwmaFolderImpl jwmaFolder = session.getJwmaStore().getInboxFolder();
		    	 if( jwmaFolder == null){
		    		 session.getJwmaStore().prepareInboxFolder();
		  	    	 jwmaFolder=session.getJwmaStore().getInboxFolder();
		  	     } 
		    	 Folder folder = jwmaFolder.getFolder();
		    	 if(folder != null){
		    		 if(!folder.isOpen()){
		    			 folder.open(Folder.READ_ONLY);
		    		 }
		    		 unreadInternalMessageCount = getUnreadMailCount(folder);// session.getJwmaStore().getInboxFolder().getInboxUnReadMessageCount();
		    	 } else {
		    		 unreadInternalMessageCount = session.getJwmaStore().getInboxFolder().getInboxUnReadMessageCount();
		    	 }
				
			}
		}catch(Exception e){
			// nothing handling here...
		}
		try{	
			/*if (session == null || !session.isValidWebSession()
					|| !session.isAuthenticated()) {
				session = getEmailAuthentication(req, res, loggedInUser);
			}
			if (null == session.getJwmaStore().getInboxFolder()) {
					session.getJwmaStore().prepareInboxFolder();
			}*/
		/*	Object o = websession.getValue("jwma.emailSession");
		    session = ((o == null)? new JwmaSession(websession, "email"):((JwmaSession) o));
			//session = checkMailAuthentication(req, res, "email");
		//	if(session !=null && session.isValidWebSession()
				//	&& session.isAuthenticated() && session.getJwmaStore().getInboxFolder() !=null){
			if(session !=null && session.isAuthenticated()){
				 JwmaFolderImpl jwmaFolder = session.getJwmaStore().getInboxFolder();
		    	 if( jwmaFolder == null){
		    		 session.getJwmaStore().prepareInboxFolder();
		  	    	 jwmaFolder=session.getJwmaStore().getInboxFolder();
		  	     } 
		    	 Folder folder = jwmaFolder.getFolder();
		    	 if(folder != null){
		    		 if(!folder.isOpen()){
		    			 folder.open(Folder.READ_ONLY);
		    		 }
		    		 unreadEmailCount = getUnreadMailCount(folder);
		    	 } else {
		    		 unreadEmailCount = session.getJwmaStore().getInboxFolder().getInboxUnReadMessageCount() + (session.getJwmaStore().getInboxFolder().getMessageCount() - session.getJwmaStore().getInboxFolder().getOldMessageCount()) ;
		    	 }
				
			}
		} catch (Exception e) {
			// nothing handling here...
		}*/
		int totalCount = unreadEmailCount + unreadInternalMessageCount;
		mailCount.put("unreadEmailCount", unreadEmailCount);
		mailCount.put("unreadMessageCount", unreadInternalMessageCount);
		mailCount.put("totalCount",totalCount);
		mailCount.put("user", loggedInUser.getUsername());
		Map<String,Object> taskCountMap = new HashMap<String,Object>();
		try{
			taskCountMap = taskService.getTaskCountByUserId(CommonUtil.getLoggedInUser());
			mailCount.put("toDoListCount", taskCountMap.get("toDoListCount"));
			mailCount.put("toReadCount", taskCountMap.get("toReadCount"));
		} catch(Exception e){
			//Nothing to handle here
		}
		
		return mailCount;
	}
	
	/**
	 * get the folder unread mail count
	 * 
	 * @param folder
	 * @return
	 */
	public int getUnreadMailCount(Folder folder){
		int startMailCount = 1;
		int unReadMailCount = 0;
		try{ 
			if(folder.getMessages().length > 20){
				startMailCount = folder.getMessages().length - 20;
			}
			Message messages[] = folder.getMessages(startMailCount, folder.getMessages().length);
			Message inboxMessages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false), messages);
			unReadMailCount = inboxMessages.length;
		} catch(Exception e){
		}
		return unReadMailCount;
	}

	
}
