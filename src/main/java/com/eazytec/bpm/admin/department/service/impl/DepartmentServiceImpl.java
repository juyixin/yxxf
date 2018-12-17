/**
 * 
 */
package com.eazytec.bpm.admin.department.service.impl;

/**
 * Implementation of DepartmentService Interface
 * 
 * @author mathi
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.dao.DepartmentDao;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.DepartmentComparator;
import com.eazytec.dto.DepartmentDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.DepartmentExistsException;
import com.eazytec.service.GroupExistsException;
import com.eazytec.service.UserManager;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("departmentService")
public class DepartmentServiceImpl extends GenericManagerImpl<Department, String> implements
	DepartmentService {

	private DepartmentDao departmentDao;

	private UserManager userManager;
	
	private RoleService roleService;
	
	@Autowired
	public void setUserManager(UserManager userManager) {
	    this.userManager = userManager;
	}
	
	@Autowired
	public void setRoleService(RoleService roleService) {
	    this.roleService = roleService;
	}
	    
	@Autowired
	public DepartmentServiceImpl(DepartmentDao departmentDao) {
		super(departmentDao);
		this.departmentDao = departmentDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Department> getDepartments(Department department) {
		return dao.getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public Department getDepartmentByName(String departmentName) {
		return departmentDao.getDepartmentByName(departmentName);
	}

	@Autowired
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.dao = departmentDao;
		this.departmentDao = departmentDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public Department getDepartment(String departmentId) {
		return departmentDao.get(departmentId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Department> getDepartments() {
		return departmentDao.getAllDistinct();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean updateDepartmentOrderNos(
			List<Map<String, Object>> departmentList) throws EazyBpmException {
		
		boolean hasUpdated = false;
 		for (Map<String, Object> department : departmentList) {
 			hasUpdated = departmentDao.updateDepartmentOrderNos(
					(String) department.get("departmentId"),
					Integer.parseInt((String)department.get("orderNo")));
		}

		return hasUpdated;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws GroupExistsException
	 */
	public Department getDepartment(Department department) throws GroupExistsException {

		try {
			return departmentDao.saveDepartment(department);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			throw new GroupExistsException("Department '" + department.getName()
					+ "' already exists!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeDepartment(Department department) {
		log.debug("removing department: " + department);
		departmentDao.remove(department);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeDepartment(String departmentId) {
		log.debug("removing department: " + departmentId);
		departmentDao.remove(departmentId);
	}
	
	/**
     * {@inheritDoc}
     */
    public Department getDepartmentById(String id) {
        return departmentDao.getDepartmentById(id);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @throws GroupExistsException
	 */
	public Department saveDepartment(Department department) throws DepartmentExistsException {

		try {
			
			if(department.getDepartmentRole() == null || department.getDepartmentRole().isEmpty()){
	   			 Role role=new Role("ROLE_"+department.getName()+"_DEPARTMENT","ROLE_"+department.getName()+"_DEPARTMENT","Internal");
	   			 role=roleService.save(role);
	   			 department.setDepartmentRole(role.getName());
	             log.info("Creating the ROLE :: "+role.getName());
	        }
			if(StringUtils.isBlank(department.getId())){
				department.setId(department.getName());
			}
			
			department.setVersion(department.getVersion() + 1);
			
			//For API
			if(department.getOrderNo() == null){
				department.setOrderNo(1);
			}
			return departmentDao.saveDepartment(department);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			throw new DepartmentExistsException("Department '" + department.getName()
					+ "' already exists!");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int generateOrderNo(String superDepartment) throws EazyBpmException {
		
		List<Department> departmentList = departmentDao.getDepartmentBySuperDepartmentId(superDepartment);
		if(departmentList != null && !departmentList.isEmpty()) {
			Collections.sort(departmentList, new DepartmentComparator());
			int maxOrderNo = departmentList.get(departmentList.size() - 1).getOrderNo();
			return maxOrderNo + 1;
		}else {
			return Constants.DEFAULT_ORDER_NO;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean removeDepartments(List<String> departmentIds, User user, boolean isPermitToDelete) throws BpmException{    	
    	if(CommonUtil.isUserAdmin(user)){
    		isPermitToDelete = true;
		}else {
			List<String> allAdminDepartmentIds = getDepartmentAdminDepartmentIds(user.getId());
			if(allAdminDepartmentIds.containsAll(departmentIds)){
				isPermitToDelete = true;
			}
		}    	
    	if(isPermitToDelete){
	    	List<Department> departmentList=departmentDao.getDepartmentsByIds(departmentIds);
	    	for (Department department : departmentList) {
	    		roleService.updateRoleDepartment(department,null);
	    		List<User> partTimeDepartmentUsers = userManager.getUsersByPartTimeDepartment(department);
	    		if(partTimeDepartmentUsers != null){
	    			userManager.updateUsersPartTimeDepartment(new HashSet<User>(partTimeDepartmentUsers), null);
	    		}
	    		List<User> departmentUsers = userManager.getUsersByDepartment(department);
	    		if(departmentUsers != null){
		    		if(department.getUserAction().equalsIgnoreCase("delete")){
		    				userManager.removeUsers(departmentUsers);
		    		}else{
		    			userManager.updateUsersDepartment(new HashSet<User>(departmentUsers),getDepartment(department.getSuperDepartment()));
		    		}
	    		}
	    		List<Department> departments = getDepartmentBySuperDepartmentId(department.getId());
	    		if(departments!=null){
	    			updateDepartments(departments,department.getSuperDepartment());
	    		}
	    		roleService.removeRole(department.getDepartmentRole());
	    		departmentDao.removeDepartment(department.getId());
	    	}
    	}
    	return isPermitToDelete;
    }
	
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllDepartments() {
        List<Department> departments = departmentDao.getDepartments();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Department department : departments) {
            list.add(new LabelValue(department.getName(), department.getId()));
        }

        return list;
    }
    
    /**
	 * {@inheritDoc}
	 */
	public List<User> getDepartmentUsers(String departmentId) {
		Department department=getDepartment(departmentId);
		return userManager.getUsersByDepartment(department);
	}
	
	/**
     * {@inheritDoc}
     */
    public List<LabelValue> getUsersByDepartmentId(String id) throws EazyBpmException{
    	return departmentDao.getUsersByDepartmentId(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Department> getDepartmentBySuperDepartmentId(String superDepartment) throws EazyBpmException{
    	log.info("Getting sub departments of : "+superDepartment);
    	return departmentDao.getDepartmentBySuperDepartmentId(superDepartment);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateDepartments(List<Department> departments,String superDepartment){
    	log.info("Updating Departments");
    	try {
    		for(Department department : departments){
        		if(!superDepartment.isEmpty()){
        		department.setSuperDepartment(superDepartment);
        		}
        		departmentDao.save(department);
        	}
		} catch (Exception e) {
			throw new BpmException(""+e.getMessage(),e);
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentsByLabelValue(String departmentId){
    	List<Department> departments = new ArrayList<Department>();
    	List<LabelValue> departmentList = new ArrayList<LabelValue>();
    	Department SuperDepartment=departmentDao.getDepartmentById(departmentId);
    	departments.add(0, SuperDepartment);
    	List<Department> depts = departmentDao.getDepartmentBySuperDepartmentId(departmentId);
    	if(depts!=null){
    		departments.addAll(depts);
    	}
    	if(departments != null){
	        for (Department department : new HashSet<Department>(departments)) {
	        	departmentList.add(new LabelValue(department.getName(), department.getId()));
	        }
    	}
        return departmentList;
    }
    
	public Department getDepartmentIdByDepartmentRole(String roleId) throws BpmException{
		return departmentDao.getDepartmentIdByDepartmentRole(roleId);

	}

    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllDepartmentNames() throws EazyBpmException {
    	
    	return departmentDao.getAllDepartmentNames();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateDepartment(String name){
    	return departmentDao.isDublicateDepartment(name);
    }
    
    public List<DepartmentDTO> getAllDepartmentDTO() throws BpmException{
    	return departmentDao.getAllDepartmentDTO();
    }
    
    public List<Department> getDepartmentsByNames(List<String> names) throws EazyBpmException{
    	return departmentDao.getDepartmentsByNames(names);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getOrganizationAsLabelValue() throws BpmException {
    	
    	return departmentDao.getOrganizationAsLabelValue();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException {
    	
    	return departmentDao.getDepartmentsAsLabelValueByParent(parentDepartment);
    }
    
    public List<String> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException{
    	return departmentDao.getUsersByDepartmentIds(ids);
    }

	 public void updateDepartmentOwner(String userId,String departmentOwner){
		 departmentDao.updateDepartmentOwner(userId,departmentOwner);
    }

	 public List<String> getUserNamesByDepartmentId(String id) throws EazyBpmException{
		 List<Object> userlist=departmentDao.getUserNamesByDepartmentId(id);
		return Arrays.asList(userlist.toArray(new String[0]));
	 }
	 
	 /**
      * {@inheritDoc}
      */
	 public List<String> getAdminDepartments(List<String> listDep)throws EazyBpmException{
		 return departmentDao.getAdminDepartments(listDep);
	 }
	 
	 /**
	  * {@inheritDoc}
	 */
	 public boolean getIsDepartmentAdmin(User user)throws EazyBpmException{
		 user= userManager.get(user.getId());
		 if(user.getDepartmentByAdministrators().isEmpty()){
			 return false;
		 }else{
			 return true;
		 }
	 }
	 
	 /**
	  * {@inheritDoc}
	 */
	 public boolean getIsDepartmentAdmin(User user,Department department)throws EazyBpmException{
		 boolean isEdit=false;
		 user= userManager.get(user.getId());
		   //It is a System Admin 
			if(CommonUtil.isUserAdmin(user)){
				isEdit=true;
			}else{
				Set<Department> userDepartments=user.getDepartmentByAdministrators();
				//not a Department Admin
				 if(userDepartments.isEmpty()){
					 isEdit=false;
				 }else{//Department Admin
					// =role.getRoleDepartment();
					 if(department!=null && department.getName()!=""){
						 department=get(department.getName());
						 List<String> userAdminDep=new ArrayList<String>();
						 
						 List<String> depIds=new ArrayList<String>();
						 	for(Department depObj:userDepartments){
								depIds.add(depObj.getId());
							}
						 List<String> adminDepartments =getAdminDepartments(depIds);
						 
						 if(adminDepartments!=null && !adminDepartments.isEmpty()){
							 userAdminDep.addAll(adminDepartments);
						 }
						
						 depIds.addAll(userAdminDep);
						 for(String userDepartment:depIds){
							//Condition to check the role Department belong to the Department admin
							if(userDepartment.equals(department.getId())){
								isEdit=true;
								break;
							}
						 }
					 }
				 }
			 }
			return isEdit;
		}
	 
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getSuperDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException {
    	return departmentDao.getSuperDepartmentsAsLabelValueByParent(parentDepartment);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean setDepartmentPermissionValues(ModelMap model,User user) throws BpmException {
		boolean isAdmin = CommonUtil.isUserAdmin(user);
		boolean isDepartmentAdmin = false;
		if(!isAdmin){
			List<LabelValue> departments = getUserAdministrationSuperDepartments(user.getId());
			if(departments.size() > 0){
				isDepartmentAdmin =true;
				if(departments.size() == 1){
		    		for(LabelValue department : departments){
		    			model.addAttribute("superDepartment",department.getValue());
		    			model.addAttribute("superDepartmentLabel",department.getLabel());
		    		}
				}
			}
		}
		if(isAdmin || isDepartmentAdmin){
			
			model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
			model.addAttribute("isSystemAdmin",isAdmin);
			return true;
		}
		return false;
    }
    
    public List<LabelValue> getUserAdministrationDepartments(String userId) throws BpmException {
    	return departmentDao.getUserAdministrationDepartments(userId);
    }
    
    public List<String> getUserAdministrationDepartmentIds(String userId) throws BpmException {
    	return departmentDao.getUserAdministrationDepartmentIds(userId);
    }
    
    public List<LabelValue> getUserAdministrationSuperDepartments(String userId) throws BpmException {
    	return departmentDao.getUserAdministrationSuperDepartments(userId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getDepartmentAdminDepartmentIds(String userId) throws BpmException{
    	List<String> adminDepartmentIds = getUserAdministrationDepartmentIds(userId);
    	if(adminDepartmentIds != null && !adminDepartmentIds.isEmpty()){
    		List<String> superDepartmentIds = getAdminDepartments(adminDepartmentIds);
    		if(superDepartmentIds != null && !superDepartmentIds.isEmpty()){
    			adminDepartmentIds.addAll(superDepartmentIds);
    		}
    	}
		return adminDepartmentIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean setDepartmentPermissionForEdit(ModelMap model,User user,String departmentId) throws BpmException {
		boolean isAdmin = CommonUtil.isUserAdmin(user);
		if(!isAdmin){
	    	Set<String> departmentIdsSet = new HashSet<String>();
	    	List<String> departmentIdsList = getDepartmentAdminDepartmentIds(user.getId());
	    	if(departmentIdsList != null && !departmentIdsList.isEmpty()){
		    	departmentIdsSet.addAll(departmentIdsList);
	    	}
    		for(String adminDepartmentId : departmentIdsSet){
    			if(adminDepartmentId.equals(departmentId)){
    				model.addAttribute("isDepartmentAdmin",true);
    				return true;
    			}
    		}
			return false;
		}else {
			model.addAttribute("isSystemAdmin",true);
			return true;
		}
    }
    
    public Department getRootDepartment() throws BpmException{
    	return departmentDao.getRootDepartment();
    }
}
