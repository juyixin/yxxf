package com.eazytec.webapp.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eazytec.Constants;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Menu;
import com.eazytec.model.SysConfig;
import com.eazytec.service.LookupManager;
import com.eazytec.util.PropertyReader;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 * <p/>
 * <p>This listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the
 * GenericDao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author madan
 */
public class StartupListener implements ServletContextListener {
    private static final Log log = LogFactory.getLog(StartupListener.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent event) {
//        log.info("Starting Server");

        ServletContext context = event.getServletContext();

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map<String, Object> config = (HashMap<String, Object>) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap<String, Object>();
        }

        ApplicationContext ctx =
                WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        PasswordEncoder passwordEncoder = null;
        try {
            ProviderManager provider = (ProviderManager) ctx.getBean("org.springframework.security.authentication.ProviderManager#0");
            for (Object o : provider.getProviders()) {
                AuthenticationProvider p = (AuthenticationProvider) o;
                if (p instanceof RememberMeAuthenticationProvider) {
                    config.put("rememberMeEnabled", Boolean.TRUE);
                } else if (ctx.getBean("passwordEncoder") != null) {
                    passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncoder");
                }
            }
        } catch (NoSuchBeanDefinitionException n) {
            log.info("authenticationManager bean not found, assuming test and ignoring...");
            // ignore, should only happen when testing
        }

        context.setAttribute(Constants.CONFIG, config);

        // output the retrieved values for the Init and Context Parameters
        if (log.isDebugEnabled()) {
            log.info("Remember Me Enabled? " + config.get("rememberMeEnabled"));
            if (passwordEncoder != null) {
                log.info("Password Encoder: " + passwordEncoder.getClass().getSimpleName());
            }
            log.info("Populating drop-downs...");
        }

