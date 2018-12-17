package com.eazytec.bpm.admin.widget.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.layout.service.LayoutService;
import com.eazytec.bpm.admin.widget.dao.WidgetDao;
import com.eazytec.bpm.admin.widget.service.WidgetService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.Department;
import com.eazytec.model.Layout;
import com.eazytec.model.User;
import com.eazytec.model.UserWidget;
import com.eazytec.model.Widget;
import com.eazytec.model.WidgetDepartment;
import com.eazytec.service.UserService;



@Service("widgetService")
public class WidgetServiceImpl implements WidgetService{
	WidgetDao widgetDao;
	LayoutService layoutService;
    
	private DepartmentService departmentService;
	
	private UserService userService;
    
	@Autowired
	public void setLayoutService(LayoutService layoutService){
		this.layoutService = layoutService;
	}
	
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
	
	public Widget saveWidget(Widget widget) throws BpmException{
		//widget.setId(widget.getName().toString().trim().toLowerCase());
			return widgetDao.saveWidget(widget);
		}
	
    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
	
	/**
	 * save UserWidget
	 * @param selectedWidgets
	 * @param userName
	 * @param noOfColumns
	 * @return void
	 */
	public void saveUserWidget(String selectedWidgets,String userName,Integer noOfColumns ){
		//if(!selectedWidgets.isEmpty() && selectedWidgets != ""){
			UserWidget getUserWidgetById = widgetDao.getUserWidgetForId(userName);
			if(getUserWidgetById != null){
				getUserWidgetById.setNoOfWidget(noOfColumns);
				if(selectedWidgets.equals("adminEMpty")){
					selectedWidgets = "";
				}
				getUserWidgetById.setWidgetId(selectedWidgets);
				widgetDao.saveUserWidget(getUserWidgetById);
			}else{
				UserWidget userWidget=new UserWidget();
				userWidget.setUserName(userName);
				userWidget.setNoOfWidget(noOfColumns);
				userWidget.setWidgetId(selectedWidgets);
				widgetDao.saveUserWidget(userWidget);
			}
		//}
	}
	
	public void saveUserSelectedWidget(String selectedWidgets,String userName){
		if(!selectedWidgets.isEmpty() && selectedWidgets != ""){
			UserWidget getUserWidgetById = widgetDao.getUserWidgetForId(userName);
			if(getUserWidgetById != null){
				getUserWidgetById.setWidgetId(selectedWidgets);
				widgetDao.saveUserWidget(getUserWidgetById);
			}
		}
	}
	
	/**
	 * get All Wdiget
	 * @param 
	 * @return List
	 */
	public List<Widget> getAllWidget() throws EazyBpmException{
		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 if(CommonUtil.isUserAdmin(user)){
			 return widgetDao.getAllWidget();
		 }else{
			 List<String> userDeps=userService.getUserAdminDepartments(user);
			 return widgetDao.getPublicWidgetByDepartment(userDeps);
		 }
		
	}
	
	/**
	 * get All widget that equivalent id for that specific User
	 * @param
	 * @return List of widget
	 */
	public List<Widget> getAllUserSelectedWidget() throws EazyBpmException{
		List<Widget> widgetList = new ArrayList<Widget>();
		List<String> widgetIds = new  ArrayList<String>();
		Layout layout = layoutService.getLoggedInUserLayout();
		if(layout != null){
				List<String> widgetIdList = new ArrayList<String>(Arrays.asList(layout.getColumnWidget().split(",")));	
				widgetIds.addAll(widgetIdList);
		}
		if(widgetIds != null && !widgetIds.isEmpty()){
			widgetList = widgetDao.getAllUserSelectedWidget(widgetIds);
			if(widgetList != null && !widgetList.isEmpty()){
				widgetList = widgetList;
			}else{
				widgetList =  null;
			}
				
		}
		return widgetList;
		
	}
	
