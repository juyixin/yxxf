package com.eazytec.crm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.crm.model.Telreg;
import com.eazytec.crm.service.TelregService;
import com.eazytec.dao.SearchException;
import com.eazytec.webapp.controller.BaseFormController;


@Controller
public class TelregController extends BaseFormController{
	@Autowired
	private TelregService  telregService;

	@Autowired
	public VelocityEngine velocityEngine;

	@Autowired
	private DataDictionaryService dicService;
	
	@RequestMapping(value = "bpm/crm/telregs", method = RequestMethod.GET)
	public ModelAndView showTelregs(String num,String telnum)  {
		Model model = new ExtendedModelMap();
		try {
			List<Telreg> telregList = telregService.getTelregs(num,telnum);
			model.addAttribute("telregList", telregList);

			String[] fieldNames = { "id", "num","conDet","telnum","theme","startTimeByString","endTimeByString","note", "createdTimeByString","createdBy.fullName" };
			String script = generateScriptForTelregGrid(CommonUtil.getMapListFromObjectListByFieldNames(telregList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("queryNum", num);
			model.addAttribute("queryTelnum", telnum);
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("crm/telregList", model.asMap());
	}
	
	
	public static String generateScriptForTelregGrid(List<Map<String,Object>> list, VelocityEngine velocityEngine){
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','编号','咨询内容','来电手机号','主题','开始时间','结束时间','备注','创建时间','创建用户']";		
		context.put("title", "Groups");
		context.put("gridId", "GROUP_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "num", "100", "center", "_showEditTelreg", "false");
		CommonUtil.createFieldNameList(fieldNameList, "conDet", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "telnum", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "theme", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTimeByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTimeByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "note", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdBy.fullName", "100", "center", "", "false");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	/**
	 * 记录新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/telregForm", method = RequestMethod.GET)
	public ModelAndView showNewTelregForm(ModelMap model) {
		Telreg telreg = new Telreg();
		model.addAttribute("telreg", telreg);
		return new ModelAndView("crm/telregForm", model);
	}
	/**
	 * 记录保存
	 * @param request
	 * @param record
	 * @param model
	 * @param errors
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/saveTelreg", method = RequestMethod.POST)
	public ModelAndView saveGroup(HttpServletRequest request, @ModelAttribute("telreg") Telreg telreg, ModelMap model, BindingResult errors) {
	
		validator.validate(telreg, errors);

		if (errors.hasErrors()) { // don't validate when deleting
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("telregType");
			model.addAttribute("telreg", telreg);
			model.addAttribute("dicList", dicList);
			return new ModelAndView("crm/telregForm", model);
		}
		
		if(StringUtils.isBlank(telreg.getId())){
			Date date = new Date();
			telreg.setCreatedTime(date);
		}
		
		User user=CommonUtil.getLoggedInUser();
		telreg.setCreatedBy(user);
		telregService.saveTelreg(telreg);
		return new ModelAndView("redirect:telregs");
	}
	/**
	 * 记录删除
	 * @param recordIds
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "bpm/crm/deleteSelectedTelreg", method = RequestMethod.GET)
	public ModelAndView deleteSelectedTelregs(@RequestParam("telregIds") String telregIds, HttpServletRequest request) {
		
		List<String> telregIdList = new ArrayList<String>();
		if (telregIds.contains(",")) {
			String[] ids = telregIds.split(",");
			for (String id : ids) {
				telregIdList.add(id);
			}
		} else {
			telregIdList.add(telregIds);
		}

		telregService.removeTelregs(telregIdList);
		return new ModelAndView("redirect:telregs");
	}
	
	/**
	 * 记录编辑
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/editTelreg",method = RequestMethod.GET)
    public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model){
		
		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("telregType");
		
	  	Telreg telreg = telregService.getTelregById(id);
		model.put("telreg", telreg);
		model.addAttribute("dicList", dicList);
        return new ModelAndView("crm/telregForm", model);   
    }
}

