/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

/**
 * An interface defining the contract for interaction with
 * the JwmaMessagePart model.
 * <p>
 * The JwmaMessagePart allows a view programmer to obtain
 * information about a part of a multipart message.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public interface JwmaMessagePart {

  /**
   * Returns an <tt>int</tt> representing the number
   * of this message part.
   * <p>
   * This number is the unique identifier for a part of
   * a message.
   *
   * @return the number of this message part.
   */
  public int getPartNumber();

  /**
   * Returns a <tt>String</tt> representing the
   * content type of this message part.
   *
   * @return the content type of this message part
   *         as String.
   */
  public String getContentType();

  /**
   * Tests if this <tt>JwmaMessagePart</tt> is of
   * the given type.
   *
   * @param type the Mime type as <tt>String</tt>.
   */
  public boolean isMimeType(String type);

  /**
   * Returns a <tt>String</tt> representing the
   * description of this message part.
   *
   * @return description of this message part
   *         as String.
   */
  public String getDescription();

  /**
   * Returns a <tt>String</tt> representing the
   * name of this message part.
   *
   * @return name of this message part
   *         as String.
   */
  public String getName();

  /**
   * Returns an <tt>int</tt> representing the size
   * of this message part in bytes.
   *
   * @return the size of this message part in bytes.
   */
  public int getSize();


}//JwmaMessagePart
