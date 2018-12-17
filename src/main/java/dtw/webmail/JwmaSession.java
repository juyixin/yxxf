/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.event.ConnectionAdapter;
import javax.mail.event.ConnectionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dtw.webmail.model.JwmaException;
import dtw.webmail.model.JwmaHtmlHelper;
import dtw.webmail.model.JwmaPreferences;
import dtw.webmail.model.JwmaStoreImpl;
import dtw.webmail.model.JwmaTransportImpl;
import dtw.webmail.util.config.JwmaConfiguration;
import dtw.webmail.util.config.MailTransportAgent;
import dtw.webmail.util.config.PostOffice;

/**
 * Class that models the state of a jwma session.
 * <p>
 * Each instance stores the state and provides accessor and mutator operations
 * for retrieving and changing.
 * <p>
 * The reference to the instance itself is stored in the <tt>HttpSession</tt> as
 * session attribute.
 * <p>
 * Note that this class is actually the bridge between the web session and the
 * mail client session, and that instances have the responsibility to manage the
 * state of both.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaSession implements Serializable {

	// logging
	private static Logger log = Logger.getLogger(JwmaSession.class);

	// instance attributes
	private boolean m_Authenticated;

	// http session state related
	private List m_BeanNames;
	transient private HttpServletRequest m_Request;
	transient private HttpServletResponse m_Response;
	transient private HttpSession m_WebSession;

	// mail session state related
	transient private PostOffice m_PostOffice;
	transient private MailTransportAgent m_MTA;
	private boolean m_PostOfficeConnected;
	transient private Session m_MailSession;
	transient private JwmaStoreImpl m_JwmaStore;
	transient private JwmaTransportImpl m_JwmaTransport;
	transient private JwmaAuthenticator m_Authenticator;

	// utility attributes
	transient private JwmaConfiguration m_Configuration;
	transient private JwmaPreferences m_Preferences = new JwmaPreferences();
	transient private JwmaHtmlHelper m_HtmlHelper = new JwmaHtmlHelper();

	private String m_Username;
	private String m_LastLogin;
	private Store webMailStore;

	/**
	 * Constructs a <tt>JwmaSession</tt> instance.
	 *
	 * @param session
	 *            reference to the actual <tt>HttpSession</tt> instance.
	 */
	public JwmaSession(HttpSession session) {
		m_Configuration = JwmaKernel.getReference().getConfiguration();
		m_MTA = m_Configuration.getMTA();
		m_PostOffice = m_Configuration.getDefaultPostOffice();
		m_Authenticated = false;
		m_WebSession = session;
		m_BeanNames = new ArrayList(15);
		storeDefaultBeans();
	}// constructor

	/***
	 * Web session related part
	 ***********************************************************/

	/**
	 * Sets the reference to the actual <tt>HttpServletRequest</tt> instance.
	 *
	 * @param request
	 *            reference to the actual incoming <tt>HttpServletRequest</tt>
	 *            instance.
	 */
	public void setRequest(HttpServletRequest request) {
		m_Request = request;
	}// setRequest

	/**
	 * Returns the reference to the actual <tt>HttpServletRequest</tt> instance.
	 *
	 * @return reference to the actual <tt>HttpServletRequest</tt> instance.
	 */
	public HttpServletRequest getRequest() {
		return m_Request;
	}// getRequest

	/**
	 * Sets the reference to the actual <tt>HttpServletResponse</tt> instance.
	 *
	 * @param request
	 *            reference to the actual <tt>HttpServletResponse</tt> instance.
	 */
	public void setResponse(HttpServletResponse response) {
		m_Response = response;
	}// setResponse

	/**
	 * Returns the reference to the actual <tt>HttpServletResponse</tt>
	 * instance.
	 *
	 * @return reference to the actual <tt>HttpServletResponse</tt> instance.
	 */
	public HttpServletResponse getResponse() {
		return m_Response;
	}// getResponse

	/**
	 * Returns the reference to the actual <tt>HttpSession</tt> instance.
	 *
	 * @return reference to the actual <tt>HttpSession</tt> instance.
	 */
	public HttpSession getWebSession() {
		return m_WebSession;
	}// getWebSession

	/**
	 * Tests if the associated web session is valid. This is the case if it is
	 * not new, and if it has not been invalidated.
	 *
	 * @return true if valid, false otherwise.
	 */
	public boolean isValidWebSession() {
		try {
			if (m_WebSession == null || m_WebSession.isNew()) {
				return false;
			}
		} catch (IllegalStateException illex) {
			log.error("isValidWebSession()", illex);
			return false;
		}
		return true;
	}// isValidWebSession

	/**
	 * Sets the reference to the actual <tt>HttpSession</tt> instance.
	 *
	 * @param reference
	 *            to the actual <tt>HttpSession</tt> instance.
	 */
	public void setWebSession(HttpSession websession) {
		m_WebSession = websession;
	}// setWebSession

	private String getRequestAddress() {
		return getRequest().getRemoteHost();
	}// getRequestAddress

	/**
	 * Returns the value of a request parameter from this session's
	 * <tt>HttpServletRequest</tt>. This is a convenience method that trims the
	 * Strings.
	 *
	 * @return the value of the request parameter or null if it doesn't have a
	 *         value.
	 */
	public String getRequestParameter(String name) {
		String value = getRequest().getParameter(name);

		if (value != null && value.length() > 0) {
			return value.trim();
		} else {
			return null;
		}
	}// getRequestParameter

	/**
	 * Returns the values of a request parameter from this session's
	 * <tt>HttpServletRequest</tt>. This is a convenience method that trims the
	 * Strings.
	 *
	 * @return the value of the request parameters or null if it doesn't have
	 *         any value.
	 */
	public String[] getRequestParameters(String name) {
		String[] value = getRequest().getParameterValues(name);

		if (value != null && value.length > 0) {
			for (int i = 0; i < value.length; i++) {
				value[i] = value[i].trim();
			}
		}
		return value;
	}// getRequestParameter

	public void setWebMailStore(Store webMailStore) {
		this.webMailStore = webMailStore;
	}

	public Store getWebMailStore() {
		return this.webMailStore;
	}

	/**
	 * Stores a reference to a bean(<i>model</i>) in the actual
	 * <tt>HttpSession</tt>.
	 * <p>
	 * All stored beans are traced, so that they can be properly removed.
	 *
	 * @param name
	 *            the unique identifier of bean to be stored as session
	 *            attribute.
	 * @param bean
	 *            the reference to the bean to be stored as session attribute.
	 */
	public void storeBean(String name, Object bean) {
		m_BeanNames.add(name);
		m_WebSession.putValue(name, bean);
	}// storeBean

	/**
	 * Stores the default beans of any session. This is done directly, to bypass
	 * the mechanism that traces the other stored beans. On cleanup, the default
	 * beans remain referenceable within the session.
	 */
	private void storeDefaultBeans() {
		// the session bean
		m_WebSession.putValue("jwma.emailSession", this);

		// the HtmlHelper bean
		m_WebSession.putValue("jwma.htmlhelper", m_HtmlHelper);

	}// storeDefaultBeans

	/**
	 * Returns a reference to a bean(<i>model</i>) in the actual
	 * <tt>HttpSession</tt>.
	 * <p>
	 * If no bean with the given name/identifyer is stored in the actual
	 * session, then it returns null.
	 *
	 * @param name
	 *            the unique identifier of bean to be stored as session
	 *            attribute.
	 * @return the reference to the bean stored as session attribute or null.
	 */
	public Object retrieveBean(String name) {
		return m_WebSession.getValue(name);
	}// retrieveBean

	/**
	 * Removes a reference to a bean (<i>model</i>) in the actual
	 * <tt>HttpSession</tt>.
	 *
	 * @param name
	 *            the unique identifier of bean to be stored as session
	 *            attribute.
	 */
	public void removeBean(String name) {
		m_WebSession.removeValue(name);
	}// removeBean

	/**
	 * Removes all traced bean references from the actual <tt>HttpSession</tt>.
	 */
	private void removeBeans() {
		try {
			for (int i = 0; i < m_BeanNames.size(); i++) {
				m_WebSession.removeValue((String) m_BeanNames.get(i));
			}
			m_BeanNames.clear();
		} catch (IllegalStateException illex) {
			return;
		} catch (Exception ex) {
			log.error(JwmaKernel.getReference().getLogMessage("jwma.session.beanreferences"), ex);
		}
	}// removeBeans

	/**
	 * Returns a <tt>String</tt> representing the user's last login date and
	 * originating host.
	 *
	 * @return the last login with date and originating host as <tt>String</tt>.
	 */
	public String getLastLogin() {
		return m_LastLogin;
	}// getLastLogin

	/***
	 * End web session related part
	 *************************************************/

	/***
	 * Mail session related part
	 ****************************************************/

	/**
	 * Initializes the mail session and prepares the store wrapper instance.
	 *
	 * @param password
	 *            completing the login for the post office of the jwma user.
	 * @throws JwmaException
	 *             when something goes wrong while opening or preparing the mail
	 *             session.
	 */
	private Store initMailSession(String password) throws JwmaException {

		log.debug("initMailSession()");
		// prepare for formatted log message
		// changed by mathi.. when there is no request.
		// Object[] args = {m_Username + "@" + m_PostOffice.getAddress(),
		// getRequestAddress()};
		Object[] args = { m_Username + "@" + m_PostOffice.getAddress() };
		try {
			Properties props = System.getProperties();

			props.setProperty("mail.imap.host", m_PostOffice.getAddress());
			props.setProperty("mail.imap.socketFactory.port", "" + m_PostOffice.getPort());
			props.setProperty("mail.imap.port", "" + m_PostOffice.getPort());
			if (m_PostOffice.isSecure()) {
				props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.imap.starttls.enable", "true");
			} else {
				props.setProperty("mail.imap.socketFactory.class", "");
				props.setProperty("mail.imap.starttls.enable", "false");
			}

			props.setProperty("mail.smtp.host", m_MTA.getAddress());
			props.setProperty("mail.host", m_MTA.getAddress());
			props.setProperty("mail.smtp.port", "" + m_MTA.getPort());
			props.setProperty("mail.port", "" + m_MTA.getPort());
			props.setProperty("mail.smtp.socketFactory.port", "" + m_MTA.getPort());
			if (m_MTA.isSecure()) {
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.smtp.starttls.enable", "true");
			} else {
				props.setProperty("mail.smtp.socketFactory.class", "");
				props.setProperty("mail.smtp.starttls.enable", "false");
			}

			// JwmaSettings settings = JwmaKernel.getReference().getSettings();
			// log.debug("Retrieved Settings from Kernel");

			// prepare authenticator
			m_Authenticator = new JwmaAuthenticator(m_Username, password);
			log.debug("Prepared Authenticator");

			if (m_MailSession == null) {
				// get session instance
				m_MailSession = Session.getInstance(System.getProperties(), m_Authenticator);
			}

			// m_MailSession.setDebug(true);
			log.debug("Retrieved Session instance.");

			// setup store and connect to it
			Store store = m_MailSession.getStore(m_PostOffice.getProtocol());

			this.setWebMailStore(store);
			log.debug("Retrieved store instance");
			if (!isAuthenticated()) {
				store.connect(m_PostOffice.getAddress(), m_PostOffice.getPort(), m_Username, password);
			}
			log.debug("connected to store");
			m_PostOfficeConnected = true;

			// added by mathi
			setAuthenticated(true);

			// 1. setup MTA transport
			Transport transport = null;
			if (m_MTA.isAuthenticated()) {
				transport = m_MailSession.getTransport(new URLName(m_MTA.getProtocol(), m_MTA.getAddress(), m_MTA.getPort(), null, m_Username, password));
				log.debug("Prepared authenticated transport.");
			} else {
				transport = m_MailSession.getTransport(new URLName(m_MTA.getProtocol(), m_MTA.getAddress(), m_MTA.getPort(), null, null, null));
				log.debug("Prepared non authenticated transport.");
			}

			// create JwmaTransport wrapper instance
			m_JwmaTransport = JwmaTransportImpl.createJwmaTransportImpl(transport, m_Authenticator);
			log.debug("Created JwmaTransport wrapper instance.");

			// log to syslog
			log.info(MessageFormat.format(JwmaKernel.getReference().getLogMessage("jwma.session.login.success"), args));
			return store;

		} catch (AuthenticationFailedException afe) {
			afe.printStackTrace();
			// cleanup
			m_MailSession = null;
			// log failure to syslog
			log.info(MessageFormat.format(JwmaKernel.getReference().getLogMessage("jwma.session.login.failure"), args));
			if (!afe.getMessage().equals("[ALERT] Too many simultaneous connections. (Failure)")) {
				// throw JwmaException
				throw new JwmaException("session.login.authentication", true).setException(afe);
			} else {
				// throw JwmaException
				throw new JwmaException(afe.getMessage()).setException(afe);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			// cleanup
			m_MailSession = null;
			// throw JwmaException
			throw new JwmaException("jwma.session.initmail", true).setException(e);
		}
	}// initMailSession

	/**
	 * Ends a jwma user's mail session.
	 */
	public void endMailSession() {

		// prepare for formatted log message
		// Object[] args = {m_Username + "@" + m_PostOffice.getAddress(),
		// getRequestAddress()};
		Object[] args = { m_Username + "@" + m_PostOffice.getAddress() };
		try {
			// remove authentication flag
			setAuthenticated(false);
			// cleanup all mailsession references
			m_MailSession = null;
			m_JwmaStore.close();
			m_JwmaStore = null;

			log.info(MessageFormat.format(JwmaKernel.getReference().getLogMessage("jwma.session.logout.success"), args));

		} catch (Exception ex) {
			log.error(MessageFormat.format(JwmaKernel.getReference().getLogMessage("jwma.session.logout.failed"), args), ex);
			// JwmaKernel.getReference().debugLog().writeStackTrace(ex);
		}
	}// endMailSession

	/**
	 * Returns this instances mailsession.
	 *
	 * @return this instances mailsession as <tt>javax.mail.Session</tt>.
	 *
	 * @see javax.mail.Session
	 */
	public Session getMailSession() {
		return m_MailSession;
	}// getSession;

	/**
	 * Returns the actual mailsession's <tt>JwmaStoreImpl</tt>.
	 *
	 * @return the actual mailsession's store as <tt>JwmaStoreImpl</tt>
	 *
	 * @see dtw.webmail.model.JwmaStoreImpl
	 */
	public JwmaStoreImpl getJwmaStore() {
		return m_JwmaStore;
	}// getJwmaStore;

	/**
	 * Returns the actual mailsession's <tt>JwmaTransportImpl</tt>.
	 *
	 * @return the actual mailsession's transport as <tt>JwmaTransportImpl</tt>
	 *
	 * @see dtw.webmail.model.JwmaTransportImpl
	 */
	public JwmaTransportImpl getJwmaTransport() {
		return m_JwmaTransport;
	}// getJwmaTransport

	public MailTransportAgent getMTA() {
		return m_MTA;
	}// getMTA

	public void setMTA(MailTransportAgent MTA) {
		m_MTA = MTA;
	}// setMTA

	public PostOffice getPostOffice() {
		return m_PostOffice;
	}// getPostOffice

	public void setPostOffice(PostOffice office) {
		if (office != null) {
			m_PostOffice = office;
		}
	}// setPostOffice

	/**
	 * Tests wheter a connection to the post office exists, throwing a
	 * <tt>JwmaException</tt> if not, thus it ensures that a connection to the
	 * post office exists.
	 *
	 * @throws JwmaException
	 *             if there is no connection to the post office.
	 */
	public void ensurePostOfficeConnection() throws JwmaException {

		if (!isPostOfficeConnected()) {
			// throw JwmaException
			throw new JwmaException("jwma.session.mailconnection");
		}
	}// ensurePostOfficeConnection

	/**
	 * Tests if this session has a connection to the post office.
	 *
	 * @return true if there is a connection, false otherwise.
	 */
	public boolean isPostOfficeConnected() {
		return (m_PostOfficeConnected);
	}// isPostOfficeConnected

	/***
	 * End mail session related part
	 ********************************************/

	/**
	 * Sets the <tt>Locale</tt> of this <tt>Session</tt> to the one that is
	 * stored in the user's preferences.
	 */
	public void setLocale() {
		storeBean("jwma.locale", m_Preferences.getLocale());
	}// setLocale

	/**
	 * Returns the assembled user's identity as <tt>String</tt>.
	 *
	 * @param username
	 *            the user's name as <tt>String</tt>.
	 *
	 * @return the identifier is assembled like a standard email address using
	 *         the set postoffice host.
	 */
	public String getUserIdentity(String username) {
		return new StringBuffer(username).append('@').append(m_PostOffice.getAddress()).toString();
	}// getUserIdentity

	/**
	 * Returns the assembled user's identity as <tt>String</tt>.
	 *
	 * @return the identifier assembled like a standard email address using the
	 *         set username and hostname.
	 */
	public String getUserIdentity() {
		return new StringBuffer(m_Username).append('@').append(m_PostOffice.getAddress()).toString();
	}// getUserIdentity

	/**
	 * Returns the username associated with this <tt>JwmaSession</tt>.
	 *
	 * @return the name as <tt>String</tt>.
	 */
	public String getUsername() {
		return m_Username;
	}// getUsername

	/**
	 * Returns the <tt>JwmaPreferencesImpl</tt> instance of this session
	 * instance's user.
	 *
	 * @return this session instance's user's preferences as
	 *         <tt>JwmaPreferencesImpl</tt>.
	 */
	public JwmaPreferences getPreferences() {
		return m_Preferences;
	}// getPreferences

	/**
	 * Tests if this session is authenticated. If the session is flagged as
	 * authenticated, the initialization of the mail session worked
	 * successfully, and the user associated with this session could be
	 * authenticated against the post office.
	 *
	 * @return true if authenticated, false otherwise.
	 */
	public boolean isAuthenticated() {
		return m_Authenticated;
	}// isAuthenticated

	/**
	 * Sets wheter this session is authenticated or not. Private a really good
	 * reason: security.
	 *
	 * @param b
	 *            true if authenticated, false otherwise.
	 */
	private void setAuthenticated(boolean b) {
		m_Authenticated = b;
		storeBean("jwma.session.authenticated", "true");
	}// setAuthenticated

	/**
	 * Tests wheter this session is authenticated, throwing a
	 * <tt>JwmaException</tt> if not, thus ensuring an authenticated session.
	 *
	 * @throws JwmaException
	 *             if the session is not authenticated.
	 */
	public void ensureAuthenticated() throws JwmaException {

		if (!isAuthenticated()) {
			// throw JwmaException
			throw new JwmaException("jwma.session.authentication");
		}
	}// ensureAuthenticated

	/**
	 * Authenticates the given user by initializing the mail session, thus
	 * authenticating against the post office of this session. Note that the
	 * credentials (i.e. password) are not stored anywhere.
	 *
	 * @param username
	 *            the name of this session's user as <tt>String</tt>.
	 * @param password
	 *            the related credentials as <tt>String</tt>.
	 * @param newAccount
	 *            a flag that denotes whether a jwma account already exists
	 *            (false) or not (true).
	 *
	 * @returns true if the session was successfully authenticated, false if
	 *          not.
	 *
	 * @throws JwmaException
	 *             if initializing a mailsession or authenticating against the
	 *             post office fails.
	 *
	 */
	public boolean authenticate(String username, String password, boolean newAccount) throws JwmaException {
		log.debug("authenticate()");
		if (isAuthenticated()) {
			// we already have a mail session
			/*
			 * throw new JwmaException( "jwma.session.multiplelogin", true );
			 */
		} else {
			// store username and hostname
			m_Username = username;

			// initialize mail session
			Store store = initMailSession(password);
			if (!isAuthenticated()) {
				store.addConnectionListener(new StoreConnectionHandler(store));
			}
			log.debug("Added store connection listener.");

			// store jwmapreferences information
			if (m_Preferences != null) {
				storeBean("jwma.preferences", m_Preferences);
				log.debug("Stored prefs bean");
				setLocale();
				log.debug("Stored locale bean");
			}

			// worked, remember that
			setAuthenticated(true);
			if (m_JwmaStore == null) {
				// prepare store wrapper instance
				m_JwmaStore = JwmaStoreImpl.createJwmaStoreImpl(this, store);
			}
			log.debug("Created JwmaStore wrapper instance.");

			// and store the last login
			// setLastLogin();
		}
		return isAuthenticated();
	}// authenticate

	/**
	 * Ends this session, by cleaning up user related data and resources. If the
	 * session is authenticated, then the new last login <tt>String</tt> is
	 * stored in the users preferences, before they are saved. Consequently the
	 * mail session is closed, and the bean references stored in this session
	 * are cleaned up.
	 *
	 * @see #isAuthenticated()
	 * @see #savePreferences()
	 */
	public void end() {
		if (isAuthenticated()) {

			// end mailsession
			endMailSession();

			// removes the traced beans
			removeBeans();
		}
	}// end

	/**
	 * Overrides the superclass method to call <tt>end()</tt>.
	 *
	 * @see #end()
	 */
	public void finalize() {
		// end();
	}// finalize

	class StoreConnectionHandler extends ConnectionAdapter {

		private Store m_Store;

		public StoreConnectionHandler(Store store) {
			m_Store = store;
		}// constructor

		/**
		 * Invoked when a service connection is disconnected. This probably
		 * means the user exceeded idle timeout, and might want to be
		 * reconnected.
		 *
		 * @param e
		 *            <tt>ConnectionEvent</tt> the event that was fired.
		 */
		public void disconnected(ConnectionEvent e) {
			try {
				if (isAuthenticated()) {
					m_Store.connect(m_PostOffice.getAddress(), m_PostOffice.getPort(), m_Authenticator.getUserName(), m_Authenticator.getPassword());
					log.debug("Reconnected " + m_Store.getURLName());
				}
			} catch (Exception ex) {
				log.debug("StoreConnectionHandler.disconnected()", ex);
				m_PostOfficeConnected = false;
				end();
			}
		}// disconnected

	}// inner class StoreConnectionHandler

}// class JwmaSession
