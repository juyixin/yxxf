package com.eazytec.webapp.controller;

/**
 * @author Megala
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.common.util.SmsUtil;

@Controller
public class SmsController {

	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    public VelocityEngine velocityEngine;
    
    /**
	 * To show the compose sms 
	 * * @param model
	 * @return
	 */
    @RequestMapping(value = "bpm/sms/composeSms",method = RequestMethod.GET)
	   public String showComposeSms(ModelMap model) {
	      return "sms/composeSms";
	   }
    
    /**
	 * To display all the Sms Deatils 
	 * @param model
	 * @return view containing list of form
	 * @throws Exception
	 */
	@RequestMapping(value="bpm/sms/smsList", method = RequestMethod.GET)
	public ModelAndView showSmsList(ModelMap model) throws Exception {
		List<Map<String,Object>> smsList = new ArrayList<Map<String, Object>>();
		String script = SmsUtil.generateScriptForSms(smsList, velocityEngine);
		model.addAttribute("script", script);
		log.info("Sms Detail Displayed Successfully");
		return new ModelAndView("sms/smsList",model);
	}
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
