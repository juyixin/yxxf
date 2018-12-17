package com.eazytec.bpm.metadata.form.dao;

import java.util.List;
import java.util.Set;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Form;
import com.eazytec.model.FormButton;
import com.eazytec.model.FormNameMappings;
import com.eazytec.model.Forms;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Module;
import com.eazytec.model.ProcessFormName;
import com.eazytec.model.ProcessPermission;


/**
 * <p>
 * Performs all the CRUD functionalities related to the form entity.
 * </p>
 * @author Karthick
 * @author Megala
 */

public interface MetaFormDao extends GenericDao<Form, String> {
	
	Form saveForm(Form form);
	/**
	 * Save dynamic form
	 * @param metaForm
	 * @return
	 */
	MetaForm saveDynamicForm(MetaForm metaForm);
	
	/**
	 * Update dynamic form
	 * @param metaForm
	 * @return
	 */
	MetaForm updateDynamicForm(MetaForm metaForm);
	
	FormNameMappings saveFormNameMappings(FormNameMappings metaForm);
	
	/**
	 * Save user-defined form button
	 * @param formButton
	 * @return
	 * @throws BpmException
	 */
	FormButton saveFormButton(FormButton formButton) throws BpmException;
	
	/**
	 * delete user-defined form button
	 * @param keys
	 * @throws BpmException
	 */
	void deleteFormButton(String formButtonId) throws EazyBpmException;

	
	/**
	 * Get all user defined form buttons
	 * @return
	 * @throws BpmException
	 */
	List<FormButton> getFormButtons() throws BpmException;
	
	List<Form> getForm() throws BpmException;

	Form getFormById(String formId) throws BpmException;
	
	MetaForm getDynamicFormById(String formId) throws BpmException;
	
	/**
	 * <p>Get all versions for a form by using formName </p> 
	 * @param formName
	 *        name of the form to be fetched.
	 * @return
	 *  	returns list of forms of previous versions.
	 * @throws EazyBpmException
	 */
	public List<MetaForm> getAllFormVersions(String formName) throws EazyBpmException;
	
	
	/**
	 * @return returns all dynamic forms
	 *  
	 */
	public List<MetaForm> getAllDynamicForms() throws EazyBpmException;
	/**
	 * <P>Get all form by formName</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	public List<MetaForm> getFormByFormName(String formName) throws EazyBpmException;
	
	/**
	 * <P>Update form status by formName</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	void updateFromStatusByFormName(String formName)throws EazyBpmException;
	
	/**
	 * <P>Update metaform status by formName</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	void updateFromStatusByDynamicFormName(String formName)throws EazyBpmException;
	
	/**
	 * <P>Update form status by formId</P>
	 * @param formId
	 * @throws EazyBpmException
	 */
	void updateFromStatusById(String formId)throws EazyBpmException;
	
	/**
	 * Returns all the latest version of forms.
	 * @return list of forms.
	 */
	public List<Form> getAllLatestVersion() throws EazyBpmException;
	
	public List<MetaForm> getAllLatestVersionForms() throws EazyBpmException;
	/**
	 * Get All latest version of forms for given id
	 * @param moduleId
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaForm> getAllLatestVersionFormsForModule(List<String> formListIds,boolean isTemplateView) throws EazyBpmException;
	
	/**
	 * Returns all the latest version of template forms.
	 * @return list of forms.
	 */
	public List<MetaForm> getAllLatestVersionTemplateForms() throws EazyBpmException;
	
	/**
	 * Get active form as label value pair
	 */
	public List<LabelValue> getAllLatestVersionFormNames() throws EazyBpmException;
	
	/**
	 * Get child table form names as label value pair
	 */
	public List<LabelValue> getAllChildTableFormNames(String tableId) throws EazyBpmException;
	
	/**
	 * Returns latest version of form.
	 * @return version number.
	 */
	Integer getLatestVersionByForm(String formName) throws EazyBpmException;
	
	/**
	 * Returns latest version of form.
	 * @return version number.
	 */
	Integer getLatestVersionByDynamicForm(String formName) throws EazyBpmException;
	
	/**
	 * <P>To delete the form design by its name</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	void deleteAllFromVersions(String formName) throws EazyBpmException;
	
	
	/**
	 * <P>To delete the form design by its name</P>
	 * @param formName
	 * @throws EazyBpmException
	 */
	void deleteFormById(String formId) throws EazyBpmException;
	
