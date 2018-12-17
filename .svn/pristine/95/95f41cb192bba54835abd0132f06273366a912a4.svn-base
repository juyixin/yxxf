/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import dtw.webmail.util.StringUtil;
import org.apache.log4j.Logger;

import javax.mail.Folder;
import javax.mail.MessagingException;

import java.io.Serializable;
import java.util.*;

/**
 * Class implementing a list for <tt>JwmaFolder</tt>
 * instances.
 * It has caching functionality, which reduces the need to
 * reconstruct the list after moving and deleting folders.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
class JwmaFolderList implements Serializable{

  //logging
  private static Logger log = Logger.getLogger(JwmaFolderList.class);

  //class attributes
  public static boolean c_SubscribedOnly = true;

  //instance attributes
  private Folder m_Folder;
  private List m_Folders;
  private boolean m_Recursive;
  private boolean m_SubscribedOnly = c_SubscribedOnly;
  private String m_Pattern;

  /**
   * Constructs a new <tt>JwmaFolderList</tt> instance.
   *
   * @param f the folder to be listed as <tt>javax.mail.Folder</tt>.
   * @param recurse flags if the list should be build recursive.
   */
  private JwmaFolderList(Folder f, boolean recurse) {
    m_Recursive = recurse;
    if (recurse) {
      m_Pattern = "*";
    } else {
      m_Pattern = "%";
    }
    m_Folder = f;
  }//JwmaFolderList

  /**
   * Tests if this <tt>JwmaFolderList</tt> will work
   * only with subscribed folders.
   *
   * @return true if subscribed only, false otherwise.
   */
  public boolean isSubscribedOnly() {
    return m_SubscribedOnly;
  }//isSubscribedOnly

  /**
   * Sets or resets the flag for working only with
   * subscribed folders.
   */
  public void setSubscribedOnly(boolean subscribedOnly) {
    m_SubscribedOnly = subscribedOnly;
  }//setSubscribedOnly

  /**
   * Sets the pattern used for listing folders.
   *
   * @return the pattern as <tt>String</tt>.
   */
  public String getPattern() {
    return m_Pattern;
  }//getPattern

  /**
   * Returns the pattern for listing folders.
   *
   * @param pattern the pattern as <tt>String</tt>.
   */
  public void setPattern(String pattern) {
    m_Pattern = pattern;
  }//setPattern

  /**
   * Returns an Iterator over the <tt>JwmaFolder</tt> instances
   * contained within this list.
   *
   * @return the <tt>Iterator</tt> over the items in this list.
   */
  public Iterator iterator() {
    return m_Folders.listIterator();
  }//iterator


  /**
   * Returns the size of this list.
   *
   * @return the size of this list.
   */
  public int size() {
    return m_Folders.size();
  }//getSize

  /**
   * Returns a sublist of this list, that contains only
   * the folders of the given type.
   *
   * @param type the requested folder type as <tt>int</tt>.
   *
   * @return the list containing all folders of the given type as
   *         <tt>List</tt>.
   */
  public List sublist(int type) {

    if (type == JwmaFolder.TYPE_ALL) {
      return m_Folders;
    } else {
      List folders = new ArrayList();
      for (Iterator iter = m_Folders.listIterator(); iter.hasNext();) {
        JwmaFolderImpl f = (JwmaFolderImpl) iter.next();
        switch (type) {
          case JwmaFolder.TYPE_MESSAGE_CONTAINER:
            if (f.isType(JwmaFolder.TYPE_MAILBOX)
                || f.isType(JwmaFolder.TYPE_MIXED)) {
              folders.add(f);
            }
            break;
          case JwmaFolder.TYPE_FOLDER_CONTAINER:
            if (f.isType(JwmaFolder.TYPE_FOLDER)
                || f.isType(JwmaFolder.TYPE_MIXED)) {
              folders.add(f);
            }
            break;
          default:
            if (f.isType(type)) {
              folders.add(f);
            }
        }
      }
      //force sort
      Collections.sort(folders, LEXOGRAPHICAL);
      return folders;
    }
  }//sublist


  /**
   * Returns a sublist of this list, that contains only
   * the folders of the given type.
   *
   * @param type the requested folder type as <tt>int</tt>.
   *
   * @return the list containing all folders of the given type as
   *         <tt>List</tt>.
   */
  public List sublist(int type, boolean subscribed) {

    if (type == JwmaFolder.TYPE_ALL) {
      return m_Folders;
    } else {
      List folders = new ArrayList();
      for (Iterator iter = m_Folders.listIterator(); iter.hasNext();) {
        JwmaFolderImpl f = (JwmaFolderImpl) iter.next();
        switch (type) {
          case JwmaFolder.TYPE_MESSAGE_CONTAINER:
            if (f.isType(JwmaFolder.TYPE_MAILBOX)
                || f.isType(JwmaFolder.TYPE_MIXED)) {
              if (subscribed) {
                if (f.isSubscribed()) {
                  folders.add(f);
                }
              } else {
                folders.add(f);
              }
            }
            break;
          case JwmaFolder.TYPE_FOLDER_CONTAINER:
            if (f.isType(JwmaFolder.TYPE_FOLDER)
                || f.isType(JwmaFolder.TYPE_MIXED)) {
              if (subscribed) {
                if (f.isSubscribed()) {
                  folders.add(f);
                }
              } else {
                folders.add(f);
              }
            }
            break;
          default:
            if (f.isType(type)) {
              if (subscribed) {
                if (f.isSubscribed()) {
                  folders.add(f);
                }
              } else {
                folders.add(f);
              }
            }
        }
      }
      //force sort
      Collections.sort(folders, LEXOGRAPHICAL);
      return folders;
    }
  }//sublist

  /**
   * Returns a sublist of this list, that contains only
   * the folders of the given type, filtering the given
   * folder.
   * This method can be used to obtain targets for moving
   * messages or other folders.
   *
   * @param type the requested folder type as <tt>int</tt>.
   * @param folder the <tt>JwmaFolderImpl</tt> instance to be filtered
   *        from the list.
   *
   * @return the list containing all folders of the given type as
   *         <tt>List</tt>.
   */
  public List sublist(int type, JwmaFolderImpl folder) {

    List folders = new ArrayList();
    for (Iterator iter = m_Folders.listIterator(); iter.hasNext();) {
      JwmaFolderImpl f = (JwmaFolderImpl) iter.next();
      if (f.equals(folder)) {
        //filter this one
        continue;
      }
      switch (type) {
        case JwmaFolder.TYPE_ALL:
          folders.add(f);
          break;
        case JwmaFolder.TYPE_MESSAGE_CONTAINER:
          if (f.isType(JwmaFolder.TYPE_MAILBOX)
              || f.isType(JwmaFolder.TYPE_MIXED)) {
            folders.add(f);
          }
          break;
        case JwmaFolder.TYPE_FOLDER_CONTAINER:
          if (f.isType(JwmaFolder.TYPE_FOLDER)
              || f.isType(JwmaFolder.TYPE_MIXED)) {
            folders.add(f);
          }
          break;
        default:
          if (f.isType(type)) {
            folders.add(f);
          }
      }
    }
    //force sort
    Collections.sort(folders, LEXOGRAPHICAL);
    return folders;
  }//sublist


  /**
   * Returns a sublist of this list, that contains only
   * the folders of the given type, filtering the given
   * folder.
   * This method can be used to obtain targets for moving
   * messages or other folders.
   *
   * @param type the requested folder type as <tt>int</tt>.
   * @param folder the <tt>JwmaFolderImpl</tt> instance to be filtered
   *        from the list.
   *
   * @return the list containing all folders of the given type as
   *         <tt>List</tt>.
   */
  public List sublist(int type, JwmaFolderImpl folder, boolean subscribed) {

    List folders = new ArrayList();
    for (Iterator iter = m_Folders.listIterator(); iter.hasNext();) {
      JwmaFolderImpl f = (JwmaFolderImpl) iter.next();
      if (f.equals(folder)) {
        //filter this one
        continue;
      }
      switch (type) {
        case JwmaFolder.TYPE_ALL:
          if (subscribed) {
            if (f.isSubscribed()) {
              folders.add(f);
            }
          } else {
            folders.add(f);
          }
          break;
        case JwmaFolder.TYPE_MESSAGE_CONTAINER:
          if (f.isType(JwmaFolder.TYPE_MAILBOX)
              || f.isType(JwmaFolder.TYPE_MIXED)) {
            if (subscribed) {
              if (f.isSubscribed()) {
                folders.add(f);
              }
            } else {
              folders.add(f);
            }
          }
          break;
        case JwmaFolder.TYPE_FOLDER_CONTAINER:
          if (f.isType(JwmaFolder.TYPE_FOLDER)
              || f.isType(JwmaFolder.TYPE_MIXED)) {
            if (subscribed) {
              if (f.isSubscribed()) {
                folders.add(f);
              }
            } else {
              folders.add(f);
            }
          }
          break;
        default:
          if (f.isType(type)) {
            if (subscribed) {
              if (f.isSubscribed()) {
                folders.add(f);
              }
            } else {
              folders.add(f);
            }
          }
      }
    }
    //force sort
    Collections.sort(folders, LEXOGRAPHICAL);
    return folders;
  }//sublist

  /**
   * Returns a sublist of this list, that contains only
   * the folders of the given type, filtering the given
   * folders.
   * This method should be used to obtain a list of subscribed
   * or unsubscribed folders, pasing in the special folders (trash,
   * draft etc.)
   *
   * @param type the requested folder type as <tt>int</tt>.
   * @param exfolders the folders (by path) to be filtered
   *        from the list as <tt>String[]</tt>.
   *
   * @return the list containing all folders of the given type as
   *         <tt>List</tt>.
   */
  public List sublist(int type, String[] exfolders, boolean subscribed) {
    List folders = new ArrayList();
    for (Iterator iter = m_Folders.listIterator(); iter.hasNext();) {
      JwmaFolderImpl f = (JwmaFolderImpl) iter.next();

      if (StringUtil.contains(exfolders, f.getPath())) {
        continue;
      } else {
        switch (type) {
          case JwmaFolder.TYPE_ALL:
            if (subscribed) {
              if (f.isSubscribed()) {
                folders.add(f);
              }
            } else {
              folders.add(f);
            }
            break;
          case JwmaFolder.TYPE_MESSAGE_CONTAINER:
            if (f.isType(JwmaFolder.TYPE_MAILBOX)
                || f.isType(JwmaFolder.TYPE_MIXED)) {
              if (subscribed) {
                if (f.isSubscribed()) {
                  folders.add(f);
                }
              } else {
                folders.add(f);
              }
            }
            break;
          case JwmaFolder.TYPE_FOLDER_CONTAINER:
            if (f.isType(JwmaFolder.TYPE_FOLDER)
                || f.isType(JwmaFolder.TYPE_MIXED)) {
              if (subscribed) {
                if (f.isSubscribed()) {
                  folders.add(f);
                }
              } else {
                folders.add(f);
              }
            }
            break;
          default:
            if (f.isType(type)) {
              if (subscribed) {
                if (f.isSubscribed()) {
                  folders.add(f);
                }
              } else {
                folders.add(f);
              }
            }
        }
      }
    }
    //force sort
    Collections.sort(folders, LEXOGRAPHICAL);
    return folders;
  }//sublist

  /**
   * Tests if this list contains a folder with the
   * given path.
   *
   * @param path the path of the folder as <tt>String</tt>.
   *
   * @return true if this list contains a folder with the given path,
   *         false otherwise.
   */
  public boolean contains(String path) {
    for (Iterator iter = iterator(); iter.hasNext();) {
      if (path.equals(((JwmaFolderImpl) iter.next()).getPath())) {
        return true;
      }
    }
    return false;
  }//contains

  /**
   * Tests if this list contains a given folder.
   *
   * @param folder the folder as <tt>JwmaFolder</tt>.
   *
   * @return true if this list contains the given folder,
   *         false otherwise.
   */
  public boolean contains(JwmaFolder folder) {
    for (Iterator iter = iterator(); iter.hasNext();) {
      if (folder.equals(iter.next())) {
        return true;
      }
    }
    return false;
  }//contains

  /**
   * Creates an array of <tt>JwmaFolder</tt> instances from
   * the given <tt>List</tt>.
   * If the list is empty, then the returned array will be
   * empty, but not null.
   *
   * @param folders the <tt>List</tt> of folders.
   *
   * @return the newly created array.
   */
  public JwmaFolder[] createFolderArray(List folders) {
    //create array from it
    JwmaFolder[] list = new JwmaFolder[folders.size()];
    return (JwmaFolder[]) folders.toArray(list);
  }//createFolderArray

  /**
   * Removes a folder with the given path from this list.
   *
   * @param path the path of the folder to be removed as <tt>String</tt>.
   */
  public void removeFolderFromList(String path) {
    for (Iterator iter = m_Folders.iterator(); iter.hasNext();) {
      if (path.equals(
          ((JwmaFolderImpl) iter.next()).getPath())) {
        iter.remove();
        //done
        break;
      }
    }
  }//removeFolderFromList

  /**
   * Adds a given folder to this list.
   *
   * @param folder the folder to be added as <tt>JwmaFolder</tt>.
   */
  public void addFolderToList(JwmaFolderImpl folder) {
    m_Folders.add(folder);
    Collections.sort(m_Folders, LEXOGRAPHICAL);
  }//addFolderToList

  /**
   * Rebuilds this list of folders.
   */
  public void rebuild()
      throws MessagingException, JwmaException {
    Folder[] folders = m_Folder.list(m_Pattern);
    //build list
    m_Folders = new ArrayList(folders.length);
    buildFolderList(folders);
    Collections.sort(m_Folders, LEXOGRAPHICAL);
  }//rebuild

  /**
   * Builds this list of folders from the given array of folders.
   * If recursive, then it will build a flat list of the complete
   * folder tree.
   *
   * @param folders the array of <tt>javax.mail.Folder</tt> instances
   *        in the root folder.
   */
  private void buildFolderList(Folder[] folders)
      throws JwmaException, MessagingException {

    for (int i = 0; i < folders.length; i++) {
      //add a lightweight JwmaFolderImpl to the list
      m_Folders.add(
          JwmaFolderImpl.createLight(folders[i])
      );
      //recurse if necessary
      //if (m_Recursive) {
      //  buildFolderList(folders[i].list());
      //}
    }
  }//buildFolderList

  /**
   * Factory method that creates a flat list of all folders on the store.
   * The given folder should be the root folder, or the default folder
   * on the store.
   *
   * @param folder the <tt>javax.mail.Folder</tt> instance representing the
   *        root folder.
   *
   * @return the newly created <tt>JwmaFolderList</tt> instance.
   *
   * @throws JwmaException if it fails to build the folder list.
   */
  public static JwmaFolderList createStoreList(Folder folder)
      throws JwmaException {

    try {
      JwmaFolderList flist = new JwmaFolderList(folder, true);
      flist.rebuild();
      //DEBUG:flist.dumpList();
      return flist;
    } catch (MessagingException mex) {
      throw new JwmaException(mex.getMessage()).setException(mex);
    }
  }//createStoreList

  /**
   * Factory method that creates a list of all subfolders of the given
   * folder.
   * There is no recursion into subfolders.
   *
   * @param folder the <tt>javax.mail.Folder</tt> instance to be listed.
   *
   * @return the newly created <tt>JwmaFolderList</tt> instance.
   *
   * @throws JwmaException if it fails to build the folder list.
   */
  public static JwmaFolderList createSubfolderList(Folder folder)
      throws JwmaException {
    try {
      JwmaFolderList flist = new JwmaFolderList(folder, false);
      flist.rebuild();
      return flist;
    } catch (MessagingException mex) {
      throw new JwmaException(mex.getMessage()).setException(mex);
    }
  }//createSubfolderList


  /**
   * Defines a <tt>Comparator</tt> that sorts
   * the folder instances lexographical.
   */
  public static final Comparator LEXOGRAPHICAL =
      new Comparator() {

        public int compare(Object o1, Object o2) {
          JwmaFolder f1 = (JwmaFolder) o1;
          JwmaFolder f2 = (JwmaFolder) o2;
          return f1.getName().compareTo(
              f2.getName()
          );
        }//compare(Object,Object)
      };

}//class JwmaFolderList
