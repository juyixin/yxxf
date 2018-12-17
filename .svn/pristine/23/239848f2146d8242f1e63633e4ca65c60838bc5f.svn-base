/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import javax.mail.Folder;

/**
 * An interface defining the contract for interaction with
 * the JwmaFolder model.
 * <p>
 * The JwmaFolder allows a view programmer to obtain
 * information about a folder.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public interface JwmaFolder {

  /**
   * Returns a <tt>String</tt> representing the name
   * of this folder.
   *
   * @return the name of this folder as String.
   */
  public String getName();

  /**
   * Returns a <tt>String</tt> representing the path
   * of this folder object.
   *
   * @return the path of this folder as String.
   */
  public String getPath();

  /**
   * Returns an <tt>int</tt> representing the type
   * of this folder.
   *
   * @return the type of this folder object as <tt>int</tt>.
   */
  public int getType();

  /**
   * Tests if this folder is of a given type.
   *
   * @return true if this folder is of the given type, false otherwise.
   */
  public boolean isType(int type);

  /**
   * Tests if this folder contains subfolders.
   *
   * @return true if this folder contains subfolders, false otherwise.
   */
  public boolean hasSubfolders();

  /**
   * Tests if this folder object contains messages.
   *
   * @return true if this folder contains messages, false otherwise.
   */
  public boolean hasMessages();

  /**
   * Tests if this folder is subscribed.
   *
   * @return true if subscribed, false otherwise.
   */
  public boolean isSubscribed();

  /**
   * Returns a<tt>JwmaFolder[]</tt> containing all subfolders of
   * the given type within this folder, observing subscription.
   * <p>
   * If the store does not contain any matching folder, then this
   * method returns an empty array. Otherwise it contains
   * one <tt>JwmaFolder</tt> for each subfolder of the given type.
   *
   * @return a <tt>JwmaFolder[]</tt> containing all subfolders of
   *         the given type within this folder. The array will be
   *         empty if there are none.
   */
  public JwmaFolder[] listSubfolders(int type, boolean subscribed);

  /**
   * Returns a<tt>JwmaFolder[]</tt> containing all subfolders of
   * the given type within this folder.
   * <p>
   * If the store does not contain any matching folder, then this
   * method returns an empty array. Otherwise it contains
   * one <tt>JwmaFolder</tt> for each subfolder of the given type.
   *
   * @return a <tt>JwmaFolder[]</tt> containing all subfolders of
   *         the given type within this folder. The array will be
   *         empty if there are none.
   */
  public JwmaFolder[] listSubfolders(int type);

  /**
   * Convenience method that returns a<tt>JwmaFolder[]</tt>
   * containing all subfolders within this folder.
   * <p>
   * If this folder does not contain any subfolder, then this
   * method returns an empty array. Otherwise it contains
   * one <tt>JwmaFolder</tt> for each subfolder.
   *
   * @return a <tt>JwmaFolder[]</tt> containing all subfolders of
   *         this folder. The array will be empty if there are none.
   */
  public JwmaFolder[] listSubfolders();

  /**
   * Returns a <tt>JwmaMessageInfo[]</tt>.
   * <p>
   * If this folder does not contain any messages, then this
   * method returns an empty array. Otherwise it contains
   * one <tt>JwmaMessageInfo</tt> instance for each message
   * in this folder, encapsulating all necessary information
   * for list displaying of the message.
   *
   * @return a <tt>JwmaMessageInfo[]</tt> containing a info instance
   *         for each message in this folder. The array will be empty if
   *         there are no messages in this folder.
   *
   * @see dtw.webmail.model.JwmaMessageInfo
   */
  public JwmaMessageInfo[] listMessageInfos();


  /**
   * Defines folder type that can only hold messages.
   */
  public static final int TYPE_MAILBOX = Folder.HOLDS_MESSAGES;

  /**
   * Defines folder type that can only hold folders.
   */
  public static final int TYPE_FOLDER = Folder.HOLDS_FOLDERS;

  /**
   * Defines folder type that can hold messages and folders.
   */
  public static final int TYPE_MIXED = TYPE_MAILBOX + TYPE_FOLDER;

  /**
   * Defines a virtual type that represents all folders that
   * can hold messages.
   */
  public static final int TYPE_MESSAGE_CONTAINER = TYPE_MAILBOX + TYPE_MIXED;

  /**
   * Defines a virtual type that represents all folders that can
   * hold folders.
   */
  public static final int TYPE_FOLDER_CONTAINER = TYPE_FOLDER + TYPE_MIXED;

  /**
   * Defines a virtual type that represents all of the above.
   */
  public static final int TYPE_ALL = 10;


}//JwmaFolder
