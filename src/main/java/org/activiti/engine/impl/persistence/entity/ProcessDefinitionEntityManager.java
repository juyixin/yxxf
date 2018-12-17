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

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.ProcessDefinitionQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.event.MessageEventHandler;
import org.activiti.engine.impl.jobexecutor.TimerStartEventJobHandler;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;


/**
 * @author Tom Baeyens
 * @author Falko Menge
 * @author Saeid Mirzaei
 * @author madan
 * @author Revathi
 */
public class ProcessDefinitionEntityManager extends AbstractManager {

  public ProcessDefinitionEntity findLatestProcessDefinitionByKey(String processDefinitionKey) {
    return (ProcessDefinitionEntity) getDbSqlSession().selectOne("selectLatestProcessDefinitionByKey", processDefinitionKey);
  }

  public void deleteProcessDefinitionsByDeploymentId(String deploymentId) {
    getDbSqlSession().delete("deleteProcessDefinitionsByDeploymentId", deploymentId);
  }

  public ProcessDefinitionEntity findLatestProcessDefinitionById(String processDefinitionId) {
    return (ProcessDefinitionEntity) getDbSqlSession().selectOne("selectProcessDefinitionById", processDefinitionId);
  }
  
  @SuppressWarnings("unchecked")
  public List<ProcessDefinition> findProcessDefinitionsByQueryCriteria(ProcessDefinitionQueryImpl processDefinitionQuery, Page page) {
//    List<ProcessDefinition> processDefinitions = 
    return getDbSqlSession().selectList("selectProcessDefinitionsByQueryCriteria", processDefinitionQuery, page);

    //skipped this after discussion within the team
//    // retrieve process definitions from cache (http://jira.codehaus.org/browse/ACT-1020) to have all available information
//    ArrayList<ProcessDefinition> result = new ArrayList<ProcessDefinition>();
//    for (ProcessDefinition processDefinitionEntity : processDefinitions) {      
//      ProcessDefinitionEntity fullProcessDefinition = Context
//              .getProcessEngineConfiguration()
//              .getDeploymentCache().resolveProcessDefinition((ProcessDefinitionEntity)processDefinitionEntity);
//      result.add(fullProcessDefinition);
//    }
//    return result;
  }

  public long findProcessDefinitionCountByQueryCriteria(ProcessDefinitionQueryImpl processDefinitionQuery) {
    return (Long) getDbSqlSession().selectOne("selectProcessDefinitionCountByQueryCriteria", processDefinitionQuery);
  }
  
  public ProcessDefinitionEntity findProcessDefinitionByDeploymentAndKey(String deploymentId, String processDefinitionKey) {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("deploymentId", deploymentId);
    parameters.put("processDefinitionKey", processDefinitionKey);
    return (ProcessDefinitionEntity) getDbSqlSession().selectOne("selectProcessDefinitionByDeploymentAndKey", parameters);
  }

  public ProcessDefinition findProcessDefinitionByKeyAndVersion(String processDefinitionKey, Integer processDefinitionVersion) {
    ProcessDefinitionQueryImpl processDefinitionQuery = new ProcessDefinitionQueryImpl()
      .processDefinitionKey(processDefinitionKey)
      .processDefinitionVersion(processDefinitionVersion);
    List<ProcessDefinition> results = findProcessDefinitionsByQueryCriteria(processDefinitionQuery, null);
    if (results.size() == 1) {
      return results.get(0);
    } else if (results.size() > 1) {
      throw new ActivitiException("There are " + results.size() + " process definitions with key = '" + processDefinitionKey + "' and version = '" + processDefinitionVersion + "'.");
    }
    return null; 
  }
  
  public List<ProcessDefinition> findProcessDefinitionsStartableByUser(String user) {
    return  new ProcessDefinitionQueryImpl().startableByUser(user).list();
  }
  
	@SuppressWarnings("unchecked")
	public List<ProcessDefinition> findProcessDefinitionsByUser(String user) {
		return getDbSqlSession().selectList("selectProcessDefinitionByUser",
				user);
	}
	
