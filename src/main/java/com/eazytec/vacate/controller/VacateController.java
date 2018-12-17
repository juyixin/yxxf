package com.eazytec.vacate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.sendDocuments.model.SendDocuments;
import com.eazytec.sendDocuments.service.SendDocumentsService;
import com.eazytec.service.UserService;
import com.eazytec.vacate.model.Vacate;
import com.eazytec.vacate.service.VacateService;

@Controller
public class VacateController {
	
	@Autowired
	private  VacateService  vacateService;
	
	@Autowired
	private  SendDocumentsService  sendDocumentsService;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private FileManageService fileManageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/vacate/vacateList", method = RequestMethod.GET)
	public ModelAndView vacateList(HttpServletRequest request, ModelMap model) {
          String script = null;
          try{
        	String type = request.getParameter("type");
        	String state = request.getParameter("state");
        	List<Vacate> linkList = new ArrayList<Vacate>();
 
        	linkList = vacateService.searchVacate(type,state);
        	
			if(!linkList.isEmpty()&&linkList.size()!=0){
				for(Vacate s : linkList){
					User u = new User();
					if(s.getCopyPeople()!=null){
						String [] people = s.getCopyPeople().split(",");
						String jc = "";
						for(String t : people){
							u = userService.getUserById(t);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.substring(0,jc.length()-1);	
						s.setCopyPeopleName(jc);
					}
					
					if(s.getApprover()!=null){
						u = userService.getUserById(s.getApprover());
						s.setRecipientName(u.getFirstName());
					}
					
					s.setSenderName(s.getOriginator().getFirstName());
					
					DataDictionary dcl = new DataDictionary();
					dcl = vacateService.getNameList(s.getVacateType());
					s.setVacateTypeName(dcl.getName());
					dcl = vacateService.getNameList(s.getState());
					s.setStateName(dcl.getName());
				}
				
			}
			DataDictionary dcl = new DataDictionary();
			dcl.setCode("200");
			dcl.setName("---请选择请假类型---");
			List<DataDictionary> list = new ArrayList<DataDictionary>();
			list = dataDictionaryService.getWgtlList("QJLX");
			list.add(0, dcl);
			model.addAttribute("list", list);
			List<DataDictionary> list2 = new ArrayList<DataDictionary>();
			list2 = dataDictionaryService.getWgtlList("SHZT");
			DataDictionary dcl1 = new DataDictionary();
			dcl1.setCode("200");
			dcl1.setName("---请选择审核状态---");
			list2.add(0, dcl1);
			model.addAttribute("list2", list2);
			String[] fieldNames = { "id","vacateTypeName", "startTime", "endTime", "vacateDay", "recipientName", "copyPeopleName","senderName","stateName","createTime"};
			script = generateScriptForSendDocumentsGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("vacate/vacateList", model);
	}
	
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/vacate/vacateGrridList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String vacateGrridList( HttpServletRequest request, ModelMap model) {
		 String script = null;
         try{
       	String type = request.getParameter("type");
       	String state = request.getParameter("state");
       	List<Vacate> linkList = new ArrayList<Vacate>();

       	linkList = vacateService.searchVacate(type,state);
       	
			if(!linkList.isEmpty()&&linkList.size()!=0){
				for(Vacate s : linkList){
					User u = new User();
					if(s.getCopyPeople()!=null){
						String [] people = s.getCopyPeople().split(",");
						String jc = "";
						for(String t : people){
							u = userService.getUserById(t);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.substring(0,jc.length()-1);	
						s.setCopyPeopleName(jc);
					}
					
					if(s.getApprover()!=null){
						u = userService.getUserById(s.getApprover());
						s.setRecipientName(u.getFirstName());
					}
					
					s.setSenderName(s.getOriginator().getFirstName());
					
					DataDictionary dcl = new DataDictionary();
					dcl = vacateService.getNameList(s.getVacateType());
					s.setVacateTypeName(dcl.getName());
					dcl = vacateService.getNameList(s.getState());
					s.setStateName(dcl.getName());
				}
				
			}
			
			String[] fieldNames = { "id","vacateTypeName", "startTime", "endTime", "vacateDay", "recipientName", "copyPeopleName","senderName","stateName","createTime"};
			script = generateScriptForSendDocumentsGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
         } catch (Exception e) {
             log.error(e.getMessage(), e);
   	}
		    return script;
	}
	
	
	public static String generateScriptForSendDocumentsGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','请假类型','开始时间','结束时间','请假天数','审批人','抄送人','申请人','审核状态','发送时间']";
		context.put("title", "Vacates");
		context.put("gridId", "VACATE_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "vacateTypeName", "30", "center", "_showVacateServed", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "vacateDay", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "recipientName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "copyPeopleName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "senderName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "stateName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
     * Description:打开查看页面
     * 作者 : 蒋晨 
     * 时间 : 2017-4-17 上午10:15:25
     */
	@RequestMapping(value = "bpm/vacate/viewDetails", method = RequestMethod.GET)
	public ModelAndView viewDetails(String id, HttpServletRequest request, ModelMap model) {
		Vacate s = new Vacate();
		s = vacateService.getDetail(id);
		User u = new User();
		String jc = "";
		if(s.getCopyPeople()!=null&&s.getCopyPeople()!=""){
			String [] people = s.getCopyPeople().split(",");
			for(String t : people){
				u = userService.getUserById(t);
				jc= jc+u.getFirstName()+",";
			}
			jc = jc.substring(0,jc.length()-1);	
			s.setCopyPeopleName(jc);
		}
			
		
		String recipientName = "";
		if(s.getApprover()!=null){
			u = userService.getUserById(s.getApprover());
			recipientName = u.getFirstName();
			s.setRecipientName(recipientName);
		}
		
		
		DataDictionary dcl = new DataDictionary();
		dcl = vacateService.getNameList(s.getVacateType());
		s.setVacateTypeName(dcl.getName());
		dcl = vacateService.getNameList(s.getState());
		s.setStateName(dcl.getName());
		
		String senderName = "";
		if(s.getOriginator()!=null){
			senderName = s.getOriginator().getFirstName();
			s.setSenderName(senderName);
		}
		
		
		List<FileManage> list = fileManageService.getEvidence(id);
		model.addAttribute("list", list);
		model.addAttribute("vacate", s);
		return new ModelAndView("vacate/modifyVacate", model);
	}
	
	
	
	
	/**
     * Description:请假统计
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/vacate/vacateCount", method = RequestMethod.GET)
	public ModelAndView vacateCount(HttpServletRequest request, ModelMap model) {
          String script = null;
          try{
        	  
        	String startTime = request.getParameter("startTime");
        	
        	String endTime = request.getParameter("endTime");
        	
        	List<Vacate> linkList = new ArrayList<Vacate>();
 
        	linkList = vacateService.getVacate(startTime, endTime);
        	
       

			if(!linkList.isEmpty()&&linkList.size()!=0){
				//int count = 0 ;
				for(Vacate s : linkList){
					//count = vacateService.getCount(s.getOriginator().getId(), startTime,endTime);
					s.setSearchStartTime(startTime);
					s.setEndTime(endTime);
				}
				
			}
			
			String[] fieldNames = { "name","recipientName", "count","total","searchStartTime", "searchEndTime"};
			script = generateScriptVacateGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("vacate/vacateCountList", model);
	}
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/vacate/getUnitUnionGrid", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getUnitUnionGrid(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, HttpServletRequest request, ModelMap model) {
		 String script = null;
         try{
       	  
       	
       	List<Vacate> linkList = new ArrayList<Vacate>();

       	linkList = vacateService.getVacate(startTime, endTime);
       	
       	if(startTime==""){
       		startTime=null;
       	}
        if(endTime==""){
        	endTime=null;
       	}
       	if(!linkList.isEmpty()&&linkList.size()!=0){
			//int count = 0 ;
			for(Vacate s : linkList){
				//count = vacateService.getCount(s.getOriginator().getId(), startTime,endTime);
				s.setSearchStartTime(startTime);
				s.setSearchEndTime(endTime);
			}
			
		}
		
		String[] fieldNames = { "name","recipientName", "count","total","searchStartTime", "searchEndTime"};
		script = generateScriptVacateGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
		model.addAttribute("script", script);
      } catch (Exception e) {
          log.error(e.getMessage(), e);
	}
		    return script;
	}
	
	public static String generateScriptVacateGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['编号','姓名','请假总次数','请假总天数','搜索开始时间','搜索结束时间']";
		context.put("title", "VacateCounts");
		context.put("gridId", "VACATECOUNT_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "name", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "recipientName", "30", "center", "_showVacateDetailServed", "false");
		CommonUtil.createFieldNameList(fieldNameList, "count", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "total", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "searchStartTime", "30", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "searchEndTime", "30", "center", "", "true");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
     * Description:请假统计详情
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/vacate/vacateCountDetail", method = RequestMethod.GET)
	public ModelAndView vacateCountDetail(HttpServletRequest request, ModelMap model) {
          String script = null;
          try{
        	
        	String code = request.getParameter("id");
        	
        	String [] array = code.split(",");
        	
        	String id = array[0];
        	
        	String startTime = array[1];
        	
        	String endTime = array[2];
        	
        	if(endTime.equals("null")){
        		endTime = "";
        	}
        	
        	if(startTime.equals("null")){
        		startTime = "";
        	}
        	
        	List<Vacate> linkList = new ArrayList<Vacate>();
 
        	linkList = vacateService.getVacateList(id,startTime,endTime);
        	
			if(!linkList.isEmpty()&&linkList.size()!=0){
			
				for(Vacate s : linkList){
					DataDictionary dcl = new DataDictionary();
					dcl = vacateService.getNameList(s.getVacateType());
					s.setVacateTypeName(dcl.getName());
				}
				
			}
			
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			
			String[] fieldNames = { "vacateTypeName","startTime", "endTime", "vacateDay"};
			script = generateScriptVacateDetailGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("vacate/vacateCountDetail", model);
	}
	
	
	public static String generateScriptVacateDetailGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['请假类型','开始时间','结束时间','请假天数']";
		context.put("title", "VacateCountDetails");
		context.put("gridId", "VACATECOUNTDETAIL_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "vacateTypeName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "vacateDay", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

}
