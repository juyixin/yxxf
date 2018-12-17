package com.eazytec.crm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.eazytec.crm.model.Chance;
import com.eazytec.crm.service.ChanceService;
import com.eazytec.dao.SearchException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.webapp.controller.BaseFormController;



@Controller
public class ChanceController extends BaseFormController{
	@Autowired
	private ChanceService  chanceService;

	@Autowired
	public VelocityEngine velocityEngine;
	
	@Autowired
	private DataDictionaryService dicService;
	
	
	/**
	 * 记录列表页
	 * @return
	 */

	@RequestMapping(value = "bpm/crm/chances", method = RequestMethod.GET)
	public ModelAndView showChances(String customerName) {
		Model model = new ExtendedModelMap();
		try {
			List<Chance> chanceList = chanceService.getChances(customerName);
			model.addAttribute("chanceList", chanceList);

			String[] fieldNames = { "id","customerName","salesStage","importance","estimatedSales","success","detail","analysis","salesStrategy","createdTimeByString","createdBy.fullName" };
			String script = generateScriptForChanceGrid(CommonUtil.getMapListFromObjectListByFieldNames(chanceList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("querycustomerName", customerName);
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("crm/chanceList", model.asMap());
	}
	
	
	public static String generateScriptForChanceGrid(List<Map<String,Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','客户姓名','销售阶段','重要程度','预计销售额','成功可能性','细节','分析','销售策略','创建时间','创建用户']";		
		context.put("title", "Groups");
		context.put("gridId", "GROUP_LIST");
		context.put("noOfChances", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "customerName", "100", "left", "_showEditChance", "false");
		CommonUtil.createFieldNameList(fieldNameList, "salesStage", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "importance", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "estimatedSales", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "success", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "detail", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "analysis", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "salesStrategy", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdBy.fullName", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	/**
	 * 记录新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/chanceForm", method = RequestMethod.GET)
	public ModelAndView showNewChanceForm(ModelMap model) {
		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("salesStage");
		List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
		Chance chance = new Chance();
		model.addAttribute("chance", chance);
		model.addAttribute("dicList", dicList);
		model.addAttribute("deList", deList);
		return new ModelAndView("crm/chanceForm", model);
	}
	
	/**
	 * 记录保存
	 * @param request
	 * @param record
	 * @param model
	 * @param errors
	 * @return
	 */	
	@RequestMapping(value = "bpm/crm/saveChance", method = RequestMethod.POST)
	public ModelAndView saveGroup(HttpServletRequest request, @ModelAttribute("chance") Chance chance, ModelMap model, BindingResult errors) {
		validator.validate(chance, errors);
		
		if (errors.hasErrors()) { // don't validate when deleting
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("salesStage");
			List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
			model.addAttribute("chance", chance);
			model.addAttribute("dicList", dicList);
			model.addAttribute("deList", deList);
			return new ModelAndView("crm/chanceForm", model);
		}
		if(StringUtils.isBlank(chance.getId())){
			Date date = new Date();
			chance.setCreatedTime(date);
		}
		
		User user=CommonUtil.getLoggedInUser();
		chance.setCreatedBy(user);
		
		chanceService.saveChance(chance);
		return new ModelAndView("redirect:chances");
	}
	
	/**
	 * 记录删除
	 * @param recordIds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/deleteSelectedChance", method = RequestMethod.GET)
	public ModelAndView deleteSelectedChance(@RequestParam("chanceIds") String chanceIds, HttpServletRequest request) {
		
		List<String> chanceIdList = new ArrayList<String>();
		if (chanceIds.contains(",")) {
			String[] ids = chanceIds.split(",");
			for (String id : ids) {
				chanceIdList.add(id);
			}
		} else {
			chanceIdList.add(chanceIds);
		}

		chanceService.removeChances(chanceIdList);
		return new ModelAndView("redirect:chances");
	}
	
	/**
	 * 记录编辑
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/editChance",method = RequestMethod.GET)
    public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model){
		
		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("salesStage");
		List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
	  	Chance chance = chanceService.getChanceById(id);
		model.put("chance", chance);
		model.addAttribute("dicList", dicList);
		model.addAttribute("deList", deList);
        return new ModelAndView("crm/chanceForm", model);   
    }
}

