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
 * the JwmaMessage model.
 * <p>
 * The JwmaMessage allows a view programmer to obtain
 * information about a message to display it for reading
 * or composing.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 *
 */
public interface JwmaMessage {

  /**
   * Returns the full message header as <tt>String</tt>.
   *
   * @return the full message header as <tt>String</tt>.
   */
  public String getFullHeader();

  /**
   * Returns an <tt>int</tt> representing the number
   * of this message.
   * <p>
   * This number is the unique identifier for a message within
   * a folder, or <tt>-1</tt> in case of a message created
   * for being composed.
   *
   * @return the number of this message, or -1 if newly created
   *         for composing.
   */
  public int getMessageNumber();

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
   * Returns a <tt>String</tt> representing the sender(s)
   * of the message.
   *
   * @return the sender(s) of the message as String.
   */
  public String getFrom();

  /**
   * Returns a <tt>String</tt> representing the Reply-To
   * address(es) of the message.
   *
   * @return the Reply-To address(es) of the message as String.
   */
  public String getReplyTo();


  /**
   * Returns a <tt>String</tt> representing the receivers(s)
   * of the message.
   *
   * @return the receiver(s) of the message as String.
   */
  public String getTo();

  /**
   * Returns a <tt>String</tt> representing the
   * carbon copy receivers(s) of the message.
   *
   * @return the carbon copy receiver(s) of the
   *         message as String.
   */
  public String getCCTo();

  /**
   * Returns a <tt>String</tt> representing the
   * blind carbon copy receivers(s) of the message.
   *
   * @return the blind carbon copy receiver(s) of the
   *         message as String.
   */
  public String getBCCTo();

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
   * Returns a <tt>String</tt> representing the body
   * of the message.
   * <p>
   * Note that the body will be the (plain text) content of
   * a singlepart message. The method will return an empty
   * <tt>String</tt> for a multipart message.
   * <p>
   * A view programmer should base display decisions on
   * the <tt>isSinglepart()</tt> (or <tt>isMultipart()</tt>
   * method.
   *
   * @return the content of the message as String.
   *
   * @see #isSinglepart()
   * @see #isMultipart()
   */
  public String getBody();

  /**
   * Returns an array of <tt>JwmaMessagePart</tt> objects.
   * <p>
   * If this message does not contain any parts, then this
   * method returns an empty array. Otherwise it contains
   * one <tt>JwmaMessagePart</tt> object for each part
   * of this message representing information about the part.
   * <p>
   * A view programmer should base display decisions on
   * the <tt>isSinglepart()</tt> (or <tt>isMultipart()</tt>
   * method.
   *
   * @return an array of JwmaMessagePart objects each representing
   *  	   information about a part of this message.
   *         The array will be empty if this message has no
   *         parts.
   *
   * @see dtw.webmail.model.JwmaMessagePart
   * @see #isSinglepart()
   * @see #isMultipart()
   */
  public JwmaMessagePart[] getMessageParts();

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

}//interface JwmaMessage
