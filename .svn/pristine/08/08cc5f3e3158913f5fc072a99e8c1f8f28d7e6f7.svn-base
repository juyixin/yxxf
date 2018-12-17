package com.eazytec.webapp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.common.util.TableDefinitionUtil;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.Module;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;
import com.eazytec.util.JSTreeUtil;

/**
 * Does the form related actions like
 * save form, retrieve the form details
 * @author Rajmohan
 */
@Controller
public class TableController extends BaseFormController{
	
	 /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    private TableService tableService;
    private FormDefinitionService formService;
    public VelocityEngine velocityEngine;
    private ModuleService moduleService;
    
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
    
    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    /**
     * Form to create table
     * @param model
     * @return to creatTable.jsp
     * @throws JSONException 
     */
    @RequestMapping(value = "/bpm/table/manageTables",method = RequestMethod.GET)
     public ModelAndView manageTables(ModelMap model,HttpServletRequest request,@ModelAttribute("metaTable") MetaTable metaTable,BindingResult errors) {
    	 Locale locale = request.getLocale();
    	try{	
    	List<Module> moduleList = moduleService.getAllModuleList();
	    	List<MetaTable> tableList = tableService.getTables();
	        model.addAttribute("tableList", tableList);
	        model.addAttribute("tableTree", JSTreeUtil.getJsonTreeForModuleWithTableList(moduleList));
     } catch (Exception e) {
         log.error("Error while saving a new table ",e);
         saveError(request, getText("table.manage.error"+ e.getMessage(),locale));
     }
        return new ModelAndView("table/manageTables",model);
     }
    
    @RequestMapping(value = "/bpm/table/defaultTable",method = RequestMethod.GET)
    public ModelAndView defaultTable(ModelMap model,HttpServletRequest request,@ModelAttribute("metaTable") MetaTable metaTable,BindingResult errors) {
   	 Locale locale = request.getLocale();
   	try{	
    } catch (Exception e) {
        log.error("Error while saving a new table ",e);
        saveError(request, getText("table.manage.error"+ e.getMessage(),locale));
    }
       return new ModelAndView("table/tableEmpty",model);
    }
    
    /**
     * Form to create table
     * @param model
     * @return to creatTable.jsp
     */
    @RequestMapping(value = "/bpm/table/createTable",method = RequestMethod.GET)
	public ModelAndView createTable(ModelMap model,@ModelAttribute("metaTable") MetaTable metaTable,BindingResult errors, @RequestParam("enableRelationTab") String enableRelationTab, @RequestParam("isEdit") String isEdit,HttpServletRequest request) {
    	 if(metaTable.getId()==null){
	    	 List<MetaTable> tableList = tableService.getTables();
	    	 model.addAttribute("tableList", tableList);
	    	 Module moduleObj= null;
	    	 if(request.getParameter("moduleId")!=null){
	    		 moduleObj=moduleService.getModule(request.getParameter("moduleId"));
	    	 }else{
	    		 moduleObj=moduleService.getModuleByName("Default Module");
	    	 }
	    	 
	    	 model.addAttribute("moduleName", moduleObj.getName());
	     }
	     
	     model.addAttribute("isEdit", Boolean.valueOf(isEdit));
	     model.addAttribute("enableRelationTab", enableRelationTab);
	     return new ModelAndView("table/createTable",model);
	   }
    
