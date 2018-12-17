package com.eazytec.bpm.oa.dms.service;

import java.util.List;

import org.springframework.ui.ModelMap;

import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.Folder;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;

/**
 * Interface to provide service for scheduling the events 
 * 
 * @author Vinoth
 */
public interface FolderService {

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
	 * @param folder
	 * @param loggedInUser
	 * @return
	 */
	public Folder saveOrUpdateFoler(Folder folder,String loggedInUser, ModelMap model);
	
	/**
	 * 
	 * @return List<Folder>
	 */
	public List<Folder> getAllFolders();
	
	/**
	 * Get Folder by id
	 * @param id
	 * @return
	 */
	public Folder getFolder(String id);
	
	/**
	 * 
	 * @return
	 */
	public List<Folder> getAllFoldersByLoggedInUser();
	
	public Folder getPermission(Folder folder,User user);
	
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
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsUserPermission(String id);
	
	/**
	 * get all dms role permissions
	 * 
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsRolePermission(String id);
	
	/**
	 * get all dms group permissions
	 * 
	 * @return {@link List}
	 * 
	 */
	public DMSRolePermission getDmsGroupPermission(String id);
	
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
	public List<DMSRolePermission> getDmsRolePermission(String userId, String folderId);
	
	/**
	 * get all dms group permissions
	 * 
	 * @param folderId
	 * 
	 * @return {@link List}
	 * 
	 */
	public List<DMSRolePermission> getDmsGroupRolePermission(String userId, String idfolderId);
	
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
	public List<Folder> getFolderByOwner(String owner);
	
	/**
	 * update folder owner
	 * 
	 * @param oldOwner
	 * @param newOwner
	 * 
	 */
	void updateFolderOwner (String oldOwner, String newOwner);
	
	//public boolean checkNameExists(String name) throws EazyBpmException;
	
	
	public List<DMSRolePermission> getDmsRolePermissionByFolderId(String folderId);
	
	public List<DMSRolePermission> getDmsRolePermissionByDocumentId(String documentId);
	
}

