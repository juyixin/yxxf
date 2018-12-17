/***
 * jwma Java WebMail
 * Copyright (c) 2000-2003 jwma team
 *
 * jwma is free software; you can distribute and use this source
 * under the terms of the BSD-style license received along with
 * the distribution.
 ***/
package dtw.webmail;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eazytec.bpm.oa.mail.service.MailService;
import com.eazytec.model.EmailConfiguration;

/**
 * Class extending the <tt>HttpServlet</tt> to implement the main controller of
 * jwma.
 * <p>
 * Please see the related documentation for more detailed information on process
 * flow and functionality.
 *
 * @author Dieter Wimberger
 * @version 0.9.7 07/02/2003
 */
public class JwmaController extends HttpServlet {

	// logging
	private static Logger log = Logger.getLogger(JwmaController.class);

	/**
	 * Initializes the servlet when it is loaded by the servlet engine.
	 * <p>
	 * This implementation "boots" jwma by starting up the <tt>JwmaKernel</tt>.
	 *
	 * @param config
	 *            the configuration as <tt>ServletConfig</tt>
	 *
	 * @throws ServletException
	 *             if initialization fails.
	 *
	 * @see dtw.webmail.JwmaKernel
	 */
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		try {
			// kernel bootup
			JwmaKernel myKernel = JwmaKernel.getReference();

			// check runtime environment
			String datadir = config.getInitParameter("datadir");
			boolean absolutepaths = new Boolean(config.getInitParameter("absolutepaths")).booleanValue();
			boolean status = new Boolean(config.getInitParameter("status")).booleanValue();

			if (!absolutepaths) {
				datadir = config.getServletContext().getRealPath(datadir);
			}

			ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
			MailService mailService = (MailService) ctx.getBean("mailService");
			EmailConfiguration emailConfig = mailService.getEmailConfig();
			myKernel.setEmailConfig(emailConfig);
			// set status
			myKernel.setJwmaStatusEnabled(status);
			// setup kernel with location
			myKernel.setup(datadir);

			log.info("Controller inited.");
			log.info("jwma ready.");

		} catch (Exception ex) {
			log.error("Initialization failed.");
		}
	}// init

}// class JwmaController