    /**
     * Save the Table in meta and create a Runtime table.
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="/table/previewQuery", method = RequestMethod.POST)
    public @ResponseBody  Map<String,String> previewQuery(HttpServletRequest request)throws Exception {
        Locale locale = request.getLocale();
           try {

            Map<String,String> tableOperation=new HashMap<String, String>();
               List<Map<String,Object>> fieldProperties=CommonUtil.convertJsonToList(request.getParameter("fieldPropertiesList"));
               List<Map<String,Object>> parentTableList=CommonUtil.convertJsonToList(request.getParameter("parentTableList"));
               List<Map<String,Object>> subTableList=CommonUtil.convertJsonToList(request.getParameter("subTableList"));
               List<String> deleteExistingTableColumns=CommonUtil.convertJsonToListStrings(request.getParameter("deleteExistingTableColumns"));
               
               tableService.checkExistingTableColumnRelation(deleteExistingTableColumns);
               
               boolean isUpdate=Boolean.parseBoolean(request.getParameter("isUpdate").toString());
               tableOperation.put("previewQuery", tableService.createQuery(request.getParameter("tableName"),fieldProperties,parentTableList,subTableList,isUpdate,deleteExistingTableColumns) );
               return tableOperation;
        } catch (Exception e) {
            log.error("Error while saving a new table ",e);
            saveError(request, getText("table.created.error",locale));
        }
        return null;
    }
    
    /**
     * Save the Table in meta and create a Runtime table.
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="/bpm/table/saveTable", method = RequestMethod.POST)
    public ModelAndView saveTable(@ModelAttribute("metaTable") MetaTable metaTable,@RequestParam("isModuleChanged") boolean isModuleChanged,@RequestParam("isAutoFormCreate") boolean isAutoFormCreate, ModelMap model,HttpServletRequest request,BindingResult errors){
        Locale locale = request.getLocale();
        String moduleId="";
        try {
        	   metaTable.setIsAutoFormCreate(isAutoFormCreate);
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
               if(user != null) {
                   metaTable.setCreatedBy(user.getUsername());
               }else{
                   metaTable.setCreatedBy(Constants.EMPTY_STRING);
               }
               List<Map<String,Object>> fieldProperties=CommonUtil.convertJsonToList(request.getParameter("fieldPropertiesList"));
               List<Map<String,Object>> parentTableList=CommonUtil.convertJsonToList(request.getParameter("selectedParentTableList"));
               List<Map<String,Object>> subTableList=CommonUtil.convertJsonToList(request.getParameter("selectedSubTableList"));
               List<String> deleteParentTableList=CommonUtil.convertJsonToListStrings(request.getParameter("deleteParentTableList"));
               List<String> deleteSubTableList=CommonUtil.convertJsonToListStrings(request.getParameter("deleteSubTableList"));
               List<String> deleteExistingTableColumns=CommonUtil.convertJsonToListStrings(request.getParameter("deleteExistingTableColumns"));
              
               tableService.checkExistingTableColumnRelation(deleteExistingTableColumns);
               
               String moduleName=request.getParameter("_moduleName");
               Module module=moduleService.getModuleByName(moduleName);
               moduleId=module.getId();
               model.addAttribute("moduleId",moduleId);
  			   model.addAttribute("moduleName",module.getName());
  			   
               String tableId;
               String modeType;
               model.addAttribute("isEdit", true);
               if(metaTable.getId()!=""){
            	  modeType = "edit";
            	  tableId = tableService.createTable(metaTable,fieldProperties,parentTableList,subTableList,true,deleteExistingTableColumns,module);
            	  saveMessage(request, getText("table.altered",locale));
               }else{
	              modeType = "create";
	              metaTable.setTableName(metaTable.getTableName().toUpperCase());
                  tableId = tableService.createTable(metaTable,fieldProperties,parentTableList,subTableList,false,deleteExistingTableColumns,module);
                  saveMessage(request, getText("table.created",locale));
               }
               model.addAttribute("tableId", tableId);
               model.addAttribute("modeType", modeType);
               
               //Create a Form Automatically based on table data's
               if(isAutoFormCreate){
            	   String defaultColumns = "";
            	   String dataBase = tableService.getDataBaseSchema();
            	   if(dataBase.equals("mysql")){
              		 defaultColumns=getText("mysql.table.default.values",null);
              	   }else if(dataBase.equals("mssql")){
              		 defaultColumns=getText("mssql.table.default.values",null);
              	   }else if(dataBase.equals("oracle")){
              		 defaultColumns=getText("oracle.table.default.values",null);
              	   }
            	   
            	   List<MetaTableColumns> tableColumns = tableService.getAllMetaTableColumnsByTableId(tableId, defaultColumns);
            	   String formName = "Auto_"+metaTable.getTableName().toUpperCase().replaceAll(" ", "_");
            	   MetaForm metaForm = formService.getMetaFormByFormName(formName);
            	   if(metaForm == null){
            	 	   metaForm = new MetaForm();
            		   metaForm.setFormName(formName);
            		   String englishName = UUID.randomUUID().toString().substring(0, 13).replace('-', '3');
            		   metaForm.setEnglishName(englishName);
                	   metaForm.setCreatedBy(CommonUtil.getLoggedInUserId());
                	   metaForm.setCreatedByFullName(CommonUtil.getLoggedInUser().getFullName());
                	   metaForm.setCreatedOn(new Date());
                	   metaForm.setIsJspForm(false);
                	   metaForm.setIsDelete(false);
                	   metaForm.setTable(tableService.getTableById(tableId));
                	   metaForm.setTableName(tableService.getTableById(tableId).getTableName());
            	   }
            	   metaForm = tableService.setFormFieldResources(metaForm, tableColumns);
            	   metaForm = formService.saveDynamicForm(metaForm, module.getId());
               }
               
               if(!deleteParentTableList.isEmpty()){
            	   tableService.deleteParentTableList(metaTable,deleteParentTableList);
               }
               
               if(!deleteSubTableList.isEmpty()){
            	   tableService.deleteSubTableList(metaTable,deleteSubTableList);
               }
               
               model.addAttribute("isSystemModule", module.getIsSystemModule());
              if(modeType == "create"){
            	  //Redirect the page to table relation page while creation of table
            	  log.info("Table '"+metaTable.getTableName()+"' is created successfully");
            	  return isShowRelationTable(metaTable,model);
               }else{
            	   log.info("Table '"+metaTable.getTableName()+"' is altered successfully");
            	   if(isModuleChanged){
            		   
              		  model.addAttribute("isModuleChanged",isModuleChanged);
              		  model.addAttribute("tableModuleId",model.get("moduleId"));
              		  model.addAttribute("metaTable", metaTable);
              	  }
             	   model.addAttribute("enableRelationTab", "false");
             	   return defaultTable(model,request,metaTable,errors);
            	   //return manageTables(model,request,metaTable,errors);
               }
        }catch (Exception e) {
            //log.info("Error while saving a new table ",e);
            log.error(e.getMessage(),e);
            //saveError(request, getText("table.sql.error",e.getMessage(),locale));
            saveError(request,  "Error while creating table." + e.getMessage());
           
            if(metaTable.getId()!=null){
            	model.addAttribute("tableId", metaTable.getId());
            	 List<MetaTable> tableList = tableService.getNonRelationTables(metaTable.getId());
                 model.addAttribute("tableList", tableList);
                 MetaTable tempMetaTable=tableService.getTableDetails(metaTable.getId());
                 model.addAttribute("metaTable", tempMetaTable);
            }
            model.addAttribute("modeType", "create");
            model.addAttribute("hasError", "true");
            model.addAttribute("enableRelationTab", "false");
            return  createTable(model,metaTable,errors,"false","true",request);
        }
    }
    
    /**
     * Redirect the page to table relation page while create a new table
     * @param metaTable
     * @param model
     * @return
     */
    private ModelAndView isShowRelationTable(MetaTable metaTable,ModelMap model){
    	model.addAttribute("isTableRelation", "true");
    	//append the table to module child js-tree usage
    	//model.addAttribute("newTableModuleId",metaTable.getModule().getId());
  	  	model.addAttribute("metaTable", metaTable);
        model.addAttribute("enableRelationTab", true);
        List<MetaTable> tableList = tableService.getNonRelationTables(metaTable.getId());
        model.addAttribute("isFieldEdit", true);
        model.addAttribute("tableList", tableList);
  	    return new ModelAndView("/table/createTable", model);
    }
    
