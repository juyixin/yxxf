package com.eazytec.bpm.admin.group.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.group.dao.GroupDao;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.role.dao.RoleDao;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GroupComparator;
import com.eazytec.dto.GroupDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.service.GroupExistsException;
import com.eazytec.service.UserManager;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("groupService")
public class GroupServiceImpl extends GenericManagerImpl<Group, String> implements
		GroupService {

	private GroupDao groupDao;
	private RoleDao roleDao;
	private UserManager userManager;
	
	@Autowired
	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;
	}
	
	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	@Autowired
	public void setUserManager(UserManager userManager) {
	    this.userManager = userManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Group> getGroups(Group group) {
		return dao.getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public Group getGroupByName(String groupname) {
		return groupDao.getGroupByName(groupname);
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.dao = groupDao;
		this.groupDao = groupDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public Group getGroup(String groupId) {
		return groupDao.get(groupId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Group> getGroups() {
		return groupDao.getAllGroupList();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Group> getGroupsForAPI() {
		return groupDao.getGroupsForAPI();
	}

	/**
	 * {@inheritDoc}
	 */
	public int generateOrderNo(String superGroup) throws EazyBpmException {
		
		List<Group> groupList = groupDao.getGroupBySuperGroup(superGroup);
		if(groupList != null && !groupList.isEmpty()) {
			Collections.sort(groupList, new GroupComparator());
			int maxOrderNo = groupList.get(groupList.size() - 1).getOrderNo();
			return maxOrderNo + 1;
		}else {
			return Constants.DEFAULT_ORDER_NO;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateGroupOrderNos(
			List<Map<String, Object>> groupList) throws EazyBpmException {
		
		boolean hasUpdated = false;
 		for (Map<String, Object> group : groupList) {
 			hasUpdated = groupDao.updateGroupOrderNos(
					(String) group.get("groupId"),
					Integer.parseInt((String)group.get("orderNo")));
 		}

		return hasUpdated;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws GroupExistsException
	 */
	public Group saveGroup(Group group) throws GroupExistsException {

		try {
			group.setVersion(group.getVersion() + 1);
			return groupDao.saveGroup(group);
		} catch (Exception e) {
			throw new GroupExistsException("Group '" + group.getName()
					+ "' already exists!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeGroup(Group group) {
		log.debug("removing group: " + group);
		groupDao.remove(group);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeGroup(String groupId) {
		log.debug("removing group: " + groupId);
		groupDao.remove(groupId);
	}
	
	/**
     * {@inheritDoc}
     */
    public Group getGroupById(String id) {
        return groupDao.getGroupById(id);
    }
    
    /**
     * Removes a groups from the database by their groupIds
     *
     * @param groupId the group's id
     */
    public String removeGroups(List<String> groupIds,User user){
    	StringBuffer notDeletedGroups=new StringBuffer();
    	notDeletedGroups.append("(");
    	int groupsCount=0;
    	
    	List<Group> groupList=groupDao.getGroupsByIds(groupIds);
    	
    	for(Group group : groupList){
			if(!getIsGroupAdmin(user, group,true)){
				notDeletedGroups.append(group.getId()+",");
				groupsCount++;
			}
		}
    	
    	if(groupsCount!=0){
			if(notDeletedGroups.lastIndexOf(",")>0){
				notDeletedGroups.deleteCharAt(notDeletedGroups.lastIndexOf(","));
			}
			notDeletedGroups.append(")");
		}else{
			for (Group group : groupList) {
				updateSuperGroup(group.getId(),"");
				roleDao.remove(group.getGroupRole());
				groupDao.deleteGroup(group);
			}
			return null;
		}
    	
    	return notDeletedGroups.toString();	
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllGroups() {
        List<Group> Groups = groupDao.getAllGroups();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Group group : Groups) {
            list.add(new LabelValue(group.getName(), group.getId()));
        }

        return list;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getUsersAsLabelValueByGroupId(String id) throws EazyBpmException{
    	return groupDao.getUsersAsLabelValueByGroupId(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Group> getGroupBySuperGroup(String superGroup){
    	return groupDao.getGroupBySuperGroup(superGroup);
    }
    
    /**
     * {@inheritDoc}
     */
    public  List<LabelValue> getGroupsByLabelValue(String groupId){
    	List<Group> groups = new ArrayList<Group>();
    	List<LabelValue> groupList = new ArrayList<LabelValue>();
    	Group SuperGroup=groupDao.getGroupById(groupId);
    	groups.add(0, SuperGroup);
    	List<Group> subGroups = groupDao.getGroupBySuperGroup(groupId);
    	if(subGroups!=null){
    		groups.addAll(subGroups);
    	}
    	if(groups !=null){
	        for (Group group : new HashSet<Group>(groups)) {
	        	groupList.add(new LabelValue(group.getName(), group.getId()));
	        }
        }
        return groupList;
    }
    
    public Group getGroupIdByGroupRole(String roleId) {
    	return groupDao.getGroupIdByGroupRole(roleId);
    }
    
    public List<User> getUsersByGroupId(String id) throws EazyBpmException{
    	return groupDao.getUsersByGroupId(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateGroupIncharge(String userId,String personIncharge){
    	groupDao.updateGroupIncharge(userId,personIncharge);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateGroupLeader(String userId,String leader){
    	groupDao.updateGroupLeader(userId,leader);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDublicateGroup(String groupName){
    	return groupDao.isDublicateGroup(groupName);
    }
    
    /**
     * update super group
     * 
     * @param oldSuperGroup
     * @param newSuperGroup
     */
    public void updateSuperGroup(String oldSuperGroup,String newSuperGroup){
    	groupDao.updateSuperGroup(oldSuperGroup, newSuperGroup);
    }
    
    public List<GroupDTO> getGroupsDTO() throws BpmException{
    	return groupDao.getGroupsDTO();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllParentGroupsAsLabelValue() throws BpmException {
    	
    	return groupDao.getAllParentGroupsAsLabelValue();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Group> getAllParentGroups() throws BpmException {
    	
    	return groupDao.getAllParentGroups();
    }
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getGroupsAsLabelValueByParent(String parentGroup) throws BpmException {
    	
    	return groupDao.getGroupsAsLabelValueByParent(parentGroup);
    }
    
    /**
     * Finds a groups by their ids.
     * @param ids
     * @return
     */
    public List<Group> getGroupsByIds(List<String> ids){
    	return groupDao.getGroupsByIds(ids);
    }
    
    public List<User> getUsersByGroupIds(List<String> ids){
    	return groupDao.getUsersByGroupIds(ids);
    }
    
    public List<String> getUserNamesByGroupId(String id) throws EazyBpmException{
    	List<Object> userlist=groupDao.getUserNamesByGroupId(id);
		return Arrays.asList(userlist.toArray(new String[0]));
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean getIsGroupAdmin(User user,Group group,boolean isSave) throws EazyBpmException{
    	boolean isEdit=false;
		 user= userManager.get(user.getId());
		
		   //It is a System Admin 
			if(CommonUtil.isUserAdmin(user)){
				isEdit=true;
			}else{
				Set<Group> userGroups=user.getGroupByAdministrators();
				//not a Group Admin
				 if(userGroups.isEmpty()){
					 isEdit=false;
				 }else{//Group Admin
					 String groupName="";
					 if(isSave){
						 groupName=group.getSuperGroup();
					 }else{
						 groupName=group.getId();
					 }
					 
					 if(groupName!=null && groupName!=""){
						 List<String> userAdminGroup=new ArrayList<String>();
						 
						 List<String> groupIds=new ArrayList<String>();
						 	for(Group groupObj:userGroups){
						 		groupIds.add(groupObj.getId());
							}
						 	
						 	List<String> adminGroups =getAdminGroups(groupIds);
							 
							 if(adminGroups!=null && !adminGroups.isEmpty()){
								 userAdminGroup.addAll(adminGroups);
							 }
							
						 	groupIds.addAll(userAdminGroup);
						 for(String userGroup:groupIds){
							//Condition to check the group belong to the Group admin
							if(userGroup.equals(groupName)){
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
    public boolean getIsGroupAdmin(User user) throws EazyBpmException{
    	user= userManager.get(user.getId());
		 if(user.getGroupByAdministrators().isEmpty()){
			 return false;
		 }else{
			 return true;
		 }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getAdminGroups(List<String> groupIds)throws EazyBpmException{
    	return groupDao.getAdminGroups(groupIds);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getUserGroupId(String userId)throws EazyBpmException{
    	String groupId="";
    	User user=userManager.get(userId);
    	Set<Group> userGroups=user.getGroupByAdministrators();
    	
    	for(Group userGroup:userGroups){
    		groupId=userGroup.getId();
    		break;
    	}
    	return groupId;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUserNamesByGroupIds(List<String> ids) throws EazyBpmException{
    	return groupDao.getUserNamesByGroupIds(ids);
    }
}
