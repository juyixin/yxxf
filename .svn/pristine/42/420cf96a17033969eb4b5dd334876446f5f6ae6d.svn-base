package com.eazytec.bpm.admin.widget.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.widget.dao.WidgetDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Folder;
import com.eazytec.model.Layout;
import com.eazytec.model.QuickNavigation;
import com.eazytec.model.Widget;
import com.eazytec.model.UserWidget;
import com.eazytec.model.Classification;
import com.eazytec.model.WidgetDepartment;


@Repository("widgetDao")
public class WidgetDaoImpl extends GenericDaoHibernate<Widget, String> implements WidgetDao{

	public WidgetDaoImpl() {
		super(Widget.class);
	}
	/**
	 * {@inheritDoc}
	 */
	public Widget saveWidget(Widget widget) throws EazyBpmException{
		if(widget.getId()==null || widget.getId()=="" ){
			getSession().save(widget);
		}else{
			getSession().update(widget);
		}
	//	getSession().save(widget);
		if (log.isDebugEnabled()) {
			log.debug("widget id: " + widget.getId());
		}
		getSession().flush();
		return widget;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public UserWidget saveUserWidget(UserWidget userWidget) throws EazyBpmException{
		getSession().saveOrUpdate(userWidget);
		return  userWidget;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Widget> getAllWidget() throws EazyBpmException{
		Query qry = getSession().createQuery("from Widget widget where widget.isPublic=true order by widget.name asc");
        return qry.list();
		
	}
	
		
    public List<UserWidget> getUserSelectedWidget(String userName) throws EazyBpmException {
    	List<UserWidget> widgetIdList = (List<UserWidget>) getSession().createQuery("select userWidget from UserWidget as userWidget where userWidget.userName='"+userName+"'").list();
    	if(widgetIdList != null && !widgetIdList.isEmpty()){
    		return widgetIdList;
    	}else{
    		return null;
    	}
    	
    }
	
	/**
	 * {@inheritDoc}
	 */
	public List<Widget> getAllUserSelectedWidget(List<String> widgetIdList) throws EazyBpmException{
		List<Widget> widgetList = (List<Widget>) getSession().createQuery("select widget from Widget as widget where widget.id in (:list)").setParameterList("list", widgetIdList).list();
		if(widgetList != null && !widgetList.isEmpty()){
    		return widgetList;
    	}else{
    		return null;
    	}
		
	}
	
	public UserWidget getUserWidgetForId(String userName) throws EazyBpmException{
		Query qry = getSession().createQuery("from UserWidget userWidget where userWidget.userName='"+userName+"'");
		if(qry.list()!=null && !qry.list().isEmpty()){
			return (UserWidget) qry.list().get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Widget getWidgetForId(String id) throws EazyBpmException{
		Query qry = getSession().createQuery("from Widget widget where widget.id='"+id+"'");
        if(qry.list()!=null){
        	return (Widget) qry.list().get(0);
        }else{
        	return null;
        }
		
	}
	
	public List<Widget> getWidgetForNames(String name) throws EazyBpmException{
		Query qry = getSession().createQuery("from Widget widget where widget.id in "+name+"");
       if(qry.list()!=null){
        	return (List<Widget>) qry.list();
        }else{
        	return null;
        }
		
	}
	
	
//	public List<QuickNavigation> getQuickNavigationsByNames(List<String> names){
//		List<QuickNavigation> quickNavigations = (List<QuickNavigation>) getSession().createQuery("from QuickNavigation as quickNavigation where quickNavigation.id in (:list)").setParameterList("list", names).list();
       
	public List<Widget> getWidgetForIds(List<String> names) throws EazyBpmException{
		//List<Widget> widgets = (List<Widget>) getSession().createQuery("select widget from Widget as widget where widget.id in (:list) ORDER BY widget.id ").setParameterList("list", names).list();
		List<Widget> widgets = (List<Widget>) getSession().createQuery("select widget from Widget as widget where widget.id in (:list) ").setParameterList("list", names).list();
		
		 if (widgets.isEmpty()) {
	            return null;
	        } else {
	            return widgets;
	        }
		
	}
	
	public Widget getActiveWidget() throws EazyBpmException{
		Query qry = getSession().createQuery("from Widget widget where widget.status=1");
        if(qry.list()!=null){
        	return (Widget) qry.list().get(0);
        }else{
        	return null;
        }
	}
        public ArrayList<Widget> getWidgetGridData() {
        	 ArrayList<Widget> widget= null;
        	return  widget ;
        }
        
        public void removeWidget(String Id) {
        	Query qry = getSession().createQuery("delete from Widget widget where widget.id =  '"+Id+"'");
        	int result = qry.executeUpdate();
        	qry=getSession().createQuery("delete from WidgetDepartment widget where widget.widgetId =  '"+Id+"'");
        	result = qry.executeUpdate();
        }
        /**
    	 * {@inheritDoc}
    	 */
        public List<Classification> getAllClassification() throws EazyBpmException{
        	List<Classification> classificationList = (List<Classification>) getSession().createQuery("select c from Classification as c order by c.orderNo").list();
        	if(classificationList != null && !classificationList.isEmpty()){
        		return classificationList;
        	}else{
        		return null;
        	}
        }
        
        public WidgetDepartment saveWidgetDepartment(WidgetDepartment widgetDepartment) throws EazyBpmException{
        	if(widgetDepartment.getId()==null || widgetDepartment.getId()=="" ){
    			getSession().save(widgetDepartment);
    		}else{
    			getSession().update(widgetDepartment);
    		}
    	//	getSession().save(widget);
    		if (log.isDebugEnabled()) {
    			log.debug("widget Department id: " + widgetDepartment.getId());
    		}
    		getSession().flush();
    		return widgetDepartment;
        }
        
        public List<WidgetDepartment> getWidgetDepartmentList(String widgetId,List<String> departments) throws EazyBpmException{
        	List<WidgetDepartment> widgetDepartments = (List<WidgetDepartment>) getSession().createQuery("select widget from WidgetDepartment as widget where widget.widgetId='"+widgetId+"' and widget.departmentId in (:list)").setParameterList("list", departments).list();
    		
   		 if (widgetDepartments.isEmpty()) {
   	            return null;
   	        } else {
   	            return widgetDepartments;
   	        }
   		}
        
        public List<Widget> getPublicWidgetByDepartment(List<String> userDeps)throws EazyBpmException{
        	Query qry = getSession().createQuery("from Widget widget where widget.isPublic=true or widget.id in (select widgetDepartment.widgetId from WidgetDepartment as widgetDepartment where widgetDepartment.departmentId in (:list)) order by widget.name asc").setParameterList("list", userDeps);
            return qry.list();
        }
}