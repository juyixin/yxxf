/**
 * 
 */
package com.eazytec.bpm.admin.department.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.department.dao.DepartmentDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.dto.DepartmentDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;

/**
 * This class interacts with Hibernate session to save/delete, retrieve Department
 * objects.
 * 
 * @author mathi
 */
@Repository("departmentDao")
public class DepartmentDaoImpl extends GenericDaoHibernate<Department, String> implements
		DepartmentDao {

	/**
	 * Constructor to create a Generics-based version using Department as the entity
	 */
	public DepartmentDaoImpl() {
		super(Department.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Department getDepartmentByName(String departmentName) {
		List<Department> departments = getSession().createCriteria(Department.class)
				.add(Restrictions.eq("name", departmentName)).list();
		if (departments.isEmpty()) {
			return null;
		} else {
			return (Department) departments.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeDepartment(String departmentName) {
		log.info("removing Department"+departmentName);
		Department department = getDepartmentByName(departmentName);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(department);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getDepartments() {
		Query qry = getSession().createQuery(
				"from Department u order by upper(u.name)");
		return qry.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public Department saveDepartment(Department department) {
		if (log.isDebugEnabled()) {
			log.debug("department's id: " + department.getId());
		}
		getSession().saveOrUpdate(department);
		// necessary to throw a DataIntegrityViolation and catch it in
		// DepartmentManager
		getSession().flush();
		return department;
	}

	/**
	 * Overridden to call the saveDepartment method since saveDepartment flushes the
	 * session and saveObject of BaseDaoHibernate does not.
	 * 
	 * @param department
	 *            the department to save
	 * @return the modified department (with a primary key set if they're new)
	 */
	@Override
	public Department save(Department department) {
		return this.saveDepartment(department);
	}

	/**
	 * {@inheritDoc}
	 */
	public Department getDepartmentById(String id) {
		List departments = getSession().createCriteria(Department.class)
				.add(Restrictions.eq("id", id)).list();
		if (departments.isEmpty()) {
			return null;
		} else {
			return (Department) departments.get(0);
		}
	}

	/**
     * {@inheritDoc}
     */
    public List<Department> getDepartmentsByIds(List<String> ids) {
//    	log.info("Getting departments object of : "+ids);
    	List<Department> departments = getSession().createQuery("from Department as department where department.id in (:list)").setParameterList("list", ids).list();
        if (departments.isEmpty()) {
            return null;
        } else {
            return departments;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getUsersByDepartmentId(String id) throws EazyBpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName) as userName,user.id as id) from User as user join user.department as department where department.id = '"+id+"'").list();
    	if(userList.isEmpty()){ 
    		return null;
    	}else{
    		return userList;
    	}
    }
    
    /**
	 * {@inheritDoc}
	 */
	public boolean updateDepartmentOrderNos(String departmentId, int orderNo) throws EazyBpmException {
		Query qry = getSession().createQuery("update Department department set department.orderNo = "+orderNo+" where department.id = '"+ departmentId + "' ");
		int result = qry.executeUpdate();
		return result != 0  ? true : false;
	}
    
    @SuppressWarnings("unchecked")
  	public List<String> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException{
      	List<String> userList = (List<String>) getSession().createQuery("select user.username from User as user join user.department as department where department.id in (:list)").setParameterList("list", ids).list();
      	if(userList.isEmpty()){
      		return null;
      	}else{
      		return userList;
      	}
      }
    /**
	 * {@inheritDoc}
	 */
	public List<Department> getDepartmentBySuperDepartmentId(String superDepartment) {
    	List<Department> departments = getSession().createQuery("from Department as department where department.superDepartment ='"+superDepartment+"' order by orderNo").list();
		if (departments.isEmpty()) {
			return null;
		} else {
			return departments;
		}
	}
    
	/**
     * {@inheritDoc}
     */
    public boolean isDublicateDepartment(String name){
    	List<Department> departments = getSession().createCriteria(Department.class)
    	.add( Restrictions.or(
        Restrictions.eq( "id", name ))).list();
		if (departments.isEmpty()) {
			return false;
		} else {
			return true;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<DepartmentDTO> getAllDepartmentDTO() throws BpmException{
    	List<DepartmentDTO> departmentDTOList = (List<DepartmentDTO>) getSession().createQuery("select new com.eazytec.dto.DepartmentDTO(department.id as id,department.name as name,department.superDepartment as superDepartment,department.departmentType as departmentType,department.isParent as isParent,department.viewName as viewName) from Department as department").list();
    	return departmentDTOList;
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<LabelValue> getAllDepartmentNames() throws EazyBpmException {

		List<LabelValue> userList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(department.name as name,department.id as id) from Department as department")
				.list();

		return userList;
				

	}
    
    public List<Department> getDepartmentsByNames(List<String> names) {
    	List<Department> departments = getSession().createQuery("from Department as department where department.name in (:list)").setParameterList("list", names).list();
        if (departments.isEmpty()) {
            return null;
        } else {
            return departments;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getOrganizationAsLabelValue() throws BpmException {
    	List<LabelValue> departmentList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(department.viewName as name,department.id as id) from Department as department where department.departmentType = 'root'")
				.list();
		return departmentList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException {
    	List<LabelValue> departmentList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(department.viewName as name,department.id as id) from Department as department where department.superDepartment = '"+parentDepartment+"'")
				.list();

		return departmentList;
    }
    
    public Department getDepartmentIdByDepartmentRole(String roleId) {
		List<Department> department = getSession().createQuery("select department from Department as department where department.departmentRole='"+roleId+"'").list();
		return department.get(0);	
    }
    
	 public void updateDepartmentOwner(String userId,String departmentOwner){
			Query query = getSession().createQuery("UPDATE Department set departmentOwner = '"+departmentOwner+"'WHERE departmentOwner='"+userId+"'");
		   	query.executeUpdate();
	 }
	 
	 /**
     * {@inheritDoc}
     */
    public List<Object> getUserNamesByDepartmentId(String id) throws EazyBpmException{
    	return getSession().createQuery("select user.id as id from User as user join user.department as department where department.id = '"+id+"'").list();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getAdminDepartments(List<String> listDep) throws EazyBpmException{
    	List<String> departments = getSession().createQuery("select department.id from Department as department where department.superDepartment in (:list) ").setParameterList("list", listDep).list();
        if (departments.isEmpty()) {
            return null;
        } else {
        	return departments;
        }
    }
    
    public List<LabelValue> getSuperDepartmentsAsLabelValueByParent(String parentDepartment) throws BpmException {
    	return (List<LabelValue>)getSession().createQuery(	"select new com.eazytec.model.LabelValue(department.name as name,department.id as id) from Department as " +
    			"department where department.superDepartment = '"+parentDepartment+"' and department.isParent = true")
				.list();
    }
    
    public List<LabelValue> getUserAdministrationDepartments(String userId) throws BpmException {
    	return (List<LabelValue>)getSession().createQuery(	"select new com.eazytec.model.LabelValue(department.name as name,department.id as id) from Department as " +
    			"department join department.administrators as user where user.id = '"+userId+"'")
				.list();
    }
    
    public List<String> getUserAdministrationDepartmentIds(String userId) throws BpmException {
    	return getSession().createQuery("select department.id as id from Department as " +
    			"department join department.administrators as user where user.id = '"+userId+"'")
				.list();
    }
    
    public List<LabelValue> getUserAdministrationSuperDepartments(String userId) throws BpmException {
    	return (List<LabelValue>)getSession().createQuery(	"select new com.eazytec.model.LabelValue(department.name as name,department.id as id) from Department as " +
    			"department join department.administrators as user where user.id = '"+userId+"' and department.isParent = true")
				.list();
    }
    
    /**
     * {@inheritDoc}
     */
    public Department getRootDepartment() throws BpmException {
    	List<Department> departmentList = getSession()
				.createQuery(
						"select department from Department as department where department.departmentType = 'root'")
				.list();
    	if(departmentList.size() > 0){
    		return departmentList.get(0);
    	}else {
    		return null;
    	}
    }
}

