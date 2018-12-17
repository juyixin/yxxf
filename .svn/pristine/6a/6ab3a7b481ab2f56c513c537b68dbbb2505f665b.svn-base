package com.eazytec.bpm.metadata.form.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.AbstractQuery;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.metadata.form.dao.MetaFormDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Folder;
import com.eazytec.model.Form;
import com.eazytec.model.FormButton;
import com.eazytec.model.FormNameMappings;
import com.eazytec.model.Forms;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Module;
import com.eazytec.model.ProcessFormName;
import com.eazytec.model.ProcessPermission;
import com.eazytec.model.Role;

/**
 *
 * @author karthick
 *
 */
@Repository("metaFormDao")
public class MetaFormDaoImpl extends GenericDaoHibernate<Form, String> implements MetaFormDao{

    /**
     * Constructor to create a Generics-based version using Group as the entity
     */
    public MetaFormDaoImpl() {
        super(Form.class);
    }

    /**
     * {@inheritDoc}
     */
    public Form saveForm(Form form){
        if (log.isDebugEnabled()) {
            log.debug("form's id: " + form.getId());
        }
        getSession().save(form);
        getSession().flush();
        return form;
    }
    
    /**
     * {@inheritDoc}
     */
    public MetaForm saveDynamicForm(MetaForm metaForm){
    	if (log.isDebugEnabled()) {
            log.debug("form's id: " + metaForm.getId());
        }
        getSession().save(metaForm);
        getSession().flush();
        return metaForm;
    }
    public Forms saveDynamicHistoryForm(Forms metaForm){
    	if (log.isDebugEnabled()) {
            log.debug("form's id: " + metaForm.getId());
        }
        getSession().save(metaForm);
        getSession().flush();
        return metaForm;
    }
    /**
     * {@inheritDoc}
     */
    public MetaForm updateDynamicForm(MetaForm metaForm){
    	if (log.isDebugEnabled()) {
            log.debug("form's id: " + metaForm.getId());
        }
    	getSession().update(metaForm);
        getSession().flush();
        return metaForm;
    }
    
    public FormNameMappings saveFormNameMappings(FormNameMappings formMappingObj){
    	if (log.isDebugEnabled()) {
            log.debug("form's id: " + formMappingObj.getId());
        }
        getSession().save(formMappingObj);
        getSession().flush();
        return formMappingObj;
    }
    
