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

/** 
 * This class...
 * <p>
 * @author Dieter Wimberger (wimpi)
 * @version (created Feb 13, 2003)
 */
public class MailTransportAgent
    implements Serializable {

  //instance attributes
  private String m_Name="Default";
  private String m_Address="localhost";
  private int m_Port=-1; //protocol default
  private boolean m_Secure=false;
  private String m_SSLFactory="";
  private String m_Protocol="smtp";
  private boolean m_Authenticated=false;
  private int m_TransportLimit=2048;

  /**
   * Constructs a new <tt>MailTransportAgent</tt> instance.
   */
  public MailTransportAgent() {

  }//constructor

  /**
   * Constructs a new <tt>MailTransportAgent</tt> instance.
   */
  public MailTransportAgent(String m_Name, String m_Address, int m_port, boolean m_secure, String m_SSLFactory, String m_Protocol, boolean m_Authenticated){
	  this.m_Name = m_Name;
	  this.m_Address = m_Address;
	  this.m_Port = m_port;
	  this.m_Secure = m_secure;
	  this.m_SSLFactory = m_SSLFactory;
	  this.m_Protocol = m_Protocol;
	  this.m_Authenticated = m_Authenticated;
	  this.m_TransportLimit = 2048;
	  
  }//constructor
  
  /**
   * Returns a common name for this <tt>MailTransportAgent</tt>.
   * Naming for different transport agents should be kept unique.
   *
   * @return the name as <tt>String</tt>.
   */
  public String getName() {
    return m_Name;
  }//getName

  /**
   * Sets the common name for this <tt>MailTransportAgent</tt>.
   *
   * @param name the name as <tt>String</tt>.
   */
  public void setName(String name) {
    m_Name = name;
  }//setName

  /**
   * Returns the protocol required for
   * this <tt>MailTransportAgent</tt>.
   *
   * @return the protocol as <tt>String</tt>.
   */
  public String getProtocol() {
    return m_Protocol;
  }//getProtocol

  /**
   * Sets the protocol required for
   * this <tt>MailTransportAgent</tt>.
   *
   * @param protocol the protocol as <tt>String</tt>.
   */
  public void setProtocol(String protocol) {
    m_Protocol = protocol;
  }//setProtocol

  /**
   * Returns the internet address of this <tt>MailTransportAgent</tt>.
   *
   * @return the address as <tt>String</tt>.
   */
  public String getAddress() {
    return m_Address;
  }//getAddress

  /**
   * Sets the internet address of this <tt>MailTransportAgent</tt>.
   *
   * @param address the address as <tt>String</tt>.
   */
  public void setAddress(String address) {
    m_Address = address;
  }//setAddress

  /**
   * Returns the port of this <tt>MailTransportAgent</tt>.
   *
   * @param the port as <tt>int</tt>.
   */
  public int getPort() {
    return m_Port;
  }//getPort

  /**
   * Sets the port of this this <tt>MailTransportAgent</tt>.
   *
   * @param port the port as <tt>int</tt>
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort

  /**
   * Tests if the communication with this <tt>MailTransportAgent</tt>
   * should be secure.
   *
   * @return true if secure, false otherwise.
   * @see #getSecureSocketFactory()
   */
  public boolean isSecure() {
    return m_Secure;
  }//isSecure

  /**
   * Sets the communication with this <tt>MailTransportAgent</tt>
   * to be secure.
   *
   * @param secure true if secure, false otherwise.
   * @see #setSecureSocketFactory(String factory)
   */
  public void setSecure(boolean secure) {
    m_Secure = secure;
  }//setSecure

  /**
   * Tests if this <tt>MailTransportAgent</tt> should
   * be used with authentication.
   *
   * @return true if authenticated use, false otherwise.
   */
  public boolean isAuthenticated() {
    return m_Authenticated;
  }//isAuthenticated

  /**
   * Sets the use of this <tt>MailTransportAgent</tt> to be
   * authenticated.
   *
   * @param auth true if authenticated use, false otherwise.
   */
  public void setAuthenticated(boolean auth) {
    m_Authenticated = auth;
  }//setAuthenticated

  /**
   * Returns the maximum message size in kB's allowed for
   * transport.
   *
   * @return maximum size as <tt>int</tt>.
   */
  public int getTransportLimit() {
    return m_TransportLimit;
  }//getTransportLimit

  /**
   * Sets the maximum message size in kB's allowed for
   * transport.
   *
   * @param size the maximum size as <tt>int</tt>.
   */
  public void setTransportLimit(int size) {
    m_TransportLimit = size;
  }//setTransportLimit

}//class MailTransportAgent
