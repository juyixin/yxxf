package com.eazytec.webapp.controller;

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.licensekit.LicenseDef;
import com.licensekit.ExpDate;
import com.eazytec.model.License;
import com.eazytec.util.DateUtil;
import com.eazytec.bpm.license.service.LicenseService;
import com.eazytec.common.util.CommonUtil;

@Controller
public class LicenserController extends BaseFormController{
	
	private LicenseService licenseService;
	
	@Autowired  
	public void setLicenseService(LicenseService licenseService){
		this.licenseService = licenseService;
	}
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     * Show form to create License
     * @param model
     * @param request
     * @return
     */
   @RequestMapping(value="bpm/licenser/showCreateLicenser" , method=RequestMethod.GET)
    public ModelAndView showLiceseCreator(ModelMap model,HttpServletRequest request){
	   return new ModelAndView("/licenser/createLicenser",model);
    }
   
   /**
    * To create license key
    * @param expDate
    * @param serialField
    * @param versionField
    * @param model
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(value="bpm/licenser/getCreatedLicense" , method=RequestMethod.GET)
   public @ResponseBody	Map<String, Object>  getCreatedLicense(@ModelAttribute("expDate") String expDate,@ModelAttribute("serialField") String serialField,@ModelAttribute("versionField")String versionField,ModelMap model,HttpServletRequest request,HttpServletResponse response){
	   LicenseDef licenseDef = new LicenseDef();
	   License license = new License();
	   int i, col;
	   Map<String, Object> message = new HashMap<String, Object>();
	   
      // Assign values for the mode (module) and index (date reference) popups.
	      licenseDef.setMode(0, 3);
	      licenseDef.setMode(1, 3);
	      licenseDef.setMode(2, 3);
	      licenseDef.setMode(3, 3);
	      licenseDef.setMode(4, 3);
	      licenseDef.setMode(5, 3);
	    
	      licenseDef.setExpDateIndex(0, -1);
	      licenseDef.setExpDateIndex(1, -1);
	      licenseDef.setExpDateIndex(2, -1);
	      licenseDef.setExpDateIndex(3, -1);
	      licenseDef.setExpDateIndex(4, -1);
	      licenseDef.setExpDateIndex(5, -1);
	      
	   // Check whether the dates entered are valid.
	      // The proper format is "YYYY MM DD", or "-" if undefined.
	      try {
	    	  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	    	  license.setExpiryDate(sf.parse(expDate));
	    	  expDate=expDate.replace("-", " ");
	    	  license.setCreatedBy(CommonUtil.getLoggedInUser().getFullName());
	    	  license.setCreatedTime(new Date());
	          licenseDef.setExpDate(0, new ExpDate(expDate));
	      } catch (Exception e) {
	    	  //saveError(request,expDate);
	    	  message.put("errorMessage", "Invalid Expiry Date");
	    	  message.put("elementId", "expDate");
	      }
	      if (!licenseDef.getExpDate(0).isDefined()) {
	         for (i = 0; i < 6; i++)
	            if (licenseDef.getExpDateIndex(i) == 0) {
	            	message.put("errorMessage", "Invalid Expiry Date");
	  	    	  	message.put("elementId", "expDate");
	            }
	      }
	      
	      try {
	          licenseDef.setExpDate(1, new ExpDate("-"));
	       } catch (Exception e) {
	    	   message.put("errorMessage", "Invalid Expiry Date");
		      message.put("elementId", "expDate");
	       }
	       if (!licenseDef.getExpDate(1).isDefined()) {
	          for (i = 0; i < 6; i++)
	             if (licenseDef.getExpDateIndex(i) == 1) {
	            	 saveError(request,expDate);
	            	 message.put("errorMessage", "Invalid Expiry Date");
	   	    	  message.put("elementId", "expDate");
	             }
	       }
	             
	       try {
	          licenseDef.setExpDate(2, new ExpDate("-"));
	       } catch (Exception e) {
	    	   saveError(request,expDate);
	    	   message.put("errorMessage", "Invalid Expiry Date");
		    	  message.put("elementId", "expDate");
	       }
	       if (!licenseDef.getExpDate(2).isDefined()) {
	          for (i = 0; i < 6; i++)
	             if (licenseDef.getExpDateIndex(i) == 2) {
	            	 saveError(request,expDate);
	            	 message.put("errorMessage", "Invalid Expiry Date");
	   	    	     message.put("elementId", "expDate");
	             }
	       }
	 	          
	       // Checks to see if the User ID (aka serial number) is valid.
	       // The User ID is a combo of two characters plus four digits,
	       // from 'AA0001' to 'zz9999'. The characters are case-sensitive.

	       try {
	          licenseDef.setUserID(serialField);
	          license.setSerialNumber(serialField);
	       } catch (Exception e) {
	    	   message.put("errorMessage", "Invalid Serial number");
		       message.put("elementId", "serialFieldLetter");
	       }
	          
	       // Checks to see if the version number is valid.
	       // The proper value is 0 to 255.

	       try {
	          licenseDef.setVersion(Integer.parseInt(versionField));
	          license.setVersion(Integer.parseInt(versionField));
	       } catch (Exception e) {
	    	   message.put("errorMessage", "Invalid Version number");
		       message.put("elementId", "versionNumber");
	       }
	       
	    // Obtain Max Users. Possible values are 0 (single-user license),
	       // 1..100 (network license for 1..100 users), or 101 (unlimited
	       // network license).
	    
	       int max_users=101;
	       
	       try {
	          licenseDef.setMaxUsers(max_users);
	       } catch (Exception e) {
	    	   saveError(request,"Maximum user '"+max_users+"'");
	       }
	   
	       // Obtain Host ID. Possible values are 0 (i.e. not bound to Host ID),
	       // or a Host ID number in 1..1023 range, extracted from the "XX####"
	       // Host ID string via HostID.decodeHostID().
	          
	       int host_id=0;
	       
	       try {
	          licenseDef.setHostID(host_id);
	       } catch (Exception e) {
	    	   saveError(request,"Host Id '"+host_id+"'");
	       }
	       
	       // Check and encode the license settings.
	       
	       String keyString = "<invalid>";
	       try {
	    	  //keyString = licenseDef.encode();
	    	  license.setLicenserKey(keyString);
	    	  licenseService.saveLicense(license);
	          log.info("Licenser Key :: "+keyString);
	          message.put("sucessMessage", "Lincense created sucessfully");
		      message.put("elementId", "licenserKey");
		      message.put("licenserKey", keyString);
	       } catch (Exception e) {
	    	   message.put("errorMessage", "Error in generating key");
			   message.put("elementId", "licenserKey");
	       }
	       return message;
	 }
   
}
