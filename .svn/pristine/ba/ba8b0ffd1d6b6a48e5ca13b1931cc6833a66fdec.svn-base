package com.eazytec.bpm.metadata.form.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.xml.sax.SAXException;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Form;
import com.eazytec.model.FormButton;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;

/**
 * <p>The definition calls for form related functionalities like form designer, CRUD for form entities
 * and all service calls related to form specific operations in the bpm</p>
 * 
 * @author madan
 * @author Karthick
 *
 */

public interface FormDefinitionService {
	
	/**
	 * <p>Retrieves all data that is enough to render a form when starting a process.</p>
	 * @param processDefinitionId for the process that is going to start
	 * @return the form data
	 */
	StartFormData getStartFormData(String processDefinitionId);
	
	/**
	 * Gets the form entity associated with a particular task
	 * @param taskId for the task
	 * @return the form data entity
	 */
	TaskFormData getTaskFormData(String taskId);
	
	TaskFormData getHistoricTaskFormData(HistoricTaskInstance task);
	
	Form saveForm(Form form) throws BpmException;
	
	MetaForm saveDynamicForm(MetaForm metaForm,String moduleId)throws BpmException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Save user-defined form button
	 * @param formButton
	 * @return
	 * @throws BpmException
	 */
	FormButton saveFormButton(FormButton formButton) throws BpmException;
	
	/**
	 * <P>Delete user-defined form button</P>
	 * @param formButtonId
	 * @throws EazyBpmException
	 */
	boolean deleteFormButton(String formButtonId) throws EazyBpmException;
	
	/**
	 * Get all user defined form buttons
	 * @return
	 * @throws BpmException
	 */
	List<FormButton> getFormButtons() throws BpmException;
	
	
	List<Form> getForm() throws BpmException;
	
	/**
	 * <p>
	 * Get list of form field attribute
	 * 	- Using the formId we will get the form xml, Parse source xml and get form properties. 
	 * 	  as list of Map(id,name)
	 * </p>
	 * @param  form Id
	 *      id of the form to be fetched 
	 * @return List of form attribute map (id,name)
	 * @throws BpmException
	 */
	List<Map<String,String>> getFields(String formId) throws EazyBpmException,JDOMException,IOException;
	
	/**
	 * <p>
	 * Get list of form field attribute with xmlElement
	 * 	- Using the formId we will get the form xml, Parse source xml and get form properties. 
	 * 	  as list of Map(id,name)
	 * </p>
	 * @param  form Id
	 *      id of the form to be fetched 
	 * @return List of form attribute map (id,name)
	 * @throws BpmException
	 */
	List<Map<String,String>> getFieldsWithXMLElement(String formId) throws EazyBpmException,JDOMException,IOException;
	
	/**
	 * 
	 * @param formId
	 * @return
	 * @throws BpmException
	 */
	Form getFormById(String formId) throws BpmException;
	
	List<LabelValue> getFormsLabelValueByIds(String formId)throws EazyBpmException ;
	
	MetaForm getDynamicFormById(String formId) throws BpmException;
	
	/**
	 * <p>Fetches all versions of forms for the given form id </p>
	 * @param  form Id
	 *      id of the form to be fetched 
	 * @return
	 * 		List of forms of previous versions
	 */
	public List<MetaForm> showAllFormVersions(String formName,String isEdit,String isSystemModule) throws EazyBpmException;
	
	
	/**
	 * <P>Check the given form name is present</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	boolean isFormNamePresent(String formName) throws EazyBpmException;
	
	/**
	 * <P>Restore the selected form is active</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	void restoreForm(String formId, String formName) throws EazyBpmException;
	
	/**
	 * <P>To delete the form design by its name</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	boolean deleteAllFromVersions(String formName) throws EazyBpmException;
	
	/**
	 * <P>To delete the form design by its id</P>
	 * @param formId
	 * @throws EazyBpmException
	 */
	boolean deleteFormById(String formId) throws EazyBpmException;
	
	/**
	 * To parser the Bpmnxml by DOM parser
	 * @param bpmnXML
	 * @return
	 * @throws BpmException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws JDOMException 
	 * @throws Exception 
	 */
	String parseXmlByDomParser(String bpmnXML,String processName,String svgDOM, String jsonData,boolean isProcessCreate) throws EazyBpmException, JDOMException, IOException, Exception;
	
	List<Map<String,Object>> resolveFormDetailsListMap(List<Map<String,Object>> formDetailsListMap) throws BpmException;
	
	/**
	 * Returns list of dynamic forms
	 * @return
	 * @throws BpmException
	 */
	List<MetaForm> getAllDynamicForms() throws BpmException;
	
