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
import com.eazytec.crm.model.Record;
import com.eazytec.crm.service.RecordService;
import com.eazytec.dao.SearchException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;
import com.eazytec.webapp.controller.BaseFormController;

@Controller
public class RecordController extends BaseFormController {

	@Autowired
	private RecordService recordService;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private DataDictionaryService dicService;

	/**
	 * 记录列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/records", method = RequestMethod.GET)
	public ModelAndView showRecords(String num) {
		Model model = new ExtendedModelMap();
		try {
			List<Record> recordList = recordService.getRecords(num);
			model.addAttribute("recordList", recordList);

			String[] fieldNames = { "id", "num", "type", "startTimeByString", "endTimeByString", "note", "createdTimeByString", "createdBy.fullName" };
			String script = generateScriptForRecordGrid(CommonUtil.getMapListFromObjectListByFieldNames(recordList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("queryNum", num);
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("crm/recordList", model.asMap());
	}

	public static String generateScriptForRecordGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','编号', '记录类型', '开始时间', '结束时间', '备注', '创建时间', '创建人']";
		context.put("title", "Groups");
		context.put("gridId", "GROUP_LIST");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "num", "100", "center", "_showEditRecord", "false");
		CommonUtil.createFieldNameList(fieldNameList, "type", "100", "center", "", "false");
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
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/recordForm", method = RequestMethod.GET)
	public ModelAndView showNewRecordForm(ModelMap model) {

		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("recordType");

		Record record = new Record();
		model.addAttribute("record", record);
		model.addAttribute("dicList", dicList);
		return new ModelAndView("crm/recordForm", model);
	}

	/**
	 * 记录保存
	 * 
	 * @param request
	 * @param record
	 * @param model
	 * @param errors
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/saveRecord", method = RequestMethod.POST)
	public ModelAndView saveGroup(HttpServletRequest request, @ModelAttribute("record") Record record, ModelMap model, BindingResult errors) {

		validator.validate(record, errors);

		if (errors.hasErrors()) { // don't validate when deleting
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("recordType");
			model.addAttribute("record", record);
			model.addAttribute("dicList", dicList);
			return new ModelAndView("crm/recordForm", model);
		}
		
		User user = CommonUtil.getLoggedInUser();
		record.setCreatedBy(user);
		record.init();
		
		recordService.saveRecord(record);
		return new ModelAndView("redirect:records");
	}

	/**
	 * 记录删除
	 * 
	 * @param recordIds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/deleteSelectedRecord", method = RequestMethod.GET)
	public ModelAndView deleteSelectedRecords(@RequestParam("recordIds") String recordIds, HttpServletRequest request) {

		List<String> recordIdList = new ArrayList<String>();
		if (recordIds.contains(",")) {
			String[] ids = recordIds.split(",");
			for (String id : ids) {
				recordIdList.add(id);
			}
		} else {
			recordIdList.add(recordIds);
		}

		recordService.removeRecords(recordIdList);
		return new ModelAndView("redirect:records");
	}

	/**
	 * 记录编辑
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/editRecord", method = RequestMethod.GET)
	public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model) {

		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("recordType");

		Record record = recordService.getRecordById(id);
		model.put("record", record);
		model.addAttribute("dicList", dicList);
		return new ModelAndView("crm/recordForm", model);
	}

	// ========================================================================================================================================

	/**
	 * 记录列表页（分页）
	 * 
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/records2", method = RequestMethod.GET)
	public ModelAndView showRecords2(String num, ModelMap model) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','编号', '记录类型', '开始时间', '结束时间', '备注', '创建时间', '创建人']";
		context.put("title", "Groups");
		context.put("gridId", "RECORD_LIST");
		context.put("needCheckbox", true);
		context.put("reurl", "bpm/crm/recordToString");
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "num", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "type", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTimeByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTimeByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "note", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdBy.fullName", "100", "center", "", "false");

		context.put("fieldNameList", fieldNameList);
		String script = GridUtil.generateScriptDy(velocityEngine, context);
		model.addAttribute("script", script);
		return new ModelAndView("crm/recordList2", model);
	}

	@RequestMapping(value = "bpm/crm/recordToString", method = RequestMethod.GET)
	public @ResponseBody String recordToString(String num, Integer page, Integer rows, String orderName, String orderType, ModelMap model, HttpServletRequest request) {
		PageBean<Record> pageBean = new PageBean<Record>(page, rows);
		pageBean = recordService.getRecords(pageBean,num,orderName,orderType);
		String[] fieldNames = { "id", "num", "type", "startTimeByString", "endTimeByString", "note", "createdTimeByString", "createdBy.fullName" };
		PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
		String data = CommonUtil.convertBeanToJsonString(pageResultBean);
		return data;
	}

}