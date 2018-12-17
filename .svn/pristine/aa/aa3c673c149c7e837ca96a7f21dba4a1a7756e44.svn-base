/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.util.config;

import org.apache.log4j.Logger;

import java.util.Properties;
import java.net.InetAddress;
import java.io.Serializable;

/** 
 * This class...
 * <p>
 * @author Dieter Wimberger (wimpi)
 * @version (created Feb 13, 2003)
 */
public class PostOffice
    implements Serializable {

  //class attributes
  private static Logger log = Logger.getLogger(PostOffice.class);

  //instance attributes
  private String m_Name="Default";
  private String m_Address="localhost";
  private int m_Port=-1; //protocol default
  private String m_RootFolder="";
  private boolean m_Secure=false;
  private String m_Type="mixed";
  private String m_Protocol="imap";
  private boolean m_Default = false;
  private String m_ReplyToDomain;

  /**
   * Constructs a new <tt>PostOffice</tt> instance.
   */
  public PostOffice() {
  }//constructor

  /**
   * Constructs a new <tt>PostOffice</tt> instance.
   */
  public PostOffice(String m_Name, String m_Address, int m_Port, String m_RootFolder, boolean m_Secure, String m_Type, String m_Protocol, boolean m_Default, String m_ReplyToDomain) {
	  this.m_Name=m_Name;
	  this.m_Address=m_Address;
	  this.m_Port=m_Port; //protocol default
	  this.m_RootFolder=m_RootFolder;
	  this.m_Secure=m_Secure;
	  this.m_Type=m_Type;
	  this.m_Protocol=m_Protocol;
	  this.m_Default = m_Default;
	  this.m_ReplyToDomain = m_ReplyToDomain;
  }//constructor
  
  /**
   * Returns a common name for this <tt>PostOffice</tt>.
   * Naming for different post offices should be kept unique.
   *
   * @return the name as <tt>String</tt>.
   */
  public String getName() {
    return m_Name;
  }//getName

  /**
   * Sets the common name for this <tt>PostOffice</tt>.
   *
   * @param name the name as <tt>String</tt>.
   */
  public void setName(String name) {
    m_Name = name;
  }//setName

  /**
   * Returns the protocol required for
   * this <tt>PostOffice</tt>.
   *
   * @return the protocol as <tt>String</tt>.
   */
  public String getProtocol() {
    return m_Protocol;
  }//getProtocol

  /**
   * Sets the protocol required for
   * this <tt>PostOffice</tt>.
   *
   * @param protocol the protocol as <tt>String</tt>.
   */
  public void setProtocol(String protocol) {
    m_Protocol = protocol;
  }//setProtocol

  /**
   * Returns the type of this <tt>PostOffice</tt>.
   *
   * @return the type as <tt>String</tt>.
   * @see #TYPE_MIXED
   * @see #TYPE_PLAIN
   */
  public String getType() {
    return m_Type;
  }//getType

  /**
   * Sets the type of this <tt>PostOffice</tt>.
   *
   * @param type the type as <tt>String</tt>.
   * @see #TYPE_MIXED
   * @see #TYPE_PLAIN
   */
  public void setType(String type) {
    m_Type = type;
  }//setType

  /**
   * Tests if this <tt>PostOffice</tt> is of a
   * given type.
   *
   * @return true if of the given type, false otherwise.
   * @see #TYPE_MIXED
   * @see #TYPE_PLAIN
   */
  public boolean isType(String type) {
    return m_Type.equals(type);
  }//isType

  /**
   * Returns the internet address of this <tt>PostOffice</tt>.
   *
   * @return the address as <tt>String</tt>.
   */
  public String getAddress() {
    return m_Address;
  }//getAddress

  /**
   * Sets the internet address of this <tt>PostOffice</tt>.
   *
   * @param address the address as <tt>String</tt>.
   */
  public void setAddress(String address) {
    try {
      if (address == null ||
        address.equals("") ||
        address.equals("localhost")) {
        m_Address = InetAddress.getLocalHost().getHostName();
      }
    } catch (Exception ex) {
      log.error("setAddress()",ex);
    }
    m_Address = address;
  }//setAddress

  /**
   * Returns the port of this <tt>PostOffice</tt>.
   *
   * @param the port as <tt>int</tt>.
   */
  public int getPort() {
    return m_Port;
  }//getPort

  /**
   * Sets the port of this this <tt>PostOffice</tt>.
   *
   * @param port the port as <tt>int</tt>
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort

  /**
   * Tests if the communication with this <tt>PostOffice</tt>
   * should be secure.
   *
   * @return true if secure, false otherwise.
   * @see #getSecureSocketFactory()
   */
  public boolean isSecure() {
    return m_Secure;
  }//isSecure

  /**
   * Sets the communication with this <tt>PostOffice</tt>
   * to be secure.
   *
   * @param secure true if secure, false otherwise.
   * @see #setSecureSocketFactory(String factory)
   */
  public void setSecure(boolean secure) {
    m_Secure = secure;
  }//setSecure

  /**
   * Returns the default root folder
   * this <tt>PostOffice</tt>.
   *
   * @return the folder as <tt>String</tt>.
   */
  public String getRootFolder() {
    return m_RootFolder;
  }//getRootFolder

  /**
   * Sets the default root folder for
   * this <tt>PostOffice</tt>.
   *
   * @param folder the folder as <tt>String</tt>.
   */
  public void setRootFolder(String folder) {
    m_RootFolder = folder;
  }//setRootFolder

  /**
   * Returns the reply-to domain setting for
   * this <tt>PostOffice</tt>.
   *
   * @return the reply-to domain as <tt>String</tt>.
   */
  public String getReplyToDomain() {
    return m_ReplyToDomain;
  }//getReplyToDomain

  /**
   * Sets the reply-to domain for
   * this <tt>PostOffice</tt>.
   *
   * @param domain the reply-to domain as <tt>String</tt>.
   */
  public void setReplyToDomain(String domain) {
    m_ReplyToDomain = domain;
  }//setReplyToDomain

  /**
   * Tests if this <tt>PostOffice</tt> is
   * the default.
   *
   * @return true if default, false otherwise.
   */
  public boolean isDefault() {
    return m_Default;
  }//isDefault

  /**
   * Sets the default flag for this <tt>PostOffice</tt>.
   */
  public void setDefault(boolean aDefault) {
    m_Default = aDefault;
  }//setDefault

  /**
   * Defines a mixed type post office, were
   * folders in the store can have subfolders and messages
   * at the same time.
   */
  public static final String TYPE_MIXED="mixed";

  /**
   * Defines a plain type post office, were
   * folders in the store cannot have subfolders and messages
   * at the same time.
   */
  public static final String TYPE_PLAIN="plain";

}//class PostOffice
