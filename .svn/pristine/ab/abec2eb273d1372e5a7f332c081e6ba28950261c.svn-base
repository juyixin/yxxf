/**
 * 
 */
package com.eazytec.bpm.admin.department.dao;

import java.util.List;
import java.util.Set;

import com.eazytec.dao.GenericDao;
import com.eazytec.dto.DepartmentDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;

/**
 * 
 * @author mathi
 *
 */
public interface DepartmentDao extends GenericDao<Department, String>{
	
	  /**
     * Gets department information based on departmentName
     * @param departmentName the departmentName
     * @return populated department object
     */
	Department getDepartmentByName(String departmentName);

    /**
     * Removes a department from the database by name
     * @param departmentName the department's departmentName
     */
    void removeDepartment(String departmentName);

   
    /**
     * Gets a list of department's ordered by the uppercase version of their departmentName.
     *
     * @return List populated list of department's
     */
    List<Department> getDepartments();

    /**
     * Saves a department's information.
     * @param department the object to be saved
     * @return the persisted Department object
     */
    Department saveDepartment(Department department);
	
    /**
     * Finds a department by their id.
     *
     * @param id the department's id used to login
     * @return Department a populated department object
     */
    Department getDepartmentById(String id);
    
    /**
     * Finds a departments by their ids.
     * @param ids
     * @return
     */
    public List<Department> getDepartmentsByIds(List<String> ids);
    
    /**
     * 
     * @param id
     * @return
     * @throws EazyBpmException
     */
    public List<LabelValue> getUsersByDepartmentId(String id) throws EazyBpmException;
    
    /**
    * Finds a departments by super department ids.
    * @param superDepartment
    * @return
    */
	public List<Department> getDepartmentBySuperDepartmentId(String superDepartment);
	
	/**
     * check the department already exist or not
     * @param name
     * @return
     */
    public boolean isDublicateDepartment(String name);
    
    /**
     * Getting all the department by its dto object.
     * @author vinoth
     * @return
     * @throws BpmException
     */
    public List<DepartmentDTO> getAllDepartmentDTO() throws BpmException;
    
    /**
     * Gets all department names as label value pair.
     * @return list of department names.
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
  	 * Updates department order no 
  	 * @param departmentId
  	 * @param orderNo
  	 * @return
  	 * @throws EazyBpmException
  	 */
  	public boolean updateDepartmentOrderNos(String departmentId, int orderNo) throws EazyBpmException;

	public Department getDepartmentIdByDepartmentRole(String roleId);
	
	public void updateDepartmentOwner(String userId,String departmentOwner);
	
	public List<Object> getUserNamesByDepartmentId(String id) throws EazyBpmException;
	
	/**
	  * To get the Department Admin's Department
	  * @param listDep
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getAdminDepartments(List<String> listDep) throws EazyBpmException;
	 
	 /**
	  * get Super Departments As LabelValue By Parent
	  * @param parentDepartment
	  * @return
	  * @throws BpmException
	  */
	 List<LabelValue> getSuperDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException;
	 
	 List<LabelValue> getUserAdministrationDepartments(String userId) throws BpmException;
	 
	 List<String> getUserAdministrationDepartmentIds(String userId) throws BpmException;
	 
	 List<LabelValue> getUserAdministrationSuperDepartments(String userId) throws BpmException;
	 
	 Department getRootDepartment() throws BpmException;
}
