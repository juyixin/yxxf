package com.eazytec.bpm.oa.dms.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eazytec.bpm.common.util.PathUtils;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.dao.DocumentFormDao;
import com.eazytec.bpm.oa.dms.service.DocumentFormService;
import com.eazytec.bpm.oa.dms.service.FolderService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.FileImportForm;
import com.eazytec.model.PermissionDTO;
import com.eazytec.model.User;
import com.eazytec.service.UserService;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("documentFormService")
public class DocumentFormServiceImpl extends GenericManagerImpl<DocumentForm, String>  implements DocumentFormService {

	private DocumentFormDao documentFormDao;
	
	private FileImportForm importFileForm;
	
	private FolderService folderService;
	
	private UserService userService;
	
	public FolderService getFolderService() {
		return folderService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}

	@Autowired
	public void setDocumentFormDao(DocumentFormDao documentFormDao){
		this.dao=documentFormDao;
		this.documentFormDao=documentFormDao;
	}
	
	@Override
	public void deleteDocumentForm(DocumentForm documentForm) {
		// TODO Auto-generated method stub
		dao.remove(documentForm);
	}

	@Override
	public DocumentForm saveOrUpdateDocumentForm(DocumentForm documentForm) {
		return dao.save(documentForm);
	}
	
	public DocumentForm saveOrUpdateDocumentForm(DocumentForm documentForm,List<MultipartFile> files, String loggedInUser,String path) throws BpmException{
		Date date = new Date();
		try {
			if(StringUtil.isEmptyString(documentForm.getId())) {
				documentForm.setCreatedBy(loggedInUser);
				documentForm.setUpdatedBy(loggedInUser);
				documentForm.setCreatedTime(date);
				documentForm.setModifiedTime(date);
			} else { 
				documentForm.setUpdatedBy(loggedInUser);
				documentForm.setModifiedTime(date);
			}
			String parentDir = path+documentForm.getFolder().getName();
			//String parentDir = path+"test"+"/";
			if(files != null  && !files.isEmpty()){
				documentForm.setDocuments(new HashSet<Document>());
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						Document document = new Document();
						String fileName = multipartFile.getOriginalFilename();
						fileName = fileName.replace(' ', '_');
						/*if(!ValidationUtil.validateFileFormats(fileName)){
							 throw new BpmException("Uploaded file format not support");
						 }*/
						String parentPath = parentDir;//PathUtils.getParent("/bpm:root/trabajo/");
						String name = PathUtils.escape(fileName);
						document.setPath(parentPath);
						document.setName(name);
						document.setCreatedBy(loggedInUser);
						document.setCreatedTime(date);
						document.setModifiedTime(date);
						document.setMimeType(multipartFile.getContentType());
						documentForm.getDocuments().add(document);
						if(!StringUtil.isEmptyString(documentForm.getId())){
							DocumentForm oldDocumentForm = dao.get(documentForm.getId());   
							if(null != oldDocumentForm.getDocuments() && oldDocumentForm.getDocuments().size() >0){
								documentForm.getDocuments().addAll(oldDocumentForm.getDocuments());
							}
						}
					}
				} 
			} else if(!StringUtil.isEmptyString(documentForm.getId())){
				DocumentForm oldDocumentForm = dao.get(documentForm.getId());   
				if(null != oldDocumentForm.getDocuments() && oldDocumentForm.getDocuments().size() >0){
					documentForm.getDocuments().addAll(oldDocumentForm.getDocuments());
				}
			}
			
			documentForm =  dao.save(documentForm);
			
			String uploadDir = path+documentForm.getFolder().getName()+"/"+documentForm.getId()+"/";
	         // Create the directory if it doesn't exist
	         File dirPath = new File(uploadDir);
	         if (!dirPath.exists()) {
	             dirPath.mkdirs();
	         }
	         
