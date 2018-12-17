/**
 * 
 */
package com.eazytec.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.model.User;
import com.eazytec.service.UserExistsException;

/**
 * <p>All config related controller classes like changing languages, preferences etc</p>
 * @author madan
 *
 */

@Controller
public class ConfigController extends BaseFormController{

	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());   
    
	
	/**
     * method for updating locale
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/admin/setLocale",method = RequestMethod.GET)
	public String setLocaleConfig(@RequestParam("locale") String locale, HttpServletRequest request,ModelMap model) {
    	HttpSession session = request.getSession(false);
    	Locale localeObj = null;
   	
    	try{
    		User user = CommonUtil.getLoggedInUser();
    		if(locale.equals("en")){
    			localeObj = new Locale(locale);
    		}else{
    			localeObj = Locale.SIMPLIFIED_CHINESE;
    		}
    		session.setAttribute(Constants.PREFERRED_LOCALE_KEY, localeObj);
    		Config.set(session, Config.FMT_LOCALE, localeObj);
    		user.setLanguage(locale);
    		getUserManager().saveUser(user);
 	
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	
		return "redirect:/";
	}   
    
    /**
    * method for updating User Preferred Skin
    * 
    * @param request
    * @param model
    * @return
    */
    @RequestMapping(value = "bpm/admin/setUserPreferredSkin",method = RequestMethod.GET)
    public String setUserPreferredSkinConfig(@RequestParam("preferredSkin") String preferredSkin, HttpServletRequest request,ModelMap model) {
	   	HttpSession session = request.getSession();
	   	try {
		   	User user  = CommonUtil.getLoggedInUser();
		   	session.setAttribute(Constants.USER_PREFERRED_SKIN, preferredSkin);
		   	user.setPreferredSkin(preferredSkin);
			getUserManager().saveUser(user);
			
		} catch (UserExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   	
		return "redirect:/";
	}  
}
