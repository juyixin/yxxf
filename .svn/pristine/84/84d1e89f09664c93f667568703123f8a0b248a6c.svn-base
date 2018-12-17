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
import com.eazytec.crm.model.Contract;
import com.eazytec.crm.model.Record;
import com.eazytec.crm.service.ContractService;
import com.eazytec.dao.SearchException;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;
import com.eazytec.webapp.controller.BaseFormController;

@Controller
public class ContractController extends BaseFormController {

	@Autowired
	private ContractService contractService;

	@Autowired
	public VelocityEngine velocityEngine;
	
	@Autowired
	private DataDictionaryService dicService;

	/**
	 * 记录列表页
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/contracts", method = RequestMethod.GET)
	public ModelAndView showContracts(String title){
		Model model = new ExtendedModelMap();
		try {
			List<Contract> contractList = contractService.getContracts(title);
			model.addAttribute("contractList", contractList);

			String[] fieldNames = { "id", "szlx", "type", "title", "num", "contractor", "signingTimeByString", "amount", "content", "createdTimeByString", "createdBy.fullName" };
			String script = generateScriptForContractGrid(CommonUtil.getMapListFromObjectListByFieldNames(contractList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("queryTitle", title);
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("crm/contractList", model.asMap());
	}
	
	
	public static String generateScriptForContractGrid(List<Map<String,Object>> list, VelocityEngine velocityEngine) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','收支类型','合同类型','合同名称','合同编号','客户签约人','签约时间','合同金额','合同内容','创建时间','创建用户']";		
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
		CommonUtil.createFieldNameList(fieldNameList, "szlx", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "type", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "num", "100", "center", "_showEditContract", "false");
		CommonUtil.createFieldNameList(fieldNameList, "contractor", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "signingTimeByString", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "amount", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "content", "100", "left", "", "false");
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
	@RequestMapping(value = "bpm/crm/contractForm", method = RequestMethod.GET)
	public ModelAndView showNewContractForm(ModelMap model) {
		
		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("contractType");
		
		Contract contract = new Contract();
		model.addAttribute("contract", contract);
		model.addAttribute("dicList", dicList);
		return new ModelAndView("crm/contractForm", model);
	}
	
	/**
	 * 记录保存
	 * @param request
	 * @param contract
	 * @param model
	 * @param errors
	 * @return
	 */
	@RequestMapping(value = "bpm/crm/saveContract", method = RequestMethod.POST)
	public ModelAndView saveGroup(HttpServletRequest request, @ModelAttribute("contract") Contract contract, ModelMap model, BindingResult errors) {
		
		validator.validate(contract, errors);
		
		if (errors.hasErrors()) { // don't validate when deleting
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("contractType");
			model.addAttribute("contract", contract);
			model.addAttribute("dicList", dicList);
			return new ModelAndView("crm/contractForm", model);
		}
		
		if(StringUtils.isBlank(contract.getId())){
			Date date = new Date();
			contract.setCreatedTime(date);
		}
		
		User user=CommonUtil.getLoggedInUser();
		contract.setCreatedBy(user);
		
		contractService.saveContract(contract);
		return new ModelAndView("redirect:contracts");
	}
	
	/**
	 * 记录删除
	 * @param contractIds
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "bpm/crm/deleteSelectedContract", method = RequestMethod.GET)
	public ModelAndView deleteSelectedContracts(@RequestParam("contractIds") String contractIds, HttpServletRequest request) {
		
		List<String> contractIdList = new ArrayList<String>();
		if (contractIds.contains(",")) {
			String[] ids = contractIds.split(",");
			for (String id : ids) {
				contractIdList.add(id);
			}
		} else {
			contractIdList.add(contractIds);
		}

		contractService.removeContracts(contractIdList);
		return new ModelAndView("redirect:contracts");
	}

/**
 * 记录编辑
 * @param id
 * @param model
 * @return
 */
@RequestMapping(value = "bpm/crm/editContract",method = RequestMethod.GET)
public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model){
	
	List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("contractType");
	
  	Contract contract = contractService.getContractById(id);
	model.put("contract", contract);
	model.addAttribute("dicList", dicList);
    return new ModelAndView("crm/contractForm", model);   
 }


/**
 * 记录列表页（分页）
 * 
 * @param num
 * @return
 */
@RequestMapping(value = "bpm/crm/contracts2", method = RequestMethod.GET)
public ModelAndView showContracts2(String num, ModelMap model) {
	Map<String, Object> context = new HashMap<String, Object>();
	List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
	String columnNames = "['Id','收支类型','合同类型','合同名称','合同编号','客户签约人','签约时间','合同金额','合同内容','创建时间','创建用户']";
	context.put("title", "Groups");
	context.put("gridId", "GROUP_LIST");
	context.put("needCheckbox", true);
	context.put("reurl", "bpm/crm/contractToString");
	context.put("columnNames", columnNames);
	CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
	CommonUtil.createFieldNameList(fieldNameList, "szlx", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "type", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "num", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "contractor", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "signingTimeByString", "100", "left", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "amount", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "content", "100", "left", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "center", "", "false");
	CommonUtil.createFieldNameList(fieldNameList, "createdBy.fullName", "100", "center", "", "false");

	context.put("fieldNameList", fieldNameList);
	String script = GridUtil.generateScriptDy(velocityEngine, context);
	model.addAttribute("script", script);
	return new ModelAndView("crm/contractList2", model);
}

@RequestMapping(value = "bpm/crm/contractToString", method = RequestMethod.GET)
public @ResponseBody String contractToString(HttpServletRequest request, Integer page, Integer rows,  ModelMap model) {
	PageBean<Contract> pageBean = new PageBean<Contract>(page, rows);
	pageBean = contractService.getContracts(pageBean);
	String[] fieldNames = { "id", "szlx", "type", "title", "num", "contractor", "signingTimeByString", "amount", "content", "createdTimeByString", "createdBy.fullName" };
	PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
	String data = CommonUtil.convertBeanToJsonString(pageResultBean);
	return data;
}

}