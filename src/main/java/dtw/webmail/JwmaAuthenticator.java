/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail;

import javax.mail.*;
import java.io.Serializable;

/**
 * Class that implements an authenticator.
 * <p>
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaAuthenticator
    extends Authenticator {

  private PasswordAuthentication m_Authentication;

  public JwmaAuthenticator(String username, String password) {
    m_Authentication = new PasswordAuthentication(username, password);
  }//constructor

  protected PasswordAuthentication getPasswordAuthentication() {
    //FIXME: Handle/extend to check for usage
    return m_Authentication;
  }//getPasswordAuthentication

  /**
   * Returns the password associated with this
   * Authenticator.
   */
  public String getPassword() {
    return m_Authentication.getPassword();
  }//getPassword

  /**
   * Returns the username associated with this
   * Authenticator.
   *
   */
  public String getUserName() {
    return m_Authentication.getUserName();
  }//getUsername

}//JwmaAuthenticator
