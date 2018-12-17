package com.eazytec.common.util;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;


/**
 * <p>Util for template specific blocks like merging for vm templates, context etc</p>
 * 
 * @author madan
 */
public class TemplateUtil {
	
	/**
	 * Generates script by merging the context into the given template
	 * 
	 * @param velocityEngine
	 * @param templateName the template to which context merged
	 * @param context the context which has the values to be merged
	 * @return the script produced as string
	 * @throws BpmException
	 */
	public static String generateScriptForTemplate(VelocityEngine velocityEngine, String templateName, Map<String, Object> context) throws BpmException{
	        try {
	            return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName,"UTF-8", context);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new EazyBpmException("Cannot merge template for: "+templateName, e);
	        }
	}
}
