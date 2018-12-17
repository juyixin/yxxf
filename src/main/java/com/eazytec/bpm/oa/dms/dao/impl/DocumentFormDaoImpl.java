package com.eazytec.bpm.oa.dms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.dao.DocumentFormDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSGroupPermission;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DMSUserPermission;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.FileImportForm;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
@Repository("documentFormDao")
public class DocumentFormDaoImpl extends GenericDaoHibernate<DocumentForm, String> implements DocumentFormDao {

	public DocumentFormDaoImpl() {
		super(DocumentForm.class);
	}

	@Override
	public void deleteDocumentForm(DocumentForm documentForm) {
		getSession().delete(documentForm);
	}

	@Override
	public DocumentForm saveOrUpdateDocumentForm(DocumentForm documentForm) {
		 getSession().saveOrUpdate(documentForm);
		 return documentForm;
	}
	
	@Override
	public FileImportForm saveOrUpdateImportFile(FileImportForm  importFileForm) {
		log.info("=============ImportFileForm=== in Save========"+importFileForm.toString());
		getSession().saveOrUpdate(importFileForm);
		getSession().flush();
		return importFileForm;
	}
	@Override
	public String searchImportFileById(String fileId){
		String path = (String) getSession().createQuery("select fileImportFileForm.path from FileImportForm fileImportFileForm where fileImportFileForm.id = '"+ fileId +"'").list().get(0);
		return path;
	}
	@Override
	public FileImportForm getImportFileById(String fileId){
		List<FileImportForm> importFileForms = getSession().createQuery("from FileImportForm fileImportFileForm where fileImportFileForm.id = '"+ fileId +"'").list();
		if(importFileForms != null && importFileForms.size() >0){
			return importFileForms.get(0);
		} else {
			return null;
		}
	}
	@Override
	public boolean deleteImportFile(FileImportForm fileImportForm){
		log.info(fileImportForm.getId());
		Query query=getSession().createQuery("delete from FileImportForm as fileImportForm where fileImportForm.id = '"+fileImportForm.getId()+"'");
        query.executeUpdate();
		getSession().flush();
    	return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentForm> getAllDocuments(){
		List<DocumentForm> documentForms = (List<DocumentForm>)getSession().createQuery("select new com.eazytec.model.DocumentForm(documentForm.id as id,documentForm.name as name,documentForm.createdBy as createdBy,documentForm.updatedBy as updatedBy,documentForm.createdTime as createdTime) from DocumentForm as documentForm").list();
		return documentForms;
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentForm> getAllDocumentsByLoggedInUser(){
		List<DocumentForm> documentForms = (List<DocumentForm>)getSession().createQuery("select new com.eazytec.model.DocumentForm(documentForm.id as id,documentForm.name as name,documentForm.createdBy as createdBy,documentForm.updatedBy as updatedBy,documentForm.createdTime as createdTime) from DocumentForm as documentForm").list();
		return documentForms;
	}

	public DMSRolePermission saveOrUpdateDmsUserPermission(DMSRolePermission dmsUserPermission) {
		getSession().saveOrUpdate(dmsUserPermission);
		getSession().flush();
		return dmsUserPermission;
	}
	
	public DMSRolePermission saveOrUpdateDmsRolePermission(DMSRolePermission dmsRolePermission) {
		getSession().saveOrUpdate(dmsRolePermission);
		getSession().flush();
		return dmsRolePermission;
	}
	
	public DMSRolePermission saveOrUpdateDmsGroupPermission(DMSRolePermission dmsGroupPermission) {
		getSession().saveOrUpdate(dmsGroupPermission);
		getSession().flush();
		return dmsGroupPermission;
	}
	public DMSRolePermission saveOrUpdateDmsDepartmentPermission(DMSRolePermission dmsdepartmentPermission) {
		getSession().saveOrUpdate(dmsdepartmentPermission);
		getSession().flush();
		return dmsdepartmentPermission;
	}
	@SuppressWarnings("unchecked")
	public List<DMSRolePermission> getDMSDepartmentPermissionByDocumentForm(String departmentId, String documentFormId){
		return (List<DMSRolePermission>)getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.name='"+departmentId+"' and dmsRolePermission.documentForm.id='"+documentFormId+"') AND (dmsRolePermission.name='"+departmentId+"' and dmsRolePermission.roleType='DEPARTMENT')").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DMSRolePermission> getDMSUserPermissionByDocumentForm(String userid,String documentFormId){
		return (List<DMSRolePermission>)getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.name='"+userid+"' and dmsRolePermission.documentForm.id='"+documentFormId+"')  AND (dmsRolePermission.name='"+userid+"'  and dmsRolePermission.roleType='USER')").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DMSRolePermission> getDMSRolePermissionByDocumentForm(String roleNames,String documentFormId){
		return (List<DMSRolePermission>)getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.roleName in ("+roleNames+") and dmsRolePermission.documentForm.id='"+documentFormId+"') AND (dmsRolePermission.documentForm.id='"+documentFormId+"'  and dmsRolePermission.roleType='ROLE')").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DMSRolePermission> getDMSGroupPermissionByDocumentForm(String groupNames,String documentFormId){
		return (List<DMSRolePermission>)getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where (dmsRolePermission.name in ("+groupNames+") and dmsRolePermission.documentForm.id='"+documentFormId+"') AND (dmsRolePermission.documentForm.id='"+documentFormId+"' and dmsRolePermission.roleType='GROUP')").list();
	}

	 /**
     * {@inheritDoc}
     */
    public DMSRolePermission getDMSUserPermission(DocumentForm documentForm, String userName) {
    	List<DMSRolePermission> userPermissions = getSession().createCriteria(DMSRolePermission.class)
    	.add(Restrictions.eq( "documentForm", documentForm))
    	.add(Restrictions.eq("name", userName))
    	.add(Restrictions.eq("roleType","USER")).list();
		if (userPermissions.isEmpty()) {
			return null;
		} else {
			return userPermissions.get(0);
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public DMSRolePermission getDMSRolePermission(DocumentForm documentForm, String roleName) {
    	List<DMSRolePermission> rolePermissions = getSession().createCriteria(DMSRolePermission.class)
    	.add(Restrictions.eq( "documentForm", documentForm))
    	.add(Restrictions.eq("roleName", roleName))
    	.add(Restrictions.eq("roleType","ROLE")).list();
		if (rolePermissions.isEmpty()) {
			return null;
		} else {
			return rolePermissions.get(0);
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public DMSRolePermission getDMSGroupPermission(DocumentForm documentForm, String groupName) {
    	List<DMSRolePermission> groupPermissions = getSession().createCriteria(DMSRolePermission.class)
    	.add(Restrictions.eq( "documentForm", documentForm))
    	.add(Restrictions.eq("name", groupName))
    	.add(Restrictions.eq("roleType","GROUP")).list();
		if (groupPermissions.isEmpty()) {
			return null;
		} else {
			return groupPermissions.get(0);
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public DMSRolePermission getDMSDepartmentPermission(DocumentForm documentForm, String departmentName) {
    	List<DMSRolePermission> groupPermissions = getSession().createCriteria(DMSRolePermission.class)
    	.add(Restrictions.eq( "documentForm", documentForm))
    	.add(Restrictions.eq("name", departmentName))
    	.add(Restrictions.eq("roleType","DEPARTMENT")).list();
		if (groupPermissions.isEmpty()) {
			return null;
		} else {
			return groupPermissions.get(0);
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSUserPermission (DocumentForm documentForm) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as rolePermission where rolePermission.documentForm.id = '"+documentForm.getId()+"' and rolePermission.roleType='USER'");
        query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSRolePermission (DocumentForm documentForm) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as rolePermission where rolePermission.documentForm.id = '"+documentForm.getId()+"' and rolePermission.roleType='ROLE'");
        query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSGroupPermission (DocumentForm documentForm) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as rolePermission where rolePermission.documentForm.id = '"+documentForm.getId()+"' and rolePermission.roleType='GROUP'");
        query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeDMSDepartmentPermission (DocumentForm documentForm) {
    	Query query = getSession().createQuery("delete from DMSRolePermission as rolePermission where rolePermission.documentForm.id = '"+documentForm.getId()+"' and rolePermission.roleType='DEPARTMENT' ");
        query.executeUpdate();
    }
    
    public Document getDocumentById(String id){
		return (Document) getSession().get(Document.class, id);
	}

    /**
     * {@inheritDoc}
     */
    public List<DocumentForm> getDocumentsByFolderId (String folderId) {
    	List<DocumentForm> documentForms = (List<DocumentForm>)getSession().createQuery("select new com.eazytec.model.DocumentForm(documentForm.id as id,documentForm.name as name,documentForm.createdBy as createdBy,documentForm.updatedBy as updatedBy,documentForm.createdTime as createdTime) from DocumentForm as documentForm where documentForm.folder.id='"+folderId+"'").list();
        if(documentForms.isEmpty()){
        	return null;
        }else{
        	return documentForms;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<DMSRolePermission> getDmsDepartmentPermissionByDocument(String documentId){
 	  log.debug("Retrieving all DMS Document Department Permission ...");
 	  List<DMSRolePermission> dmsUserPermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.documentForm.id = '"+documentId+"' and dmsRolePermission.roleType='DEPARTMENT'").list();
       return dmsUserPermissions;
    } 
   
    
   /**
     * {@inheritDoc}
     */
    public List<DMSRolePermission> getDmsUserPermissionByDocument(String documentId){
 	  log.debug("Retrieving all DMS Document User Permission ...");
 	  List<DMSRolePermission> dmsUserPermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.documentForm.id = '"+documentId+"' and dmsRolePermission.roleType='USER'").list();
       return dmsUserPermissions;
    } 
   
    /**
     * {@inheritDoc}
     */
    public List<DMSRolePermission> getDmsRolePermissionByDocument(String documentId){
 	  log.debug("Retrieving all DMS Document Role Permission ...");
 	  List<DMSRolePermission> dmsRolePermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.documentForm.id = '"+documentId+"'").list();
       return dmsRolePermissions;
    }
   
    /**
     * {@inheritDoc}
     */
    public List<DMSRolePermission> getDmsGroupPermissionByDocument(String documentId){
 	  log.debug("Retrieving all DMS Document Group Permission ...");
 	  List<DMSRolePermission> dmsGroupPermissions = (List<DMSRolePermission>) getSession().createQuery("select dmsRolePermission from DMSRolePermission as dmsRolePermission where dmsRolePermission.documentForm.id = '"+documentId+"' and dmsRolePermission.roleType='GROUP'").list();
       return dmsGroupPermissions;
    }
    public void deleteGroupPermission(List<String> ids){
	    Query query = getSession().createQuery("delete DMSRolePermission as permission  where permission.id in (:list)").setParameterList("list", ids);
	    query.executeUpdate();
    }
    
    public void deleteUserPermission(List<String> ids){
    	Query query = getSession().createQuery("delete DMSRolePermission as permission  where permission.id in (:list)").setParameterList("list", ids);
	    query.executeUpdate();
    }
    
    public void deleteRolePermission(List<String> ids){
    	Query query = getSession().createQuery("delete DMSRolePermission as permission  where permission.id in (:list)").setParameterList("list", ids);
	    query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteDocumentById(String documentId) {
    	Query query = getSession().createQuery("delete from Document as document where document.id = '"+documentId+"'");
        query.executeUpdate();
    }
    
    @SuppressWarnings("unchecked")
	public boolean checkDocumentName(String documentFormName,String documentFormId,String folderId) {
    	boolean isDuplicate = false ;
        List<DocumentForm> documentForms = getSession().createQuery("select documentForm from DocumentForm as documentForm where documentForm.name='"+documentFormName+"' and documentForm.folder='"+folderId+"'").list();
		if (documentForms.isEmpty()) {
			isDuplicate = false;
		} else {
			DocumentForm documentForm= documentForms.get(0);
			if (documentFormId != null && !(documentFormId.isEmpty())
					&& documentFormId.equals(documentForm.getId())) {
				isDuplicate = false;
			} else {
				isDuplicate = true;
			}
		}
        return isDuplicate;
    }

    public List<FileImportForm> getAllFiles()throws EazyBpmException{
    	List<FileImportForm> documentForms = (List<FileImportForm>)getSession().createQuery("select new com.eazytec.model.FileImportForm(fileImportForm.id as id,fileImportForm.name as name,fileImportForm.resourcePath as path) from FileImportForm as fileImportForm").list();
    	return documentForms; 
    	
    	
	    	/*List<LabelValue> listViews = getSession().createQuery("select new com.eazytec.model.LabelValue(fileImportForm.name  as name,fileImportForm.id as id) from FileImportForm as fileImportForm").list();
	        if (listViews.isEmpty()) {
	            return null;
	        } else {
	            return listViews;
	        }*/
    }
	public int checkIfPermissionSet(String documentFormId) {
		int permissionCount = ((Long)getSession().createQuery("select count(*) FROM DMSRolePermission as dmsRolePermission where dmsRolePermission.documentForm.id='"+documentFormId+"'").uniqueResult()).intValue();
	       return permissionCount;
	}


}
