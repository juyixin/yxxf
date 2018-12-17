/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.search.FlagTerm;

import org.apache.log4j.Logger;

import com.eazytec.util.PropertyReader;

//import dtw.webmail.JwmaKernel;

/**
 * Class implementing the <tt>JwmaFolder</tt> model.
 *
 * It also implements the <tt>JwmaTrashInfo</tt> and the
 * <tt>JwmaInboxInfo</tt> models, because both are just
 * simplified interfaces to a folder.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaFolderImpl
    implements JwmaFolder,
    JwmaTrashInfo,
    JwmaInboxInfo,
    Serializable {

  //logging
  private static Logger log = Logger.getLogger(JwmaFolderImpl.class);


  //associations
  protected JwmaStoreImpl m_Store;
  protected Folder m_Folder;

  //instance attributes
  protected JwmaFolderList m_Subfolders;
  protected JwmaMessageInfoListImpl m_MessageInfoList;

  protected String m_Name;
  protected String m_Path;
  protected int m_Type;
  protected JwmaMessage m_ActualMessage;
  protected boolean m_OnlineCounting;
  protected FetchProfile m_DraftProfile;
  private int oldMessageCount;
  private int inboxUnreadMessageCount; 
 
  public int getInboxUnReadMessageCount(){
	  return inboxUnreadMessageCount;
  }
  
  public void setInboxUnReadMessages(int inboxUnreadMessageCount){
	  this.inboxUnreadMessageCount = inboxUnreadMessageCount;
  }
  
  /**
   * Creates a <tt>JwmaFolderImpl</tt> instance.
   *
   * @param f the <tt>javax.mail.Folder</tt> instance to be wrapped.
   */
  private JwmaFolderImpl(Folder f) {
    m_Folder = f;
  }//constructor

  /**
   * Creates a <tt>JwmaFolderImpl</tt> instance.
   *
   * @param f the <tt>javax.mail.Folder</tt> instance to be wrapped.
   */
  private JwmaFolderImpl(Folder f, JwmaStoreImpl store) {
    m_Folder = f;
    m_Store = store;
  }//constructor

  /*** Basic info ************************************************************/

  public String getName() {
    return m_Name;
  }//getName

  /**
   * Sets this folder's name.
   *
   * @param name the name of the folder as <tt>String</tt>.
   */
  private void setName(String name) {
    m_Name = name;
  }//setName

  public String getPath() {
    return m_Path;
  }//getPath

  /**
   * Sets this folder's path.
   *
   * @param name the path of the folder as <tt>String</tt>.
   */
  private void setPath(String path) {
    m_Path = path;
  }//setPath

  public int getType() {
    return m_Type;
  }//getType

  /**
   * Sets this folder's type.
   *
   * @param type this folder's type as <tt>int</tt>.
   */
  private void setType(int type) {
    m_Type = type;
  }//setType

  public boolean isType(int type) {
    return (m_Type == type);
  }//isType

  public boolean isSubscribed() {
    return m_Folder.isSubscribed();
  }//isSubscribed

  public void setSubscribed(boolean b)
      throws JwmaException {
    try {
      m_Folder.setSubscribed(b);
    } catch (Exception ex) {
      log.error("setSubscribed()",ex);
      throw new JwmaException("folder.subscription.failed");
    }
  }//setSubscribed

  /**
   * Returns this folder's wrapped mail folder instance.
   *
   * @return wrapped instance as <tt>javax.mail.Folder</tt>.
   */
  public Folder getFolder() {
    return m_Folder;
  }//getFolder

  /**
   * Tests if this folder returns an online count of the
   * contained messages.
   * Online count means that it will request the count
   * from the JavaMail folder implementation instance, instead of
   * returning the size of the cached list of messages.
   *
   * @return boolean true if online counting, false otherwise.
   */
  public boolean isOnlineCounting() {
    return m_OnlineCounting;
  }//isOnlineCounting

  /**
   * Sets the online counting flag.
   * This will cause this folder to return the count
   * of messages as obtained from the JavaMail folder
   * implementation instance, instead of returning the size
   * of the cached list of messages.
   */
  public void setOnlineCounting(boolean b) {
    m_OnlineCounting = b;
  }//setOnlineCounting

  /*** Subfolders related ****************************************************/

  /**
   * Adds a folder to the cached list of subfolders, if
   * it is a subfolder of this folder.
   *
   * @param folder a <tt>JwmaFolderImpl</tt> instance.
   */
  public void addIfSubfolder(JwmaFolderImpl folder) {
    if (isSubfolder(this.getPath(), folder.getPath())) {
      m_Subfolders.addFolderToList(folder);
    }
  }//addIfSubfolder

  /**
   * Removes a folder from the cached list of subfolders, if
   * it is a subfolder of this folder.
   *
   * @param path a <tt>String</tt> representing the path of a folder.
   */
  public void removeIfSubfolder(String path) {
    if (isSubfolder(this.getPath(), path)) {
      m_Subfolders.removeFolderFromList(path);
    }
  }//removeIfSubfolder

  /**
   * Removes all folder from the cached list of subfolders, if
   * they are a subfolder of this folder.
   * This is a convenience method, it iterates over the array and
   * calls <tt>removeIfSubfolder(String)</tt>
   *
   * @param folders a <tt>String[]</tt> containing paths of folders.
   *
   * @see #removeIfSubfolder(String)
   */
  public void removeIfSubfolder(String[] folders) {
    for (int i = 0; i < folders.length; i++) {
      removeIfSubfolder(folders[i]);
    }
  }//removeIfSubfolder

  /**
   * Tests if a folder is a subfolder of another folder, by using
   * their respective paths.
   * A folder is a subfolder if the path starts with the path of
   * the folder it is tested against.
   *
   * @param folder path of a folder as <tt>String</tt>.
   * @param possiblesubfolder path of a possible subfolder of the folder
   *        as <tt>String</tt>.
   * @return boolean that is true, if possiblesubfolder is a subfolder
   *         of folder, false otherwise.
   */
  public boolean isSubfolder(String folder, String possiblesubfolder) {
    return possiblesubfolder.startsWith(folder);
  }//isSubfolder

  public JwmaFolder[] listSubfolders(int type) {
    //fetch list with type filter and create array from it
    return m_Subfolders.createFolderArray(m_Subfolders.sublist(type,JwmaStoreImpl.c_SubscribedOnly));
  }//listFolders

  public JwmaFolder[] listSubfolders() {
    //fetch list with type filter and create array from it
    return m_Subfolders.createFolderArray(m_Subfolders.sublist(JwmaFolder.TYPE_ALL,JwmaStoreImpl.c_SubscribedOnly));
  }//listFolders

  public JwmaFolder[] listSubfolders(int type, boolean subscribed) {
    String[] excludes = {
      m_Store.getTrashFolder().getFullName(),
      m_Store.getDraftFolder().getFullName(),
      ((m_Store.getReadMailFolder()!=null)? m_Store.getReadMailFolder().getFullName():""),
      ((m_Store.getSentMailFolder()!=null)? m_Store.getSentMailFolder().getFullName():"")
    };
    return m_Subfolders.createFolderArray(m_Subfolders.sublist(type, excludes, subscribed));
  }//listFolders

  public boolean hasSubfolders() {
    if (m_Subfolders != null && m_Subfolders.size() > 0) {
      return true;
    } else {
      return false;
    }
  }//hasFolders

  /*** End subfolder related *************************************************/

  /*** Messages related ******************************************************/

  public boolean hasMessages() {
    if (!isType(TYPE_FOLDER) && getMessageCount() > 0) {
      return true;
    } else {
      return false;
    }
  }//hasMessages

  public int getNewMessageCount() {
    try {
      return m_Folder.getNewMessageCount();
    } catch (MessagingException ex) {
      return -1;
    }
  }//getNewMessageCount
  
  public int getOldMessageCount() {
      return oldMessageCount;
  }//getNewMessageCount

  public boolean hasNewMessages() {
    return (getNewMessageCount() > 0);
  }//hasNewMessages

   public int getUnreadMessageCount() {
    try {
      return m_Folder.getUnreadMessageCount();
    } catch (MessagingException ex) {
      return -1;
    }
  }//getUnreadMessageCount

  public boolean hasUnreadMessages() {
    return (getUnreadMessageCount() > 0);
  }//hasNewMessages

  public int getMessageCount() {
    if (isOnlineCounting()) {
      try {
        return m_Folder.getMessageCount();
      } catch (MessagingException mex) {
        log.error(mex.getMessage(), mex);
        //JwmaKernel.getReference().debugLog().writeStackTrace(mex);
        return 0;
      }
    } else {
      if (m_MessageInfoList != null) {
        return m_MessageInfoList.size();
      } else {
        return 0;
      }
    }
  }//getMessageCount

  public boolean isEmpty() {
    return (!hasMessages());
  }//isEmpty

  /**
   * Returns the actual message.
   * The actual message is a reference to the message wrapper
   * instance of the message that was requested last.
   *
   * @return the <tt>JwmaMessage</tt> instance wrapping the last
   *         requested message.
   */
  public JwmaMessage getActualMessage() {
    return m_ActualMessage;
  }//getActualMessage

  /**
   * Returns the message with the given number from the
   * mailfolder.
   *
   * @return the message with the given number as <tt>javax.mail.Message</tt>.
   *
   * @throws <tt>JwmaException</tt> if a message with this number does not exist,
   *         or the message cannot be retrieved from the store.
   */
  private Message getMessage(int num) throws JwmaException {
    try {
      return m_Folder.getMessage(num);
    } catch (MessagingException mex) {
      throw new JwmaException("jwma.folder.getmessage.failed", true)
          .setException(mex);
    }
  }//getMessage

  /**
   * Returns the numbers of all read messages within
   * this instance.
   *
   * @return array of int's representing the message numbers of
   *         all read messages.
   * @throws JwmaException if retrieving the numbers from the mailfolder fails.
   */
  public int[] getReadMessages()
      throws JwmaException {

    int[] readmsg = new int[0];

    try {
      m_Folder.open(Folder.READ_ONLY);
      Message[] messages = m_Folder.getMessages();
      ArrayList readlist = new ArrayList(messages.length);

      for (int i = 0; i < messages.length; i++) {
        if (messages[i].isSet(Flags.Flag.SEEN)) {
          readlist.add(
              new Integer(messages[i].getMessageNumber())
          );
        }
      }

      readmsg = new int[readlist.size()];
      int c = 0;
      for (Iterator iter = readlist.iterator(); iter.hasNext(); c++) {
        readmsg[c] = ((Integer) iter.next()).intValue();
      }

    } catch (MessagingException ex) {
      //pass it on as JwmaException
      throw new JwmaException("jwma.folder.readmessages");
    } finally {
      //ensure the inbox is closed
      try {
        m_Folder.close(false);
      } catch (MessagingException mex) {
        //closed anyway regarding to specs
      }
    }
    return readmsg;
  }//getReadMessages

  /**
   * Returns the number of the next message (relative to the actual)
   * in this folder, observing the actual sorting.
   * The method returns -1 if the actual message is the last message
   * in this folder.
   *
   * @return the number of the next message as <tt>int</tt> or -1 if
   *         the actual message is the last message in this folder.
   *
   */
  public int getNextMessageNumber() {
    return m_MessageInfoList.getNextMessageNumber(
        m_ActualMessage.getMessageNumber()
    );
  }//getNextMessageNumber

  /**
   * Returns the number of the previous message (relative to the actual)
   * in this folder, observing the actual sorting.
   * The method returns -1 if the actual message is the first message
   * in this folder.
   *
   * @return the number of the previous message as <tt>int</tt> or -1 if
   *         the actual message is the first message in this folder.
   *
   */
  public int getPreviousMessageNumber() {
    return m_MessageInfoList.getPreviousMessageNumber(
        m_ActualMessage.getMessageNumber()
    );
  }//getPreviousMessageNumber


  /**
   * Tests if a message with the given number exists in this folder.
   *
   * @return true if it exists, false otherwise.
   */
  public boolean checkMessageExistence(int number) {
    return (number >= 1 && number <= getMessageCount());
  }//checkMessageExistence

  /**
   * Returns a <tt>JwmaMessage</tt> instance that wraps
   * the mailmessage with the given number.
   *
   * @param num the number of the message to be retrieved as <tt>int</tt>.
   * @return the <tt>JwmaMessage</tt> instance wrapping the retrieved message.
   * @throws JwmaException if the message does not exist, or
   *         cannot be retrieved from the store.
   */
  public JwmaMessage getJwmaMessage(int num)
      throws JwmaException {
    try {
      //we want to change the seen flag if JwmaMessage created
      //m_Folder.open(Folder.READ_WRITE);
      if (!m_Folder.isOpen()) {
      	m_Folder.open(Folder.READ_WRITE);
  	  } else {
  		m_Folder.close(false);
  		m_Folder.open(Folder.READ_WRITE);
  	  }
      //get message and create wrapper
      Message msg = getMessage(num);
      JwmaMessage message =
          JwmaDisplayMessage.createJwmaDisplayMessage(msg, m_Store.getPreferences());

      //set seen flag if not set already
      if (!msg.isSet(Flags.Flag.SEEN)) {
        msg.setFlag(Flags.Flag.SEEN, true);
      }
      //close without expunge
      m_Folder.close(false);
      //store the actual message
      m_ActualMessage = message;

      //rebuild the file list
      return message;
    } catch (MessagingException mex) {
      throw new JwmaException("jwma.folder.jwmamessage", true).setException(mex);
    } finally {
      try {
        //ensure closed the folder
        if (m_Folder.isOpen()) {
          m_Folder.close(false);
        }
      } catch (MessagingException mesx) {
        //don't care, the specs say it IS closed anyway
      }
    }
  }//getJwmaMessage

  public JwmaMessage getDraftMessage(int num) throws JwmaException {
    //get message and create wrapper
    try {

      m_Folder.open(Folder.READ_WRITE);//to ensure message can be changed
      Message[] msg = {getMessage(num)};
      if (m_DraftProfile == null) {
        m_DraftProfile = new FetchProfile();
        m_DraftProfile.add(FetchProfile.Item.ENVELOPE);		//contains the headers
        m_DraftProfile.add(FetchProfile.Item.FLAGS);		//contains the flags;
        m_DraftProfile.add(FetchProfile.Item.CONTENT_INFO); //contains the content info
      }
      m_Folder.fetch(msg, m_DraftProfile);
      return JwmaComposeMessage.createDraft(msg[0]);

    } catch (MessagingException ex) {
      throw new JwmaException("jwma.folder.draftmessage", true).setException(ex);
    } finally {
      try {
        //ensure closed the folder
        if (m_Folder.isOpen()) {
          m_Folder.close(false);
        }
      } catch (MessagingException mesx) {
        //don't care, the specs say it IS closed anyway
      }
    }

  }//getDraftMessage

  /**
   * Returns an array of <tt>JwmaMessageInfo[]</tt> listing
   * the info's of all messages in this folder.
   * The method actually delegates the job to the associated
   * <tt>JwmaMessageInfoListImpl</tt> instance.
   *
   * @return an array of <tt>JwmaMessageInfo</tt> instances.
   *
   * @see dtw.webmail.model.JwmaMessageInfoListImpl#listMessageInfos()
   */
  public JwmaMessageInfo[] listMessageInfos() {
	  if(m_MessageInfoList != null){
		  return m_MessageInfoList.listMessageInfos();
	  }else {
		  return null;
	  }
  }//getMessageInfoList

  /**
   * Returns an array of <tt>JwmaMessageInfo[]</tt> listing
   * the info's of all messages in this folder.
   * The method actually delegates the job to the associated
   * <tt>JwmaMessageInfoListImpl</tt> instance.
   *
   * @return an array of <tt>JwmaMessageInfo</tt> instances.
   *
   * @see dtw.webmail.model.JwmaMessageInfoListImpl#listMessageInfos()
   */
  public List<JwmaMessageInfo> getMessageInfos(){
	  return m_MessageInfoList.getMessageInfos();
  }
  
  
  /**
   * Returns the <tt>JwmaMessageInfoListImpl</tt> instance that
   * contains a list of stored <tt>JwmaMessageInfoImpl</tt> references
   * wrapping information about the messages in the wrapped mailfolder.
   *
   * @return the list containing the <tt>JwmaMessageInfoImpl</tt> references.
   */
  public JwmaMessageInfoListImpl getMessageInfoList() {
    return m_MessageInfoList;
  }//getMessageInfoList

  /**
   * Deletes the actual message from this folder.
   *
   * @return the number of the next message or -1 if there is none.
   *
   * @throws JwmaException if it fails to delete the message from the store.
   */
  public int deleteActualMessage()
      throws JwmaException {

    //remember the next message (or if ther is none)
    int nextmsgnum = getNextMessageNumber() - 1;

    //delete message
    deleteMessage(m_ActualMessage.getMessageNumber());

    return nextmsgnum;
  }//deleteActualMessage

  /**
   * Deletes the given message from this folder.
   * Note that this is a convenience method that creates an array with a
   * single entry, and calls <tt>deleteMessage(int[])</tt>.
   *
   * @param number the number of the message to be deleted as <tt>int</tt>.
   *
   * @throws JwmaException if it fails to delete the given message.
   *
   * @see #deleteMessages(int[])
   */
  public void deleteMessage(int number) throws JwmaException {
    int[] nums = {number};
    deleteMessages(nums);
  }//deleteMessage

  /**
   * Deletes all messages from this folder.
   * Note that this is a convenience method that creates an array with a
   * all message numbers, and calls <tt>deleteMessage(int[])</tt>.
   *	 *
   * @throws JwmaException if it fails to delete any of the messages.
   *
   * @see #deleteMessages(int[])
   */
  public void deleteAllMessages() throws JwmaException {

    int[] msgnumbers = new int[getMessageCount()];
    for (int i = 0; i < getMessageCount(); i++) {
      msgnumbers[i] = i + 1;
    }
    deleteMessages(msgnumbers);
  }//deleteAllMessages

  /**
   * Deletes the messages with the given numbers.
   *
   * @param numbers the messages to be deleted as <tt>int[]</tt>.
   *
   * @throws JwmaException if it fails to delete any of the given messages.
   */
  public void deleteMessages(int[] numbers)
      throws JwmaException {

    //dont work with null or empty arrays
    if (numbers == null || numbers.length == 0) {
      return;
    }

    try {
      Folder trashFolder = null;
      if(m_Store.getTrashInfo() != null){
    	  trashFolder = m_Store.getTrashFolder();
      } else {
    	  m_Store.prepareTrashFolder();
    	  trashFolder = m_Store.getTrashFolder();
      }
      /*if(trashFolder == null){
    	  m_Store.prepareTrashFolder();
    	  trashFolder = m_Store.getTrashFolder();
      }*/
      if (!m_Folder.isOpen()) {
    	  m_Folder.open(Folder.READ_WRITE);
      } else {
    	  if(m_Folder.getMode() == 1){
    		  m_Folder.close(false); 
    		  m_Folder.open(Folder.READ_WRITE);
    	  }
      }
      Message[] msgs = m_Folder.getMessages(numbers);
      if (msgs.length != 0) {
        //if not the trash copy the messages to the trash
        /*if (!m_Path.equals(trashFolder.getFullName())) {
          m_Folder.copyMessages(msgs, m_Store.getTrashFolder());
        }*/
        //flag deleted, so when closing with expunge
        //the messages are erased.
        m_Folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
      }
      //close with expunge
      m_Folder.close(true);
      //update messagelist:
      //deleted and already flagged and now expunged messages
      //m_MessageInfoList.remove(numbers);
      //m_MessageInfoList.removeDeleted();
      if (!m_Folder.isOpen()) {
    	  m_Folder.open(Folder.READ_ONLY);
      } 
      //renumber to reflect standard behaviour
      //m_MessageInfoList.renumber(m_Folder.getMessageCount(),numbers);

    } catch (MessagingException mex) {
      throw new JwmaException("jwma.folder.deletemessage.failed", true)
          .setException(mex);
    } finally {
      try {
        //ensure closed the folder
        if (m_Folder.isOpen()) {
          m_Folder.close(false);
        }
      } catch (MessagingException mesx) {
        //don't care, the specs say it IS closed anyway
      }
    }
  }//deleteMessages

  /**
   * Moves the actual message to the given destination folder.
   *
   * @param destfolder the path of the destination folder as <tt>String</tt>.
   * @return the number of the next message, or -1 if there is none.
   *
   * @throws JwmaException if it fails to move the message.
   *
   * @see #moveMessage(int,String)
   */
  public int moveActualMessage(String destfolder) throws JwmaException {

    //remember if next messagenum (or if there is one at all)
    int nextmsgnum = getNextMessageNumber() - 1;

    //delete message
    moveMessage(m_ActualMessage.getMessageNumber(), destfolder);

    return nextmsgnum;
  }//moveActualMessage

  /**
   * Moves the given message to the given destination folder.
   * Note that this is actually a convenience method. It wraps
   * the message number into an array and calls
   * <tt>moveMessages(int[],String)</tt>.
   *
   * @param destfolder the path of the destination folder as <tt>String</tt>.
   *
   * @return the number of the next message, or -1 if there is none.
   *
   * @throws JwmaException if it fails to move the message.
   *
   * @see #moveMessages(int[],String)
   */
  public void moveMessage(int number, String destfolder) throws JwmaException {
    int[] nums = {number};
    moveMessages(nums, destfolder);
  }//moveMessage

  /**
   * Moves the messages with the given numbers to the given destination folder.
   *
   * @param numbers the messages to be moved as <tt>int[]</tt>.
   * @param destfolder the destination folder path as <tt>String</tt>.
   *
   * @throws JwmaException if it fails to move if the destination folder does not
   *         exist, or if any of the given messages cannot be moved.
   */
  public void moveMessages(int[] numbers, String destfolder)
      throws JwmaException {

    //dont work with null or empty arrays
    if (numbers == null || numbers.length == 0
        || destfolder == null || destfolder.length() == 0) {
      return;
    }
    //JwmaKernel.getReference().debugLog().write("Moving msgs to->"+destfolder+"<-");
    try {
      if (!m_Store.checkFolderExistence(destfolder)) {
        throw new JwmaException(
            "jwma.folder.movemessage.destination.missing", true
        );
      }
      Folder dest = m_Store.getFolder(destfolder);
      //check destination type
      if (dest.getType() == JwmaFolder.TYPE_FOLDER) {
        throw new JwmaException("jwma.folder.movemessage.destination.foul", true);
      }
      //Note that apidocs state that only source
      //has to be opened
      m_Folder.open(Folder.READ_WRITE);
      //prepare messages
      Message[] msgs = m_Folder.getMessages(numbers);
      if (msgs.length != 0) {
        m_Folder.copyMessages(msgs, dest);
        m_Folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
      }
      m_Folder.close(true);
      //update messagelist:
      //deleted and already flagged and now expunged messages
      m_MessageInfoList.remove(numbers);
      m_MessageInfoList.removeDeleted();
      m_MessageInfoList.renumber(m_Folder.getMessageCount(),numbers);
    } catch (MessagingException mex) {
      throw new JwmaException(
          "jwma.folder.movemessage.failed", true
      ).setException(mex);
    } finally {
      try {
        //ensure closed the folder
        if (m_Folder.isOpen()) {
          m_Folder.close(false);
        }
      } catch (MessagingException mesx) {
        //don't care, the specs say it IS closed anyway
      }
    }
  }//moveMessages

  /*** End Messages related ******************************************************/

  /*** MessageParts related ******************************************************/

  /**
   * Writes the given message part from the given message to the given
   * output stream.
   *
   * @param part the part to be written to the output stream.
   * @param out the <tt>OutputStream</tt> to be written to.
   *
   * @throws IOException if an I/O related error occurs.
   * @throws JwmaException if the message part does not exist, or
   *         cannot be retrieved from the message.
   */
  public void writeMessagePart(Part part, OutputStream out)
      throws IOException, JwmaException {

    try {
      m_Folder.open(Folder.READ_ONLY);

      InputStream in = part.getInputStream();

      int i;
      while ((i = in.read()) != -1) {
        //this is not very efficient, should
        //write in blocks (byte[]'s)
        out.write(i);
      }
      out.flush();

    } catch (MessagingException mex) {
      throw new JwmaException("message.displaypart.failed")
          .setException(mex);
    } finally {
      try {
        if (m_Folder.isOpen()) {
          m_Folder.close(false);
        }
      } catch (MessagingException ex) {
        log.error(ex.getMessage(), ex);
        //JwmaKernel.getReference().debugLog().writeStackTrace(ex);
      }
    }
  }//writeMessagePart

  /*** End MessageParts related ******************************************************/

  /**
   * Tests if this folder instance equals a given object.
   * Overrides the superclass behaviour to compare the folder's
   * paths in case the given object is a JwmaFolderImpl.
   *
   * @param o the object to compare this folder with.
   *
   * @return true if the paths are equal, false otherwise, or when the
   *         object is not an instance of <tt>JwmaFolderImpl</tt>.
   */
  public boolean equals(Object o) {
    if (o instanceof JwmaFolderImpl) {
      return this.getPath().equals(((JwmaFolderImpl) o).getPath());
    } else {
      return false;
    }
  }//equals


  /**
   * Prepares this folder instance.
   * This method fills in values from the wrapped mailfolder instance
   * and creates the cached subfolder list and the cached messages list.
   *
   * @throws JwmaException if it fails to retrieve values from the wrapped instance, or
   *         if it fails to create the subfolder list.
   */
  public void prepare()
      throws JwmaException {
	  int startcount = 1;
    try {
    	int emailLimit = Integer.parseInt(PropertyReader.getInstance()
				.getPropertyFromFile("Integer", "system.email.limit"));
      //set basic data
      m_Name = m_Folder.getName();
      m_Path = m_Folder.getFullName();
     // m_Type = m_Folder.getType(); //Commanted By ramachandran.Because of yahoo mail connection
      if (!m_Folder.isOpen()) {
    	  m_Folder.open(Folder.READ_ONLY);
      }
      oldMessageCount = m_Folder.getMessageCount();
      int messageCount = m_Folder.getMessages().length;
      if(messageCount > emailLimit){
   		 startcount = messageCount - emailLimit;
      }
   	 
      
      
    /*  Message[] msgs = m_Folder.getMessages(startcount, m_Folder.getMessages().length);
    //fetch messages with a slim profile
		 FetchProfile fp = new FetchProfile();
		 fp.add(FetchProfile.Item.ENVELOPE);		//contains the headers
		 fp.add(FetchProfile.Item.FLAGS);
		 fp.add(FetchProfile.Item.CONTENT_INFO);//contains the flags
		 m_Folder.fetch(msgs, fp);*/
	  Message[] messages = m_Folder.getMessages(startcount, m_Folder.getMessages().length);
      Message[] inboxMessages = m_Folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false), messages);
      if(inboxMessages != null){
    	  setInboxUnReadMessages(inboxMessages.length);
      } else {
    	  setInboxUnReadMessages(0);
      }
      if (m_Folder.isOpen()) {
    	  m_Folder.close(false);
      }
      //create folder list
      m_Subfolders = JwmaFolderList.createSubfolderList(m_Folder);
      if (isType(JwmaFolder.TYPE_MAILBOX) || isType(JwmaFolder.TYPE_MIXED)) {
        //create m_MessageInfoList, or set null
        //m_MessageInfoList = JwmaMessageInfoListImpl.createJwmaMessageInfoListImpl(m_Folder);
        //m_MessageInfoList.buildMessageInfos(m_Folder);
       /* if (!m_Folder.isOpen()) {
        	m_Folder.open(Folder.READ_ONLY);
    	}
        Thread messageInfoThread = new Thread(new MessageInfoThread(m_Folder, m_Folder.getMessages(), m_MessageInfoList));
		messageInfoThread.start();*/
      }
    } catch (MessagingException mex) {
    	mex.printStackTrace();
      throw new JwmaException(
          "jwma.folder.failedcreation", true
      ).setException(mex);
    } catch (Exception e) {
		// TODO: handle exception
	}
  }//prepare

  public void refreshFolder(Folder folder, int newMessageCount) throws JwmaException, MessagingException{
	    m_MessageInfoList.refreshMessageInfoList(folder, newMessageCount);
	    if(!folder.isOpen()){
	    	folder.open(Folder.READ_ONLY);
	    }
    	oldMessageCount = folder.getMessageCount();
    	if(folder.isOpen()){
	    	folder.close(false);
	    }
  }
  /**
   * Updates this <tt>JwmaFolderImpl</tt> instance by setting the store instance
   * reference and calling <tt>update()</tt>.
   * This method can be used on a primarily lightweight created folder, to get it fully
   * prepared for extended use.
   *
   * @param store the reference to the store this folder belongs to.
   *
   * @throws JwmaException if the <tt>update()</tt> method fails.
   *
   * @see #prepare()
   */
  public void update(JwmaStoreImpl store) throws JwmaException {
    m_Store = store;
    prepare();
  }//update

  /**
   * Creates a <tt>JwmaFolderImpl</tt> instance from the given
   * <tt>Folder</tt>.
   *
   * @param f mail <tt>Folder</tt> this instance will "wrap".
   *
   * @return the newly created instance.
   *
   * @throws JwmaException if it fails to create the new instance.
   */
  public static JwmaFolderImpl createJwmaFolderImpl(JwmaStoreImpl store, Folder f)
      throws JwmaException {

    JwmaFolderImpl folder = new JwmaFolderImpl(f, store);
    folder.prepare();
    return folder;
  }//createJwmaFolderImpl

  /**
   * Creates a <tt>JwmaFolderImpl</tt> instance from the given
   * <tt>Folder</tt>.
   *
   * @param f mail <tt>Folder</tt> this instance will "wrap".
   *
   * @return the newly created instance.
   *
   * @throws JwmaException if it fails to create the new instance.
   */
  public static JwmaFolderImpl createJwmaFolderImpl(JwmaStoreImpl store, String fullname)
      throws JwmaException {

    //FIXME: What if...the folder name is ""
    JwmaFolderImpl folder = new JwmaFolderImpl(store.getFolder(fullname), store);
    folder.prepare();
    return folder;
  }//createJwmaFolderImpl


  /**
   * Creates a <tt>JwmaFolderImpl</tt> instance from the given
   * <tt>Folder</tt>.
   *
   * @param folder mail <tt>Folder</tt> this instance will "wrap".
   *
   * @return the newly created instance.
   *
   * @throws JwmaException if it fails to create the new instance.
   */
  public static JwmaFolderImpl createLight(Folder folder)
      throws JwmaException {

    try {
      JwmaFolderImpl jwmafolder = new JwmaFolderImpl(folder);

      jwmafolder.setName(folder.getName());
      jwmafolder.setPath(folder.getFullName());
      jwmafolder.setType(folder.getType());
      return jwmafolder;

    } catch (MessagingException mex) {
      throw new JwmaException(
          "jwma.folder.failedcreation", true
      ).setException(mex);
    }
  }//createJwmaFolderImpl

}//JwmaFolderImpl
