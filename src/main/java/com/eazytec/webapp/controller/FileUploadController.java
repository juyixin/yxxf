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
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.sysConfig.service.SysConfigManager;
import com.eazytec.model.SysConfig;

/**
 * Controller class to upload Files.
 *
 * @author madan
 */
@Controller
//@RequestMapping("/fileupload*")
public class FileUploadController extends BaseFormController {

    public FileUploadController() {
        setCancelView("redirect:/mainMenu");
        setSuccessView("uploadDisplay");
    }
    
    private SysConfigManager sysConfigManager;

    
    @Autowired
	public void setsysConfigManager(SysConfigManager sysConfigManager) {
		this.sysConfigManager = sysConfigManager;
	}

    
    @ModelAttribute
    public FileUpload showForm() {
        return new FileUpload();
    }

    @RequestMapping(value = "/fileupload*", method = RequestMethod.POST)
    public String onSubmit(FileUpload fileUpload, BindingResult errors, HttpServletRequest request)
            throws Exception {

        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }

        if (validator != null) { // validator is null during testing
            validator.validate(fileUpload, errors);

            if (errors.hasErrors()) {
                return "fileupload";
            }
        }

        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args =
                    new Object[]{getText("uploadForm.file", request.getLocale())};
            errors.rejectValue("file", "errors.required", args, "File");

            return "fileupload";
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