	/**
	 * get User widget
	 * @param userName
	 * @return UserWidget
	 */
	public UserWidget getUserWidgetForId(String userName) throws EazyBpmException{
		return widgetDao.getUserWidgetForId(userName);
	}
	public Widget getWidgetForId(String id) throws EazyBpmException{
		return widgetDao.getWidgetForId(id);
	}

	/*public Widget getWidgetForName(String name) throws EazyBpmException{
		return widgetDao.getWidgetForName(name);
	}*/
	
	public List<Widget> getWidgetForNames(String name) throws EazyBpmException{
		return widgetDao.getWidgetForNames(name);
	}
	
	public List<Widget> getUserWidgetForNames(String name) throws EazyBpmException{
		return widgetDao.getWidgetForNames(name);
	}
	
	public Widget getActiveWidget() throws EazyBpmException{
		return widgetDao.getActiveWidget();
	}
	
	public WidgetDao getWidgetDao() {
		return widgetDao;
	}
	
	//List<FileImportForm> fileImportFormList = documentFormService.getAllFiles();

	@Autowired
	public void setWidgetDao(WidgetDao widgetDao) {
		this.widgetDao = widgetDao;
	}
	
	/*@Autowired
	public void setDocumentFormService(DocumentFormService documentFormService) {
		this.documentFormService = documentFormService;
	}*/
	
	public ArrayList<Widget> getWidgetGridData() {
		//log.info("getting widget grid datas ");
		
        return  widgetDao.getWidgetGridData();
	}
	
	public void removeWidget(String Id) {
		widgetDao.removeWidget(Id);
	}
		
	 public void deleteWidgets(List<String> ids){
	    	try {
	    		List<Widget> widgets = widgetDao.getWidgetForIds(ids);
	    		for(Widget widget  : widgets){
	    			removeWidget(widget.getId());
	    		}
	    		/*if(Ids != null && !Ids.isEmpty()){
	    			if (Ids.contains(",")) {
	    				String[] ids = Ids.split(",");
	    				for(String eventId :ids){
	    					widgetDao.remove(eventId);
	    				}
	    			} else {
	    				widgetDao.remove(Ids);
	    			}
	    		}*/
	    	} catch (Exception e) {
	    		throw new BpmException(e.getMessage(),e);
			}
	  }
	 
	 /**
	  * Get all the classification name
	  * @return
	  * @throws Exception
	  * 
	  */
	 public List<Classification> getAllClassification() throws EazyBpmException{
		 return widgetDao.getAllClassification();
	 }
	 public List<Widget> getWidgetForIds(List<String> names) throws EazyBpmException{
		 Collections.sort(names);
		 return widgetDao.getWidgetForIds(names);
	 }
	 
	 public void setDepartmentType(Widget widget)throws EazyBpmException{
		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 if(!CommonUtil.isUserAdmin(user)){
			boolean isDepartmentAdmin=departmentService.getIsDepartmentAdmin(user);
			if(isDepartmentAdmin){
				user =userService.getUser(user.getId());
				Set<Department> userDeps=user.getDepartmentByAdministrators();
				for(Department userDep:userDeps){
					WidgetDepartment widgetDepartment=new WidgetDepartment((String)widget.getId(),(String)userDep.getId());
					widgetDao.saveWidgetDepartment(widgetDepartment);
				}
			}
		}
	 }
	 
	 public boolean getWidgetIsDepartmentAdmin(User user,Widget widget)throws EazyBpmException{
		 boolean isDepartmentAdmin=departmentService.getIsDepartmentAdmin(user);
		 if(isDepartmentAdmin){
				List<String> userDeps=userService.getUserAdminDepartments(user);
				List<WidgetDepartment> widgetDepartmentList=widgetDao.getWidgetDepartmentList((String)widget.getId(),userDeps);
				if(widgetDepartmentList!=null && !widgetDepartmentList.isEmpty()){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
	 }
	 
	}

	
