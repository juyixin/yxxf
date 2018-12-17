/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;


import javax.mail.*;
import javax.mail.event.*;

import org.apache.log4j.Logger;
import dtw.webmail.*;

import java.io.Serializable;
import java.util.Date;


/**
 * Class that implements a <tt>Transport</tt> wrapper.
 * <p>
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaTransportImpl implements Serializable{

  private static final Logger log = Logger.getLogger(JwmaTransportImpl.class);

  private Transport m_Transport;
  private URLName m_URLName;
  private JwmaAuthenticator m_Authenticator;


  private JwmaTransportImpl(Transport trans, JwmaAuthenticator auth) {
    m_Transport = trans;
    m_URLName = trans.getURLName();
    m_Authenticator = auth;
  }//JwmaTransportImpl

  public void sendMessage(Message msg)
      throws MessagingException {
    if (!m_Transport.isConnected()) {
      m_Transport.connect(
          m_URLName.getHost(),
          m_URLName.getPort(),
          m_Authenticator.getUserName(),
          m_Authenticator.getPassword()
      );
      log.debug("Connected to transport:" + m_URLName.toString());
    }
    //ensure that a sent date is set
    msg.setSentDate(new Date());
    m_Transport.sendMessage(msg, msg.getAllRecipients());
  }//sendMessage

  public static JwmaTransportImpl createJwmaTransportImpl(Transport trans,
                                                          JwmaAuthenticator auth) {
    return new JwmaTransportImpl(trans, auth);
  }//createJwmaTransportImpl

  class TransportHandler
      extends TransportAdapter {


  }//inner class TransportHandler

}//class JwmaTransportImpl
