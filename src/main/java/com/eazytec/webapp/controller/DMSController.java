package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.common.util.FileUtils;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.service.DocumentFormService;
import com.eazytec.bpm.oa.dms.service.FolderService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.Department;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.FileImportForm;
import com.eazytec.model.Folder;
import com.eazytec.model.FolderPermission;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.PermissionDTO;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.service.UserService;
import com.eazytec.util.PropertyReader;


@Controller
public class DMSController  extends BaseFormController {
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    private DocumentFormService documentFormService;
    
   // private ImportFileFormService importFileFormService;
    
    private FolderService folderService;
    
    public VelocityEngine velocityEngine;
    
    private GroupService groupService;
    
    private DepartmentService departmentService;
    
    private UserService userService;
    
    private UserManager userManager;
    
    @Autowired
	private FileManageService fileManageService;
    
    @Autowired
	public void setUserManager(UserManager userManager) {
	    this.userManager = userManager;
	}
     
    @Autowired    
    public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
    
    @Autowired    
    public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
    @Autowired    
    public void setUserService(UserService userService) {
		this.userService = userService;
	}
    
    @Autowired    
    public void setDocumentFormService(DocumentFormService documentFormService) {
		this.documentFormService = documentFormService;
	}

    @Autowired    
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
    
    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

	/**
	 * show Document Form page.
	 * 
	 * @param model
	 * @param schedule
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/documentForm",method = RequestMethod.GET)
	public ModelAndView showDcoumentForm(ModelMap model,
			HttpServletRequest request) {
		//log.info("User Name : "+user.getFirstName()+" "+user.getLastName()+" "+getText("add.success",locale));
		//log.info("Opening Document form to create");
		Locale locale = request.getLocale();
		String fileFormat = (String) PropertyReader.getInstance()
		.getPropertyFromFile("String", "system.file.format");
		model.addAttribute("fileFormat", fileFormat);
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				PermissionDTO permissionDTO;
				DocumentForm documentForm = documentFormService.getDocumentForm(request.getParameter("id"));    
				if(documentForm.getFolder() != null){
					model.addAttribute("folderId", documentForm.getFolder().getId());
				}
				setDocumentPermissionGrid(model, documentForm.getId());
				//Getting permissions for delete while editing document form
				if (request.isUserInRole(Constants.ADMIN_ROLE) || documentForm.getCreatedBy().equalsIgnoreCase(request.getRemoteUser())) {
					permissionDTO=new PermissionDTO(true, true, true, true, true, true);
		      	}else {
					permissionDTO = documentFormService.getPermission(documentForm,CommonUtil.getLoggedInUser());
						if(!permissionDTO.getCanDelete()) {
							List<DMSRolePermission> dmsUserRolePermissions = folderService.getDmsUserRolePermission(request.getRemoteUser(),documentForm.getFolder().toString());
							for (DMSRolePermission dmsUserRolePermission : dmsUserRolePermissions) {
								if(dmsUserRolePermission.getCanDelete()) {
									permissionDTO.setCanDelete(true);
								}
							}
						}
		      	}
				documentForm.setPermissionDTO(permissionDTO);
	    		model.addAttribute("documentForm",documentForm);
	    	}else{
	    		DocumentForm documentForm = new DocumentForm();
	    		String folderId = request.getParameter("folderId");
	    		if(folderId != null && !folderId.equalsIgnoreCase("null") ){
	    			Folder folder = folderService.getFolder(folderId);
	    			documentForm.setFolder(folder);
	    			model.addAttribute("folderId",folderId);
	    		}
	    		model.addAttribute("documentForm", documentForm);
	    	}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.get",locale));
		}
		if( (!StringUtil.isEmptyString(request.getParameter("id"))) || (!StringUtil.isEmptyString(request.getParameter("folderId")))) {
			return new ModelAndView("dms/documentForm", model);
		}else {
			return new ModelAndView("redirect:showManageDms",model);	
		}
	}
	
	/**
	 * view Document Form page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/viewDocumentForm",method = RequestMethod.GET)
	public ModelAndView viewDocumentForm(ModelMap model,
			HttpServletRequest request) {
		//log.info("Opening Document form to create");	
		Locale locale = request.getLocale();
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				PermissionDTO permissionDTO;
				DocumentForm documentForm = documentFormService.getDocumentForm(request.getParameter("id"));   
				
				if (request.isUserInRole(Constants.ADMIN_ROLE) || documentForm.getCreatedBy().equalsIgnoreCase(request.getRemoteUser())) {
					permissionDTO=new PermissionDTO(true, true, true, true, true, true);
		      	}else{
		      		//Getting Document Form permission
		      		permissionDTO = documentFormService.getPermission(documentForm,CommonUtil.getLoggedInUser());
		      		//Getting Folder permission if document form permission is not set
		      			if(!permissionDTO.getCanEdit()  || !permissionDTO.getCanDownload() || !permissionDTO.getCanDelete() || !permissionDTO.getCanPrint()) {
							permissionDTO = documentFormService.getFolderPermission(permissionDTO,request.getRemoteUser(),documentForm.getFolder().toString());
		      			}
		       	}
				documentForm.setPermissionDTO(permissionDTO);
	    		model.addAttribute("documentForm",documentForm);
	    	}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.view",locale));
		}			
		if(!StringUtil.isEmptyString(request.getParameter("id"))) {
			return new ModelAndView("dms/documentFormOverview", model);
		} else {
			return new ModelAndView("redirect:showManageDms",model);	
		}
			
	}
	
	
	/**
	 * delete Document Form page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/deleteDocumentForm",method = RequestMethod.GET)
	public String deleteDocumentForm(ModelMap model,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				DocumentForm documentForm = documentFormService.getDocumentForm(request.getParameter("id"));   
				documentFormService.deleteDocumentForm(documentForm);
				saveMessage(request,"Document Deleted Successfully");
	    	}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.delete",locale));
		}
		return "redirect:showManageDms";
	}
	
	/**
	 * show Manage Document Form page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/showDMS",method = RequestMethod.GET)
	public ModelAndView showDMSPage(ModelMap model,
			HttpServletRequest request) {
		//log.info("Opening Document form to create");
		Locale locale = request.getLocale();
		try{
			 String script=getDocumentGridScript(request);
			 model.addAttribute("script",script);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.folder.get",locale));
		}
		return new ModelAndView("dms/dms", model);
	}
	
	/**
	 * download Document Form page.
	 * 
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/downloadDocument",method = RequestMethod.GET)
	public void downloadDocument(HttpServletResponse response,
			HttpServletRequest request) {
		//log.info("Preparing to download Document");
		String fileName = null;
		Document document = new Document();
		Locale locale = request.getLocale();
		FileManage fileManage = new FileManage();
		try {
			if (!StringUtil.isEmptyString(request.getParameter("id"))) {
				document = documentFormService.getDocumentById(request
						.getParameter("id"));
			}
			if(document==null||document.equals("")){
				fileManage = fileManageService.getEvidence1(request.getParameter("id"));
			}else{
				fileManage = null;
			}
			if(fileManage==null){
			fileName = document.getName();
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
			response.setContentType(document.getMimeType());
			response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
			OutputStream o;
			o = response.getOutputStream();
			InputStream is = new FileInputStream(document.getPath() + "/"
							+ document.getDocumentForm().getId() + "/"
							+ document.getName());
			IOUtils.copy(is, o);
			o.flush();
			o.close();
			log.info("File Name : "+fileName+" "+getText("download.success",locale));
			}else{
				fileName = fileManage.getFileName();
				fileName = URLEncoder.encode(fileName, "UTF-8");
	            fileName = URLDecoder.decode(fileName, "ISO8859_1");
	            
				response.setContentType(fileManage.getFileType());
				response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
				OutputStream o;
				o = response.getOutputStream();
				String path = fileManage.getFilePath();
				InputStream is = new FileInputStream(getServletContext().getRealPath("/resources/fileManage") + "/"
								+ "" + "/"
								+ path.substring(path.lastIndexOf("/")+1)); 
				IOUtils.copy(is, o);
				o.flush();
				o.close();
				log.info("File Name : "+fileName+" "+getText("download.success",locale));
			}
		} catch (Exception e) {
			
			if(fileManage!=null){	
				log.error(e.getMessage(),e);
				e.printStackTrace();
			}
			saveError(request, getText("error.document.download",locale));
		}
	}
	
	/**
	 * save Document Form.
	 * 
	 * @param model
	 * @param documentForm
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/saveDocumentForm",method = RequestMethod.POST)
    public ModelAndView saveDocumentForm(ModelMap model,DocumentForm documentForm, BindingResult errors, HttpServletRequest request,
            HttpServletResponse response) {
		
	//	log.info("Begining: Save DocumentForm");
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String fileFormat = (String) PropertyReader.getInstance()
		.getPropertyFromFile("String", "system.file.format");
		model.addAttribute("fileFormat", fileFormat);
		
		
		try{
/*			if (validator != null) {
	        	validator.validate(documentForm, errors); 
	            if (errors.hasFieldErrors("name")) {
	            	model.addAttribute("folderId", folderId);
		            return new ModelAndView("/dms/documentForm", model);
	            }
	        }*/
			if(folderId !=null && !folderId.isEmpty()){
				Folder folder = folderService.getFolder(folderId);
        		documentForm.setFolder(folder);
           }else{
        	   documentForm.setFolder(null);
           }

