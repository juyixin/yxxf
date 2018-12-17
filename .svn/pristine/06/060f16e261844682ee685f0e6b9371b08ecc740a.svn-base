package com.eazytec.generalProcess.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.generalProcess.model.GeneralProcess;
import com.eazytec.generalProcess.service.GeneralProcessService;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.sendDocuments.service.SendDocumentsService;
import com.eazytec.service.UserService;
import com.eazytec.vacate.model.Vacate;
import com.eazytec.vacate.service.VacateService;

@Controller
public class GeneralProcessController {
	
	@Autowired
	private  VacateService  vacateService;
	
	@Autowired
	private  GeneralProcessService  generalProcessService;
	
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
	@RequestMapping(value = "bpm/generalProcess/generalProcessList", method = RequestMethod.GET)
	public ModelAndView generalProcessList(HttpServletRequest request, ModelMap model) {
          String script = null;
          try{
        	String name = request.getParameter("name");
        	String state = request.getParameter("state");
        	List<GeneralProcess> linkList = new ArrayList<GeneralProcess>();
 
        	linkList = generalProcessService.searchGeneralProcess(name,state);
        	
			if(!linkList.isEmpty()&&linkList.size()!=0){
				for(GeneralProcess s : linkList){
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
					dcl = vacateService.getNameList(s.getState());
					s.setStateName(dcl.getName());
				}
				
			}
			DataDictionary dcl = new DataDictionary();
			dcl.setCode("200");
			dcl.setName("---请选择审核状态---");
			List<DataDictionary> list2 = new ArrayList<DataDictionary>();
			list2 = dataDictionaryService.getWgtlList("SHZT");
			list2.add(0, dcl);
			model.addAttribute("list2", list2);
			String[] fieldNames = { "id","itemName", "itemDescription","recipientName", "copyPeopleName","senderName","stateName","createTime"};
			script = generateScriptForGeneralProcessGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("generalProcess/generalProcessList", model);
	}
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/generalProcess/generalProcessGrridList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String generalProcessGrridList( HttpServletRequest request, ModelMap model) {
		 String script = null;
         try{
       	String name = request.getParameter("name");
       	String state = request.getParameter("state");
       	List<GeneralProcess> linkList = new ArrayList<GeneralProcess>();

       	linkList = generalProcessService.searchGeneralProcess(name,state);
       	
			if(!linkList.isEmpty()&&linkList.size()!=0){
				for(GeneralProcess s : linkList){
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
					dcl = vacateService.getNameList(s.getState());
					s.setStateName(dcl.getName());
				}
				
			}
			
			String[] fieldNames = { "id","itemName", "itemDescription","recipientName", "copyPeopleName","senderName","stateName","createTime"};
			script = generateScriptForGeneralProcessGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
         } catch (Exception e) {
             log.error(e.getMessage(), e);
   	}
		    return script;
	}
	
	public static String generateScriptForGeneralProcessGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','事项名称','事项描述','审批人','抄送人','申请人','审核状态','发送时间']";
		context.put("title", "GeneralProcess");
		context.put("gridId", "GENERALPROCESS_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "itemName", "30", "center", "_showGeneralProcessServed", "false");
		CommonUtil.createFieldNameList(fieldNameList, "itemDescription", "30", "center", "", "true");
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
	@RequestMapping(value = "bpm/generalProcess/viewDetails", method = RequestMethod.GET)
	public ModelAndView viewDetails(String id, HttpServletRequest request, ModelMap model) {
		GeneralProcess s = new GeneralProcess();
		s = generalProcessService.getDetail(id);
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
		dcl = vacateService.getNameList(s.getState());
		s.setStateName(dcl.getName());
		
		String senderName = "";
		if(s.getOriginator()!=null){
			senderName = s.getOriginator().getFirstName();
			s.setSenderName(senderName);
		}
		
		
		List<FileManage> list = fileManageService.getEvidence(id);
		model.addAttribute("list", list);
		model.addAttribute("generalProcess", s);
		return new ModelAndView("generalProcess/modifyGeneralProcess", model);
	}

}
