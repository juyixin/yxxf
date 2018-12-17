/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.eazytec.common.util.CipherUtils;

import sun.misc.BASE64Decoder;

//import dtw.webmail.JwmaKernel;

/**
 * Class implementing the JwmaMessagePart model.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaMessagePartImpl
    implements JwmaMessagePart, Serializable {

  //logging
  private static Logger log = Logger.getLogger(JwmaMessagePartImpl.class);

  //instance attributes
  private String m_ContentType;
  private String m_Description;
  private String m_Name;
  private String m_TextContent = "";
  private int m_Number;
  private int m_Size;
  private Part m_Part;

  /**
   * Private empty constructor, to prevent construction.
   */
  private JwmaMessagePartImpl() {
  }//constructor

  /**
   * Constructs a <tt>JwmaMessagePartImpl</tt> with a
   * given part and number.
   *
   * @param part the part that is wrapped.
   * @param number an <tt>int</tt> that represents the part number.
   */
  private JwmaMessagePartImpl(Part p, int number) {
    m_Part = p;
    m_Number = number;
  }//constructor

  /**
   * Constructs a <tt>JwmaMessagePartImpl</tt> with a
   * given part number.
   *
   * @param number an <tt>int</tt> that represents the part number.
   */
  private JwmaMessagePartImpl(int number) {
    m_Number = number;
  }//constructor

  public int getPartNumber() {
    return m_Number;
  }//getNumber

  public boolean isMimeType(String type) {
    if (m_Part != null) {
      try {
        return m_Part.isMimeType(type);
      } catch (MessagingException mex) {
        log.error("isMimeType()", mex);
      }
    }
    //FIX: probably evaluate based on m_ContentType otherwise
    return false;

  }//isMimeType

  public String getContentType() {
    return m_ContentType;
  }//getContentType

  /**
   * Gets the <tt>javax.mail.Part</tt> wrapped by this instance.
   *
   * @return the wrapped part instance.
   */
  public Part getPart() {
    return m_Part;
  }//getPart

  public String getTextContent() {
    return m_TextContent;
  }//getTextContent

  private void setTextContent(String text) {
    m_TextContent = text;
  }//setTextContent


  /**
   * Sets the content-type (mime) of this <tt>JwmaMessagePart</tt>.
   *
   * @param type the content type of this <tt>JwmaMessagePart</tt>
   *        as <tt>String</tt>.
   */
  public void setContentType(String type) {
    m_ContentType = type;
  }//setContentType

  public int getSize() {
    return m_Size;
  }//getSize

  /**
   * Sets the size of this <tt>JwmaMessagePart</tt>.
   *
   * @param size of this <tt>JwmaMessagePart</tt> in bytes.
   */
  private void setSize(int size) {
    m_Size = size;
  }//setSize

  public String getName() {
    return m_Name;
  }//getName

  /**
   * Sets the name of this <tt>JwmaMessagePart</tt>.
   *
   * @param name the name of this <tt>JwmaMessagePart</tt>
   *        as <tt>String</tt>.
   */
  public void setName(String name) {
    m_Name = name;
  }//setName

  public String getDescription() {
    return m_Description;
  }//getDescription

  /**
   * Sets the description of this <tt>JwmaMessagePart</tt>.
   *
   * @param description of this <tt>JwmaMessagePart</tt>
   *        as <tt>String</tt>.
   */
  public void setDescription(String description) {
    m_Description = description;
  }//setDescription

  /**
   * Creates a <tt>JwmaMessagePartImpl</tt> instance from a given
   * <tt>javax.mail.Part</tt> instance.
   *
   * @param part a <tt>javax.mail.Part</tt> instance.
   * @param number the number of the part as <tt>int</tt>.
   *
   * @return the newly created instance.
   * @throws JwmaException if it fails to create the new instance.
   */
  public static JwmaMessagePartImpl createJwmaMessagePartImpl(Part part, int number)
      throws JwmaException {
    JwmaMessagePartImpl partinfo =
        new JwmaMessagePartImpl(part, number);

    //content type
    try {
      partinfo.setContentType(part.getContentType());

      
      //size
      int size = part.getSize();
      //JwmaKernel.getReference().debugLog().write("Part size="+size);
      String fileName = part.getFileName();
      //correct size of encoded parts
      String[] encoding = part.getHeader("Content-Transfer-Encoding");
      if (fileName!= null && encoding != null &&
          encoding.length > 0 && (
          encoding[0].equalsIgnoreCase("base64")
          || encoding[0].equalsIgnoreCase("uuencode"))
      ) {
    	  if((fileName.startsWith("=?GB2312?B?")&&fileName.endsWith("?="))){  
    		  byte[] decoded = Base64.decodeBase64(fileName.substring(11,fileName.indexOf("?=")).getBytes());
    		  fileName = new String(decoded, "GB2312");
    		  /*fileName = CipherUtils.decrypt(fileName);
    		  System.out.println("fileName----"+fileName);*/
    		  //fileName = getFromBASE64(fileName.substring(11,fileName.indexOf("?=")));  
		  } else if((fileName.startsWith("=?UTF-8?") || fileName.startsWith("=?UTF8?")) && fileName.endsWith("?=")) {
			  fileName = MimeUtility.decodeText(fileName);
		  }
        //an encoded file is about 35% smaller in reality,
        //so correct the size
        size = (int) (size * 0.65);
      }

      partinfo.setSize(size);
      //description
      partinfo.setDescription(part.getDescription());

      //filename
      partinfo.setName(fileName);

      //textcontent
      if (partinfo.isMimeType("text/*")) {
        Object content = part.getContent();
        if (content instanceof String) {
          partinfo.setTextContent((String) content);
        } else if (content instanceof InputStream) {
          InputStream in = (InputStream) content;
          ByteArrayOutputStream bout = new ByteArrayOutputStream();
          byte[] buffer = new byte[8192];
          int amount = 0;
          while ((amount = in.read(buffer)) >= 0) {
            bout.write(buffer, 0, amount);
          }
          partinfo.setTextContent(
              new String(bout.toString())
          );
        }
      }
    } catch (Exception mex) {
      throw new JwmaException(
          "jwma.messagepart.failedcreation", true
      ).setException(mex);
    }
    return partinfo;
  }//createJwmaMessagePartImpl

  public static String getFromBASE64(String s) {  
      if (s == null) return null;  
      BASE64Decoder decoder = new BASE64Decoder();  
      try {  
        byte[] b = decoder.decodeBuffer(s);  
        return new String(b);  
      } catch (Exception e) {  
        return null;  
      }  
    } 
  
  /**
   * Creates a <tt>JwmaMessagePartImpl</tt> instance with a given
   * part number.
   *
   * @param number the number of the part as <tt>int</tt>.
   *
   * @return the newly created instance.
   */
  public static JwmaMessagePartImpl createJwmaMessagePartImpl(int number) {
    return new JwmaMessagePartImpl(number);
  }//createMessagePartImpl

}//class JwmaMessagePartImpl
