/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import dtw.webmail.JwmaKernel;
import dtw.webmail.JwmaSession;
import dtw.webmail.util.config.PostOffice;
import org.apache.log4j.Logger;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Class implementing a wrapper for the mail store. Offers methods and utilities
 * to manage the store.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaStoreImpl implements JwmaStoreInfo, Serializable {

	// logging
	private static Logger log = Logger.getLogger(JwmaStoreImpl.class);

	public static final boolean c_SubscribedOnly = true;

	// instance attributes & associations
	private PostOffice m_PostOffice;
	private JwmaSession m_Session;
	private Store m_Store;
	private Folder m_RootFolder;
	private JwmaFolderImpl m_TrashFolder;
	private Folder m_SentMailFolder;
	private Folder m_SentLaterMailFolder;
	private Folder m_DraftFolder;
	private Folder m_ReadMailFolder;
	private JwmaFolderImpl m_JwmaRootFolder;
	private JwmaFolderImpl m_ActualFolder;
	private JwmaFolderImpl m_InboxFolder;
	private JwmaFolderImpl m_InboxDraftFolder;
	private JwmaFolderImpl m_SentFolder;
	private JwmaFolderImpl m_SentLaterFolder;
	private char m_FolderSeparator;
	private JwmaFolderList m_Folders;

	/**
	 * Constructs a <tt>JwmaStoreImpl</tt> instance.
	 *
	 * @param session
	 *            the <tt>JwmaSession</tt> instance this store belongs to.
	 * @param mstore
	 *            the mail <tt>Store</tt> this instance should wrap.
	 */
	private JwmaStoreImpl(JwmaSession session, Store mstore) {
		m_Store = mstore;
		m_Session = session;
		m_PostOffice = session.getPostOffice();
	}// JwmaStoreImpl

	/*** jwma special folders **********************************/

	/**
	 * Returns the actual folder.
	 *
	 * @return the actual folder as <tt>JwmaFolderImpl</tt>.
	 *
	 * @see dtw.webmail.model.JwmaFolderImpl
	 */
	public JwmaFolderImpl getActualFolder() {
		return m_ActualFolder;
	}// getActualFolder

	/**
	 * Sets the actual folder.
	 *
	 * @param f
	 *            the actual folder as <tt>JwmaFolderImpl</tt>.
	 *
	 *
	 * @see dtw.webmail.model.JwmaFolderImpl.
	 */
	public void setActualFolder(JwmaFolderImpl f) {
		m_ActualFolder = f;
	}// setActualFolder

	public JwmaFolderImpl getInboxFolder() {
		setActualFolder(m_InboxFolder);
		return m_InboxFolder;
	}

	public JwmaFolderImpl getInboxDraftFolder() {
		setActualFolder(m_InboxDraftFolder);
		return m_InboxDraftFolder;
	}

	public JwmaFolderImpl getSentFolder() {
		setActualFolder(m_SentFolder);
		return m_SentFolder;
	}

	public JwmaFolderImpl getSentLaterFolder() {
		setActualFolder(m_SentLaterFolder);
		return m_SentLaterFolder;
	}

	/**
	 * Returns the <tt>JwmaInboxInfo</tt> instance that can be used to retrieve
	 * information about the store's INBOX folder (i.e. where new messages
	 * should be arriving).
	 *
	 * @return the store's INBOX folder as <tt>JwmaInboxInfo</tt>.
	 *
	 * @see dtw.webmail.model.JwmaInboxInfo
	 */
	public JwmaInboxInfo getInboxInfo() {
		return (JwmaInboxInfo) m_InboxFolder;
	}// getInboxInfo

	/**
	 * Returns the <tt>JwmaTrashInfo</tt> instance that can be used to retrieve
	 * information about the store's trash folder (i.e. where deleted messages
	 * end up first).
	 *
	 * @return the store's trash folder as <tt>JwmaTrashInfo</tt>.
	 *
	 * @see dtw.webmail.model.JwmaTrashInfo
	 */
	public JwmaTrashInfo getTrashInfo() throws JwmaException {
		return ((JwmaTrashInfo) m_TrashFolder);
	}// getTrashInfo

	/**
	 * Returns the trash folder.
	 * <p>
	 * The type of this folder will be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @return the trash folder as <tt>Folder</tt>.
	 *
	 * @see javax.mail.Folder
	 */
	public Folder getTrashFolder() {
		return m_TrashFolder.getFolder();
	}// getTrashFolder

	/**
	 * Sets the trash folder from a name or path given as <tt>String</tt>.
	 * <p>
	 * This method will check and modify the name as follows:
	 * <ol>
	 * <li>name is null or an empty string:<br>
	 * <root folder path>+<folder separator>+ "trash"</li>
	 * <li>name does not start with <root folder path>:<br>
	 * <root folder path>+<folder separator>+name</li>
	 * </ol>
	 * The method will finally set the newly created folder name in the
	 * associated preferences.
	 *
	 * @param name
	 *            the name or full name of the draft folder as <tt>String</tt>.
	 *
	 * @throws JwmaException
	 *             if it fails to set the draft folder and create a JwmaFolder
	 *             instance with it.
	 */

	/**
	 * Returns the draft folder.
	 * <p>
	 * The type of this folder will be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @return the draft folder as <tt>Folder</tt>.
	 *
	 * @see javax.mail.Folder
	 */
	public Folder getDraftFolder() {
		return m_DraftFolder;
	}// getDraftFolder

	/**
	 * Sets the draft folder.
	 * <p>
	 * The type of this folder should be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @param f
	 *            the draft folder as <tt>Folder</tt>.
	 *
	 * @see javax.mail.Folder
	 */
	protected void setDraftFolder(Folder f) {
		m_DraftFolder = f;
	}// setDraftFolder

	/**
	 * Returns the sent mail archive folder.
	 * <p>
	 * The type of this folder will be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @return the sent mail archive folder as <tt>Folder</tt>.
	 *
	 * @see javax.mail.Folder
	 */
	public Folder getSentMailFolder() {
		return m_SentMailFolder;
	}// getSentMailFolder

	public Folder getSentLaterMailFolder() {
		return m_SentLaterMailFolder;
	}// getSentMailFolder

	/**
	 * Sets the sent mail archive folder.
	 * <p>
	 * The type of this folder will be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @param f
	 *            the sent mail archive folder as <tt>Folder</tt>.
	 *
	 * @see javax.mail.Folder
	 */
	protected void setSentMailFolder(Folder f) {
		m_SentMailFolder = f;
	}// setSentMailFolder

	protected void setSentLaterMailFolder(Folder f) {
		m_SentLaterMailFolder = f;
	}// setSentMailFolder

	public void setSentLaterMailFolder(String name) throws JwmaException {

		try {
			if (name == null || name.length() == 0) {
				name = m_RootFolder.getFullName() + getFolderSeparator() + "sent-later";
			} else if (!name.startsWith(m_RootFolder.getFullName())) {
				// Note: causes this special folder to be subfolder of root
				// always.
				int lastindex = name.lastIndexOf(getFolderSeparator());
				if (lastindex > 0) {
					name = name.substring(lastindex, name.length());
				}
				name = m_RootFolder.getFullName() + getFolderSeparator() + name;
			}
			m_SentLaterMailFolder = getFolder(name);
			if (!m_SentLaterMailFolder.exists()) {
				boolean success = m_SentLaterMailFolder.create(JwmaFolderImpl.TYPE_MAILBOX);
				log.debug("setSentLaterMailFolder(): Created sent-later folder=" + name + ":" + success);
			} else {
				log.debug("setSentLaterMailFolder(): Set sent-later folder=" + name);
			}
			m_SentLaterMailFolder.setSubscribed(true);
		} catch (MessagingException ex) {
			throw new JwmaException(ex.getMessage()).setException(ex);
		}
	}// setSentLaterMailFolder

	/**
	 * Returns the read mail archive folder.
	 * <p>
	 * The type of this folder will be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @return the read mail archive folder as <tt>JwmaFolderImpl</tt>.
	 * @see dtw.webmail.model.JwmaFolderImpl
	 */
	public Folder getReadMailFolder() {
		return m_ReadMailFolder;
	}// getReadMailFolder

	/**
	 * Sets the read mail archive folder.
	 * <p>
	 * The type of this folder will be
	 * <tt>JwmaFolderImpl.TYPE_MESSAGE_CONTAINER</tt>.
	 *
	 * @param f
	 *            the read mail archive folder as <tt>Folder</tt>.
	 *
	 * @see javax.mail.Folder
	 */
	protected void setReadMailFolder(Folder f) {
		m_ReadMailFolder = f;
	}// setReadMailFolder

	/**
	 * Put's a message into the read-mail archive, if archivation is enabled.
	 *
	 * @param message
	 *            the <tt>Message</tt> to be archived.
	 *
	 * @throws JwmaException
	 *             if it fails to archive the message.
	 *
	 * @see javax.mail.Message
	 */
	public void archiveSentMail(Message message) throws JwmaException {

		if (m_Session.getPreferences().isAutoArchiveSent()) {
			Folder archive = null;
			try {

				// Set the sent date
				message.setSentDate(new Date());
				// get the folder
				archive = getSentMailFolder();
				if (archive == null) {
					prepareSentFolder();
					// archive = getSentMailFolder();
					archive = getFolder("INBOX" + getFolderSeparator() + "sent-mail");
				}
				// open it read write
				archive.open(Folder.READ_WRITE);
				// save the message in archive, append only works as array
				Message[] tosave = { message };
				// append it
				archive.appendMessages(tosave);
				// close without expunging
				archive.close(false);
			} catch (MessagingException mex) {
				try {
					if (archive.isOpen()) {
						archive.close(false);
					}
				} catch (Exception unex) {
					// ignore, will be closed anyway
				}
			}
		}
	}// archiveSentMail

	public int archiveSentLaterMail(Message message) throws JwmaException {
		int messageNumber = 1;
		Folder archive = null;
		try {
			// Set the sent date
			// message.setSentDate(new Date());
			// get the folder
			archive = getSentLaterMailFolder();
			if (archive == null) {
				// prepareSentFolder();
				// archive = getSentMailFolder();
				archive = getFolder("INBOX" + getFolderSeparator() + "sent-later");
			}
			// open it read write
			archive.open(Folder.READ_WRITE);
			// save the message in archive, append only works as array
			Message[] tosave = { message };
			// append it
			archive.appendMessages(tosave);
			log.info("message number-----------" + archive.getMessageCount());
			messageNumber = archive.getMessageCount();
			// close without expunging
			archive.close(false);
			return messageNumber;
		} catch (MessagingException mex) {
			try {
				if (archive.isOpen()) {
					archive.close(false);
				}
			} catch (Exception unex) {
				// ignore, will be closed anyway
			}
		}
		return messageNumber;
	}// archiveSentLaterMail

	/*** end jwma special folders ******************************/

	/*** folder management methods *****************************/

	/**
	 * Resets the actual folder to the root folder.
	 *
	 * @return the prepared root folder as <tt>JwmaFolder</tt>.
	 */
	public JwmaFolder resetToRootFolder() throws JwmaException {
		// set as actual
		setActualFolder(m_JwmaRootFolder);
		m_JwmaRootFolder.update(this);
		// return reference
		return m_JwmaRootFolder;
	}// resetToRootFolder

	/**
	 * Sets a new actual folder from a given path.
	 *
	 * @return the new actual folder as <tt>JwmaFolder</tt>.
	 *
	 * @throws JwmaException
	 *             if it fails to set the new folder and create a JwmaFolder
	 *             instance with it.
	 */
	public JwmaFolder setActualFolder(String path) throws JwmaException {
		setActualFolder(JwmaFolderImpl.createJwmaFolderImpl(this, getFolder(path)));
		return getActualFolder();
	}// setActualFolder

	/**
	 * Returns a <tt>JwmaFolderImpl</tt> with the given path from the store.
	 *
	 * @return the folder as <tt>JwmaFolderImpl</tt> and set as actual
	 *         folder(!).
	 *
	 * @throws JwmaException
	 *             if a folder with the given path does not exist or an error
	 *             occurs when creating the wrapper instance.
	 */
	private JwmaFolderImpl getJwmaFolder(String fullname) throws JwmaException {

		setActualFolder(JwmaFolderImpl.createJwmaFolderImpl(this, getFolder(fullname)));
		return getActualFolder();
	}// getJwmaFolder

	/**
	 * Returns a <tt>Folder</tt> with a given path from the mail store.
	 *
	 * @return the folder as <tt>Folder</tt>.
	 *
	 * @throws JwmaException
	 *             if a folder with the given path does not exist on the store
	 *             or a MessagingException occurs.
	 */
	public Folder getFolder(String fullname) throws JwmaException {
		try {
			// FIXME: Microsoft Exchange returns "" as Default Folder, but
			// asking for the
			// folder "" does not return any subfolders.
			if (fullname.length() != 0) {
				return m_Store.getFolder(fullname);
			} else {
				// assume to return the default folder...
				return m_Store.getDefaultFolder();
			}
		} catch (MessagingException mex) {
			throw new JwmaException("jwma.store.getfolder", true).setException(mex);
		}
	}// getFolder

	/**
	 * Creates a new folder on the store.
	 *
	 * @throws JwmaException
	 *             if the folder already exists, or if it fails to create the
	 *             folder.
	 */
	public void createFolder(String fullname, int type) throws JwmaException {
		try {
			// relative name to path
			if (fullname.indexOf(getFolderSeparator()) == -1) {
				// In case of the root folder being "" this can represent a
				// problem
				// The MS Exchange does not take /fullname as valid...
				if (m_ActualFolder.getPath().length() > 0) {
					fullname = m_ActualFolder.getPath() + getFolderSeparator() + fullname;
				}
			}
			Folder newfolder = getFolder(fullname);
			if (newfolder.exists()) {
				throw new JwmaException("jwma.store.createfolder.exists", true);
			} else {
				newfolder.create(type);
				// ensure new folder is subscribed
				newfolder.setSubscribed(true);
				// update store list & subfolder list
				JwmaFolderImpl folder = JwmaFolderImpl.createLight(newfolder);
				m_Folders.addFolderToList(folder);
				m_ActualFolder.addIfSubfolder(folder);
			}
		} catch (MessagingException mex) {
			throw new JwmaException("jwma.store.createfolder.failed", true).setException(mex);
		}
	}// createFolder

	/**
	 * Deletes the given folders from the store.
	 * <p>
	 * Note that this method will not remove any special folder from the store.
	 * Despite that, it is a convenience method, looping over the array and
	 * calling <tt>deleteFolder()</tt>
	 *
	 * @param folders
	 *            an array of strings; each <tt>String</tt> representing the
	 *            full path of a valid folder of the actual store.
	 *
	 * @throws JwmaException
	 *             if a folder does not exist, or if an error occurs when
	 *             deleting.
	 *
	 * @see #deleteFolder(String)
	 */
	public void deleteFolders(String[] folders) throws JwmaException {

		for (int i = 0; i < folders.length; i++) {
			deleteFolder(folders[i]);
		}
	}// deleteFolders

	/**
	 * Deletes a given folders from the store.
	 * <p>
	 * Note that this method will not remove the folder if it is a special
	 * folder.
	 *
	 * @param fullname
	 *            the folder's path as <tt>String</tt>.
	 *
	 * @throws JwmaException
	 *             if a folder does not exist, or if an error occurs when
	 *             deleting.
	 */
	public void deleteFolder(String fullname) throws JwmaException {
		try {
			Folder delfolder = getFolder(fullname);
			// ensure not to delete jwma special folders
			if (isSpecialFolder(fullname)) {
				throw new JwmaException("jwma.store.deletefolder.systemfolder", true);
			} else {
				// UW does not update subscriptions
				delfolder.setSubscribed(false);
				delfolder.delete(true);
				// update cached store list
				m_Folders.removeFolderFromList(fullname);
				m_ActualFolder.removeIfSubfolder(fullname);
			}
		} catch (MessagingException mex) {
			throw new JwmaException("jwma.store.deletefolder.failed", true).setException(mex);
		}
	}// deleteFolder

	/**
	 * Moves the given folder on the store.
	 * <p>
	 * Note that this method is a convenience method it creates a single entry
	 * array and calls <tt>moveFolders()</tt>.
	 *
	 * @param foldername
	 *            the full path of the folder as <tt>String</tt>.
	 * @param destfolder
	 *            the full path of a valid folder on the actual store.
	 *
	 * @throws JwmaException
	 *             if a folder does not exist, or if an error occurs when
	 *             deleting.
	 *
	 * @see #moveFolders(String[],String)
	 */
	public void moveFolder(String foldername, String destfolder) throws JwmaException {
		String[] folders = { foldername };
		moveFolders(folders, destfolder);
	}// moveFolder

	/**
	 * Moves the given folders to the given destination folder.
	 *
	 * @param foldernames
	 *            an array of strings; each <tt>String</tt> representing the
	 *            full path of a valid folder of the actual store.
	 * @param destfolder
	 *            the full path of a valid folder of the actual store.
	 *
	 * @return the new actual folder as <tt>JwmaFolder</tt>.
	 *
	 * @throws JwmaException
	 *             if the source folders or the destination folder do not exist,
	 *             the destination is a subfolder of a source folder, the
	 *             destination cannot contain any subfolders, or if an error
	 *             occurs when moving.
	 */
	public void moveFolders(String[] foldernames, String destfolder) throws JwmaException {

		try {
			// ensure existing destination folder
			if (!checkFolderExistence(destfolder)) {
				throw new JwmaException("jwma.store.movefolder.destination.missing", true);
			}
			// ensure basically valid destination
			Folder dest = getFolder(destfolder);
			if (!(dest.getType() == JwmaFolderImpl.TYPE_FOLDER || dest.getType() == JwmaFolderImpl.TYPE_MIXED)) {

				throw new JwmaException("jwma.store.movefolder.destination.foul", true);
			}
			// create list that does not contain any special folder
			List folders = getFolders(foldernames);

			//
			for (Iterator iter = folders.iterator(); iter.hasNext();) {
				Folder oldfolder = (Folder) iter.next();
				Folder newfolder = getFolder(destfolder + getFolderSeparator() + oldfolder.getName());
				if (newfolder.exists()) {
					throw new JwmaException("jwma.store.movefolder.destination.exists", true
					// " (@ "+
					// newfolder.getFullName()+
					// ")"
					);
				}
				// move but ensure a parent is not moved into
				// one of its childs
				if (!newfolder.getFullName().regionMatches(0, oldfolder.getFullName(), 0, oldfolder.getFullName().length())) {
					// UW does not change subscriptions on moving!
					if (oldfolder.isSubscribed()) {
						oldfolder.setSubscribed(false);
					}
					oldfolder.renameTo(newfolder);
					newfolder.setSubscribed(true);
				} else {
					throw new JwmaException("jwma.store.movefolder.destination.subfolder", true);
				}
			}
			// update the cached list
			m_Folders.rebuild();
			m_ActualFolder.removeIfSubfolder(foldernames);
		} catch (MessagingException mex) {
			throw new JwmaException("jwma.store.movefolder.failed", true).setException(mex);

		}
	}// moveFolders

	public void updateFolderSubscription(String[] foldernames, boolean subscribe) throws JwmaException {
		// create list that does not contain any special folder
		try {
			List folders = getFolders(foldernames);
			for (Iterator iter = folders.iterator(); iter.hasNext();) {
				((Folder) iter.next()).setSubscribed(subscribe);
			}
		} catch (Exception ex) {
			log.error("updateFolderSubscription()", ex);
			throw new JwmaException("jwma.store.updatesubscription.failed", true).setException(ex);
		}
	}// updateFolderSubscription

	public void unsubscribeFolders() {

	}// unsubscribeFolders

	/**
	 * An utility method that collects all non special folders of a given array
	 * of folder paths, into a list of jvax.mail.Folder instances.
	 */
	private List getFolders(String[] foldernames) throws MessagingException, JwmaException {

		ArrayList folders = new ArrayList(foldernames.length);
		Folder f = null;
		for (int i = 0; i < foldernames.length; i++) {
			f = getFolder(foldernames[i]);
			// add if existant and NOT the trash folder
			if (f.exists() && !isSpecialFolder(foldernames[i])) {
				folders.add(f);
			}
		}
		return folders;
	}// getFolders

	/**
	 * Tests if a given path is a special jwma folder.
	 */
	private boolean isSpecialFolder(String fullname) {
		return (fullname.equals(m_RootFolder.getFullName()) || fullname.equals(m_InboxFolder.getPath()) || fullname.equals(m_TrashFolder.getPath()) || fullname.equals(m_DraftFolder.getFullName()) || (m_Session.getPreferences().isAutoArchiveSent() && fullname.equals(m_SentMailFolder.getFullName())) || (m_Session.getPreferences().isAutoMoveRead() && fullname.equals(m_ReadMailFolder.getFullName())));
	}// isSpecialFolder

	/*** end folder management methods *************************/

	/*** Storeinfo implementation **********************************/

	public JwmaFolder[] listFolders(int type) {
		return m_Folders.createFolderArray(m_Folders.sublist(type, c_SubscribedOnly));
	}// listFolders

	public JwmaFolder[] listFolders(int type, boolean subscribed) {
		String[] excludes = { m_RootFolder.getFullName(), m_InboxFolder.getPath(), m_TrashFolder.getPath(), m_DraftFolder.getFullName(), ((m_Session.getPreferences().isAutoArchiveSent()) ? m_SentMailFolder.getFullName() : ""), ((m_Session.getPreferences().isAutoMoveRead()) ? m_ReadMailFolder.getFullName() : "") };
		return m_Folders.createFolderArray(m_Folders.sublist(type, excludes, subscribed));
	}// listFolders

	public JwmaFolder[] listFolderMoveTargets() {
		// sublist with actual folder filtered
		return m_Folders.createFolderArray(m_Folders.sublist(JwmaFolder.TYPE_FOLDER_CONTAINER, m_ActualFolder, c_SubscribedOnly));
	}// listFolderMoveTargets

	public JwmaFolder[] listMessageMoveTargets() {
		// sublist with actual folder filtered
		return m_Folders.createFolderArray(m_Folders.sublist(JwmaFolder.TYPE_MESSAGE_CONTAINER, m_ActualFolder, c_SubscribedOnly));
	}// listMessageMoveTargets

	/*** End display methods ***********************************/

	/*** Utility methods ***************************************/

	/**
	 * Returns a reference to the associated preferences.
	 *
	 * @return the associated preferences <tt>JwmaPreferences</tt>.
	 *
	 * @see dtw.webmail.model.JwmaPreferences
	 */
	public JwmaPreferences getPreferences() {
		return (JwmaPreferences) m_Session.getPreferences();
	}// getPreferences

	/**
	 * Tests if the store is mixed mode, which means it can hold folders that
	 * hold messages and subfolders at once.
	 *
	 * @return true if store is supporting mixed mode, false otherwise.
	 */
	public boolean isMixedMode() {
		return m_PostOffice.isType(PostOffice.TYPE_MIXED);
	}// isMixedMode

	public char getFolderSeparator() {
		return m_FolderSeparator;
	}// getFolderSeparator

	/**
	 * Sets the folder separator character.
	 *
	 * @param the
	 *            folder separator as <tt>char</tt>.
	 */
	protected void setFolderSeparator(char c) {
		m_FolderSeparator = c;
	}// setFolderSeparator

	/**
	 * Cleans up the store.
	 * <p>
	 * This method performs some of the do-automatic functions within jwma:
	 * <ol>
	 * <li>Move read messages from the inbox to the read messages folder, if the
	 * feature is enabled.</li>
	 * <li>Empty the trashbin.</li>
	 * </ol>
	 *
	 * @throws JwmaException
	 *             if cleanup routines fail to perform error free.
	 */
	public boolean cleanup() throws JwmaException {

		// perform auto features like set in users prefs
		JwmaPreferences prefs = m_Session.getPreferences();

		// 1. automove read from Inbox to readmessagesfolder if set
		if (prefs.isAutoMoveRead()) {
			// move the read messages
			m_InboxFolder.moveMessages(m_InboxFolder.getReadMessages(), m_ReadMailFolder.getFullName());
		}

		// 2. autoempty trash if set
		if (prefs.isAutoEmpty()) {
			m_TrashFolder.update(this);
			m_TrashFolder.deleteAllMessages();
			return (m_TrashFolder.getMessageCount() != 0);
		}

		return true;
	}// cleanup

	/**
	 * Closes the associated mail store.
	 */
	public void close() {
		try {
			// close store
			m_Store.close();
		} catch (MessagingException mex) {
			log.error(JwmaKernel.getReference().getLogMessage("jwma.store.closefailed"), mex);
		}
	}// close

	/**
	 * Prepares all special folders.
	 */
	public void prepare() throws JwmaException {
		log.debug("prepare()");
		try {
			JwmaPreferences prefs = m_Session.getPreferences();

			// if empty or null string, set store default
			// folder as root
			if (prefs.getRootFolder() == null || prefs.getRootFolder().equals("")) {
				m_RootFolder = m_Store.getDefaultFolder();
			} else {
				m_RootFolder = m_Store.getFolder(prefs.getRootFolder());
				if (!m_RootFolder.exists()) {
					m_RootFolder = m_Store.getDefaultFolder();
				}
			}

			// ensure the root folder exists
			if (!m_RootFolder.exists()) {
				throw new JwmaException("jwma.store.rootfolder");
			} else {
				log.debug("Prepare set root folder to:" + prefs.getRootFolder());
			}

			// set folder separator
			setFolderSeparator(m_RootFolder.getSeparator());

			// build folder list
			m_Folders = JwmaFolderList.createStoreList(m_RootFolder);
			// buildFolderList(m_RootFolder.list());

			// Inbox the folder that contains the incoming mail
			// this has to exist, regarding to the IMAP specification
			m_InboxFolder = getJwmaFolder("INBOX");

			// ensure always fresh counts
			m_InboxFolder.setOnlineCounting(true);

			m_InboxDraftFolder = getJwmaFolder(prefs.getDraftFolder());

			m_SentFolder = getJwmaFolder(prefs.getSentMailArchive());

			// store JwmaFolderImpl of root as last, to set the actual folder
			// to root
			m_JwmaRootFolder = getJwmaFolder(m_RootFolder.getFullName());

		} catch (MessagingException ex) {
			log.debug("prepare()", ex);
			throw new JwmaException("jwma.store.prepare").setException(ex);
		}
	}// prepare

	/**
	 * Prepares all special folders.
	 */
	public void prepareRootFolder() throws JwmaException {
		log.debug("prepare()");
		try {
			JwmaPreferences prefs = m_Session.getPreferences();

			// if empty or null string, set store default
			// folder as root
			if (prefs.getRootFolder() == null || prefs.getRootFolder().equals("")) {
				m_RootFolder = m_Store.getDefaultFolder();
			} else {
				m_RootFolder = m_Store.getFolder(prefs.getRootFolder());
				if (!m_RootFolder.exists()) {
					m_RootFolder = m_Store.getDefaultFolder();
				}
			}

			// ensure the root folder exists
			if (!m_RootFolder.exists()) {
				throw new JwmaException("jwma.store.rootfolder");
			} else {
				log.debug("Prepare set root folder to:" + prefs.getRootFolder());
			}

			// set folder separator
			setFolderSeparator(m_RootFolder.getSeparator());

			// build folder list
			m_Folders = JwmaFolderList.createStoreList(m_RootFolder);
			// buildFolderList(m_RootFolder.list());

		} catch (MessagingException ex) {
			log.debug("prepare()", ex);
			throw new JwmaException("jwma.store.prepare").setException(ex);
		}
	}// prepare

	/**
	 * pull inbox messages from server.
	 */
	public void prepareInboxFolder() throws JwmaException {
		log.debug("prepare()");
		try {
			// Inbox the folder that contains the incoming mail
			// this has to exist, regarding to the IMAP specification
			m_InboxFolder = getJwmaFolder("INBOX");

			// ensure always fresh counts
			m_InboxFolder.setOnlineCounting(true);
			m_JwmaRootFolder = m_InboxFolder;
		} catch (Exception ex) {
			log.debug("prepare()", ex);
			throw new JwmaException("jwma.store.prepare").setException(ex);
		}
	}// prepare

	/**
	 * refresh inbox messages from server.
	 */
	public void refreshInboxFolder(JwmaFolder oldFolder) throws JwmaException {
		log.debug("prepare()");
		try {

			// Inbox the folder that contains the incoming mail
			// this has to exist, regarding to the IMAP specification
			// m_InboxFolder = getJwmaFolder("INBOX");

			// ensure always fresh counts
			m_InboxFolder.setOnlineCounting(true);
			m_JwmaRootFolder = m_InboxFolder;
		} catch (Exception ex) {
			log.debug("prepare()", ex);
			throw new JwmaException("jwma.store.prepare").setException(ex);
		}
	}// prepare

	/**
	 * pull draft messages from server.
	 */
	public void prepareDraftFolder() throws JwmaException {
		log.debug("pullDraftFromServer()");
		JwmaPreferences prefs = m_Session.getPreferences();
		m_InboxDraftFolder = getJwmaFolder(prefs.getDraftFolder());
	}// pullDraftFromServer

	/**
	 * pull sent messages from server.
	 */
	public void prepareSentFolder() throws JwmaException {
		log.debug("pullSentFromServer()");
		JwmaPreferences prefs = m_Session.getPreferences();
		m_SentFolder = getJwmaFolder(prefs.getSentMailArchive());
	}// pullSentFromServer

	/**
	 * pull sent messages from server.
	 */
	public void prepareSentLaterFolder() throws JwmaException {
		log.debug("pullSentLaterFromServer()");
		JwmaPreferences prefs = m_Session.getPreferences();

		setSentLaterMailFolder("sent-later");

		m_SentLaterFolder = getJwmaFolder("INBOX" + getFolderSeparator() + "sent-later");
	}// pullSentLaterFromServer

	/**
	 * pull sent messages from server.
	 */
	public void prepareTrashFolder() throws JwmaException {
		log.debug("pullSentFromServer()");
		JwmaPreferences prefs = m_Session.getPreferences();
		m_TrashFolder = getJwmaFolder(prefs.getTrashFolder());
	}// pullSentFromServer

	/**
	 * pull inbox messages from server.
	 */
	public void pullInboxFromServer() throws JwmaException {
		log.debug("pullInboxFromServer()");
		// Inbox the folder that contains the incoming mail
		// this has to exist, regarding to the IMAP specification
		m_InboxFolder = getJwmaFolder("INBOX");

		// ensure always fresh counts
		m_InboxFolder.setOnlineCounting(true);
	}// pullInboxFromServer

	/**
	 * pull draft messages from server.
	 */
	public void pullDraftFromServer() throws JwmaException {
		log.debug("pullDraftFromServer()");
		JwmaPreferences prefs = m_Session.getPreferences();
		m_InboxDraftFolder = getJwmaFolder(prefs.getDraftFolder());
	}// pullDraftFromServer

	/**
	 * pull sent messages from server.
	 */
	public void pullSentFromServer() throws JwmaException {
		log.debug("pullSentFromServer()");
		JwmaPreferences prefs = m_Session.getPreferences();
		m_SentFolder = getJwmaFolder(prefs.getSentMailArchive());
	}// pullSentFromServer

	/**
	 * Tests if a folder with the given path exists on the store.
	 * <p>
	 * Note that the method checks against the cached folder list.
	 *
	 * @param path
	 *            the path of the folder as <tt>String</tt>.
	 *
	 * @return true if a folder with this path exists on the store, false
	 *         otherwise.
	 *
	 * @throws JwmaException
	 *             if the folder already exists, or if it fails to create the
	 *             folder.
	 *
	 */
	public boolean checkFolderExistence(String path) throws JwmaException {

		if (path.equals(m_RootFolder.getFullName()) || path.equals("INBOX")) {
			return true;
		}
		return m_Folders.contains(path);
	}// checkFolderExistence

	/**
	 * Creates a new <tt>JwmaStoreImpl</tt> instance.
	 * <p>
	 * The method will also prepare the store for use.
	 *
	 * @param session
	 *            the actual <tt>JwmaSession</tt>.
	 * @param mstore
	 *            the mail store that should be wrapped.
	 *
	 * @return the newly created <tt>JwmaStoreImpl</tt> instance.
	 *
	 * @throws JwmaException
	 *             if preparing the store for use fails.
	 *
	 * @see #prepare()
	 * @see dtw.webmail.JwmaSession
	 * @see javax.mail.Store
	 */
	public static JwmaStoreImpl createJwmaStoreImpl(JwmaSession session, Store mstore) throws JwmaException {

		JwmaStoreImpl store = new JwmaStoreImpl(session, mstore);
		// prepare this store
		// store.prepare();

		return store;
	}// createJwmaStoreImpl

}// JwmaStoreImpl