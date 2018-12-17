/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.model.User;


/**
 * @author Tom Baeyens
 * @author Saeid Mirzaei
 */
public class IdentityLinkEntityManager extends AbstractManager {

  public void deleteIdentityLink(IdentityLinkEntity identityLink, boolean isAddHisIdentity) {
	  if(isAddHisIdentity){
		  HistoricIdentityLinkEntity.createAndInsert(identityLink);
	  }
    getDbSqlSession().delete(identityLink);
  }
  
  public void deleteIdentityLinkWithAgency(IdentityLinkEntity identityLink, boolean isAddHisIdentity) {
	  if(identityLink.getParentId() != null){
		  deleteIdentityLinksWithRelationById(identityLink.getParentId(), isAddHisIdentity);
		  
	  }else{
		  deleteIdentityLinksWithRelationById(identityLink.getId(), isAddHisIdentity);
	  }
  }
  
  public void deleteIdentityLinksWithRelationById(String id, boolean isAddHistory) {
	  List<IdentityLinkEntity> identityLinks = findIdentityLinksWithRelation(id); 
	  for (IdentityLinkEntity identityLink: identityLinks) {
	      deleteIdentityLink(identityLink, isAddHistory);
	    }
  }
  @SuppressWarnings("unchecked")
  public List<IdentityLinkEntity> findIdentityLinksByTaskId(String taskId) {
    return getDbSqlSession().selectList("selectIdentityLinksByTask", taskId);
  }
  
  @SuppressWarnings("unchecked")
  public List<IdentityLinkEntity> findIdentityLinksByProcessDefinitionId(String processDefinitionId) {
    return getDbSqlSession().selectList("selectIdentityLinksByProcessDefinition", processDefinitionId);
  }
  
  @SuppressWarnings("unchecked")
  public List<IdentityLinkEntity> findIdentityLinksByParentId(String parentId) {
    return getDbSqlSession().selectList("selectIdentityLinksByParentId", parentId);
  }
  
  @SuppressWarnings("unchecked")
  public List<IdentityLinkEntity> findIdentityLinksWithRelation(String id) {
    return getDbSqlSession().selectList("selectIdentityLinksOfSubstitute", id);
  }

  @SuppressWarnings("unchecked")
  public List<IdentityLinkEntity> findIdentityLinkByTaskUserGroupAndType(String taskId, String userId, String groupId, String type) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("taskId", taskId);
    parameters.put("userId", userId);
    parameters.put("groupId", groupId);
    parameters.put("type", type);
    return getDbSqlSession().selectList("selectIdentityLinkByTaskUserGroupAndType", parameters);
  }
  
  @SuppressWarnings("unchecked")
  public List<IdentityLink> findIdentityLinkByTaskAndType(String taskId, String type) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("taskId", taskId);
    parameters.put("type", type);
    return getDbSqlSession().selectList("selectIdentityLinkByTaskAndType", parameters);
  }
  
  @SuppressWarnings("unchecked")
  public List<IdentityLink> findHistoricIdentityLinksForTask(String taskId, String type) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("taskId", taskId);
    parameters.put("type", type);
    return getDbSqlSession().selectList("selectHistoricIdentityLinkByTaskAndType", parameters);
  }
  
  @SuppressWarnings("unchecked")
  public List<IdentityLinkEntity> findIdentityLinkByProcessDefinitionUserAndGroup(String processDefinitionId, String userId, String groupId) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("processDefinitionId", processDefinitionId);
    parameters.put("userId", userId);
    parameters.put("groupId", groupId);
    return getDbSqlSession().selectList("selectIdentityLinkByProcessDefinitionUserAndGroup", parameters);
  }
  
  public IdentityLinkEntity findNextIdentityLinkByOrder(TaskEntity task, String type){
	  Map<String, Object> parameters = new HashMap<String, Object>();	
	  User user = CommonUtil.getLoggedInUser();
	  
	  String userId=user.getId();
	  List<String> groupIds=user.getGroupIds();
	  List<String> roleIds = user.getRoleIds();
	  String departmentId = user.getDepartment().toString();
	  	if(StringUtil.isEmptyString(userId)){
		  throw new BpmException("Querying with invalid or empty user id");
	    }
	  	if(!StringUtil.isEmptyString(task.id)){
		  parameters.put("taskId", task.id);
	    }
	  	if(!StringUtil.isEmptyString(userId)){
	  		parameters.put("userId", userId);
		    }
	  	if(!StringUtil.isEmptyString(departmentId)){
			  parameters.put("departmentId", departmentId);
		    }	  	
	    
	    if(!StringUtil.isEmptyString(type)){
	    	parameters.put("type", type);
	    }
	    if(roleIds!=null && roleIds.size()>0){
	    	parameters.put("roleIds", roleIds);
	    }
	    if(groupIds!=null && groupIds.size()>0){
	    	parameters.put("groupIds", groupIds);
	    }
	    parameters.put("signOffType",String.valueOf(task.getSignOffType()));
	    List results = getDbSqlSession().selectList("selectNextIdentityLink", parameters);
	    if(results!=null && results.size()>0){
	    	
	    	return (IdentityLinkEntity) results.get(0);
	    }
	    return null;
	    
  }
  