	@SuppressWarnings("unchecked")
	public void updateProcessDefinition(ProcessDefinitionEntity processDefinitionEntity) {
		getDbSqlSession().update(processDefinitionEntity);
	}

	@SuppressWarnings("unchecked")
	public List<ProcessDefinition> findProcessDefinitionByKey(String processKey) {
		return getDbSqlSession().selectList("selectProcessDefinitionsByKey",
				processKey);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessDefinition> findActiveProcessDefinitionByKey(String processKey) {
		return getDbSqlSession().selectList("selectActiveProcessDefinitionsByKey",
				processKey);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessDefinitionEntity> findProcessDefinitionListByIds(List<String> processDefinitionIds) {
		return getDbSqlSession().selectList("selectProcessDefinitionListByIds",
				processDefinitionIds);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessDefinition> findProcessDefinitionByIds(List<String> processDefinitionIds) {
		return getDbSqlSession().selectList("selectProcessDefinitionListByIds",
				processDefinitionIds);
	}
  public List<User> findProcessDefinitionPotentialStarterUsers() {
    return null;
  }
  
  public List<Group> findProcessDefinitionPotentialStarterGroups() {
    return null;
  }
  
  /**
   * Deletes the process definition entity and the associated entities like instances, identities if needed.
   * 
   * @param processDefinitionId for the process definition
   * @param deleteReason for what the definition is deleted and instances deleted
   * @param cascade should associated instances running under should also be deleted
   * 
   * @author madan
   */
  public void deleteProcessDefinition(String processDefinitionId, String deleteReason, boolean cascade) {	    
    if (cascade) {
      // delete process instances
    	getProcessInstanceManager()
        .deleteProcessInstancesByProcessDefinition(processDefinitionId, deleteReason, cascade);	  
	}	 	      
    // remove related authorization parameters in IdentityLink table
	getIdentityLinkManager().deleteIdentityLinksByProcDef(processDefinitionId);
	
	ProcessDefinition processDefinition = findLatestProcessDefinitionById(processDefinitionId);
	      
	  // remove timer start events:
	  List<Job> timerStartJobs = Context.getCommandContext()
	    .getJobEntityManager()
	    .findJobsByConfiguration(TimerStartEventJobHandler.TYPE, processDefinition.getKey());
	  for (Job job : timerStartJobs) {
	    ((JobEntity)job).delete();        
	  }	      
      // remove message event subscriptions:
      List<EventSubscriptionEntity> findEventSubscriptionsByConfiguration = Context
        .getCommandContext()
        .getEventSubscriptionEntityManager()
        .findEventSubscriptionsByConfiguration(MessageEventHandler.EVENT_HANDLER_TYPE, processDefinition.getId());
      for (EventSubscriptionEntity eventSubscriptionEntity : findEventSubscriptionsByConfiguration) {
        eventSubscriptionEntity.delete();        
      }
      //Delete process reference from module table before delete process from process table
     // getDbSqlSession().delete("deleteProcessModuleById", processDefinitionId);
      getDbSqlSession().delete("deleteProcessDefinitionsById", processDefinitionId);
  }
  
  /**
   * Deletes all the process definitions and if needed the related associations like instances, identities.
   * 
   * @param processDefinitions list of definitions
   * @param deleteReason for deleting process definition
   * @param cascade should related instances running under should also be deleted
   * 
   * @author madan
   */
  public void deleteProcessDefinition(List<ProcessDefinition>processDefinitions, String deleteReason, boolean cascade) {	    
	   for (ProcessDefinition processDefinition : processDefinitions) {
		   String processDefinitionId = processDefinition.getId();
		   deleteProcessDefinition(processDefinitionId, deleteReason, cascade);		   
	   }		
  } 
  
  /**
   * Get all process definition list for given key list
   * @param processDefinitionIds
   * @return
   */
  @SuppressWarnings("unchecked")
	public List<ProcessDefinition> findProcessDefinitionByKeys(List<String> processDefinitionKeys) {
		return getDbSqlSession().selectList("selectProcessDefinitionListByKeys",
				processDefinitionKeys);
	}
}