    /**
     * To check the table name is already exits.
     * @param table name
     * @return
     */
    @RequestMapping(value="/table/checkTableName", method = RequestMethod.GET)
      public @ResponseBody Map<String, String> checkTableName(@RequestParam("tableName") String tableName,@RequestParam("isCreateAutomaticForm") boolean isCreateAutomaticForm) {
    	
          try{
              Map<String,String> tableOperation = new HashMap<String, String>();
              boolean isTablePresent = tableService.checkTableName(tableName);
              String formName = "Auto_"+tableName;
              boolean isFormPresent = formService.isFormNamePresent(formName);
             // String tableId = request.getParameter("id");
             
             
              
              if(isCreateAutomaticForm) {
            	  if(isFormPresent) {
            		  tableOperation.put("tableOperationObj", "false" );
            	  } else if (!isFormPresent && isTablePresent) {
            		  tableOperation.put("tableOperationObj", "true" );
            	  }
              } else if (isTablePresent) {
        		  tableOperation.put("tableOperationObj", "true" );

              }
             
              return tableOperation;
          }catch(EazyBpmException e){
              log.error("Error while checking the table name "+e);
             // saveMessage("success.report.update"); 
          }
         return null;
      }

    /**
     *  Delete all the Form versions
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/deleteTableById", method = RequestMethod.GET)
    public RedirectView deleteTableById(@RequestParam("tableId") String tableId,@RequestParam("tableName") String tableName,@RequestParam("moduleId") String moduleId,ModelMap model,HttpServletRequest request) throws Exception {
        Locale locale = request.getLocale();
        try{
            String deleteTableResponce=tableService.deleteTableById(tableId);
            if(deleteTableResponce==null){
            	saveMessage(request, getText("table.delete",locale));
            	log.info("Table '"+tableName+"' is deleted successfully");
            }else{
            	saveError(request,deleteTableResponce);
            }
            
        }catch(EazyBpmException e){
            log.error(e.getMessage(), e);
            model.addAttribute("message", "Table details cannot be deleted:"+e.getMessage());
            saveError(request, getText("table.delete.error",locale));
        }
        return new RedirectView("/bpm/module/manageModuleRelation?isTableCancel=true&moduleId="+moduleId);
    }
    
    /**
     *  Displays all the table in grid
     * @param model
     * @return view containing list of table
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/getTableList", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    public @ResponseBody String getTableList(ModelMap model,HttpServletRequest request) throws BpmException {
        Locale locale = request.getLocale();
        String script=null;
        try{
            List<MetaTable> tableList = tableService.getTables();
            List<Map<String,Object>> mapListFromObjectList = formService.resolveFormDetailsListMap(CommonUtil.getMapListFromObjectList(tableList));

            script =   TableDefinitionUtil.generateScriptForTable(mapListFromObjectList,velocityEngine, locale);
        }catch(BpmException e){
            log.error(e.getMessage(), e);
            model.addAttribute("script", "Problem loading grid, check log files");
        }

        return script;
    }

    /**
     * To get the Table details by table Id through API
     * @param formId
     * @return
     */
    @RequestMapping(value="/bpm/table/showEditTable", method = RequestMethod.GET)
      public ModelAndView showEditTable(@RequestParam("tableId") String tableId, ModelMap model, @RequestParam("tableId") String enableRelationTab,@RequestParam("moduleId") String moduleId) throws BpmException{
         try{
             MetaTable metaTableObj = tableService.getTableDetails(tableId);
             List<MetaTable> tableList = tableService.getNonRelationTables(tableId);
             model.addAttribute("tableList", tableList);
             model.addAttribute("metaTable", metaTableObj);
             model.addAttribute("isEdit", true);
             model.addAttribute("moduleId", moduleId);
             model.addAttribute("moduleName",  moduleService.getModule(moduleId).getName());
             model.addAttribute("enableRelationTab", enableRelationTab);
         }catch(Exception e){
             log.error("Error while getting table by id "+e);
         }
         return new ModelAndView("/table/createTable", model);
      }
    
