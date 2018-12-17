package com.eazytec.sendDocuments.controller;

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

@Controller
public class SendDocumentsController {
	
	@Autowired
	private  SendDocumentsService  sendDocumentsService;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private FileManageService fileManageService;
	
	@Autowired
	private UserService userService;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/sendDocuments/sendDocumentsList", method = RequestMethod.GET)
	public ModelAndView sendDocumentsList(HttpServletRequest request, ModelMap model) {
          String script = null;
          try{
        	String name = request.getParameter("searchText");
        	List<SendDocuments> linkList = new ArrayList<SendDocuments>();
        	if(name!=null&&name!=""){
        		name=java.net.URLDecoder.decode(name, "utf-8");
        		linkList = sendDocumentsService.searchSendDocuments(name);
        	}else{
        		linkList = sendDocumentsService.getSendDocuments();
        	}
			if(!linkList.isEmpty()&&linkList.size()!=0){
				for(SendDocuments s : linkList){
					User u = new User();
					if(s.getCopyPeople()!=null){
						String [] people = s.getCopyPeople().split(",");
						String jc = "";
						for(String t : people){
							u = userService.getUser(t);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.substring(0,jc.length()-1);	
						s.setCopyPeopleName(jc);
					}
					
					if(s.getRecipient()!=null){
						String [] people = s.getRecipient().split(",");
						String jc = "";
						for(String t : people){
							u = userService.getUser(t);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.substring(0,jc.length()-1);	
						s.setRecipientName(jc);
					}
					
				
					
					s.setSenderName(s.getSender().getFirstName());
				}
				
			}
			String[] fieldNames = { "id","theme", "content", "recipientName", "copyPeopleName","senderName","sendTime"};
			script = generateScriptForSendDocumentsGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("send/sendList", model);
	}
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/sendDocuments/sendDocumentsGridList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String sendDocumentsGridList( HttpServletRequest request, ModelMap model) {
		  String script = null;
          try{
        	String name = request.getParameter("searchText");
        	List<SendDocuments> linkList = new ArrayList<SendDocuments>();
        	if(name!=null&&name!=""){
        		name=java.net.URLDecoder.decode(name, "utf-8");
        		linkList = sendDocumentsService.searchSendDocuments(name);
        	}else{
        		linkList = sendDocumentsService.getSendDocuments();
        	}
			if(!linkList.isEmpty()&&linkList.size()!=0){
				for(SendDocuments s : linkList){
					User u = new User();
					if(s.getCopyPeople()!=null){
						String [] people = s.getCopyPeople().split(",");
						String jc = "";
						for(String t : people){
							u = userService.getUser(t);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.substring(0,jc.length()-1);	
						s.setCopyPeopleName(jc);
					}
					
					if(s.getRecipient()!=null){
						String [] people = s.getRecipient().split(",");
						String jc = "";
						for(String t : people){
							u = userService.getUser(t);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.substring(0,jc.length()-1);	
						s.setRecipientName(jc);
					}
					
				
					
					s.setSenderName(s.getSender().getFirstName());
				}
				
			}
			String[] fieldNames = { "id","theme", "content", "recipientName", "copyPeopleName","senderName","sendTime"};
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
		String columnNames = "['Id','主题','内容','收件人','抄送人','发件人','发送时间']";
		context.put("title", "SendDocuments");
		context.put("gridId", "SENDDOCUMENTS_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "theme", "30", "center", "_showEditDocumentsServed", "false");
		CommonUtil.createFieldNameList(fieldNameList, "content", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "recipientName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "copyPeopleName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "senderName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sendTime", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
     * Description:打开查看页面
     * 作者 : 蒋晨 
     * 时间 : 2017-4-17 上午10:15:25
     */
	@RequestMapping(value = "bpm/sendDocuments/viewDetails", method = RequestMethod.GET)
	public ModelAndView viewDetails(String id, HttpServletRequest request, ModelMap model) {
		SendDocuments sendDocuments = new SendDocuments();
		sendDocuments = sendDocumentsService.getDetail(id);
		User u = new User();
		String jc = "";
		if(sendDocuments.getCopyPeople()!=null&&sendDocuments.getCopyPeople()!=""){
			String [] people = sendDocuments.getCopyPeople().split(",");
			for(String t : people){
				u = userService.getUser(t);
				jc= jc+u.getFirstName()+",";
			}
			jc = jc.substring(0,jc.length()-1);	
			sendDocuments.setCopyPeopleName(jc);
		}
			
		
		String recipientName = "";
		String c = "";
		if(sendDocuments.getRecipient()!=null&&sendDocuments.getRecipient()!=""){
			
			String [] people = sendDocuments.getRecipient().split(",");
			for(String t : people){
				u = userService.getUser(t);
				c= c+u.getFirstName()+",";
			}
			c = c.substring(0,c.length()-1);	
			recipientName = c;
		}
		model.addAttribute("recipientName", recipientName);
		
		String senderName = "";
		if(sendDocuments.getSender()!=null){
			senderName = sendDocuments.getSender().getFirstName();
		}
		model.addAttribute("senderName", senderName);
		
		List<FileManage> list = fileManageService.getEvidence(id);
		model.addAttribute("list", list);
		model.addAttribute("sendDocuments", sendDocuments);
		return new ModelAndView("send/modifySend", model);
	}

}
