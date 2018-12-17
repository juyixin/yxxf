package com.eazytec.webapp.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.alxxgl.service.AllxService;
import com.eazytec.alxxgl.service.AlxxbDocumentService;
import com.eazytec.alxxgl.service.AlxxbService;
import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.service.DocumentFormService;
import com.eazytec.bpm.oa.dms.service.FolderService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.crm.model.Contract;
import com.eazytec.dao.SearchException;
import com.eazytec.dto.AllxDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.Folder;
import com.eazytec.model.LabelValue;
import com.eazytec.model.PermissionDTO;
import com.eazytec.model.User;
import com.eazytec.service.DepartmentExistsException;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;
import com.eazytec.util.PropertyReader;
import com.eazytec.webapp.controller.BaseFormController;

@Controller
public class AllxbController extends BaseFormController {

	protected final Log log = LogFactory.getLog(getClass());
	 
	@Autowired
	private AllxService allxService;
	
	@Autowired
	private AlxxbService alxxbService;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private DataDictionaryService dicService;
	
	@Autowired
    private FolderService folderService;
	 
	@Autowired
	private AlxxbDocumentService alxxbDocumentService;
	@Autowired
	private DocumentFormService documentFormService;

	@Autowired
	public void setAllxService(AllxService allxService) {
		this.allxService = allxService;
	}

	@Autowired
	public void setAlxxbService(AlxxbService alxxbService) {
		this.alxxbService = alxxbService;
	}
	
	@Autowired    
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	
   @Autowired    
    public void setDocumentFormService(DocumentFormService documentFormService) {
		this.documentFormService = documentFormService;
	}
   
