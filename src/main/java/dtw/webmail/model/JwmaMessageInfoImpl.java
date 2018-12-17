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
import java.util.Date;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import dtw.webmail.util.EntityHandler;

//import dtw.webmail.JwmaKernel;

/**
 * Class implementing the JwmaMessageInfo model.
 * It is designed to wrap the minimum information of
 * a mail message necessary to be listed in a list view.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaMessageInfoImpl implements JwmaMessageInfo, Serializable {

  //logging
  private static Logger log = Logger.getLogger(JwmaMessageInfoImpl.class);

  //instance attributes
  private Integer m_Number;
  private boolean m_Read;
  private boolean m_Answered;
  private boolean m_Recent;
  private boolean m_Deleted;
  private boolean m_Draft;
  private boolean m_Received;
  private boolean m_Singlepart;
  private Date m_ReceivedDate;
  private Date m_SentDate;
  private String m_From;
  private String m_To;
  private String m_Subject;
  private int m_Size;

  /**
   * Constructs a new <tt>JwmaMessageInfoImpl</tt>.
   */
  private JwmaMessageInfoImpl(int number) {
    setMessageNumber(number);
  }//constructor

  public int getMessageNumber() {
    return m_Number.intValue();
  }//getMessageNumber

  /**
   * Returns the message's number as <tt>Integer</tt>.
   *
   * This method is only used internally, and supposed
   * to save a lot of resources, due to the fact that the
   * standard collection sort <tt>Comparator</tt> does not
   * need to create the wrapper instances on the fly for every
   * comparison.
   *
   * @returns the message's number as <tt>Integer</tt>.
   *
   * @see dtw.webmail.util.MessageSortingUtil#NUMBER_NUMERICAL
   * @see dtw.webmail.util.MessageSortingUtil#NUMBER_REVERSE_NUMERICAL
   */
  public Integer getNumberForSort() {
    return m_Number;
  }//getNumberForSort

  /**
   * Sets the message number of this MessageInfo.
   * This method is public to allow caching in the <tt>JwmaMessageInfoListImpl</tt>.
   * The number should reflect the number of the wrapped <tt>javax.mail.Message</tt>
   * instance.
   *
   * @param num the number as <tt>int</tt>.
   *
   * @see dtw.webmail.model.JwmaMessageInfoListImpl#renumber()
   */
  public void setMessageNumber(int num) {
    m_Number = new Integer(num);
  }//setMessageNumber

  public boolean isRead() {
    return m_Read;
  }//isRead

  /**
   * Sets the read flag of this MessageInfo.
   * It flags if the wrapped message was already read.
   *
   * @param b true if read, false otherwise.
   */
  private void setRead(boolean b) {
    m_Read = b;
  }//setRead

  public boolean isDraft() {
    return m_Draft;
  }//isDraft

  /**
   * Sets the draft flag of this MessageInfo.
   * It flags if the wrapped message is a draft.
   *
   * @param b true if draft, false otherwise.
   */
  private void setDraft(boolean b) {
    m_Draft = b;
  }//setDraft

  public boolean isAnswered() {
    return m_Answered;
  }//isAnswered

  /**
   * Sets the answered flag of this MessageInfo.
   * It flags if the wrapped message was answered.
   *
   * @param b true if answered, false otherwise.
   */
  private void setAnswered(boolean b) {
    m_Answered = b;
  }//setAnswered

  public boolean isDeleted() {
    return m_Deleted;
  }//isDeleted

  /**
   * Sets the deleted flag of this MessageInfo.
   * It flags if the wrapped message was deleted.
   *
   * @param b true if deleted, false otherwise.
   */
  private void setDeleted(boolean b) {
    m_Deleted = b;
  }//setDeleted

  public boolean isNew() {
    return m_Recent;
  }//isNew


  /**
   * Sets the new flag of this MessageInfo.
   * Flags if the wrapped message is new.
   *
   * @param b true if new, false otherwise.
   */
  private void setNew(boolean b) {
    m_Recent = b;
  }//setNew

  public boolean isReceived() {
    return m_Received;
  }//isReceived

  /**
   * Sets the received flag of this MessageInfo.
   * Flags if the wrapped message was received.
   *
   * @param true if received, false otherwise.
   */
  private void setReceived(boolean b) {
    m_Received = b;
  }//setReceived

  public boolean isSent() {
    return !m_Received;
  }//isSent

  public Date getDate() {
    if (isReceived() && m_ReceivedDate != null) {
      return m_ReceivedDate;
    } else if (isSent() && m_SentDate != null) {
      return m_SentDate;
    } else {
      return new Date();
    }
  }//getDate

  public Date getReceivedDate() {
    return m_ReceivedDate;
  }//getReceivedDate

  /**
   * Sets the received date of this MessageInfo.
   *
   * @param d the date when the wrapped message was received.
   */
  private void setReceivedDate(Date d) {
    m_ReceivedDate = d;
  }//setReceivedDate

  public Date getSentDate() {
    return m_SentDate;
  }//getSentDate

  /**
   * Sets the sent date of this MessageInfo.
   *
   * @param d the date when the wrapped message was sent.
   */
  private void setSentDate(Date d) {
    m_SentDate = d;
  }//setSentDate

  public String getWho() {
    if (isReceived()) {
      return getFrom();
    } else {
      return "<i>" + getTo() + "</i>";
    }
  }//getWho

  public String getFrom() {
    return m_From;
  }//getFrom

  /**
   * Sets the author's address of this MessageInfo.
   *
   * @param from the address of the author as <tt>String</tt>.
   */
  private void setFrom(String from) {
    m_From = from;
  }//setFrom

  public String getTo() {
    return m_To;
  }//getTo

  /**
   * Sets the receiver's address(es) of this MessageInfo.
   *
   * @param from the address(es) of the receiver(s) as <tt>String</tt>.
   */
  private void setTo(String to) {
    m_To = to;
  }//setTo

  public String getSubject() {
    return m_Subject;
  }//getSubject

  /**
   * Sets the subject of this MessageInfo.
   * Note that the subject will be set to an empty string, if the
   * given String is null, or if it cannot be decoded.
   *
   * @param from the address of the author as <tt>String</tt>.
   */
  private void setSubject(String subject) {
    if (subject == null) {
      m_Subject = "";
    } else {
      m_Subject = subject;
    }
  }//setSubject

  public boolean isSinglepart() {
    return m_Singlepart;
  }//isSinglepart

  public boolean isMultipart() {
    return !m_Singlepart;
  }//isMultipart

  /**
   * Sets the singlepart flag of this MessageInfo.
   * It flags if the message contains attachments.
   *
   * @param b true if singlepart, false otherwise.
   */
  private void setSinglepart(boolean b) {
    m_Singlepart = b;
  }//setSinglepart

  public int getSize() {
    return m_Size;
  }//getSize

  /**
   * Sets the message size for this MessageInfo.
   *
   * @param bytes the size of the message in bytes.
   */
  private void setSize(int bytes) {
    m_Size = bytes;
  }//setSize

  /**
   * Method that prepares the given <tt>String</tt>
   * by decoding it through the <tt>MimeUtility</tt>
   * and encoding it through the <tt>EntitiyHandler</tt>.
   */
  private static String prepareString(String str)
      throws Exception {

    if (str == null) {
      return "";
    } else {
      return (
          EntityHandler.encode(
              MimeUtility.decodeText(
                  str
              )
          )
          );
    }
  }//prepareString

  /**
   * Factoy method that creates a new <tt>JwmaMessageInfoImpl</tt> instance.
   * The passed in message should have been loaded with a slim profile. Values are
   * extracted and set in the newly created instance.
   *
   * @param msg the message to be wrapped  as <tt>javax.mail.Message</tt>.
   */
  public static JwmaMessageInfoImpl createJwmaMessageInfoImpl(Message msg)
      throws JwmaException {

    JwmaMessageInfoImpl messageinfo = null;
    try {
      //create instance with number
      messageinfo = new JwmaMessageInfoImpl(msg.getMessageNumber());

      //set flags
      messageinfo.setNew(msg.isSet(Flags.Flag.RECENT));
      messageinfo.setRead(msg.isSet(Flags.Flag.SEEN));
      messageinfo.setAnswered(msg.isSet(Flags.Flag.ANSWERED));
      messageinfo.setDeleted(msg.isSet(Flags.Flag.DELETED));
      messageinfo.setDraft(msg.isSet(Flags.Flag.DRAFT));

      //determine if received, will have a header named Received
      messageinfo.setReceived(
          (msg.getHeader("Received") != null)
      );

      //senders and receivers
      messageinfo.setFrom(
          prepareString(InternetAddress.toString(msg.getFrom()))
      );

      messageinfo.setTo(
          prepareString(
              InternetAddress.toString(
                  msg.getRecipients(Message.RecipientType.TO)
              )
          )
      );

      //Dates
      messageinfo.setReceivedDate(msg.getReceivedDate());
      messageinfo.setSentDate(msg.getSentDate());

      //subject
      messageinfo.setSubject(
          prepareString(
              msg.getSubject()
          )
      );

      //size
      messageinfo.setSize(msg.getSize());

      //attachments or none
      messageinfo.setSinglepart(!msg.isMimeType("multipart/*"));

    } catch (Exception ex) {
    	ex.printStackTrace();
      throw new JwmaException("jwma.messageinfo.failedcreation");
    }
    return messageinfo;
  }//createJwmaMessageInfoImpl

}//class JwmaMessageInfoImpl
