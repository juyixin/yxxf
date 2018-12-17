package com.eazytec.bpm.oa.dms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.FileImportForm;
import com.eazytec.model.PermissionDTO;
import com.eazytec.model.User;

/**
 * Interface to provide service for scheduling the events 
 * 
 * @author Vinoth
 */
public interface DocumentFormService {

	/**
	 * To save/update the documentForm
	 * @param folder
	 * @return
	 */
	public DocumentForm saveOrUpdateDocumentForm(DocumentForm documentForm);
	
	/**
	 * To delete the documentForm
	 * @param folder
	 * @return
	 */
	public void deleteDocumentForm(DocumentForm documentForm);
	
	/**
	 * This method used to delete the fileimported information 
	 *
	 * @param List
	 * @author Ramachandran.K
	 * @return 
	 */
	public boolean deleteImportFile(List<FileImportForm> fileImportFormList)  throws Exception;
	
	/**
	 * 
	 * @param documentForm
	 * @param files
	 * @param loggedInUser
	 * @return
	 */
	public DocumentForm saveOrUpdateDocumentForm(DocumentForm documentForm,List<MultipartFile> files, String loggedInUser,String path);
	
	/**
	 * This method used to save or update the fileimported information based on flag value
	 *
	 * @param fileImportForm
	 * @param flag
	 * @param request
	 * @author Ramachandran.K
	 * @return 
	 */
	public FileImportForm saveOrUpdateImportFile(FileImportForm importFileForm,String flag,String currentUser) throws Exception;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DocumentForm getDocumentForm(String id);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public FileImportForm getFileImportForm();
	
	/**
	 * This method used to get fileimported information 
	 *
	 * @param fileId
	 * @author Ramachandran.K
	 * @return 
	 */
	public FileImportForm getImportFileById(String fileId) throws Exception;
		
	/**
	 * 
	 * @return
	 */
	public List<DocumentForm> getAllDocuments();
	
	/**
	 * method will returns documents for the logged in user.
	 * @return
	 */
	public List<DocumentForm> getAllDocumentsByLoggedInUser();

	public PermissionDTO getPermission(DocumentForm documentForm,
			User loggedInUser);

	public DMSRolePermission getDMSUserPermission(DocumentForm documentForm, String userName);
    public DMSRolePermission getDMSRolePermission(DocumentForm documentForm, String roleName);
    public DMSRolePermission getDMSGroupPermission(DocumentForm documentForm, String groupName);
    public DMSRolePermission getDMSDepartmentPermission(DocumentForm documentForm, String deparmentId);
    
    public void removeDMSUserPermission (DocumentForm documentForm);
    public void removeDMSRolePermission (DocumentForm documentForm);
    public void removeDMSGroupPermission (DocumentForm documentForm);
    public void removeDMSDepartmentPermission (DocumentForm documentForm);
    
    public Document getDocumentById(String id);
    
    public List<DocumentForm> getDocumentsByFolderId (String folderId);
    
    /**
	 * get all dms user permissions
	 * 
	 * 
	 * @param documentId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsUserPermissionByDocument(String documentId);
	
	/**
	 * get all dms department permissions
	 * 
	 * 
	 * @param documentId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsDepartmentPermissionByDocument(String documentId);
	
	/**
	 * get all dms role permissions
	 * 
	 * @param documentId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsRolePermissionByDocument(String documentId);
	
	/**
	 * get all dms group permissions
	 * 
	 * @param documentId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsGroupPermissionByDocument(String documentId);
	
	
	public void deleteGroupPermission(List<String> ids);
	
	public void deleteUserPermission(List<String> ids);
	
	public void deleteRolePermission(List<String> ids);
	 
	/**
	 * Delete Document from Document Form
	 * @param documentFormId
	 */
	void deleteDocumentById(String documentId);

	public boolean checkDocumentName(String documentFormName,String documentFormId,String folderId);

	public PermissionDTO getFolderPermission(PermissionDTO permissionDTO, String remoteUser, String string);
	
	public List<FileImportForm> getAllFiles()throws EazyBpmException;

}

