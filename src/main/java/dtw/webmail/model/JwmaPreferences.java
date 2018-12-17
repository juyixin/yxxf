/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import dtw.webmail.JwmaKernel;
import net.wimpi.text.Processor;

/**
 * An interface defining the contract for interaction with the JwmaPreferences
 * model.
 * <p>
 * JwmaPreferences allows a view programmer to obtain information about the
 * users preferences to display them for reading or editing.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaPreferences implements Serializable {

	// instance attributes
	private String m_UserIdentity;
	private String m_Firstname = "firstname";
	private String m_Lastname = "lastname";
	private String m_LastLogin = "Never.";
	private String m_Signature = "";
	private String m_QuoteChar = ">";
	private boolean m_AutoQuote = false;
	private boolean m_AutoSign = true;

	// Folders
	private String m_RootFolder = "INBOX";
	private String m_SentMailArchive = "Sent Messages";//163:已发送  QQ:Sent Messages
	private String m_ReadMailArchive = "read-mail";
	private String m_TrashFolder = "Deleted Messages";//163:已删除  QQ:Deleted Messages
	private String m_DraftFolder = "Drafts";//163:草稿箱  QQ:Drafts

	// Auto features
	private boolean m_AutoArchiveSent = false;
	private boolean m_AutoMoveRead = false;
	private boolean m_AutoEmpty = false;

	private Processor m_MessageProcessor = JwmaKernel.getReference().getMessageProcessor(null);
	private Locale m_Locale = new Locale("en", "");

	private SimpleDateFormat m_DateFormat = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, m_Locale);

	private boolean m_Expert = false;
	private boolean m_DisplayingInlined = true;
	private String m_Style = "";
	public int m_MessageSortCriteria;

	private List m_RemovedAssociations;
	private boolean m_Update = false;

	/**
	 * Returns a <tt>String</tt> representing identity of the owner of this
	 * <tt>JwmaPreferences</tt>.
	 * <p>
	 * <em>Note</em>:<br>
	 * The format of the string has to be
	 * <tt>&lt;username&gt;@&lt;postofficehost&gt;</tt>. <br>
	 *
	 * @return the firstname of this preferences owner.
	 */
	public String getUserIdentity() {
		return m_UserIdentity;
	}

	/**
	 * Returns a <tt>String</tt> representing the firstname of the owner of this
	 * <tt>JwmaPreferences</tt>.
	 *
	 * @return the firstname of this preferences owner.
	 */
	public String getFirstname() {
		return m_Firstname;
	}

	/**
	 * Returns a <tt>String</tt> representing the lastname of the owner of this
	 * <tt>JwmaPreferences</tt>.
	 *
	 * @return the lastname of this preferences owner.
	 */
	public String getLastname() {
		return m_Lastname;
	}

	/**
	 * Returns a <tt>String</tt> representing the user's last login date and
	 * originating host.
	 *
	 * @return the last login with date and originating host as <tt>String</tt>.
	 */
	public String getLastLogin() {
		return m_LastLogin;
	}

	/**
	 * Returns a <tt>String</tt> representing the quote character.
	 *
	 * <i><b>Note</b>: it always contains only one character.</i>
	 *
	 * @return the quote character as String.
	 */
	public String getQuoteChar() {
		return m_QuoteChar;
	}

	/**
	 * Tests if messages should be quoted automatically when replying.
	 *
	 * @return true if messages should be quoted automatically, false otherwise.
	 */
	public boolean isAutoQuote() {
		return m_AutoQuote;
	}

	/**
	 * Returns a <tt>String</tt> representing the path of the mail root folder.
	 * <p>
	 * <i><b>Note</b>: This setting varies for different IMAP
	 * implementations. </i>
	 *
	 * @return the mail root folder as String.
	 */
	public String getRootFolder() {
		return m_RootFolder;
	}

	/**
	 * Returns a <tt>String</tt> representing the full name of the draft folder.
	 *
	 * @return the full name of the draft folder as String.
	 */
	public String getDraftFolder() {
		return m_DraftFolder;
	}

	/**
	 * Tests if messages should be automatically archived when sent.
	 *
	 * @return true if messages should be archived automatically, false
	 *         otherwise.
	 */
	public boolean isAutoArchiveSent() {
		return m_AutoArchiveSent;
	}

	/**
	 * Returns a <tt>String</tt> representing the path of the sent-mail-archive.
	 *
	 * @return the path of the sent-mail-archive as <tt>String</tt>.
	 */
	public String getSentMailArchive() {
		return m_SentMailArchive;
	}

	/**
	 * Tests if messages should be automatically moved when read.
	 *
	 * @return true if messages should be moved automatically, false otherwise.
	 */
	public boolean isAutoMoveRead() {
		return m_AutoMoveRead;
	}

	/**
	 * Returns a <tt>String</tt> representing the path of the read-mail-archive.
	 *
	 * @return the path of the read-mail-archive as String.
	 */
	public String getReadMailArchive() {
		return m_ReadMailArchive;
	}
	/**
	 * Tests if the trash should be emptied automatically when logging out.
	 *
	 * @return true if the trash should be emptied automatically, false
	 *         otherwise.
	 */
	public boolean isAutoEmpty(){
		return m_AutoEmpty;
	}

	/**
	 * Returns a <tt>String</tt> representing the path of the read-mail-archive.
	 *
	 * @return the path of the read-mail-archive as String.
	 */
	public String getTrashFolder(){
		return m_TrashFolder;
	}

	/**
	 * Returns a <tt>String</tt> representing the language.
	 * <p>
	 * Note that this method is a shortcut for
	 * <tt>getLocale().getLanguage()</tt>. <br>
	 *
	 * @return the language locale as <tt>String</tt>.
	 */
	public String getLanguage(){
		return m_Locale.getLanguage();
	}

	/**
	 * Returns the <tt>Locale</tt> associated with this <tt>JwmaPreferences</tt>
	 * .
	 *
	 * @return the associated <tt>Locale</tt>.
	 */
	public Locale getLocale(){
		return m_Locale;
	}

	/**
	 * Returns a <tt>Processor</tt> representing the users preferred message
	 * processor for processing text/plain messages.
	 *
	 * @return the message processor as <tt>Processor</tt>.
	 */
	public Processor getMessageProcessor(){
		return m_MessageProcessor;
	}

	public void setMessageProcessor(Processor processor){
		m_MessageProcessor = processor;
	}

	/**
	 * Returns a <tt>DateFormat</tt> representing the user's preferred date
	 * format.
	 *
	 * @return the user's preferred <tt>DateFormat</tt>.
	 */
	public DateFormat getDateFormat(){
		return m_DateFormat;
	}

	/**
	 * Tests if the user considers to be an xpert.
	 *
	 * @return true if expert, false otherwise.
	 */
	public boolean isExpert(){
		return m_Expert;
	}

	/**
	 * Returns a <tt>String</tt> representing the users preferred style.
	 *
	 * @return the style as <tt>String</tt>.
	 */
	public String getStyle(){
		return m_Style;
	}

	/**
	 * Tests if known mime message content should be inlined when displaying the
	 * message.
	 *
	 * @return true if inlining is enabled, false otherwise.
	 */
	public boolean isDisplayingInlined(){
		return m_DisplayingInlined;
	}



}// class JwmaPreferences
