/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.util.*;
import java.io.IOException;
import java.io.Serializable;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

//import dtw.webmail.JwmaKernel;
import dtw.webmail.util.EntityHandler;

/**
 * Class implementing the JwmaMessage model.
 * <p>
 * This implementation is specialized to wrap a
 * <tt>javax.mail.Message</tt> for displaying it.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaDisplayMessage implements JwmaMessage, Serializable {

  //logging
  private static Logger log = Logger.getLogger(JwmaDisplayMessage.class);

  //instance attributes
  private Message m_Message;
  private boolean m_Received;
  private String m_FullHeader;
  private int m_Number;
  private Date m_SentDate;
  private Date m_ReceivedDate;
  private String m_From;
  private String m_ReplyTo;
  private String m_To;
  private String m_Subject;
  private String m_Body;
  private boolean m_Singlepart;
  private String m_CC;
  private String m_BCC;
  private JwmaMessagePart[] m_MessageParts;

  /**
   * Constructs a <tt>JwmaDisplayMessage</tt> instance.
   *
   * @param number the number of the message as <tt>int</tt>
   */
  protected JwmaDisplayMessage(Message msg, int number) {
    m_Message = msg;
    m_Number = number;
  }//constructor

  public Message getMessage() {
    return m_Message;
  }//getMessage

  /**
   * Returns the full message header as <tt>String</tt>.
   *
   * @return Full message header as <tt>String</tt>.
   */
  public String getFullHeader() {
    return m_FullHeader;
  }//getFullHeader

  /**
   * Sets the full message header.
   *
   * @param header the full message header as <tt>String</tt>.
   */
  private void setFullHeader(String header) {
    m_FullHeader = header;
  }//setFullHeader

  public int getMessageNumber() {
    return m_Number;
  }//getMessageNumber

  public boolean isReceived() {
    return m_Received;
  }//isReceived

  /**
   * Flags the message as a received message.
   *
   * @param true if the message was received,
   *         false otherwise.
   */
  private void setReceived(boolean b) {
    m_Received = b;
  }//setReceived

  public boolean isSent() {
    return !m_Received;
  }//isSent

  public Date getReceivedDate() {
    return m_ReceivedDate;
  }//getDate

  /**
   * Sets the received date of this message.
   *
   * @param d the date when this message was received.
   */
  private void setReceivedDate(Date d) {
    m_ReceivedDate = d;
  }//setReceivedDate

  public Date getSentDate() {
    return m_SentDate;
  }//getSentDate

  /**
   * Sets the <tt>date</tt> associated with this message.
   *
   * @param d the <tt>Date</tt> associated with this message.
   */
  private void setSentDate(Date d) {
    m_SentDate = d;
  }//setSentDate

  public Date getDate() {
    if (isReceived() && m_ReceivedDate != null) {
      return m_ReceivedDate;
    } else if (isSent() && m_SentDate != null) {
      return m_SentDate;
    } else {
      return new Date();
    }
  }//getDate


  public String getFrom() {
    return m_From;
  }//getFrom

  /**
   * Sets the sender's address(es) of the message as
   * <tt>String</tt>.
   *
   * @param from the sender(s) address(es) of the message as String.
   */
  private void setFrom(String from) {
    m_From = from;
  }//setFrom

  public String getReplyTo() {
    return m_ReplyTo;
  }//getReplyTo

  /**
   * Set the Reply-To address(es) of the message.
   *
   * @param from the Reply-To address(es) of the message
   *        as <tt>String</tt>.
   */
  public void setReplyTo(String replyto) {
    m_ReplyTo = replyto;
  }//setReplyTo

  public String getTo() {
    return m_To;
  }//getTo

  /**
   * Sets the receiver's address(es) of the message as
   * <tt>String</tt>.
   *
   * @param to the receiver(s) address(es) of the message as String.
   */
  private void setTo(String to) {
    m_To = to;
  }//setTo

  public String getCCTo() {
    return m_CC;
  }//getCC

  /**
   * Sets the carbon copy receiver's address(es) of the message.
   *
   * @param to the carbon copy receiver(s) address(es) of the
   *        message as String.
   */
  private void setCCTo(String cc) {
    m_CC = cc;
  }//setCC

  public String getBCCTo() {
    return m_BCC;
  }//getBCCTo

  /**
   * Sets the blind carbon copy receiver's address(es) of the message.
   *
   * @param to the blind carbon copy receiver(s) address(es) of the
   *        message as String.
   */
  private void setBCCTo(String bcc) {
    m_BCC = bcc;
  }//setBCC

  public String getSubject() {
    return m_Subject;
  }//getSubject

  /**
   * Sets the subject of the message.
   *
   * @param subject the subject of the message as <tt>String</tt>.
   */
  private void setSubject(String subject) {
    try {
      if (subject == null) {
        m_Subject = "";
      } else {
        m_Subject = MimeUtility.decodeText(subject);
      }
    } catch (Exception ex) {
      m_Subject = "";
    }
  }//setSubject

  public boolean isSinglepart() {
    return m_Singlepart;
  }//isSinglepart

  /**
   * Flags the message as a singlepart message.
   *
   * @param true if the message should be flagged singlepart,
   *         false otherwise.
   */
  private void setSinglepart(boolean b) {
    m_Singlepart = b;
  }//setSinglepart

  public boolean isMultipart() {
    return !m_Singlepart;
  }//isMultipart

  public String getBody() {
    return m_Body;
  }//getBody

  /**
   * Sets the body of the message.
   *
   * @param body the text/plain content of the message as
   *        <tt>String</tt>.
   */
  public void setBody(String body) {
    m_Body = body;
  }//setBody;

  /**
   * Returns the message part with the given number.
   *
   * @param number the number of the requested part as <tt>int</tt>.
   *
   * @return the reference to wrapper instance of the requested part.
   *
   * @throws JwmaException if the part does not exist.
   */
  public JwmaMessagePart getMessagePart(int number)
      throws JwmaException {
    if (number < 0 || number > getMessageParts().length) {
      throw new JwmaException("message.displaypart.failed");
    } else {
      return getMessageParts()[number];
    }
  }//getMessagePart

  public JwmaMessagePart[] getMessageParts() {
    return m_MessageParts;
  }//getMessageParts

  /**
   * Sets the associated <tt>JwmaMessagePart</tt> objects.
   *
   * @param parts an array of JwmaMessagePart objects each representing
   *  	   information about a part of this message.
   *
   * @see dtw.webmail.model.JwmaMessagePart
   */
  private void setMessageParts(JwmaMessagePart[] parts) {
    m_MessageParts = parts;
  }//setMessageParts

  private static void buildPartInfoList(List partlist, Multipart mp, boolean isDublicatePart, boolean isReceived)
      throws Exception, JwmaException {
    for (int i = 0; i < mp.getCount(); i++) {
      //Get part
      Part apart = mp.getBodyPart(i);
      //handle single & multiparts
      if (apart.isMimeType("multipart/*")) {
        //recurse
        buildPartInfoList(partlist, (Multipart) apart.getContent(), true, isReceived);
      } else {
        //append the part
    	if(apart.isMimeType("text/plain") && apart.getFileName() == null && isReceived == true){
    		/*if(apart.getFileName() != null){
		        partlist.add(
		            JwmaMessagePartImpl.createJwmaMessagePartImpl(apart, partlist.size())
		        );
	    	} */
    	} else {
    		partlist.add(
		            JwmaMessagePartImpl.createJwmaMessagePartImpl(apart, partlist.size())
		        );
    	}
      }
    }
  }//buildPartList

  private static String getAddressesAsString(Address[] addr)
      throws Exception {
    if (addr != null && addr.length > 0) {
     /* return EntityHandler.encode(
          MimeUtility.decodeText(
              InternetAddress.toString(addr)
          )
      );*/
      return  MimeUtility.decodeText(InternetAddress.toString(addr));
    } else {
      return "";
    }
  }//getAddressAsString

  /**
   * Creates a <tt>JwmaDisplayMessage</tt> instance.
   * At the moment it just delegates the call to
   * <tt>createJwmaDisplayMessage(Message,boolean)</tt>, but the
   * idea is to recycle the passed in <tt>JwmaMessageInfoImpl</tt> instance
   * somewhen.
   *
   * @param msginfo the <tt>JwmaMessageInfoImpl</tt> to create
   *        this instance from.
   * @param msg the <tt>Message</tt> to create this instance from.
   * @param prefs the user's preferences.
   *
   * @return the created <tt>JwmaDisplayMessage</tt> instance.
   *
   * @throws JwmaException if it fails to create the new instance.
   */
  public static JwmaDisplayMessage createJwmaDisplayMessage(
      JwmaMessageInfoImpl msginfo, Message msg, JwmaPreferences prefs) throws JwmaException {

    //FIXME: recycle existing information once!
    return createJwmaDisplayMessage(msg, prefs);
  }//create JwmaDisplayMessage


  /**
   * Creates a <tt>JwmaDisplayMessage</tt> instance.
   *
   * @param msg the <tt>Message</tt> to create this instance from.
   * @param prefs the user's preferences.
   *
   * @return the newly created instance.
   *
   * @throws JwmaException if it fails to create the new instance.
   */
  public static JwmaDisplayMessage createJwmaDisplayMessage(Message msg,
                                                            JwmaPreferences prefs)
      throws JwmaException {

    JwmaDisplayMessage message = null;

    try {

      //create instance with number
      message = new JwmaDisplayMessage(msg, msg.getMessageNumber());

      //retrieve full header
      StringBuffer fullhead = new StringBuffer();
      for (Enumeration enumeration = ((MimeMessage) msg).getAllHeaderLines();
      	enumeration.hasMoreElements();) {
        fullhead.append((String) enumeration.nextElement())
            .append("\n");
      }
      //probably is better to see the "raw" headers, instead of
      //decoded ones.
      message.setFullHeader(
          EntityHandler.encode(
              fullhead.toString()
          )
      );
      //senders and receivers
      message.setFrom(getAddressesAsString(
          msg.getFrom())
      );
      message.setReplyTo(getAddressesAsString(
          msg.getReplyTo())
      );

      message.setTo(getAddressesAsString(
          msg.getRecipients(Message.RecipientType.TO))
      );
      message.setCCTo(getAddressesAsString(
          msg.getRecipients(Message.RecipientType.CC))
      );
      message.setBCCTo(getAddressesAsString(
          msg.getRecipients(Message.RecipientType.BCC))
      );

      //determine if received, will have a header named Received
      message.setReceived(
          (msg.getHeader("Received") != null)
      );

      message.setReceivedDate(msg.getReceivedDate());
      message.setSentDate(msg.getSentDate());


      //set encoded subject
      String subject = msg.getSubject();
      if (subject != null) {
        message.setSubject(
            EntityHandler.encode(MimeUtility.decodeText(subject))
        );
      }

      //flag attachments or none
      message.setSinglepart(!msg.isMimeType("multipart/*"));

      //handle alternatetively
      if (message.isSinglepart()) {
        //set body as String processed with the users msgprocessor
        try {
          /*message.setBody(
              prefs.getMessageProcessor().process(
                  msg.getContent().toString()
              )
          );*/
        	
        	message.setBody(msg.getContent().toString());

        } catch (IOException ex) {
          //handle!?
          message.setBody(
              "System puzzled by corrupt singlepart message."
          );
        }
      } else {
        try {
          //get main body part
          Multipart mp = (Multipart) msg.getContent();

          //build flatlist
          List partlist = new ArrayList(10);
          buildPartInfoList(partlist, mp, false, message.isReceived());

          //set flatlist
          JwmaMessagePart[] parts = new JwmaMessagePart[partlist.size()];
          message.setMessageParts(
              (JwmaMessagePart[]) partlist.toArray(parts)
          );
        } catch (IOException ex) {
          //handle!?
          message.setSinglepart(true);
          message.setBody("System puzzled by corrupt multipart message.");
        }
      }
      return message;
    } catch (Exception mex) {
      throw new JwmaException("jwma.displaymessage.failedcreation", true).setException(mex);
    }
  }//createJwmaDisplayMessage

}//class JwmaDisplayMessage