        setupContext(context);
    }

    /**
     * This method uses the LookupManager to lookup available roles from the data layer.
     *
     * @param context The servlet context
     */
    public static void setupContext(ServletContext context) {
    	
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");

        // get list of possible users
        Map<String, String> usersMap = getAsMap(mgr.getAllUsers());
        if(usersMap.size()>0 && usersMap!=null){
        	context.setAttribute("usersMap", usersMap);
        	context.setAttribute("usersJSON", new JSONObject(usersMap));
        }
        
        Map<String, String> departmentsMap = getAsMap(mgr.getAllDepartments());
        if(departmentsMap.size()>0 && departmentsMap!=null){
        	context.setAttribute("departmentsMap", departmentsMap);
        	context.setAttribute("departmentsJSON", new JSONObject(departmentsMap));
        }
        
        Map<String, String> groupsMap = getAsMap(mgr.getAllGroups());
        if(groupsMap.size()>0 && groupsMap!=null){
        	context.setAttribute("groupsMap", groupsMap);
        	context.setAttribute("groupsJSON", new JSONObject(groupsMap));
        }
        
        Map<String, String> rolesMap = getAsMap(mgr.getAllRoles());
        if(rolesMap.size()>0 && rolesMap!=null){
        	context.setAttribute("rolesMap", rolesMap);
        	context.setAttribute("rolesJSON", new JSONObject(rolesMap));
        }
        
        // get list of possible roles
        context.setAttribute(Constants.AVAILABLE_USERS, new HashSet<LabelValue>(mgr.getAllUsers()));
//        log.info("Loading All Users");
        
        // get list of possible roles
        context.setAttribute(Constants.AVAILABLE_ROLES, new HashSet<LabelValue>(mgr.getAllRoles()));
//        log.info("Loading All Roles");
        
        // get list of possible gropus
        context.setAttribute(Constants.AVAILABLE_GROUPS, new HashSet<LabelValue>(mgr.getAllGroups()));
//        log.info("Loading All Groups");

        // get list of possible departments
        context.setAttribute(Constants.AVAILABLE_DEPARTMENTS, new HashSet<LabelValue>(mgr.getAllDepartments()));
//        log.info("Loading All Department");
        
        // get list of possible departments
        context.setAttribute(Constants.AVAILABLE_MENUS, new HashSet<Menu>(mgr.getAllMenus()));
//        log.info("Loading All Departments");
        
        // get list of possible departments
        context.setAttribute(Constants.AVAILABLE_TOP_MENUS,mgr.getAllTopMenus());
//        log.info("Loading All Top Menus");
        
        // get list of possible task roles
        context.setAttribute(Constants.AVAILABLE_TASK_ROLES,mgr.getAllTaskRoles());
//        log.info("Loading All Task Roles");
        
        context.setAttribute(Constants.AVAILABLE_RT_TASK_ROLES,mgr.getTaskRolesApplicableForRuntime());
//        log.info("Task Role : Applicable runtime task roles initialization complete [OK]");
        
        // get list of process classification
        context.setAttribute(Constants.PROCESS_CLASSIFICATION,mgr.getAllProcessClassifications());
//        log.info("Loading All Classification");
        
        context.setAttribute(Constants.AGENCY_SETTING,mgr.getAllAgencySetting());
//        log.info("Agency Setting  : Agency Setting List initialization complete [OK]");
        
//        log.info("update server time zone");
        mgr.updateTimeZonetoSysconfig();
        
//        log.info("Start Loading Application Configuration");
        loadSysConfigParameters(mgr);
		
//        log.info("Sysconfig initialization complete [OK]");

    }
	
    private static Map<String, String> getAsMap(List<LabelValue> list){
		Map<String, String> map = new HashMap<String, String>();
		if(list != null && list.size() > 0){
			for(LabelValue lv : list){
				map.put(lv.getValue(),lv.getLabel());
			}
		}
		return map;
	}

    /**
     * Shutdown servlet context (currently a no-op method).
     *
     * @param servletContextEvent The servlet context event
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //LogFactory.release(Thread.currentThread().getContextClassLoader());
        //Commented out the above call to avoid warning when SLF4J in classpath.
        //WARN: The method class org.apache.commons.logging.impl.SLF4JLogFactory#release() was invoked.
        //WARN: Please see http://www.slf4j.org/codes.html for an explanation.
    }
    
    
    public static void loadSysConfigParameters(LookupManager mgr) {
		List parameterList = null;
		String configType = null;
		Properties properties;
		PropertyReader reader = PropertyReader.getInstance();
		List<String> sysConfigList = mgr.getAllSysConfig();

		if (sysConfigList != null && sysConfigList.size() > 0) {
			/*if (logger.isDebugEnabled()) {
				logger
						.debug(MessageToken
								.getMessage(
										MessageToken.DEBUG_LOADSYSCONFIGPARAMETERS_CONFIG_FOUND,
										new Object[] { configList.size() }));
			}*/
			
			for (String sysConfig : sysConfigList) {
				System.out.println("================sysConfig================="+sysConfig);
				properties = new Properties();
				String key = null;
				String value = null;
				configType =sysConfig;
				if (configType != null) {
					parameterList = mgr.getSysConfigsByType(configType);
					for (int j = 0; j < parameterList.size(); j++) {
						SysConfig config = (SysConfig) parameterList.get(j);
						key = config.getSelectKey();
						value = config.getSelectValue();
						if(value!=null && value!="" && !value.isEmpty()){
							properties.put(key, value);
						}
					}
				}
				reader.setProperties(configType, properties);
				/*if (logger.isDebugEnabled()) {
					logger
							.debug(MessageToken
									.getMessage(
											MessageToken.DEBUG_LOADSYSCONFIGPARAMETERS_CONFIG_ADDED_TO_PROPERTIES,
											null));
				}*/
			
			}
			/*for (int i = 0; i < sysConfigList.size(); i++) {

				properties = new Properties();
				String key = null;
				String value = null;
				configType = (String) sysConfigList.get(i);
				if (configType != null) {
					parameterList = mgr.getSysConfigsByType(configType);
					for (int j = 0; j < parameterList.size(); j++) {
						SysConfig config = (SysConfig) parameterList.get(j);
						key = config.getselectKey();
						value = config.getselectValue();
						properties.put(key, value);
					}
				}
				reader.setProperties(configType, properties);
				if (logger.isDebugEnabled()) {
					logger
							.debug(MessageToken
									.getMessage(
											MessageToken.DEBUG_LOADSYSCONFIGPARAMETERS_CONFIG_ADDED_TO_PROPERTIES,
											null));
				}
			}*/
		}
	}
}
