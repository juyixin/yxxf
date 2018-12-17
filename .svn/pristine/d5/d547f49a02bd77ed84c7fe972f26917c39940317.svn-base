package com.eazytec.bpm.common.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.eazytec.exceptions.BpmException;


/**
 * <p>Util class for all logics related to Internationalization of bpm 
 * specific messages</p>
 * 
 * @author madan
 *
 */
public class I18nUtil {	
	
	private final static String bundle = "BpmResources";
	
	private final static String appBundle = "ApplicationResources";
	
	private final static String mailBundle = "mail";
	
	/**
	 * Gets the exact message for key in the preferred locale
	 * @param key for which message needed
	 * @return the message
	 */
	public static String getMessage(String key) {
		try{
			Locale locale = LocaleContextHolder.getLocale();
		    return ResourceBundle.getBundle(bundle, locale)
	                .getString(key);
		}catch(MissingResourceException e){
			throw new BpmException(e.getMessage(), e);
		}		
	  }
	
	public static String getMessageProperty(String key) {
		try{
			Locale locale = LocaleContextHolder.getLocale();
		    return ResourceBundle.getBundle(appBundle, locale)
	                .getString(key);
		}catch(MissingResourceException e){
			throw new BpmException(e.getMessage(), e);
		}		
	  }
	
	public static String getMailProperty(String key) {
		try{
			Locale locale = LocaleContextHolder.getLocale();
		    return ResourceBundle.getBundle(mailBundle, locale)
	                .getString(key);
		}catch(MissingResourceException e){
			throw new BpmException(e.getMessage(), e);
		}		
	  }
}
