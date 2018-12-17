package com.eazytec.bpm.admin.group.service;

import java.util.List;
import java.util.Map;

import com.eazytec.dto.GroupDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.service.GroupExistsException;

public interface GroupService {
	
	/**
     * Retrieves a group by groupId.  An exception is thrown if group not found
     *
     * @param groupId the identifier for the group
     * @return Group
     */
    Group getGroup(String groupId);

    /**
     * Finds a group by their groupname.
     *
     * @param groupname the group's groupname used to login
     * @return Group a populated group object
     */
    Group getGroupByName(String groupname);
    
    List<Group> getGroups();

    /**
     * Saves a group's information
     *
     * @param group the group's information
     * @return updated group
     * @throws GroupExistsException thrown when group already exists
     */
    Group saveGroup(Group group) throws GroupExistsException;

    /**
     * Removes a group from the database by their groupId
     *
     * @param groupId the group's id
     */
    void removeGroup(String groupId);
    
    /**
     * Finds a group by their id.
     *
     * @param id the group's id used to login
     * @return Group a populated group object
     */
    Group getGroupById(String id);
    
    /**
     * Removes a groups from the database by their groupIds
     *
     * @param groupId the group's id
     */
    String removeGroups(List<String> groupIds,User user);
    
    /**
     * Get the users lists based on the group id
     * @param id
     * @return
     */
    public List<LabelValue> getUsersAsLabelValueByGroupId(String id) throws EazyBpmException;
    
    public List<LabelValue> getAllGroups();
    
    /**
     * get groups by super group
     *
     * @param superGroup
     * @return
     */
    public List<Group> getGroupBySuperGroup(String superGroup);
    
    /**
     * get groups by groupId
     *
     * @param superGroup
     * @return
     */
    public  List<LabelValue> getGroupsByLabelValue(String groupId);
    
    /**
     * 
     * @param id
     * @return
     * @throws EazyBpmException
     * @author vinoth
     */
    public List<User> getUsersByGroupId(String id) throws EazyBpmException;
    
    /**
     * update group persion incharge
     * 
     * @param userId
     * @param personIncharge
     */
    public void updateGroupIncharge(String userId,String personIncharge);
    
    /**
     * update group leader
     * 
     * @param userId
     * @param leader
     */
    public void updateGroupLeader(String userId,String leader);
    
    /**
     * check the group already exist or not
     * @param groupName
     * @return
     */
    public boolean isDublicateGroup(String groupName);
    
    /**
     * update super group
     * 
     * @param oldSuperGroup
     * @param newSuperGroup
     */
    public void updateSuperGroup(String oldSuperGroup,String newSuperGroup);
    
    /**
     * get all groups dto
     * @return
     * @throws BpmException
     */
    public List<GroupDTO> getGroupsDTO() throws BpmException;
    
    /**
     * get all parents groups as Label Value
     * @return
     * @throws BpmException
     */
    public List<LabelValue> getAllParentGroupsAsLabelValue() throws BpmException;
    
    /**
     * get groups as Label Value by group parent
     * @return
     * @throws BpmException
     */
    public List<LabelValue> getGroupsAsLabelValueByParent(String parentGroup) throws BpmException;
    
    /**
     * get groups by their ids.
     * @param ids
     * @return
     */
    public List<Group> getGroupsByIds(List<String> ids);
    
    List<User> getUsersByGroupIds(List<String> ids);
    
    /**
	 * Generates order no for a group based on the order no of the parent id related to its child group
	 * @param parentDictId
	 * 		 Super group which the group belongs to 
	 * @return
	 * 		 Order no of the group id
	 * @throws EazyBpmException
	 */
	public int generateOrderNo(String superGroup) throws EazyBpmException;
	
   /**
	 * 
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean updateGroupOrderNos(List<Map<String,Object>> groupList) throws EazyBpmException;

	Group getGroupIdByGroupRole(String roleId);
	
	public List<String> getUserNamesByGroupId(String id) throws EazyBpmException;
	
	/**
	 * Is the group belongs to group admin
	 * @param user
	 * @param group
	 * @return
	 * @throws EazyBpmException
	 */
	boolean getIsGroupAdmin(User user,Group group,boolean isSave) throws EazyBpmException;
	
	/**
	 * Is group admin
	 * @param user
	 * @return
	 * @throws EazyBpmException
	 */
	boolean getIsGroupAdmin(User user) throws EazyBpmException;
	
	/**
	 * Get the group of group admin
	 * @param groupIds
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getAdminGroups(List<String> groupIds)throws EazyBpmException;
	
	/**
	 * Get one user group
	 * @param user
	 * @return
	 * @throws EazyBpmException
	 */
	String getUserGroupId(String user)throws EazyBpmException;
	
	/**
	 * get  user names for given group id list
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getUserNamesByGroupIds(List<String> ids) throws EazyBpmException;

	List<Group> getAllParentGroups() throws BpmException;

}