public IdentityLinkEntity findNextIdentityLinkByOrder(IdentityLinkEntity identityLinkEntity){

	  Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put("id", identityLinkEntity.getId());
	    parameters.put("taskId", identityLinkEntity.taskId);
	    parameters.put("userId", identityLinkEntity.userId);
	    parameters.put("groupId", identityLinkEntity.groupId);
	    parameters.put("groupType", identityLinkEntity.groupType);
	    parameters.put("type", TaskRole.ORGANIZER.getName());
	    parameters.put("order",String.valueOf(identityLinkEntity.getOrder()));
	    parameters.put("parentId",identityLinkEntity.getParentId());
	    List results = getDbSqlSession().selectList("selectNextIdentityLinkByOrder", parameters);
	    if(results!=null && results.size()>0){
	    	identityLinkEntity =(IdentityLinkEntity) results.get(0);
	    	return (IdentityLinkEntity) results.get(0);
	    }
	    return null;
	    
}
  
  public IdentityLink findIdentityLinksForUser(String userId, String type){
	  Map<String, String> parameters = new HashMap<String, String>();	    
	    parameters.put("userId", userId);
	    parameters.put("type", type);
	    List results = getDbSqlSession().selectList("selectIdentityLinkByUserGroupAndType", parameters);
	    if(results!=null && results.size()>0){
	    	return (IdentityLinkEntity) results.get(0);
	    }
	    return null;
  }
  
  public List<IdentityLink> findIdentityLinksForUser(String taskId, String userId, List<String> groupIds, String type){
	  Map<String, Object> parameters = new HashMap<String, Object>();
	  	if(StringUtil.isEmptyString(userId)){
		  throw new BpmException("Querying with invalid or empty user id");
	    }
	  	if(!StringUtil.isEmptyString(taskId)){
		  parameters.put("taskId", taskId);
	    }	  	
	    parameters.put("userId", userId);
	    if(!StringUtil.isEmptyString(type)){
	    	parameters.put("type", type);
	    }
	    if(groupIds!=null && groupIds.size()>0){
	    	parameters.put("groupIds", groupIds);
	    }
	    return getDbSqlSession().selectList("selectIdentityLinksByTaskAndUserOrGroups", parameters);	    
  }
  
	public List<IdentityLink> findIdentityLinksApplicableForUser(String taskId, User user, String type) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String departmentId = null;
		String userId = user.getId();
		List<String> groupIds = user.getGroupIds();
		List<String> roleIds = user.getRoleIds();
		if (user.getDepartment() != null) {
			departmentId = user.getDepartment().toString();
		}
		if (StringUtil.isEmptyString(userId)) {
			throw new BpmException("Querying with invalid or empty user id");
		}
		if (!StringUtil.isEmptyString(taskId)) {
			parameters.put("taskId", taskId);
		}
		if (!StringUtil.isEmptyString(userId)) {
			parameters.put("userId", userId);
		}
		if (!StringUtil.isEmptyString(departmentId)) {
			parameters.put("departmentId", departmentId);
		}

		if (!StringUtil.isEmptyString(type)) {
			parameters.put("type", type);
		}
		if (roleIds != null && roleIds.size() > 0) {
			parameters.put("roleIds", roleIds);
		}
		if (groupIds != null && groupIds.size() > 0) {
			parameters.put("groupIds", groupIds);
		}
		return getDbSqlSession().selectList("selectIdentityLinksByTaskAndUserOrGroups", parameters);
	}
  
  public List<IdentityLink> findIdentityLinksApplicableForUserExcludingTypes(String taskId, User user,  List<String> excludeTypes){
	  Map<String, Object> parameters = new HashMap<String, Object>();
	  String userId=user.getId();
	  List<String>groupIds=user.getGroupIds();
	  List<String>roleIds = user.getRoleIds();
	  String departmentId = null;
	  if(user.getDepartment() != null){
		  departmentId = user.getDepartment().toString();  
	  }
	  	if(StringUtil.isEmptyString(userId)){
		  throw new BpmException("Querying with invalid or empty user id");
	    }
	  	if(!StringUtil.isEmptyString(taskId)){
		  parameters.put("taskId", taskId);
	    }
	  	if(!StringUtil.isEmptyString(userId)){
	  		parameters.put("userId", userId);
		    }
	  	if(!StringUtil.isEmptyString(departmentId)){
			  parameters.put("departmentId", departmentId);
		    }	  	
	    
	    if(!StringUtil.isEmptyString(excludeTypes)){
	    	parameters.put("excludeTypes", excludeTypes);
	    }
	    if(roleIds!=null && roleIds.size()>0){
	    	parameters.put("roleIds", roleIds);
	    }
	    if(groupIds!=null && groupIds.size()>0){
	    	parameters.put("groupIds", groupIds);
	    }
	    return getDbSqlSession().selectList("selectIdentityLinksByTaskAndUserOrGroupsExcType", parameters);	    
  }
  
  public List<IdentityLink> findIdentityLinksByUsersAndType(String taskId, List<String> userIds, String type){
	  Map<String, Object> parameters = new HashMap<String, Object>();
	  	if(userIds==null||userIds.size()==0){
		  throw new BpmException("Querying with invalid or empty user ids");
	    }
	  	if(!StringUtil.isEmptyString(taskId)){
		  parameters.put("taskId", taskId);
	    }	  	
	    parameters.put("userIds", userIds);
	    if(!StringUtil.isEmptyString(type)){
	    	parameters.put("type", type);
	    }	    
	    return getDbSqlSession().selectList("selectIdentityLinksByUsersAndType", parameters);	    
  }

  public void deleteIdentityLinksByTaskId(String taskId) {
    List<IdentityLinkEntity> identityLinks = findIdentityLinksByTaskId(taskId);
    for (IdentityLinkEntity identityLink: identityLinks) {
      deleteIdentityLink(identityLink, true);
    }
  }
  
  public void deleteIdentityLinksByTaskAndUser(String taskId, String userId, String identityType,String loggedInUserId) {
	    List<IdentityLinkEntity> identityLinks = findIdentityLinkByTaskUserGroupAndType(taskId, userId, null, identityType);
	    if(loggedInUserId != null) {
		    for (IdentityLinkEntity identityLink: identityLinks) {
		    	if(identityLink.getUserId().equals(loggedInUserId)) {
		    		deleteIdentityLink(identityLink, true);
		    	}
		    }
	    }
  }
  
  public void deleteCandidateLinksByTaskAndUser(String taskId, String userId,String loggedInUserId) {
	    deleteIdentityLinksByTaskAndUser(taskId, userId, IdentityLinkType.CANDIDATE, loggedInUserId);
  }
  
  public void deleteCoordinatorLinksByTaskAndUser(String taskId, String userId,String loggedInUserId) {
	    deleteIdentityLinksByTaskAndUser(taskId, userId, IdentityLinkType.COORDINATOR, loggedInUserId);
  }
  
  public void deleteIdentityLinks(List<IdentityLinkEntity> identityLinks) {
	  for (IdentityLinkEntity identityLink: identityLinks) {
	      deleteIdentityLink(identityLink, true);
	   }
  }
  
  public void deleteIdentityLinksByProcDef(String processDefId) {
	  List<IdentityLinkEntity> identityLinks = findIdentityLinksByProcessDefinitionId(processDefId); 
	  for (IdentityLinkEntity identityLink: identityLinks) {
	      deleteIdentityLink(identityLink, true);
	    }
   // getDbSqlSession().delete("deleteIdentityLinkByProcDef", processDefId);
  }
  
  /**
   * Delete substitutes Identity Link when Organizer complete that task
   * @param parentId
   */
  public void deleteIdentityLinksByParentId(String parentId) {
	  List<IdentityLinkEntity> identityLinks = findIdentityLinksByParentId(parentId); 
	  for (IdentityLinkEntity identityLink: identityLinks) {
	      deleteIdentityLink(identityLink, true);
	    }
  }
  
  
  
  
  public List<IdentityLinkEntity> findCreatorIdentityLink(String taskId){
	  Map<String, Object> parameters = new HashMap<String, Object>();	    
	    parameters.put("taskId", taskId);
	    parameters.put("type", TaskRole.CREATOR);
	    try{
	    	    
	 	    return getDbSqlSession().selectList("selectCreatorIdentityLink", parameters);
	    }catch(Exception e){
	    	throw new BpmException("cannot get Identity Link for "+taskId, e);
	    }
	   
  }

	public List<IdentityLink> findIdentityLinksForTaskByInstanceId(String processInstanceId) {
		return getDbSqlSession().selectList("selectIdentityLinksByInstanceId", processInstanceId);
	}
}
