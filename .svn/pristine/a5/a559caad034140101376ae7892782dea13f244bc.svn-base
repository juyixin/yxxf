package com.eazytec.bpm.oa.dms.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.dao.FolderDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSGroupPermission;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DMSUserPermission;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.Folder;
import com.eazytec.model.FolderPermission;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Report;
/**
 * 
 * @author vinoth
 *
 */
@Repository("folderDao")
public class FolderDaoImpl extends GenericDaoHibernate<Folder, String> implements FolderDao {

	public FolderDaoImpl() {
		super(Folder.class);
	}

	@Override
	public void deleteFoler(Folder folder) {
		getSession().delete(folder);		
	}


	@Override
	public Folder saveOrUpdateFoler(Folder folder) {
		if(StringUtil.isEmptyString(folder.getId())){
			getSession().save(folder);
		}else{
			getSession().update(folder);
		}
		getSession().flush();
		return folder;
	}
	
	public FolderPermission saveOrUpdateFolerPermission(FolderPermission folderPermission) {
		
		if(StringUtil.isEmptyString(folderPermission.getId())){
			getSession().save(folderPermission);
		}else{
			getSession().update(folderPermission);
		}
		getSession().flush();
		return folderPermission;
	}
	
	public void deleteFolderPermission(String id){
		FolderPermission folderPermission= (FolderPermission) getSession().get(FolderPermission.class, id);
		getSession().delete(folderPermission);
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Folder> getAllFolders() {
		List<Folder> folders = (List<Folder>)getSession().createQuery("select new com.eazytec.model.Folder(folder.id as id,folder.name as name,folder.owner as owner) from Folder as folder").list();
		return folders;
	}
	
	@SuppressWarnings("unchecked")
	public List<Folder> getAllFoldersByUser(String userId,String roles){	
		List<Folder> folders = (List<Folder>) getSession().createQuery("select new com.eazytec.model.Folder(folder.id as id,folder.name as name,folder.owner as owner) from Folder as folder join folder.folderPermissions as folderPermission where folder.owner = '"+userId+"' or folderPermission.roleName in("+roles+")").list();
		Set<Folder> distinctFolder= new HashSet<Folder>(folders);
		folders.clear();
		folders.addAll(distinctFolder);
	    return folders;
	}
	
	/**
     * {@inheritDoc}
     */
    public void removeDMSUserPermission (String folderId) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as userPermission where userPermission.folder.id = '"+folderId+"' and userPermission.roleType='USER'");
        query.executeUpdate();
        getSession().flush();
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSRolePermission (String folderId) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as rolePermission where rolePermission.folder.id = '"+folderId+"' and rolePermission.roleType='ROLE'");
        query.executeUpdate();
        getSession().flush();
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSGroupPermission (String folderId) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as groupPermission where groupPermission.folder.id = '"+folderId+"' and groupPermission.roleType='GROUP'");
        query.executeUpdate();
        getSession().flush();
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSDepartmentPermission (String folderId) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as grolePermission where grolePermission.folder.id = '"+folderId+"' and grolePermission.roleType='DEPARTMENT'");
        query.executeUpdate();
        getSession().flush();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getRootFolderAsLabelValue(){
    	List<LabelValue> folderList = (List<LabelValue>) getSession()
				.createQuery("select new com.eazytec.model.LabelValue(folder.name as name,folder.id as id) from Folder as folder where folder.name = 'root'").list();
		return folderList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getChildFoldersAsLabelValue(String parentFolderId) {
    	List<LabelValue> folderList = (List<LabelValue>) getSession()
				.createQuery("select new com.eazytec.model.LabelValue(folder.name as name,folder.id as id) from Folder as folder where folder.parentFolder = '"+parentFolderId+"'")
				.list();

		return folderList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Folder> getChildFolders(String parentFolderId) {
    	List<Folder> folderList = (List<Folder>) getSession()
				.createQuery("select folder from Folder as folder where folder.parentFolder = '"+parentFolderId+"'")
				.list();

		return folderList;
    }
    
    /**
     * {@inheritDoc}
     */
    public void removechildFolders (String parentFolderId) {
    	Query query = getSession().createQuery("delete from Folder as folder where folder.parentFolder = '"+parentFolderId+"'");
        query.executeUpdate();
        getSession().flush();
   }
    
    /**
     * {@inheritDoc}
     */
   public DMSRolePermission saveDmsUserPermission(DMSRolePermission dmsUserPermisison){
	   getSession().saveOrUpdate(dmsUserPermisison);
	   return dmsUserPermisison;
   }
   
   /**
    * {@inheritDoc}
    */
   public DMSRolePermission saveDmsGroupPermission(DMSRolePermission dmsGroupPermisison){
	   getSession().saveOrUpdate(dmsGroupPermisison);
	   return dmsGroupPermisison;
   }
 
   /**
    * {@inheritDoc}
    */
   public DMSRolePermission saveDmsRolePermission(DMSRolePermission dmsRolePermisison){
	   getSession().saveOrUpdate(dmsRolePermisison);
	   return dmsRolePermisison;
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsUserPermission(){
	  log.debug("Retrieving all DMS User Permission ...");
      return getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("roleType", "USER")).list();
   }
  
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsRolePermission(){
	  log.debug("Retrieving all DMS Role Permission ...");
     return getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("roleType", "ROLE")).list();
   }
  
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsGroupPermission(){
	  log.debug("Retrieving all DMS Group Permission ...");
     return getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("roleType", "GROUP")).list();
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsDepartmentPermission(){
	  log.debug("Retrieving all DMS Department Permission ...");
     return getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("roleType", "DEPARTMENT")).list();
   }
   
   
   /**
    * {@inheritDoc}
    */
   public DMSRolePermission getDmsUserPermission(String id){
	  log.debug("Retrieving all DMS User Permission ...");
	  List<DMSRolePermission> permissions = getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("id", id)).add(Restrictions.eq("roleType","USER")).list();
	  if(permissions.size() >0 ){
		  return permissions.get(0);
	  }else{
		  return null;
	  }
   }
  
