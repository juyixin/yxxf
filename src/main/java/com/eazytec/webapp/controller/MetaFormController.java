package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Form;
import com.eazytec.model.FormButton;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.Module;
import com.eazytec.model.User;
import com.eazytec.util.JSTreeUtil;


/**
 * Does the form related actions like
 * save form, retrieve the form details
 * @author Karthick
 * @author Megala
 * @author Maheswaran
 */
@Controller
public class MetaFormController extends BaseFormController{

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    private FormDefinitionService formService;
    public VelocityEngine velocityEngine;
    private ModuleService moduleService;
    private TableService tableService;
    
    @Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Autowired
    public void setFormService(FormDefinitionService formService) {
        this.formService = formService;
    }

    
    
    @Autowired
	public void setTableService(TableService tableService) {
		this.tableService = tableService;
	}
    /**
     * To show the form bulider, to design
     * the form by drag and drop the fields
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/form/formDesigner",method = RequestMethod.GET)
    public ModelAndView showFormBuilder(ModelMap model) {
        model.addAttribute("type", "form");
       return new ModelAndView("form/formIndex",model);
    }

    /**
     * To show the dynamic form bulider, to design the form
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/form/createForm",method = RequestMethod.GET)
    public ModelAndView createDynamicForm(ModelMap model) {
        model.addAttribute("type", "form");
       return new ModelAndView("form/createForm",model);
    }

    /**
     * To save the constructed form
     * @param form
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/form/saveForm", method = RequestMethod.POST)
       public String saveForm(@ModelAttribute("form") Form form, ModelMap model,HttpServletRequest request) {
        Locale locale = request.getLocale();
           try {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
               if(user != null) {
                   form.setCreatedBy(user.getUsername());
               }else{
                   form.setCreatedBy(Constants.EMPTY_STRING);
               }
               form = formService.saveForm(form);
               saveMessage(request, getText("form.saved",locale));
        } catch (Exception e) {
            log.error(getText("form.saveNewForm.error",locale),e);
            saveError(request, getText("form.save.error",locale));
        }
           return "redirect:formList";
      }

    @RequestMapping(value= "bpm/form/saveDynamicForm", method = RequestMethod.POST)
    public RedirectView saveDynamicForm(@ModelAttribute("metaForm") MetaForm metaForm, HttpServletRequest request,final RedirectAttributes redirectAttributes){
        Locale locale = request.getLocale();
        Map<String, String> formDetail = new HashMap<String, String>();
        try {
        	String existingForm = metaForm.getId();
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user != null) {
                metaForm.setCreatedBy(user.getUsername());
            }else{
                metaForm.setCreatedBy(Constants.EMPTY_STRING);
            }
            String moduleId=request.getParameter("module.Id");
            String isDefaultProcess = request.getParameter("isDefaultForm");
            if(isDefaultProcess.equalsIgnoreCase("true")) {
                metaForm.setIsDefaultForm("true");
            } 
            metaForm = formService.saveDynamicForm(metaForm,moduleId);
            if(existingForm != null){
            	if(existingForm.equals(metaForm.getId())){
            		saveMessage(request, getText("form.updated",locale));
                	log.info("Form '"+metaForm.getFormName()+"' updated successfully");
            	}else{
            		/*if(formService.isFormMappedInProcess(existingForm)){
            			saveMessage(request, getText("form.process.saved.version",locale));
                    	log.info("Form '"+metaForm.getFormName()+"' version saved successfully");
            		}else{*/
	            		List<MetaForm> metaFormList = formService.getAllFormByFormName(metaForm.getFormName());
	            		String formIds = "";
	            		if(metaFormList != null && metaFormList.size() > 0){
	    				
	    				for(MetaForm form: metaFormList){
	    					formIds += "'"+form.getId()+"',";
	    				}
	            		}
	            		if(formIds != "" && formIds.length() > 0){
	            			formIds = formIds.substring(0, formIds.length()-1);
	            		}
            			List<LabelValue> processNameList = formService.getAllProcessNameByFormId(formIds);
            			Set<String> processNameSet = new HashSet<String>();
            			for(LabelValue processName : processNameList){
            				processNameSet.add(processName.getLabel());
            			}
            			if(processNameSet.size() > 0){
            				saveMessage(request,getText("form.saveVersion",processNameSet.toString(),locale));
            			} else {
            				saveMessage(request,getText("form.saved.version",locale));
            			}
                    	log.info(getText("form.saved",locale));
            	//	}
                }
            }else{
            	saveMessage(request, getText("form.saved",locale));
            }
            if(isDefaultProcess.equalsIgnoreCase("true")) {
            	formService.deployProcessForForm(metaForm,getServletContext());
            }
            formDetail.put("isFormShow", "true");
           formDetail.put("formModuleId", moduleId);
            redirectAttributes.addFlashAttribute("formDetail", formDetail);
        } catch (Exception e) {
            saveError(request, getText("form.save.error",metaForm.getFormName(),locale));
            e.printStackTrace();
        }
        return new RedirectView("/bpm/module/manageModuleRelation");
    }


    /**
     * To save the user defined form button
     * @param form
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/form/saveFormButton", method = RequestMethod.POST)
       public @ResponseBody Map<String, Object> saveFormButton(@ModelAttribute("formButton") FormButton formButton, ModelMap model,HttpServletRequest request) {
        Locale locale = request.getLocale();
        Map<String, Object> responseMap = new HashMap<String, Object>();
           try {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
               if(user != null) {
                   formButton.setCreatedBy(user.getUsername());
               }else{
                   formButton.setCreatedBy(Constants.EMPTY_STRING);
               }
               formButton = formService.saveFormButton(formButton);
               responseMap.put("formButton", formButton);
          	   //log.info("Form Button saved Successfully");
        } catch (Exception e) {
            log.error(getText("form.saveButton.error",locale),e);
        }
           return responseMap;
      }
    
    /**
     * To delete the user defined form button
     * @param form
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/form/deleteFormButton", method = RequestMethod.GET)
       public @ResponseBody Map<String, Object> deleteFormButton(@ModelAttribute("formButtonId") String formButtonId, ModelMap model,HttpServletRequest request) {
        Locale locale = request.getLocale();
        Map<String, Object> responseMap = new HashMap<String, Object>();
           try {
        	   if(formButtonId.equals("aa59d398-9773-11e2-aca8-00270e0048cc")||formButtonId.equals("c4c64568-9773-11e2-aca8-00270e0048cc")||formButtonId.equals("ccaa5710-9773-11e2-aca8-00270e0048cc")){
        		   responseMap.put("status", "error");
                  	responseMap.put("message",getText("default.button",locale));
                   log.info(getText("default.button",locale));
                   return responseMap;
        	   } 
        		   
    		   if(formService.deleteFormButton(formButtonId)){
               	responseMap.put("status", "success");
               	responseMap.put("message", getText("user.button.delete",locale));
           	    log.info(getText("user.button.delete",locale));
               }else{
               	responseMap.put("status", "error");
               	responseMap.put("message",getText("button.mapped.in.process",locale));
                log.info(getText("button.mapped.in.process",locale));
               }
        } catch (Exception e) {
            log.error(getText("form.deleteButton.error",locale),e);
        }
           return responseMap;
      }


    /**
        * To get menu icons through ajax call
        * @return icons url
        */
        @RequestMapping(value="bpm/form/getFormButtons", method = RequestMethod.GET)
          public @ResponseBody List<FormButton> getFormButtons(HttpServletRequest request) {
           List<FormButton> responseMap = new ArrayList<FormButton>();
           Locale locale = request.getLocale();
             try{
                 responseMap = formService.getFormButtons();
            	 //log.info("FormButton Retrived Successfully");
                 return responseMap;
             }catch(Exception e){
                 log.error(getText("form.gettingMenu.error",locale),e);
             }
             return null;
          }


    /**
     *  Displays all the forms
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/form/formList", method = RequestMethod.GET)
    public ModelAndView showForms(ModelMap model,HttpServletRequest request) {
        Locale locale = request.getLocale();
        try{
        	//List<Form> formList = formService.getForm();
            List<MetaForm> formList = formService.getAllDynamicForms();
            String[] fieldNames={"id","formName","createdOn","createdBy","version","active","module"};  
            List<Map<String,Object>> mapListFromObjectList = formService.resolveFormDetailsListMap(CommonUtil.getMapListFromObjectListByFieldNames(formList,fieldNames,""));

            String script =   FormDefinitionUtil.generateScriptForTask(mapListFromObjectList,velocityEngine, locale, false);
            List<Module> moduleList = moduleService.getAllModuleList();
            //String script = FormDefinitionUtil.generateScriptForTask(CommonUtil.getMapListFromObjectList(formList),velocityEngine);
            model.addAttribute("script", script);
            model.addAttribute("type", "formList");
            model.addAttribute("moduleTree", JSTreeUtil.getJsonTreeForModuleList(request,moduleList,true));
       	   //log.info("Grid Loaded Successfully");
        }catch(BpmException e){
            log.error(e.getMessage(), e);
            model.addAttribute("script", "Problem loading grid, check log files");
        }catch(EazyBpmException e){
            log.error(e.getMessage(), e);
            saveError(request, e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage(), e);
            saveError(request, getText("form.formList.error",locale));
        }

        return new ModelAndView("form/formList",model);
    }
    
    /**
     * Get all forms grid for selected module id
     * @param model
     * @param request
     * @param moduleId
     * @return
     */
    @RequestMapping(value="bpm/form/formListForModule", method = RequestMethod.GET)
    public ModelAndView showFormsFormModule(ModelMap model,HttpServletRequest request,@ModelAttribute("moduleId") String moduleId,@RequestParam("isEdit") String isEdit,@RequestParam("isSystemModule") String  isSystemModule,@RequestParam("isJspForm")boolean isJspForm) {
        Locale locale = request.getLocale();
        try{
            //List<Form> formList = formService.getForm();
        	String isTemplateList=request.getParameter("isTemplateList");
        	
        	boolean isTemplateView=false;
        	if(isTemplateList!=null && isTemplateList.equals("true")){
        		isTemplateView=true;
        	}
            List<MetaForm> formList = formService.getAllDynamicFormsForModule(moduleId,isEdit,isSystemModule, isJspForm,isTemplateView);
            String[] fieldNames={"id","formName","createdOn","createdByFullName","version","active","module", "isDelete","isEdit","isSystemModule","templateForm"};  
            List<Map<String,Object>> mapListFromObjectList = formService.resolveFormDetailsListMap(CommonUtil.getMapListFromObjectListByFieldNames(formList,fieldNames,moduleId));
            String script =   FormDefinitionUtil.generateScriptForTask(mapListFromObjectList,velocityEngine, locale, isJspForm);
            List<Module> moduleList = moduleService.getAllModuleList();
            //String script = FormDefinitionUtil.generateScriptForTask(CommonUtil.getMapListFromObjectList(formList),velocityEngine);
            model.addAttribute("script", script);
            Module moduleObj=moduleService.getModule(moduleId);
            if(moduleObj.getName().equals("Default Module")){
            	isEdit="false";
            }
            model.addAttribute("isJspForm",isJspForm);
            model.addAttribute("isEdit", isEdit);
            model.addAttribute("type", "formList");
            model.addAttribute("moduleId", moduleId);
            model.addAttribute("moduleTree", JSTreeUtil.getJsonTreeForModuleList(request,moduleList,true));
        	//log.info("forms grid for selected module id is Loaded Successfully");
        }catch(BpmException e){
            log.error(e.getMessage(), e);
            model.addAttribute("script", "Problem loading grid, check log files");
        }catch(EazyBpmException e){
            log.error(e.getMessage(), e);
            saveError(request, e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage(), e);
            saveError(request, getText("form.formList.error",locale));
        }

        return new ModelAndView("form/manageForm",model);
    }

    @RequestMapping(value="/form/getAllFormsByModuleId", method = RequestMethod.GET)
    public @ResponseBody List<LabelValue> getAllFormsByModuleId(@RequestParam("moduleId") String moduleId) throws Exception{
    	List<LabelValue> formLabelValueList = new ArrayList<LabelValue>();
    	try{
    		Module module = moduleService.getModule(moduleId);
    		if(module != null){
    			List<String> formIdLists = new ArrayList<String>();
    			Set<MetaForm> metaForms=module.getForms();
    			for(MetaForm metaForm:metaForms){
    				if(metaForm.isActive() == true && metaForm.getIsJspForm() == false){
    					//formIdLists.add(metaForm.getId());
    					LabelValue formLabelvalue = new LabelValue();
    					formLabelvalue.setLabel(metaForm.getId());
    					formLabelvalue.setValue(metaForm.getFormName());
    					formLabelValueList.add(formLabelvalue);
    				}
    			}
    		}
    	}catch(Exception e){
    		
    	}
    	return formLabelValueList;
    }

    /**
     * Get the saved form based on id,
     * to show the edit view of form builder
     * @param formId
     * @param model
     * @return
     * @throws BpmException
     */
    @RequestMapping(value="bpm/form/getDynamicFormContent", method = RequestMethod.GET)
    public @ResponseBody MetaForm showFormDesignerForEdit(@RequestParam("formId") String formId,HttpServletRequest request){
        MetaForm formDetails = null;
        Locale locale = request.getLocale();
        try {
            //log.info("Getting Form name:"+formId);
            formDetails = formService.getDynamicFormById(formId);
        } catch (Exception e) {
            log.error(getText("form.renderDesigner.error",locale),e);
        }
        return formDetails;
    }

    /**
     * Returns a view page for editing ck editor form
     * @param formId
     * @param model
     * @return
     * @throws EazyBpmException
     */
    @RequestMapping(value="bpm/form/showFormDesignerForEdit", method = RequestMethod.GET)
    public ModelAndView loadCKEditorOnEdit(@RequestParam("formId") String formId,@RequestParam("moduleId") String moduleId,ModelMap model,HttpServletRequest request){
        MetaForm formDetails = null;
        Locale locale = request.getLocale();
        try {
            formDetails = formService.getDynamicFormById(formId);
            model.addAttribute("metaForm",formDetails);
            model.addAttribute("moduleId",moduleId);
        } catch (Exception e) {
            log.error(getText("form.renderDesigner.error",locale),e);
        }
        return new ModelAndView("form/editCkEditorForm",model);
    }
     
    /**
     * <p>
     * Retrieves all the Inactivated versions of a paritcular form and displays it in a grid.
     * </p>
     * @return
     *
     * @throws EazyBpmException
     */
    @RequestMapping(value="bpm/form/showAllFormVersions", method = RequestMethod.GET)
    public ModelAndView showAllFormVersions(@RequestParam("formName") String formName,@RequestParam("moduleId") String moduleId,@RequestParam("isEdit") String isEdit,@RequestParam("isSystemModule") String isSystemModule,ModelMap model,HttpServletRequest request) throws EazyBpmException {
        Locale locale = request.getLocale();

        try {
            List<MetaForm> forms = formService.showAllFormVersions(formName,isEdit,isSystemModule);
            String[] fieldNames={"id","formName","createdOn","createdByFullName","version","active","module","isDelete","isEdit","isSystemModule","templateForm"};  
            List<Map<String,Object>> mapListFromObjectList = formService.resolveFormDetailsListMap(CommonUtil.getMapListFromObjectListByFieldNames(forms,fieldNames,moduleId));
            String script =   FormDefinitionUtil.generateFormVersionsScript(mapListFromObjectList,velocityEngine,locale);
            model.addAttribute("script",script);
            List<Module> moduleList = moduleService.getAllModuleList();
            model.addAttribute("moduleTree", JSTreeUtil.getJsonTreeForModuleList(request,moduleList,true));
            model.addAttribute("type","formList");
        } catch (Exception e) {
            log.error(getText("form.versionShowing.error",formName,locale),e);
        }
        return new ModelAndView("/form/formList",model);
    }

    /**
     * Check the given form is exist
     * @return boolean, if form exist return is true otherwise return is false
     * @throws Exception
     */
    @RequestMapping(value="bpm/form/isFormNamePresent", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> isFormNamePresent(@RequestParam("formName") String formName) throws Exception {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try{
            boolean isPresent = formService.isFormNamePresent(formName);
            responseMap.put("isPresent", isPresent);
        }catch(BpmException e){
            log.error(e.getMessage(), e);
            responseMap.put("isPresent", false);
            responseMap.put("message", "Exception occur while checking the form is present or not :"+e.getMessage());
        }
        return responseMap;
    }

    /**
     * Restore the selected form
     * @return view containing list of form
     */
    @RequestMapping(value="bpm/form/restoreForm", method = RequestMethod.GET)
    public ModelAndView restoreForm(@RequestParam("formId") String formId, @RequestParam("formName") String formName, @RequestParam("moduleId") String moduleId,@RequestParam("isEdit") String isEdit,@RequestParam("isSystemModule") String isSystemModule, ModelMap model, HttpServletRequest request) throws Exception {
        Locale locale = request.getLocale();
        try{
            formService.restoreForm(formId, formName);
            saveMessage(request, getText("form.restored",locale));
            log.info("Form '"+formName+"' Restored Successfully");
        }catch(BpmException e){
            log.error(e.getMessage(), e);
            saveError(request, getText("form.restored.error",locale));
        }
        return showAllFormVersions(formName,moduleId,isEdit,isSystemModule,model,request);
    }

    /**
     *  Delete all the Form versions
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/form/deleteAllFromVersions", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> deleteAllFromVersions(@RequestParam("formName") String formName,ModelMap model,HttpServletRequest request) throws Exception {
    	 Map<String, Object> responseMap = new HashMap<String, Object>();
        Locale locale = request.getLocale();
        try{
        	if(formService.deleteAllFromVersions(formName)){
        		responseMap.put("status", "success");
            	responseMap.put("message", getText("form.versions.delete",formName,locale));
                log.info("Deleted All From Versions of '"+formName+"' Successfully");
            	responseMap.put("redirectUrl", "#bpm/form/formListForModule");
    		}else{
    			responseMap.put("status", "error");
            	responseMap.put("message",getText("form.mapped.in.process",locale));
            	responseMap.put("redirectUrl", "#bpm/form/formListForModule");
    		}   
        }catch(EazyBpmException e){
            log.error(e.getMessage(), e);
            model.addAttribute("message", getText("form.definition.delete.error",e.getMessage(),locale));
            responseMap.put("status", "error");
        	responseMap.put("message",getText("form.version.delete.error",e.getMessage(),locale));
        	responseMap.put("redirectUrl", "#bpm/form/formListForModule");
        }
        return responseMap;
    }

    /**
     *  Delete Form by versions
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/form/deleteFormById", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> deleteFormById(@RequestParam("formId") String formId,@RequestParam("formName") String formName,@RequestParam("moduleId") String moduleId,@RequestParam("isEdit") String isEdit,@RequestParam("isSystemModule") String isSystemModule,@RequestParam("version") int version,ModelMap model,HttpServletRequest request) throws Exception {
    	 Map<String, Object> responseMap = new HashMap<String, Object>();
        Locale locale = request.getLocale();
       
        try{
            if(formService.deleteFormById(formId)){
            	responseMap.put("status", "success");
            	responseMap.put("message", getText(formName+" form version "+version+" deleted successfully",locale));
                log.info(getText("form.version.delete",new Object[] { formName, formId },locale));
            	responseMap.put("redirectUrl", "#bpm/form/showAllFormVersions");
            }else{
            	responseMap.put("status", "error");
            	responseMap.put("message",getText("form.mapped.in.process",locale));
            	responseMap.put("redirectUrl", "#bpm/form/showAllFormVersions");
            }
            //showVersion=showAllFormVersions(formName,moduleId,isEdit,isSystemModule,model,request);        	
        }catch(EazyBpmException e){
            log.error(e.getMessage(), e);
            model.addAttribute("message", getText("form.delete.version",e.getMessage(),locale));
            responseMap.put("status", "error");
        	responseMap.put("message", getText("form.version.delete.error",locale));
        	responseMap.put("redirectUrl", "#bpm/form/showAllFormVersions");
        }
        return responseMap;
    }
    
    @RequestMapping(value="bpm/form/deleteFormsByNames", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> deleteFormsByNames(@RequestParam("formNames") String formNames,@RequestParam("formName") String formName, @RequestParam("selectedRowId") List<String> selectedRowIds, @RequestParam("versionGridUrlStatus") boolean versionGridUrlStatus, ModelMap model,HttpServletRequest request) throws Exception {
    	Locale locale = request.getLocale();
		 Map<String,Object> message = new HashMap<String, Object>();
		 
		try {
			List<String> formNameList=CommonUtil.convertJsonToListStrings(formNames);
			if(!versionGridUrlStatus) {
				if(formService.deleteFormsByNames(formNameList)){
					   message.put("successMessage", getText("forms.deleted",formNames,locale));
					   log.info("Bulk Deleted Form Names "+formNames);
				   }else{
					   message.put("errorMessage", getText("form.mapped.in.process",locale));
					   log.error(getText("form.mapped.in.process",locale));
				   } 
			} else {
				if(formService.deleteFormByIds(selectedRowIds)){
					   message.put("successMessage", getText("Form "+formName+" version deleted successfully",locale));
					   log.info("Bulk Deleted Form Names ");
				   }else{
					   message.put("errorMessage", getText("Form is in Active State",locale));
					   log.error(getText("form.mapped.in.process",locale));
				   }
			}
		   
		} catch (EazyBpmException e1) {
			// TODO Auto-generated catch block
			  message.put("errorMessage", getText("forms.deleted.error",locale));
			//saveError(request,	getText("error.trigger.delete", e1.getMessage(), locale));
			e1.printStackTrace();
		}
		 return message;
   }
     /**
     * <p>Submits the form associated to a task that the user may need to fill in at processing a task.
     * Various runtime entity values are created based on the values submitted.</p>
     * @param request
     * @param model
     * @return
     */
     @RequestMapping(value="/form/getAllForms", method = RequestMethod.GET)
       public @ResponseBody Map<String, List<Map<String, String>>> getAllForms() {
         List<Map<String, String>> formDetailsList = new ArrayList<Map<String, String>>();
         Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
          try{
              List<LabelValue> forms =  formService.getAllNonPublicForms();
              if (forms != null){
                  for(LabelValue form : forms){
                      Map<String,String> formDetail = new HashMap<String, String>();
                      formDetail.put("formName", form.getLabel());
                      formDetail.put("id", form.getValue());
                      formDetailsList.add(formDetail);
                  }
                  resultValue.put("forms", formDetailsList);
                 // log.info("Retrived All Forms Successfully"); 
                  return resultValue;
              }
          }catch(Exception e){
              log.error("Error while getting all forms "+e);
          }
          return null;
       }
     
     /**
      * <p>Submits the form associated to a task that the user may need to fill in at processing a task.
      * Various runtime entity values are created based on the values submitted.</p>
      * @param request
      * @param model
      * @return
      */
      @RequestMapping(value="/form/getAllFormsByJspForm", method = RequestMethod.GET)
        public @ResponseBody Map<String, List<Map<String, String>>> getAllFormsByJspForm(@RequestParam("isJspForm") String isJspForm) {
          List<Map<String, String>> formDetailsList = new ArrayList<Map<String, String>>();
          Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
           try{
               List<LabelValue> forms =  formService.getAllNonPublicFormsByJspForm(isJspForm);
               if (forms != null){
            	   Map<String,String> emptyFormDetail = new HashMap<String, String>();
            	   emptyFormDetail.put("formName","");
                   emptyFormDetail.put("id", "");
                   formDetailsList.add(emptyFormDetail);
            	   for(LabelValue form : forms){
                       Map<String,String> formDetail = new HashMap<String, String>();
                       formDetail.put("formName", form.getLabel());
                       formDetail.put("id", form.getValue());
                       formDetailsList.add(formDetail);
                   }
                   resultValue.put("forms", formDetailsList);
                   return resultValue;
               }
           }catch(Exception e){
               log.error("Error while getting all forms "+e);
           }
           return null;
        }
     
     /**
      * <p>Submits the form associated to a task that the user may need to fill in at processing a task.
      * Various runtime entity values are created based on the values submitted.</p>
      * @param request
      * @param model
      * @return
      */
      @RequestMapping(value="/form/getFormById", method = RequestMethod.GET)
        public @ResponseBody Map<String, List<Map<String, String>>> getFormById(@RequestParam("formId") String formId) {
          List<Map<String, String>> formDetailsList = new ArrayList<Map<String, String>>();
          Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
           try{
        	   LabelValue form =  formService.getFormLabelValueById(formId);
               if(form != null){
            	   Map<String,String> formDetail = new HashMap<String, String>();
            	   formDetail.put("formName", form.getLabel());
            	   formDetail.put("id", form.getValue());
            	   formDetailsList.add(formDetail);
            	   resultValue.put("forms", formDetailsList);
            	   return resultValue;
	           	}
              // log.info("Retrived FromBy ID Successfully"); 
           }catch(Exception e){
        	   log.error("Error while getting Form "+e);
           }
           return null;
        }
     
    @RequestMapping(value = "/form/getFormsByIds" , method = RequestMethod.GET)
    public @ResponseBody Map<String,List<Map<String,String>>> getFormsByIds(@RequestParam("formIds") String formIds){
    	 Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
    	 List<Map<String, String>> formDetailsList = new ArrayList<Map<String, String>>();
    	 try{
    		 List<LabelValue> forms = formService.getFormsLabelValueByIds(formIds);
    		 for(LabelValue form : forms){
    			 Map<String,String> formDetail = new HashMap<String, String>();
                 formDetail.put("formName", form.getLabel());
                 formDetail.put("id", form.getValue());
                 formDetailsList.add(formDetail);
    		 }
    		 resultValue.put("forms", formDetailsList);
             return resultValue;
    	 } catch(Exception e){
    		 log.error("Error Occured while getting form label value using id"+e.getMessage());
    	 }
    	return null;
    }
      
	@RequestMapping(value = "/form/getLatestFormVersion", method = RequestMethod.GET)
	public @ResponseBody Map<String,Map<String, String>> getLatestFormVersion(
			@RequestParam("formId") String formId) {
		Map<String,Map<String,String>> responseMap = new HashMap<String, Map<String,String>>();
		try {
			List<MetaForm> forms = formService.getFormVersionByFormId(formId);
			if (forms != null && !forms.isEmpty()) {
				MetaForm form = forms.get(0);
				Map<String, String> formDetail = new HashMap<String, String>();
				//formDetail.put("formName", form.getFormName()+" ("+form.getVersion()+")");
				formDetail.put("formName", form.getFormName()+" - "+form.getVersion());
				formDetail.put("id", form.getId());
				responseMap.put("forms", formDetail);
				return responseMap;
			}
        } catch (Exception e) {
			log.error("Error while getting all forms " + e);
		}
		return new HashMap<String, Map<String,String>>();
	}

     /**
      * 
      */
     @RequestMapping(value="/form/getAllChildTableFormNames", method = RequestMethod.GET)
     public @ResponseBody Map<String, List<Map<String, String>>> getAllChildTableFormNames(@RequestParam("tableId") String tableId) {
       List<Map<String, String>> formDetailsList = new ArrayList<Map<String, String>>();
      
       Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
        try{
            List<LabelValue> forms =  formService.getAllChildTableFormNames(tableId);
            if (forms != null){
                for(LabelValue form : forms){
                    Map<String,String> formDetail = new HashMap<String, String>();
                    formDetail.put("formName", form.getLabel());
                    formDetail.put("id", form.getValue());
                    formDetailsList.add(formDetail);
                }
                resultValue.put("forms", formDetailsList);
                return resultValue;
            }
        }catch(Exception e){
            log.error("Error while getting all forms "+e);
        }
        return null;
     }
     
     /**
      * <p>Submits the form associated to a task that the user may need to fill in at processing a task.
      * Various runtime entity values are created based on the values submitted.</p>
      * @param request
      * @param model
      * @return
      */
      @RequestMapping(value="bpm/form/getAllFormFields", method = RequestMethod.GET)
        public @ResponseBody Map<String,List<Map<String,String>>> getAllFields(@RequestParam("formId") String formId) {
          Map<String,List<Map<String,String>>> formFieldDetails = new HashMap<String,List<Map<String,String>>>();

           try{
               List<Map<String,String>> formFields =  formService.getFields(formId);
               formFieldDetails.put("forms", formFields);
               return formFieldDetails;

           }catch(Exception e){
               log.error("Error while getting all forms fields "+e);
           }
           return null;
        }

     /**
      * To get the form details by formId through API
      * @param formId
      * @return
      */
     @RequestMapping(value="/form/getSubFormById", method = RequestMethod.GET)
       public @ResponseBody Map<String,Form> getSubFormById(@RequestParam("formId") String formId) {
         Map<String,Form> formDetails = new HashMap<String,Form>();
          try{
              Form subForm = formService.getFormById(formId);
              formDetails.put("subForm", subForm);
              return formDetails;
          }catch(Exception e){
              log.error("Error while getting all forms "+e);
          }
          return null;
       }

        /**
            * To get form button icons through ajax call
            * @return icons url
            */
            @RequestMapping(value="bpm/form/getButtonIcons", method = RequestMethod.GET)
              public @ResponseBody List<String> getMenuIcons(HttpServletRequest request) {
                 List<String> buttonIcons = new ArrayList<String>();
                 try{
                     String buttonIconDir = getServletContext().getRealPath("/images/formbutton");
                     File iconDir = new File(buttonIconDir);
                     for(File icon : iconDir.listFiles()){
                       buttonIcons.add("/images/formbutton/"+icon.getName());
                  }
                     return buttonIcons;
                 }catch(Exception e){
                     log.error("Error while getting form button icon list ",e);
                 }
                 return null;
              }

               /**
                 * Save the Table in meta and create a Runtime table.
                 * @param model
                 * @return view containing list of form
                 * @throws Exception
                 */
                @RequestMapping(value=" bpm/form/createRelationshipTables", method = RequestMethod.POST)
                public @ResponseBody  void createRelationshipTables(HttpServletRequest request)throws Exception {
                    Locale locale = request.getLocale();
                       try {

                        Map<String,String> tableOperation=new HashMap<String, String>();
                           List<Map<String,Object>> subFormRelationList=CommonUtil.convertJsonToList(request.getParameter("subFormRelationList"));
                           //tabelService.createSubFormRelationshipTables(subFormRelationList);
                       } catch (Exception e) {
                        log.error("Error while saving a new table ",e);
                        saveError(request, getText("table.created.error",locale));
                    }
                }
                
   			 /**
   			  * To get the table column name and id as label value pair by table id
   			  * @param formId
   			  * @return
   			  */
   			 @RequestMapping(value="/bpm/form/getAllSubFormFields", method = RequestMethod.GET)
   			   public @ResponseBody List<Map<String, String>> getAllSubFormFields(@RequestParam("formId") String formId, HttpServletRequest request) throws BpmException{
   				
   				  try{
   					  List<Map<String, String>> tabelColumns = formService.getFieldsWithXMLElement(formId);
   					  if (tabelColumns != null && formId != null){
   						  return tabelColumns;
   					  }
                  }catch(Exception e){
   					  log.error("Error while getting all columns of table "+e);
   				  }		  
   				  return null;
   			   } 
   			 
   		/**
   	     * @return
   	     * @throws JSONException 
   	     */
   	      @RequestMapping(value="bpm/form/generateSubFormHtml", method = RequestMethod.POST)
   	      public @ResponseBody String generateSubFormHtml(@RequestParam("subFormFieldList") String subFormFieldList,@RequestParam("subFormName") String subFormName) throws BpmException{
   	         
   	          String subFormHtml="";
   	          try {
   	                  List<Map<String,Object>> fieldProperties=CommonUtil.convertJsonToList(subFormFieldList);
   	                  subFormHtml=formService.generateSubFormHtml(fieldProperties,subFormName);
   	                  } catch (Exception e) {
   	               //log.info("Error while saving a new table ",e);
   	           }
   	          
   	          return subFormHtml;
   	      }	 
   	      
   	   /**
 		 * Show the Form organization tree
 		 * @param title
 		 * @param requestorId
 		 * @param selectType
 		 * @param model
 		 * @param request
 		 * @return
 		 * @throws Exception
 		 */
   	   @RequestMapping(value="bpm/admin/formTree", method = RequestMethod.GET)
	    public ModelAndView showFormTree(ModelMap model, @RequestParam("selectionType") String selectionType, 
	    		@RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues, 
	    		@RequestParam("callAfter") String callAfter, User user,	BindingResult errors, 
	    		HttpServletRequest request) throws Exception {
	    	List<LabelValue> formList = formService.getMetaFormAsLabelValue();
	    	JSONArray nodes = new JSONArray();
	    	nodes = CommonUtil.getNodesFromLabelValue(formList);
	    	model.addAttribute("nodes", nodes);
			model.addAttribute("nodeDetail",formList );
			model.addAttribute("selectionType", selectionType);
			model.addAttribute("selection", "form");
			model.addAttribute("selectedValues", selectedValues);
			model.addAttribute("appendTo",appendTo);
			model.addAttribute("callAfter",callAfter);
			model.addAttribute("selectionType",selectionType);
			model.addAttribute("selectedValues",selectedValues);
		  	return new ModelAndView("/admin/showModuleTree", model);
	    }
   	   
	     /**
	      * <p>To get all forms as template </p>
	      * 
	      * @return Map<String,List<Map<String, String>>>
	      */
	    @RequestMapping(value="/form/getAllTemplateForms", method = RequestMethod.GET)
        public @ResponseBody Map<String,List<Map<String, String>>> getAllTemplateForms() {
          Map<String,List<Map<String, String>>> formDetails = new HashMap<String,List<Map<String, String>>>();
           try{
               List<MetaForm> forms =  formService.getAllTemplateForms();
               List<Map<String, String>> formList=new ArrayList<Map<String,String>>();
               if(forms != null) {
                   for(MetaForm form:forms){
                	   Map<String, String> subForm=new HashMap<String, String>();
                	   subForm.put("formName", form.getFormName());
                	   subForm.put("description", form.getDescription());
                	   subForm.put("htmlSource", form.getHtmlSource());
                	   formList.add(subForm);
                   }
               }
               formDetails.put("forms", formList);
               return formDetails;
           }catch(Exception e){
               log.error("Error while getting all forms "+e);
           }
           return null;
        }
	    
	    /**
	     * uploadFormImage
	     * @param modelba
	     * @return view containing list of form
	     * @throws Exception
	     */
	    @RequestMapping(value="/form/uploadFormImage", method = RequestMethod.POST)
	     public @ResponseBody Map<String,Object>  uploadFormImage(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
	          Locale locale = request.getLocale();
	          Map<String,Object> message = new HashMap<String, Object>();
	          String fileNameWithPath="";
	          String fileName="";
	          MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		        Map<String, List<MultipartFile>> fileInList = multipartRequest.getMultiFileMap();
		        List<MultipartFile> files =fileInList.get("file");
		       // the directory to upload to
		         String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
		         // Create the directory if it doesn't exist
		         File dirPath = new File(uploadDir);
		         if (!dirPath.exists()) {
		             dirPath.mkdirs();
		         }
		         //iterate the list of file and create the file in specified path and deploy it
		        if(null != files && files.size() > 0) {
		            for (MultipartFile multipartFile : files) {
		                // retrieve the file data
		                InputStream stream = multipartFile.getInputStream();
		                // write the file to the file specified
		                fileName = multipartFile.getOriginalFilename();
		                int pos = fileName.lastIndexOf(".");
			       		String extension = fileName.substring(pos+1);
			       		fileName = fileName.substring(0, pos-1);
			       		fileName = fileName.replace(" ", "_");
			           	byte[] encodedFileName = fileName.getBytes("UTF-8");
			           	fileName= encodedFileName.toString();
			           	if(fileName.startsWith("[")){
			           		fileName = fileName.substring(1);
			           	}
			       		fileName = fileName+"."+extension;
		                OutputStream bos = new FileOutputStream(uploadDir + fileName);
		                int bytesRead;
		                byte[] buffer = new byte[8192];
		                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		                    bos.write(buffer, 0, bytesRead);
		                }
		                bos.close();
		                // close the stream
		                stream.close();
		                fileNameWithPath = dirPath.getAbsolutePath() + Constants.FILE_SEP + fileName;
		            }
		        } 
		     
	               try {
	                    //log.info("Dump file :: "+fileNameWithPath);
	                    message.put("success", getText("from.image.uploaded",locale));
	                    message.put("fileName", fileName);
	                    message.put("loginUser", request.getRemoteUser());
	                    //log.info("Retrived uploadFormImage Successfully"); 
	                   
	          }catch(Exception e) {
	        	  log.error("Error while uploading form image "+e);
	        	  message.put("error", getText("from.image.upload.error",locale));
	          }
	          return message;
	    }
	    
	    @RequestMapping(value="bpm/form/copyFormsToModule",method = RequestMethod.GET)
	    public RedirectView copyFormsToModule(@RequestParam("moduleId") String moduleId,@RequestParam("formIds") String formIds,@RequestParam("newFormName") String newFormName,ModelMap model,HttpServletRequest request,HttpServletResponse response )throws Exception{
	    	Locale locale = request.getLocale();
	    	List<String> formIdList=CommonUtil.convertJsonToListStrings(formIds);
	    	MetaForm metaFormObj=formService.getDynamicFormById(formIdList.get(0));
	    	if(formService.copyFormsToModule(moduleId,formIdList,newFormName)){
	    		log.info("From '"+newFormName+"' is Copied from the From :'"+metaFormObj.getFormName()+"' Successfully");
	    		saveMessage(request, getText("form.copied.successfully",locale));
	    	}else{
	    		saveError(request,getText("form.copied.failed",locale));
	    	}
	    	return new RedirectView("/bpm/module/manageModuleRelation?isFormCancel=true&moduleId="+moduleId);
	    }
	    
	    @RequestMapping(value="/form/saveFormPrintTemplate", method = RequestMethod.POST)
        public @ResponseBody Map<String, String> saveFormPrintTemplate(@RequestParam("printTemplate") String printTemplate,@RequestParam("formId") String formId,ModelMap model,HttpServletRequest request) {
	       Map<String, String> response = null;
	       Locale locale = request.getLocale();
           try{
        	   response = new HashMap<String,String>();
        	   MetaForm result= formService.saveFormPrintTemplate(formId, printTemplate);
               response.put("isSaved", "true");
               response.put("printTemplate", String.valueOf(result.getPrintTemplate()));
               response.put("message", getText("form.template.save",locale));
               //log.info("Form print template saved successfully.");
           }catch(Exception e){
        	   response.put("isSaved", "false");
        	   response.put("message", getText("form.template.save.error",locale));
               log.error("Error occur while save form print template "+e);
           }
           return response;
        }
	    
	    @RequestMapping(value="/form/getContentForPrintTemplate", method = RequestMethod.POST)
        public @ResponseBody Map<String,String> getContentForPrintTemplate(@RequestParam("formId") String formId, @RequestParam("templateValue") String templateValue,@RequestParam("pastValues") String pastValues, @RequestParam("subFormAttrValues") String subFormAttrValues ,ModelMap model,HttpServletRequest request) {
	       String printHtmlContent = null;
	       Map<String,String> printHtmlContentMap = new HashMap<String,String>();
           try{
        	   List<Map<String,Object>> context=CommonUtil.convertJsonToList(templateValue);
        	   printHtmlContent = formService.getContentForPrintTemplate(formId, context.get(0), velocityEngine,pastValues,subFormAttrValues);
        	   //log.info("Form template print got successfully.");
        	   printHtmlContentMap.put("printHtmlContent", printHtmlContent);
           }catch(Exception e){
        	   e.printStackTrace();
               log.error("Error occur while get form print template "+e);
           }
           return printHtmlContentMap;
        }
	    @RequestMapping(value="bpm/form/showJspUpoladForm" ,method = RequestMethod.GET)
	    public  ModelAndView fileImportForm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
	    	MetaForm metaForm= new MetaForm();
	    	String formId = request.getParameter("formId");
	    	String formName = request.getParameter("formName");

	    	if(formId != null) {
	    		metaForm.setId(formId);
		    	model.addAttribute("formName",formName);
		    	model.addAttribute("formId",formId);

	    	}
	    	Module moduleObj=moduleService.getModule(request.getParameter("moduleId"));
	    	model.addAttribute("moduleId", moduleObj.getId());
	    	model.addAttribute("metaForm",metaForm);

	    	return new ModelAndView("form/createJspForm",model);
	    }
	    
	    @RequestMapping(value= "bpm/form/saveDynamicJspForm", method = RequestMethod.POST)
	    public ModelAndView saveDynamicJspForm(@ModelAttribute("metaForm") MetaForm metaForm, ModelMap model,HttpServletRequest request,BindingResult errors){
	    	 Map<String, String> formDetail = new HashMap<String, String>();
	    	 Locale locale = request.getLocale();
	    	String moduleId=request.getParameter("moduleId");
	    	String formId = request.getParameter("formId");

	    	boolean isSuccess = true;
	    	try{
	        	metaForm.setIsJspForm(true);
	        	if(!formId.isEmpty() && formId != null) {
	        		metaForm.setId(formId);
	        	} else {
	        		metaForm.setId(null);
	        	}
	        	 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	             if(user != null) {
	                 metaForm.setCreatedBy(user.getUsername());
	             }else{
	                 metaForm.setCreatedBy(Constants.EMPTY_STRING);
	             }
	        	String tableId=request.getParameter("tableId");
	        	
	        	MetaTable table = tableService.getTableById(tableId);
	        	metaForm.setTable(table);
	        	metaForm.setTableName(table.getTableName());
	        	formService.saveDynamicForm(metaForm, moduleId);
	        	formDetail.put("isFormShow", "true");
	            formDetail.put("formModuleId", moduleId);
	            model.addAttribute("isJspSuccess", isSuccess);
		        //log.info("New DynamicJspForm saved Successfully");
	        } catch (Exception e) {
	        	isSuccess = false;
	        	log.error("Error while saving a new form ",e);
	            e.printStackTrace();
	            model.addAttribute("isJspSuccess", isSuccess);
	        }
	    	return showFormsFormModule(model,request,moduleId,"true","true",true) ;
	    	// return new ModelAndView("redirect:formListForModule");
	    }
  
	    @Autowired
	    public void setVelocityEngine(VelocityEngine velocityEngine) {
	        this.velocityEngine = velocityEngine;
	    }
	    
	    /**
		 * List view name search
		 * @param listViewName
		 * @param appendTo
		 * @param model
		 * @param request
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="bpm/form/searchFromNames", method = RequestMethod.GET)
	    public @ResponseBody List<Map<String, String>> searchModuleTableNames(@RequestParam("fromName") String fromName,ModelMap model,HttpServletRequest request) throws Exception {
			List<Map<String, String>> fromNames = new ArrayList<Map<String, String>>();
	         try{
	        	 	fromNames = formService.searchModuleFromNames(fromName);
	        	 if (fromNames != null && !fromNames.isEmpty()){
	                 return fromNames;
	             }
	         }catch(Exception e){
	             log.error("Error while getting froms "+e.getMessage());
	         }
	         return new ArrayList<Map<String, String>>();
	    }
		
		@RequestMapping(value = "bpm/form/showJspSelection", method = RequestMethod.GET)
		public ModelAndView showJspSelection(ModelMap model,
				@RequestParam("selectionType") String selectionType,
				@RequestParam("appendTo") String appendTo,
				@RequestParam("selectedValues") String selectedValues,
				@RequestParam("rootNodeURL") String rootNodeURL,
				@RequestParam("callAfter") String callAfter, User user,
				BindingResult errors, HttpServletRequest request,
				HttpServletResponse response) {
			JSONArray nodes = new JSONArray();
			Map<String, Object> context = new HashMap<String, Object>();
			try {
				context.put("nodes", nodes);
				context.put("rootNodeURL", rootNodeURL);
				context.put("selectionType", selectionType);
				context.put("selection", "selectedJsp");
				context.put("selectedValues", selectedValues);
				context.put("needContextMenu", true);
				context.put("needTreeCheckbox", true);
				String script = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "organizationTree.vm", context);
				model.addAttribute("script", script);
				model.addAttribute("appendTo", appendTo);
				model.addAttribute("callAfter", callAfter);
				model.addAttribute("selectionType", selectionType);
				model.addAttribute("selectedValues", selectedValues);
				model.addAttribute("selection", "selectedJsp");

			} catch (BpmException e) {
				e.printStackTrace();
				log.warn(e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getMessage(), e);
			}
			return new ModelAndView("admin/organizationTree", model);
		}
		
		
		@RequestMapping(value = "bpm/form/getAllJspFromSource", method = RequestMethod.GET)
		public @ResponseBody List<Map<String, Object>> getAllJspFromSource(ModelMap model, HttpServletRequest request) {
			List<Map<String,Object>> filepathsList =  new ArrayList<Map<String, Object>>();
			Map<String,Object> filepaths = new HashMap<String, Object>();
			try {
				String path=getServletContext().getRealPath("/resources") + "/jsp/";
				File dir = new File(path);
				String[] extensions = new String[] { "jsp" };
				List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
				for (File file : files) {
					CommonUtil.createReportRootTreeNode(filepathsList,file.getName(),file.getName(),file.getName());
				}

			} catch (BpmException e) {
				e.printStackTrace();
				log.warn(e.getMessage(), e);
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getMessage(), e);
			}
			return filepathsList;
		}
}
