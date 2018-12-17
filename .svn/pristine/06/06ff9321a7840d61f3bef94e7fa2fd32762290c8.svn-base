package com.eazytec.bpm.oa.dms.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSGroupPermission;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DMSUserPermission;
import com.eazytec.model.Folder;
import com.eazytec.model.FolderPermission;
import com.eazytec.model.LabelValue;
/**
 * 
 * @author vinoth
 *
 */
public interface FolderDao extends GenericDao<Folder, String> {
	
	/**
	 * To save/update the folder
	 * @param folder
	 * @return
	 */
	public Folder saveOrUpdateFoler(Folder folder);
	
	/**
	 * To delete the folder
	 * @param folder
	 * @return
	 */
	public void deleteFoler(Folder folder);
	

	/**
	 * 
	 * @return
	 */
	public List<Folder> getAllFolders();
	
	public FolderPermission saveOrUpdateFolerPermission(FolderPermission folderPermission);
	
	public void deleteFolderPermission(String id);
	
	public List<Folder> getAllFoldersByUser(String userId,String roles);
	
	public void removeDMSUserPermission (String folderId);
    public void removeDMSRolePermission (String folderId);
    public void removeDMSGroupPermission (String folderId);
    
    /**
     * get root folder as label value
     * 
     * @return {@link List}
     */
    public List<LabelValue> getRootFolderAsLabelValue();
    
    /**
     * get child folder as label value
     * 
     * @return {@link List}
     */
    public List<LabelValue> getChildFoldersAsLabelValue(String parentFolderId);
    
    /**
     * delete child folders when delete parent folder
     * 
     * @param parentFolderId
     */
    public void removechildFolders (String parentFolderId);
    
    /**
     * get child folder as label value
     * 
     * @return {@link List}
     */
    public List<Folder> getChildFolders(String parentFolderId);
    
    /**
	 * To save/update the DMSUserPermission
	 * @param dmsUserPermisison
	 * @return
	 */
	public DMSRolePermission saveDmsUserPermission(DMSRolePermission dmsUserPermisison);
	
	/**
	 * get all dms user permissions
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsUserPermission();
	
	/**
	 * get all dms role permissions
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsRolePermission();
	
	/**
	 * get all dms group permissions
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsGroupPermission();
	
	/**
	 * get all dms department permissions
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsDepartmentPermission();
	
	/**
	 * To save/update the DMSRolePermission
	 * @param dmsRolePermisison
	 * @return
	 */
	public DMSRolePermission saveDmsRolePermission(DMSRolePermission dmsRolePermisison);
	
	 /**
	 * To save/update the DMSGroupPermission
	 * @param dmsGroupPermisison
	 * @return
	 */
	public DMSRolePermission saveDmsGroupPermission(DMSRolePermission dmsGroupPermisison);
	
	
	/**
	 * get all dms user permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsUserPermission(String id);
	
	/**
	 * get all dms role permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsRolePermission(String id);
	
	/**
	 * get all dms group permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsGroupPermission(String id);
	
	/**
	 * get all dms department permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsDepartmentPermission(String id);
	
	/**
	 * get all dms user permissions
	 * 
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsUserPermissionByRoleType(String folderId);
	
	/**
	 * get all dms role permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsRolePermissionByFolder(String folderId);
	
	/**
	 * get all dms group permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsGroupPermissionByRoleType(String folderId);
	
	/**
	 * get all dms department permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsDepartmentPermissionByRoleType(String folderId);
		
	/**
	 * get all dms user permissions
	 * 
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsUserRolePermission(String userId, String folderId);
	
	/**
	 * get all dms role permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsRolePermission(String roleId, String folderId);
	
	/**
	 * get all dms group permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsGroupRolePermission(String groupId, String idfolderId);
	
	/**
	 * get all dms department permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsDepartmentRolePermission(String departmentId, String idfolderId);
	
	
	/**
	 * get folder by owner
	 * 
	 * @param owner
	 * 
	 * @return {@link List}
	 * 
	 */
	List<Folder> getFolderByOwner(String owner);
	
	/**
	 * update folder owner
	 * 
	 * @param oldOwner
	 * @param newOwner
	 * 
	 */
	void updateFolderOwner (String oldOwner, String newOwner);
	
	/**
	 * get folders
	 * 
	 * @param folderName
	 * @param parentFolder
	 * @return
	 */
	List<Folder> getFolders(String folderName);
	
	//public boolean checkNameExists(String name) throws EazyBpmException;
	
	public List<DMSRolePermission> getDmsRolePermissionByFolderId(String folderId);
	
	public List<DMSRolePermission> getDmsRolePermissionByDocumentId(String documentId);
}
