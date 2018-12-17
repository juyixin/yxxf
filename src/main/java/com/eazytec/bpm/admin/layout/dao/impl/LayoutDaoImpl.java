package com.eazytec.bpm.admin.layout.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.layout.dao.LayoutDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Layout;
import com.eazytec.model.QuickNavigation;
import com.eazytec.model.Role;
import com.eazytec.model.User;


@Repository("layoutDao")
public class LayoutDaoImpl extends GenericDaoHibernate<Layout, String> implements LayoutDao{

	public LayoutDaoImpl() {
		super(Layout.class);
	}
	/**
	 * {@inheritDoc}
	 */
	public Layout saveLayout(Layout layout) throws EazyBpmException{
		getSession().saveOrUpdate(layout);
		if (log.isDebugEnabled()) {
			log.debug("layout id: " + layout.getId());
		}
		getSession().flush();
		return layout;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Layout> getAllLayout() throws EazyBpmException{
		Query qry = getSession().createQuery("from Layout layout order by layout.name asc");
        return qry.list();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Layout getLayoutForId(String id) throws EazyBpmException{
		Query qry = getSession().createQuery("from Layout layout where layout.id='"+id+"'");
        if(qry.list()!=null){
        	return (Layout) qry.list().get(0);
        }else{
        	return null;
        }
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Layout getActiveLayout() throws EazyBpmException{
		Query qry = getSession().createQuery("from Layout layout where layout.status=1");
        if(qry.list()!=null){
        	return (Layout) qry.list().get(0);
        }else{
        	return null;
        }
		
	}
	
	/**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public Layout getLayoutByAssignedTo(String assignedTo) throws EazyBpmException {
    	List<Layout> layouts = getSession().createCriteria(Layout.class).add(Restrictions.and(
    	        Restrictions.eq( "assignedTo", assignedTo ),
    	        Restrictions.eq( "status", true ))).list();
        if (layouts == null || layouts.isEmpty()) {
            return null;
        } else {
            return layouts.get(0);
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	public QuickNavigation getQuickNavForId(String id) throws EazyBpmException{
		Query qry = getSession().createQuery("from QuickNavigation quickNav where quickNav.id='"+id+"'");
        if(qry.list()!=null){
        	return (QuickNavigation) qry.list().get(0);
        }else{
        	return null;
        }
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<QuickNavigation> getAllQuickNavigation() throws EazyBpmException{
		Query qry = getSession().createQuery("from QuickNavigation quickNav order by quickNav.name asc");
        return qry.list();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public QuickNavigation saveQuickNavigation(QuickNavigation quickNav) throws EazyBpmException{
		if(quickNav.getId()==null || quickNav.getId()=="" ){
			getSession().save(quickNav);
		}else{
			getSession().update(quickNav);
		}
		
		//getSession().save(quickNav);
		if (log.isDebugEnabled()) {
			log.debug("module id: " + quickNav.getId());
		}
		getSession().flush();
		return quickNav;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<QuickNavigation> getAllActiveQuickNavigation() throws EazyBpmException{
		Query qry = getSession().createQuery("from QuickNavigation quickNav where quickNav.status=1 order by quickNav.name asc");
		 return qry.list();
		
	}
	
	public int getnoOfWidget() throws EazyBpmException{
	//	getSession().
		Query qry = getSession().createQuery("from Layout no_of_widget");
        return qry.hashCode();
		
	}
	
	public List<QuickNavigation> getQuickNavigationsByNames(List<String> names){
		List<QuickNavigation> quickNavigations = (List<QuickNavigation>) getSession().createQuery("from QuickNavigation as quickNavigation where quickNavigation.id in (:list)").setParameterList("list", names).list();
        if (quickNavigations.isEmpty()) {
            return null;
        } else {
            return quickNavigations;
        }
	}
	public void removeQuickNavigation(String id) {
	//	QuickNavigation quickNavigation = getQuickNavForId(id);
		Query qry = getSession().createQuery("delete from QuickNavigation quickNavigation where quickNavigation.id =  '"+id+"'");
		
		int result = qry.executeUpdate();
		

		//return result != 0  ? true : false;
	}
	 
	/**
     * {@inheritDoc}
     */
    public void deleteLayoutsByAssignedTos(List<String> asssignedTos) {
    	Query query = getSession().createQuery("delete from Layout as layout where layout.assignedTo in (:list)").setParameterList("list", asssignedTos);
        query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Layout> getLayoutsByAssignedTos(List<String> assignedTos) throws EazyBpmException {
    	List<Layout> layouts = (List<Layout>) getSession().createQuery("select layout from Layout as layout where layout.assignedTo in (:list)").setParameterList("list", assignedTos).list();
    	if (layouts != null && !layouts.isEmpty()) {
        	return layouts;
        } else {
        	 return null;
            
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getAllRelativeDepartments(String departmentId) throws Exception {
    	List<String> departmentChildNodes = getSession().createSQLQuery("SELECT getDepartmentChilds(:id,:isChild)").setParameter("isChild", true)
				 .setParameter("id", departmentId).list();
    	if(departmentChildNodes != null && !departmentChildNodes.isEmpty()){
    		return departmentChildNodes;
    	} else {
    		return null;
    	}

    }

}
