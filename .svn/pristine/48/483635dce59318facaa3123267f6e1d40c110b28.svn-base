package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.ClockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.opinion.service.UserOpinionService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Opinion;
import com.eazytec.model.UserOpinion;
import com.eazytec.service.UserManager;
import com.eazytec.util.DateUtil;


@Controller
public class UserOpinionController extends BaseFormController{
	
	private UserOpinionService userOpinionService; 
	
	@Autowired
	public void setUserOpinionService(UserOpinionService userOpinionService) {
		this.userOpinionService = userOpinionService;
	}
	
	/**
	 * create new User opinion word.
	 * 
	 * @param model
	 * @return
	 */
    @RequestMapping(value="bpm/opinion/userOpinion" ,method = RequestMethod.GET)
	public  ModelAndView createUserOpinion(ModelMap model) {
    	UserOpinion userOpinion = new UserOpinion();
    	model.addAttribute(userOpinion);
    	return new ModelAndView("userOpinion",model);
    }
    
    /**
	 * edit the user Opinion word.
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
    @RequestMapping(value = "bpm/opinion/editUserOpinion",method = RequestMethod.GET)
    public ModelAndView editUserOpinion(@RequestParam("id") String id,ModelMap model,HttpServletRequest request,
	           HttpServletResponse response) {
    	
    	try{
    		UserOpinion userOpinion = userOpinionService.getUserOpinionById(id);	
    		model.addAttribute("userOpinion", userOpinion);
			log.info("userOpinion edited successfully");
    	}catch(Exception ex){
			log.info(ex.getMessage(),ex);
    	}	
            return new ModelAndView("userOpinion", model);   
    }
	 
    	
		@RequestMapping(value = "bpm/opinion/saveUserOpinion",method = RequestMethod.POST)
	    public ModelAndView saveUserOpinion(ModelMap model,@ModelAttribute("userOpinion") UserOpinion userOpinion,HttpServletRequest request,
	           HttpServletResponse response, BindingResult errors) { 
			 String listView="USEROPINION";
			 String container = "target";
			 String listViewParams = "[{}]";
			 String title = "User Opinion";
			 model.addAttribute("listViewName",listView);
			 model.addAttribute("container",container);
			 model.addAttribute("listViewParams",listViewParams);
			 model.addAttribute("title",title);
			 String id = userOpinion.getId();	     
			 String userOpinionId = request.getParameter("id");
			 	 
		 try{
			 if (validator != null) {
		        validator.validate(userOpinion, errors); 
		        if (errors.hasFieldErrors("word")) {
		            model.addAttribute("userOpinionId", userOpinionId);
		            return new ModelAndView("userOpinion", model);
		        } 
		     }
			  userOpinionService.saveUserOpinion(userOpinion);
			  log.info("userOpinion Saved successfully");
			} catch(Exception ex){
				log.info("Duplicate Entry of word",ex);
				saveError(request,"Duplicate Entry of word.");	
				
			}
			return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
		}
	 
		/**
		 * delete the userOpinion.
		 * 
		 * @param userOpinionIds
		 * @param request
		 * 
		 * @return
		 */
	 	@RequestMapping(value = "bpm/opinion/deleteUserOpinion", method = RequestMethod.GET)
		public ModelAndView deleteUserOpinion(@RequestParam("userOpinionIds") String userOpinionId, HttpServletRequest request,ModelMap model) {
		 Locale locale = request.getLocale();
		 List<String> userOpinionIdList = new ArrayList<String>();
		 String listView="USEROPINION";
		 String container = "target";
		 String listViewParams = "[{}]";
		 String title = "User Opinion";
		 model.addAttribute("listViewName",listView);
		 model.addAttribute("container",container);
		 model.addAttribute("listViewParams",listViewParams);
		 model.addAttribute("title",title);
		 
		 if (userOpinionId.contains(",")) {
			  String[] ids = userOpinionId.split(",");
			  for(String id :ids){
				  userOpinionIdList.add(id);
			  }
			} else {
				userOpinionIdList.add(userOpinionId);
			}
			try{
				
				boolean isDelete = userOpinionService.deleteUserOpinion(userOpinionIdList);
				if(isDelete = true){
					saveMessage(request,"Deleted Successfully");
		        	log.info("opinion deleted Successfuly");
				}else {
					saveMessage(request,"Opinion not found");
		        	 log.info("Opinion not found");
				}
				
			}catch(Exception e){
				log.info("Exception Occured",e);
				saveMessage(request,"Requested Opinion not available");
			}
		
			return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
		}
	 	