	/**
	 * Return list of dynamic forms for given id
	 * @param moduleId
	 * @param isJspForm TODO
	 * @return
	 * @throws BpmException
	 */
	List<MetaForm> getAllDynamicFormsForModule(String moduleId,String isEdit,String isSystemModule, boolean isJspForm,boolean isTemplateView) throws BpmException;
	
	/**
	 * Returns list of Template forms
	 * @return
	 * @throws BpmException
	 */
	List<MetaForm> getAllTemplateForms() throws BpmException;
	
	/**
	 * Return list of metaform expect public forms
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaForm> getAllMetaFormForOrgTree() throws BpmException;
	
	/**
	 * Return All MetaForm Label value bean
	 * @return
	 * @throws BpmException
	 */
	List<LabelValue> getMetaFormAsLabelValue() throws BpmException;
	
	/**
	 * Get all active forms as label value pair 
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllDynamicFormNames() throws EazyBpmException;
	
	/**
	 * To get the form by the names
	 * @param formNames
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaForm> getFormDetailsByNames(String formNames) throws EazyBpmException;
	
	/**
	 * Get all child table form names as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllChildTableFormNames(String tableId) throws EazyBpmException;
	
	List<Form> getAllFormList() throws EazyBpmException;
	
	List<MetaForm> getDynamicFormsByIds(List<String> ids) throws EazyBpmException;
	
	/**
	 * Returns html content for the sub form 
	 * @return
	 * @throws BpmException
	 */
	String generateSubFormHtml(List<Map<String,Object>> fieldProperties,String subFormName)throws EazyBpmException;
	
	/**
	 * Returns all non public forms 
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getAllNonPublicForms() throws EazyBpmException;
	
	/**
	 * Returns all non public forms 
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getAllNonPublicFormsByJspForm(String isJspForm) throws EazyBpmException;
	
	/**
	 * Returns labelValue 
	 * @return
	 * @throws EazyBpmException
	 */
	public LabelValue getFormLabelValueById(String formId) throws EazyBpmException;
	
	/**
	 * Gets the latest version of the form by its id
	 * @return
	 * 		Latest form version id and name
	 * @throws EazyBpmException
	 */
	public List<MetaForm> getFormVersionByFormId(String formId) throws EazyBpmException;
	
	/**
	 * To copy the forms to modules
	 * @param moduleId
	 * @param formIdList
	 * @return
	 * @throws EazyBpmException
	 */
	boolean copyFormsToModule(String moduleId,List<String> formIdList,String formName) throws EazyBpmException;
	
	/**
	 * Save form print template for form id
	 * @param formId
	 * @param printTemplate
	 * @return
	 * @throws EazyBpmException
	 */
	MetaForm saveFormPrintTemplate(String formId, String printTemplate) throws EazyBpmException;
	
	/**
	 * Get html content for template
	 * @param formId
	 * @return
	 * @throws EazyBpmException
	 * @throws JSONException 
	 */
	String getContentForPrintTemplate(String formId, Map<String, Object> context, VelocityEngine velocityEngine,String pastValues, String subFormAttrValues) throws EazyBpmException, ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException,JDOMException, JSONException;
	
	/**
	 * To delete the forms by names
	 * @param formNameList
	 * @throws EazyBpmException
	 */
	boolean deleteFormsByNames(List<String> formNameList)throws EazyBpmException;
	
	/**
	 * Delete form by process
	 * @param keys
	 * @throws BpmException
	 */
	void deleteFormNameByProcessKey(String keys)throws BpmException;
	
	/**
	 * To check the form is mapped in process
	 * @param formId
	 * @return
	 * @throws BpmException
	 */
	boolean isFormMappedInProcess(String formId)throws BpmException;
	
	/**
	 * get Meta Form by form name
	 * 
	 * @param formName
	 * @return
	 * @throws BpmException
	 */
	MetaForm getMetaFormByFormName(String formName) throws BpmException;
	
	List<LabelValue> getAllProcessNameByFormId(String formId) throws Exception;
	
	List<MetaForm> getAllFormByFormName(String formName) throws Exception;
	
	/**
	 * <p> To delete forms from form version by ids </p>
	 * @param formIds
	 * @return boolean
	 * @throws EazyBpmException
	 */
	public boolean deleteFormByIds(List<String> formIds) throws EazyBpmException;
	
	/**
	 * <p> To set process permission </p> 
	 * @param bpmnXml
	 * @param processName TODO
	 * @return String
	 * @throws Exception
	 */
	public void setProcessPermission(String bpmnXml, String processName) throws Exception;

	List<Map<String, String>> searchModuleFromNames(String fromName) throws Exception;

	void deployProcessForForm(MetaForm metaform, ServletContext servletContext) throws Exception;

}
