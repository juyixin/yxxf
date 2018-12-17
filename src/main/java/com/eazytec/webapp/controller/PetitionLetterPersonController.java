package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.link.model.Link;
import com.eazytec.petitionLetter.model.PetitionLetterEvent;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.petitionLetter.service.PetitionLetterEventService;
import com.eazytec.petitionLetter.service.PetitionLetterPersonService;
import com.eazytec.webapp.util.ValidationUtil;

@Controller
public class PetitionLetterPersonController extends BaseFormController{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	public VelocityEngine velocityEngine;
	@Autowired
	private PetitionLetterPersonService petitionLetterPersonService;
	@Autowired
	private PetitionLetterEventService petitionLetterEventService;
	

	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	@RequestMapping(value = "bpm/admin/petitionLetterPersonList", method = RequestMethod.GET)
	public ModelAndView linkList(HttpServletRequest request, ModelMap model) {
        String script = null;
        String name = request.getParameter("searchText");
          try{
        	  List<PetitionLetterPerson> petitionLetterPersonList = new ArrayList<PetitionLetterPerson>();
        	  if(name!=null&&name!=""){
          		  name=java.net.URLDecoder.decode(name, "utf-8");
          		  petitionLetterPersonList = petitionLetterPersonService.searchPerson(name);
          	  }else{
          		  petitionLetterPersonList = petitionLetterPersonService.getPerson();
          	  }

			String[] fieldNames = { "id","xm", "xb", "dh", "sfzhm","address", "createTime"};
			script = GridUtil.generateScriptForPetitionLetterPersonGrid(CommonUtil.getMapListFromObjectListByFieldNames(petitionLetterPersonList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("petitionLetter/petitionLetterPersonList", model);
	}
	
	/** 
     * Description:打开新增事件页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午5:14:14
     */
	@RequestMapping(value = "bpm/admin/newEvent", method = RequestMethod.GET)
	public ModelAndView newEvent(HttpServletRequest request, ModelMap model) {
		String personId = request.getParameter("personId");
		PetitionLetterEvent event = new PetitionLetterEvent();
		event.setPersonId(personId);
		model.addAttribute("event", event);
		return new ModelAndView("petitionLetter/newEvent", model);
	}
	
	
	/** 
     * Description:打开修改查看事件页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午5:14:14
     */
	@RequestMapping(value = "bpm/admin/modifyEvent", method = RequestMethod.GET)
	public ModelAndView modifyEvent(HttpServletRequest request, ModelMap model,PetitionLetterEvent event) {
		event = petitionLetterEventService.getDetail(event.getId());
		model.addAttribute("event", event);
		model.addAttribute("first", "1");
		return new ModelAndView("petitionLetter/modifyEvent", model);
	}
	
	/** 
     * Description:保存事件信息
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午1:13:13
     */
	@RequestMapping(value = "bpm/admin/saveEvent", method = RequestMethod.POST)
	public ModelAndView saveEvent(HttpServletRequest request,@ModelAttribute("event") PetitionLetterEvent event,ModelMap model, BindingResult errors) throws Exception {
		PetitionLetterPerson  person = new PetitionLetterPerson();
	   try{
			if(event.getId().equals(Constants.EMPTY_STRING)){
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				event.setCreateTime(sdf.format(date));
				event.setIsActive("1");
				petitionLetterEventService.saveEvent(event);
				person.setId(event.getPersonId());
			}else{
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				event.setLastModifyTime(sdf.format(date));
				petitionLetterEventService.updateEvent(event);
			}
			
		}catch (Exception e) {
		   e.printStackTrace();
		}
	   String first="1";
	   return modifyPerson(request,model,person,first);
	}
	
	/** 
     * Description:打开新增页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午5:14:14
     */
	@RequestMapping(value = "bpm/admin/newPerson", method = RequestMethod.GET)
	public ModelAndView newLink(HttpServletRequest request, ModelMap model,PetitionLetterPerson person) {
		//String id = request.getParameter("id");
		//PetitionLetterPerson person = new PetitionLetterPerson();
		model.addAttribute("person", person);
		//model.addAttribute("url", person.getPhoto());
		return new ModelAndView("petitionLetter/newPerson", model);
	}
	
	/** 
     * Description:打开修改查看页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午5:14:14
	 * @throws UnsupportedEncodingException 
     */
	@RequestMapping(value = "bpm/admin/modifyPerson", method = RequestMethod.GET)
	public ModelAndView modifyPerson(HttpServletRequest request, ModelMap model,PetitionLetterPerson person,String first) throws UnsupportedEncodingException {
		String script = null;
		String id = person.getId();
		String f = request.getParameter("first");
		String personId = request.getParameter("personId");
		String name = request.getParameter("searchText");
		if(id!=null){
			//if()
			person = petitionLetterPersonService.getDetail(id);
		}else{
			id = personId;
			person = petitionLetterPersonService.getDetail(id);
		}
		model.addAttribute("oldSfzCode", person.getSfzhm());
		List<PetitionLetterEvent> petitionLetterEventList = new ArrayList<PetitionLetterEvent>();
		if(name!=null&&name!=""){
    		name=java.net.URLDecoder.decode(name, "utf-8");
    		petitionLetterEventList = petitionLetterEventService.searchEvent(name);
		}else{ 
    		petitionLetterEventList = petitionLetterEventService.getEvent(id);
        }
		String[] fieldNames = { "id","eventName", "eventDetail", "bz", "createTime"};
		script = GridUtil.generateScriptForPetitionLetterEventGrid(CommonUtil.getMapListFromObjectListByFieldNames(petitionLetterEventList, fieldNames, ""), velocityEngine);
		model.addAttribute("script", script);		
		model.addAttribute("url", person.getPhoto());
		model.addAttribute("person", person);
		if(first==null){
		/*	if(f==null){
				first = "1";
			}else{*/	
				first= f;
			/*}*/
		}
		model.addAttribute("first", first);
		return new ModelAndView("petitionLetter/modifyPerson", model);
	}
	
	
	/** 
     * Description:错误页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午5:14:14
     */
	@RequestMapping(value = "bpm/admin/errorPerson", method = RequestMethod.GET)
	public ModelAndView errorPerson(HttpServletRequest request, ModelMap model,PetitionLetterPerson person) {
		model.addAttribute("url", person.getPhoto());
		model.addAttribute("person", person);
		return new ModelAndView("petitionLetter/errorPerson", model);
	}
	
	
	/**
     * Description:保存信访人信息
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午3:42:57
     */
	@RequestMapping(value = "bpm/Link/savePersonList", method = RequestMethod.POST)
	public ModelAndView saveLinkList(HttpServletRequest request,@ModelAttribute("person") PetitionLetterPerson person,ModelMap model, BindingResult errors) throws Exception {
	    String oldSfzCode = request.getParameter("oldSfzCode");
	    String oldSfzCode1 = "";
	    if(oldSfzCode!=null){ 	
	    	if(oldSfzCode.endsWith("?")){
	    		oldSfzCode1=oldSfzCode.replace("?","");
	    	}
	    }
		try {
			String remoteUser = request.getRemoteUser();
			String contextPath = request.getContextPath();
			int count = 0;
			if (person.getId().equals(Constants.EMPTY_STRING)) {
				String a = person.getModifyFileImg();
				if(a!=null){
					
					if(a.equals("1")){
						
						MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
						CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
						if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
							String path = getUserProfileImagePath(person,file, remoteUser, contextPath,request);
							/*	String [] array = path.split(";");
					String array0 = array[0];
					String array1 = array[1];*/
							/*person.setPictureUrl(array0);
					person.setFileName(array1);*/
							person.setPhoto(path);
							//person.setImgExists("2");
						}
					}else{
						person.setPhoto(person.getModifyFileUrl());
					}
				}else{
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
					if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
						String path = getUserProfileImagePath(person,file, remoteUser, contextPath,request);
						person.setPhoto(path);
					}
				}
				count = petitionLetterPersonService.getCount(person.getSfzhm());
				if(count>0){
					person.setId(null);
					saveError(request, "身份证号已被其他信访人使用");
					return errorPerson(request,model,person);
				}
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				person.setCreateTime(sdf.format(date));
			 
				person.setIsActive("1");
				petitionLetterPersonService.savePerson(person);
			}else{
				String a = person.getModifyFileImg();
				if(a.equals("1")){
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
					if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
						String path = getUserProfileImagePath(person,file, remoteUser, contextPath,request);
						person.setPhoto(path);
					}
				}else{
					person.setPhoto(person.getModifyFileUrl());
				}
				if(!oldSfzCode1.equals(person.getSfzhm())){
					count = petitionLetterPersonService.getCount(person.getSfzhm());
					if(count>0){
						saveError(request, "身份证号已被其他信访人使用");
						String first="2";
						return modifyPerson(request,model,person,first);
					}
				}
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				person.setLastModifyTime(sdf.format(date));
				petitionLetterPersonService.updatePerson(person);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		    return linkList(request, model);
	}
	
	
	
	/**
	 * method to get user profile image path
	 *
	 * @param user
	 * @param file
	 * @param remoteUser
	 * @param contextPath
	 * @return pictureByteArrayId
	 * @throws IOException
	 * @throws InvalidNameException 
	 */
	private String getUserProfileImagePath( PetitionLetterPerson person, CommonsMultipartFile file,String remoteUser, String contextPath,HttpServletRequest request)throws IOException, InvalidNameException{
		 if (file!=null){
			 String fileName = file.getOriginalFilename();
			 int pos = fileName.lastIndexOf(".");
			 String extension = fileName.substring(pos+1);
			 fileName = fileName.substring(0, pos-1);
			 fileName = fileName.replace(" ", "_");
   	     byte[] encodedFileName = fileName.getBytes("UTF-8");
   	     fileName= encodedFileName.toString();
   	     if(fileName.startsWith("[")){
   	     fileName = fileName.substring(1);
   	     }
			 fileName = fileName+"."+extension;
   	     if(!ValidationUtil.validateImage(fileName)){
				 throw new InvalidNameException("Invalid FileName");
			 }
			 
            String uploadDir = getServletContext().getRealPath("/resources/userPhoto")+ "/";
            File dirPath = new File(uploadDir);

            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            
            InputStream stream = file.getInputStream();
            OutputStream bos = new FileOutputStream(uploadDir + fileName);
            int bytesRead;
            byte[] buffer = new byte[8192];

            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.close();
            stream.close();
            String link1 = contextPath + "/resources/userPhoto"+ "/";
            return link1 + fileName;
        }else if (person.getPhoto().isEmpty()){
        	 return "/images/profile/default.png";
        }else {
       	 return person.getPhoto();
        }
	}
	
	
	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午9:53:13
     */
	@RequestMapping(value = "bpm/admin/deletePersonList", method = RequestMethod.GET)
	@ResponseBody
	public void deleteUnitUnion(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
        JSONObject jsonResult = new JSONObject();
        String [] array = id.split(",");
        List<String> titleIdList= new ArrayList<String>();
        Collections.addAll(titleIdList, array);
	    String id2 = "";
	    String id1 = "";
	    List<String> isFalse = new ArrayList<String>();
	    for(String s : titleIdList){
	    	
	    	if(s.indexOf("[")!=-1){
	    		id2 = s.replace("[", "").trim();
	    		id1 = id2.replace("\"", "").trim();
	    		if(id1.indexOf("]")!=-1){
	    			id1 = id1.replace("]", "").trim();
	    		}
	    	}else if(s.indexOf("]")!=-1){
	    		id2 = s.replace("]", "").trim();
	    		id1 = id2.replace("\"", "").trim();
	    	}else{
	    		id1 = s.replace("\"", "").trim();
	    	}
	    	isFalse.add(id1);
	    }
	    String message="";
	    if(isFalse!=null&&isFalse.size()!=0){
			message = petitionLetterPersonService.deletePersonInfo(isFalse);
	    }
	    try {
			out = response.getWriter();
			if(message.equals("true")){
                jsonResult.put("code", "1");
                jsonResult.put("msg", "删除成功");   
            }else{
            	 jsonResult.put("code", "-1");
                 jsonResult.put("msg", "删除失败");
            } 
		}catch (IOException e) {
            jsonResult.put("code", "-1");
            jsonResult.put("msg", e.getMessage());
        } finally {
            if (out != null) {
                out.write(jsonResult.toString());
                out.flush();
                out.close();
            }
        }
	    
	}
	
	
	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午9:53:13
     */
	@RequestMapping(value = "bpm/admin/deleteEventList", method = RequestMethod.GET)
	@ResponseBody
	public void deleteEvent(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
        JSONObject jsonResult = new JSONObject();
        String [] array = id.split(",");
        List<String> titleIdList= new ArrayList<String>();
        Collections.addAll(titleIdList, array);
	    String id2 = "";
	    String id1 = "";
	    List<String> isFalse = new ArrayList<String>();
	    for(String s : titleIdList){
	    	
	    	if(s.indexOf("[")!=-1){
	    		id2 = s.replace("[", "").trim();
	    		id1 = id2.replace("\"", "").trim();
	    		if(id1.indexOf("]")!=-1){
	    			id1 = id1.replace("]", "").trim();
	    		}
	    	}else if(s.indexOf("]")!=-1){
	    		id2 = s.replace("]", "").trim();
	    		id1 = id2.replace("\"", "").trim();
	    	}else{
	    		id1 = s.replace("\"", "").trim();
	    	}
	    	isFalse.add(id1);
	    }
	    String message="";
	    String personId="";
	    if(isFalse!=null&&isFalse.size()!=0){
	    	PetitionLetterEvent event = petitionLetterEventService.getDetail(isFalse.get(0)); 
	    	personId = event.getPersonId();
			message = petitionLetterEventService.deleteEvent(isFalse);
	    }
	    try {
			out = response.getWriter();
			if(message.equals("true")){
                jsonResult.put("code", "1");
                jsonResult.put("first", "1"); 
                jsonResult.put("personId", personId);
                jsonResult.put("msg", "删除成功");   
            }else{
            	 jsonResult.put("code", "-1");
                 jsonResult.put("msg", "删除失败");
            } 
		}catch (IOException e) {
            jsonResult.put("code", "-1");
            jsonResult.put("msg", e.getMessage());
        } finally {
            if (out != null) {
                out.write(jsonResult.toString());
                out.flush();
                out.close();
            }
        }
	    
	}
	
}
