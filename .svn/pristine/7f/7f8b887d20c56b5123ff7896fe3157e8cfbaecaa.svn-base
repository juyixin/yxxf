package com.eazytec.bpm.oa.dms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.eazytec.Constants;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.dao.FolderDao;
import com.eazytec.bpm.oa.dms.service.FolderService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.Folder;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.impl.GenericManagerImpl;


@Service("folderService")
public class FolderServiceImpl extends GenericManagerImpl<Folder, String> implements FolderService {
	
	private FolderDao folderDao;
	
	
	@Autowired
	public void setFolderDao(FolderDao folderDao) {
		this.dao=folderDao;
		this.folderDao = folderDao;
	}

	@Override
	public void deleteFoler(Folder folder) {	
		folderDao.deleteFoler(folder);
	}

	@Override
	public Folder saveOrUpdateFoler(Folder folder) {
		return folderDao.saveOrUpdateFoler(folder);
	}
	
	public Folder saveOrUpdateFoler(Folder folder,String loggedInUser, ModelMap model) {
		Date date = new Date();
		String mode = Constants.EDIT_MODE;
		if(folder.getId().equals(Constants.EMPTY_STRING)) {
			mode = Constants.CREATE_MODE;
			model.addAttribute("mode", mode);
		} else {
			mode = Constants.EDIT_MODE;
			model.addAttribute("mode", mode);
		}
		if(StringUtil.isEmptyString(folder.getOwner())){
			folder.setOwner(loggedInUser);
		}
		if(StringUtil.isEmptyString(folder.getId())) {
			folder.setCreatedBy(loggedInUser);
			folder.setModifiedBy(loggedInUser);
			folder.setCreatedTime(date);
			folder.setModifiedTime(date);
			boolean isDublicate = checkDublicateFolder(folder.getName());
			if(isDublicate){
				throw new BpmException("Folder Name already exists");
			}
		} else { 
			folder.setModifiedBy(loggedInUser);
			folder.setModifiedTime(date);
		}
		folder=folderDao.saveOrUpdateFoler(folder);
		return folder;
	}
	
	public List<Folder> getAllFolders(){
		return folderDao.getAllFolders();
	}
	
	public Folder getFolder(String id){
		return dao.get(id);
	}
	
	public List<Folder> getAllFoldersByLoggedInUser(){
		return folderDao.getAllFoldersByUser(CommonUtil.getLoggedInUserId(), StringUtil.convertArrayToString(CommonUtil.getLoggedInUserRoles(), ",", true));
	}
	
	public Folder getPermission(Folder folder,User user){
		
		Set<DMSRolePermission> dmsUserPermissions = folder.getDmsUserPermission();
		for (DMSRolePermission dmsUserPermission : dmsUserPermissions) {
			if(dmsUserPermission.getName().equalsIgnoreCase(user.getUsername())){
				if(dmsUserPermission.isCanAccess()){
					return folder;
				}
			}
		}
		for (DMSRolePermission dmsRolePermission : folder.getDmsRolePermission()) {
			for(Role role:user.getRoles()){
				if(dmsRolePermission.getRoleName().equalsIgnoreCase(role.getName())){
					if(dmsRolePermission.isCanAccess()){
						return folder;
					}
				}
			}
		}
		for (DMSRolePermission dmsGroupPermission : folder.getDmsGroupPermission()) {
			for(Group group:user.getGroups()){
				if(dmsGroupPermission.getName().equalsIgnoreCase(group.getName())){
					if(dmsGroupPermission.isCanAccess()){
						return folder;
					}
				}
			}
		}
		for (DMSRolePermission dmsDepartmentPermission : folder.getDmsDepartmentPermission()) {
			for(Group group:user.getGroups()){
				if(dmsDepartmentPermission.getName().equalsIgnoreCase(user.getDepartment().getName())){
					if(dmsDepartmentPermission.isCanAccess()){
						return folder;
					}
				}
			}
		}
		return null;
	}
	
