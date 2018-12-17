package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.task.CustomOperatingFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.runtime.customOperating.service.CustomOperatingService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.ListView;
import com.eazytec.model.MetaTable;

@Controller
public class CustomOperatingController extends BaseFormController {
	 /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    public VelocityEngine velocityEngine;
    private CustomOperatingService customOperatingService;
    
    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Autowired
    public void setCustomOperatingService(CustomOperatingService customOperatingService) {
        this.customOperatingService = customOperatingService;
    }
    
    /**
     * <p>To get all Custom Operations</p>
     * @param request
     * @param model
     * @return
     */
     @RequestMapping(value="bpm/customOperating/getAllCustomOperations", method = RequestMethod.GET)
       public @ResponseBody Map<String, List<Map<String, String>>> getAllCustomOperations()throws EazyBpmException {
         List<Map<String, String>> functionDetailsList = new ArrayList<Map<String, String>>();
         Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
          try{
              List<LabelValue> customOperations =  customOperatingService.getAllCustomOperations();
              if (customOperations != null){
                  for(LabelValue form : customOperations){
                      Map<String,String> formDetail = new HashMap<String, String>();
                      formDetail.put("functionName", form.getLabel());
                      formDetail.put("id", form.getValue());
                      functionDetailsList.add(formDetail);
                  }
                  resultValue.put("customOperations", functionDetailsList);
                  return resultValue;
              }
          }catch(Exception e){
              log.error("Error while getting all Custom Operating Function "+e);
          }
          return null;
       }
     
     /**
      * <p>To save or update Custom Operating Function </p>
      * @param request
      * @param model
      * @return
      */
      @RequestMapping(value="bpm/customOperating/saveCustomOperatingFunction", method = RequestMethod.POST)
        public @ResponseBody ModelAndView saveCustomOperatingFunction(@ModelAttribute("customOperatingFunction") CustomOperatingFunction customOperatingFunction,BindingResult errors, ModelMap model,HttpServletRequest request)throws EazyBpmException {
    	  
    	  Map<String, CustomOperatingFunction> resultValue = new HashMap<String, CustomOperatingFunction>();
    	  Locale locale = request.getLocale();
    	  boolean isCustomOperatingUpdate=false;
           try{
        	   if (validator != null) {
   	        	validator.validate(customOperatingFunction, errors);
	   	            if (errors.hasErrors()) { 
	   	            	model.addAttribute("customOperatingObj", customOperatingFunction);
	   	   				return new ModelAndView("process/customOperationalFunction",model);
	   	            }
   	        	}
        	   
        	   if(customOperatingFunction.getId().equals("")){
        		   if(customOperatingService.checkCustomFunctionName(customOperatingFunction.getName())){
            		   saveError(request, getText("listview.name.duplicate",customOperatingFunction.getName(),locale));
            		   model.addAttribute("customOperatingObj", customOperatingFunction);
	   	   			   return new ModelAndView("process/customOperationalFunction",model);
            	   }
               }
        	   
        	   String fileNameWithPath="";
        	   MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		        Map<String, List<MultipartFile>> fileInList = multipartRequest.getMultiFileMap();
		        List<MultipartFile> files =fileInList.get("file");
		       // the directory to upload to
		         String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
		         // Create the directory if it doesn't exist
		         File dirPath = new File(uploadDir);
		         if (!dirPath.exists()) {
		             dirPath.mkdirs();
		         }
		         //iterate the list of file and create the file in specified path and deploy it
		        if(null != files && files.size() > 0) {
		            for (MultipartFile multipartFile : files) {
		                // retrieve the file data
		                InputStream stream = multipartFile.getInputStream();
		                // write the file to the file specified
		                OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename());
		                int bytesRead;
		                byte[] buffer = new byte[8192];
		                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		                    bos.write(buffer, 0, bytesRead);
		                }
		                bos.close();
		                // close the stream
		                stream.close();
		                fileNameWithPath = "/resources/"+request.getRemoteUser() + "/"+multipartFile.getOriginalFilename();
		            }
		        } 
		        if(files!=null){
		        	customOperatingFunction.setPictureByteArrayId(fileNameWithPath);
		        }
		        
        	   	
		        if(customOperatingFunction.getId()!="" && customOperatingFunction.getId()!=null && !String.valueOf(customOperatingFunction.getId()).isEmpty() ){
        	   		log.info("Custom Function id going to update: "+customOperatingFunction.getId());
	            	isCustomOperatingUpdate=true;
	            }
	            CustomOperatingFunction customOperatingObj= customOperatingService.saveCustomOperatingFunction(customOperatingFunction,isCustomOperatingUpdate);
	            
