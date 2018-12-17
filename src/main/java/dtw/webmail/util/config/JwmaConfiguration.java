/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class implementing a wrapper for jwma's settings.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaConfiguration
    implements Serializable {

  //instance attributes
  private boolean m_POAllowOverride = false;
  private List m_PostOffices;
  private MailTransportAgent m_MTA;
  private Security m_Security;
  private boolean m_AutoAccount = true;
  private String m_DefaultMessageProcessor = "cleanmessage";


  /**
   * Constructs a new <tt>JwmaConfiguration</tt> instance.
   */
  public JwmaConfiguration() {
    m_PostOffices = Collections.synchronizedList(new ArrayList(5));
  }//constructor

  public Security getSecurity() {
    return m_Security;
  }//getSecurity

  public void setSecurity(Security security) {
    m_Security = security;
  }//setSecurity

  /*** Postoffice related **********************************************/

  public Iterator getPostOffices() {
    return m_PostOffices.iterator();
  }//getPostOffices

  public Collection getPostOfficeCollection() {
    return m_PostOffices;
  }//getPostOffices

  public void setPostOfficeCollection(Collection collection) {
    m_PostOffices = Collections.synchronizedList(
        new ArrayList(collection));
  }//setPostOffices

  public void addPostOffice(PostOffice po) {
    if(!existsPostOfficeByName(po.getName())) {
      m_PostOffices.add(po);
    }
  }//addPostOffice

  public void removePostOffice(PostOffice po) {
    m_PostOffices.remove(po);
  }//removePostOffice

  public boolean existsPostOfficeByName(String name) {
    for (Iterator iterator = m_PostOffices.iterator(); iterator.hasNext();) {
      if(((PostOffice) iterator.next()).getName().equals(name)) {
        return true;
      }
    }
    return false;
  }//existsPostOfficeByName

  public PostOffice getPostOfficeByName(String name) {
    for (Iterator iterator = m_PostOffices.iterator(); iterator.hasNext();) {
      PostOffice office = (PostOffice) iterator.next();
      if(office.getName().equals(name)) {
        return office;
      }
    }
    return null;
  }//getPostOfficeByName

  /**
   * Tests if overriding the system's set postoffice is allowed.
   *
   * @return true if overriding is allowed, false otherwise.
   */
  public boolean getPostOfficeAllowOverride() {
    return m_POAllowOverride;
  }//getPostOfficeAllowOverride

  /**
   * Sets the flag that controls if overriding the
   * system's set postoffice is allowed or not.
   *
   * @param b true if the overriding is to be allowed,
   *        false otherwise.
   */
  public void setPostOfficeAllowOverride(boolean b) {
    m_POAllowOverride = b;
  }//setPostOfficeAllowOverride

  public PostOffice getDefaultPostOffice() {
    for (Iterator iterator = m_PostOffices.iterator(); iterator.hasNext();) {
      PostOffice office = (PostOffice) iterator.next();
      if(office.isDefault()) {
        return office;
      }
    }
    return null;
  }//getDefaultPostOffice

  public void setDefaultPostOffice(PostOffice ndpo) {

    PostOffice odpo = getDefaultPostOffice();
    odpo.setDefault(false);
    ndpo.setDefault(true);
  }//setDefaultPostOffice

  /*** END Postoffice related ******************************************/

  /*** Mail transport related ******************************************/

  public MailTransportAgent getMTA() {
    return m_MTA;
  }//getMTA

  public void setMTA(MailTransportAgent mta) {
    m_MTA = mta;
  }//setMTA

  /*** END Mail transport related **************************************/

  public boolean isSSLSetupRequired() {
    if(m_MTA.isSecure()) {
      return true;
    }
    for (Iterator iterator = m_PostOffices.iterator(); iterator.hasNext();) {
      if(((PostOffice) iterator.next()).isSecure()) {
        return true;
      }
    }
    return false;
  }//isSSLSetupRequired

  /*** Account related *************************************************/

  /**
   * Tests if creation of accounts is enabled.
   *
   * @return true if account creation is enabled, false otherwise.
   */
  public boolean isAccountCreationEnabled() {
    return m_AutoAccount;
  }//isAccountCreationEnabled

  /**
   * Sets the flag that controls if the automatic creation of
   * jwma accounts is enabled.
   * This will cause jwma to create user specific data, if
   * the user can be authenticated against the IMAP server.
   *
   * @param b true if account creation is enabled, false otherwise.
   */
  public void setAccountCreationEnabled(boolean b) {
    m_AutoAccount = b;
  }//setAccountCreationEnabled

  /*** End Account related *********************************************/

  /*** Processing related **********************************************/

  /**
   * Returns a <tt>String</tt> representing the name of the default
   * message processor.
   *
   * @return the name of the default message processor as
   *         <tt>String</tt>.
   */
  public String getDefaultMessageProcessor() {
    return m_DefaultMessageProcessor;
  }//getDefaultMessageProcessor

  /**
   * Returns a <tt>String</tt> representing the name of the default
   * message processor.
   *
   * @return the name of the default message processor as
   *         <tt>String</tt>.
   */
  public void setDefaultMessageProcessor(String name) {
    m_DefaultMessageProcessor = name;
  }//setDefaultMessageProcessor

  /*** END Processing related ******************************************/


  /**
   * Defines a name of the static jwma direcory architecture.
   */
  public static final String ETC_DIR = "etc";
  public static final String I18N_DIR = "i18n";
  public static final String JTEXTPROC_CONFIG = "textproc.properties";

}//class JwmaConfiguration