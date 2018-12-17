/**
 * 
 */
package com.eazytec.bpm.admin.department.service;

/**
 * @author mathi
 *
 */
import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

import com.eazytec.dto.DepartmentDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.service.DepartmentExistsException;

public interface DepartmentService {
	
	/**
     * Retrieves a Department by departmentId.  An exception is thrown if Department not found
     *
     * @param groupId the identifier for the group
     * @return Group
     */
    Department getDepartment(String departmentId);

    /**
     * Finds a department by their departmentName.
     *
     * @param departmentName the group's departmentName used to login
     * @return Department a populated group object
     */
    Department getDepartmentByName(String departmentName);

    /**
     * Retrieves a list of all Departments.
     *
     * @return List
     */
    List<Department> getDepartments();
    
    /**
	 * 
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean updateDepartmentOrderNos(List<Map<String,Object>> departmentList) throws EazyBpmException;

    /**
     * Saves a Department's information
     *
     * @param department the Department's information
     * @return updated department
     * @throws DepartmentExistsException thrown when department already exists
     */
    Department saveDepartment(Department department) throws DepartmentExistsException;

    /**
     * Removes a group from the database by their groupId
     *
     * @param groupId the group's id
     */
    void removeDepartment(String departmentId);
    
    /**
     * Finds a department by their id.
     *
     * @param id the department's id used to login
     * @return department a populated department object
     */
    Department getDepartmentById(String id);
    
    /**
     * Remove a departments from the database by their departmentIds
     *
     * @param departmentId the department's id
     * @throws BpmException
     */
    boolean removeDepartments(List<String> departmentIds, User user, boolean isPermitToDelete) throws BpmException;
    
    /**
     * Retrieves a list of all Departments.
     *
     * @return List
     */
    public List<LabelValue> getAllDepartments();
    
    /**
     * Retrieves a list of Users who are all in the department.
     *
     * @return List
     */
    public List<User> getDepartmentUsers(String departmentId);

	/**
     * Get the users lists based on the group id
     * @param id
     * @return
	 * @throws Exception 
     */
    public List<LabelValue> getUsersByDepartmentId(String id) throws Exception;
    
    
    /**
    * Retrieves a list of Departments.
    * @param superDepartment
    *
    * @return List
    * @throws Exception 
    */
    public List<Department> getDepartmentBySuperDepartmentId(String superDepartment) throws Exception;
    	
    /**
     * update a Department
     *
     * @param departments
     * @param superDepartment
     */
    public void updateDepartments(List<Department> departments,String superDepartment);
    
    /**
     * Retrieves a list of Departments.
     * @param departmentId
     *
     * @return List
     */
    public List<LabelValue> getDepartmentsByLabelValue(String departmentId);
    
    /**
     * check the department already exist or not
     * @param name
     * @return
     */
    public boolean isDuplicateDepartment(String name);
    
    /**
     * 
     * @return
     * @throws BpmException
     */
    public List<DepartmentDTO> getAllDepartmentDTO() throws BpmException;
    
    /**
     * Gets all the departments as label value pairs.
     * @return List of department names
     * @throws EazyBpmException
     */
    public List<LabelValue> getAllDepartmentNames() throws EazyBpmException;
    
    List<Department> getDepartmentsByNames(List<String> names);
    
    /**
     * get root department as Label Value
     * @return
     * @throws BpmException
     */
    public List<LabelValue> getOrganizationAsLabelValue() throws BpmException;
 
    /**
     * get departments as Label Value by department parent
     * @return
     * @throws BpmException
     */
    public List<LabelValue> getDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException;
    
    List<String> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException;
    
    
    /**
	 * Generates order no for a department based on the order no of the parent id related to its child department
	 * @param parentDictId
	 * 		 Super department which the department belongs to 
	 * @return
	 * 		 Order no of the department id
	 * @throws EazyBpmException
	 */
	public int generateOrderNo(String superDepartment) throws EazyBpmException;

	public Department getDepartmentIdByDepartmentRole(String roleId);
	
	 public void updateDepartmentOwner(String userId,String departmentOwner);
	 
	 public List<String> getUserNamesByDepartmentId(String id) throws EazyBpmException;
	 
	 /**
	  * To get the Department Admin's Department
	  * @param listDep
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getAdminDepartments(List<String> listDep)throws EazyBpmException;
	 
	 /**
	  * To check the user is a Department Admin's
	  * @param user
	  * @return
	  * @throws EazyBpmException
	  */
	 boolean getIsDepartmentAdmin(User user)throws EazyBpmException;
	 
	 /**
	  * To check the role belongs to the Department Admin's
	  * @param user
	  * @param role
	  * @return
	  * @throws EazyBpmException
	  */
	 boolean getIsDepartmentAdmin(User user,Department department)throws EazyBpmException;
	 
	 /**
	  * get Super Departments As LabelValue By Parent
	  * @param parentDepartment
	  * @return
	  * @throws BpmException
	  */
	 List<LabelValue> getSuperDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException;
	 
	 boolean setDepartmentPermissionValues(ModelMap model,User user) throws BpmException;
	 
	 List<LabelValue> getUserAdministrationDepartments(String userId) throws BpmException;
	 
	 List<String> getUserAdministrationDepartmentIds(String userId) throws BpmException;
	 
	 List<LabelValue> getUserAdministrationSuperDepartments(String userId) throws BpmException;
	 
	 List<String> getDepartmentAdminDepartmentIds(String userId) throws BpmException;
	 
	 boolean setDepartmentPermissionForEdit(ModelMap model,User user,String departmentId) throws BpmException ;
	 
	 Department getRootDepartment() throws BpmException;
}
