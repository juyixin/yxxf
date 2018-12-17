package com.eazytec.bpm.admin.group.dao;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.GenericDao;
import com.eazytec.dto.GroupDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;

/**
 * 
 * @author Karthick
 *
 */
public interface GroupDao extends GenericDao<Group, String>{
	
	  /**
     * Gets group information based on groupname
     * @param groupname the groupname
     * @return populated group object
     */
    Group getGroupByName(String groupname);

    /**
     * Removes a group from the database by name
     * @param groupname the group's groupname
     */
    void removeGroup(String groupname);

   
    /**
     * Gets a list of groups ordered by the uppercase version of their groupname.
     *
     * @return List populated list of groups
     */
    List<Group> getGroups();

    /**
     * Gets list of groups by orderno
     * 
     * @return List populated list of groups
     */
    List<Group> getAllGroupList();

    /**
     * Saves a group's information.
     * @param group the object to be saved
     * @return the persisted Group object
     */
    @Transactional
    Group saveGroup(Group group);
	
    /**
     * Finds a group by their id.
     *
     * @param id the group's id used to login
     * @return Group a populated group object
     */
    Group getGroupById(String id);
    
    /**
     * Finds a groups by their ids.
     * @param ids
     * @return
     */
    public List<Group> getGroupsByIds(List<String> ids);
    
    public List<Group> getAllGroups();
    
    public void deleteGroup(Group group);
    
    /**
     * get Users by group id
     * 
     * @param id
     * @return
     * @throws EazyBpmException
     */
    public List<LabelValue> getUsersAsLabelValueByGroupId(String id) throws EazyBpmException;
    
    /**
     * get groups by super group
     *
     * @param superGroup
     * @return
     */
    public List<Group> getGroupBySuperGroup(String superGroup);
    
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
     * get all groups as dto
     * @return
     * @throws EazyBpmException
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
    
    List<User> getUsersByGroupIds(List<String> ids) throws EazyBpmException;
    
    /**
  	 * Updates group order no 
  	 * @param groupId
  	 * @param orderNo
  	 * @return
  	 * @throws EazyBpmException
  	 */
  	public boolean updateGroupOrderNos(String groupId, int orderNo) throws EazyBpmException;

	Group getGroupIdByGroupRole(String roleId);
    
	 public List<Object> getUserNamesByGroupId(String id) throws EazyBpmException;
	 
	 List<String> getAdminGroups(List<String> groupIds)throws EazyBpmException;
	 
	 /**
	  * retrieve all user names for given groupid list
	  * @param ids
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUserNamesByGroupIds(List<String> ids) throws EazyBpmException;

	List<Group> getGroupsForAPI();
	
    public List<Group> getAllParentGroups() throws BpmException;

}
