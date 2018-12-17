package com.eazytec.webapp.jsp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspFactory;

/**
 * Registers ELResolver that escapes XML in EL expression String values.
 * 
 * @author madan
 * 
 */
public class EscapeXmlELResolverListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
    	
    	try {
			Class.forName("org.apache.jasper.compiler.JspRuntimeContext");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        JspFactory.getDefaultFactory()
                .getJspApplicationContext(event.getServletContext())
                .addELResolver(new EscapeXmlELResolver());
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