			String path=getServletContext().getRealPath("/resources") + "/root/";
			setDocumentPermissionGrid(model, documentForm.getId());
			String userName = CommonUtil.getLoggedInUser().getFullName();
			
			
			documentForm = documentFormService.saveOrUpdateDocumentForm(documentForm,documentForm.getFiles(), userName, path);
			//Getting permissions for delete while editing document form
			PermissionDTO permissionDTO;
			if (request.isUserInRole(Constants.ADMIN_ROLE) || documentForm.getCreatedBy().equalsIgnoreCase(request.getRemoteUser())) {
				permissionDTO=new PermissionDTO(true, true, true, true, true, true);
	      	}else {
				permissionDTO = documentFormService.getPermission(documentForm,CommonUtil.getLoggedInUser());
					if(!permissionDTO.getCanDelete()) {
						List<DMSRolePermission> dmsUserRolePermissions = folderService.getDmsUserRolePermission(request.getRemoteUser(),documentForm.getFolder().toString());
						for (DMSRolePermission dmsUserRolePermission : dmsUserRolePermissions) {
							if(dmsUserRolePermission.getCanDelete()) {
								permissionDTO.setCanDelete(true);
							}
						}
					}
	      	}
			documentForm.setPermissionDTO(permissionDTO);
			model.addAttribute("documentForm",documentForm);
			model.addAttribute("folderId", folderId);
			
		
			saveMessage(request, getText("success.document.save",locale));
			log.info("Document Form Name : "+documentForm.getName()+" "+getText("save.success",locale));
		} catch (BpmException e) {
			model.addAttribute("documentForm",documentForm);
			model.addAttribute("folderId", folderId);
			log.error(e.getMessage(),e);
			saveError(request, getText("error.upload.format",locale));
			 return new ModelAndView("dms/documentForm",model);
		} catch (Exception e) {
			model.addAttribute("documentForm",documentForm);
			model.addAttribute("folderId", folderId);
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.save",locale));
			 return new ModelAndView("dms/documentForm",model);
		}
       return new ModelAndView("dms/manageDms",model);
    }
	
	/**
	 * get document grid.
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private String getDocumentGridScript(HttpServletRequest request){
		List<DocumentForm> documentForms=new ArrayList<DocumentForm>();
		if (request.isUserInRole(Constants.ADMIN_ROLE)) {
			documentForms=documentFormService.getAllDocuments();
       	}else{
       		documentForms=documentFormService.getAllDocumentsByLoggedInUser();
       	}
		String[] fieldNames={"id","name","createdBy","createdTimeByString"};
        String script=GridUtil.generateScriptForDocumentFormGrid(CommonUtil.getMapListFromObjectListByFieldNames(documentForms,fieldNames,""), velocityEngine);
	  return script;
	
	}
	
	/**
	 * show manage folder page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/showManageFolderPage",method = RequestMethod.GET)
	public ModelAndView showManageFolderPage(ModelMap model,HttpServletRequest request) {
		Locale locale = request.getLocale();
		Folder folder = new Folder();
		FolderPermission folderPermission = new FolderPermission();
		try{
			folderPermission.setRoleName(Constants.ADMIN_ROLE);
			folderPermission.setCanAccess(true);
	    	model.addAttribute("canAccess", true);
	    	model.addAttribute("folder", folder);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.folder.get",locale));
		}
		return new ModelAndView("dms/manageFolders", model);
	}
	
	/**
	 * show folder Form page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/showFolderPage",method = RequestMethod.GET)
	public ModelAndView showFolderPage(ModelMap model,HttpServletRequest request) {
		Locale locale = request.getLocale();
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
	    		Folder folder = folderService.getFolder(request.getParameter("id"));
	    		String owner = folder.getOwner();
	    		String ownerFullName  = userManager.getUserFullNamesByUsername(owner);
	    		
	    		model.addAttribute("ownerFullName", ownerFullName);
	    		model.addAttribute("folder",folder);
	    		setFolderPermissionGrid(model, folder.getId());
	    	}else{
	    		Folder folder= new Folder();
	    		folder.setParentFolder(request.getParameter("parentNodeId"));
	    		model.addAttribute("folder", folder);
	    	}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.folder.get",locale));
		}
		return new ModelAndView("dms/folderForm", model);
	}
	
	/**
	 * save folder.
	 * 
	 * @param model
	 * @param folder
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/savefolder",method = RequestMethod.POST)
	public ModelAndView savefolder(@ModelAttribute("folder") Folder folder, 
			BindingResult errors, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) {
		//log.info("Begining: Save Folder");
		Locale locale = request.getLocale();
	//	String owner = request.getParameter("owner");
		if (validator != null) {
        	validator.validate(folder, errors);
            if (errors.hasErrors()) { 
            	return new ModelAndView("dms/folderForm", model);
            }
        }
		try {
			
			folder = folderService.saveOrUpdateFoler(folder,request.getRemoteUser(),model);
			String parentFolder = folder.getParentFolder();
			setFolderPermissionGrid(model, folder.getId());
			String owner = folder.getOwner();
    		String ownerFullName  = userManager.getUserFullNamesByUsername(owner);
    		
    		
    		
		//	model.addAttribute("owner", owner);
			//String parentFolder = folder.getParentFolder();\
			if(folder.getId().equals(Constants.EMPTY_STRING) || folder.getId() != null) {
				JSONObject childNodes = getFolderChildNodesAfterCreate(parentFolder,folder,request,locale);
				model.addAttribute("childNodes", childNodes);
				model.addAttribute("folderId", folder.getId());
				model.addAttribute("parentFolder", folder.getParentFolder());	
				model.addAttribute("ownerFullName", ownerFullName);		
				model.addAttribute("folder", folder);
			} else {
				model.addAttribute("ownerFullName", ownerFullName);		
				model.addAttribute("folder", folder);
			}
			/*if(mode != "EDIT" || !mode.equals("EDIT")) {
				
			}*/
				
			
			
			saveMessage(request, getText("success.folder.save",locale));
			log.info("Folder Name : "+folder.getName()+" "+getText("save.success",locale));
		} catch(BpmException e){
			log.error(e.getMessage(),e);
			saveError(request,e.getMessage());
			return new ModelAndView("dms/folderForm", model);
		} catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.folder.save",locale));
			return new ModelAndView("dms/folderForm", model);
		} 
		return new ModelAndView("dms/folderForm", model);
	}
    
	/**
	 * method for get the selected folder documents
	 * 
	 * @param folderId
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/getFolderDocumentGrid", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public @ResponseBody String getFolderDocumentGrid(@RequestParam("folderId") String folderId,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		String script = "";
		List<DocumentForm> documentForms=new ArrayList<DocumentForm>();
		List<DocumentForm> documentFormsForLoggedInUser = new ArrayList<DocumentForm>();
		
		
		try {
			if(!StringUtil.isEmptyString(folderId)){
				documentForms=documentFormService.getDocumentsByFolderId(folderId);
				PermissionDTO permissionDTO;
				if(documentForms != null && !documentForms.isEmpty()) {
					for(DocumentForm documentForm : documentForms) {
						if (request.isUserInRole(Constants.ADMIN_ROLE) || documentForm.getCreatedBy().equalsIgnoreCase(request.getRemoteUser())) {
							permissionDTO=new PermissionDTO(true, true, true, true, true, true);
							documentFormsForLoggedInUser.add(documentForm);
				      	}else{
				      		//Getting Document Form permission
				      		permissionDTO = documentFormService.getPermission(documentForm,CommonUtil.getLoggedInUser());
				      		//Getting Folder permission if document form permission is not set
				      		//System.out.println("================="+permissionDTO.getCanRead());
				      			if(permissionDTO.getCanRead() || permissionDTO.getCanCreate() || permissionDTO.getCanDownload() || permissionDTO.getCanEdit()
				      					|| permissionDTO.getCanPrint() || permissionDTO.getCanRead() || permissionDTO.isPublic()) {
				      				documentFormsForLoggedInUser.add(documentForm);
				      			}
				       	}
					}
				}
			}else{
				if (request.isUserInRole(Constants.ADMIN_ROLE)) {
					documentFormsForLoggedInUser=documentFormService.getAllDocuments();
		       	}else{
		       		documentFormsForLoggedInUser=documentFormService.getAllDocumentsByLoggedInUser();
		       	}
			}
			String[] fieldNames={"id","name","createdBy","createdTimeByString"};
	        script=GridUtil.generateScriptForDocumentFormGrid(CommonUtil.getMapListFromObjectListByFieldNames(documentFormsForLoggedInUser,fieldNames,""), velocityEngine);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.get",locale));
		}
		return script;
	}
	
	
	/**
	 * show manage DMS page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/showManageDms",method = RequestMethod.GET)
	public ModelAndView showManageDms(ModelMap model,HttpServletRequest request) {
		//log.info("Opening Folder form to Manage");
		Locale locale = request.getLocale();
		try {
			Folder folder = new Folder();
			FolderPermission folderPermission = new FolderPermission();
			folderPermission.setRoleName(Constants.ADMIN_ROLE);
			folderPermission.setCanAccess(true);
	    	model.addAttribute("canAccess", true);
	    	model.addAttribute("folder", folder);
	    	model.addAttribute("folderId", "ff8081813e78c109013e78c35b7f0000");
			String script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizationTree.vm", getDmsRootNode());
			model.addAttribute("script", script);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.folder.get",locale));
		}
		return new ModelAndView("dms/manageDms", model);
	}
	
	/**
	 * get DMS root node 
	 * 
	 * @return
	 * @throws Exception 
	 */
    public Map<String, Object> getDmsRootNode() throws Exception{
    	JSONObject jsonNodes = new JSONObject();
    	JSONArray nodes = new JSONArray();
    	Map<String, Object> context = new HashMap<String, Object>();
    	List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
    	Map<String, Object> nodeDetails = new HashMap<String, Object>();
    	Map<String, Object> attr = new HashMap<String, Object>();
    	List<LabelValue> folders = folderService.getRootFolderAsLabelValue();
		for(LabelValue folder : folders){
    		attr.put("id", folder.getValue());
    		attr.put("name", folder.getLabel());
    		attr.put("isRoot", true);
    		nodeDetails.put("data", folder.getLabel());
    		nodeDetails.put("attr", attr);
    		nodeList.add(nodeDetails);
    		jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
    		nodes.put(jsonNodes);
		}
		context.put("nodes", nodes);
		context.put("needTreeCheckbox", false);
		context.put("needContextMenu", true);
    	return context;
    }
    
    /**
	 * get root elements of user selection tree
	 * 
	 * @param model
	 * @param currentNode
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/dms/getFolderChildNodes", method = RequestMethod.GET)
    public @ResponseBody String getFolderChildNodes(ModelMap model,@RequestParam("currentNode") String currentNode, 
    		Folder folder,BindingResult errors, HttpServletRequest request, HttpServletResponse response){
//		log.info("Getting Folder child nodes");
		Locale locale = request.getLocale();
		JSONObject jsonNodes = new JSONObject();
    	JSONArray nodes = new JSONArray();
    	List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
    	Map<String, Object> nodeDetails = new HashMap<String, Object>();
    	Map<String, Object> attr = new HashMap<String, Object>();
    	try {
    		List<LabelValue> folders = folderService.getChildFoldersAsLabelValue(currentNode);
			for(LabelValue child : folders){
	        	boolean canEdit = false;
	        	boolean canDelete = false;
	        	boolean canCreate = false; 
	        	boolean canRead = false;
				attr.put("id", child.getValue());
	    		attr.put("name", child.getLabel());
	    		attr.put("isRoot", false);
				folder = folderService.getFolder(child.getValue());
				
				if(request.isUserInRole(Constants.ADMIN_ROLE)){
					canEdit = true;
					canDelete = true;
					canCreate = true;
					canRead = true;
				//	attr.put("isAdmin", true);
	    		}
				
				if(null != folder.getOwner() && folder.getOwner().equals(request.getRemoteUser())){
					canEdit = true;
					canDelete = true;
					canCreate = true;
					canRead = true;
				//	attr.put("isOwner", true);
				}
				
				if(folder.getCreatedBy().equals(request.getRemoteUser())){
					canEdit = true;
					canDelete = true;
					canCreate = true;
					canRead = true;
				//	attr.put("isCreator", false);
				}
				
	    		if(canEdit != true){
    	    		for(DMSRolePermission dmsRolePermission : folderService.getDmsUserRolePermission(request.getRemoteUser(), child.getValue())){
    					if(dmsRolePermission.getCanEdit() == true){
    					//	canEdit = true;
    					//	canDelete = true;
    						canCreate = true;
    						canRead = true;
    					} else if(dmsRolePermission.getCanRead() == true) {
    						canCreate = false;
    						canRead = true;
    					}
    				}
	    		}
	    		
	    		if(canEdit != true || canDelete != true){
	    			for(DMSRolePermission dmsRolePermission : folderService.getDmsRolePermission(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserRoles(), ",", true), child.getValue())) {
    					if(dmsRolePermission.getCanEdit() == true){
    					//	canEdit = true;
    					//	canDelete = true;
    						canCreate = true;
    						canRead = true;
    					} else if(dmsRolePermission.getCanRead() == true) {
    						canCreate = false;
    						canRead = true;
    					}
    				}
	    		}
	    		
	    		if(canEdit != true || canDelete != true){
    				for(DMSRolePermission dmsGroupRolePermission : folderService.getDmsGroupRolePermission(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserGroups(), ",", true), child.getValue())){
    					if(dmsGroupRolePermission.getCanEdit() == true){
    					//	canEdit = true;
    					//	canDelete = true;
    						canCreate = true;
    						canRead = true;
    					} else if(dmsGroupRolePermission.getCanRead() == true) {
    						canCreate = false;
    						canRead = true;
    					}
    				}
	    		}
	    		
	    		if(canEdit != true || canDelete != true){
    				for(DMSRolePermission dmsDepartmentRolePermission : folderService.getDmsDepartmentRolePermission(userService.getUserByUsername(request.getRemoteUser()).getDepartment().getId(), child.getValue())){
    					if(dmsDepartmentRolePermission.getCanEdit() == true){
    					//	canEdit = true;
    					//	canDelete = true;
    						canCreate = true;
    						canRead = true;
    					} else if(dmsDepartmentRolePermission.getCanRead() == true) {
    						canCreate = false;
    						canRead = true;
    					}
    				}
	    		}
	    		
				attr.put("canEdit", canEdit);
	    		attr.put("canDelete", canDelete);
	    		attr.put("canCreate", canCreate);
	    		attr.put("canRead", canRead);
	    		nodeDetails.put("data", child.getLabel());
	    		nodeDetails.put("attr", attr);
	    		nodeList.add(nodeDetails);
	    		jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
	    		nodes.put(jsonNodes);
			}
    	}catch (BpmException e) {
			log.warn(e.getMessage(),e);
			saveError(request, getText("errors.folder.getNodes",locale));
		} catch (Exception e) {
			saveError(request, getText("errors.folder.getNodes",locale));
    		log.warn(e.getMessage(),e);
    	}
		return nodes.toString();
    }
	
	/**
	 * delete folder.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/deleteFolder",method = RequestMethod.GET)
	public ModelAndView deleteFolder(ModelMap model,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		try{
		if(!StringUtil.isEmptyString(request.getParameter("id"))){
			folderService.removechildFolders(request.getParameter("id"));
			Folder parentFolder = folderService.getFolder(request.getParameter("id"));   
			log.info(parentFolder.getName()+" deleted successfully");	
			folderService.deleteFoler(parentFolder);
			saveMessage(request, getText("deleted successfully",locale));
			Folder folder = new Folder();
			FolderPermission folderPermission = new FolderPermission();
			folderPermission.setRoleName(Constants.ADMIN_ROLE);
			folderPermission.setCanAccess(true);
	    	model.addAttribute("canAccess", true);
	    	model.addAttribute("folder", folder);
			String script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizationTree.vm", getDmsRootNode());
			model.addAttribute("script", script);
    	}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.folder.delete",locale));
		}
		return new ModelAndView("dms/manageDms", model);
	}
	
	/**
	 * show DMS user perimssion page.
	 * 
	 * @param model
	 * @param dmsUserPermission
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "bpm/dms/showDmsUserPermission",method = RequestMethod.GET)
	public ModelAndView showDmsUserPermission(ModelMap model, DMSRolePermission dmsRolePermission,
			BindingResult errors, HttpServletRequest request) {
		log.info("Opening Dms User Permisison");
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String from = request.getParameter("from");
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				dmsUserPermission = folderService.getDmsUserPermission(request.getParameter("id"));
				if(from.equals("folder")){
					folderId = dmsUserPermission.getFolder().getId();
				} else {
					folderId = dmsUserPermission.getDocumentForm().getId();
				}
				model.addAttribute("users",dmsUserPermission.getUserName());
	    	}else {
	    		dmsUserPermission = new DMSUserPermission();
	    	}
		model.addAttribute("from",from);
		model.addAttribute("folderId",folderId);
		model.addAttribute("documentId",folderId);
		model.addAttribute("dmsUserPermission",dmsUserPermission);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.userPermission.get",locale));
		}
		return new ModelAndView("dms/showDmsUserPermission", model);
	}*/
	
	
	/**
	 * save DMS User Permission
	 * 
	 * @param model
	 * @param DMSUserPermission
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "bpm/dms/saveDmsUserPermission",method = RequestMethod.POST)
	public ModelAndView saveDmsUserPermission(@ModelAttribute("dmsUserPermission") DMSUserPermission dmsUserPermission, 
			BindingResult errors, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) {
		log.info("Begining: Save user permission");
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String documentId = request.getParameter("documentId");
		String users = request.getParameter("users");
		String from = request.getParameter("from");
		try{
			if (validator != null) {
	        	validator.validate(dmsUserPermission, errors);
	            if (errors.hasErrors() &&  !errors.hasFieldErrors("folder")) { 
	            	return new ModelAndView("dms/showDmsUserPermission", model);
	            }
	        }
			
			if(!StringUtil.isEmptyString(folderId)){
				dmsUserPermission.setFolder(folderService.getFolder(folderId));
			}
			
			if(!StringUtil.isEmptyString(documentId)){
				dmsUserPermission.setDocumentForm(documentFormService.getDocumentForm(documentId));
			}
			
			if(StringUtil.isEmptyString(dmsUserPermission.getId())){
				dmsUserPermission.setId(null);
			}
			if (users.contains(",")) {
				String[] userNames = users.split(",");
				for(String userName : userNames){
					dmsUserPermission.setUserName(userName);
					dmsUserPermission = folderService.saveDmsUserPermission(dmsUserPermission);
					dmsUserPermission.setId(null);
				}
			} else {
				dmsUserPermission.setUserName(users);
				dmsUserPermission = folderService.saveDmsUserPermission(dmsUserPermission);
			}
			saveMessage(request, getText("success.userPermission.save",locale));
			model.addAttribute("from",from);
			setFolderPermissionGrid(model, folderId);
			model.addAttribute("documentId",documentId);
			model.addAttribute("dmsUserPermission",dmsUserPermission);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.userPermission.save",locale));
			return new ModelAndView("dms/showDmsUserPermission", model);
		}
		return new ModelAndView("dms/showDmsUserPermission", model);
	}*/
	
	/**
	 * show DMS Role Permisison page.
	 * 
	 * @param model
	 * @param dmsRolePermission
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/showDmsRolePermission",method = RequestMethod.GET)
	public ModelAndView showDmsRolePermission(ModelMap model, @RequestParam("from") String from,
			DMSRolePermission dmsRolePermission, BindingResult errors,	 HttpServletRequest request) {
		//log.info("Opening Dms Role Permisison");	
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String roleType=request.getParameter("roleType");
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				dmsRolePermission = folderService.getDmsRolePermission(request.getParameter("id"));   
				if(from.equals("folder")){
					folderId = dmsRolePermission.getFolder().getId();
				} else {
					folderId = dmsRolePermission.getDocumentForm().getId();
				}
				if(dmsRolePermission.getRoleType().equals("USER")){
					model.addAttribute("users",dmsRolePermission.getName());
					model.addAttribute("usersFullName",dmsRolePermission.getUserFullName());
					
					
				//	String usersFullName = userManager.getUserFullNamesByUsername(dmsRolePermission.getName());
					
				//	model.addAttribute("usersFullName", usersFullName);
					/*Set<User> userFullNames =  CommonUtil.getUsersFromuserIds(dmsRolePermission.getName());		
					for(User userFullName : userFullNames) {
						model.addAttribute("usersFullName",userFullName.getFullName());
					}*/
				}else if(dmsRolePermission.getRoleType().equals("ROLE")){
					model.addAttribute("roles",dmsRolePermission.getName());
				}else if(dmsRolePermission.getRoleType().equals("GROUP")){
					model.addAttribute("groups",dmsRolePermission.getName());
				}else {
					model.addAttribute("departments",dmsRolePermission.getName());
				}
				
	    	}else {
	    		dmsRolePermission = new DMSRolePermission();
	    	}
			model.addAttribute("from",from);
			model.addAttribute("folderId",folderId);
			model.addAttribute("documentId",folderId);
			model.addAttribute("dmsRolePermission",dmsRolePermission);
			}catch(Exception e){
				log.error(e.getMessage(),e);
				saveError(request, getText("error.rolePermission.get",locale));
			}
		return new ModelAndView("dms/showDmsRolePermission", model);
	}
	
	
	/**
	 * save DMS Role Permission
	 * 
	 * @param model
	 * @param DMSRolePermission
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "bpm/dms/saveDmsRolePermission",method = RequestMethod.POST)
	public ModelAndView saveDmsRolePermission(@ModelAttribute("dmsRolePermission") DMSRolePermission dmsRolePermission, 
			BindingResult errors, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) {
		log.info("Begining: Save Folder");
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String roles = request.getParameter("roles");
		String from = request.getParameter("from");
		String documentId = request.getParameter("documentId");
		try{
			if (validator != null) {
	        	validator.validate(dmsRolePermission, errors);
	            if (errors.hasErrors() &&  !errors.hasFieldErrors("folder")) { 
	            	return new ModelAndView("dms/showDmsRolePermission", model);
	            }
	        }
			
			if(!StringUtil.isEmptyString(folderId)){
				dmsRolePermission.setFolder(folderService.getFolder(folderId));
			}
			
			if(!StringUtil.isEmptyString(documentId)){
				dmsRolePermission.setDocumentForm(documentFormService.getDocumentForm(documentId));
			}
			
			if(StringUtil.isEmptyString(dmsRolePermission.getId())){
				dmsRolePermission.setId(null);
			}
			
			if (roles.contains(",")) {
				String[] roleNames = roles.split(",");
				for(String roleName : roleNames){
					dmsRolePermission.setRoleName(roleName);
					dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
					dmsRolePermission.setId(null);
				}
			} else {
				dmsRolePermission.setRoleName(roles);
				dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
			}
			saveMessage(request, getText("success.rolePermission.save",locale));
			model.addAttribute("from",from);
			setFolderPermissionGrid(model, folderId);
			model.addAttribute("documentId",documentId);
			model.addAttribute("dmsRolePermission",dmsRolePermission);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.rolePermission.save",locale));
			return new ModelAndView("dms/showDmsRolePermission", model);
		}
		return new ModelAndView("dms/showDmsRolePermission", model);
	}
	*/
	
	

	@RequestMapping(value = "bpm/dms/saveDmsRolePermission",method = RequestMethod.POST)
	public ModelAndView saveDmsRolePermission(@ModelAttribute("dmsRolePermission") DMSRolePermission dmsRolePermission, 
			BindingResult errors, ModelMap model, HttpServletRequest request,
            HttpServletResponse response)  throws Exception{
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String users = request.getParameter("users");
		String groups = request.getParameter("getGroups");
		String roles = request.getParameter("getRoles");
		String from = request.getParameter("from");
		String roleType = request.getParameter("roleType");
		String departments = request.getParameter("getDepartments");
		String documentId = request.getParameter("documentId");
		String userFullNames = request.getParameter("users_FullName");
		String saveMessage = "";
		String errorMessage = "";
		try{
			if(validator != null) {
		       	validator.validate(dmsRolePermission, errors);
		        if (errors.hasErrors() &&  !errors.hasFieldErrors("folder")) { 
		           	return new ModelAndView("dms/dmsRolePermission", model);
		        }
		    }
			if(!StringUtil.isEmptyString(folderId)){
				dmsRolePermission.setFolder(folderService.getFolder(folderId));
			}
			
			if(!StringUtil.isEmptyString(documentId)){
				dmsRolePermission.setDocumentForm(documentFormService.getDocumentForm(documentId));
				//dmsRolePermission.getDocumentForm().getName();
			}
			
			if(StringUtil.isEmptyString(dmsRolePermission.getId())){
				dmsRolePermission.setId(null);
			}
			
			List<DMSRolePermission> dmsRolePermissions = null;
			if(!documentId.isEmpty()) {
				dmsRolePermissions = folderService.getDmsRolePermissionByDocumentId(documentId);
			} else {
				dmsRolePermissions = folderService.getDmsRolePermissionByFolderId(folderId);
			}
			
			/*for(DMSRolePermission dmsRolePermissionName : dmsRolePermissions) {
			if(dmsRolePermissionName.getRoleType().equalsIgnoreCase("user")) {
				if(dmsRolePermissionName.getName().contains(users)) {
					throw new Exception("Error to create Permission");
				} 
				} else if (dmsRolePermissionName.getRoleType().equalsIgnoreCase("role")) {
					if(dmsRolePermissionName.getName().contains(roles)) {
						throw new Exception("Error to create Permission");
					}
				} else if (dmsRolePermissionName.getRoleType().equalsIgnoreCase("group")) {
						if(dmsRolePermissionName.getName().contains(groups)) {
							throw new Exception("Error to create Permission");
						} 
				} else if(dmsRolePermissionName.getRoleType().equalsIgnoreCase("department")) {
						if(dmsRolePermissionName.getName().contains(departments)) {
							throw new Exception("Error to create Permission");
						} 
				}
			}*/
			
			if(roleType.equals("USER") && !users.isEmpty()){
		       if (userFullNames.contains(",")) {
		       		String[] userNames = userFullNames.split(",");
					for(String userName : userNames){
						dmsRolePermission.setRoleName(userService.getUserRoleByUserId(userName));
						dmsRolePermission.setRoleType(roleType);
						dmsRolePermission.setUserFullName(userName.substring(userName.indexOf("_")+1, userName.length()));
						dmsRolePermission.setName(userName.substring(0,userName.indexOf("_")));
						dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
						dmsRolePermission.setId(null);
						saveMessage = "success.userPermission.save";
					}
					
				} else {
					dmsRolePermission.setRoleName(userService.getUserRoleByUserId(users));
					dmsRolePermission.setUserFullName(userFullNames.substring(userFullNames.indexOf("_")+1, userFullNames.length()));
					dmsRolePermission.setName(userFullNames.substring(0,userFullNames.indexOf("_")));
					dmsRolePermission.setRoleType(roleType);
					dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
					saveMessage = "success.userPermission.save";
				}
		     // log.info("DMS user permission for "+dmsRolePermission.getDocumentForm().getName()+" saved successfully");
		    //  log.info("DMS user permission for "+dmsRolePermission.getFolder().getName()+" saved successfully");
		    }else if(roleType.equals("ROLE") &&  !roles.isEmpty()){
		    	if (roles.contains(",")) {
					String[] roleNames = roles.split(",");
					for(String roleName : roleNames){
						dmsRolePermission.setRoleName(roleName);
						dmsRolePermission.setName(roleName);
						dmsRolePermission.setRoleType(roleType);
						dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
						dmsRolePermission.setId(null);
						saveMessage = "success.rolePermission.save";
					}
				} else {
					dmsRolePermission.setRoleName(roles);
					dmsRolePermission.setRoleType(roleType);
					dmsRolePermission.setName(roles);
					dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
					saveMessage = "success.rolePermission.save";
				}
		    //	log.info("DMS role permission for "+dmsRolePermission.getDocumentForm().getName()+" saved successfully");
		    }else if( roleType.equals("GROUP") && !groups.isEmpty()){
				if (groups.contains(",")) {
					String[] groupNames = groups.split(",");
					for(String groupName : groupNames){
						Group group = groupService.getGroupByName(groupName);
						dmsRolePermission.setRoleName(group.getGroupRole());
						dmsRolePermission.setName(group.getName());
						dmsRolePermission.setRoleType(roleType);
						dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
						dmsRolePermission.setId(null);
						saveMessage = "success.groupPermission.save";
					}
				} else {
					Group group = groupService.getGroupByName(groups);
					dmsRolePermission.setRoleName(group.getGroupRole());
					dmsRolePermission.setName(group.getName());
					dmsRolePermission.setRoleType(roleType);
					dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
					saveMessage = "success.groupPermission.save";
				}
			//	log.info("DMS group permission for "+dmsRolePermission.getDocumentForm().getName()+" saved successfully");
			}else if( roleType.equals("DEPARTMENT") && !departments.isEmpty()){
				if (departments.contains(",")) {
					String[] departmentNames = departments.split(",");
					for(String departmentName : departmentNames){
						Department department = departmentService.getDepartmentByName(departmentName);
						dmsRolePermission.setRoleName(department.getDepartmentRole());
						dmsRolePermission.setName(department.getName());
						dmsRolePermission.setRoleType(roleType);
						dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
						dmsRolePermission.setId(null);
						saveMessage = "success.departmentPermission.save";
					}
				} else {
					Department department = departmentService.getDepartmentByName(departments);
					dmsRolePermission.setRoleName(department.getDepartmentRole());
					dmsRolePermission.setName(department.getName());
					dmsRolePermission.setRoleType(roleType);
					dmsRolePermission = folderService.saveDmsRolePermission(dmsRolePermission);
					saveMessage = "success.departmentPermission.save";
				}
			//	log.info("DMS department permission for "+dmsRolePermission.getDocumentForm().getName()+" saved successfully");
			}else{
				//log.info("There is no other operation");
				errorMessage = "Permission Fields should not be Empty";
				throw new Exception("Error to create  Permission");
				
			}
		    	saveMessage(request, getText(saveMessage,locale));
			model.addAttribute("from",from);
			model.addAttribute("userRoleType", roleType);
			
			model.addAttribute("documentId",documentId);
			model.addAttribute("dmsRolePermission",dmsRolePermission);
			setFolderPermissionGrid(model, folderId);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			if(roleType.equals("USER")){
				errorMessage = "error.userPermission.save";
			}else if(roleType.equals("ROLE")){
				errorMessage = "error.rolePermission.save";
				
			}else if(roleType.equals("GROUP")){
				errorMessage = "error.groupPermission.save";
			}else if(roleType.equals("DEPARTMENT")){
				errorMessage = "error.departmentPermission.save";
			}
			saveError(request, getText(errorMessage,locale));
			return new ModelAndView("dms/showDmsRolePermission", model);
		}
		return new ModelAndView("dms/showDmsRolePermission", model);
	}
	
	
	
	/**
	 * show DMS Group Permisison page.
	 * 
	 * @param model
	 * @param dmsGroupPermission
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "bpm/dms/showDmsGroupPermission",method = RequestMethod.GET)
	public ModelAndView showDmsGroupPermission(ModelMap model, @RequestParam("from") String from,
			DMSGroupPermission dmsGroupPermission, BindingResult errors, HttpServletRequest request) {
		log.info("Opening Dms Group Permisison");	
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		try{
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				dmsGroupPermission = folderService.getDmsGroupPermission(request.getParameter("id"));   
				if(from.equals("folder")){
					folderId = dmsGroupPermission.getFolder().getId();
				} else {
					folderId = dmsGroupPermission.getDocumentForm().getId();
				}
				model.addAttribute("groups",dmsGroupPermission.getGroupName());
	    	}else {
	    		dmsGroupPermission = new DMSGroupPermission();
	    	}
			model.addAttribute("from",from);
			model.addAttribute("folderId",folderId);
			model.addAttribute("documentId",folderId);
			model.addAttribute("dmsGroupPermission",dmsGroupPermission);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.groupPermission.get",locale));
		}
		return new ModelAndView("dms/showDmsGroupPermission", model);
	}*/
	
	
	/**
	 * save DMS Group Permission
	 * 
	 * @param model
	 * @param DMSGroupPermission
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "bpm/dms/saveDmsGroupPermission",method = RequestMethod.POST)
	public ModelAndView saveDmsGroupPermission(@ModelAttribute("dmsGroupPermission") DMSGroupPermission dmsGroupPermission, 
			BindingResult errors, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) {
		log.info("Begining: Save Folder");
		Locale locale = request.getLocale();
		String folderId = request.getParameter("folderId");
		String groups = request.getParameter("groups");
		String from = request.getParameter("from");
		String documentId = request.getParameter("documentId");
		try{
			if (validator != null) {
	        	validator.validate(dmsGroupPermission, errors);
	            if (errors.hasErrors() &&  !errors.hasFieldErrors("folder")) { 
	            	return new ModelAndView("dms/showDmsGroupPermission", model);
	            }
	        }
			if(!StringUtil.isEmptyString(folderId)){
				dmsGroupPermission.setFolder(folderService.getFolder(folderId));
			}
			
			if(StringUtil.isEmptyString(dmsGroupPermission.getId())){
				dmsGroupPermission.setId(null);
			}
			
			if(!StringUtil.isEmptyString(documentId)){
				dmsGroupPermission.setDocumentForm(documentFormService.getDocumentForm(documentId));
			}
			
			if (groups.contains(",")) {
				String[] groupNames = groups.split(",");
				for(String groupName : groupNames){
					dmsGroupPermission.setGroupName(groupName);
					dmsGroupPermission = folderService.saveDmsGroupPermission(dmsGroupPermission);
					dmsGroupPermission.setId(null);
				}
			} else {
				dmsGroupPermission.setGroupName(groups);
				dmsGroupPermission = folderService.saveDmsGroupPermission(dmsGroupPermission);
			}
			saveMessage(request, getText("success.groupPermission.save",locale));
			model.addAttribute("from",from);
			setFolderPermissionGrid(model, folderId);
			model.addAttribute("documentId",documentId);
			model.addAttribute("dmsGroupPermission",dmsGroupPermission);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.groupPermission.save",locale));
			return new ModelAndView("dms/showDmsGroupPermission", model);
		}
		return new ModelAndView("dms/showDmsGroupPermission", model);
	}
	*/
	
	/**
	 * set folder permissions grid in model
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelMap setFolderPermissionGrid(ModelMap model,String folderId) {
		
		Set<DMSRolePermission> userPermissionSet = new HashSet<DMSRolePermission>();
		Set<DMSRolePermission> rolePermissionSet = new HashSet<DMSRolePermission>();
		Set<DMSRolePermission> groupPermissionSet = new HashSet<DMSRolePermission>();
		
		List<DMSRolePermission> userPermissions=folderService.getDmsUserPermissionByRoleType(folderId);
		List<DMSRolePermission> rolePermissionsWithUserName = new ArrayList<DMSRolePermission>();
		if(userPermissions != null && userPermissions.size() > 0){
			for(DMSRolePermission userPermission:userPermissions){
				userPermission.setName(userService.getUserByUsername(userPermission.getName()).getUsername());
				
    			rolePermissionsWithUserName.add(userPermission);
    		}
			userPermissionSet.addAll(userPermissions);
		}
    	String[] userFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
    	String userPermissionScript=GridUtil.generateScriptForDMSUserPermissionGrid("folder", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(userPermissionSet),userFieldNames,""), velocityEngine);
    	List<DMSRolePermission> rolePermissions=folderService.getDmsRolePermissionByFolder(folderId);
    	if(rolePermissions != null && rolePermissions.size() > 0){
			rolePermissionSet.addAll(rolePermissions);
		}
    	String[] roleFieldNames={"id","name","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
    	String rolePermissionScript=GridUtil.generateScriptForDMSRolePermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(rolePermissionSet),roleFieldNames,""), velocityEngine);
    	/*List<DMSRolePermission> rolePermissions=folderService.getDmsRolePermissionByFolder(folderId);
    	
    	if(rolePermissions != null && rolePermissions.size() > 0){
    		
			rolePermissionSet.addAll(rolePermissionsWithUserName);
		}
//    	if(rolePermissions != null && rolePermissions.size() > 0){
//			rolePermissionSet.addAll(rolePermissions);
//		}
    	String[] roleFieldNames={"id","name","canEdit","canDelete","canPrint","canDownload","canRead"};
    	String rolePermissionScript=GridUtil.generateScriptForDMSRolePermissionGrid("folder", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(rolePermissionSet),roleFieldNames,""), velocityEngine);*/
    	
    	List<DMSRolePermission> groupPermissions=folderService.getDmsGroupPermissionByRoleType(folderId);
    	if(groupPermissions != null && groupPermissions.size() > 0){
			groupPermissionSet.addAll(groupPermissions);
		}
    	String[] groupFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
    	String groupPermissionScript=GridUtil.generateScriptForDMSGroupPermissionGrid("folder", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(groupPermissionSet),groupFieldNames,""), velocityEngine);
    	model.addAttribute("userPermissionScript", userPermissionScript);
    	model.addAttribute("rolePermissionScript", rolePermissionScript);
    	model.addAttribute("groupPermissionScript", groupPermissionScript);
		return model;
		/*Set<DMSRolePermission> userPermissionSet = new HashSet<DMSRolePermission>();
		Set<DMSRolePermission> rolePermissionSet = new HashSet<DMSRolePermission>();
		Set<DMSRolePermission> groupPermissionSet = new HashSet<DMSRolePermission>();
		
		List<DMSRolePermission> userPermissions=folderService.getDmsUserPermissionByDocument(folderId);
		if(userPermissions != null && userPermissions.size() > 0){
			userPermissionSet.addAll(userPermissions);
		}
    	String[] userFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead"};
    	String userPermissionScript=GridUtil.generateScriptForDMSUserPermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(userPermissionSet),userFieldNames,""), velocityEngine);
        
    	List<DMSRolePermission> rolePermissions=folderService.getDmsRolePermissionByDocument(folderId);
    	if(rolePermissions != null && rolePermissions.size() > 0){
			rolePermissionSet.addAll(rolePermissions);
		}
    	String[] roleFieldNames={"id","name","canEdit","canDelete","canPrint","canDownload","canRead"};
    	String rolePermissionScript=GridUtil.generateScriptForDMSRolePermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(rolePermissionSet),roleFieldNames,""), velocityEngine);
    	
    	List<DMSRolePermission> groupPermissions=folderService.getDmsGroupPermissionByFolder(folderId);
    	if(groupPermissions != null && groupPermissions.size() > 0){
			groupPermissionSet.addAll(groupPermissions);
		}
    	String[] groupFieldNames={"id","groupName","canEdit","canDelete","canPrint","canDownload","canRead"};
    	String groupPermissionScript=GridUtil.generateScriptForDMSGroupPermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(groupPermissionSet),groupFieldNames,""), velocityEngine);
    	
    	model.addAttribute("userPermissionScript", userPermissionScript);
    	model.addAttribute("rolePermissionScript", rolePermissionScript);
    	model.addAttribute("groupPermissionScript", groupPermissionScript);
		return model;*/
	}
	
	/**
	 * get user permission grid
	 * 
	 * 
	 * @return
	 */
	
	@RequestMapping(value = "bpm/dms/getUserPermisisonGrid", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public @ResponseBody String getUserPermisisonGrid(@RequestParam("from") String from,
			@RequestParam("id") String id, HttpServletRequest request) {
		List<DMSRolePermission> userPermissions = new ArrayList<DMSRolePermission>();
		Set<DMSRolePermission> userPermissionSet = new HashSet<DMSRolePermission>();
		String userPermissionScript = "";
		if(from.equalsIgnoreCase("folder")){
			userPermissions = folderService.getDmsUserPermissionByRoleType(id);
		} else {
			userPermissions = documentFormService.getDmsUserPermissionByDocument(id);
		}
		if(userPermissions != null && userPermissions.size() > 0){
			userPermissionSet.addAll(userPermissions);
		}
		if(from.equalsIgnoreCase("folder")){
	    	String[] userFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead"};
			userPermissionScript=GridUtil.generateScriptForDMSUserPermissionGrid(from, CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(userPermissionSet),userFieldNames,""), velocityEngine);
		} else {
			String[] userFieldNames = {"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead"};
			userPermissionScript=GridUtil.generateScriptForDMSUserPermissionGrid(from, CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(userPermissionSet),userFieldNames,""), velocityEngine);
		}
        
		return userPermissionScript;
	}
	
	/**
	 * get role permission grid
	 * 
	 * 
	 * @return
	 */
	
	@RequestMapping(value = "bpm/dms/getRolePermisisonGrid", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public @ResponseBody String getRolePermisisonGrid(@RequestParam("from") String from,
			@RequestParam("id") String id, HttpServletRequest request) {
		List<DMSRolePermission> rolePermissions = new ArrayList<DMSRolePermission>();
		Set<DMSRolePermission> rolePermissionSet = new HashSet<DMSRolePermission>();
		String rolePermissionScript = "";
		if(from.equalsIgnoreCase("folder")){
			rolePermissions=folderService.getDmsRolePermissionByFolder(id);
		} else {
			rolePermissions = documentFormService.getDmsRolePermissionByDocument(id);
		}
		if(rolePermissions != null && rolePermissions.size() > 0){
			rolePermissionSet.addAll(rolePermissions);
		}
		
		//if(from.equalsIgnoreCase("folder")){
			String[] roleFieldNames={"id","name","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
	    	rolePermissionScript=GridUtil.generateScriptForDMSRolePermissionGrid(from, CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(rolePermissionSet),roleFieldNames,""), velocityEngine);
		/*} else {
			String[] roleFieldNames={"id","name","canEdit","canDelete","canPrint","canDownload"};
	    	rolePermissionScript=GridUtil.generateScriptForDMSRolePermissionGrid(from, CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(rolePermissionSet),roleFieldNames,""), velocityEngine);
		}*/
		
        
		return rolePermissionScript;
	}
	
	/**
	 * get group permission grid
	 * 
	 * 
	 * @return
	 */
	
	@RequestMapping(value = "bpm/dms/getGroupPermisisonGrid", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public @ResponseBody String getGroupPermisisonGrid(@RequestParam("from") String from,
			@RequestParam("id") String id, HttpServletRequest request) {
		List<DMSRolePermission> groupPermissions = new ArrayList<DMSRolePermission>();
		Set<DMSRolePermission> groupPermissionSet = new HashSet<DMSRolePermission>();
		String groupPermissionScript = "";
		if(from.equalsIgnoreCase("folder")){
			groupPermissions=folderService.getDmsGroupPermissionByRoleType(id);
		} else {
			groupPermissions = documentFormService.getDmsGroupPermissionByDocument(id);
		}
		if(groupPermissions != null && groupPermissions.size() > 0){
			groupPermissionSet.addAll(groupPermissions);
		}
		
		if(from.equalsIgnoreCase("folder")){
			String[] groupFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead"};
	    	groupPermissionScript = GridUtil.generateScriptForDMSGroupPermissionGrid(from, CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(groupPermissionSet),groupFieldNames,""), velocityEngine);
		} else {
			String[] groupFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload"};
	    	groupPermissionScript = GridUtil.generateScriptForDMSGroupPermissionGrid(from, CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(groupPermissionSet),groupFieldNames,""), velocityEngine);
		}
        
		return groupPermissionScript;
	}
	
	/**
	 * set document permissions grid in model
	 * 
	 * @param model
	 * 
	 * @return
	 */
	public ModelMap setDocumentPermissionGrid(ModelMap model,String documentId) {
		
		Set<DMSRolePermission> userPermissionSet = new HashSet<DMSRolePermission>();
		Set<DMSRolePermission> rolePermissionSet = new HashSet<DMSRolePermission>();
		Set<DMSRolePermission> groupPermissionSet = new HashSet<DMSRolePermission>();
		
		List<DMSRolePermission> userPermissions=documentFormService.getDmsUserPermissionByDocument(documentId);
		if(userPermissions != null && userPermissions.size() > 0){
			userPermissionSet.addAll(userPermissions);
		}
    	String[] userFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
    	String userPermissionScript=GridUtil.generateScriptForDMSUserPermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(userPermissionSet),userFieldNames,""), velocityEngine);
        
    	List<DMSRolePermission> rolePermissions=documentFormService.getDmsRolePermissionByDocument(documentId);
    	if(rolePermissions != null && rolePermissions.size() > 0){
			rolePermissionSet.addAll(rolePermissions);
		}
    	String[] roleFieldNames={"id","name","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
    	String rolePermissionScript=GridUtil.generateScriptForDMSRolePermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(rolePermissionSet),roleFieldNames,""), velocityEngine);
    	
    	List<DMSRolePermission> groupPermissions=documentFormService.getDmsGroupPermissionByDocument(documentId);
    	if(groupPermissions != null && groupPermissions.size() > 0){
			groupPermissionSet.addAll(groupPermissions);
		}
    	String[] groupFieldNames={"id","roleName","canEdit","canDelete","canPrint","canDownload","canRead","userFullName"};
    	String groupPermissionScript=GridUtil.generateScriptForDMSGroupPermissionGrid("document", CommonUtil.getMapListFromObjectListByFieldNames(new ArrayList<DMSRolePermission>(groupPermissionSet),groupFieldNames,""), velocityEngine);
    	
    	model.addAttribute("userPermissionScript", userPermissionScript);
    	model.addAttribute("rolePermissionScript", rolePermissionScript);
    	model.addAttribute("groupPermissionScript", groupPermissionScript);
		return model;
	}
	
	/**
	 * get org tree child node object 
	 * 
	 * @param model
	 * 
	 * @return
	 * @throws Exception 
	 */
	public JSONObject getChildObject(String id, String name, boolean canEdit) throws Exception {
		//log.info("Getting Folder child nodes for "+name);
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
    	Map<String, Object> nodeDetails = new HashMap<String, Object>();
    	Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", name);
		attr.put("isRoot", false);
		attr.put("canEdit", canEdit);
		attr.put("canDelete", canEdit);
		nodeDetails.put("data", name);
		nodeDetails.put("attr", attr);
		nodeList.add(nodeDetails);
		JSONObject node = CommonUtil.getOrganizationTreeNodes(nodeList);
		return node;
	}
	
	
	/**
	 * delete Document Form page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/deleteDocumentPermission",method = RequestMethod.GET)
	public @ResponseBody String deleteDocumentPermission(@RequestParam("ids") String permIds,@RequestParam("from") String from,
			HttpServletRequest request) {
		log.info("Opening Document form Permission to delete");
		Locale locale = request.getLocale();
		try{
			if(!StringUtil.isEmptyString(request.getParameter("ids"))){
				
				List<String> permIdList = new ArrayList<String>();
				 if (permIds.contains(",")) {
					  String[] ids = permIds.split(",");
					  for(String id :ids){
						  permIdList.add(id);
					  }
					} else {
						permIdList.add(permIds);
				}
				 if(from != null && from.equalsIgnoreCase("Group")){
					 documentFormService.deleteGroupPermission(permIdList);
				 }
				 if(from != null && from.equalsIgnoreCase("Role")){
					 documentFormService.deleteRolePermission(permIdList);
				 }
				 if(from != null && from.equalsIgnoreCase("User")){
					 documentFormService.deleteUserPermission(permIdList);
				 }
				
	    	}
		}catch(Exception e){
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.delete",locale));
		}
		return "success";
	}
	
	/**
	 * delete Document Form page.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/viewDocument",method = RequestMethod.GET)
	public ModelAndView viewDocument(@RequestParam("id") String id,@RequestParam("documentFormId") String documentFormId, ModelMap model,HttpServletResponse response,
			HttpServletRequest request) {
		log.info("Preparing to download Document");
//		String fileName = null;
		Document document = new Document();
		Locale locale = request.getLocale();
		UUID uuid= UUID.randomUUID();
		String REPOSITORY_CACHE_PDF=getServletContext().getRealPath("/resources") + "/root";
		String tmpFilePath="/resources/root";
		File pdfCache = new File(REPOSITORY_CACHE_PDF + File.separator + "pdf" +  File.separator + uuid + ".pdf");
//		File tmp = null;
		try {
			model.addAttribute("canPrint",false);
			if (!StringUtil.isEmptyString(documentFormId)) {
				DocumentForm documentForm = documentFormService.getDocumentForm(documentFormId);
	      		PermissionDTO permissionDTO = documentFormService.getPermission(documentForm,CommonUtil.getLoggedInUser());
				log.info("Permission To Print the File:"+permissionDTO);
				if(permissionDTO.getCanPrint() || documentForm.getCreatedBy().equals(request.getRemoteUser()) || request.isUserInRole(Constants.ADMIN_ROLE)){
					model.addAttribute("canPrint",true);
				}
			}
			if (!StringUtil.isEmptyString(id)) {
				document = documentFormService.getDocumentById(id);
			}
			model.addAttribute("title",document.getName());
			// Save content to temporary file
			model.addAttribute("filePath", FileUtil.writeTempFileToViewFile(document.getPath() + "/"+ document.getDocumentForm().getId() + "/"+ document.getName(), 
					document.getName(), document.getMimeType(), REPOSITORY_CACHE_PDF, tmpFilePath));
		}catch(FileNotFoundException e){
			log.error(e.getMessage(),e);
			model.addAttribute("filePath", "/resources/file_not_found.pdf");
		}catch(NotImplementedException ne){
			pdfCache.delete();
			log.error(ne.getMessage(),ne); 
			model.addAttribute("filePath", "/resources/conversion_not_avail.pdf");
			saveError(request, getText("error.document.download",locale));
		} catch (Exception e) {
			pdfCache.delete();
			e.printStackTrace();
			log.error(e.getMessage(),e);
			model.addAttribute("filePath","/resources/conversion_problem.pdf");
			saveError(request, getText("error.document.download",locale));
		}
		return new ModelAndView("dms/viewDocument",model);
	}
	
	@RequestMapping(value="bpm/dms/deleteTmpFile",method = RequestMethod.GET)
	public String deleteTmpFile(@RequestParam("docPath") String docPath,HttpServletResponse response, 	HttpServletRequest request){
		if (!StringUtil.isEmptyString(docPath) && !docPath.contains("conversion_not_avail.pdf") && !docPath.contains("conversion_problem.pdf") && !docPath.contains("file_not_found.pdf")) {
			String resourcePath = getServletContext().getRealPath("/resources");
			String splittedpath[] = resourcePath.split("(?i)resources");
			String fileName = splittedpath[0] + docPath;
			log.info("File to delete" + fileName);
			FileUtils.deleteQuietly(new File(fileName));
		}
		return "Success";
	}
	
	@RequestMapping(value="bpm/dms/deleteDocument",method = RequestMethod.GET)
	public @ResponseBody ModelAndView deleteDocument(@RequestParam("documentId") String documentId,@RequestParam("documentFormId") String documentFormId,HttpServletRequest request){
    	ModelMap model=new ModelMap();
    	Locale locale = request.getLocale();
    	model.addAttribute("id", documentFormId);
		if(!StringUtil.isEmptyString(documentId)){
			 documentFormService.deleteDocumentById(documentId);
		}
		saveMessage(request, getText("success.document.delete",locale));
		return new ModelAndView("redirect:viewDocumentForm",model);	
	}
	
	
	@RequestMapping(value="bpm/dms/deleteDocumentWhileUpload",method = RequestMethod.GET)
	public @ResponseBody ModelAndView deleteDocumentWhileUpload(@RequestParam("documentId") String documentId,@RequestParam("documentFormId") String documentFormId,HttpServletRequest request){
    	ModelMap model=new ModelMap();
    	Locale locale = request.getLocale();
    	model.addAttribute("id", documentFormId);
		if(!StringUtil.isEmptyString(documentId)){
			 documentFormService.deleteDocumentById(documentId);
		}
		saveMessage(request, getText("success.document.delete",locale));
		return new ModelAndView("redirect:documentForm",model);	
	}
	
	/**
	 * Check whether Document Form Name is duplicate or not
	 * @param documentName
	 * @return
	 */
    @RequestMapping(value="/dms/checkDocumentFormName", method = RequestMethod.GET)
    public @ResponseBody boolean checkDocumentFormName(@RequestParam("documentFormName") String documentFormName,@RequestParam("documentFormId") String documentFormId,@RequestParam("folderId") String folderId) {
    	boolean isDuplicateDocumentName = false;
        try{
        	if( documentFormId != null || !(documentFormId.isEmpty()) ) {
        		isDuplicateDocumentName = documentFormService.checkDocumentName(documentFormName,documentFormId,folderId);
        	}else {
        		isDuplicateDocumentName = documentFormService.checkDocumentName(documentFormName,null,folderId);        		
        	}
        }catch(Exception e){
            log.error("Error while checking the Document Form name "+e);
        }
        return isDuplicateDocumentName;
    }
    /**
     * method for Manage FileImport
     * 
     * @param request
     * @param model
     * @author Ramachandran.K
     * @return
     */
    @RequestMapping(value="bpm/dms/fileImportForm" ,method = RequestMethod.GET)
    public  ModelAndView fileImportForm(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
    	FileImportForm importFile= new FileImportForm();
    	model.addAttribute("importFile",importFile);
    	return new ModelAndView("dms/importFileForm",model);
    }
    
    /**
	 * save import File Form.
	 * 
	 * @param model
	 * @param fileImport
	 * @param files
	 * @param request
	 * @param response
	 * @author Ramachandran.K
	 * @return
	 */
	@RequestMapping(value = "bpm/dms/saveImportFile",method = RequestMethod.POST)
    public ModelAndView saveImportFile(ModelMap model,@ModelAttribute("importFile") FileImportForm importFile,@RequestParam CommonsMultipartFile[] file,BindingResult errors, HttpServletRequest request,
           HttpServletResponse response) { 
       	try{
       		String path=getServletContext().getRealPath("/WEB-INF") + "/pages/jsp/";
			String flag=request.getParameter("flag");
			File mkFolder = new File(path);
			if(!mkFolder.isDirectory()){
				if(mkFolder.mkdir()){
					log.info("Directory Created Successfully at the Particular Location");
				}
			}
			for (CommonsMultipartFile uploadFile : file){
				//log.info("Saving file Location==: " + uploadFile.getOriginalFilename());
				if (!uploadFile.getOriginalFilename().equals("")) {
					String uploadFilePath = path +  uploadFile.getOriginalFilename();
					String uploadedPath="/WEB-INF/pages/jsp/"+uploadFile.getOriginalFilename();
					importFile.setResourcePath(uploadedPath);
					importFile.setPath(uploadFilePath);
					importFile.setMimeType(uploadFile.getContentType());
					importFile = documentFormService.saveOrUpdateImportFile(importFile,flag,request.getRemoteUser());
					uploadFile.transferTo(new File( path + uploadFile.getOriginalFilename()));
					String listView="FILEIMPORTFORMGRID";// Here to redirect the list view attributes
					String container = "target";
					String listViewParams = "[{}]";
					String title = "File List";
					model.addAttribute("listViewName",listView);
					model.addAttribute("container",container);
					model.addAttribute("listViewParams",listViewParams);
					model.addAttribute("title",title);
					saveMessage(request,"File Saved Successfully");	
				} else {
					log.info("The content of File Is Empty");
					saveMessage(request,"Please Select the jsp template file");
				}
			}
		} catch(Exception ex){
			log.info("Upload Failed ",ex);
			saveMessage(request,"File Save Failed");	
			
		}
		
       	return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
	}
	/**
	 * method for delete the selected Files from Imported File grid
	 * 
	 * @param fileIds
	 * @param request
	 * @return
	 * @author Ramachandran.K
	 */
	@RequestMapping(value = "bpm/dms/deleteSelectedFiles", method = RequestMethod.GET)
	public ModelAndView deleteSelectedFiles(@RequestParam("fileIds") String fileId, HttpServletRequest request,ModelMap model) {
		 List<FileImportForm> fileImportFormList = new ArrayList<FileImportForm>();
		 String listView="FILEIMPORTFORMGRID";
		 String container = "target";
		 String listViewParams = "[{}]";
		 String title = "File List";
		 model.addAttribute("listViewName",listView);
		 model.addAttribute("container",container);
		 model.addAttribute("listViewParams",listViewParams);
		 model.addAttribute("title",title);
		 if (fileId.contains(",")) {
			  String[] fileIdList = fileId.split(",");
			  for(String id :fileIdList){
			  	FileImportForm fileImportForm = new FileImportForm();
			  	fileImportForm.setId(id);
				fileImportFormList.add(fileImportForm);
			  }
			} else {
				FileImportForm fileImportForm = new FileImportForm();
			  	fileImportForm.setId(fileId);
				fileImportFormList.add(fileImportForm);
			}
		try{
			boolean isDeleted = documentFormService.deleteImportFile(fileImportFormList);
			if(isDeleted){
				saveMessage(request,"File Deleted Successfully");
			}
		}catch(Exception e){
			log.info("While Deleting Selected Files Exception Occured"+e);
			saveMessage(request,"The Requested File not available");
		}
	
		return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
	}
	 /**
	  * display import File Form.
	  * 
	  * @param model
	  * @param fileId
	  * @param request
	  * @param response
	  * @author Ramachandran.K
	  * @return
	  */
	 @RequestMapping(value = "bpm/dms/editImportFile",method = RequestMethod.GET)
	 public ModelAndView editImportFile(@RequestParam("fileId") String fileId,ModelMap model, HttpServletRequest request,HttpServletResponse response) { 
		try{
			model.addAttribute("importFile",documentFormService.getImportFileById(fileId));
		} catch(Exception e){
			log.info(e.getMessage(),e);
		}
		return new ModelAndView("dms/importFileForm",model);
	}

	 @RequestMapping(value = "bpm/admin/showImportFilesSelection", method = RequestMethod.GET)
		public ModelAndView showImportFilesSelection(ModelMap model,@RequestParam("selectionType") String selectionType,@RequestParam("appendTo") String appendTo,
				@RequestParam("selectedValues") String selectedValues,
				@RequestParam("rootNodeURL") String rootNodeURL,
				@RequestParam("callAfter") String callAfter, User user,BindingResult errors, HttpServletRequest request,
				HttpServletResponse response) {
			//log.info("getting ListView selection details");
			JSONArray nodes = new JSONArray();
			Map<String, Object> context = new HashMap<String, Object>();
			try {
				context.put("nodes", nodes);
				context.put("rootNodeURL", rootNodeURL);
				context.put("selectionType", selectionType);
				context.put("selection", "widgetImportFiles");
				context.put("selectedValues", selectedValues);
				context.put("needContextMenu", true);
				context.put("needTreeCheckbox", true);
				String script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizationTree.vm", context);
				model.addAttribute("script", script);
				model.addAttribute("selection","widgetImportFiles" );
				model.addAttribute("appendTo", appendTo);
				model.addAttribute("callAfter", callAfter);
				model.addAttribute("selectionType", selectionType);
				model.addAttribute("selectedValues", selectedValues);
				
			} catch (BpmException e) {	
				log.warn(e.getMessage(), e);
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
			return new ModelAndView("admin/organizationTree", model);
		}


@RequestMapping(value="bpm/admin/getImportFilesRootNodes", method = RequestMethod.GET)
public @ResponseBody List<Map<String, Object>> getImportFilesRootNodes(ModelMap model,@RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel){
    	//log.info("getting list view selection details");
    	List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
   // 	List<Widget> widgetsList= widgetService.getAllWidget();
    	
    	List<FileImportForm> fileImportFormList = documentFormService.getAllFiles();
    //	documentFormService.getAllDocuments();
    	try {
    		for(FileImportForm fileImportForm : fileImportFormList ){
    			CommonUtil.createImportFilesRootTreeNode(nodeListOfMap,fileImportForm.getId(),fileImportForm.getId().replace(".",""),  fileImportForm.getName(),fileImportForm.getResourcePath());
    		//	CommonUtil.createMenuRootTreeNode(nodeListOfMap, menu.getId(), menu.getId().replace(".",""), menu.getName(),menu.getMenuType());
    		}
/*
    		for(LabelValue listView : listViewList ){
    			
    			CommonUtil.createListViewRootTreeNode(nodeListOfMap,listView.getLabel() ,  listView.getValue(),listView.getLabel());
    		}*/
    		
    	} catch (BpmException e) {
			log.warn(e.getMessage(),e);
			
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
    	}
    	return nodeListOfMap;
}


   public JSONObject getFolderChildNodesAfterCreate(String parentFolder,Folder folder,HttpServletRequest request,Locale locale ){
		log.info("getting user selection child nodes");
		JSONObject jsonNodes = new JSONObject();
   	JSONArray nodes = new JSONArray();
   	List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
   	Map<String, Object> nodeDetails = new HashMap<String, Object>();
   	Map<String, Object> attr = new HashMap<String, Object>();
   	try {
		boolean canEdit = false;
    	boolean canDelete = false;
    	boolean canCreate = false; 
    	boolean canRead = false;
   		List<LabelValue> folders = folderService.getChildFoldersAsLabelValue(parentFolder);
			for(LabelValue child : folders){
	        	if(folder.getId().equalsIgnoreCase(child.getValue())) {
					attr.put("id", child.getValue());
		    		attr.put("name", child.getLabel());
		    		attr.put("isRoot", false);
					folder = folderService.getFolder(child.getValue());
					
					if(request.isUserInRole(Constants.ADMIN_ROLE)){
						canEdit = true;
						canDelete = true;
						canCreate = true;
						canRead = true;
		    		}
					
					if(folder.getOwner().equals(request.getRemoteUser())){
						canEdit = true;
						canDelete = true;
						canCreate = true;
						canRead = true;
					}
					
					if(folder.getCreatedBy().equals(request.getRemoteUser())){
						canEdit = true;
						canDelete = true;
						canCreate = true;
						canRead = true;
					}
					
		    		if(canEdit != true){
	   	    		for(DMSRolePermission dmsRolePermission : folderService.getDmsUserRolePermission(request.getRemoteUser(), child.getValue())){
	   					if(dmsRolePermission.getCanEdit() == true){
	   						canEdit = true;
	   						canDelete = true;
	   						canCreate = true;
	   						canRead = true;
	   					} else if(dmsRolePermission.getCanRead() == true) {
	   						canCreate = false;
	   						canRead = true;
	   					}
	   				}
		    		}
		    		
		    		if(canEdit != true || canDelete != true){
		    			for(DMSRolePermission dmsRolePermission : folderService.getDmsRolePermission(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserRoles(), ",", true), child.getValue())) {
	   					if(dmsRolePermission.getCanEdit() == true){
	   						canEdit = true;
	   						canDelete = true;
	   						canCreate = true;
	   						canRead = true;
	   					} else if(dmsRolePermission.getCanRead() == true) {
	   						canCreate = false;
	   						canRead = true;
	   					}
	   				}
		    		}
		    		
		    		if(canEdit != true || canDelete != true){
	   				for(DMSRolePermission dmsGroupRolePermission : folderService.getDmsGroupRolePermission(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserGroups(), ",", true), child.getValue())){
	   					if(dmsGroupRolePermission.getCanEdit() == true){
	   						canEdit = true;
	   						canDelete = true;
	   						canCreate = true;
	   						canRead = true;
	   					} else if(dmsGroupRolePermission.getCanRead() == true) {
	   						canCreate = false;
	   						canRead = true;
	   					}
	   				}
		    		}
		    		
		    		if(canEdit != true || canDelete != true){
	   				for(DMSRolePermission dmsDepartmentRolePermission : folderService.getDmsDepartmentRolePermission(userService.getUserByUsername(request.getRemoteUser()).getDepartment().getId(), child.getValue())){
	   					if(dmsDepartmentRolePermission.getCanEdit() == true){
	   						canEdit = true;
	   						canDelete = true;
	   						canCreate = true;
	   						canRead = true;
	   					} else if(dmsDepartmentRolePermission.getCanRead() == true) {
	   						canCreate = false;
	   						canRead = true;
	   					}
	   				}
		    		}
		    		
					attr.put("canEdit", canEdit);
		    		attr.put("canDelete", canDelete);
		    		attr.put("canCreate", canCreate);
		    		attr.put("canRead", canRead);
		    		nodeDetails.put("data", child.getLabel());
		    		nodeDetails.put("attr", attr);
		    		nodeList.add(nodeDetails);
		    		jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
		    		
		    		break;
	        	}
				
			}
   	}catch (BpmException e) {
			log.warn(e.getMessage(),e);
			saveError(request, getText("errors.folder.getNodes",locale));
		} catch (Exception e) {
			saveError(request, getText("errors.folder.getNodes",locale));
   		log.warn(e.getMessage(),e);
   	}
		return jsonNodes;
   }


}
