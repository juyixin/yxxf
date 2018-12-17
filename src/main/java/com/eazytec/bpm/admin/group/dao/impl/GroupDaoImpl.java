package com.eazytec.bpm.admin.group.dao.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.HSQLCaseFragment;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.group.dao.GroupDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.dto.GroupDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;

/**
 * This class interacts with Hibernate session to save/delete, retrieve Group
 * objects.
 * 
 * @author madan
 */
@Repository("groupDao")
public class GroupDaoImpl extends GenericDaoHibernate<Group, String> implements
		GroupDao {

	/**
	 * Constructor to create a Generics-based version using Group as the entity
	 */
	public GroupDaoImpl() {
		super(Group.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Group getGroupByName(String groupname) {
		List groups = getSession().createCriteria(Group.class)
				.add(Restrictions.eq("name", groupname)).list();
		if (groups.isEmpty()) {
			return null;
		} else {
			return (Group) groups.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeGroup(String groupname) {
		Object group = getGroupByName(groupname);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(group);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteGroup(Group group) {
		Session session = getSessionFactory().getCurrentSession();
		for(User user:group.getUsers()){
			user.getGroups().remove(group);
			session.saveOrUpdate(user);
		}
		session.delete(group);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getGroups() {
		Query qry = getSession().createQuery(
				"from Group u order by upper(u.groupname)");
		return qry.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getGroupsForAPI() {
		Query qry = getSession().createQuery(
				"from Group ");
		return qry.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getAllGroupList() {
		Query qry = getSession().createQuery(
				"from Group u order by u.orderNo");
		return qry.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public Group saveGroup(Group group) {
		if (log.isDebugEnabled()) {
			log.debug("group's id: " + group.getId());
		}
		getSession().saveOrUpdate(group);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return group;
	}

	/**
	 * Overridden to call the saveGroup method since saveGroup flushes the
	 * session and saveObject of BaseDaoHibernate does not.
	 * 
	 * @param group
	 *            the group to save
	 * @return the modified group (with a primary key set if they're new)
	 */
	@Override
	public Group save(Group group) {
		return this.saveGroup(group);
	}

	/**
	 * {@inheritDoc}
	 */
	public Group getGroupById(String id) {
		List groups = getSession().createCriteria(Group.class)
				.add(Restrictions.eq("id", id)).list();
		if (groups.isEmpty()) {
			return null;
		} else {
			return (Group) groups.get(0);
		}
	}
	
	 /**
     * {@inheritDoc}
     */
    public List<Group> getGroupsByIds(List<String> ids) {
    	List<Group> groups = getSession().createQuery("from Group as group where group.id in (:list)").setParameterList("list", ids).list();
        if (groups.isEmpty()) {
            return null;
        } else {
            return groups;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Group> getAllGroups() {
        log.debug("Retrieving all department names...");
        return getSession().createCriteria(Group.class).list();
    }
	
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getUsersAsLabelValueByGroupId(String id) throws EazyBpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName) as userName,user.id as id) from User as user join user.groups as group where group.id = '"+id+"'").list();
    	if(userList.isEmpty()){ 
    		return null;
    	}else{
    		return userList;
    	}
    }
    
    
    
    /**
	 * {@inheritDoc}
	 */
	public List<Group> getGroupBySuperGroup(String superGroup) {
		List<Group> groups = getSession().createCriteria(Group.class)
				.add(Restrictions.eq("superGroup", superGroup)).list();
		if (groups.isEmpty()) {
			return null;
		} else {
			return groups;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateGroupOrderNos(String groupId, int orderNo) throws EazyBpmException {
		Query qry = getSession().createQuery("update Group groupObj set groupObj.orderNo = "+orderNo+" where groupObj.id = '"+ groupId + "' ");
		int result = qry.executeUpdate();
		return result != 0  ? true : false;
	}
	
    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByGroupId(String id) throws EazyBpmException{
    	List<User> userList = (List<User>) getSession().createQuery("select group.users from Group as group where group.id = '"+id+"'").list();
    	if(userList.isEmpty()){ 
    		return null;
    	}else{
    		return userList;
    	}
    }
    
    @SuppressWarnings("unchecked")
  	public List<User> getUsersByGroupIds(List<String> ids) throws EazyBpmException{
      	List<User> userList = (List<User>) getSession().createQuery("select group.users from Group as group where group.id in (:list)").setParameterList("list", ids).list();
      	if(userList.isEmpty()){
      		return null;
      	}else{
      		return userList;
      	}
      }
    
    /**
     * {@inheritDoc}
     */
    public void updateGroupIncharge(String userId,String personIncharge){
    	Query query = getSession().createQuery("UPDATE Group set personIncharge = '"+personIncharge+"'WHERE personIncharge='"+userId+"'");
    	query.executeUpdate();
    }
    
   /**
    * {@inheritDoc}
    */
   public void updateGroupLeader(String userId,String leader){
   	Query query = getSession().createQuery("UPDATE Group set leader = '"+leader+"'WHERE leader='"+userId+"'");
   	query.executeUpdate();
   }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDublicateGroup(String groupName){
    	List<Group> groups = getSession().createCriteria(Group.class)
    	.add( Restrictions.or(
        Restrictions.eq( "id", groupName ))).list();
		if (groups.isEmpty()) {
			return false;
		} else {
			return true;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateSuperGroup(String oldSuperGroup,String newSuperGroup){
    	Query query = getSession().createQuery("UPDATE Group set superGroup = '"+newSuperGroup+"', isParent = 1 WHERE superGroup='"+oldSuperGroup+"'");
    	query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<GroupDTO> getGroupsDTO() throws BpmException{
    	List<GroupDTO> groupList = (List<GroupDTO>) getSession().createQuery("select new com.eazytec.dto.GroupDTO(group.id as id,group.name as name,group.superGroup as superGroup,group.isParent as isParent,group.viewName as viewName) from Group as group").list();
    	return groupList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllParentGroupsAsLabelValue() throws BpmException {
    	List<LabelValue> groupList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(group.viewName as name,group.id as id) from Group as group where group.isParent != '0'")
				.list();

		return groupList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Group> getAllParentGroups() throws BpmException {
		List<Group> groupList = getSession().createCriteria(Group.class)
				.add(Restrictions.eq("isParent", true)).list();
    	
		return groupList;
    }
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getGroupsAsLabelValueByParent(String parentGroup) throws BpmException {
    	
    	String hql = "";
    	if(StringUtils.isNotBlank(parentGroup)){
    		hql = "select new com.eazytec.model.LabelValue(group.viewName as name,group.id as id) from Group as group where group.superGroup = '"+parentGroup+"'";
    	}else{
    		hql = "select new com.eazytec.model.LabelValue(group.viewName as name,group.id as id) from Group as group where group.superGroup ='' or group.superGroup is null";
    	}
    	
    	List<LabelValue> groupList = (List<LabelValue>) getSession().createQuery(hql).list();

		return groupList;
    }
    
	public Group getGroupIdByGroupRole(String roleId) {
		List<Group> group = getSession().createQuery("select group from Group as group where group.groupRole='"+roleId+"'").list();
		return group.get(0);
	}

	/**
     * {@inheritDoc}
     */
    public List<Object> getUserNamesByGroupId(String id) throws EazyBpmException{
    	return getSession().createQuery("select user.id as id from User as user join user.groups as group where group.id = '"+id+"'").list();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getAdminGroups(List<String> groupIds)throws EazyBpmException{
    	List<String> groups = getSession().createQuery("select group.id from Group as group where group.superGroup in (:list) ").setParameterList("list", groupIds).list();
        if (groups.isEmpty()) {
            return null;
        } else {
        	return groups;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUserNamesByGroupIds(List<String> ids) throws EazyBpmException{
    	List<String> userList = getSession().createQuery("select user.username from User as user join user.groups as group where group.id in (:list)").setParameterList("list", ids).list();
    	if(userList.isEmpty()){ 
    		return null;
    	}else{
    		return userList;
    	}
    }
}
