package com.eazytec.webapp.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.log4j.Logger;

import com.eazytec.bpm.admin.systemLog.service.SystemLogManager;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;
import com.eazytec.webapp.listener.StartupListener;
import com.eazytec.webapp.listener.UserCounterListener;

/**
 * Filter to wrap request with a request including user preferred locale.
 * 
 * @author Sangeetha Guhan
 */
public class LoginFilter extends OncePerRequestFilter {

    protected final Log log = LogFactory.getLog(getClass());

	/**
	 * This method looks for a "locale" request parameter. If it finds one, it
	 * sets it as the preferred locale and also configures it to work with JSTL.
	 * 
	 * @param request
	 *            the current request
	 * @param response
	 *            the current response
	 * @param chain
	 *            the chaindoFilterInternal
	 * @throws IOException
	 *             when something goes wrong
	 * @throws ServletException
	 *             when a communication failure happens
	 */
	@SuppressWarnings("unchecked")
	public void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,FilterChain chain)
			throws IOException, ServletException {
		
		String username = request.getParameter("j_username");
		username = request.getParameter("username");

		 final Logger logger = Logger.getLogger("filter");
		 if(MDC.get("userName") == null){
			 MDC.put("userName","");
		 }else{
			 MDC.put("userName",MDC.get("userName"));
		 }
		 
		String error = request.getParameter("error");
		String ip=((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
		if(ip == null || ip == "" || ip.length() <= 0 || ip.isEmpty()){
			ip = request.getRemoteAddr();
		}
		MDC.put("ip",ip);
		Enumeration names = request.getSession().getAttributeNames();
		HttpSession sess = request.getSession();
		Enumeration e = request.getSession().getAttributeNames();
		if(e.hasMoreElements()){
//			MDC.put("userName", CommonUtil.getLoggedInUserId());
//			System.out.println("SessionAttribute: "+ CommonUtil.getLoggedInUserId());
		}
//		for (Enumeration e = request.getSession().getAttributeNames(); 
//				e.hasMoreElements();) {
////			MDC.put("userName",CommonUtil.getLoggedInUserId());
//			String attrib = new String(e.nextElement().toString());
//			System.out.println("SessionAttribute: " +e.hasMoreElements());
//			//System.out.println(request.getSession().getAttribute(attrib));
//		}
//		if(CommonUtil.getLoggedInUserId()!=null){
//			System.out.println("============================================="+CommonUtil.getLoggedInUserId());
//		}
		Object object = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if (object instanceof BadCredentialsException) {
			BadCredentialsException badCredentialsException = (BadCredentialsException) object;
			//System.out.println("========== " + badCredentialsException.getAuthentication().getName());
		}
		if (StringUtil.checkAndParseBooleanAttribute(error)) {
			ApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext());
			SystemLogManager systemLogManager = (SystemLogManager) ctx
					.getBean("systemLogManager");
			String timeStamp = DateUtil.getCurrentDateTime();
			String ipAddr = ((ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes()).getRequest().getRemoteAddr();
			//systemLogManager.saveUserDetails("", "login",	ipAddr, timeStamp,"Error","Wrong User name or Password");
			//MDC.put("userName",username);
			//logger.info("Wrong User name or Password");
			log.info("Wrong User name or Password");
		}
		chain.doFilter(request, response);
	}
}