	/**
     * {@inheritDoc}
     */
    public List<LabelValue> getRootFolderAsLabelValue(){
    	return folderDao.getRootFolderAsLabelValue();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getChildFoldersAsLabelValue(String parentFolderId) {
    	return folderDao.getChildFoldersAsLabelValue(parentFolderId);
    }
    
    /**
     * {@inheritDoc}
     */
    public void removechildFolders (String parentFolderId){
    	List<Folder> childs = folderDao.getChildFolders(parentFolderId);
    	if(childs !=null && childs.size() > 0){
	    	for(Folder child : childs){
	    		folderDao.remove(child);
	    	}
    	}
    	/*folderDao.removeDMSUserPermission(parentFolderId);
		folderDao.removeDMSRolePermission(parentFolderId);
		folderDao.removeDMSGroupPermission(parentFolderId);
    	folderDao.removechildFolders(parentFolderId);*/
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Folder> getChildFolders(String parentFolderId){
    	return folderDao.getChildFolders(parentFolderId);
    }
    
    /**
     * {@inheritDoc}
     */
    public DMSRolePermission saveDmsUserPermission(DMSRolePermission dmsUserPermission) {
		dmsUserPermission = folderDao.saveDmsUserPermission(dmsUserPermission);
		return dmsUserPermission;
	}
    
    /**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsUserPermission(){
		return folderDao.getDmsUserPermission();
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsRolePermission(){
		return folderDao.getDmsRolePermission();
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsGroupPermission(){
		return folderDao.getDmsGroupPermission();
	}
	
	/**
     * {@inheritDoc}
     */
    public DMSRolePermission saveDmsRolePermission(DMSRolePermission dmsRolePermission) {
		dmsRolePermission = folderDao.saveDmsRolePermission(dmsRolePermission);
		return dmsRolePermission;
	}
    
    /**
     * {@inheritDoc}
     */
    public DMSRolePermission saveDmsGroupPermission(DMSRolePermission dmsGroupPermission) {
		dmsGroupPermission = folderDao.saveDmsGroupPermission(dmsGroupPermission);
		return dmsGroupPermission;
	}
    
    /**
     * {@inheritDoc}
     */
	public DMSRolePermission getDmsUserPermission(String id){
		return folderDao.getDmsUserPermission(id);
	}
	
	/**
     * {@inheritDoc}
     */
	public DMSRolePermission getDmsRolePermission(String id){
		return folderDao.getDmsRolePermission(id);
	}
	
	/**
     * {@inheritDoc}
     */
	public DMSRolePermission getDmsGroupPermission(String id){
		return folderDao.getDmsGroupPermission(id);
	}
	
	
	 /**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsUserPermissionByRoleType(String folderId){
		return folderDao.getDmsUserPermissionByRoleType(folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsRolePermissionByFolder(String folderId){
		return folderDao.getDmsRolePermissionByFolder(folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsGroupPermissionByRoleType(String folderId){
		return folderDao.getDmsGroupPermissionByRoleType(folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsUserRolePermission(String userId, String folderId){
		return folderDao.getDmsUserRolePermission(userId, folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsRolePermission(String userId, String folderId){
		return folderDao.getDmsRolePermission(userId, folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsGroupRolePermission(String userId, String folderId){
		return folderDao.getDmsGroupRolePermission(userId, folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<DMSRolePermission> getDmsDepartmentRolePermission(String departmentId, String folderId){
		return folderDao.getDmsDepartmentRolePermission(departmentId, folderId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<Folder> getFolderByOwner(String owner){
		return folderDao.getFolderByOwner(owner);
	}
	
	/**
     * {@inheritDoc}
     */
	public void updateFolderOwner (String oldOwner, String newOwner){
		folderDao.updateFolderOwner(oldOwner, newOwner);
	}
	
	public boolean checkDublicateFolder(String folderName){
		List<Folder> folders = folderDao.getFolders(folderName);
		if(folders != null && folders.size() > 0){
			return true;
		}
		return false;
	}
	/*public boolean checkNameExists(String name) throws EazyBpmException {
		
		return folderDao.checkNameExists(name);
	}*/
	
	public List<DMSRolePermission> getDmsRolePermissionByFolderId(String folderId) {
		return folderDao.getDmsRolePermissionByFolder(folderId);
	}
	
	public List<DMSRolePermission> getDmsRolePermissionByDocumentId(String documentId) {
		return folderDao.getDmsRolePermissionByDocumentId(documentId);
	}
}