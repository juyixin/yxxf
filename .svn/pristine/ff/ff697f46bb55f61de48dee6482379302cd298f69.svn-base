/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.eazytec.model.EmailConfiguration;

import dtw.webmail.model.JwmaException;
import dtw.webmail.util.config.JwmaConfiguration;
import dtw.webmail.util.config.MailTransportAgent;
import dtw.webmail.util.config.PostOffice;
import net.wimpi.text.ProcessingKernel;
import net.wimpi.text.Processor;

/**
 * A kernel that represents a hub for internal "globally" used jwma functions &
 * data (system settings).
 *
 * This class implements the singleton pattern, which means there is only one
 * instance throughout run-time.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaKernel {

	// class attributes
	private static JwmaKernel c_Self = null; // Singleton instance
	// logging
	private static Logger log = Logger.getLogger(JwmaKernel.class);

	// instance attributes
	private JwmaConfiguration m_Configuration = new JwmaConfiguration(); // configuration

	// i18n
	private URLClassLoader m_i18nLoader; // class loader for resource bundles
	private ResourceBundle m_LogMessages; // Log message resource bundle
	private ResourceBundle m_ErrorMessages; // Error message resource bundle

	// Admin related
	private boolean m_StatusEnabled; // Status enabled

	// Text processing
	private Processor m_MessageProcessor; // default message processor
	private ProcessingKernel m_ProcessingKernel; // processing kernel
	private String m_TextProcFile; // jTextproc properties

	// Directories
	private String m_RootDir; // root directory
	private String m_EtcDir; // etc dir

	private EmailConfiguration emailConfig;

	/**
	 * Constructs a JwmaKernel instance. Private to prevent construction from
	 * outside of this class.
	 */
	private JwmaKernel() {
		c_Self = this;
	}// constructor

	/**
	 * Prepares the kernel for service.
	 *
	 * @param path
	 *            representing the root directory where jwma's files reside, as
	 *            <tt>String</tt>.
	 *
	 * @throws Exception
	 *             when it fails to load the properties or set up system
	 *             functionality according to the settings in the properties.
	 */
	public void setup(String path) throws Exception {
		// Configure to log to standard out at least.
		BasicConfigurator.configure();

		// 1.setup directories
		setupDirectories(path);

		// 2.setup filenames
		setupFiles();

		setConfiguration();
		
		// 4.setup i18n
		prepareI18N();

		// From here on we write to the own log
		try {

			// 7.prepare processing
			prepareProcessing();

			// 8.prepare postoffice and transport settings
			prepareMailServices();

		} catch (JwmaException err) {
			// log exception with description and trace if inlined exception
			// available, else with stacktrace.
			if (err.hasException()) {
				log.error(err.getMessage(), err.getException());
			} else {
				log.error(err.getMessage(), err);
			}
			throw err;
		} catch (Exception ex) {
			throw ex;
		} finally {
			// JwmaStatus.createJwmaStatus();
		}
	}// setup

	/**
	 * Sets up all necessary directory paths.
	 *
	 * @param path
	 *            the root path pointing to the directory where the jwma data
	 *            resides.
	 */
	private void setupDirectories(String path) {
		// Directories
		m_RootDir = path;
		m_EtcDir = m_RootDir + File.separator + "etc" + File.separator;
	}// setupDirectories

	/**
	 * Sets up all necessary file paths.
	 */
	private void setupFiles() {
		m_TextProcFile = m_EtcDir + JwmaConfiguration.JTEXTPROC_CONFIG;
	}// setupFiles

	/**
	 * @return the emailConfig
	 */
	public EmailConfiguration getEmailConfig() {
		return emailConfig;
	}

	/**
	 * @param emailConfig
	 *            the emailConfig to set
	 */
	public void setEmailConfig(EmailConfiguration emailConfig) {
		this.emailConfig = emailConfig;
	}

	/**
	 * set external mail configuration
	 * 
	 */
	public void setConfiguration() {
		MailTransportAgent m_MTA = new MailTransportAgent(emailConfig.getMtaName(), emailConfig.getMtaAddress(), emailConfig.getMtaPort(), emailConfig.isMtaSecure(), "", emailConfig.getMtaProtocol(), emailConfig.isMtaAuthenticated());
		m_Configuration.setMTA(m_MTA);
		m_Configuration.removePostOffice(m_Configuration.getDefaultPostOffice());
		PostOffice po = new PostOffice(emailConfig.getPoName(), emailConfig.getPoAddress(), emailConfig.getPoPort(), emailConfig.getPoRootFolder(), emailConfig.isPoSecure(), emailConfig.getPoType(), emailConfig.getPoProtocol(), emailConfig.isPoDefault(), emailConfig.getPoReplyToDomain());
		m_Configuration.addPostOffice(po);
	}

	/**
	 * Prepares i18n for jwma
	 */
	private void prepareI18N() throws Exception {

		// 1. setup classloader for bundles
		URL[] urls = { new File(m_EtcDir).toURL() };
		log.debug(urls[0].toString());
		m_i18nLoader = new URLClassLoader(urls);

		// 3. load log messages bundle for system locale
		m_LogMessages = ResourceBundle.getBundle("logmessages", Locale.ENGLISH, m_i18nLoader);

		// 4. load error messages for system locale
		m_ErrorMessages = ResourceBundle.getBundle("errormessages", Locale.ENGLISH, m_i18nLoader);

		log.info(getLogMessage("jwma.kernel.prepared.i18n"));
	}// prepareI18N

	/**
	 * Prepares the processing.
	 */
	private void prepareProcessing() throws Exception {

		Properties procprops = new Properties();
		FileInputStream in = new FileInputStream(m_TextProcFile);
		procprops.load(in);

		// create processing kernel
		m_ProcessingKernel = ProcessingKernel.createProcessingKernel(procprops);

		// lookup default message processing pipe
		m_MessageProcessor = getMessageProcessor(m_Configuration.getDefaultMessageProcessor());

		if (m_MessageProcessor == null) {
			throw new Exception("Failed to load default message processing pipe.");
		}

		log.info(getLogMessage("jwma.kernel.prepared.processing"));
	}// prepareProcessing

	/**
	 * Prepares the mail service related settings.
	 */
	private void prepareMailServices() throws Exception {

		// 1. setup transport agent
		MailTransportAgent mta = m_Configuration.getMTA();

		// set system properties
		// TODO: Check if really required
		System.getProperties().put("mail." + mta.getProtocol() + ".auth", new Boolean(mta.isAuthenticated()).toString());
		System.getProperties().put("mail.smtp.host", mta.getAddress());
		if (mta.isSecure()) {
			m_Configuration.getSecurity().addSocketProperties(mta.getProtocol(), mta.getPort());
		}

		// 2. post office security setup
		for (Iterator iter = m_Configuration.getPostOffices(); iter.hasNext();) {
			PostOffice po = (PostOffice) iter.next();
			if (po.isSecure()) {
				m_Configuration.getSecurity().addSocketProperties(po.getProtocol(), po.getPort());
			}
		}

		log.info(getLogMessage("jwma.kernel.prepared.mailservices"));
	}// prepareMailServices

	/**
	 * Returns the active <tt>JwmaConfiguration</tt> instance.
	 *
	 * @return a <tt>JwmaConfiguration</tt> instance.
	 */
	public JwmaConfiguration getConfiguration() {
		return m_Configuration;
	}// getConfiguration

	/**
	 * Returns the class loader for i18n resource bundles.
	 * <p>
	 * 
	 * @return the <tt>ClassLoader</tt> used for loading i18n
	 *         <tt>ResourceBundle</tt>s.
	 */
	public ClassLoader getResourceClassLoader() {
		return m_i18nLoader;
	}// getResourceClassLoader

	/**
	 * Returns the error message for the given key in the set system locale.
	 * <p>
	 * 
	 * @return the error message as localized <tt>String</tt>.
	 */
	public String getErrorMessage(String key) {
		if (key == null) {
			return "KEY NULL!!!!!!!!";
		}
		try {
			return m_ErrorMessages.getString(key);
		} catch (MissingResourceException mrex) {
			return ("#UNDEFINED=" + key + "#");
		}
	}// getErrorMessage

	/**
	 * Returns the log message for the given key in the set system locale.
	 * <p>
	 * 
	 * @return the log message as localized <tt>String</tt>.
	 */
	public String getLogMessage(String key) {
		if (key == null) {
			return "KEY NULL!!!!!!!!";
		}
		try {
			return m_LogMessages.getString(key);
		} catch (MissingResourceException mrex) {
			return ("#UNDEFINED=" + key + "#");
		}
	}// getLogMessage

	/*** Administration *************************************************************/

	/**
	 * Tests if status is enabled.
	 *
	 * @return true if status is enabled, false otherwise.
	 */
	public boolean isJwmaStatusEnabled() {
		return m_StatusEnabled;
	}// isJwmaStatusEnabled

	/**
	 * Sets the flag that controls if the system's status is enabled.
	 *
	 * @param true
	 *            if status is enabled, false otherwise.
	 */
	public void setJwmaStatusEnabled(boolean enabled) {
		m_StatusEnabled = enabled;
	}// setJwmaStatusEnabled

	/***
	 * Text processing related methods
	 *********************************************/

	/**
	 * Returns the message processor indicated by the given name. If the name
	 * does not refer to any processing pipe, then the default processing pipe
	 * will be returned.
	 *
	 * @param name
	 *            String that should represent a valid processing pipe name.
	 * @return Processor to be used for message processing.
	 */
	public Processor getMessageProcessor(String name) {
		// shortcut for null name
		if (name == null || name.length() == 0) {
			return m_MessageProcessor;
		}
		// Try to get a pipe with the specified name
		Processor proc = m_ProcessingKernel.getProcessingPipe(name);
		if (proc == null) {
			// try to get a processor with the specified name
			proc = m_ProcessingKernel.getProcessor(name);
			if (proc == null) {
				// set the default processor
				proc = m_MessageProcessor;
			}
		}
		// return the pipe, the processor or the default
		return proc;
	}// getMessageProcessor

	public String[] listMessageProcessors() {
		// return just processing pipes for now
		return m_ProcessingKernel.listProcessingPipes();
	}// listMessageProcessors

	/*************************************************************************/

	/**
	 * Returns the reference of the JwmaKernel singleton instance.
	 *
	 * <p>
	 * <i><b>Note:</b>this also implements kind of a factory method pattern. If
	 * the singleton instance does not exist yet, it will be created.</i>
	 *
	 * @return reference of the <tt>JwmaKernel</tt> singleton instance.
	 *
	 */
	public static JwmaKernel getReference() {
		if (c_Self != null) {
			return c_Self;
		} else {
			return new JwmaKernel();
		}
	}// getReference

}// JwmaKernel
