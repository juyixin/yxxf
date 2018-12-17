package com.eazytec.bpm.oa.dms.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSGroupPermission;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DMSUserPermission;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.FileImportForm;
import com.eazytec.model.LabelValue;
/**
 * 
 * @author vinoth
 *
 */
public interface DocumentFormDao extends GenericDao<DocumentForm, String> {
	
	/**
	 * To save/update the documentForm
	 * @param folder
	 * @return
	 */
	public DocumentForm saveOrUpdateDocumentForm(DocumentForm documentForm);
	
	/**
	 * To save/update the importFileForm
	 * @param folder
	 * @return
	 */
	public FileImportForm saveOrUpdateImportFile(FileImportForm  importFileForm);
	
	/**
	 * To delete the documentForm
	 * @param folder
	 * @return
	 */
	public void deleteDocumentForm(DocumentForm documentForm);
	
	/**
	 * 
	 * @return
	 */
	public List<DocumentForm> getAllDocuments();
			
	/**
	 * 
	 * @return
	 */
	public String searchImportFileById(String fileId);
	
	/**
	 * 
	 * @return
	 */
	public FileImportForm getImportFileById(String fileId);
	
	/**
	 * 
	 * @return
	 */
	public boolean deleteImportFile(FileImportForm fileImportForm);
	
	/**
	 * 
	 * @return
	 */
	public List<DocumentForm> getAllDocumentsByLoggedInUser();
	
	public DMSRolePermission saveOrUpdateDmsUserPermission(DMSRolePermission dmsUserPermission);
	public DMSRolePermission saveOrUpdateDmsRolePermission(DMSRolePermission dmsRolePermission);
	public DMSRolePermission saveOrUpdateDmsGroupPermission(DMSRolePermission dmsGroupPermission);
	public DMSRolePermission saveOrUpdateDmsDepartmentPermission(DMSRolePermission dmsdepartmentPermission);
	
	public List<DMSRolePermission> getDMSDepartmentPermissionByDocumentForm(String departmentId, String documentFormId);
	public List<DMSRolePermission> getDMSGroupPermissionByDocumentForm(String groupNames,String documentFormId);
	public List<DMSRolePermission> getDMSRolePermissionByDocumentForm(String roleNames,String documentFormId);
	public List<DMSRolePermission> getDMSUserPermissionByDocumentForm(String string ,String documentFormId);

   public DMSRolePermission getDMSUserPermission(DocumentForm documentForm, String userName);
    public DMSRolePermission getDMSRolePermission(DocumentForm documentForm, String roleName);
    public DMSRolePermission getDMSGroupPermission(DocumentForm documentForm, String groupName);
    public DMSRolePermission getDMSDepartmentPermission(DocumentForm documentForm, String departmentName);
    
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
	 * get all dms user permissions
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
	 
	 public List<FileImportForm> getAllFiles()throws EazyBpmException;

	public int checkIfPermissionSet(String id);
}
