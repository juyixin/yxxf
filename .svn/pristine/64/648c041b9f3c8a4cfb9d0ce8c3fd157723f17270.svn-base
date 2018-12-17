package com.eazytec.bpm.metadata.form.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.activiti.engine.FormService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.metadata.form.dao.MetaFormDao;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.dao.ProcessDao;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Form;
import com.eazytec.model.FormButton;
import com.eazytec.model.FormNameMappings;
import com.eazytec.model.Forms;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.MetaTableRelation;
import com.eazytec.model.Module;
import com.eazytec.model.ProcessFormName;
import com.eazytec.model.ProcessPermission;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.util.DateUtil;
import com.eazytec.util.JdomXmlParserUtil;




/**
 * <p>Form metadata related form service calls. Implements {@link FormDefinitionService}.</p>
 * 
 * @author madan 
 * @author Karthick
 *
 */
@Service("formDefintionService")
public class FormDefinitionServiceImpl implements FormDefinitionService{
	
	private FormService formService;
	private MetaFormDao formDAO;
	private UserManager userManager;
	private ModuleService moduleService;
	private ProcessDefinitionService processDefinitionService;
	private com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService;
	private ProcessDao processDAO;
	
	@Autowired
	public void setProcessDAO(ProcessDao processDAO) {
		this.processDAO = processDAO;
	}