    /**
     * To get the Table details by table Id through API
     * @param formId
     * @return
     */
    @RequestMapping(value="/bpm/table/showFieldEdit", method = RequestMethod.GET)
      public ModelAndView showFieldEdit(@RequestParam("tableId") String tableId, ModelMap model, @RequestParam("enableRelationTab") String enableRelationTab,@RequestParam("moduleId") String moduleId,@RequestParam("isEdit") String isEdit) throws BpmException{
         try{
             MetaTable metaTableObj = tableService.getTableDetails(tableId);
             List<MetaTable> tableList = tableService.getNonRelationTables(tableId);
             model.addAttribute("tableList", tableList);
             model.addAttribute("metaTable", metaTableObj);
             model.addAttribute("moduleId", moduleId);
             model.addAttribute("isEdit", isEdit);
             model.addAttribute("moduleName",  moduleService.getModule(moduleId).getName());
             model.addAttribute("enableRelationTab", enableRelationTab);
             model.addAttribute("isFieldEdit", true);
             model.addAttribute("isTableRelation", "false");
         }catch(Exception e){
             log.error("Error while getting table by id "+e);
         }
         return new ModelAndView("/table/createTable", model);
      }
    
    /**
     * To get the table name and id as label value pair
     * @param formId
     * @return
     */
    @RequestMapping(value="/bpm/table/allTable", method = RequestMethod.GET)
      public @ResponseBody List<Map<String, String>> getAllTable(HttpServletRequest request) throws BpmException{
        List<Map<String, String>> tableDetailsList = new ArrayList<Map<String, String>>();
         try{
             List<LabelValue> allTables = tableService.getAllTable();
             if (allTables != null){
                 for(LabelValue table : allTables){
                     Map<String,String> tableDetail = new HashMap<String, String>();
                     tableDetail.put("tableName", table.getLabel());
                     tableDetail.put("id", table.getValue());
                     tableDetailsList.add(tableDetail);
                 }
                 return tableDetailsList;
             }
         }catch(Exception e){
             log.error("Error while getting all tables "+e);
         }
         return new ArrayList<Map<String, String>>();
      }
    
    /**
     * To get the parent table name and id as label value pair
     * @param formId
     * @return
     */
    @RequestMapping(value="/bpm/table/getTableWithParentTable", method = RequestMethod.GET)
      public @ResponseBody List<Map<String, String>> getTableWithParentTable(@RequestParam("tableId") String tableId,HttpServletRequest request) throws BpmException{
        List<Map<String, String>> tableDetailsList = new ArrayList<Map<String, String>>();
         try{
             List<LabelValue> allTables = tableService.getTableWithParentTable(tableId);
             if (allTables != null){
                 for(LabelValue table : allTables){
                     Map<String,String> tableDetail = new HashMap<String, String>();
                     tableDetail.put("tableName", table.getLabel());
                     tableDetail.put("id", table.getValue());
                     tableDetailsList.add(tableDetail);
                 }
                 return tableDetailsList;
             }
         }catch(Exception e){
             log.error("Error while getting parent tables "+e);
         }
         return new ArrayList<Map<String, String>>();
      }
    
    /**
     * To get the child table name and id as label value pair for give table id
     * @param formId
     * @return
     */
    @RequestMapping(value="/bpm/table/getTablesByParentTableId", method = RequestMethod.GET)
      public @ResponseBody List<Map<String, String>> getTablesByParentTableId(@RequestParam("parentTableId") String parentTableId, HttpServletRequest request) throws BpmException{
        List<Map<String, String>> tableDetailsList = new ArrayList<Map<String, String>>();
         try{
             List<LabelValue> allTables = tableService.getTablesByParentTableId(parentTableId);
             if (allTables != null){
                 for(LabelValue table : allTables){
                     Map<String,String> tableDetail = new HashMap<String, String>();
                     tableDetail.put("tableName", table.getLabel());
                     tableDetail.put("id", table.getValue());
                     tableDetailsList.add(tableDetail);
                 }
                 return tableDetailsList;
             }
         }catch(Exception e){
             log.error("Error while getting all tables "+e);
         }
         return null;
      }
    
    /**
     * To get the table column name and id as label value pair by table id
     * @param formId
     * @return
     */
    @RequestMapping(value="/bpm/table/getAllColumnByTableId", method = RequestMethod.GET)
      public @ResponseBody List<Map<String, String>> getAllColumnByTableId(@RequestParam("tableId") String tableId,@RequestParam("fieldType") String fieldType, HttpServletRequest request) throws BpmException{
        List<Map<String, String>> tableColumnDetailsList = new ArrayList<Map<String, String>>();
         try{
        	 String defaultColumns= "";
        	 String dataBase = tableService.getDataBaseSchema();
        	 if(dataBase.equals("mysql")){
        		 defaultColumns=getText("mysql.table.default.values",null);
        	 }else if(dataBase.equals("mssql")){
        		 defaultColumns=getText("mssql.table.default.values",null);
        	 }else if(dataBase.equals("oracle")){
        		 defaultColumns=getText("oracle.table.default.values",null);
        	 }
             
             String selectDataType = TableDefinitionUtil.getDatatypeFromFieldtype(fieldType);
             List<LabelValue> tabelColumns = tableService.getAllColumnByTableId(tableId,defaultColumns,selectDataType);
             if (tabelColumns != null){
                 for(LabelValue tableColumn : tabelColumns){
                     Map<String,String> tableColumnDetail = new HashMap<String, String>();
                     tableColumnDetail.put("columnName", tableColumn.getLabel());
                     tableColumnDetail.put("id", tableColumn.getValue());
                     tableColumnDetailsList.add(tableColumnDetail);
                 }
                 return tableColumnDetailsList;
             }
         }catch(Exception e){
             log.error("Error while getting all columns of table "+e);
         }
         return null;
      }
    
