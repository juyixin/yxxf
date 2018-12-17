/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.eazytec.util.PropertyReader;

import dtw.webmail.util.MessageSortCriterias;
import dtw.webmail.util.MessageSortingUtil;

/**
 * Class implementing a list for <tt>JwmaMessageInfo</tt>
 * instances.
 * It has caching functionality, which reduces the need to
 * reconstruct the list after moving and deleting messages.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaMessageInfoListImpl implements Serializable{

  //logging
  private static Logger log = Logger.getLogger(JwmaMessageInfoListImpl.class);


  //instance attributes
  public List m_MessageInfos = new ArrayList();
  protected boolean m_HasDeleted = false;
  protected int m_LastSortCriteria = MessageSortCriterias.NUMBER_NUMERICAL;

  /**
   * Constructs a new <tt>JwmaMessagListImpl</tt>.
   */
  public JwmaMessageInfoListImpl() {
  }//JwmaMessageInfoListImpl

  /**
   * Returns the size of this list.
   *
   * @return the size of this list.
   */
  public int size() {
    return m_MessageInfos.size();
  }//size

  public List getMessageInfos(){
	  return m_MessageInfos;
  }
  
  public void setMessageInfos(List m_MessageInfos){
	  log.info("mails count----"+m_MessageInfos.size());
	  this.m_MessageInfos = m_MessageInfos;
  }
  
  /**
   * Returns an array of <tt>JwmaMessageInfo[]</tt> listing
   * the info's stored in this list.
   *
   * @return an array of <tt>JwmaMessageInfo</tt> instances.
   *
   * @see dtw.webmail.model.JwmaMessageInfoListImpl#listMessageInfos()
   */
  public JwmaMessageInfoImpl[] listMessageInfos() {
    //create array from it
    JwmaMessageInfoImpl[] list =
        new JwmaMessageInfoImpl[m_MessageInfos.size()];
    return (JwmaMessageInfoImpl[]) m_MessageInfos.toArray(list);
  }//listMessageInfos

  /**
   * Sorts this <tt>MessageInfoListImpl</tt> by the given criteria.
   * If the criteria does not match any existing <tt>Comparator</tt>
   * then the method returns without action.
   * The used criteria is remembered for possible re-sorting.
   *
   * @param criteria the criteria used for sorting.
   *
   * @see dtw.webmail.util.MessageSortCriterias
   * @see dtw.webmail.util.MessageSortingUtil
   */
  public void sort(int criteria) {
    sort(criteria, true);
  }//sort

  /**
   * Sorts this <tt>MessageInfoListImpl</tt> by the given criteria.
   * If the criteria does not match any existing <tt>Comparator</tt>
   * then the method returns without action.
   *
   * @param criteria the criteria used for sorting.
   * @param remember flags to remember or forget the criteria used for sorting.
   *
   * @see dtw.webmail.util.MessageSortCriterias
   * @see dtw.webmail.util.MessageSortingUtil
   */
  private void sort(int criteria, boolean remember) {
    //get the comparator
    Comparator comp = null;
    try {
      comp = MessageSortingUtil.CRITERIA_COMPARATOR[criteria];
    } catch (IndexOutOfBoundsException ex) {
      return;
    }
    //sort it
    Collections.sort(m_MessageInfos, comp);

    //possibly remember the criteria
    if (remember) {
      m_LastSortCriteria = criteria;
    }
  }//sort(boolean)

  /**
   * Returns the last used sort criteria as <tt>int</tt>.
   *
   * @return the last sort criteria as <tt>int</tt>.
   */
  public int getLastSortCriteria() {
    return m_LastSortCriteria;
  }//getLastSortCriteria

  /**
   * Returns the message number of the next message in the list,
   * observing the sorting.
   * The method returns -1 if the message does not exist in the list,
   * or when there is no next message.
   *
   * @param msgnum the message number of the message to start from.
   * @return the messagenumber of the next message as <tt>int</tt>.
   */
  public int getNextMessageNumber(int msgnum) {
    int listindex = getListIndex(msgnum);
    if (listindex == -1 || listindex == (m_MessageInfos.size() - 1)) {
      return -1;
    } else {
      return ((JwmaMessageInfoImpl) m_MessageInfos.get(listindex + 1))
          .getMessageNumber();
    }
  }//getNextMessageNumber

  /**
   * Returns the message number of the previous message in the list,
   * observing the sorting.
   * The method returns -1 if the message does not exist in the list,
   * or when there is no previous message.
   *
   * @param msgnum the message number of the message to start from.
   * @return the messagenumber of the previous message as <tt>int</tt>.
   */
  public int getPreviousMessageNumber(int msgnum) {
    int listindex = getListIndex(msgnum);
    if (listindex == -1 || listindex == 0) {
      return -1;
    } else {
      return ((JwmaMessageInfoImpl) m_MessageInfos.get(listindex - 1))
          .getMessageNumber();
    }
  }//getNextMessageNumber

  /**
   * Returns the list index of message given by it's
   * message number.
   *
   * @return the list index as <tt>int</tt>.
   */
  public int getListIndex(int msgnum) {
    for (Iterator iter = iterator(); iter.hasNext();) {
      JwmaMessageInfoImpl msginfo = (JwmaMessageInfoImpl) iter.next();
      if (msginfo.getMessageNumber() == msgnum) {
        return m_MessageInfos.indexOf(msginfo);
      }
    }
    return -1;
  }//getListIndex

  /**
   * Returns an Iterator over the <tt>JwmaMessageInfoImpl</tt> instances
   * contained within this list.
   *
   * @return the <tt>Iterator</tt> over the items in this list.
   */
  public Iterator iterator() {
    return m_MessageInfos.iterator();
  }//iterator

  /**
   * Removes the items with the given numbers from this list.
   *
   * @param msgsnums the numbers of the items to be removed as <tt>int[]</tt>.
   */
  public void remove(int[] msgsnums) {
    for (Iterator iter = iterator(); iter.hasNext();) {
      int num = ((JwmaMessageInfoImpl) iter.next()).getMessageNumber();
      for (int n = 0; n < msgsnums.length; n++) {
        if (num == msgsnums[n]) {
          iter.remove();
        }
      }
    }
  }//remove

  /**
   * Removes items that are flagged deleted from this list.
   * This method can be used to clean out those messages
   * when the folder was closed with expunging.
   */
  public void removeDeleted() {
    if (m_HasDeleted) {
      JwmaMessageInfoImpl msg = null;
      for (Iterator iter = iterator(); iter.hasNext();) {
        msg = (JwmaMessageInfoImpl) iter.next();
        if (msg.isDeleted()) {
          iter.remove();
        }
      }
      m_HasDeleted = false;
    }
  }//removeDeleted


  /**
   * Renumbers the items in this list.
   * This message should be called if items were removed from
   * this list.
   */
  public void renumber(int msgCount, int[] msgNums) {
    //ensure integrity by sorting back the message numbers
    //sort(MessageSortCriterias.NUMBER_NUMERICAL, false);
    sort(MessageSortCriterias.NUMBER_NUMERICAL, false);
    
    //int i = 1;
    int i = msgNums[0];
    List<JwmaMessageInfoImpl> msgInfos = (List<JwmaMessageInfoImpl>)m_MessageInfos;
    m_MessageInfos = new ArrayList<JwmaMessageInfoImpl>();
    for(JwmaMessageInfoImpl msgInfo : msgInfos){
    	if(msgNums[0] < msgInfo.getMessageNumber()){
    		msgInfo.setMessageNumber(msgInfo.getMessageNumber()-1);
    	}
    	m_MessageInfos.add(msgInfo);
    }
    /*for (Iterator iter = iterator(); iter.hasNext(); i++) {
      ((JwmaMessageInfoImpl) iter.next()).setMessageNumber(i);
    }*/

    //ensure the right order by sorting back to the users last
    //applied criteria
    sort(MessageSortCriterias.NUMBER_REVERSE_NUMERICAL, false);
  }//renumber


  /**
   * Builds the list of <tt>JwmaMessageInfoImpl</tt> instances from
   * the given array of messages.
   *
   * @param messages array of <tt>javax.mail.Message</tt> instances.
   *
   * @throws JwmaException if it fails to create a <tt>JwmaMessageInfoImpl</tt>
   *         instance.
   */
  private void buildMessageInfoList(Message[] messages)
      throws JwmaException {
	int emailLimit = Integer.parseInt(PropertyReader.getInstance()
				.getPropertyFromFile("Integer", "system.email.limit"));
    m_MessageInfos = new ArrayList(messages.length);
    JwmaMessageInfoImpl msginfo = null;
    //for (int i = 0; i < messages.length && i<5; i++) {
    for (int i = messages.length-1; i > -1 && (messages.length - emailLimit) < i; i--) {
    	msginfo = JwmaMessageInfoImpl.createJwmaMessageInfoImpl(messages[i]);
    	//flag deleted messages
    	if (msginfo.isDeleted() && !m_HasDeleted) {
    		m_HasDeleted = true;
    	}
    	m_MessageInfos.add(msginfo);
    }
    setMessageInfos(m_MessageInfos);
  }//buildMessageInfoList
  
  /**
   * Builds the list of <tt>JwmaMessageInfoImpl</tt> instances from
   * the given array of messages.
   *
   * @param messages array of <tt>javax.mail.Message</tt> instances.
   *
   * @throws JwmaException if it fails to create a <tt>JwmaMessageInfoImpl</tt>
   *         instance.
 * @throws MessagingException 
   */
  public void refreshMessageInfoList(Folder folder, int newMessageCount)
      throws JwmaException, MessagingException {
	  try {
		  int emailLimit = Integer.parseInt(PropertyReader.getInstance()
					.getPropertyFromFile("Integer", "system.email.limit"));
		  
		  //for listing only
	      if (!folder.isOpen()) {
	    	  folder.open(Folder.READ_ONLY);
	      }
	      
	      Message[] messages = folder.getMessages();
	      int size = messages.length-1;
		  JwmaMessageInfoImpl msginfo = null;
		  int count= 0;
		  //List<JwmaMessageInfoImpl> messageInfos = m_MessageInfos;
		  //m_MessageInfos = new ArrayList<JwmaMessageInfoImpl>();
		  for (int i = size; (i > 0 && count < newMessageCount) ; i--) {
			  msginfo = JwmaMessageInfoImpl.createJwmaMessageInfoImpl(messages[i]);
			  //flag deleted messages
		  	  if (msginfo.isDeleted() && !m_HasDeleted) {
		  		  m_HasDeleted = true;
		  	  }
		  	  m_MessageInfos.add(0,msginfo);
		  	  count++;
		  } 
		  setMessageInfos(m_MessageInfos);
		  /*for(JwmaMessageInfoImpl messageInfo : (List<JwmaMessageInfoImpl>)messageInfos){
			  //msgInfo.setMessageNumber(msgInfo.getMessageNumber()+newMessageCount);
			  m_MessageInfos.add(messageInfo);
		  }*/
		  
	  }catch (MessagingException mex) {
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
  }//refreshMessageInfoList
  
  /**
   * Builds the list of <tt>JwmaMessageInfoImpl</tt> instances from
   * the given array of messages.
   *
   * @param messages array of <tt>javax.mail.Message</tt> instances.
   *
   * @throws JwmaException if it fails to create a <tt>JwmaMessageInfoImpl</tt>
   *         instance.
   */
  public void buildMessageInfos(Folder folder)
      throws JwmaException {
	  
	  JwmaMessageInfoImpl msginfo = null;

	  try {
		  if (!folder.isOpen()) {
			  folder.open(Folder.READ_ONLY);
		      }
		  //Thread messageInfoThread = new Thread(new MessageInfoThread(folder, folder.getMessages(), msginfo));
		  //messageInfoThread.start();
	  } catch (MessagingException e) {
		  // TODO Auto-generated catch block
	  } catch (Exception e) {
		// TODO: handle exception
	}
	  
  }//buildMessageInfoList
  

  /**
   * Factory method that creates a new <tt>JwmaMessageInfoListImpl</tt> instance
   * from the given array of messages.
   *
   * @param messages array of <tt>javax.mail.Message</tt> instances.
   *
   * @return the newly created <tt>JwmaMessageInfoListImpl</tt> instance.
   *
   * @throws JwmaException if it fails to build the list.
   */
  public static JwmaMessageInfoListImpl createJwmaMessageInfoListImpl(Message[] messages)
      throws JwmaException {

    JwmaMessageInfoListImpl msglist = new JwmaMessageInfoListImpl();
    //method comment and called new method
    //buildMessageInfoList
    msglist.buildMessageInfoList(messages);
    //msglist.buildMessageInfos(messages);
    return msglist;

  }//createJwmaMessageList
  
  
  /**
   * Factory method that creates a new <tt>JwmaMessageInfoListImpl</tt> instance
   * from the given array of messages.
   *
   * @param messages array of <tt>javax.mail.Message</tt> instances.
   *
   * @return the newly created <tt>JwmaMessageInfoListImpl</tt> instance.
   *
   * @throws JwmaException if it fails to build the list.
   */
  public static JwmaMessageInfoListImpl createJwmaMessageInfoListImpl(Folder folder, Message[] messages)
      throws JwmaException {

    JwmaMessageInfoListImpl msglist = new JwmaMessageInfoListImpl();
    //method comment and called new method
    //buildMessageInfoList
    //msglist.buildMessageInfoList(messages);
    //msglist.buildMessageInfos(folder,messages);
    return msglist;

  }//createJwmaMessageList

  /**
   * Factory method that creates a new <tt>JwmaMessageInfoListImpl</tt> instance
   * wrapping the list of messages in the given folder.
   *
   * @param f the <tt>javax.mail.Folder</tt> instance, the new list instance should
   *        be created for.
   *
   * @return the newly created <tt>JwmaMessageInfoListImpl</tt> instance.
   *
   * @throws JwmaException if it fails retrieve the list of <tt>javax.mail.Message</tt>
   *         instances from the folder, or when it fails to build the list.
   */
  public static JwmaMessageInfoListImpl createJwmaMessageInfoListImpl(Folder f)
      throws JwmaException {

    try {
      //for listing only
      if (!f.isOpen()) {
        f.open(Folder.READ_ONLY);
      }
      Message[] msgs = f.getMessages();
      //commented by mathi need to refer this.. because taking more time for fetching all mails
      //fetch messages with a slim profile
      /*FetchProfile fp = new FetchProfile();
      fp.add(FetchProfile.Item.ENVELOPE);		//contains the headers
      fp.add(FetchProfile.Item.FLAGS);		//contains the flags
      f.fetch(msgs, fp);*/
      return createJwmaMessageInfoListImpl(msgs);
      //return createJwmaMessageInfoListImpl(f, msgs);
    } catch (MessagingException mex) {
      throw new JwmaException("jwma.messagelist.failedcreation");
    } finally {
      try {
        //close the folder
        if (f.isOpen()) {
          f.close(false);
        }
      } catch (MessagingException mesx) {
        //don't care, the specs say it IS closed anyway
      }
    }
  }//createJwmaMessageInfoListImpl
  
}//class JwmaMessageInfoListImpl

