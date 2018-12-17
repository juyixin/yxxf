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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.crm.model.Custom;
import com.eazytec.crm.service.CustomService;
import com.eazytec.dao.SearchException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;
import com.eazytec.webapp.controller.BaseFormController;

@Controller
public class CustomController extends BaseFormController{
	
	@Autowired
	private CustomService customService;

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private DataDictionaryService dicService;

	/**
	 * 记录列表页
	 * @return
	 */
	
	@RequestMapping(value = "bpm/crm/customs", method = RequestMethod.GET)
	public ModelAndView showCustoms(String num,String name)  {
		Model model = new ExtendedModelMap();
		try {
			List<Custom> customList = customService.getCustoms(num,name);
			model.addAttribute("customList", customList);

			String[] fieldNames = { "id", "num","source","belong","name","phone","email","address","degree","profile", "createdTimeByString" ,"createdBy.fullName"};
			String script = generateScriptForCustomGrid(CommonUtil.getMapListFromObjectListByFieldNames(customList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("queryNum", num);
			model.addAttribute("queryName", name);
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("crm/customList", model.asMap());
	}
	
	
	public static String generateScriptForCustomGrid(List<Map<String,Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','客户编号','来源','所属客户','客户名称','手机','邮箱','地址','重要程度','简介','创建时间','创建用户']";		
		context.put("title", "Groups");
		context.put("gridId", "GROUP_LIST");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "num", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "source", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "belong", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "phone", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "email", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "address", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "degree", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "profile", "100", "left", "", "false");
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
	@RequestMapping(value = "bpm/crm/customForm", method = RequestMethod.GET)
	public ModelAndView showNewCustomForm(ModelMap model) {
		
		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("sourceType");
		List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
		Custom custom = new Custom();
		model.addAttribute("custom", custom);
		model.addAttribute("dicList", dicList);
		model.addAttribute("deList", deList);
		return new ModelAndView("crm/customForm", model);
	}
		
	/**
	 * 记录保存
	 * @param request
	 * @param custom
	 * @param model
	 * @param errors
	 * @return
	 */	
	
	@RequestMapping(value = "bpm/crm/saveCustom", method = RequestMethod.POST)
		public ModelAndView saveGroup(HttpServletRequest request, @ModelAttribute("custom") Custom custom, ModelMap model, BindingResult errors) {
		validator.validate(custom, errors);

		if (errors.hasErrors()) { // don't validate when deleting
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("sourceType");
			List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
			model.addAttribute("custom", custom);
			model.addAttribute("dicList", dicList);
			model.addAttribute("deList", deList);
			return new ModelAndView("crm/customForm", model);
		}
		
		if(StringUtils.isBlank(custom.getId())){
			Date date = new Date();
			custom.setCreatedTime(date);
		}
		
		User user=CommonUtil.getLoggedInUser();
		custom.setCreatedBy(user);
		    
		customService.saveCustom(custom);
		return new ModelAndView("redirect:customs");
	}
		
	/**
	 * 记录删除
	 * @param customIds
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "bpm/crm/deleteSelectedCustom", method = RequestMethod.GET)
		public ModelAndView deleteSelectedCustoms(@RequestParam("customIds") String customIds, HttpServletRequest request) {
			
			List<String> customIdList = new ArrayList<String>();
			if (customIds.contains(",")) {
				String[] ids = customIds.split(",");
				for (String id : ids) {
					customIdList.add(id);
				}
			} else {
				customIdList.add(customIds);
			}

			customService.removeCustoms(customIdList);
			return new ModelAndView("redirect:customs");
		}
	
	/**
	 * 记录编辑
	 * @param id
	 * @param model
	 * @return
	 
	@RequestMapping(value = "bpm/crm/editCustom",method = RequestMethod.GET)
    public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model){
		
		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("sourceType");
		List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
	  	Custom custom = customService.getCustomById(id);
		model.put("custom", custom);
		model.addAttribute("dicList", dicList);
		model.addAttribute("deList", deList);
        return new ModelAndView("crm/customForm", model);   
    }
	*/
	// ========================================================================================================================================

		/**
		 * 记录列表页（分页）
		 * 
		 * @param num
		 * @return
		 */
		@RequestMapping(value = "bpm/crm/customs2", method = RequestMethod.GET)
		public ModelAndView showCustoms2(String num, ModelMap model) {
			Map<String, Object> context = new HashMap<String, Object>();
			List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
			String columnNames = "['Id','客户编号','来源','所属客户','客户名称','手机','邮箱','地址','重要程度','简介','创建时间','创建用户']";
			context.put("title", "Groups");
			context.put("gridId", "GROUP_LIST");
			context.put("needCheckbox", true);
			context.put("reurl", "bpm/crm/customToString");
			context.put("columnNames", columnNames);
			CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
			CommonUtil.createFieldNameList(fieldNameList, "num", "100", "left", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "source", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "belong", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "phone", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "email", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "address", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "degree", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "profile", "100", "left", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "createdBy.fullName", "100", "center", "", "false");

			context.put("fieldNameList", fieldNameList);
			String script = GridUtil.generateScriptDy(velocityEngine, context);
			model.addAttribute("script", script);
			return new ModelAndView("crm/myList", model);
		}
		
		/**
		 * 记录编辑
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "bpm/crm/editCustom",method = RequestMethod.GET)
	    public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model){
			
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("sourceType");
			List<DataDictionary> deList = dicService.getDataDictionaryByParentCode("degreeType");
		  	Custom custom = customService.getCustomById(id);
			model.put("custom", custom);
			model.addAttribute("dicList", dicList);
			model.addAttribute("deList", deList);
	        return new ModelAndView("crm/customForm", model);   
	    }
		
		@RequestMapping(value = "bpm/crm/customToString", method = RequestMethod.GET)
		public @ResponseBody String recordToString(HttpServletRequest request, Integer page, Integer rows,  ModelMap model) {
			PageBean<Custom> pageBean = new PageBean<Custom>(page, rows);
			pageBean = customService.getCustoms(pageBean);
			String[] fieldNames = { "id", "num","source","belong","name","phone","email","address","degree","profile", "createdTimeByString" ,"createdBy.fullName"};
			PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
			String data = CommonUtil.convertBeanToJsonString(pageResultBean);
			return data;
		}

}


