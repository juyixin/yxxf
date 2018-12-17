/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.util.Date;

/**
 * An interface defining the contract for interaction with
 * the JwmaMessageInfo model.
 * <p>
 * The JwmaMessageInfo allows a view programmer to obtain
 * enough information about a message to show it in a list
 * of messages. It could be considered as a lightweight proxy
 * interface, concentrated on providing information for lists
 * of messages.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 *
 * @see dtw.webmail.model.JwmaFolder
 */
public interface JwmaMessageInfo {

  /**
   * Returns an <tt>int</tt> representing the number
   * of this message.
   * <p>
   * This number is the unique identifier for a message within
   * a folder, and should be used on subsequent display, move
   * or delete actions.
   *
   * @return the number of this message.
   */
  public int getMessageNumber();

  /**
   * Tests if the message was already read.
   *
   * @return true if the message was read,
   *         false otherwise.
   */
  public boolean isRead();

  /**
   * Tests if the message was answered.
   *
   * @return true if the message was answered,
   *         false otherwise.
   */
  public boolean isAnswered();

  /**
   * Tests if the message was marked for deletion.
   *
   * @return true if the message was marked for deletion,
   *         false otherwise.
   */
  public boolean isDeleted();

  /**
   * Tests if the message is new.
   *
   * @return true if the message is new,
   *         false otherwise.
   */
  public boolean isNew();

  /**
   * Tests if the message is a draft.
   *
   * @return true if the message is a draft,
   *         false otherwise.
   */
  public boolean isDraft();

  /**
   * Tests if the message was received.
   *
   * @return true if the message was received,
   *         false otherwise.
   */
  public boolean isReceived();

  /**
   * Tests if the message was sent.
   * <p>
   * Note that this method will always return the opposite of
   * isReceived() (i.e. represents !getReceived()).
   *
   * @return true if the message was sent,
   *         false otherwise.
   */
  public boolean isSent();


  /**
   * Convenience method that returns a <tt>String</tt>
   * representing the sender's or receiver's address of the message.
   * (Depending on whether it was sent or received).
   *
   * @return the sender's or receiver's address of the message.
   */
  public String getWho();


  /**
   * Returns a <tt>String</tt> representing the sender(s)
   * of the message.
   *
   * @return the sender(s) of the message as String.
   */
  public String getFrom();

  /**
   * Returns a <tt>String</tt> representing the receivers(s)
   * of the message.
   *
   * @return the receiver(s) of the message as String.
   */
  public String getTo();

  /**
   * Convenience method that returns a <tt>Date</tt>
   * representing the received or sent date of the message.
   * (Depending on whether it was sent or received).
   *
   * @return the received or sent date of the message.
   */
  public Date getDate();

  /**
   * Returns a <tt>Date</tt> representing the
   * date when the message was received.
   *
   * @return the received date of the message.
   */
  public Date getReceivedDate();

  /**
   * Returns a <tt>Date</tt> representing the
   * date when the message was sent.
   *
   * @return the sent date of the message.
   */
  public Date getSentDate();


  /**
   * Returns a <tt>String</tt> representing the subject
   * of the message.
   *
   * @return the subject of the message as String.
   */
  public String getSubject();

  /**
   * Tests if the message is singlepart.
   * <p>
   * A singlepart message does not have any
   * attachments.
   *
   * @return true if the message is singlepart,
   *         false otherwise.
   */
  public boolean isSinglepart();

  /**
   * Tests if the message is multipart.
   * <p>
   * A multipart message has attachments
   * or is composed out of different parts.
   * Note that this method will always return the opposite
   * of isSinglepart() (i.e. represents !isSinglepart()).
   *
   * @return true if the message is multipart,
   *         false otherwise.
   */
  public boolean isMultipart();

  /**
   * Returns the size of the message in bytes.
   *
   * @return size of message in bytes as <tt>int</tt>.
   */
  public int getSize();


}//class JwmaMessageInfo