   @Autowired    
   public void setAlxxbDocumentService(AlxxbDocumentService alxxbDocumentService) {
		this.alxxbDocumentService = alxxbDocumentService;
	}

	
	@RequestMapping(value = "bpm/alxxgl/alxxwh", method = RequestMethod.GET)
	public ModelAndView manageAlxxwh(HttpServletRequest request, ModelMap model,String code) {
		String script = null;
		List<Allx> allx = null;
		Allx a = null;
		List<Alxxb> alxxb = null;
		Locale locale = request.getLocale();
		String id = request.getParameter("id");
		String name = request.getParameter("searchText");
		try {
			//List<AllxDTO> allxList = allxService.getAllAllxDTO();
			if(name!=null&&name!=""){
      		  name=java.net.URLDecoder.decode(name, "utf-8");
      		  if(id!=null&&id!=""){
      			a = allxService.getAllxByLxdm(id);
				alxxb = alxxbService.getAllxByAllx2(a.getLxdm(),name);
      		  }else{
      			alxxb = alxxbService.getAllxByAllx1(name);
      		  }
			}else{
				
				allx = allxService.getAllxBySuperDepartmentId("Organization");
			}
			List<Allx> allxList = allxService.getAllAllx();
			 
			
			model.addAttribute(Constants.ALLX_LIST, allxList);
			String[] fieldNames = { "id","name", "dsr", "allx","bz"};
			script = generateScriptForAllxGridS(CommonUtil.getMapListFromObjectListByFieldNames(alxxb, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			saveError(request, getText("error.allx.delete", e.getMessage(), locale));
		}

		return new ModelAndView("alxxb/allxScriptTreeAlxxwh", model);
	}
	
	@RequestMapping(value = "bpm/allxgl/getAlxxbGrid", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getAllxGrid(@RequestParam("id") String id, @RequestParam("parentNode") String parentNode, HttpServletRequest request) {
		Locale locale = request.getLocale();
		String script = null;

		Allx allx = null;
		List<Alxxb> alxxb = null;
		try {
			log.info("Getting alxxbs for selected " + id + " : for : " + parentNode);
//			allx = allxService.getAllxBySuperDepartmentId(id);
			 
				allx = allxService.getAllxByLxdm(id);
				alxxb = alxxbService.getAllxByAllx(allx.getLxdm());
				if(alxxb!=null&&alxxb.size()>0){
					for(Alxxb a : alxxb){
						a.setAllx(allx.getLxmc());
					}
				}
			String[] fieldNames = { "id","name", "dsr", "allx","bz"};
			script = generateScriptForAllxGridS(CommonUtil.getMapListFromObjectListByFieldNames(alxxb, fieldNames, ""), velocityEngine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("报错", e.getMessage(), locale));
		}
		return script;
	}
	
	public static String generateScriptForAllxGridS(List<Map<String, Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','案例名称', '当事人', '案例类型', '备注']";
		
		context.put("title", "Groups");
		context.put("gridId", "ALXXB_LIST");
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
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showViewAlxxb", "false");
		CommonUtil.createFieldNameList(fieldNameList, "dsr", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "allx", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "100", "center", "", "false");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	

	// ========================================================================================================================================
		/**
		 * 记录新增
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "bpm/alxxb/alxxbForm", method = RequestMethod.GET)
		public ModelAndView showNewAlxxbForm(HttpServletRequest request,ModelMap model) {
			String id = request.getParameter("id");
			System.out.println(id);
			Allx allx = new Allx();
			Alxxb alxxb = new Alxxb();
			String fileFormat = (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.file.format");
			model.addAttribute("fileFormat", fileFormat);
			try{
				String code= "";
				if(id==null||id==""){
					 saveError(request, "请选择节点新建");
					 
					 return manageAlxxwh(request,model,code); 
				}
				if(id.equals("Organization")){
					 saveError(request, "请选择节点新建");
					 
					 return manageAlxxwh(request,model,code); 
				}
				
				
				String folderId = request.getParameter("folderId");
	    		if(folderId != null && !folderId.equalsIgnoreCase("null") ){
	    			Folder folder = folderService.getFolder(folderId);
	    			alxxb.setFolder(folder);
	    			model.addAttribute("folderId",folderId);
	    		}
				
				allx = allxService.getAllxByLxdm(id);
	    		model.addAttribute("alxxb", alxxb);
			
			}catch (Exception e) {
				e.printStackTrace();
			}	
			
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
			
			model.addAttribute("alxxb", alxxb);
			model.addAttribute("allx", allx);
			model.addAttribute("dicList", dicList);
			if( (!StringUtil.isEmptyString(request.getParameter("id")))) {
				return new ModelAndView("alxxb/alxxbForm4", model);
			}else {
				 String code= "";
				 return manageAlxxwh(request,model,code); 
			}
		 
		}
		   
		
		/**
		 * 记录编辑
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "bpm/alxxb/editAlxxbForm", method = RequestMethod.GET)
		public ModelAndView showEditAlxxbForm(HttpServletRequest request,ModelMap model) {
			String id = request.getParameter("id");
			Alxxb alxxb = new Alxxb();
			Allx allx = new Allx();
			String fileFormat = (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.file.format");
			model.addAttribute("fileFormat", fileFormat);
			try{
				alxxb = alxxbService.getAlxxbById(id);
				List<AlxxbDocument> documents=alxxbDocumentService.getDocumentsById(id);
				 if(documents !=null){
					 alxxb.setAlxxbDocument(documents);
				}
				 allx = allxService.searchlxmc(alxxb.getAllx());
				List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
				model.addAttribute("alxxb", alxxb);
				model.addAttribute("lxmc", allx.getLxmc());
				model.addAttribute("documents", documents);
				model.addAttribute("dicList", dicList);
			}catch (Exception e) {
				e.printStackTrace();
			}	
		
			return new ModelAndView("alxxb/alxxbForm6", model);
		 
		}
		
		
		
		/**
		 * 记录保存
		 * @param request
		 * @param contract
		 * @param model
		 * @param errors
		 * @return
		 */						 
		@RequestMapping(value = "bpm/alxxb/saveAlxxb", method = RequestMethod.POST)
		public ModelAndView saveAlxxb(ModelMap model,Alxxb alxxbForm,  BindingResult errors,HttpServletRequest request,
	            HttpServletResponse response) {
			String type1 = request.getParameter("type");
			String type="";
			//System.out.println(type1+"@@@@@@@@");
			if(type1.endsWith("?")){
				type=type1.replace("?","");
			}
		/*	 validator.validate(alxxbForm, errors);
			if (errors.hasErrors()) { // don't validate when deleting
				List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
				model.addAttribute("alxxb", alxxbForm);
				model.addAttribute("dicList", dicList);
				return new ModelAndView("alxxb/alxxbForm", model);
			}*/
		 
			String path=getServletContext().getRealPath("/resources") + "/allxb/";
			
			if(StringUtils.isBlank(alxxbForm.getId())){
				Date date = new Date();
				alxxbForm.setCreatedTime(date);
			}
			alxxbForm.setAllx(type);
		/*	
		 		String documentFormId = request.getParameter("id");
			List<AlxxbDocument> documents=alxxbDocumentService.getDocumentsById(documentFormId);
			alxxbForm.setAlxxbDocument(documents); 
*/
			alxxbForm = alxxbService.saveOrUpdateAlxxbForm(alxxbForm,alxxbForm.getFiles(), path);
			
			 
			model.addAttribute("alxxbForm",alxxbForm);
			 String code= "";
			 return manageAlxxwh(request,model,code); 
		}
		
	 
		
		/**
		 * 记录删除
		 * @param alxxbIds
		 * @param request
		 * @return
		 */
	    @RequestMapping(value = "bpm/alxxb/deleteSelectedAlxxb", method = RequestMethod.GET)
		public ModelAndView deleteSelectedAlxxb(@RequestParam("alxxbIds") String alxxbIds, HttpServletRequest request,ModelMap model) {
			
			List<String> alxxbIdList = new ArrayList<String>();
			if (alxxbIds.contains(",")) {
				String[] ids = alxxbIds.split(",");
				for (String id : ids) {
					alxxbIdList.add(id);
				}
			} else {
				alxxbIdList.add(alxxbIds);
			}

			alxxbService.removeAlxxb(alxxbIdList);
			 String code= "";
			 return manageAlxxwh(request,model,code); 
		}
	    
	    /**
	     * 记录编辑
	     * @param id
	     * @param model
	     * @return
	     */
	    @RequestMapping(value = "bpm/alxxb/editAlxxb",method = RequestMethod.GET)
	    public ModelAndView editAlxxb(@RequestParam("id") String id, ModelMap model){
	    	
	    	List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("contractType");
	    	
	      	Alxxb alxxb = alxxbService.getAlxxbById(id);
	    	model.put("alxxb", alxxb);
	    	model.addAttribute("dicList", dicList);
	        return new ModelAndView("alxxb/alxxbForm", model);   
	     }
	    
	    /**
	     * 详情页面
	     * @param id
	     * @param model
	     * @return
	     */
	    @RequestMapping(value = "bpm/alxxb/viewAlxxbForm",method = RequestMethod.GET)
		public ModelAndView viewAlxxbForm(ModelMap model,HttpServletRequest request) {
			Locale locale = request.getLocale();
			Alxxb alxxb = new Alxxb();
			try{
				if(!StringUtil.isEmptyString(request.getParameter("id"))){
					List<AlxxbDocument> documents=alxxbDocumentService.getDocumentsById(request.getParameter("id"));
					System.out.println(documents);
					 if(documents !=null){
						 alxxb.setAlxxbDocument(documents);
					} 
					alxxb = alxxbService.getAlxxbById(request.getParameter("id"));
		    		model.addAttribute("alxxb",alxxb);
		    		model.addAttribute("documents", documents);
		    	}
			}catch(Exception e){
				log.error(e.getMessage(),e);
				saveError(request, getText("error.document.view",locale));
			}			
			if(!StringUtil.isEmptyString(request.getParameter("id"))) {
				return new ModelAndView("alxxb/alxxbFormOverview", model);
			} else {
				return new ModelAndView("redirect:alxxwh",model);	
			}
				
		}
	    
		/**
		 * download Document Form page.
		 * 
		 * @param request
		 * @param response
		 * 
		 * @return
		 */
		@RequestMapping(value = "bpm/alxxb/downloadDocument",method = RequestMethod.GET)
		public void downloadDocument(HttpServletResponse response,
				HttpServletRequest request) {
			//log.info("Preparing to download Document");
			String fileName = null;
			AlxxbDocument document = new AlxxbDocument();
			Locale locale = request.getLocale();
			try {
				if (!StringUtil.isEmptyString(request.getParameter("id"))) {
					document = alxxbDocumentService.getDocumentById(request
							.getParameter("id"));
				}
				fileName = document.getName();
	            fileName = URLEncoder.encode(fileName, "UTF-8");
	            fileName = URLDecoder.decode(fileName, "ISO8859_1");
				response.setContentType(document.getMimeType());
				response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
				OutputStream o;
				o = response.getOutputStream();
			 	InputStream is = new FileInputStream(document.getPath());
				 IOUtils.copy(is, o);
				o.flush();
				o.close();
				log.info("File Name : "+fileName+" "+getText("download.success",locale));
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				saveError(request, getText("error.document.download",locale));
			}
		}
		 
		/**
		 * 删除附件
		 * 
		 * @param request
		 * @param response
		 * 
		 * @return
		 */
		
		@RequestMapping(value="bpm/alxxb/deleteDocument",method = RequestMethod.GET)
		public @ResponseBody ModelAndView deleteDocument(@RequestParam("documentId") String documentId,@RequestParam("documentFormId") String documentFormId,HttpServletRequest request){
	    	ModelMap model=new ModelMap();
	    	Locale locale = request.getLocale();
	    	model.addAttribute("id", documentFormId);
			if(!StringUtil.isEmptyString(documentId)){
				alxxbDocumentService.deleteDocumentById(documentId);
			}
			saveMessage(request, getText("success.document.delete",locale));
			return new ModelAndView("redirect:viewAlxxbForm",model);	
		}
	    
}