    /**
     * {@inheritDoc}
     */
    public FormButton saveFormButton(FormButton formButton){
        if (log.isDebugEnabled()) {
            log.debug("form button's id: " + formButton.getId());
        }
        getSession().saveOrUpdate(formButton);
        getSession().flush();
        return formButton;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void deleteFormButton(String formButtonId) throws EazyBpmException{
        Query query = getSession().createQuery("DELETE FROM FormButton WHERE id ='"+formButtonId+"'");
        int result = query.executeUpdate();
        if(result==0){
            throw new EazyBpmException("Failed in deleting Form "+formButtonId);
        }
    }


    /**
     * {@inheritDoc}
     */
    public List<FormButton> getFormButtons() throws BpmException{
        List<FormButton> formButtons = getSession().createCriteria(FormButton.class).list();
        if (formButtons.isEmpty()) {
            return null;
        } else {
            return formButtons;
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Form> getForm() throws EazyBpmException{
        List<Form> forms = getSession().createCriteria(Form.class).list();
        if (forms.isEmpty()) {
            return null;
        } else {
            return forms;
        }
    }


    /**
     * {@inheritDoc}
     */
    public Form getFormById(String formId) throws EazyBpmException{

        List<Form> form = getSession().createCriteria(Form.class)
                .add(Restrictions.eq("id", formId)).list();
        if (!form.isEmpty()) {
            return (Form) form.get(0);
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public MetaForm getMetaFormByFormName(String formName) throws BpmException {
    	List<MetaForm> metaForm = getSession().createCriteria(MetaForm.class).add(Restrictions.eq("formName", formName)).list();
    	if(metaForm != null && metaForm.size() > 0){
    		return metaForm.get(0);
    	} else {
    		return null;
    	}
    }
    
    public List<LabelValue> getAllProcessNameByFormId(String formId) throws Exception{
    	List<LabelValue> processNameList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(processFormName.processKey as name,processFormName.id as id) from ProcessFormName as processFormName where processFormName.formId in ("+formId+")")
				.list();
    	if(processNameList != null){
    		return processNameList;
    	} else {
    		return null;
    	}
         
    }
    
    /**
     * {@inheritDoc}
     */
    public MetaForm getDynamicFormById(String formId) throws BpmException{

        List<MetaForm> metaForm = getSession().createCriteria(MetaForm.class)
                .add(Restrictions.eq("id", formId)).list();
        if (!metaForm.isEmpty()) {
            return (MetaForm) metaForm.get(0);
            
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<MetaForm> getAllFormVersions(String formName) throws EazyBpmException {

       /* List<MetaForm> forms = getSession().createCriteria(MetaForm.class)
                .add(Restrictions.eq("formName", formName)).addOrder(Order.desc("version")).list();
        return forms;*/
    	Query query = getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.formName = '"+formName+"' and form.isDelete = 0 order by form.version desc");
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
    public List<MetaForm> getFormByFormName(String formName) throws EazyBpmException {

        List<MetaForm> forms = getSession().createCriteria(MetaForm.class)
                .add(Restrictions.eq("formName", formName)).list();
        return forms;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void updateFromStatusByFormName(String formName) throws EazyBpmException{
        Query query = getSession().createQuery("UPDATE MetaForm set active = 0 WHERE formName='"+formName+"'");
        int result = query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void updateFromStatusByDynamicFormName(String formName) throws EazyBpmException{
        Query query = getSession().createQuery("UPDATE MetaForm set active = 0 WHERE formName='"+formName+"'");
        int result = query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
    public void updateHistoricForms(String formName) throws EazyBpmException{
        Query query = getSession().createQuery("UPDATE Forms set active = 0 WHERE formName='"+formName+"'");
        query.executeUpdate();
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void updateFromStatusById(String formId) throws EazyBpmException{
        Query query = getSession().createQuery("UPDATE MetaForm set active = 1 WHERE id='"+formId+"'");
        int result = query.executeUpdate();
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Form> getAllLatestVersion() throws EazyBpmException {
        //Query query = getSession().createQuery("SELECT form FROM Form AS form WHERE form.version = (SELECT MAX(subForm.version ) FROM Form AS subForm WHERE subForm.formName = form.formName and form.active = 1)");
        Query query = getSession().createQuery("SELECT form FROM Form AS form WHERE form.active = 1)");
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllLatestVersionForms() throws EazyBpmException {
        //Query query = getSession().createQuery("SELECT form FROM Form AS form WHERE form.version = (SELECT MAX(subForm.version ) FROM Form AS subForm WHERE subForm.formName = form.formName and form.active = 1)");
        //Query query = getSession().createQuery("SELECT form FROM Form AS form WHERE form.active = 1)");
    	Query query = getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.active = 1 ");
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllLatestVersionFormsForModule(List<String> formListIds,boolean isTemplateView) throws EazyBpmException {
    	//Query query = getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.active = 1 and form.module.id='"+moduleId+"' and form.isDelete = 0");
    	String condition="";
    	if(isTemplateView){
    		condition=" form.templateForm="+isTemplateView+" and ";
    	}
    	Query query = getSession().createQuery("SELECT form FROM MetaForm AS form WHERE "+condition+" form.id in (:formListIds)").setParameterList("formListIds", formListIds);
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllLatestVersionTemplateForms() throws EazyBpmException {
    	Query query = getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.active = 1 and form.templateForm = 1 and form.isDelete = 0");
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllTemplateFormsByModuleList(List<Module> moduleList) throws BpmException {
    	Set<MetaForm>forms = new HashSet<MetaForm>();
		/** GetForms assigned on module*/
    	forms.addAll(getSession().createQuery("SELECT form FROM MetaForm AS form join form.modules as module WHERE form.active = 1 and form.templateForm = 1 and form.isDelete = 0 and (module in (:list) or form.publicForm = 1) and form.templateForm = 1").setParameterList("list", moduleList).list());
    	if(forms != null && forms.size() > 0){
    		return new ArrayList<MetaForm>(forms);
    	} else {
    		return null;
    	}
    }
        
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllLatestVersionFormsInModules(List<Module>moduleList, String loggedInUser) throws EazyBpmException {
    	try{
    		Set<MetaForm>forms = new HashSet<MetaForm>();
       		/** GetForms assigned on module*/
    		forms.addAll(getSession().createQuery("SELECT form FROM MetaForm AS form join form.modules as module WHERE form.active = 1 and (module in (:list) or form.publicForm = 1)  ").setParameterList("list", moduleList).list());
    		
       		/** GetForms created by the logged in user*/
    		forms.addAll(getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.createdBy = '"+loggedInUser+"' ").list());
    		return new ArrayList<MetaForm>(forms);
    	}catch(Exception e){
    		throw new EazyBpmException("Problem retrieving forms from Database", e);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllLatestVersionTemplateFormsInModules(List<Module>moduleList, String loggedInUser) throws EazyBpmException {
    	try{		
    		Set<MetaForm>forms = new HashSet<MetaForm>();
       		/** GetForms assigned on module*/
    		forms.addAll(getSession().createQuery("SELECT form FROM MetaForm AS form join form.modules as module WHERE form.active = 1 and (module in (:list) or form.publicForm = 1) and form.templateForm = 1").setParameterList("list", moduleList).list());
    		
       		/** GetForms created by the logged in user*/
    		forms.addAll(getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.createdBy = '"+loggedInUser+"' ").list());
    		return new ArrayList<MetaForm>(forms);
    	}catch(Exception e){
    		throw new EazyBpmException("Problem retrieving forms from Database", e);
    	}
    	
    }
    
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllMetaFormForOrgTree() throws EazyBpmException {
    	Query query = getSession().createQuery("SELECT form FROM MetaForm AS form WHERE form.active = 1 and form.publicForm != 1");
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getMetaFormAsLabelValue() throws BpmException {
    	List<LabelValue> metaFormList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(metaForm.formName as name,metaForm.id as id) from MetaForm as metaForm")
				.list();
         
             return metaFormList;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MetaForm> getAllDynamicForms() throws EazyBpmException {
        Query query = getSession().createQuery("SELECT form FROM MetaForm AS form");
        return query.list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<LabelValue> getAllLatestVersionFormNames() throws EazyBpmException {
    	List<LabelValue> formList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) from MetaForm as form WHERE form.active = 1 and form.publicForm != 1").list();
    	if(formList.isEmpty()){ 
    		return null;
    	}else{
    		return formList;
    	}
    }
    
    /**
     * {@inheritDoc}
     */
	public List<LabelValue> getAllNonPublicFormsByJspForm(String isJspForm) throws EazyBpmException {
		List<LabelValue> formList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) from MetaForm as form WHERE form.active = 1 and form.publicForm != 1 and form.isJspForm = "+isJspForm+"")
				.list();
		return formList;
	}
	
	/**
     * {@inheritDoc}
     */
	public List<LabelValue> getAllNonPublicForms() throws EazyBpmException {
		List<LabelValue> formList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) from MetaForm as form WHERE form.active = 1 and form.publicForm != 1")
				.list();
		return formList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LabelValue getFormLabelValueById(String formId) throws EazyBpmException {
		List<LabelValue> form = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) from MetaForm as form WHERE form.id = '"+formId+"'").list();
		if(form != null){
			return form.get(0);
		} else{
			return null;
		}
		
	}
	
	/**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<LabelValue> getAllLatestVersionFormNamesInModules(List<Module>moduleList, String loggedInUser) throws EazyBpmException {
       	try{		
       		Set<LabelValue> forms = new HashSet<LabelValue>();
       		/** GetForms assigned on module*/
       		forms.addAll(getSession().createQuery("SELECT new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) FROM MetaForm AS form join form.modules as module WHERE form.active = 1 and (module in (:list) or form.publicForm = 1)").setParameterList("list", moduleList).list());
       		
       		/** GetForms created by the logged in user*/
       		forms.addAll(getSession().createQuery("SELECT new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) FROM MetaForm AS form WHERE form.createdBy = '"+loggedInUser+"' ").list());
       		return new ArrayList<LabelValue>(forms);
       	}catch(Exception e){
       		throw new EazyBpmException("Problem retrieving forms from Database", e);
       	} 
       	
       }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<LabelValue> getAllChildTableFormNames(String tableId) throws EazyBpmException {
    	List<LabelValue> formList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(form.formName as formName, form.id as id) from MetaTableRelation as metaTableRelation, MetaForm as form WHERE form.table = metaTableRelation.childTable and metaTableRelation.table = '"+tableId+"' and form.active= 1").list();
    	if(formList.isEmpty()){ 
    		return null;
    	}else{
    		return formList;
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer getLatestVersionByForm(String formName) throws EazyBpmException {
        Query query = getSession().createQuery("SELECT MAX(form.version) FROM Form AS form WHERE form.formName = '"+formName+"'");
        return Integer.valueOf(String.valueOf(query.list().get(0)));
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Integer getLatestVersionByDynamicForm(String formName) throws EazyBpmException {
        Query query = getSession().createQuery("SELECT MAX(form.version) FROM MetaForm AS form WHERE form.formName = '"+formName+"'");
        return Integer.valueOf(String.valueOf(query.list().get(0)));
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void deleteAllFromVersions(String formName) throws EazyBpmException{
        Query query = getSession().createQuery("DELETE FROM MetaForm WHERE formName='"+formName+"'");
        int result = query.executeUpdate();
        if(result==0){
            throw new EazyBpmException("Failed in deleting Form "+formName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void deleteFormById(String formId) throws EazyBpmException{
        Query query = getSession().createQuery("DELETE FROM MetaForm WHERE id='"+formId+"'");
        int result = query.executeUpdate();
        if(result==0){
            throw new EazyBpmException("Failed in deleting Form "+formId);
        }
    }
    
    public List<MetaForm> getDynamicFormsByIds(List<String> ids)throws EazyBpmException { 
    	log.info("Getting metaform object of : "+ids);
    	List<MetaForm> forms = getSession().createQuery("from MetaForm as form where form.id in (:list)").setParameterList("list", ids).list();
        if (forms.isEmpty()) {
            return null;
        } else {
            return forms;
        }
    }
    
    /**
	 * {saveFormAutoGenerationId}
	 */
	public Integer saveFormAutoGenerationId(String formAutoId, String formId) throws BpmException{
		Query query = getSession().createQuery("UPDATE MetaForm set autoGenerationId = '"+formAutoId+"' WHERE id='"+formId+"'");
		int result = query.executeUpdate();
		return result;
	}
	/**
	 * {saveFormAutoGenerationId}
	 */
	public void deleteAllVersionOfDynamicForm(List<MetaForm> metaForms) throws BpmException{
		for(MetaForm metaFom : metaForms){
			getSession().delete(metaFom);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public List<MetaForm> getFormVersionByFormId(String formId) throws EazyBpmException {
		
		Query query =  getSession()
				.createQuery(
						" from MetaForm as form WHERE form.id = ?");
		query.setParameter(0, formId);
		List<MetaForm> formList = (List<MetaForm>) query.list();
		
		return formList;
	}
	
    /**
     * {@inheritDoc}
     */
    public FormNameMappings getFormNameMappingsByFormId(String formId) throws BpmException{

		Query query =  getSession()
				.createQuery(
						" from FormNameMappings as formNameMappings WHERE formNameMappings.formId = ?");
		query.setParameter(0, formId);
		List<FormNameMappings> formNameMappingsList = (List<FormNameMappings>) query.list();
		if(formNameMappingsList.size() > 0){
			return formNameMappingsList.get(0);
		}
		return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<MetaForm> getFormDetailsByNames(String formNames)throws EazyBpmException{
    	Query query = getSession().createQuery("from MetaForm as form WHERE form.formName in "+formNames);
		getSession().flush();
		return query.list();
    }
    
    /**
     *  {@inheritDoc}
     */
    public void deleteFormsByNames(String formNames)throws EazyBpmException{
    	String formsByNames="DELETE FROM MetaForm as form  WHERE form.formName in "+formNames;
    	 Query query = getSession().createQuery(formsByNames);
    	 int result = query.executeUpdate();
    }
    
    /**
     *  {@inheritDoc}
     */
    public void saveProcessAndFormName(List<ProcessFormName> processFormNameList)throws EazyBpmException{
    	for(ProcessFormName processFormName:processFormNameList){
    		getSession().save(processFormName);
    		getSession().flush();
    	}
    }
    
    public void saveProcessPermission(List<ProcessPermission> permissionLists)throws EazyBpmException{
    	for(ProcessPermission permissionList:permissionLists){
    		getSession().saveOrUpdate(permissionList);
    		getSession().flush();
    	}
    }
    
    /**
     *  {@inheritDoc}
     */
    public void deleteFormNameByProcessKey(String keys)throws EazyBpmException{
    	String formsNames="DELETE FROM ProcessFormName as process  WHERE process.processKey in "+keys;
    	 Query query = getSession().createQuery(formsNames);
	   	 int result = query.executeUpdate();
    }
    
    /**
     *  {@inheritDoc}
     */
    public List<ProcessFormName> selectProcessKeyByFormId(String formNames)throws EazyBpmException{
    	Query query = getSession().createQuery("from ProcessFormName as process WHERE process.formId in "+formNames);
		getSession().flush();
		return query.list();
    }

	@Override
	public boolean deleteFormByIds(List<String> formIds)
			throws EazyBpmException {
		// TODO Auto-generated method stub
		return false;
	}
    
   /* public MetaForm selectFormVersionIsActiveByFormId(String formId) throws EazyBpmException {
    	Query query = getSession().createQuery("from MetaForm as metaForm WHERE metaForm.formId in "+formId+"");
		getSession().flush();
		return query.list();
    }*/
}