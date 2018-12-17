package com.eazytec.webapp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.eazytec.Constants;
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.service.AllxService;
import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.dto.AllxDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.service.DepartmentExistsException;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;
import com.eazytec.webapp.controller.BaseFormController;

@Controller
public class AllxController extends BaseFormController {

	@Autowired
	private AllxService allxService;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private DataDictionaryService dicService;

	@Autowired
	public void setAllxService(AllxService allxService) {
		this.allxService = allxService;
	}

	
	@RequestMapping(value = "bpm/alxxgl/allx", method = RequestMethod.GET)
	public ModelAndView manageDepartment(HttpServletRequest request, ModelMap model) {
		String script = null;
		List<Allx> allx = null;
		Locale locale = request.getLocale();
		String id = request.getParameter("id");
		String name = request.getParameter("searchText");
		try {
// 		List<AllxDTO> allxList = allxService.getAllAllxDTO();
			if(name!=null&&name!=""){
        		  name=java.net.URLDecoder.decode(name, "utf-8");
        		  if(id!=null&&id!=""){
        			  allx = allxService.getAllxBySuperDepartmentId2(id,name);
        		  }else{
        			  allx = allxService.getAllxBySuperDepartmentId1(name);
        		  }
			}else{
				
				allx = allxService.getAllxBySuperDepartmentId("Organization");
			}
			List<Allx> allxList = allxService.getAllAllx();
			model.addAttribute(Constants.ALLX_LIST, allxList);
			String[] fieldNames = { "id","lxmc", "lxdm", "sjlxdm", "bz", "createdTime", "lastModifyTime", "isActive"};
			script = generateScriptForAllxGrid2(CommonUtil.getMapListFromObjectListByFieldNames(allx, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			saveError(request, getText("error.allx.delete", e.getMessage(), locale));
		}

		return new ModelAndView("allx/allxScriptTree", model);
	}
	
	@RequestMapping(value = "bpm/allxgl/getAllxGrid", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getAllxGrid(@RequestParam("id") String id, @RequestParam("parentNode") String parentNode, HttpServletRequest request) {
		Locale locale = request.getLocale();
		String script = null;

		List<Allx> allx = null;
		try {
			log.info("Getting allxs for selected " + id + " : for : " + parentNode);
			allx = allxService.getAllxBySuperDepartmentId(id);
			String[] fieldNames = { "id","lxmc", "lxdm", "sjlxdm", "bz", "createdTime", "lastModifyTime", "isActive"};
			script = generateScriptForAllxGrid2(CommonUtil.getMapListFromObjectListByFieldNames(allx, fieldNames, ""), velocityEngine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.department.delete", e.getMessage(), locale));
		}
		return script;
	}

	@RequestMapping(value = "bpm/admin/allxForm", method = RequestMethod.GET)
	public ModelAndView showAllxForm(HttpServletRequest request, ModelMap model) {
		try {
			String id =request.getParameter("id") ;
			Allx allx =new Allx();
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
			model.addAttribute("dicList", dicList);
			if(id!=null && id !=""){
				//System.out.println(id);
				 allx = allxService.getAllxById(id);
				model.addAttribute("allx",allx);
			}else{
				model.addAttribute("allx",allx);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		if( (!StringUtil.isEmptyString(request.getParameter("id")))) {
			return new ModelAndView("allx/allxEditForm", model);
		}else{
			return new ModelAndView("allx/allxForm", model);
		}
		
	}
	
	@RequestMapping(value = "bpm/admin/getAllxNodes", method = RequestMethod.GET)
	public @ResponseBody String getAllxNodes(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			List<LabelValue> organization = allxService.getOrganizationAsLabelValue();
			nodes = CommonUtil.getNodesFromLabelValue(organization);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}
 
	@RequestMapping(value = "bpm/admin/getAllxChildNodes", method = RequestMethod.GET)
	public @ResponseBody String getDepartmentChildNodes(ModelMap model, @RequestParam("currentNode") String currentNode, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			List<LabelValue> childNodeList = allxService.getAllxsAsLabelValueByParent(currentNode);
			nodes = CommonUtil.getNodesFromLabelValue(childNodeList);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}
	
	@RequestMapping(value = "bpm/admin/saveAllx", method = RequestMethod.POST)
	public ModelAndView saveAllx(@ModelAttribute("allx") Allx allx, BindingResult errors, ModelMap model, HttpServletRequest request) {
		try {
			if (allx.getSuperDepartment() != null) {
				if (allx.getLxdm().equalsIgnoreCase(allx.getSuperDepartment())) {
					errors.rejectValue("superDepartment", "errors.superdepartment.allx");
					return new ModelAndView("allx/allxForm", model);
				}
			}
			if (allx.getLxdm().equalsIgnoreCase("Organization")) {
				allx.setSuperDepartment(null);
			} else {
				if (allx.getSuperDepartment() == null || allx.getSuperDepartment().isEmpty()) {
					saveError(request, "上级案例类型不能为空");
					model.addAttribute("request", request);
					return new ModelAndView("allx/allxForm", model);
				}
			}
			if(allx.getId().equals(Constants.EMPTY_STRING)){
				allx.setIsActive("Y");
			}
			allxService.saveAllx(allx);
		}  catch (Exception e) {
			log.error(e.getMessage(), e);
			errors.rejectValue("error.allx.save", e.getMessage());
			return new ModelAndView("allx/allxForm", model);
		}
		return new ModelAndView("redirect:/bpm/alxxgl/allx");
	}
	
	@RequestMapping(value = "bpm/admin/deleteSelectedAllxs", method = RequestMethod.GET)
	public ModelAndView deleteSelectedAllxs(@RequestParam("departmentIds") String departmentIds, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		try {
		    List<String> departmentIdList = CommonUtil.convertJsonToListStrings(departmentIds);
			boolean isPermitToDelete = true;
			isPermitToDelete = allxService.removeDepartments(departmentIdList, isPermitToDelete);
			 
			reloadContext(request.getSession().getServletContext());
			log.info("Allx Names : " + departmentIds + " " + getText("delete.success", locale));
			saveMessage(request, getText("删除案例类型成功", locale));
			 
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("删除案例类型失败", e.getMessage(), locale));
		}
		 
		return new ModelAndView("redirect:/bpm/alxxgl/allx");
	}
	 
	private void reloadContext(ServletContext context) {
		context.setAttribute(Constants.AVAILABLE_ALLXS, new HashSet<LabelValue>(allxService.getAllAllxs()));
	}
	/**
	 * 记录列表页
	 * 
	 * @return
	 */						
	@RequestMapping(value = "bpm/alxxgl/allbwh", method = RequestMethod.GET)
	public ModelAndView showAllxs(String lxmc) {
		Model model = new ExtendedModelMap();
		try {
			List<Allx> allxList = allxService.getAllxs(lxmc);
			model.addAttribute("allxList", allxList);
			String[] fieldNames = { "id","lxdm", "lxmc", "sjlxdm", "bz", "createdTime", "lastModifyTime", "isActive"};
			String script = generateScriptForAllxGrid(CommonUtil.getMapListFromObjectListByFieldNames(allxList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("queryNum", lxmc);
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("allx/allxScriptTree", model.asMap());
	}

	public static String generateScriptForAllxGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','类型代码', '类型名称', '上级类型代码', '备注', '创建时间', '最后修改时间', '是否有效' ]";
		
		context.put("title", "Groups");
		context.put("gridId", "ALLX_LIST");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lxdm", "100", "center", "_showEditAllx", "false");
		CommonUtil.createFieldNameList(fieldNameList, "lxmc", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sjlxdm", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTime", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "lastModifyTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isActive", "100", "center", "", "false");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	
	public static String generateScriptForAllxGrid2(List<Map<String, Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','类型名称', '类型代码', '上级类型代码', '备注', '创建时间', '最后修改时间', '是否有效' ]";
		
		context.put("title", "Groups");
		context.put("gridId", "ALLX_LIST");
		context.put("needCheckbox", true);
		//context.put("dynamicGridWidth", "unitGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lxmc", "100", "center", "_showEditAllx2", "false");
		CommonUtil.createFieldNameList(fieldNameList, "lxdm", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sjlxdm", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTime", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lastModifyTime", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isActive", "100", "center", "", "true");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	/**
	 * 记录新增
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/alxxgl/allxForm", method = RequestMethod.GET)
	public ModelAndView showNewAllxForm(ModelMap model) {

		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
		Allx allx = new Allx();
		model.addAttribute("allx", allx);
		model.addAttribute("dicList", dicList);
		return new ModelAndView("allx/allxForm", model);
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
	@RequestMapping(value = "bpm/alxxgl/saveAllx", method = RequestMethod.POST)
	public ModelAndView saveAllx(HttpServletRequest request, @ModelAttribute("allx") Allx allx, ModelMap model, BindingResult errors) {

		validator.validate(allx, errors);

		if (errors.hasErrors()) { // don't validate when deleting
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
			model.addAttribute("allx", allx);
			model.addAttribute("dicList", dicList);
			return new ModelAndView("allx/allxForm", model);
		}

		Date date = new Date();
		if (StringUtils.isBlank(allx.getId())) {
			allx.setCreatedTime(date);
			allxService.saveAllx(allx);
		}else{
			allx.setLastModifyTime(date);
			allxService.updateAllx(allx);
			
		}
		return new ModelAndView("redirect:allbwh");
	}

	/**
	 * 记录删除
	 * 
	 * @param recordIds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/alxxgl/deleteSelectedAllx", method = RequestMethod.GET)
	public ModelAndView deleteSelectedRecords(@RequestParam("allxIds") String allxIds, HttpServletRequest request) {

		List<String> allxIdList = new ArrayList<String>();
		if (allxIds.contains(",")) {
			String[] ids = allxIds.split(",");
			for (String id : ids) {
				allxIdList.add(id);
			}
		} else {
			allxIdList.add(allxIds);
		}

		allxService.removeAllxs(allxIdList);
		return new ModelAndView("redirect:allbwh");
	}

	/**
	 * 记录编辑
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/alxxgl/editAllx", method = RequestMethod.GET)
	public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model) {

		List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");

		Allx allx = allxService.getAllxById(id);
		
		model.put("allx", allx);
		model.addAttribute("dicList", dicList);
		return new ModelAndView("allx/allxForm", model);
	}

	// ========================================================================================================================================

	/**
	 * 记录列表页（分页）
	 * 
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "bpm/alxxgl/allbwh2", method = RequestMethod.GET)
	public ModelAndView showRecords2(String num, ModelMap model) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','类型代码', '类型名称', ‘上级类型代码', '备注', '创建时间',’最后修改时间‘, '是否有效']";
		context.put("title", "Groups");
		context.put("gridId", "RECORD_LIST");
		context.put("needCheckbox", true);
		context.put("reurl", "bpm/alxxgl/allxToString");
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lxdm", "100", "center", "_showEditAllx", "false");
		CommonUtil.createFieldNameList(fieldNameList, "lxmc", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sjlxdm", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTime", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "lastModifyTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isActive", "100", "center", "", "false");

		context.put("fieldNameList", fieldNameList);
		String script = GridUtil.generateScriptDy(velocityEngine, context);
		model.addAttribute("script", script);
		return new ModelAndView("allx/allxList2", model);
	}

	@RequestMapping(value = "bpm/alxxgl/allxToString", method = RequestMethod.GET)
	public @ResponseBody String recordToString(String num, Integer page, Integer rows, String orderName, String orderType, ModelMap model, HttpServletRequest request) {
		PageBean<Allx> pageBean = new PageBean<Allx>(page, rows);
		pageBean = allxService.getAllxs(pageBean,num,orderName,orderType);
		String[] fieldNames = { "id","lxdm", "lxmc", "sjlxdm", "bz", "createdTime", "lastModifyTime", "isActive"};
		PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
		String data = CommonUtil.convertBeanToJsonString(pageResultBean);
		return data;
	}

}