class MessageInfoThread extends JwmaMessageInfoListImpl implements Runnable{
	  public Message[] messages;
	  public Folder folder;
	  JwmaMessageInfoListImpl m_MessageInfoList;
	  private JwmaMessageInfoImpl msginfo = null;
	  private int emailLimit = Integer.parseInt(PropertyReader.getInstance()
		.getPropertyFromFile("Integer", "system.email.limit"));
	  public MessageInfoThread(Folder folder, Message[] msgs, JwmaMessageInfoListImpl m_MessageInfoList) {
		  this.folder = folder;
		  this.messages = msgs;
		  this.m_MessageInfoList = m_MessageInfoList;
		// TODO Auto-generated constructor stub
	  }
	  
	  public void run() {
	        try {
	        	if (!folder.isOpen()) {
	        		folder.open(Folder.READ_ONLY);
	        	}
		        for (int i = messages.length-1; i > -1 && (messages.length - emailLimit) < i; i--) {
		        	
					msginfo = JwmaMessageInfoImpl.createJwmaMessageInfoImpl(messages[i]);
					
					if(msginfo != null){
		    	      //flag deleted messages
		    	      if (msginfo.isDeleted() && !m_HasDeleted) {
		    	        m_HasDeleted = true;
		    	      }
		    	      m_MessageInfos.add(msginfo);
		    	      m_MessageInfoList.setMessageInfos(m_MessageInfos);
					}
		    	}
	        } catch (JwmaException e) {
				// TODO Auto-generated catch block
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
			} catch (Exception e) {
				// TODO: handle exception
			}
			
	    }
	  
}