			if(null != files && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
					      // retrieve the file data
		                InputStream stream = multipartFile.getInputStream();
		                // write the file to the file specified
		                OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename().replace(' ','_'));
		                int bytesRead;
		                byte[] buffer = new byte[8192];
		                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		                    bos.write(buffer, 0, bytesRead);
		                }
		                bos.close();
		                // close the stream
		                stream.close();
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new BpmException("problem in saving document"+e);
		}
		return documentForm;
	}
	
	@Override
	public FileImportForm	saveOrUpdateImportFile(FileImportForm importFileForm,String flag,String currentUser) throws Exception   {
		Date date = new Date();
		try{
			if(flag.equals("update")){
				importFileForm.setUpdatedBy(currentUser);
				importFileForm.setModifiedTime(date);
				File file=new File(documentFormDao.searchImportFileById(importFileForm.getId()));
				boolean isFileDelete;
				if(file.exists()){
					isFileDelete = file.delete();
					if(isFileDelete){
						importFileForm = documentFormDao.saveOrUpdateImportFile(importFileForm);
					} else {
						log.info("The Selected File can Not delete from Server"+file.getName());
					}
				} else{
					log.info("The file didnt available in Server");
				}
			} 
			if (flag.equals("insert")){
				importFileForm.setCreatedBy(currentUser);
				importFileForm.setCreatedTime(date);
				importFileForm.setUpdatedBy(currentUser);
				importFileForm.setModifiedTime(date);
				importFileForm = documentFormDao.saveOrUpdateImportFile(importFileForm);
			}
		} catch(Exception e){
			throw new Exception("File not Available:"+e,e);
		}
		return importFileForm;
	}
	
	@Override
	public boolean deleteImportFile(List<FileImportForm> fileImportFormList) throws Exception{
		boolean deleted = false;
		try{
			for(FileImportForm fileImportForm : fileImportFormList){
				File file=new File(documentFormDao.searchImportFileById(fileImportForm.getId()));
				boolean isFileDelete;
				if(file.exists()){
					isFileDelete = file.delete();
					if(isFileDelete){
						deleted = documentFormDao.deleteImportFile(fileImportForm);
					} else {
						deleted = false;
						log.info("The Imported File Doest in File Folder"+file.getName());
					}
				} else{
					deleted = false;
					log.info("The File not available");
				}
			}
		} catch(Exception e){
			throw new Exception("Exception at File deletion"+e);
		}
		return deleted;
	}
	
	@Override
	public FileImportForm getImportFileById(String fileId){
		return documentFormDao.getImportFileById(fileId);
	}
	
	public DocumentForm getDocumentForm(String id){
		return dao.get(id);
	}
	
	public FileImportForm getFileImportForm(){
		return importFileForm;
	}
	
	public List<DocumentForm> getAllDocuments(){
		return documentFormDao.getAllDocuments();
	}
	
	public List<DocumentForm> getAllDocumentsByLoggedInUser(){
		CommonUtil.getLoggedInUserId(); StringUtil.convertArrayToString(CommonUtil.getLoggedInUserRoles(), ",", true);
		return documentFormDao.getAllDocumentsByLoggedInUser();
	}
	
	public PermissionDTO getFolderPermission(PermissionDTO permissionDTO,String remoteUser, String folderId) {
		List<DMSRolePermission> dmsDepartmentPermissions = folderService.getDmsDepartmentRolePermission( userService.getUserByUsername(remoteUser).getDepartment().getId(), folderId); 
		List<DMSRolePermission> dmsUserPermissions = folderService.getDmsUserRolePermission(remoteUser,folderId);
		List<DMSRolePermission> dmsRolePermissions = folderService.getDmsRolePermission(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserRoles(), ",", true),folderId );
		List<DMSRolePermission> dmsGroupPermissions = folderService.getDmsGroupRolePermission(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserGroups(), ",", true),folderId);
		permissionDTO = isGrantedByDepartment(dmsDepartmentPermissions, permissionDTO);
		permissionDTO=isGrantedByUser(dmsUserPermissions,  permissionDTO);
		permissionDTO=isGrantedByRole(dmsRolePermissions,  permissionDTO);
		permissionDTO=isGrantedByGroup(dmsGroupPermissions,  permissionDTO);
		return permissionDTO;		
	}
	
	
	public PermissionDTO getPermission(DocumentForm documentForm,
			User loggedInUser){
		PermissionDTO permissionDTO=new PermissionDTO();
		List<DMSRolePermission> dmsDepartmentPermissions = documentFormDao.getDMSDepartmentPermissionByDocumentForm(userService.getUserByUsername(loggedInUser.getUsername()).getDepartment().getId(),documentForm.getId());
		List<DMSRolePermission> dmsUserPermissions = documentFormDao.getDMSUserPermissionByDocumentForm(loggedInUser.getUsername(), documentForm.getId());
		List<DMSRolePermission> dmsRolePermissions = documentFormDao.getDMSRolePermissionByDocumentForm(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserRoles(), ",", true), documentForm.getId());
		List<DMSRolePermission> dmsGroupPermissions = documentFormDao.getDMSGroupPermissionByDocumentForm(StringUtil.convertArrayToString(CommonUtil.getLoggedInUserGroups(), ",", true), documentForm.getId());
		permissionDTO = isGrantedByDepartment(dmsDepartmentPermissions, permissionDTO);
		permissionDTO=isGrantedByUser(dmsUserPermissions,  permissionDTO);
		permissionDTO=isGrantedByRole(dmsRolePermissions,  permissionDTO);
		permissionDTO=isGrantedByGroup(dmsGroupPermissions,  permissionDTO);
		int permissionCount = documentFormDao.checkIfPermissionSet(documentForm.getId());
		if(permissionCount == 0) {
			permissionDTO.setIsPublic(true);
		}
		return permissionDTO;
	}
	
	private PermissionDTO isGrantedByUser(List<DMSRolePermission> dmsUserPermissions,
			 PermissionDTO permissionDTO) {
		log.info("permissionDTO========isGrantedByUser======="+permissionDTO);
		if (permissionDTO.getCanCreate() == false){
			for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
				if(dmsUserPermission.getCanCreate()){
					permissionDTO.setCanCreate(true);
				}
			}
		}
		if (permissionDTO.getCanDelete() == false) {
			for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
				if(dmsUserPermission.getCanDelete()){
					permissionDTO.setCanDelete(true);
				}
			}
		}

		if (permissionDTO.getCanDownload() == false) {
			for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
				if(dmsUserPermission.getCanDownload()){
					permissionDTO.setCanDownload(true);
				}
			}
		}

		if (permissionDTO.getCanEdit() == false) {
			for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
				if(dmsUserPermission.getCanEdit()){
					permissionDTO.setCanEdit(true);
				}
			}
		}

		if (permissionDTO.getCanPrint() == false) {
			for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
				if(dmsUserPermission.getCanPrint()){
					permissionDTO.setCanPrint(true);
				}
			}
		}

		if (permissionDTO.getCanRead() == false) {
			for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
				if(dmsUserPermission.getCanRead()){
					permissionDTO.setCanRead(true);
				}
			}
		}
		log.info("permissionDTO========isGrantedByUser====end==="+permissionDTO);
		return permissionDTO;
	}
	
	private PermissionDTO isGrantedByRole(List<DMSRolePermission> dmsRolePermissions,
			 PermissionDTO permissionDTO) {
		log.info("permissionDTO=======isGrantedByRole======="+permissionDTO);
		if (permissionDTO.getCanCreate() == false){
			for (DMSRolePermission dmsRolePermission : dmsRolePermissions) {
				if(dmsRolePermission.getCanCreate()){
					permissionDTO.setCanCreate(true);
				}
			}
		}
		if (permissionDTO.getCanDelete() == false) {
			for (DMSRolePermission dmsRolePermission : dmsRolePermissions) {
				if(dmsRolePermission.getCanDelete()){
					permissionDTO.setCanDelete(true);
				}
			}
		}

		if (permissionDTO.getCanDownload() == false) {
			for (DMSRolePermission dmsRolePermission : dmsRolePermissions) {
				if(dmsRolePermission.getCanDownload()){
					permissionDTO.setCanDownload(true);
				}
			}
		}

		if (permissionDTO.getCanEdit() == false ) {
			for (DMSRolePermission dmsRolePermission : dmsRolePermissions) {
				if(dmsRolePermission.getCanEdit()){
					permissionDTO.setCanEdit(true);
				}
			}
		}

		if (permissionDTO.getCanPrint() == false) {
			for (DMSRolePermission dmsRolePermission : dmsRolePermissions) {
				if(dmsRolePermission.getCanPrint()){
					permissionDTO.setCanPrint(true);
				}
			}
		}

		if (permissionDTO.getCanRead() == false) {
			for (DMSRolePermission dmsRolePermission : dmsRolePermissions) {
				if(dmsRolePermission.getCanRead()){
					permissionDTO.setCanRead(true);
				}
			}
		}
		log.info("permissionDTO=======isGrantedByRole====end==="+permissionDTO);
		return permissionDTO;
	}
	
	private PermissionDTO isGrantedByGroup(List<DMSRolePermission> dmsGroupPermissions,
			 PermissionDTO permissionDTO) {
		log.info("permissionDTO=======isGrantedByGroup========"+permissionDTO);
		if (permissionDTO.getCanCreate()==false){
			for (DMSRolePermission dmsGroupPermission : dmsGroupPermissions) {
				if(dmsGroupPermission.getCanCreate()){
					permissionDTO.setCanCreate(true);
				}
			}
		}
		if (permissionDTO.getCanDelete()==false) {
			for (DMSRolePermission dmsGroupPermission : dmsGroupPermissions) {
				if(dmsGroupPermission.getCanDelete()){
					permissionDTO.setCanDelete(true);
				}
			}
		}

		if (permissionDTO.getCanDownload()==false) {
			for (DMSRolePermission dmsGroupPermission : dmsGroupPermissions) {
				if(dmsGroupPermission.getCanDownload()){
					permissionDTO.setCanDownload(true);
				}
			}
		}

		if (permissionDTO.getCanEdit()==false) {
			for (DMSRolePermission dmsGroupPermission : dmsGroupPermissions) {
				if(dmsGroupPermission.getCanEdit()){
					permissionDTO.setCanEdit(true);
				}
			}
		}

		if (permissionDTO.getCanPrint()==false) {
			for (DMSRolePermission dmsGroupPermission : dmsGroupPermissions) {
				if(dmsGroupPermission.getCanPrint()){
					permissionDTO.setCanPrint(true);
				}
			}
		}

		if (permissionDTO.getCanRead()==false) {
			for (DMSRolePermission dmsGroupPermission : dmsGroupPermissions) {
				if(dmsGroupPermission.getCanRead()){
					permissionDTO.setCanRead(true);
				}
			}
		}
		log.info("permissionDTO=======isGrantedByGroup====end===="+permissionDTO);
		return permissionDTO;
	}
	
	private PermissionDTO isGrantedByDepartment(List<DMSRolePermission> dmsDepartmentPermissions,
			 PermissionDTO permissionDTO) {
		log.info("permissionDTO========isGrantedByDepartment======="+permissionDTO);
		if (permissionDTO.getCanCreate() == false){
			for (DMSRolePermission dmsDepartmentPermission : dmsDepartmentPermissions) {
				if(dmsDepartmentPermission.getCanCreate()){
					permissionDTO.setCanCreate(true);
				}
			}
		}
		if (permissionDTO.getCanDelete() == false) {
			for (DMSRolePermission dmsDepartmentPermission : dmsDepartmentPermissions) {
				if(dmsDepartmentPermission.getCanDelete()){
					permissionDTO.setCanDelete(true);
				}
			}
		}

		if (permissionDTO.getCanDownload() == false) {
			for (DMSRolePermission dmsDepartmentPermission : dmsDepartmentPermissions) {
				if(dmsDepartmentPermission.getCanDownload()){
					permissionDTO.setCanDownload(true);
				}
			}
		}

		if (permissionDTO.getCanEdit() == false) {
			for (DMSRolePermission dmsDepartmentPermission : dmsDepartmentPermissions) {
				if(dmsDepartmentPermission.getCanEdit()){
					permissionDTO.setCanEdit(true);
				}
			}
		}

		if (permissionDTO.getCanPrint() == false) {
			for (DMSRolePermission dmsDepartmentPermission : dmsDepartmentPermissions) {
				if(dmsDepartmentPermission.getCanPrint()){
					permissionDTO.setCanPrint(true);
				}
			}
		}

		if (permissionDTO.getCanRead() == false) {
			for (DMSRolePermission dmsDepartmentPermission : dmsDepartmentPermissions) {
				if(dmsDepartmentPermission.getCanRead()){
					permissionDTO.setCanRead(true);
				}
			}
		}
		log.info("permissionDTO========isGrantedByDepartment====end==="+permissionDTO);
		return permissionDTO;
	}

	public boolean checkDocumentName(String documentFormName,String documentFormId,String folderId) {
		log.info("Document Name "+documentFormName+" Document Id "+documentFormId);
		return documentFormDao.checkDocumentName(documentFormName, documentFormId, folderId);
	}
	
	public Document getDocumentById(String id){
		return documentFormDao.getDocumentById(id);
	}

	public DMSRolePermission getDMSUserPermission(DocumentForm documentForm, String userName){
    	return documentFormDao.getDMSUserPermission(documentForm, userName);
    }
    
    public DMSRolePermission getDMSRolePermission(DocumentForm documentForm, String roleName){
    	return documentFormDao.getDMSRolePermission(documentForm, roleName);
    }
    
    public DMSRolePermission getDMSGroupPermission(DocumentForm documentForm, String groupName){
    	return documentFormDao.getDMSGroupPermission(documentForm, groupName);
    }
    public DMSRolePermission getDMSDepartmentPermission(DocumentForm documentForm, String deparmentId){
    	return documentFormDao.getDMSDepartmentPermission(documentForm, deparmentId);
    }
    
    public void removeDMSUserPermission (DocumentForm documentForm){
    	documentFormDao.removeDMSUserPermission(documentForm);
    }
    public void removeDMSRolePermission (DocumentForm documentForm){
    	documentFormDao.removeDMSRolePermission(documentForm);
    }
    public void removeDMSGroupPermission (DocumentForm documentForm){
    	documentFormDao.removeDMSGroupPermission(documentForm);
    }
    
    public void removeDMSDepartmentPermission (DocumentForm documentForm){
    	documentFormDao.removeDMSDepartmentPermission(documentForm);
    }
    
    public List<DocumentForm> getDocumentsByFolderId (String folderId){
    	return documentFormDao.getDocumentsByFolderId(folderId);
    }
    
    /**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsUserPermissionByDocument(String documentId){
		return documentFormDao.getDmsUserPermissionByDocument(documentId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsDepartmentPermissionByDocument(String documentId){
		return documentFormDao.getDmsDepartmentPermissionByDocument(documentId);
	}
		
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsRolePermissionByDocument(String documentId){
		return documentFormDao.getDmsRolePermissionByDocument(documentId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsGroupPermissionByDocument(String documentId){
		return documentFormDao.getDmsGroupPermissionByDocument(documentId);
	}
	
	 public void deleteGroupPermission(List<String> ids){ 
		 documentFormDao.deleteGroupPermission(ids);
	 }
	 
	 public void deleteUserPermission(List<String> ids){ 
		 documentFormDao.deleteUserPermission(ids);
	 }
	 
	 public void deleteRolePermission(List<String> ids){
		 documentFormDao.deleteRolePermission(ids);
	 }
	 
	/**
     * {@inheritDoc}
     */
	 public void deleteDocumentById(String documentId) {
		 documentFormDao.deleteDocumentById(documentId);
	 }
	 
	 public List<FileImportForm> getAllFiles()throws EazyBpmException{
			return documentFormDao.getAllFiles();
	 }
	
}