	            if(isCustomOperatingUpdate){
	            	resultValue.put("customOperatingFunctionObj", customOperatingObj);
	            	//updated
	            	saveMessage(request, getText("customOperatingFunction.updated",customOperatingObj.getName(),locale));
	            }else{
	            	resultValue.put("customOperatingFunctionObj", customOperatingObj);
	            	//saved
	            	saveMessage(request, getText("customOperatingFunction.saved",customOperatingObj.getName(),locale));
	        	}
	            model.addAttribute("customOperatingObj", customOperatingObj);
           }catch(Exception e){
               log.error("Error while getting all Custom Operating Function "+e);
               if(isCustomOperatingUpdate){
            	   saveError(request, getText("customOperatingFunction.updated.error",locale));
               }else{
            	   saveError(request, getText("customOperatingFunction.saved.error",locale));
               }
               	
           }
     
  	     return new ModelAndView("process/customOperationalFunction",model);
        }
      
      /**
       * Show Custom operation function form to create.
       * @param listView
       * @param model
       * @param request
       * @return
       */
      @RequestMapping(value = "/bpm/customOperating/showCustomOperatingForm",method = RequestMethod.GET)
      public ModelAndView showCustomOperatingForm(@ModelAttribute("CustomOperatingFunction") CustomOperatingFunction customOperatingFunction, ModelMap model,HttpServletRequest request){
      	model.addAttribute("customOperatingFunction", customOperatingFunction);
      	return new ModelAndView("process/customOperationalFunction",model);
      }      
      
      /**
       * Show Custom operation function form to create.
       * @param listView
       * @param model
       * @param request
       * @return
       */
      @RequestMapping(value = "/bpm/customOperating/showCustomOperatingFormEdit",method = RequestMethod.GET)
      public ModelAndView showCustomOperatingFormEdit(@ModelAttribute("customOperatingFunctionId") String customOperatingFunctionId, ModelMap model,HttpServletRequest request){
    	  CustomOperatingFunction customOperatingFunction=customOperatingService.getCustomOperatingById(customOperatingFunctionId);
      	model.addAttribute("customOperatingFunction", customOperatingFunction);
      	return new ModelAndView("process/customOperationalFunction",model);
      }
      
      /**
       * Delete Custom operation function form .
       * @param listView
       * @param model
       * @param request
       * @return
       */
      @RequestMapping(value = "/bpm/customOperating/deleteCustomFunctionById",method = RequestMethod.GET)
      public @ResponseBody Map<String,Object> deleteCustomFunctionById(@ModelAttribute("customOperatingFunctionId") String customOperatingFunctionId, ModelMap model,HttpServletRequest request){
    	  Locale locale = request.getLocale();
    	  Map<String,Object> message = new HashMap<String, Object>();
    	  CustomOperatingFunction customOperatingObj=null;
    	  try{
    		  	customOperatingObj=customOperatingService.getCustomOperatingById(customOperatingFunctionId);
    		  	customOperatingService.deleteCustomFunctionById(customOperatingObj);  
    		  	log.info("Custom Function  "+customOperatingObj.getName()+" is deleted successfully.");
    		  	message.put("success", getText("customOperatingFunction.deleted",customOperatingObj.getName(),locale));
      		
    	  }catch(Exception e){
              log.error("Error while deleting Custom Operating Function "+e);
              message.put("error", getText("customOperatingFunction.delete.error",customOperatingObj.getName(),locale));
          }
    
    	  return message;
      }
}