    /**
     * To get the table column name and id as label value pair by table id with default columns
     * @param tableId
     * @return
     */
      @RequestMapping(value="/bpm/table/getAllColumnIncludeDefaultByTableId", method = RequestMethod.GET)
      public ModelAndView getAllColumnIncludeDefaultByTableId(ModelMap model,@RequestParam("tableId") String tableId, @RequestParam("parentDivId") String parentDivId, @RequestParam("subTableId") String subTableId){
         List<Map<String, String>> tableColumnDetailsList = new ArrayList<Map<String, String>>();
         try{
             List<LabelValue> tabelColumns = tableService.getAllColumnIncludeDefaultByTableId(tableId);
             if (tabelColumns != null){
                 for(LabelValue tableColumn : tabelColumns){
                     Map<String,String> tableColumnDetail = new HashMap<String, String>();
                     tableColumnDetail.put("columnName", tableColumn.getLabel());
                     tableColumnDetail.put("id", tableColumn.getValue());
                     tableColumnDetailsList.add(tableColumnDetail);
                 }
                 model.addAttribute("tableDetails",tableColumnDetailsList);
                 model.addAttribute("tableId",tableId);
                 model.addAttribute("parentDivId", parentDivId);
                 model.addAttribute("subTableId",subTableId);
             }else{
            	 model.addAttribute("errorMsg", "Error in loading the table columns.");
             }
         }catch(Exception e){
             log.error("Error while getting all columns of table "+e);
         }
         return new ModelAndView("table/showTableColumns", model);
      }
    
     /**
      * To get the table column name and id as label value pair by table id with default columns
      * @param tableId
      * @return
      */
      @RequestMapping(value="/bpm/table/allTableColumns", method = RequestMethod.GET)
      public @ResponseBody List<Map<String, String>> allTableColumns(ModelMap model,@RequestParam("tableId") String tableId){
    	  List<Map<String, String>> tableColumnDetailsList = new ArrayList<Map<String, String>>();
          try{
              List<LabelValue> tabelColumns = tableService.getAllColumnIncludeDefaultByTableId(tableId);
              if (tabelColumns != null){
                  for(LabelValue tableColumn : tabelColumns){
                      Map<String,String> tableColumnDetail = new HashMap<String, String>();
                      tableColumnDetail.put("columnName", tableColumn.getLabel());
                      tableColumnDetail.put("id", tableColumn.getValue());
                      tableColumnDetailsList.add(tableColumnDetail);
                  }
                  return tableColumnDetailsList;
              }else{
             	 model.addAttribute("errorMsg", "Error in loading the table columns.");
              }
          }catch(Exception e){
              log.error("Error while getting all columns of table "+e);
          }
          
          return new ArrayList<Map<String, String>>();
       }
      