        // the directory to upload to
        String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
        
        
        
        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + file.getOriginalFilename());
        int bytesRead;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", fileUpload.getName());
        request.setAttribute("fileName", file.getOriginalFilename());
        request.setAttribute("contentType", file.getContentType());
        request.setAttribute("size", file.getSize() + " bytes");
        request.setAttribute("location", dirPath.getAbsolutePath() + Constants.FILE_SEP + file.getOriginalFilename());

        String link = request.getContextPath() + "/resources" + "/" + request.getRemoteUser() + "/";
        request.setAttribute("link", link + file.getOriginalFilename());

        return getSuccessView();
    }
    
    /**
     * Show custom icon page for upload
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "bpm/fileupload/showCustomIconPage",method = RequestMethod.GET)
    public ModelAndView showListView(FileUpload fileUpload, ModelMap model,HttpServletRequest request){
    	model.addAttribute("fileUpload", fileUpload);
    	return new ModelAndView("/showCustomIconPage", model);
    }
    
    /**
     * To remove the selected custom icon
     * @param src
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="bpm/fileupload/removeCustomIcon", method = RequestMethod.GET)
    public @ResponseBody  Map<String,String> removeCustomIcon(@RequestParam("src") String src, ModelMap model,HttpServletRequest request){
    	Locale locale = request.getLocale();
    	Map<String,String> removeCustomIcon=new HashMap<String, String>();
        try {
        	  String[] iconPathSplit=src.split("/");
        	  String iconName=iconPathSplit[iconPathSplit.length-1];
        	
        	  String menuIconDir = getServletContext().getRealPath("/images/sidemenu");
        	  File iconDir = new File(menuIconDir);
  			  for(File icon : iconDir.listFiles()){
  				  String iconPath=icon.getAbsolutePath();
  				  if(iconPath.contains(iconName)){
  					if(icon.delete()){
  						removeCustomIcon.put("status","success");
  						removeCustomIcon.put("message",getText("custom.icon.remove.success", locale) );
  					    log.info(getText("custom.icon.remove.success", locale));
  					}else{
  						removeCustomIcon.put("status","error");
  						removeCustomIcon.put("message", getText("custom.icon.remove.error", locale));
   					    log.info(getText("custom.icon.remove.error", locale));

  					}
  				  }
	  			}
      	 } catch (Exception e) {
	         log.info("Error while removing CustomIcon ",e);
	         	removeCustomIcon.put("status","error");
				removeCustomIcon.put("message", getText("custom.icon.remove.error", locale));
	     }
	     return removeCustomIcon;
    }
    
    /**
     * To remove the selected logo
     * @param src
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="bpm/fileupload/removeLogo", method = RequestMethod.GET)
    public @ResponseBody  Map<String,String> removeLogo(@RequestParam("src") String src, ModelMap model,HttpServletRequest request){
    	Locale locale = request.getLocale();
    	Map<String,String> removeLogo=new HashMap<String, String>();
        try {
        	  String[] logoPathSplit=src.split("/");
        	  String logoName=logoPathSplit[logoPathSplit.length-1];
        	
        	  String menuIconDir = getServletContext().getRealPath("/images/logo/");
        	  File logoDir = new File(menuIconDir);
  			  for(File logo : logoDir.listFiles()){
  				  String logoPath=logo.getAbsolutePath();
  				  if(logoPath.contains(logoName)){
  					if(logo.delete()){
  						removeLogo.put("status","success");
  						removeLogo.put("message",getText("logo.delete.success", locale) );
  						log.info(getText("logo.delete.success", locale));
  					}else{
  						removeLogo.put("status","error");
  						removeLogo.put("message", getText("logo.delete.error", locale));
  					    log.info(getText("logo.delete.error", locale));
  					}
  				  }
	  			}
      	 } catch (Exception e) {
	         log.info("Error while removing Logo ",e);
	         removeLogo.put("status","error");
	         removeLogo.put("message", getText("logo.delete.error", locale));
	     }
	     return removeLogo;
    }
    
    /**
     * Upload custom icon
     * @param model
     * @throws Exception
     */
    @RequestMapping(value="bpm/fileupload/uploadCustomIcon", method = RequestMethod.POST)
     public String  uploadFormImage(FileUpload fileUpload, BindingResult errors, ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Locale locale = request.getLocale();
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }

        if (validator != null) { // validator is null during testing
            validator.validate(fileUpload, errors);

            if (errors.hasErrors()) {
                return "/showCustomIconPage";
            }
        }

        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args =
                    new Object[]{getText("uploadForm.file", request.getLocale())};
            errors.rejectValue("file", "errors.required", args, "File");

            return "/showCustomIconPage";
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

        // the directory to upload to
        String uploadDir = getServletContext().getRealPath("/images/sidemenu/");
        
        
        
        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir +"/"+ file.getOriginalFilename());
        int bytesRead;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", fileUpload.getName());
        request.setAttribute("fileName", file.getOriginalFilename());
        request.setAttribute("contentType", file.getContentType());
        request.setAttribute("size", file.getSize() + " bytes");
        request.setAttribute("location", dirPath.getAbsolutePath() + Constants.FILE_SEP + file.getOriginalFilename());

        String link = request.getContextPath() + "/resources" + "/" + request.getRemoteUser() + "/";
        request.setAttribute("link", link + file.getOriginalFilename());
        saveMessage(request, getText("custom.icon.uploaded.successfully", locale));
        log.info("CustomIcon Uploaded Successfully ");
        return "/showCustomIconPage";
    }
    
    /**
     * Show logo upload page for upload
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "bpm/fileupload/showLogoUploadPage",method = RequestMethod.GET)
    public ModelAndView showLogoUploadPage(FileUpload fileUpload, ModelMap model,HttpServletRequest request){
    	model.addAttribute("fileUpload", fileUpload);
    	return new ModelAndView("/showLogoUploadPage", model);
    }   
    
    
    /**
     * Upload custom icon
     * @param model
     * @throws Exception
     */
    @RequestMapping(value="bpm/fileupload/uploadLogoImage", method = RequestMethod.POST)
     public String  uploadLogoImage(FileUpload fileUpload, BindingResult errors, ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Locale locale = request.getLocale();
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }

        if (validator != null) { // validator is null during testing
            validator.validate(fileUpload, errors);

            if (errors.hasErrors()) {
                return "/showLogoUploadPage";
            }
        }

        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args =
                    new Object[]{getText("uploadForm.file", request.getLocale())};
            errors.rejectValue("file", "errors.required", args, "File");

            return "/showLogoUploadPage";
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

        // the directory to upload to
        String uploadDir = getServletContext().getRealPath("/images/logo/");
        
        
        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir +"/"+ file.getOriginalFilename());
        int bytesRead;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", fileUpload.getName());
        request.setAttribute("fileName", file.getOriginalFilename());
        request.setAttribute("contentType", file.getContentType());
        request.setAttribute("size", file.getSize() + " bytes");
        request.setAttribute("location", dirPath.getAbsolutePath() + Constants.FILE_SEP + file.getOriginalFilename());

        String link = request.getContextPath() + "/resources" + "/" + request.getRemoteUser() + "/";
        request.setAttribute("link", link + file.getOriginalFilename());
        request.setAttribute("logoPath", "/images/logo/");
        saveMessage(request, getText("logo.uploaded.successfully", locale));
		log.info("Selected logo uploaded successfully");
        return "/showLogoUploadPage";
    }
   
    
    /**
   	 * To get icons through ajax call
   	 * @return icons url
   	 */
   	 @RequestMapping(value="bpm/fileupload/getCustomIcons", method = RequestMethod.GET)
   	   public @ResponseBody List<String> getCustomIcons(HttpServletRequest request) {
   		  List<String> menuIcons = new ArrayList<String>();
   		  try{
   			  String menuIconDir = getServletContext().getRealPath("/images/sidemenu");
   			  File iconDir = new File(menuIconDir);
   			  for(File icon : iconDir.listFiles()){  
                menuIcons.add("/images/sidemenu/"+icon.getName());
              }
   			  return menuIcons;
   		  }catch(Exception e){
   			  log.error("Error while getting menu icon list ",e);
   		  }		 
   	      return null;
   	   }
   	 
   	/**
	 * To get icons through ajax call
	 * @return icons url
	 */
	 @RequestMapping(value="bpm/fileupload/getAllLogos", method = RequestMethod.GET)
	   public @ResponseBody List<String> getAllLogos(HttpServletRequest request) {
		  List<String> menuIcons = new ArrayList<String>();
		  try{
			  String menuIconDir = getServletContext().getRealPath("/images/logo/");
			  File iconDir = new File(menuIconDir);
			  if(iconDir.exists()){
				  for(File icon : iconDir.listFiles()){  
					  menuIcons.add("/images/logo/"+icon.getName());
		           }
			  }
			  return menuIcons;
		  }catch(Exception e){
			  log.error("Error while getting logo list ",e);
		  }		 
	      return null;
	   }
	 
	    /**
		 * To get logo through ajax call
		 * @return icons url
		 */
		 @RequestMapping(value="bpm/fileupload/getLogo", method = RequestMethod.GET)
		   public @ResponseBody List<String> getLogo(HttpServletRequest request) {
			 List<String> logoPath = new ArrayList<String>();
			  try{
				  String sysConfigId = "1c437d9c-317a-11e3-b8a4-00270e0048cc";
				  SysConfig sysConfig = sysConfigManager.getId(sysConfigId);
				  logoPath.add(sysConfig.getSelectValue());
				  return logoPath;
			  }catch(Exception e){
				  log.error("Error while getting logo ",e);
			  }		 
		      return logoPath;
		   }
	 
		/**
		 * To get icons through ajax call
		 * @return icons url
		 */
		 @RequestMapping(value="bpm/fileupload/setSelectedLogoAsActive", method = RequestMethod.GET)
		   public @ResponseBody List<String> getAllLogos(@RequestParam("logoPath") String logoPath, HttpServletRequest request) {
			  List<String> logoPaths = new ArrayList<String>();
			  try{
				  SysConfig sysConfig = new SysConfig(); 
				  sysConfig.setId("1c437d9c-317a-11e3-b8a4-00270e0048cc");
				  sysConfig.setSelectKey("system.app.logo");
				  sysConfig.setSelectType("String");
				  sysConfig.setSelectValue(logoPath);
				  sysConfigManager.save(sysConfig);
				  logoPaths.add(sysConfig.getSelectValue());
				  log.info("Selected logo as active");
				  return logoPaths;
			  }catch(Exception e){
				  log.error("Set selected logo as active ",e);
			  }		 
		      return null;
		   }
}