	List<MetaForm> getDynamicFormsByIds(List<String> ids)throws EazyBpmException;
	
	/**
	 * Get all Meta form for organization tree expect public forms
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaForm> getAllMetaFormForOrgTree() throws EazyBpmException;

	/**
	 * Get All MetaForm list as LabelValue bean
	 * @return
	 * @throws BpmException
	 */
	List<LabelValue> getMetaFormAsLabelValue() throws BpmException;
	
	/**
	 * Retrieves the forms that were associated to the modules
	 * @param moduleList
	 * @return forms
	 * @throws EazyBpmException
	 */
	List<MetaForm> getAllLatestVersionFormsInModules(List<Module>moduleList,String loggedInUser) throws EazyBpmException;
	
	/**
	 * Retrieves the template forms that were associated to the modules
	 * @param moduleList
	 * @return forms
	 * @throws EazyBpmException
	 */
	List<MetaForm> getAllLatestVersionTemplateFormsInModules(List<Module>moduleList,String loggedInUser) throws EazyBpmException;
	
	/**
	 * Retrieves the forms that were associated to the modules
	 * @param moduleList
	 * @return forms
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllLatestVersionFormNamesInModules(List<Module>moduleList,String loggedInUser) throws EazyBpmException;
	
	/**
	 * Delete All version of metaform
	 * @param metaForms
	 * @throws BpmException
	 */
	void deleteAllVersionOfDynamicForm(List<MetaForm> metaForms) throws BpmException;
	
	/**
	 * 
	 * @returns all forms that are not marked as public 
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getAllNonPublicFormsByJspForm(String isJspForm) throws EazyBpmException;
	
	/**
	 * 
	 * @returns all forms that are not marked as public 
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getAllNonPublicForms() throws EazyBpmException;
	
	/**
	 * 
	 * @returns all forms that are not marked as public 
	 * @throws EazyBpmException
	 */
	public LabelValue getFormLabelValueById(String formId) throws EazyBpmException;
	
	/**
	 * 
	 * Gets latest for
	 * @param formId
	 * @return
	 * @throws EazyBpmException
	 */
	public List<MetaForm> getFormVersionByFormId(String formId) throws EazyBpmException;
	
	/**
	 * Getting Form Name Mappings by form id
	 * @param formId
	 * @return
	 * @throws BpmException
	 */
    FormNameMappings getFormNameMappingsByFormId(String formId) throws BpmException;
    
	/**
	 * To get the form by the names
	 * @param formNames
	 * @return
	 * @throws EazyBpmException
	 */
    List<MetaForm> getFormDetailsByNames(String formNames)throws EazyBpmException;
    
    /**
	 * To delete the forms by names
	 * @param formNameList
	 * @throws EazyBpmException
	 */
    void deleteFormsByNames(String formNames)throws EazyBpmException;
    
    /**
     * To save the process key and form names
     * @param processFormNameList
     * @throws EazyBpmException
     */
    void saveProcessAndFormName(List<ProcessFormName> processFormNameList)throws EazyBpmException;
    
    /**
	 * Delete form by process
	 * @param keys
	 * @throws BpmException
	 */
    void deleteFormNameByProcessKey(String keys)throws EazyBpmException;
    
    /**
     * Get the list of process key
     * @param formId
     * @return
     * @throws EazyBpmException
     */
    List<ProcessFormName> selectProcessKeyByFormId(String formId)throws EazyBpmException;
    
    /**
     * get Meta form by form name
     * 
     * @param formName
     * @return
     * @throws BpmException
     */
    MetaForm getMetaFormByFormName(String formName) throws BpmException;
    
    List<LabelValue> getAllProcessNameByFormId(String formId) throws Exception;
    
    public List<MetaForm> getAllTemplateFormsByModuleList(List<Module> moduleList) throws BpmException;
    
	public boolean deleteFormByIds(List<String> formIds) throws EazyBpmException;
	
	void saveProcessPermission(List<ProcessPermission> processPermissions);
	Forms saveDynamicHistoryForm(Forms forms);
	void updateHistoricForms(String formName);
	
	

}
	