    /**
     *  Export table data as CSV
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/exportTable", method = RequestMethod.GET)
    public void exportTableById(@RequestParam("tableId") String tableId,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Locale locale = request.getLocale();

        MetaTable metaTableObj = tableService.getTableDetails(tableId);
        Set<MetaTableColumns> metaTableColumnsSet =  metaTableObj.getMetaTableColumns();

         //convert the josn data into list of map data
         try {
         List<String> columnName = new ArrayList<String>();
         for(MetaTableColumns metaTableColumns : metaTableColumnsSet){
        	 //if(!metaTableColumns.getName().equalsIgnoreCase("id")) {
        		 columnName.add(metaTableColumns.getName());
        	 //}
         }
         List<Map<String,Object>> tableDumpList=tableService.getTableDump(metaTableObj.getTableName(),columnName);
         List<String> colmnName = new ArrayList<String>();
         List<List<String>> rows = new ArrayList<List<String>>();
         int count=0;
         //Create the header name and grid data for CSV Export
        for(Map<String,Object> rowMap : tableDumpList){
        	 List<String> rowData = new ArrayList<String>();
             for(String colName : columnName){
                 if(rowMap.containsKey(colName)){
                     //Getting Header names
                     if(count==0){
                    	 colmnName.add(colName);
                     }
                     //Getting row values
                     if(rowMap.get(colName)==null){
                    	 rowData.add("");
                     }else{
                    	 rowData.add(String.valueOf(rowMap.get(colName)));
                     }
                }
             }
             count++;
             rows.add(rowData);
         }

        Date date = new Date();
        //Get current date string and append with file name
        String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename="+metaTableObj.getTableName()+"_"+currentDate+".xls");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet newSheet = wb.createSheet(metaTableObj.getTableName()+"_"+currentDate);
               
        HSSFRow header = newSheet.createRow(0);
        
        int cellCount=0;
        for(String cellValue : colmnName){
            header.createCell(cellCount).setCellValue(cellValue);
            cellCount++;
        }
        
        int rowcount=1;
        for(List<String> row : rows){
     		cellCount=0;
     		HSSFRow cellRow = newSheet.createRow(rowcount);     	
     		for(String cellValue : row){
     			cellRow.createCell(cellCount).setCellValue(cellValue);
     			cellCount++;
     		}
     		rowcount++;
         }
        
        
        wb.write(response.getOutputStream());    
        
        } catch (Exception e) {
            saveError(request, getText("table.dataCSV.export.error",e.getMessage(),locale));
        }
    }
    
    /**
     *  Export table data as sql query dump
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/exportTableSql", method = RequestMethod.GET)
    public void  exportTableSql(@RequestParam("tableId") String tableId,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
          Locale locale = request.getLocale();
          try {
        	  tableService.exportTableSql(tableId,response);
           }catch(Exception e) {
                 saveError(request, getText("table.dataSQL.export.error",e.getMessage(),locale));
            }
    }
    
    /**
     * Import table data as sql query dump
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/importSQL", method = RequestMethod.POST)
     public RedirectView  importSQL(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
          Locale locale = request.getLocale();
          String fileNameWithPath="";
          boolean isMulti =false;
          MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        Map<String, List<MultipartFile>> fileInList = multipartRequest.getMultiFileMap();
	        List<MultipartFile> files =fileInList.get("file");
	        String fileName="";
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
	                OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename());
	                int bytesRead;
	                byte[] buffer = new byte[8192];
	                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	                    bos.write(buffer, 0, bytesRead);
	                }
	                bos.close();
	                // close the stream
	                stream.close();
	                fileNameWithPath = dirPath.getAbsolutePath() + Constants.FILE_SEP + multipartFile.getOriginalFilename();
	                fileName=multipartFile.getOriginalFilename();
	            }
	        }
	        String moduleQuery="";
	        if(fileName!=""){
	        		String[] fileNames=fileName.split("-");
	        		if(fileNames.length==2){
        				String[] table_Id=fileNames[0].split("_");
		        		String tableId=table_Id[table_Id.length-1];
		        		MetaTable metaTable=tableService.getTableDetails(tableId);
		        		if(metaTable==null){
		        			moduleQuery="insert into `ETEC_MODULE_TABLES`(`table_id`,`module_id`) values ('"+tableId+"','"+request.getParameter("module_Id")+"');";
		        		}else{
		        			saveError(request, getText("table.dataSQL.import.error","Table already exists",locale));
			        		return new RedirectView("/bpm/module/manageModuleRelation?isTableCancel=true&moduleId="+request.getParameter("module_Id"));
		        		}
		        	}else if(fileNames[0].contains("Multi_Table_Dump_")){
		        		isMulti = true;
		        		 BufferedReader bf = new BufferedReader(new FileReader(fileNameWithPath));
		        		 String komut = "";
			                String tableNames = "";
			                String tableIds = "";
			                int i=0;
			                while ((komut = bf.readLine()) != null) {
			                   if (komut.length() != 0) {
			                        if (komut.charAt(komut.length() - 1) == ';') {
			                        	if(i==0){
			                        		tableNames=komut;
			                        	}
			                        	if(i==1){
			                        		tableIds=komut;
			                        	}
			                        	i++;
			                        }
			                   }
			                }
			                
			                 if(tableNames!="" && !tableNames.isEmpty()){
			                	tableNames =tableNames.replaceAll("--", " ");
			                	List<MetaTable> metaTable=tableService.getTableDetailsByNames(tableNames);
			                    String[] moduleName=fileName.split("-");
			                	if(!metaTable.isEmpty()){
			                		saveError(request, getText("errors.import.existing.moduletable",moduleName[0],locale));
			                		return new RedirectView("/bpm/module/manageModuleRelation?isTableCancel=true&moduleId="+request.getParameter("module_Id"));
			                	}else{
			                		tableIds = tableIds.replaceAll("--", " ");
			                		tableIds = tableIds.replaceAll("moduleId", "'"+request.getParameter("module_Id")+"'");
			                		 if(!tableService.getDataBaseSchema().equals("mysql")){
		                				 tableIds = tableIds.replace("),(", ")-(");
			                			 String[] tableIdArray = tableIds.split("-");
			                			 for(String tableId :tableIdArray){
			                				 moduleQuery += "insert into `ETEC_MODULE_TABLES`(`table_id`,`module_id`) values "+tableId+";";
			                			 }
			                			 
			                		 }else{
			                			 moduleQuery="insert into `ETEC_MODULE_TABLES`(`table_id`,`module_id`) values "+tableIds;
			                		 }
			                	}
			                }
			                
		        	}else{
		        		saveError(request, getText("table.dataSQL.import.error","Wrong sql file",locale));
		        		return new RedirectView("/bpm/module/manageModuleRelation?isTableCancel=true&moduleId="+request.getParameter("module_Id"));
		        	}
	        }
	        
	        if(!tableService.getDataBaseSchema().equals("mysql")){
	        	 BufferedReader bf = new BufferedReader(new FileReader(fileNameWithPath));
	        	 String queryString = "";
	        	 try {
	        		 int i=0;
		        	 while ((queryString = bf.readLine()) != null) {
		                   if (queryString.length() != 0) {
		                	   if (queryString.charAt(queryString.length() - 1) == ';') {
		                		 	StringBuffer tempQuery = new StringBuffer(queryString);
		                		 	if(tempQuery.lastIndexOf(";")>0){
		                		 		tempQuery.deleteCharAt(tempQuery.lastIndexOf(";"));
		                    		}
		                		   tableService.executeModuleRelationQuery(tempQuery.toString().replaceAll("`", ""));
		                	   }
		                   }
		                   i++;
		                }
		        	 
		        	 if(isMulti){
		        		 String[] moduleQuerys = moduleQuery.split(";");
		        		 for(String moduleQry : moduleQuerys){
		        			 tableService.executeModuleRelationQuery(moduleQry.replaceAll("`", ""));
		        		 }
		        	 }else{
		        		 StringBuffer tempQuery = new StringBuffer(moduleQuery.replaceAll("`", ""));
		        	 	 if(tempQuery.lastIndexOf(";")>0){
		        	 		tempQuery.deleteCharAt(tempQuery.lastIndexOf(";"));
	            		 }
		        	 	 tableService.executeModuleRelationQuery(tempQuery.toString());
		        	 }
	        	 	 
	                 saveMessage(request, getText("table.dataSQL.import",locale));
	        	 }catch(Exception e) {
                    saveError(request, getText("table.dataSQL.import.error",e.getMessage(),locale));
	        	 }
	        }else{
	        	String importCommand = tableService.getMysqlImportCommand();
	   	        String[] executeCmd = new String[]{importCommand+" "+fileNameWithPath};
	            
	               Process runtimeProcess;
	               try {
	                   runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	                   int processComplete = runtimeProcess.waitFor();
	                   if (processComplete >= 0) {
	                       tableService.executeModuleRelationQuery(moduleQuery);
	                       saveMessage(request, getText("table.dataSQL.import",locale)); 
	                   }
	             }catch(Exception e) {
	                    saveError(request, getText("table.dataSQL.import.error",e.getMessage(),locale));
	                    return new RedirectView("/bpm/module/manageModuleRelation");
	               }
	        }
	     
          return new RedirectView("/bpm/module/manageModuleRelation?isTableCancel=true&moduleId="+request.getParameter("module_Id"));
    }
    
    /**
     *  Import table data as sql query dump
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/importCSV", method = RequestMethod.POST)
     public ModelAndView importCSV(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
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
	               
	                fileName=multipartFile.getOriginalFilename();
	                // write the file to the file specified
	                OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename());
	                int bytesRead;
	                byte[] buffer = new byte[8192];
	                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	                    bos.write(buffer, 0, bytesRead);
	                }
	                bos.close();
	                // close the stream
	                stream.close();
	                fileNameWithPath = dirPath.getAbsolutePath() + Constants.FILE_SEP + multipartFile.getOriginalFilename();
	            }
	        } 
	       
	        Locale locale = request.getLocale();
	        try{
	        	//tableService.importCSVDatas(fileNameWithPath,request.getParameter("importTableName"),fileName);
	        	tableService.importExcelDatas(fileNameWithPath,request.getParameter("importTableName"),fileName);	        	
	        	 saveMessage(request, getText("table.dataCVS.import",locale));
	        }catch (Exception e) {
	        	 saveError(request, getText("table.dataCVS.import.error",e.getMessage(),locale));
	     	}
	        model.addAttribute("modeType", "create");
            model.addAttribute("hasError", "true");
            model.addAttribute("enableRelationTab", "false");
            MetaTable metaTable = new MetaTable();
            return  createTable(model,metaTable,null,"false","true",request);
    }
        
        
	@RequestMapping(value="bpm/table/{methodType}", method = RequestMethod.GET)
	public ModelAndView importDataSorce(@PathVariable String methodType,ModelMap model) {
		if(methodType.contentEquals("importSQLDump")){
			 return new ModelAndView("table/importTableDump", model);	
		}else{
	        return new ModelAndView("table/importCSVDump", model);	
		}
	}
	
	@RequestMapping(value="bpm/table/exportMultiTables", method = RequestMethod.GET)
	public ModelAndView importDataSorce(ModelMap model) {
		 return new ModelAndView("table/exportMultiTables", model);	
	}
	
	@RequestMapping(value="bpm/table/multiTableSqlDump", method = RequestMethod.GET)
	public void multiTableSqlDump(ModelMap model,HttpServletRequest request,HttpServletResponse response,@RequestParam("tableNames") String tableNames) throws Exception {
		 Locale locale = request.getLocale();
         String dataBaseName=getText("table.dataBase.name",null);
         String bdUserName=getText("table.dataBase.userName",null);
         String bdPassword=getText("table.dataBase.password",null);
         Date date = new Date();
         String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
         response.setContentType("text/x-sql");
         response.setHeader("Content-Disposition", "attachment; filename=Multi_Table_Dump_"+currentDate+".sql");
         OutputStream dumpOutPut = response.getOutputStream();
           try{
        	   
        	   	  List<String> dataBaseInfo=new ArrayList<String>();
		          dataBaseInfo.add(dataBaseName);
		          dataBaseInfo.add(bdUserName);
		          dataBaseInfo.add(bdPassword);
		          tableService.constructMultiTableDump(dumpOutPut,tableNames,dataBaseInfo);
             
           }catch(Exception e) {
                saveError(request, getText("table.dataSQL.export.error",e.getMessage(),locale));
           }
	}
    /**
     *  This is to get all the data's of the table
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="bpm/table/showTableDatas", method = RequestMethod.GET)
	public @ResponseBody ModelAndView showTableDatas(@RequestParam("tableName") String tableName,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 String script=null;
    	 Locale locale = request.getLocale();
    	 try{
    		 script=tableService.getListViewByTableName(tableName,false);
    		 model.addAttribute("script", script);
    	 }catch(BpmException e){
             log.error(e.getMessage(), e);
             saveError(request, getText("list.view.error",e.getMessage(),locale));
             model.addAttribute("script", "Problem loading grid, check log files");
         }catch(Exception e){
        	 e.printStackTrace();
         }
         return new ModelAndView("table/tableGrid",model);
	}
    
    /**
     *  This is to get all the data's of the table by given query
     * @param model
     * @return view containing list of form
     * @throws Exception
     */
    @RequestMapping(value="/bpm/table/generateListViewByQuery",method = RequestMethod.GET)
    public @ResponseBody ModelAndView generateListViewByQuery(@RequestParam("constructedQuery") String constructedQuery,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String script=null;
    	Locale locale = request.getLocale();
	   	 try{
	   		 script=tableService.getListViewByTableName(constructedQuery,true);
	   		 model.addAttribute("script", script);
	   	 }catch(BpmException e){
	            log.error(e.getMessage(), e);
	            saveError(request, getText("list.view.error",e.getMessage(),locale));
	            model.addAttribute("script", "Problem loading grid, check log files");
	        }catch(Exception e){
	       	 e.printStackTrace();
	        }
	        return new ModelAndView("table/tableGrid",model);
    }
    
