package com.eazytec.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * <p>This class simply for redirecting help document.</p>
 * @author vinoth
 *
 */
@Controller
public class HelpController extends BaseFormController {
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());    
    
	
	/**
     * method for showing help document
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/help/help")
	public String showHelpDocument() {
		return "help/help";
	} 

}
