package com.eazytec.bpm.admin.module.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.module.dao.ModuleDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Module;
import com.eazytec.model.ModuleRolePrivilege;

/**
 *  This class interacts with Hibernate session to save/delete, retrieve Module
 * objects.
 * @author revathi
 *
 */
@Repository("moduleDao")
public class ModuleDaoImpl extends GenericDaoHibernate<Module, String> implements ModuleDao{
	
	public String dataBaseSchema;

	public String getDataBaseSchema() {
		return dataBaseSchema;
	}

	public void setDataBaseSchema(String dataBaseSchema) {
		this.dataBaseSchema = dataBaseSchema;
	}

	public ModuleDaoImpl() {
		super(Module.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Module saveModule(Module module) throws EazyBpmException{
		getSession().saveOrUpdate(module);
		if (log.isDebugEnabled()) {
			log.debug("module id: " + module.getId());
		}
		getSession().flush();
		return module;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Module save(Module module) throws EazyBpmException {
		return this.saveModule(module);
	}
	
	/**
	 * {@inheritDoc}
	 */
	 public List<Module> getAllModules() throws EazyBpmException {
	        Query qry = getSession().createQuery("from Module module order by module.moduleOrder,module.name asc");
	        return qry.list();
	  }
	 
	 /**
	 * {@inheritDoc}
	 */
	 public List<Module> getAllUserModules(String role_types,String privilegeType) throws EazyBpmException {
		 String userModules="select module from Module as module where module.id in (select modulePrivilege.module from ModuleRolePrivilege as modulePrivilege " +
		 		"where modulePrivilege.privilegeType='"+privilegeType+"' and modulePrivilege.roleName in "+role_types+") ";
		 if(privilegeType.equals("view")){
			 userModules=userModules+" or module.isPublic=true ";
		 }
		 userModules=userModules+" order by module.moduleOrder,module.name asc";
		    Query qry = getSession().createQuery(userModules);
	        return qry.list();
	  }
	 
	 /**
	  * {@inheritDoc}
	  */
	 public List<Classification> getAllClassification() throws EazyBpmException{
		 log.debug("Retrieving all process classifications...");
		 Session session = getSessionFactory().getCurrentSession();
	        return session.createCriteria(Classification.class).list();
	 }
	 
	/* public List<Module> getAllModulesForRoles(Set<Role>roles) throws EazyBpmException{	
		 List<Role> roleList = new ArrayList<Role>(roles);
		 List<Module> modules = getSession().createQuery("select module from Module as module join module.roles as role where role in (:list)").setParameterList("list", roleList).list();
	       if (modules.isEmpty()) {
	           return null;
	       } else {
	           return modules;
	       }
		}*/
	 
	/**
	 * {@inheritDoc}
	 */
	 public List<Classification> getClassificationByIds(List<String> ids) throws EazyBpmException{
	    	log.info("Getting classification object of : "+ids);
	    	List<Classification> classifications = getSession().createQuery("from Classification as classification where classification.id in (:list)").setParameterList("list", ids).list();
	        if (classifications.isEmpty()) {
	            return null;
	        } else {
	            return classifications;
	        }
	    }
	 
	 /**
	  * {@inheritDoc}
	  */
	 /*public List<Module> getAllParentModule() throws EazyBpmException{
		 Query qry = getSession().createQuery("from Module module where module.isParent=1 order by upper(module.moduleOrder) asc");
	     return qry.list();
			
	 }*/
	 
	 /**
	  * {@inheritDoc}
	  */
	/* public List<Module> getAllChildModule() throws EazyBpmException{
		 Query qry = getSession().createQuery("from Module module where module.isParent=0 order by upper(module.moduleOrder) asc");
	     return qry.list();
			
	 }*/
	 
	 /**
	  * {@inheritDoc}
	  */
	 public List<Module> getModulesByIds(List<String> ids)throws EazyBpmException {
	    	log.info("Getting departments object of : "+ids);
	    	List<Module> modules = getSession().createQuery("from Module as module where module.id in (:list)").setParameterList("list", ids).list();
	    	getSession().flush();
	        if (modules.isEmpty()) {
	            return null;
	        } else {
	            return modules;
	        }
	    }
	 
	 public List<Module> getModulesByName(String name)throws EazyBpmException {
			List<Module> modules = getSession().createCriteria(Module.class)
			.add(Restrictions.eq("name", name)).list();
	 		getSession().flush();
	 		return  modules;
	 		/*
			 Query qry = getSession().createQuery("from Module as module where module.name='"+name+"'");
			 getSession().flush();
		     return qry.list();*/
	    }
	 /**
	  * {@inheritDoc}
	  */
	/* public List<LabelValue> getParentModuleAsLabelValue() throws BpmException {
	    	List<LabelValue> moduleList = (List<LabelValue>) getSession()
					.createQuery(
							"select new com.eazytec.model.LabelValue(module.name as name,module.id as id) from Module as module where module.isParent=1")
					.list();

			return moduleList;
	    }*/
	 /**
	  * {@inheritDoc}
	  */
	 public LabelValue getModuleAsLabelValueForId(String moduleId) throws BpmException {
	    	List<LabelValue> moduleList = (List<LabelValue>) getSession()
					.createQuery(
							"select new com.eazytec.model.LabelValue(module.name as name,module.id as id) from Module as module where module.id='"+moduleId+"'")
					.list();
	         if (moduleList.isEmpty()) {
	             return null;
	         } else {
	             return (LabelValue) moduleList.get(0);
	         }
	    }
	 
	 public List<Module> getBetweenModuleList(int fromOrder,int toOrder)throws BpmException{
		 Query qry = getSession().createQuery("from Module module where module.moduleOrder >="+fromOrder+" and module.moduleOrder <= "+toOrder+" order by module.moduleOrder,module.name asc");
	        return qry.list();
	 }
	 
	 public ModuleRolePrivilege saveModuleRolePrivilege(ModuleRolePrivilege modulePrivileges)throws EazyBpmException{
		 getSession().saveOrUpdate(modulePrivileges);
			if (log.isDebugEnabled()) {
				log.debug("module id: " + modulePrivileges.getId());
			}
			getSession().flush();
			return modulePrivileges;
	 }
	 
	 public void removeModuleRolePrivilege(String moduleId)throws BpmException{
		 Query query = getSession().createQuery("DELETE FROM ModuleRolePrivilege WHERE module='"+moduleId+"'");
			int result = query.executeUpdate();
	 }
	 
	 public List<String> getListViewModules(String listViewId)throws EazyBpmException{
		 String moduleIds;
		 if(dataBaseSchema.equalsIgnoreCase("oracle")) {
			 moduleIds="select module_id from ETEC_MODULE_LIST_VIEW where ETEC_MODULE_LIST_VIEW.list_view_id='"+listViewId+"'"; 
		 } else {
			 moduleIds="select list_view.module_id from ETEC_MODULE_LIST_VIEW as list_view where list_view.list_view_id='"+listViewId+"'";
		 }
		 Query query = getSession().createSQLQuery(moduleIds);
			getSession().flush();
			return (List<String>) query.list();
	 }
}