    /**
     * To copy the table to modules
     * @param tableId
     * @param moduleId
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/bpm/table/copyTableToModule",method = RequestMethod.GET)
    public RedirectView copyTableToModule(@RequestParam("tableId") String tableId,@RequestParam("moduleId") String  moduleId,@RequestParam("currentModuleName")String currentModuleName,@RequestParam("moduleName") String moduleName,@RequestParam("tableName") String tableName,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	Locale locale = request.getLocale();
	   	 try{
	   		 MetaTable tableObj=tableService.getTableById(tableId);
	   		 String copyStatus=tableService.copyTableToModule(moduleId,tableId);
	   		if(copyStatus.equals("true")){
				saveMessage(request, getText("table.copied.successfully",locale));
				log.info("Table '"+tableName+"' Copied From Module:'"+currentModuleName+"' To Module:'"+moduleName+"' successfully");
			}else if(copyStatus.equals("false")){
				saveError(request,getText("table.copied.failed",locale));
				log.error("Table Copied failed");
			}else{
				saveError(request,getText("table.copied.already.exits",copyStatus,locale));
				log.error("Table already exits");
			}
            
	   	 }catch(BpmException e){
	            log.error(e.getMessage(), e);
	            saveError(request,getText("table.copied.failed",locale));
        }catch(Exception e){
       	 e.printStackTrace();
        }
	        return new RedirectView("/bpm/module/manageModuleRelation");
    }

    @RequestMapping(value="/bpm/table/deleteDataByIds",method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> deleteDataByIds(@RequestParam("ids") String ids, String tableName, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
		Map<String,Object> message = new HashMap<String, Object>();
		try {
		    List<String> idList=CommonUtil.convertJsonToListStrings(ids);
		    tableService.deleteDataByIds(idList, tableName);
			message.put("successMessage", "记录删除成功");
		} catch (EazyBpmException e1) {
			message.put("errorMessage", "记录删除失败");
			e1.printStackTrace();
		}
		 return message;
    }
    
    /**
	 * Table name search
	 * @param listViewName
	 * @param appendTo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="bpm/table/searchModuleTableNames", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> searchModuleTableNames(@RequestParam("tableName") String tableName,ModelMap model,HttpServletRequest request) throws Exception {
		List<Map<String, String>> tableNames = new ArrayList<Map<String, String>>();
         try{
        	 	tableNames = tableService.searchModuleTableNames(tableName);
        	 if (tableNames != null && !tableNames.isEmpty()){
                 return tableNames;
             }
         }catch(Exception e){
             log.error("Error while getting list view "+e.getMessage());
         }
         return new ArrayList<Map<String, String>>();
    }
	/**
	 * <p> To get all table names for search operation </p>
	 * @param tableName
	 * @param appendTo
	 * @param model
	 * @param request
	 * @return tableList
	 * @throws Exception
	 */
	