   /**
    * {@inheritDoc}
    */
   public DMSRolePermission getDmsRolePermission(String id){
	  log.debug("Retrieving all DMS Role Permission ...");
	  List<DMSRolePermission> permissions = getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("id", id)).list();
	  if(permissions.size() >0 ){
		  return permissions.get(0);
	  }else{
		  return null;
	  }
   }
  
   /**
    * {@inheritDoc}
    */
   public DMSRolePermission getDmsGroupPermission(String id){
	  log.debug("Retrieving all DMS Group Permission ...");
	  List<DMSRolePermission> permissions = getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("id", id)).add(Restrictions.eq("roleType","GROUP")).list();
	  if(permissions.size() >0 ){
		  return permissions.get(0);
	  }else{
		  return null;
	  }
   }
   
   /**
    * {@inheritDoc}
    */
   public DMSRolePermission getDmsDepartmentPermission(String id){
	  log.debug("Retrieving all DMS Group Permission ...");
	  List<DMSRolePermission> permissions = getSession().createCriteria(DMSRolePermission.class).add(Restrictions.eq("id", id)).add(Restrictions.eq("roleType","DEPARTMENT")).list();
	  if(permissions.size() >0 ){
		  return permissions.get(0);
	  }else{
		  return null;
	  }
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsUserPermissionByRoleType(String folderId){
	  log.debug("Retrieving all DMS Folder User Permission ...");
	  List<DMSRolePermission> dmsUserPermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.folder.id = '"+folderId+"' AND  dmsRolePermission.roleType = 'USER'").list();
      return dmsUserPermissions;
   }
  
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsRolePermissionByFolder(String folderId){
	  log.debug("Retrieving all DMS Folder Role Permission ...");
	  List<DMSRolePermission> dmsRolePermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.folder.id = '"+folderId+"'").list();
      return dmsRolePermissions;
   }
  
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsGroupPermissionByRoleType(String folderId){
	  log.debug("Retrieving all DMS Folder Group Permission ...");
	  List<DMSRolePermission> dmsGroupPermission = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.folder.id = '"+folderId+"' AND dmsRolePermission.roleType = 'GROUP'").list();
      return dmsGroupPermission;
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsDepartmentPermissionByRoleType(String folderId){
	  log.debug("Retrieving all DMS Folder Group Permission ...");
	  List<DMSRolePermission> dmsGroupPermission = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.folder.id = '"+folderId+"' AND dmsRolePermission.roleType = 'DEPARTMENT'").list();
      return dmsGroupPermission;
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsUserRolePermission(String userId, String folderId){
	  log.debug("Retrieving all DMS Folder User Role Permission ...");
	  List<DMSRolePermission> dmsUserRolePermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.name = '"+userId+"') AND (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.roleType = 'USER')").list();
      return dmsUserRolePermissions;
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsRolePermission(String roleId, String folderId){
	  log.debug("Retrieving all DMS Folder Role Permission ...");
	  List<DMSRolePermission> dmsRolePermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.roleName in ("+roleId+")) AND (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.roleType = 'ROLE')").list();
      return dmsRolePermissions;
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsGroupRolePermission(String groupId, String folderId){
	  log.debug("Retrieving all DMS Folder Group Permission ...");
	  List<DMSRolePermission> dmsGroupRolePermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.name in ("+groupId+")) AND (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.roleType = 'GROUP')").list();
      return dmsGroupRolePermissions;
   }
   
   /**
    * {@inheritDoc}
    */
   public List<DMSRolePermission> getDmsDepartmentRolePermission(String departmentId, String folderId){
	  log.debug("Retrieving all DMS Folder Department Permission ...");
	  List<DMSRolePermission> dmsGroupRolePermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.name ='"+departmentId+"') AND (dmsRolePermission.folder.id = '"+folderId+"' and dmsRolePermission.roleType = 'DEPARTMENT')").list();
      return dmsGroupRolePermissions;
   }
   
   @SuppressWarnings("unchecked")
   public List<Folder> getFolderByOwner(String owner){	
		List<Folder> folders = (List<Folder>) getSession().createQuery("select new com.eazytec.model.Folder(folder.id as id,folder.name as name,folder.owner as owner) from Folder as folder join folder.folderPermissions as folderPermission where folder.owner = '"+owner+"'").list();
	    return folders;
   }
   
   /**
    * {@inheritDoc}
    */
   public void updateFolderOwner (String oldOwner, String newOwner) {
   	Query query = getSession().createQuery("update from Folder as folder set folder.owner='"+newOwner+"' where folder.owner = '"+oldOwner+"'");
       query.executeUpdate();
   }
   
   /**
    * {@inheritDoc}
    */
   public List<Folder> getFolders(String folderName){	
		List<Folder> folders = (List<Folder>) getSession().createQuery("select folder from Folder as folder where folder.name = '"+folderName+"'").list();
	    return folders;
   }
   
   /*public boolean checkNameExists(String name) throws EazyBpmException {
	   Query qry = getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where lower(dmsRolePermission.name) = '"+name+"' ");
		//@SuppressWarnings("unchecked")
		List<DMSRolePermission> dmsRolePermissionNames = qry.list();

		return (dmsRolePermissionNames != null && !dmsRolePermissionNames.isEmpty()) ? true:false;
	   
   }*/
   	
   public List<DMSRolePermission> getDmsRolePermissionByDocumentId(String documentId) {
	   
	   Query query = getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where lower(dmsRolePermission.documentForm) = '"+documentId+"' ");
	   List<DMSRolePermission> documentIds = query.list();
	   return documentIds;  
	   
   }
   
   public List<DMSRolePermission> getDmsRolePermissionByFolderId(String folderId) {
	   Query query = getSession().createQuery("select dmsrolepermission from DMSRolePermission as dmsrolepermission where lower(dmsrolepermission.folder) = '"+folderId+"' ");
	   List<DMSRolePermission> folderIds = query.list();
	   return folderIds;
   }
}
