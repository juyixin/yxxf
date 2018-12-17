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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.HistoricTaskInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;


/**
 * @author Tom Baeyens
 */
public class HistoricTaskInstanceEntityManager extends AbstractManager {

  @SuppressWarnings("unchecked")
  public void deleteHistoricTaskInstancesByProcessInstanceId(String processInstanceId) {
    if (getHistoryManager().isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      List<String> taskInstanceIds = (List<String>) getDbSqlSession().selectList("selectHistoricTaskInstanceIdsByProcessInstanceId", processInstanceId);
      for (String taskInstanceId: taskInstanceIds) {
        deleteHistoricTaskInstanceById(taskInstanceId);
      }
    }
  }

  public long findHistoricTaskInstanceCountByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
    if (getHistoryManager().isHistoryEnabled()) {
      return (Long) getDbSqlSession().selectOne("selectHistoricTaskInstanceCountByQueryCriteria", historicTaskInstanceQuery);
    }
    return 0;
  }

  public String getPreviousTaskAssignee(String processInstanceId){

	  Map<String, String> parameters = new HashMap<String, String>();
	  parameters.put("processInstanceId", processInstanceId);
	  List results = getDbSqlSession().selectList("selectPreviousHistoricTaskInstance", parameters);	  
	  if(results != null && results.size()>0 ){
		  HistoricTaskInstanceEntity historicTaskInstanceEntity =(HistoricTaskInstanceEntity) results.get(0);
		  if(historicTaskInstanceEntity != null && historicTaskInstanceEntity.getAssignee() != null){
			  return historicTaskInstanceEntity.getAssignee().toString();
		  }
	   }
	   return null;
  }
  
  @SuppressWarnings("unchecked")
  public String getPreviousTaskId( String insId) {
	  Map<String, String> parameters = new HashMap<String, String>();
	  parameters.put("id", insId);
	  List results = getDbSqlSession().selectList("selectPreviousTaskIdByInsId", parameters);
	  if(results != null && results.size()>0 ){
		  HistoricTaskInstanceEntity historicTaskInstanceEntity =(HistoricTaskInstanceEntity) results.get(0);
			  return historicTaskInstanceEntity.getId();
	   }
	   return null;
  }
  
  @SuppressWarnings("unchecked")
  public HistoricTaskInstance getPreviousTaskInstace( String insId) {
	  Map<String, String> parameters = new HashMap<String, String>();
	  parameters.put("id", insId);
	  List results = getDbSqlSession().selectList("selectPreviousTaskIdByInsId", parameters);
	  if(results != null && results.size()>0 ){
		  HistoricTaskInstanceEntity historicTaskInstanceEntity =(HistoricTaskInstanceEntity) results.get(0);
			  return historicTaskInstanceEntity;
	   }
	   return null;
  }
  
  public String getLastTaskAssignee(String processInstanceId,String taskDefinitionKey){
	  Map<String, String> parameters = new HashMap<String, String>();
	  parameters.put("processInstanceId", processInstanceId);
	  parameters.put("taskDefinitionKey", taskDefinitionKey);
	  List results = getDbSqlSession().selectList("selectLastHistoricTaskInstance", parameters);
	  if(results != null && results.size()>0 ){
		  HistoricTaskInstanceEntity historicTaskInstanceEntity =(HistoricTaskInstanceEntity) results.get(0);
		  if(historicTaskInstanceEntity != null && historicTaskInstanceEntity.getAssignee() != null){
			  return historicTaskInstanceEntity.getAssignee().toString();
		  }
	   }
	   return null;
  }
  
  @SuppressWarnings("unchecked")
  public List<HistoricTaskInstance> findHistoricTaskInstancesByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery, Page page) {
    if (getHistoryManager().isHistoryEnabled()) {
      return getDbSqlSession().selectList("selectHistoricTaskInstancesByQueryCriteria", historicTaskInstanceQuery, page);
    }
    return Collections.EMPTY_LIST;
  }
  
  public HistoricTaskInstanceEntity findHistoricTaskInstanceById(String taskId) {
    if (taskId == null) {
      throw new ActivitiException("Invalid historic task id : null");
    }
    if (getHistoryManager().isHistoryEnabled()) {
      return (HistoricTaskInstanceEntity) getDbSqlSession().selectOne("selectHistoricTaskInstance", taskId);
    }
    return null;
  }
  
  public void deleteHistoricTaskInstanceById(String taskId) {
    if (getHistoryManager().isHistoryEnabled()) {
      HistoricTaskInstanceEntity historicTaskInstance = findHistoricTaskInstanceById(taskId);
      if(historicTaskInstance!=null) {
        CommandContext commandContext = Context.getCommandContext();
        
        commandContext
          .getHistoricDetailEntityManager()
          .deleteHistoricDetailsByTaskId(taskId);

        commandContext
          .getHistoricVariableInstanceEntityManager()
          .deleteHistoricVariableInstancesByTaskId(taskId);

        commandContext
          .getCommentEntityManager()
          .deleteCommentsByTaskId(taskId);
        
        commandContext
          .getAttachmentEntityManager()
          .deleteAttachmentsByTaskId(taskId);
      
        getDbSqlSession().delete(historicTaskInstance);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public List<HistoricTaskInstance> findHistoricTaskInstancesByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
    return getDbSqlSession().selectListWithRawParameter("selectHistoricTaskInstanceByNativeQuery", parameterMap, firstResult, maxResults);
  }

  public long findHistoricTaskInstanceCountByNativeQuery(Map<String, Object> parameterMap) {
    return (Long) getDbSqlSession().selectOne("selectHistoricTaskInstanceCountByNativeQuery", parameterMap);
  }
  
	public List<String> getOrganizerIdentityLinksDetails(String processInstanceId,String taskDefinitionKey){
	  Map<String, String> parameters = new HashMap<String, String>();
	  parameters.put("processInstanceId", processInstanceId);
	  parameters.put("taskDefinitionKey", taskDefinitionKey);
	  List results = getDbSqlSession().selectList("selectHistoricTaskInstanceByTaskDefinitionKey", parameters);
	  List<String> organizerDetails = new ArrayList<String>();
	  if(results != null && results.size()>0 ){
		  HistoricTaskInstanceEntity historicTaskInstanceEntity =(HistoricTaskInstanceEntity) results.get(0);
		  if(historicTaskInstanceEntity != null) {
			  List<HistoricIdentityLinkEntity> indentityLinks = (List<HistoricIdentityLinkEntity>)getDbSqlSession().selectList("selectHistoricIdentityLinksByTaskId", historicTaskInstanceEntity.getId());
			  String organizerDetail = null;
			  for(HistoricIdentityLinkEntity indentityLink : indentityLinks){
				   if(indentityLink.getType().equalsIgnoreCase(IdentityLinkType.ORGANIZER)){
					   organizerDetail =  indentityLink.getUserId() +"-"+ indentityLink.getOrder()+ "-false-false";
					   organizerDetails.add(organizerDetail);
				   }
			  }
		  }
	  }
	  return organizerDetails;
	}
	
	
	@SuppressWarnings("unchecked")
	  public List<Task> selectPreviousTaskIdByTaskId( Map<String, String> parameters) {
	    return getDbSqlSession().selectList("selectPreviousTaskIdByTaskId", parameters);
	  }
	
	@SuppressWarnings("unchecked")
	  public List<HistoricIdentityLinkEntity>  getHistoricIdentityLinksByTaskId(HistoricTaskInstance historicTask) {
		 return (List<HistoricIdentityLinkEntity>)getDbSqlSession().selectList("selectHistoricIdentityLinksByTaskId", historicTask.getId());
	  }
	
	@SuppressWarnings("unchecked")
	  public List<HistoricIdentityLinkEntity>  getHistoricIdentityLinksByProcessInstanceId(String processInstanceId) {
		 return (List<HistoricIdentityLinkEntity>)getDbSqlSession().selectList("selectHistoricIdentityLinksByProcessInstanceId", processInstanceId);
	  }

	// updating historic task instance values to active state
	public void updateHistoricTaskInstance(HistoricTaskInstanceEntity historicTaskInstanceEntity) {
		getDbSqlSession().update(historicTaskInstanceEntity);
		
	}
	
	  @SuppressWarnings("unchecked")
	  public List<HistoricTaskInstance> findLastCompletedTaskByProcessInstanceId(String processInstanceId, String taskName) {
		  Map<String, String> parameters = new HashMap<String, String>();
		  parameters.put("processInstanceId", processInstanceId);
		  parameters.put("taskName", taskName);
	      return getDbSqlSession().selectList("selectLastCompletedTaskByProcessInstanceId", parameters);
	  }

	public List<String> getHistoricOrganizers(String taskDefinitionKey) {
		  Map<String, String> parameters = new HashMap<String, String>();
		  parameters.put("taskDefinitionKey", taskDefinitionKey);
		  List<String> userId = (List<String>) getDbSqlSession().selectList("selectHistoricOrganizers", parameters);
		  return userId;
	}
}