	 	@RequestMapping(value="bpm/opinion/getUserOpinion", method = RequestMethod.GET)
	    public @ResponseBody List<Map<String, String>> getUserOpinion(@RequestParam("opinionWord") String opinionWord,ModelMap model,HttpServletRequest request) throws Exception {
			List<Map<String, String>> userOpinionDetailsList = new ArrayList<Map<String, String>>();
			String userId = CommonUtil.getLoggedInUserId();
	         try{
	             List<UserOpinion> userOpinionWordList = userOpinionService.getUserOpinion(opinionWord,userId);
	             if (userOpinionWordList != null){
	                 for(UserOpinion userOpinion : userOpinionWordList){
	                     Map<String,String> userOpinionDetail = new HashMap<String, String>();
	                     userOpinionDetail.put("opinionWord", userOpinion.getWord());
	                     userOpinionDetail.put("id", userOpinion.getUserId());
	                     userOpinionDetailsList.add(userOpinionDetail);
	                 }
	 				log.info("All opinion Retrived Successfuly");
	                 return userOpinionDetailsList;
	             }
	         }catch(Exception e){
	             log.error("Error while getting all opinion "+e);
	         }
	         return new ArrayList<Map<String, String>>();
	    }
	 	
	 	
	 	@RequestMapping(value="bpm/opinion/saveOpinion", method = RequestMethod.POST)
	    public @ResponseBody Opinion saveOpinion(@RequestParam("message") String message,@RequestParam("taskId") String taskId,
	    		@RequestParam("processInsId") String processInsId,@RequestParam("taskName") String taskName,@RequestParam("userFullName") String userFullName,
	    		ModelMap model,HttpServletRequest request) throws Exception {
			String userId = CommonUtil.getLoggedInUserId();
	         try{
	             Opinion opinion = new Opinion();
	             opinion.setMessage(message);
	             opinion.setTaskId(taskId);
	             opinion.setUserId(userId);
	             opinion.setProcessInstanceId(processInsId);
	             opinion.setSubmittedOn(ClockUtil.getCurrentTime());
	             opinion.setSubmitStr(DateUtil.convertDateToString(ClockUtil.getCurrentTime()));
	             opinion.setTaskName(taskName);
	             opinion.setUserFullName(userFullName);
	             userOpinionService.saveOpinion(opinion);
	 			 log.info("opinion Saved Successfuly");
	             return opinion;
	         }catch(Exception e){
	             log.error("Error while getting all opinion "+e);
	             e.printStackTrace();
	         }
	         return new Opinion();
	    }
	 	
	 	@RequestMapping(value="bpm/opinion/deleteOpinion", method = RequestMethod.GET)
	    public @ResponseBody boolean deleteOpinion(@RequestParam("opinionId") String opinionId) throws Exception {
	         try{
	        	 return userOpinionService.deleteOpinion(opinionId);
	         }catch(Exception e){
	             log.error("Error while deleting all opinion "+e);
	         }
	         return false;
	    }
	 	
	 	/**
	 	 * To get the user option words by log in user
	 	 * @param model
	 	 * @param request
	 	 * @return
	 	 * @throws Exception
	 	 */
	 	@RequestMapping(value="bpm/opinion/getUserOpinionsByUser", method = RequestMethod.GET)
	    public @ResponseBody List<String> getUserOpinionsByUser(ModelMap model,HttpServletRequest request) throws Exception {
	 		String userId = CommonUtil.getLoggedInUserId();
	         try{
	        	 return userOpinionService.getUserOpinionsByUser(userId);
	         }catch(Exception e){
	             log.error("Error while getting all opinion "+e);
	         }
	         return new ArrayList<String>();
	    }
}