	@Autowired
	public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}
	
	@Autowired
	public void setRtProcessService(com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * {@inheritDoc}
	 */
	public StartFormData getStartFormData(String processDefinitionId)throws BpmException {
		return formService.getStartFormData(processDefinitionId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TaskFormData getTaskFormData(String taskId){
		return formService.getTaskFormData(taskId);
	}
	
	public TaskFormData getHistoricTaskFormData(HistoricTaskInstance task){
		return formService.getHistoricTaskFormData(task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Form saveForm(Form form) throws BpmException{
		if(null != form.getId()) {
			Integer version = formDAO.getLatestVersionByForm(form.getFormName());
			form.setVersion(version + 1);
			formDAO.updateFromStatusByFormName(form.getFormName());
		} else {
			form.setVersion(1);
		}
		form.setActive(true);
		return formDAO.saveForm(form);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public MetaForm saveDynamicForm(MetaForm metaForm,String moduleId) throws BpmException, IllegalAccessException, InvocationTargetException{
		Module module=moduleService.getModule(moduleId);
		
		 if(null != metaForm.getId()) {
			MetaForm previousMetaForm =formDAO.getDynamicFormById(metaForm.getId());
			
			//if(isFormChanged(metaForm,previousMetaForm)){
				Integer version = formDAO.getLatestVersionByDynamicForm(metaForm.getFormName());
				metaForm.setVersion(version + 1);
				
				Forms forms = new Forms();
				BeanUtils.copyProperties(forms ,metaForm);
				formDAO.updateFromStatusByDynamicFormName(metaForm.getFormName());
				formDAO.updateHistoricForms(forms.getFormName());

				
				//to get the old modules
				Set<Module> oldModules=previousMetaForm.getModules();
				
				if(!oldModules.contains(module)){
					oldModules.add(module);
				}
				
				List<String> moduleIds=new ArrayList<String>();
				for(Module oldModulesObj: oldModules){
					moduleIds.add(oldModulesObj.getId());
				}
				
				Set<Module> newModules=new HashSet<Module>();
				newModules.addAll(moduleService.getModulesByIds(moduleIds));
				metaForm.setModules(newModules);
				metaForm.setPrintTemplate(previousMetaForm.getPrintTemplate());
			/*}else{
				previousMetaForm.setHtmlSource(metaForm.getHtmlSource());
				previousMetaForm.setXmlSource(metaForm.getXmlSource());
				metaForm=formDAO.updateDynamicForm(previousMetaForm);
				return metaForm;
			}*/
		} else {
			metaForm.addModule(module);
			metaForm.setVersion(1);
		}
		metaForm.setActive(true);
	
		metaForm=formDAO.saveDynamicForm(metaForm);
		
		// save forms for  history
		Forms forms = new Forms();
		BeanUtils.copyProperties(forms ,metaForm);
		System.out.println("=====================FORMS HISTORY");
		System.out.println("=============="+forms.getFormName());
		
		formDAO.saveDynamicHistoryForm(forms);
		//saving the form name for process mapping 
		FormNameMappings formNameMappings=new FormNameMappings();
		formNameMappings.setFormId(metaForm.getId());
		formNameMappings.setFormName(metaForm.getFormName());
		formDAO.saveFormNameMappings(formNameMappings);
		
		return metaForm;
	}
	
	/**
	 * Check the form elements is changed or not
	 * @param metaForm
	 * @param previousMetaForm
	 * @return
	 * @throws BpmException
	 */
	public boolean isFormChanged(MetaForm metaForm,MetaForm previousMetaForm)throws BpmException{
		//Current Form columns and names
		List<String> currentColumnId=new ArrayList<String>();
		List<String> currentFiledName=new ArrayList<String>();
		List<String> currentFieldValue = new ArrayList<String>();
		
		//Previous Form columns and names
		List<String> previousColumnId=new ArrayList<String>();
		List<String> previousFiledName=new ArrayList<String>();
		List<String> previousFieldValue = new ArrayList<String>();
		try {
			List<Element> currentFromPropertyEls = JdomXmlParserUtil.getFromElementPropertyForBpmn(metaForm.getXmlSource());
			for (Element fromPropertyEl : currentFromPropertyEls) {
				currentColumnId.add(fromPropertyEl.getAttributeValue("column"));
				currentFiledName.add(fromPropertyEl.getAttributeValue("name"));
				if(fromPropertyEl.getAttributeValue("value") != null && fromPropertyEl.getAttributeValue("value") != "null" && fromPropertyEl.getAttributeValue("value") != ""){
					currentFieldValue.add(fromPropertyEl.getAttributeValue("value"));
				}
			}
			
			List<Element> previousFromPropertyEls = JdomXmlParserUtil.getFromElementPropertyForBpmn(previousMetaForm.getXmlSource());
			for (Element fromPropertyEl : previousFromPropertyEls) {
				previousColumnId.add(fromPropertyEl.getAttributeValue("column"));
				previousFiledName.add(fromPropertyEl.getAttributeValue("name"));
				if(fromPropertyEl.getAttributeValue("value") != null && fromPropertyEl.getAttributeValue("value") != "null" && fromPropertyEl.getAttributeValue("value") != ""){
					previousFieldValue.add(fromPropertyEl.getAttributeValue("value"));
				}
				
			}
			
			if(currentColumnId.containsAll(previousColumnId) && currentFiledName.containsAll(previousFiledName) && currentFiledName.size() == previousFiledName.size() && currentFieldValue.containsAll(previousFieldValue) && currentFieldValue.size() == previousFieldValue.size()){
				return false;
			}else{
				return true;
			}
			
		} catch (EazyBpmException e) {
			// TODO Auto-generated catch block
			throw new BpmException(e.getMessage());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			throw new BpmException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BpmException(e.getMessage());
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public FormButton saveFormButton(FormButton formButton) throws BpmException{
		String buttonStyle = formButton.getStyle();
		String newButtonStyle ="";
		
		if(buttonStyle!=null && !buttonStyle.isEmpty()){
			String[] buttonStyleArray = buttonStyle.split(";");
			for(String buttonStyleElem: buttonStyleArray){
				if(!buttonStyleElem.contains(" left:") && !buttonStyleElem.contains(" top:")){
					newButtonStyle=newButtonStyle + buttonStyleElem+";";
				}
			}
		}
		if(newButtonStyle!=""){
			formButton.setStyle(newButtonStyle);
		}
		
		return formDAO.saveFormButton(formButton);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<FormButton> getFormButtons() throws BpmException{
		List<FormButton> formButtons=formDAO.getFormButtons();
		for(FormButton formButton:formButtons){
			String formButtonId= formButton.getId();
			if(formButtonId.equals("aa59d398-9773-11e2-aca8-00270e0048cc")||formButtonId.equals("c4c64568-9773-11e2-aca8-00270e0048cc")||formButtonId.equals("ccaa5710-9773-11e2-aca8-00270e0048cc")){
				formButton.setIsDefault(true);
			}else{
				formButton.setIsDefault(false);
			}
		}
		return formButtons;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteFormButton(String formButtonId) throws EazyBpmException{
		boolean status=false;
		if(formButtonId !=null){
			formDAO.deleteFormButton(formButtonId);
			status=true;
		}
		
		return status;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public boolean isFormNamePresent(String formName) throws EazyBpmException{
		// Get forms for given form name 
		List<MetaForm> forms = formDAO.getFormByFormName(formName);
		if(forms.size() > 0)
			return true;
		else
			return false;	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void restoreForm(String formId, String formName) throws EazyBpmException{
		// Update the all the form status as inactive by form name
		formDAO.updateFromStatusByFormName(formName);
		// Update the form status as active by form id 
		formDAO.updateFromStatusById(formId);
	}
	
	
	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws JDOMException 
	 * @throws TransformerConfigurationException 
	 */
	public String parseXmlByDomParser(String bpmnXML,String processName,String svgDOM, String jsonData,boolean isProcessCreate) throws Exception{
		SAXBuilder builder1 = new SAXBuilder();
		List<String> subProcessNameList = JdomXmlParserUtil.getSubProcessListFromXml(bpmnXML,processName,isProcessCreate);
		Map<String, byte[]> subProcesXmlMap = new HashMap<String, byte[]>();
		if(subProcessNameList != null && !subProcessNameList.isEmpty()){
			for(String subProcessName : subProcessNameList){
				ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(subProcessName);
				ResourceEntity resource = rtProcessService.getResourceAsEntity(processDefinition.getDeploymentId(), processDefinition.getResourceName());
				subProcesXmlMap.put(subProcessName, resource.getBytes());
			}
		}
		Set<String> formIds=JdomXmlParserUtil.getNodeListFromXml(bpmnXML,processName,isProcessCreate,subProcesXmlMap);
		Document document = builder1.build(new ByteArrayInputStream(bpmnXML.getBytes()));
		Element processRoot = document.getRootElement();
		 if(!formIds.isEmpty()){
			
			 List<ProcessFormName> processFormNameList=new ArrayList<ProcessFormName>();
			 String processKey=processName.replaceAll(" ", "_");
			 for(String formId:formIds){
				 ProcessFormName processFormName=new ProcessFormName(formId,processKey);
				 processFormNameList.add(processFormName);
				 
				MetaForm metaform = getDynamicFormById(formId);
				/*	byte[] xmlBlob =form.getXmlSource();

				String fromXMl = new String(xmlBlob);*/
				if(metaform != null){
					String fromXMl = metaform.getXmlSource();
					String formName = metaform.getFormName();
					List<Element> fromPropertyEl=JdomXmlParserUtil.getFormPropertyForBpmn(fromXMl);
				    processRoot =JdomXmlParserUtil.mergeBpmnXmlAndFormElements(processRoot,fromPropertyEl, formId,formName,processName,subProcesXmlMap);
				}else {
					FormNameMappings formNameMappings = formDAO.getFormNameMappingsByFormId(formId);
					throw new EazyBpmException(formNameMappings.getFormName()+" form is not found.");
				}
			}
			 formDAO.saveProcessAndFormName(processFormNameList);
		}else{
			processRoot =JdomXmlParserUtil.mergeBpmnXmlAndFormElements(processRoot,null,Constants.EMPTY_STRING,Constants.EMPTY_STRING,processName,subProcesXmlMap);
		}
		
		
		return JdomXmlParserUtil.generateBPMNXmlString(processRoot,document,svgDOM,jsonData);
	}
	
	public void setProcessPermission(String bpmnXml, String processName) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new ByteArrayInputStream(bpmnXml.getBytes()));
		Element root = document.getRootElement();
		List process = root.getChildren();
		Iterator processList = process.iterator();
		String userPermission = null;
		String rolePermission = null;
		String departmentPermission = null;
		String groupPermission = null;
        Set<String> formIdList = new HashSet<String>();
        List<ProcessPermission> processPermissions = new ArrayList<ProcessPermission>();
		while (processList.hasNext()) {
			Element processEl = (Element) processList.next();
			List childElement = processEl.getChildren();
			Iterator childElementList = childElement.iterator();
			while (childElementList.hasNext()) {
				Element childEl = (Element) childElementList.next();
				//For sub Process
				if(childEl.getName().equalsIgnoreCase("startevent")){
					userPermission = childEl.getAttributeValue("userPermission");
					rolePermission = childEl.getAttributeValue("rolePermission");
					departmentPermission = childEl.getAttributeValue("departmentPermission");
					groupPermission = childEl.getAttributeValue("groupPermission");
					if(userPermission != null || rolePermission != null || departmentPermission != null || groupPermission != null) {
						processPermissions.add(new ProcessPermission(processName,userPermission,rolePermission,groupPermission,departmentPermission));
					}
					break;
				}
			}
		}
		 formDAO.saveProcessPermission(processPermissions);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Form> getForm() throws BpmException {
		return formDAO.getAllLatestVersion();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Map<String,String>> getFields(String formId) throws EazyBpmException,JDOMException,IOException {
		List<Map<String,String>> formAttributes = new ArrayList<Map<String,String>>();
		MetaForm form = getDynamicFormById(formId);
		//Getting the table columns and relation 
		Set<MetaTableColumns> tableColumns=form.getTable().getMetaTableColumns();
		Set<MetaTableRelation> tableRelation=form.getTable().getMetaTableRelation();
		
		for(MetaTableRelation tableRelationObj:tableRelation){
			if(tableRelationObj.getChildTable()!=null){
				tableColumns.addAll(tableRelationObj.getChildTable().getMetaTableColumns());
			}
			
			if(tableRelationObj.getParentTable()!=null){
				tableColumns.addAll(tableRelationObj.getParentTable().getMetaTableColumns());
			}
		}
	
		Map<String,String> tableChineseName=new HashMap<String, String>();
		for(MetaTableColumns tableColumnsObj:tableColumns){
			tableChineseName.put(tableColumnsObj.getId(), tableColumnsObj.getChineseName());
		}
		
		//XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
       
		List<Element> fromPropertyEls;
		fromPropertyEls = JdomXmlParserUtil.getFromElementPropertyForBpmn(form.getXmlSource());
		
		for (Element fromPropertyEl : fromPropertyEls) {
			Map<String,String> formElements = new HashMap<String, String>();
			
			formElements.put("name", fromPropertyEl.getAttributeValue("name"));
			formElements.put("id", fromPropertyEl.getAttributeValue("id"));
			formElements.put("type", fromPropertyEl.getAttributeValue("type"));
			formElements.put("tableId", fromPropertyEl.getAttributeValue("table"));
			formElements.put("columnId", fromPropertyEl.getAttributeValue("column"));
			formElements.put("onclick", fromPropertyEl.getAttributeValue("onclick"));
			formElements.put("onfocus", fromPropertyEl.getAttributeValue("onfocus"));
			formElements.put("onchange", fromPropertyEl.getAttributeValue("onchange"));
			formElements.put("fieldClass", fromPropertyEl.getAttributeValue("fieldClass"));
			formElements.put("datadictionary", fromPropertyEl.getAttributeValue("dataDictionary"));
			formElements.put("fieldValue", fromPropertyEl.getAttributeValue("value"));
			String columnId=fromPropertyEl.getAttributeValue("column");
			if(tableChineseName.containsKey(columnId)){
				formElements.put("chineseName", tableChineseName.get(columnId));
			}
			
			if(fromPropertyEl.getAttributeValue("id") != null && fromPropertyEl.getAttributeValue("name") != null){
				formAttributes.add(formElements);
			}
		}
		return formAttributes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Map<String,String>> getFieldsWithXMLElement(String formId) throws EazyBpmException,JDOMException,IOException {
		List<Map<String,String>> formAttributes = new ArrayList<Map<String,String>>();
		MetaForm form = getDynamicFormById(formId);
		MetaTable metaTable=form.getTable();
		Set<MetaTableColumns> metaTableColumns=metaTable.getMetaTableColumns();
		
		XMLOutputter domXMLToString = new XMLOutputter(Format.getPrettyFormat());
       
		List<Element> fromPropertyEls;
		fromPropertyEls = JdomXmlParserUtil.getFromElementPropertyForBpmn(form.getXmlSource());

		for (Element fromPropertyEl : fromPropertyEls) {
			String fieldType=(String)fromPropertyEl.getAttributeValue("type");

			if(!fieldType.equals("hidden")){
				Map<String,String> formElements = new HashMap<String, String>();
				fromPropertyEl.setAttribute("isSubForm","true");
				formElements.put("formName", form.getFormName());
				formElements.put("fieldName", fromPropertyEl.getAttributeValue("name"));
				formElements.put("fieldId", fromPropertyEl.getAttributeValue("id"));
				formElements.put("tableId", fromPropertyEl.getAttributeValue("table"));
				formElements.put("columnId", fromPropertyEl.getAttributeValue("column"));
				formElements.put("onclick", fromPropertyEl.getAttributeValue("onclick"));
				formElements.put("onfocus", fromPropertyEl.getAttributeValue("onfocus"));
				formElements.put("onchange", fromPropertyEl.getAttributeValue("onchange"));
				formElements.put("onblur", fromPropertyEl.getAttributeValue("onblur"));
				formElements.put("fieldClass", fromPropertyEl.getAttributeValue("fieldClass"));
				formElements.put("datadictionary", fromPropertyEl.getAttributeValue("dataDictionary"));
				formElements.put("fieldType", fieldType);
				formElements.put("fieldValue", fromPropertyEl.getAttributeValue("value"));
				String optionTags="";
				if(fieldType.equalsIgnoreCase("select")){
					List selectOptionForms=fromPropertyEl.getChildren();
					Iterator selectOption =selectOptionForms.iterator();
					
					if(!selectOptionForms.isEmpty()){
						while(selectOption.hasNext()){
							Element subFormEl = (Element) selectOption.next();
						    optionTags=optionTags+"<option value='"+subFormEl.getAttributeValue("value")+"'>"+subFormEl.getAttributeValue("label")+"</option>";
						}
					}
					//log.info("************************ optionTags ---------   "+optionTags);
				}
				formElements.put("optionTags", optionTags);
				formElements.put("xmlContent", domXMLToString.outputString(fromPropertyEl));
				 
				if(fromPropertyEl.getAttributeValue("id") != null && fromPropertyEl.getAttributeValue("name") != null){
					for(MetaTableColumns tableCols:metaTableColumns){
						if(tableCols.getId().equals(fromPropertyEl.getAttributeValue("column"))){
							formAttributes.add(formElements);
						}
					}
					
				}
			}
			
		}
		return formAttributes;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> getAllDynamicForms() throws BpmException{
		User user = CommonUtil.getLoggedInUser();
		//if(userManager.isUserAdmin(user)){
			//log.debug("User is admin, getting all forms by default----");
			return formDAO.getAllLatestVersionForms();
		//}else{
//			log.debug("User getting forms by modules------");
//			List<Module> modules = moduleService.getAllModuleList();			
//			if(modules!=null&&modules.size()>0){
//				log.debug("User getting modules------"+modules.size()+" User ID : ------------ "+user.getId());
//				return formDAO.getAllLatestVersionFormsInModules(modules,user.getId());
//			}else{
//				return null;
//			}
//		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> getAllDynamicFormsForModule(String moduleId,String isEdit,String isSystemModule, boolean isJspForm,boolean isTemplateView) throws BpmException{
			List<String> formListIds=new ArrayList<String>();
			Module modules=moduleService.getModule(moduleId);
			Set<MetaForm> metaForms=modules.getForms();
			for(MetaForm metaForm:metaForms){
				if(metaForm.isActive() && !metaForm.getIsDelete() && metaForm.getIsJspForm() == isJspForm){
					//formList.add(metaForm);
					formListIds.add(metaForm.getId());
				}
			}
			
			if(formListIds.isEmpty()){
				return null;
			}
			List<MetaForm> metaFormList=formDAO.getAllLatestVersionFormsForModule(formListIds,isTemplateView);
			for(MetaForm metaForm:metaFormList){
					User userObj=userManager.get(metaForm.getCreatedBy());
					metaForm.setCreatedByFullName(userObj.getFullName());
					metaForm.setIsEdit(isEdit);
					metaForm.setIsSystemModule(isSystemModule);
			}
			return metaFormList;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> getAllTemplateForms() throws BpmException{
		boolean isAdmin=false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles=user.getRoles();
		for(Role role:roles){
			if(role.getId().equals("ROLE_ADMIN")){
				isAdmin=true;
				break;
			}
		}
		
		if(isAdmin){
			return formDAO.getAllLatestVersionTemplateForms();
		} else {
			List<Module> moduleList = moduleService.getModulesByUserPrivilege();
			if(!moduleList.isEmpty()) {
				return formDAO.getAllTemplateFormsByModuleList(moduleList);
			}
		}
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> getAllMetaFormForOrgTree() throws BpmException{
		return formDAO.getAllMetaFormForOrgTree();
	}
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getMetaFormAsLabelValue() throws BpmException{
		return formDAO.getMetaFormAsLabelValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllNonPublicFormsByJspForm(String isJspForm) throws EazyBpmException {
		
		return formDAO.getAllNonPublicFormsByJspForm(isJspForm);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllNonPublicForms() throws EazyBpmException {
		
		return formDAO.getAllNonPublicForms();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LabelValue getFormLabelValueById(String formId) throws EazyBpmException {
		return formDAO.getFormLabelValueById(formId);
	}
	
	public List<LabelValue> getFormsLabelValueByIds(String formId) throws EazyBpmException {
		Set<LabelValue> formLabelValues = new HashSet<LabelValue>();
		List<LabelValue> forms = new ArrayList<LabelValue>();
		if(formId.contains(",")){
			String formIds[] = formId.split(",");
			for(int i=0;i<formIds.length;i++){
				formLabelValues.add(formDAO.getFormLabelValueById(formIds[i]));
			}
			if(formLabelValues != null){
				forms.addAll(formLabelValues);
			} else{
				return null;
			}
		}else{
			forms.add(formDAO.getFormLabelValueById(formId));
			
		}
		return forms;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> getFormVersionByFormId(String formId) throws EazyBpmException {
		
		return formDAO.getFormVersionByFormId(formId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllDynamicFormNames() throws EazyBpmException{
		User user = CommonUtil.getLoggedInUser();
		if(userManager.isUserAdmin(user)){
			log.debug("User is admin, getting all forms by default----");
			return formDAO.getAllLatestVersionFormNames();
		}else{
			log.debug("User getting forms by modules------");
			List<Module> modules = moduleService.getAllModuleList();			
			if(modules!=null&&modules.size()>0){
				log.debug("User getting modules------"+modules.size()+" User ID : ------------ "+user.getId());
				return formDAO.getAllLatestVersionFormNamesInModules(modules,user.getId());
			}else{
				return null;
			}
			
		}
		//return formDAO.getAllLatestVersionFormNames();
	}
	
	/***
     * {@inheritDoc}
     */
	public List<MetaForm> getFormDetailsByNames(String formNames) throws EazyBpmException{
		return formDAO.getFormDetailsByNames(formNames);
	}
	
	public List<LabelValue> getAllChildTableFormNames(String tableId) throws EazyBpmException{
		return formDAO.getAllChildTableFormNames(tableId);
	}
	
	/***
     * {@inheritDoc}
     * @param formDetailsListMap
     * @return
     * @throws BpmException
     */
    public List<Map<String,Object>> resolveFormDetailsListMap(List<Map<String,Object>> formDetailsListMap) throws BpmException{
       
        List<Map<String,Object>> resolvedDetailsListMap = new ArrayList<Map<String,Object>>();
        Date dateTime = null;
        String dateStr = null;
        for(Map<String, Object> formDetailsMap : formDetailsListMap){
            try {
	            dateTime = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss.SSSSSS", formDetailsMap.get("createdOn").toString());
	            dateStr = DateUtil.convertDateToString(dateTime);
	            if(dateStr != null){
	                formDetailsMap.put("createdOn", dateStr);      
	            }else{
	            	formDetailsMap.put("createdOn", dateTime.toString());      
	            }
            } catch (ParseException e) {
    			log.error("Date cannot be parsed for : "+dateTime);
    		}
           resolvedDetailsListMap.add(formDetailsMap);
        }
       
        return resolvedDetailsListMap;
    }
    
	/**
	 * {@inheritDoc}
	 */
	
	public String generateSubFormHtml(List<Map<String,Object>> subFormfieldList,String subFormName)throws EazyBpmException{
		StringBuffer subFormHtml = new StringBuffer();
		subFormHtml.append("<div id='"+subFormName+"'><table id='subFormTable'><tr>");
		for(Map<String,Object> subFormField:subFormfieldList){
			String fieldType=(String)subFormField.get("fieldType");
			if(fieldType.equals("radio")){
				subFormHtml.append("<th>"+subFormField.get("fieldValue")+"</th>");
			}else{
				subFormHtml.append("<th>"+subFormField.get("fieldName")+"</th>");
			}
		}
		if(subFormfieldList == null || subFormfieldList.isEmpty()) {
			subFormHtml.append("");

		} else {
			subFormHtml.append("<th></th><th></th></tr><tr id='row1'>");
		}
		
		for(Map<String,Object> subFormField:subFormfieldList){
			String fieldType=(String)subFormField.get("fieldType");
			String fieldValue=(String)subFormField.get("fieldValue");
			String optionTags=(String)subFormField.get("optionTags");
			
			if(optionTags.equals("null")){
				optionTags="";
			}
			if(fieldValue.equals("null")){
				fieldValue = "";
			}
			String fieldOnclick="";
			String fieldOnfocus="";
			String fieldOnchange="";
			String fieldClass="";
			String fieldOnBlur = "";
			if(!(String.valueOf(subFormField.get("fieldOnclick"))).equals("") && !(String.valueOf(subFormField.get("fieldOnclick"))).equals(null) && !(String.valueOf(subFormField.get("fieldOnclick"))).equals("null") && !String.valueOf(subFormField.get("fieldOnclick")).isEmpty()){
				fieldOnclick=" onclick=\""+(String)subFormField.get("fieldOnclick")+"\" ";
			}
			
			if(!(String.valueOf(subFormField.get("fieldOnfocus"))).equals("") && !(String.valueOf(subFormField.get("fieldOnfocus"))).equals(null) && !(String.valueOf(subFormField.get("fieldOnfocus"))).equals("null") && !String.valueOf(subFormField.get("fieldOnfocus")).isEmpty()){
				fieldOnfocus=" onfocus=\""+(String)subFormField.get("fieldOnfocus")+"\" ";
			}
			
			if(!(String.valueOf(subFormField.get("fieldOnchange"))).equals("") && !(String.valueOf(subFormField.get("fieldOnchange"))).equals(null) && !(String.valueOf(subFormField.get("fieldOnchange"))).equals("null") && !String.valueOf(subFormField.get("fieldOnchange")).isEmpty()){
				fieldOnchange=" onchange=\""+(String)subFormField.get("fieldOnchange")+"\" ";
			}
			
			if(!(String.valueOf(subFormField.get("fieldClass"))).equals("") && !(String.valueOf(subFormField.get("fieldClass"))).equals(null) && !(String.valueOf(subFormField.get("fieldClass"))).equals("null") && !String.valueOf(subFormField.get("fieldClass")).isEmpty() ){
				fieldClass=" class='"+(String)subFormField.get("fieldClass")+"' ";
			}
			if(!(String.valueOf(subFormField.get("fieldOnBlur"))).equals("") && !(String.valueOf(subFormField.get("fieldOnBlur"))).equals(null) && !(String.valueOf(subFormField.get("fieldOnBlur"))).equals("null") && !String.valueOf(subFormField.get("fieldOnBlur")).isEmpty()){
				fieldOnBlur=" onblur=\""+(String)subFormField.get("fieldOnBlur")+"\" ";
			}
			
			if(fieldType.trim().equals("textarea")){
				subFormHtml.append("<td><textarea isSubForm='true' id='subForm_"+subFormField.get("fieldName")+"_Form_1'  name='subForm_"+subFormField.get("fieldName")+"_Form_1' table='"+subFormField.get("tableId")+"' column='"+subFormField.get("columnId")+"' ");
				subFormHtml.append(fieldOnclick);
				subFormHtml.append(fieldOnfocus);
				subFormHtml.append(fieldOnBlur);
				subFormHtml.append(" ></textarea></td>");
			}else if(fieldType.trim().equals("select")){
				subFormHtml.append("<td><select isSubForm='true' id='subForm_"+subFormField.get("fieldName")+"_Form_1' name='subForm_"+subFormField.get("fieldName")+"_Form_1' table='"+subFormField.get("tableId")+"' column='"+subFormField.get("columnId")+"' datadictionary='"+subFormField.get("dataDictionary")+"' ");
				subFormHtml.append(fieldOnchange);
				subFormHtml.append(fieldClass);
				subFormHtml.append(" >");
				subFormHtml.append(optionTags);
				subFormHtml.append("</select></td>");
			}else{
				subFormHtml.append("<td><input isSubForm='true' type="+fieldType+" id='subForm_"+subFormField.get("fieldName")+"_Form_1' name='subForm_"+subFormField.get("fieldName")+"_Form_1' table='"+subFormField.get("tableId")+"' column='"+subFormField.get("columnId")+"' value='"+fieldValue+"' ");
				subFormHtml.append(fieldOnclick);
				subFormHtml.append(fieldOnfocus);
				subFormHtml.append(fieldOnchange);
				subFormHtml.append(fieldClass);
				subFormHtml.append(fieldOnBlur);
				subFormHtml.append(" /></td>");
			}
			
		}
			
		if(subFormfieldList == null || subFormfieldList.isEmpty()) {
			subFormHtml.append("");

		} else {
			subFormHtml.append("<td><img id='subFormAdd' src='/images/add.gif' onClick='cloneMappingRow(this.parentNode.parentNode.parentNode.parentNode);'></td><td><img src='/images/delete.gif' id='subFormDelete' onClick='deleteRow(this,this.parentNode.parentNode.parentNode.parentNode);'></td></tr></table></div>");

		}
		
		//log.info("Genetated sub form "+subFormHtml.toString());
		
		return subFormHtml.toString();
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<Form> getAllFormList() throws EazyBpmException{
		return formDAO.getForm();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> getDynamicFormsByIds(List<String> ids)throws EazyBpmException{
		return formDAO.getDynamicFormsByIds(ids);
	}
    
	/**
	 * {@inheritDoc}
	 */
	public Form getFormById(String formId) throws BpmException{
		return formDAO.getFormById(formId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public MetaForm getDynamicFormById(String formId) throws BpmException{
		return formDAO.getDynamicFormById(formId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<MetaForm> showAllFormVersions(String formName,String isEdit,String isSystemModule) throws EazyBpmException {
		
		List<MetaForm> metaFormList=formDAO.getAllFormVersions(formName);
		for(MetaForm metaForm:metaFormList){
			User userObj=userManager.get(metaForm.getCreatedBy());
			metaForm.setCreatedByFullName(userObj.getFullName());
			metaForm.setIsEdit(isEdit);
			metaForm.setIsSystemModule(isSystemModule);
		}
		return metaFormList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteAllFromVersions(String formName) throws EazyBpmException{
		boolean status=false;
		StringBuffer ids = new StringBuffer();
		List<MetaForm> metaFormList=formDAO.getFormByFormName(formName);
		
		ids.append("(");
		for(MetaForm metaFrm:metaFormList){
			 ids.append("'"+metaFrm.getId()+"',");
		}
		
		if(ids.lastIndexOf(",")>0){
			 ids.deleteCharAt(ids.lastIndexOf(","));
		}
		ids.append(")");
		
		if(!checkFormIdInProcess(ids.toString())){
			for(MetaForm metaFrm:metaFormList){
				 Set<Module> modules = metaFrm.getModules();
				//Remove form relation from module before delete
				try {
					if(!modules.isEmpty()){
						metaFrm.getModules().removeAll(modules);
						metaFrm=formDAO.saveDynamicForm(metaFrm);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			formDAO.deleteAllVersionOfDynamicForm(metaFormList);
			status=true;
		}
		
		return status;
	}
	
	
	
	/**
	 * To check the form is mapped in process or not 
	 * @param formId
	 * @return
	 */
	private boolean checkFormIdInProcess(String formId){
		boolean isFormUsedInProcess=false;
		List<ProcessFormName> processFormNameList=formDAO.selectProcessKeyByFormId(formId);
		
		if(!processFormNameList.isEmpty()){
			isFormUsedInProcess=true;
		}
		
		return isFormUsedInProcess;
	}
	/*public boolean checkVersionIsActive(String formId) {
		boolean isFormVersionIsActive = false;
		MetaForm form = formDAO.selectFormVersionIsActiveByFormId(formId);
		
		return true;
	}*/
	/**
	 * {@inheritDoc}
	 */
	public boolean copyFormsToModule(String moduleId,List<String> formIdList,String fromName) throws EazyBpmException{
		User user = CommonUtil.getLoggedInUser();
		int count=0;
		List<MetaForm> metaForms=formDAO.getDynamicFormsByIds(formIdList);
		
		//Get module
		Set<Module> modules=new HashSet<Module>();
		Module module=moduleService.getModule(moduleId);
		modules.add(module);
	   	
		for(MetaForm metaFormObj:metaForms){
			MetaForm metaForm=metaFormObj.clone(); 
			String formOldName=metaForm.getFormName();
			String metaFormHtmlSource=metaForm.getHtmlSource();
			String metaFormXmlSource=metaForm.getXmlSource();
			
			metaForm.setFormName(fromName);
			metaForm.setVersion(1);
			metaForm.setModules(modules);
			metaForm.setCreatedBy(user.getId());
			metaForm.setCreatedOn(new Date());
			metaForm.setActive(true);
			
			//replacing the form names
			metaFormHtmlSource=metaFormHtmlSource.replace("setDefaultValue('"+formOldName+"')" , "setDefaultValue('"+fromName+"')");
			metaFormHtmlSource=metaFormHtmlSource.replaceAll("action=\"#\" id=\""+formOldName+"\"", "action=\"#\" id=\""+fromName+"\"");
			metaFormHtmlSource=metaFormHtmlSource.replaceAll("method=\"post\" name=\""+formOldName+"\"", "method=\"post\" name=\""+fromName+"\"");
			metaFormXmlSource=metaFormXmlSource.replace("'"+formOldName+"_" , "'"+fromName+"_");
			metaFormXmlSource=metaFormXmlSource.replace("initiator = '"+formOldName+"' id='"+formOldName+"' name='"+formOldName+"' action='#' label='"+formOldName+"'", "initiator = '"+fromName+"' id='"+fromName+"' name='"+fromName+"' action='#' label='"+fromName+"'");
			
			metaForm.setXmlSource(metaFormXmlSource);
			metaForm.setHtmlSource(metaFormHtmlSource);
			
			metaForm=formDAO.saveDynamicForm(metaForm);
			//saving the form name for process mapping 
			FormNameMappings formNameMappings=new FormNameMappings();
			formNameMappings.setFormId(metaForm.getId());
			formNameMappings.setFormName(metaForm.getFormName());
			formDAO.saveFormNameMappings(formNameMappings);
			count++;
		}
		
		if(formIdList.size()==count){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MetaForm getMetaFormByFormName(String formName) throws BpmException {
		return formDAO.getMetaFormByFormName(formName);
	}
	
	public List<MetaForm> getAllFormByFormName(String formName) throws Exception {
		return formDAO.getAllFormVersions(formName);
	}
	
	public List<LabelValue> getAllProcessNameByFormId(String formId) throws Exception{
		return formDAO.getAllProcessNameByFormId(formId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MetaForm saveFormPrintTemplate(String formId, String printTemplate) throws EazyBpmException{
		MetaForm metaForm = formDAO.getDynamicFormById(formId);
		metaForm.setPrintTemplate(printTemplate);
		return formDAO.saveDynamicForm(metaForm);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 * @throws ResourceNotFoundException 
	 * @throws MethodInvocationException 
	 * @throws ParseErrorException 
	 * @throws JDOMException 
	 * @throws JSONException 
	 */
	public String getContentForPrintTemplate(String formId, Map<String, Object> context, VelocityEngine velocityEngine,String pastValues, String subFormValues) throws EazyBpmException, ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException, JDOMException, JSONException{
	//	MetaForm metaForm = formDAO.getDynamicFormById(formId);
		String templateName = "Print Template";
		StringWriter writer = new StringWriter();
		//To get the disable field names
		MetaForm form = getDynamicFormById(formId);
		List<Element> fromPropertyEls=JdomXmlParserUtil.getFromElementPropertyForBpmn(form.getXmlSource());
		for (Element fromPropertyEl : fromPropertyEls) {
			String fieldType=(String)fromPropertyEl.getAttributeValue("type");
			if(!fieldType.equals("hidden")){
				String filedNames=fromPropertyEl.getAttributeValue("name");
				if(!context.containsKey(filedNames)){
					context.put(filedNames, "");
				}
			}
		}

		String templateForm = String.valueOf(form.getPrintTemplate());
		StringBuffer subformTemplate = new StringBuffer(templateForm) ;
	//	JSONObject jsonFieldsPersmission = new JSONObject(pastValues);

		if(!subFormValues.isEmpty()) {
		int dataCount = Integer.valueOf(context.get("tdCount").toString());
			//subformTemplate.append("<table border=1>");
			String[] subForm = subFormValues.split(",");
			int i = 0 ;
			for(String subFormVal : subForm) {
				if(i == 0) {
					subformTemplate.append("<tr>");
				}
				subformTemplate.append("<td>${"+subFormVal.split(":")[0]+"}</td>");
				i++;
				//System.out.println("=======count=========="+i+"============"+subFormVal+"========"+subFormVal.split(":")[0]);
				if(i == dataCount) {
					subformTemplate.append("</tr>");
					i = 0;
				}
				context.put(subFormVal.split(":")[0], subFormVal.split(":")[1] );
			}
			subformTemplate.append("</table>");
		}
		
		VelocityContext contextDetails = new VelocityContext(context);
		//System.out.println("===="+subformTemplate);
		if(form != null && form.getPrintTemplate() != null && !form.getPrintTemplate().isEmpty()){
			velocityEngine.evaluate(contextDetails, writer, templateName, subformTemplate.toString());
		}
		return writer.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteFormsByNames(List<String> formNameList)throws EazyBpmException{
		boolean status=false;
		 StringBuffer formNames = new StringBuffer();
		 formNames.append("(");
		 for(String formName:formNameList){
			 formNames.append("'"+formName+"',");
		 }
		 
		 if(formNames.lastIndexOf(",")>0){
			 formNames.deleteCharAt(formNames.lastIndexOf(","));
		}
		 formNames.append(")");
		
		 //Getting the form objects
		 List<MetaForm> formObjs=getFormDetailsByNames(formNames.toString());
		 
		 StringBuffer ids = new StringBuffer();
		 ids.append("(");
		 for(MetaForm metaFrm:formObjs){
			 ids.append("'"+metaFrm.getId()+"',");
		 }
		
		 if(ids.lastIndexOf(",")>0){
			 ids.deleteCharAt(ids.lastIndexOf(","));
		 }
		 ids.append(")");
		 
		 if(!checkFormIdInProcess(ids.toString())){
			 for(MetaForm metaForm:formObjs){
				 Set<Module> modules = metaForm.getModules();
					if(modules!=null){
						metaForm.getModules().removeAll(modules);
						metaForm=formDAO.saveDynamicForm(metaForm);
					}
			 }
			 
			 formDAO.deleteFormsByNames(formNames.toString());
			 status=true;
		 }
		return status;
	}
	
	
	
	/**
	 * <p> To delete form from version by Ids </p>
	 * @param formIds
	 * @return Status
	 */
	public boolean deleteFormByIds(List<String> formIds) throws EazyBpmException{
		boolean status=false;
		for(String formId : formIds) {
			if(!checkFormIdInProcess("('"+formId+"')")) {
				MetaForm metaForm=formDAO.getDynamicFormById(formId);
				Set<Module> modules = metaForm.getModules();
				if(modules!=null){
					metaForm.getModules().removeAll(modules);
					metaForm=formDAO.saveDynamicForm(metaForm);
				}
				if(!metaForm.isActive()) {
					formDAO.deleteFormById(formId);
					status=true;
				}
				
			} 
		} 
		 return status;
}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteFormById(String formId) throws EazyBpmException{
		boolean status=false;
		if(!checkFormIdInProcess("('"+formId+"')")){
			MetaForm metaForm = formDAO.getDynamicFormById(formId);
			Set<Module> modules = metaForm.getModules();
			if(modules != null){
				metaForm.getModules().removeAll(modules);
				metaForm=formDAO.saveDynamicForm(metaForm);
			}
			formDAO.deleteFormById(formId);
			status=true;
		}
		
		return status;
	}
	/**
	 * {@inheritDoc}
	 */
	public void deleteFormNameByProcessKey(String keys)throws BpmException{
		formDAO.deleteFormNameByProcessKey(keys);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFormMappedInProcess(String formId)throws BpmException{
		List<ProcessFormName> mappedForm=formDAO.selectProcessKeyByFormId("('"+formId+"')");
		if(mappedForm!=null && !mappedForm.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	@Autowired
	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	@Autowired
	public void setFormDAO(MetaFormDao formDAO) {
		this.formDAO = formDAO;
	}
	
	@Autowired
	public void setUserService(UserManager userManager) {
		this.userManager = userManager;
	}

	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Map<String, String>> searchModuleFromNames(String fromName) throws Exception{
		List<Map<String, String>> fromNameList= new ArrayList<Map<String,String>>();
		List<Module> userModule = moduleService.getAllModuleList();
		// Get user modules
		for(Module moduleObj:userModule){
			Set<MetaForm> metaFroms= moduleObj.getForms();
			for(MetaForm fromObj:metaFroms){
				if(fromObj.isActive()){
					Map<String, String> fromNameMap = new HashMap<String, String>();
					String tempFromName = fromObj.getFormName();
					if(fromName.length() <= tempFromName.length()){
						String tableSubString = tempFromName.substring(0, fromName.length());
						if(fromName.equalsIgnoreCase(tableSubString)){
							fromNameMap.put("dataName",fromObj.getFormName());
							fromNameMap.put("id",fromObj.getId()+"_"+moduleObj.getId()+"AllForm");
							fromNameList.add(fromNameMap);
						}
					}
				}
			}
		}
		return fromNameList;
	}

	@Override
	public void deployProcessForForm(MetaForm metaform ,ServletContext servletContext)	throws Exception {
	        String xmlData = new String(CommonUtil.getByteArry(rtProcessService.getResourceAsStream("55841", "Default Process.bpmn20.xml")));
	        String formXml = metaform.getXmlSource();
	        
	        formXml = formXml.substring(formXml.indexOf("<extensionElements>"), formXml.indexOf("</extensionElements>"));
	        
			formXml = formXml.replaceAll("<extensionElements>", "");
			formXml = formXml.replaceAll("<formProperty", "<activiti:formProperty xmlns:activiti=\"http://activiti.org/bpmn\"");
			formXml = formXml.replaceAll("></formProperty>", "/>");

			System.out.println(formXml);

			System.out.println("=========id========="+metaform.getId());
	        xmlData = xmlData.replaceAll("form=\"\"", "form=\""+metaform.getId()+"\"" );
	        System.out.println("============new========"+xmlData); 
	        xmlData = xmlData.replaceAll("formName=\"\"", "formName=\""+metaform.getFormName()+"\"");
	        xmlData = xmlData.replaceAll("Default Process", metaform.getFormName());
	        xmlData = xmlData.replaceAll("<extensionElements>", "<extensionElements>"+formXml);
	        
	        System.out.println(xmlData); 

			// the directory to upload to
					String uploadDir = servletContext.getRealPath("/resources") + "/" + CommonUtil.getLoggedInUserId() + "/";
					File dirPath = new File(uploadDir);
					// Create the directory if it doesn't exist
					if (!dirPath.exists()) {
						dirPath.mkdirs();
					}
					String fileNameWithPath = uploadDir + metaform.getFormName() + ".bpmn20.xml";
					FileWriter fr = new FileWriter(new File(fileNameWithPath));
					Writer br = new BufferedWriter(fr);
					br.write(xmlData);
					br.close();
			processDefinitionService.deployBPMNSourceFile(null,metaform.getFormName()+".bpmn20.xml", fileNameWithPath,"System Defined Default Process","Office Management", true);
			
	}
}