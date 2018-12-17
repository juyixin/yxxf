package com.eazytec.webapp.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.bpm.model.LoggedUserDTO;
import com.eazytec.bpm.oa.mail.service.MailService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.SortHelper;
import com.eazytec.common.util.StringUtil;
import com.eazytec.model.Department;
import com.eazytec.model.EmailConfiguration;
import com.eazytec.model.Group;
import com.eazytec.model.Menu;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.LookupManager;
import com.eazytec.util.PropertyReader;


/**
 * UserCounterListener class used to count the current number
 * of active users for the applications.  Does this by counting
 * how many user objects are stuffed into the session.  It also grabs
 * these users and exposes them in the servlet context.
 *
 * @author madan
 */
public class UserCounterListener implements ServletContextListener, HttpSessionAttributeListener, HttpSessionListener {
    /**
     * Name of user counter variable
     */
    public static final String COUNT_KEY = "userCounter";
    /**
     * Name of users Set in the ServletContext
     */
    public static final String USERS_KEY = "userNames";
    /**
     * The default event we're looking to trap.
     */
    public static final String EVENT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    private transient ServletContext servletContext;
    private int counter;
    private Set<User> users;
    private Set<LoggedUserDTO> loggedUserDTOSet;

    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     * Initialize the context
     *
     * @param sce the event
     */
    public synchronized void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
    }

    /**
     * Set the servletContext, users and counter to null
     *
     * @param event The servletContextEvent
     */
    public synchronized void contextDestroyed(ServletContextEvent event) {
        servletContext = null;
        users = null;
        counter = 0;
        loggedUserDTOSet = null;
    }

    synchronized void incrementUserCounter() {
        counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter++;
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
    }

    synchronized void decrementUserCounter() {
        int counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter--;

        if (counter < 0) {
            counter = 0;
        }

        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
    }

    @SuppressWarnings("unchecked")
    synchronized void addUsername(User user) {
        users = (Set<User>) servletContext.getAttribute(USERS_KEY);

        if (users == null) {
            users = new LinkedHashSet<User>();
            loggedUserDTOSet = new LinkedHashSet<LoggedUserDTO>();
        }

        if (!users.contains(user)) {
            users.add(user);
            servletContext.setAttribute(USERS_KEY, users);
            incrementUserCounter();
            LoggedUserDTO loggedUserDTO = new LoggedUserDTO(user.getUsername(), MDC.get("ip").toString());
            loggedUserDTOSet.add(loggedUserDTO);
            CommonUtil.setLoggedUserDetail(loggedUserDTOSet);
            MDC.put("userName",user.getId());
            MDC.put("type","login");
            log.info(user.getId()+" logged in successfully");
            MDC.remove("type");
        }
    }

    @SuppressWarnings("unchecked")
    synchronized void removeUsername(User user) {
        users = (Set<User>) servletContext.getAttribute(USERS_KEY);

        if (users != null) {
            users.remove(user);
            String ip = (String) MDC.get("ip");
            LoggedUserDTO loggedUserDTO = new LoggedUserDTO(user.getUsername(), ip);
            loggedUserDTOSet.remove(loggedUserDTO);
            CommonUtil.setLoggedUserDetail(loggedUserDTOSet);
            MDC.put("type","logout");
			MDC.put("userName",user.getId());           
            log.info(user.getId()+" logged out successfully");
			MDC.remove("userName");            
			MDC.remove("type");
        }

        servletContext.setAttribute(USERS_KEY, users);
        decrementUserCounter();
    }

    /**
     * This method is designed to catch when user's login and record their name
     *
     * @param event the event to process
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
			if (securityContext != null
					&& securityContext.getAuthentication().getPrincipal() instanceof User) {
				User user = (User) securityContext.getAuthentication()
						.getPrincipal();
				ApplicationContext ctx = WebApplicationContextUtils
						.getRequiredWebApplicationContext(servletContext);
				LookupManager mgr = (LookupManager) ctx
						.getBean("lookupManager");
				User dbuser = mgr.getUser(user.getUsername());
				if (dbuser.getPassword() != null) {
					user = dbuser;
				}
				getEmailAuthendication(event.getSession(), user);
				addUsername(user);
				if( user.getRoleIds().contains("ROLE_ADMIN")){
					List<Menu> activeMenus =  mgr.getAllActiveMenus();
					Set<Menu> activeMenusSet = new HashSet<Menu>(activeMenus);
					 servletContext.setAttribute(Constants.ASSIGNED_MENUS+"_"+user.getId(),SortHelper.doSorting(activeMenusSet, "menuOrder"));
			         servletContext.setAttribute("result","Welcome");
				} else {
					setUserMenus(user);
				}
				setDefaultAttributes(user,event);
				HttpSession session = event.getSession();
				if(StringUtil.isEmptyString(user.getLanguage())){
					session.setAttribute(Constants.PREFERRED_LOCALE_KEY, new Locale("en"));
				}else{
					session.setAttribute(Constants.PREFERRED_LOCALE_KEY, new Locale(user.getLanguage()));
				}
				if (StringUtil.isEmptyString(user.getPreferredSkin())) {
					session.setAttribute(Constants.USER_PREFERRED_SKIN, "");
				} else {
					session.setAttribute(Constants.USER_PREFERRED_SKIN, user
							.getPreferredSkin());
				}
				String userid = (String) servletContext.getAttribute("userid");
				CommonUtil.setUserSessionInfo(userid, session);

			}
        }
    }

    private boolean isAnonymous() {
        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            return resolver.isAnonymous(auth);
        }
        return true;
    }

    /**
     * When user's logout, remove their name from the hashMap
     *
     * @param event the session binding event
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && (auth.getPrincipal() instanceof User)) {
                User user = (User) auth.getPrincipal();
                removeUsername(user);
            }
        }
    }

    /**
     * Needed for Acegi Security 1.0, as it adds an anonymous user to the session and
     * then replaces it after authentication. http://forum.springframework.org/showthread.php?p=63593
     *
     * @param event the session binding event
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            final SecurityContext securityContext = (SecurityContext) event.getValue();
            if (securityContext.getAuthentication() != null
                    && securityContext.getAuthentication().getPrincipal() instanceof User) {
                final User user = (User) securityContext.getAuthentication().getPrincipal();
                addUsername(user);
            }
        }
    }
    
    public void sessionCreated(HttpSessionEvent se) { 
    }
    
    public void sessionDestroyed(HttpSessionEvent se) {
        Object obj = se.getSession().getAttribute(EVENT_KEY);
        if (obj != null) {
            se.getSession().removeAttribute(EVENT_KEY);
        }
    }
    
    public void setUserMenus(User user){
    	Set<Menu> userMenus = new HashSet<Menu>();
    	//List<Menu> userMenuList = new ArrayList<Menu>();
    	ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");
        MenuService menuService = (MenuService) ctx.getBean("menuService");
    	for(Role role : user.getRoles()){
    		if(role != null) {
       		 userMenus.addAll(role.getMenus());
       		 //userMenuList.addAll(role.getMenus());
    		}
    	}
    	
    	for(Group group : user.getGroups()) {
    		Role role = mgr.getRoleByName(group.getGroupRole()); 
    		if(role != null) {
        		userMenus.addAll(role.getMenus());
        		//userMenuList.addAll(role.getMenus());
    		}
    	}
    	
    	Department department = user.getDepartment();
    		if(department != null) {
    			Role role = mgr.getRoleByName(department.getDepartmentRole());
    			if(role != null) {
            		userMenus.addAll(role.getMenus());
            		//userMenuList.addAll(role.getMenus());
    			}
    		}
    		
        List<Menu> globalMenus = mgr.getGlobalMenus();
        if(globalMenus != null){
        	userMenus.addAll(globalMenus);
        	//userMenuList.addAll(globalMenus);
        }
    	 
         //String headerMenuScript = menuService.getUserMenuScript("headerMenu.vm", SortHelper.doSorting(userMenus, "menuOrder"));
         //String topMenuScript = menuService.getUserMenuScript("topMenu.vm", SortHelper.doSorting(userMenus, "menuOrder"));
         //servletContext.setAttribute("headerMenuScript", headerMenuScript);
         //servletContext.setAttribute("topMenuScript", topMenuScript);
         // get list of possible departments
         servletContext.setAttribute(Constants.ASSIGNED_MENUS+"_"+user.getId(),SortHelper.doSorting(userMenus, "menuOrder"));
         servletContext.setAttribute("result","Welcome");
         /*for(Menu menu: mgr.getMenus(new ArrayList<Role>(user.getRoles()))){
        	
         }*/
    }
    
    /**
     * Set default values
     * @param user
     */
    public void setDefaultAttributes(User user,HttpSessionBindingEvent event){
        String userRoles = "";
        String userGroups = "";
        Map<String,Object> globalAttributes = new HashMap<String,Object>();
        
        if(user.getRoles() != null && !(user.getRoles().isEmpty())){
        	// int idx = 0;
        	 for(Role role : user.getRoles()){
        		 if(role.getRoleType().equalsIgnoreCase("Internal") != true ) {
     		    	if(userRoles.length() == 0) {
     		    		userRoles += role.getId();
    		    	}else {
    		    		userRoles += "," + role.getId();
    		    	}
        		 } 
             }
        }
        if(user.getGroups() != null && !(user.getGroups().isEmpty())){
        //	int idx = 0;
        	for(Group group : user.getGroups()){
 		    	if(userGroups.length() == 0) {
 		    		userGroups += group.getId();
		    	}else {
		    		userGroups += "," + group.getId();
		    	}
            }
        }
		HttpSession defaultValueSession = event.getSession();

		defaultValueSession.setAttribute("username", user.getFullName());
		defaultValueSession.setAttribute("userlogin", user.getUsername());
		defaultValueSession.setAttribute("userid", user.getId());
        if(null != user.getDepartment()) {
        	defaultValueSession.setAttribute("deptname", user.getDepartment().getName());
        	defaultValueSession.setAttribute("deptid", user.getDepartment().getId());
            globalAttributes.put("deptname",defaultValueSession.getAttribute("deptname"));
            globalAttributes.put("deptid",defaultValueSession.getAttribute("deptid"));
        }
        defaultValueSession.setAttribute("telephone", user.getHomePhone());
        defaultValueSession.setAttribute("roleid", userRoles);
        defaultValueSession.setAttribute("groupid", userGroups);
        // set default user attributes
        globalAttributes.put("userid",defaultValueSession.getAttribute("userid"));
        globalAttributes.put("userlogin",defaultValueSession.getAttribute("userlogin"));
        globalAttributes.put("username",defaultValueSession.getAttribute("username"));
        
        globalAttributes.put("telephone",defaultValueSession.getAttribute("telephone"));
        globalAttributes.put("roleid",defaultValueSession.getAttribute("roleid"));
        globalAttributes.put("groupid",defaultValueSession.getAttribute("groupid"));
		globalAttributes.put("path",servletContext.getRealPath("/"));
        CommonUtil.setGlobalAttributes(globalAttributes);
    }
    
    public boolean isAdmin(Set<Role> roles){
    	boolean isAdmin = false; 
    	for(Role role : roles){
    		if(role.getName().equals(Constants.ADMIN_ROLE)){
    			isAdmin = true; 
    		}
    	}
    	return isAdmin;
   }
   
    /**
     * get email authentication of user
     * 
     * @param session
     * @param user
     */
	public void getEmailAuthendication(HttpSession session, User user) {
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		MailService mailService = (MailService) ctx.getBean("mailService");
		mailService.getEmailAuthentication(session, user);

		String notificationEmail = PropertyReader.getInstance().getPropertyFromFile("String", "system.notification.email");
		String notificationPassword = PropertyReader.getInstance().getPropertyFromFile("String", "system.notification.password");

		EmailConfiguration emailConfig = mailService.getEmailConfig();

		Properties props = System.getProperties();
		props.put("mail.transport.protocol", emailConfig.getMtaProtocol());
		props.put("mail.host", emailConfig.getMtaAddress());
		props.put("mail.port", emailConfig.getMtaPort());
		props.put("mail.default.from", notificationEmail);
		props.put("mail.username", notificationEmail);
		props.put("mail.password", notificationPassword);
		props.put(" mail.smtp.auth", emailConfig.isMtaAuthenticated());
		props.put(" mail.smtp.starttls.enable", true);

	}
}
