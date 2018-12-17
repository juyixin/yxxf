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
 * the JwmaTrashInfo model.
 * <p>
 * The JwmaTrashInfo allows a view programmer to obtain
 * information about the trash folder.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public interface JwmaTrashInfo {

  /**
   * Returns a <tt>String</tt> representing the name
   * of the trash folder.
   *
   * @return the name of the trash folder as <tt>String</tt>.
   */
  public String getName();

  /**
   * Returns a <tt>String</tt> representing the path
   * of the trash folder.
   *
   * @return the path of the trash folder as <tt>String</tt>.
   */
  public String getPath();

  /**
   * Tests if the trash is empty.
   *
   * @return true if the trash is empty, false otherwise.
   */
  public boolean isEmpty();

}//interface JwmaTrashInfo
