package com.eazytec.bpm.admin.role.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.role.dao.RoleDao;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.common.util.RoleComparator;
import com.eazytec.dto.RoleDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.GroupExistsException;
import com.eazytec.service.RoleExistsException;
import com.eazytec.service.UserManager;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("roleService")
public class RoleServiceImpl extends GenericManagerImpl<Role, String> implements
		RoleService {

	private RoleDao roleDao;
	private UserManager userManager;
	private DepartmentService departmentService;
	
	@Autowired
	public void setUserManager(UserManager userManager) {
	    this.userManager = userManager;
	}
	
	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
	    this.departmentService = departmentService;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public List<Role> getGroups(Group group) {
		return dao.getAll();
	}



	/**
	 * {@inheritDoc}
	 *//*
	public Role getRole(String roleId) {
		return roleDao.get(new Long(roleId));
	}*/
	

    /**
     * {@inheritDoc}
     */
    public Role getRole(String rolename) {
        return roleDao.getRoleByName(rolename);
    }
    
    
	/**
	 * {@inheritDoc}
	 */
	public List<Role> getRoles() {
		return roleDao.getAllDistinct();
	}
	
	 /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String rolename) {
        return roleDao.getRoleByName(rolename);
    }
	/**
	 * {@inheritDoc}
	 * 
	 * @throws GroupExistsException
	 */
	public Role saveRole(Role role) throws RoleExistsException {

		try {
			
			role.setVersion(role.getVersion()+1);
			return roleDao.saveRole(role);
		} catch (Exception e) {
			throw new RoleExistsException("Role '" + role.getName()
					+ "' Problem in Save/update Role!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeRole(Role role) {
		log.debug("removing group: " + role);
		roleDao.remove(role);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeRole(String roleId) {
		log.debug("removing group: " + roleId);
		roleDao.removeRole(roleId);
	}
	
	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.dao = roleDao;
		this.roleDao = roleDao;
	}

	/**
     * {@inheritDoc}
     */
    public Role getRoleById(String id) {
        return roleDao.getRoleById(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getUsersLabelValueByRoleId(String id) throws EazyBpmException{
    	return roleDao.getUsersLabelValueByRoleId(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Role> getRolesByNames(List<String> names) {
        return roleDao.getRolesByNames(names);
    }
    
    /**
     * {@inheritDoc}
     */
    public String deleteSelectedRoles(List<String> ids,User user) throws Exception{
    	StringBuffer notDeletedRoles=new StringBuffer();
    	notDeletedRoles.append("(");
    	int rolesCount=0;
    	try{
    		List<Role> roles = roleDao.getRolesByNames(ids);
    		
    		for(Role role:roles){
    			if(!departmentService.getIsDepartmentAdmin(user, role.getRoleDepartment())){
    				notDeletedRoles.append(role.getId()+",");
    				rolesCount++;
    			}
    		}
    		
    		if(rolesCount!=0){
    			if(notDeletedRoles.lastIndexOf(",")>0){
        			notDeletedRoles.deleteCharAt(notDeletedRoles.lastIndexOf(","));
    			}
    			notDeletedRoles.append(")");
    		}else{
    			for(Role role:roles){
    				removeRole(role.getId());
        		}
    			return null;
    		}
    		return notDeletedRoles.toString();	
    		
	    }catch (Exception e) {
    		throw new BpmException("error.user.delete"+e.getMessage(),e);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles() {
        List<Role> roles = roleDao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Role role1 : roles) {
        	if(!role1.getName().equalsIgnoreCase("ROLE_DEFAULT")){
        		list.add(new LabelValue(role1.getName(), role1.getName()));
        	}
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Role> getRolesByIds(List<String> roleIds) {
        return roleDao.getRolesByIds(roleIds);
    }
    
    /**
     * 
     * @param id
     * @return
     * @throws EazyBpmException
     * @author vinoth
     */
    public List<User> getUsersByRoleId(String id) throws EazyBpmException{
    	return roleDao.getUsersByRoleId(id);
    }
    
    public List<User> getUsersByRoleIds(List<String> ids) throws EazyBpmException{
    	return roleDao.getUsersByRoleIds(ids);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateRoleDepartment(Department department,Department newDepartment){
    	List<Role> roles = roleDao.getRolesByDepartment(department);
    	if(roles!=null){
	    	for(Role role:roles){
	    		role.setRoleDepartment(newDepartment);
	    		roleDao.saveRole(role);
	    	}
    	}
    }
    
    public List<RoleDTO> getRolesDTO() throws BpmException{
    	return roleDao.getRolesDTO();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoleNames() throws EazyBpmException {
    	
    	return roleDao.getAllRoleNames();
    }

	/**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRolesAsLabelValue() throws BpmException {
    	
    	return roleDao.getAllRolesAsLabelValue();
    }

    /**
	 * {@inheritDoc}
	 */
	public int generateOrderNo() throws EazyBpmException {		
		List<Role> roleList = roleDao.getRoles();
		if(roleList != null && !roleList.isEmpty()) {
			Collections.sort(roleList, new RoleComparator());
			int maxOrderNo = roleList.get(roleList.size() - 1).getOrderNo();
			return maxOrderNo + 1;
		}else {
			return Constants.DEFAULT_ORDER_NO;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateRoleOrderNos(
			List<Map<String, Object>> roleList) throws EazyBpmException {
		
		boolean hasUpdated = false;
 		for (Map<String, Object> role : roleList) {
 			hasUpdated = roleDao.updateRoleOrderNos(
					(String) role.get("roleId"),
					Integer.parseInt((String)role.get("orderNo")));
		}

		return hasUpdated;
	}
	
	public List<Role> getAllUserRoles(User user)throws EazyBpmException{
		user= userManager.get(user.getId());
		Set<Role> roles=user.getRoles();
		boolean isAdmin=false;
		for(Role role:roles){
			if(role.getId().equals("ROLE_ADMIN")){
				isAdmin=true;
				break;
			}
		}
		
		if(isAdmin){
			return roleDao.getRoles();
		}else{
			Set<Department> userDepartments=user.getDepartmentByAdministrators();
			 if(userDepartments.isEmpty()){
				 return null;
			 }else{
				 List<String> depIds=new ArrayList<String>();
				 	for(Department depObj:userDepartments){
						depIds.add(depObj.getId());
					}
				 
				 List<String> adminDepartments =departmentService.getAdminDepartments(depIds);
				 if(adminDepartments!=null && !adminDepartments.isEmpty()){
			 		depIds.addAll(adminDepartments);
				 }
				 return roleDao.getUserAdminRoles(depIds);
			 }
		}
		
	}
	
	public List<String> getUserNamesByRoleId(String id) throws EazyBpmException{
		List<Object> userlist=roleDao.getUserNamesByRoleId(id);
		return Arrays.asList(userlist.toArray(new String[0]));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getUserAdminDepartmentRoles(List<String> userAdminDep)throws EazyBpmException{
		return roleDao.getUserAdminDepartmentRoles(userAdminDep);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getUserNamesByRoleIds(List<String> ids) throws EazyBpmException{
		return roleDao.getUserNamesByRoleIds(ids);
	}
}
