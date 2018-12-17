/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.util.List;

/**
 * An interface defining the contract for interaction with
 * the JwmaStoreInfo model.
 *
 * <p>
 * The JwmaStoreInfo allows a view programmer to obtain
 * information regarding the mail store (such as the mode,
 * the folder separator in use etc.).
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public interface JwmaStoreInfo {

  /**
   * Tests if the store can hold mixed mode folders.
   *
   * @return true if it can hold mixed mode folders,
   *         false otherwise.
   */
  public boolean isMixedMode();

  /**
   * Returns the folder separator in use as <tt>char</tt>.
   *
   * @return the folder separator character.
   */
  public char getFolderSeparator();

  /**
   * Returns a <tt>JwmaFolder[]</tt> containing all
   * folders on the store of a given type.
   * <p>
   * If the store does not contain any folders of the given type,
   * then this method returns an empty array. Otherwise it contains
   * one <tt>JwmaFolder</tt> for each folder on the store of
   * the given type.
   *
   * @return a <tt>JwmaFolder[]</tt> naming all folders of
   *         given type on the store. The array will be empty if
   *         there are no such folders.
   *
   * @see dtw.webmail.model.JwmaFolder
   */
  public JwmaFolder[] listFolders(int type);

  /**
   * Returns a <tt>JwmaFolder[]</tt> containing all
   * folders on the store of a given type.
   * Note that this method will observe subscription if
   * the passed in parameter is true.
   * <p>
   * If the store does not contain any folders of the given type,
   * then this method returns an empty array. Otherwise it contains
   * one <tt>JwmaFolder</tt> for each folder on the store of
   * the given type.
   *
   * @param type the type of folder as <tt>int</tt>.
   * @param subscribed true if observe subscription, false otherwise.
   *
   * @return a <tt>JwmaFolder[]</tt> naming all folders of
   *         given type on the store. The array will be empty if
   *         there are no such folders.
   *
   * @see dtw.webmail.model.JwmaFolder
   */
  public JwmaFolder[] listFolders(int type, boolean subscribed);

  /**
   * Returns a <tt>JwmaFolder[]</tt> containing all folders
   * of the store, that can hold folders, excluding this folder.
   * <p>
   * If the store does not contain any folders, then this method
   * returns an empty array (which means something is wrong!;).
   * Otherwise it contains one <tt>JwmaFolder</tt> for each
   * folder on the store, excluding this folder.
   *
   * @return a <tt>JwmaFolder[]</tt> containing all folders
   *  	   in the store that can hold folders, excluding this one.
   *		   The array will be empty if there are no such folders.
   *
   * @see dtw.webmail.model.JwmaFolder
   */
  public JwmaFolder[] listFolderMoveTargets();

  /**
   * Returns a <tt>JwmaFolder[]</tt> containing all folders
   * of the store, that can hold messages, excluding this folder.
   * <p>
   * If the store does not contain any folders, then this method
   * returns an empty array (which means something is wrong!;).
   * Otherwise it contains one <tt>JwmaFolder</tt> for each
   * folder on the store, excluding this folder.
   *
   * @return a <tt>JwmaFolder[]</tt> containing all folders
   *  	   in the store that can hold messages, excluding this one.
   *		   The array will be empty if there are no such folders.
   *
   * @see dtw.webmail.model.JwmaFolder
   */
  public JwmaFolder[] listMessageMoveTargets();

}//interface JwmaStoreInfo
