/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

/**
 * An interface defining the contract for interaction with
 * the JwmaInboxInfo model.
 * <p>
 * The JwmaInboxInfo allows a view programmer to obtain
 * information about the inbox; basically if there are new
 * messages (and their count).
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public interface JwmaInboxInfo {

  /**
   * Returns a <tt>String</tt> representing the name
   * of the Inbox.
   *
   * @return the name of the Inbox as String.
   */
  public String getName();

  /**
   * Returns a <tt>String</tt> representing the path
   * of this folder object.
   *
   * @return the path of the inbox as String.
   */
  public String getPath();

  /**
   * Returns an <tt>int</tt> representing the count
   * of messages in the Inbox.
   * <p><i><b>Note:</b> method returns -1 if it fails
   * to retrieve the actual messages count.</i>
   *
   * @return the number of messages in this Inbox.
   */
  public int getMessageCount();

  /**
   * Tests if the Inbox contains new messages.
   *
   * @return true if the Inbox contains new messages,
   *         false otherwise.
   */
  public boolean hasNewMessages();

  /**
   * Returns an <tt>int</tt> representing the count
   * of <em>new</em> messages in the Inbox.
   * <p><i><b>Note:</b> method returns -1 if it fails
   * to retrieve the actual new messages count.</i>
   *
   * @return the number of <em>new</em> messages in the Inbox.
   */
  public int getNewMessageCount();

   /**
   * Tests if the Inbox contains new messages, based on
   * the flag for read messages.
   *
   * @return true if the Inbox contains unread messages,
   *         false otherwise.
   */
  public boolean hasUnreadMessages();

  /**
   * Returns an <tt>int</tt> representing the count
   * of <em>unread</em> messages in the Inbox.
   * <p>
   * This method is based on the flag for read messages
   * It returns -1 if it fails to retrieve the actual
   * message count.
   *
   * @return the number of <em>unread</em> messages in the Inbox.
   */
  public int getUnreadMessageCount();

}//interface JwmaInboxInfo