	@RequestMapping(value="/bpm/table/getTableNames", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> getTableNames(@RequestParam("tableName") String tableName,@RequestParam("appendTo") String appendTo,ModelMap model,HttpServletRequest request) throws Exception {
		List<Map<String, String>> tableDetails = new ArrayList<Map<String, String>>();
         try{
             List<String> tables = tableService.getTableNames(tableName);  
        	 if (tables != null){
        		 
                 for(String table : tables) {
                	 
                     Map<String,String> tableDetail = new HashMap<String, String>();
                    // if(table.startsWith("ETEC_") || table.startsWith("QRTZ_")) {
                    	 tableDetail.put("tableName", table+" AS "+table);
                         tableDetail.put("id", table+"AS"+table);
                    /* } else {
                    	 tableDetail.put("tableName", "`"+table+"` AS `"+table+"`");
                         tableDetail.put("id", table+"AS"+table);
                     }*/
                     
                    /* userDetail.put("tableName", "`"+table.getLabel()+"` "+"AS"+" `"+table.getLabel()+"`");
                     userDetail.put("id",  table.getLabel()+" "+"AS"+" "+table.getLabel());*/
                     tableDetails.add(tableDetail);
                 }
                 return tableDetails;
             }
//        	 log.info("All User Retrived Successfully");
         }catch(Exception e){
             log.error("Error while getting list view "+e.getMessage());
         }
         return new ArrayList<Map<String, String>>();
    }
	
	
	
	@RequestMapping(value="/bpm/table/getDataForUniqueKey", method = RequestMethod.GET)
    public @ResponseBody  Map getDataForUniqueKey(@RequestParam("uniquecolumn") String uniquecolumn,@RequestParam("uniqueKey") String uniqueKey,@RequestParam("tableName") String tableName,HttpServletRequest request) throws Exception {
		Map tableData = null ;
		try{
        	 tableData = tableService.getDataForUniqueKey(uniquecolumn,uniqueKey,tableName);
        	 
         }catch(Exception e){
             log.error("Error while getting list view "+e.getMessage());
             e.printStackTrace();
         }
		return tableData;
    }
}
