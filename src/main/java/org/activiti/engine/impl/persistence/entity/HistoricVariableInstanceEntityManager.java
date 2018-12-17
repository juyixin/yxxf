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

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.HistoricVariableInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.impl.persistence.AbstractManager;

import com.eazytec.exceptions.BpmException;


/**
 * @author Christian Lipphardt (camunda)
 */
public class HistoricVariableInstanceEntityManager extends AbstractManager {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void deleteHistoricVariableInstanceByProcessInstanceId(String historicProcessInstanceId) {
    if (getHistoryManager().isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {

      // Delete entries in DB
      List<HistoricVariableInstanceEntity> historicProcessVariables = (List) getDbSqlSession()
        .createHistoricVariableInstanceQuery()
        .processInstanceId(historicProcessInstanceId)
        .list();
      for (HistoricVariableInstanceEntity historicProcessVariable : historicProcessVariables) {
        historicProcessVariable.delete();
      }
      
      // Delete entries in Cache
      List<HistoricVariableInstanceEntity> cachedHistoricVariableInstances = getDbSqlSession().findInCache(HistoricVariableInstanceEntity.class);
      for (HistoricVariableInstanceEntity historicProcessVariable : cachedHistoricVariableInstances) {
        // Make sure we only delete the right ones (as we cannot make a proper query in the cache)
        if (historicProcessVariable.getProcessInstanceId().equals(historicProcessInstanceId)) {
          historicProcessVariable.delete();
        }
      }
    }
  }
  
  public List<HistoricVariableInstance> findHistoricVariableInstanceByProcessInstanceId(String historicProcessInstanceId) {
	    //if (getHistoryManager().isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
	      return (List) getDbSqlSession()
	        .createHistoricVariableInstanceQuery()
	        .processInstanceId(historicProcessInstanceId)
	        .list();
	    //}
	  }
  
  public List<HistoricVariableInstance> findHistoricVariableInstanceByProcessInstanceIds(List<String> historicProcessInstanceIds) {	    //if (getHistoryManager().isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
	      return (List) getDbSqlSession()
	        .createHistoricVariableInstanceQuery()
	        .processInstanceIds(historicProcessInstanceIds)
	        .list();	     
	    //}
	  }
  
  public List<HistoricVariableInstance> findPrevHistoricVariableInstancesByInstanceId(Map<String, Object>parameters) {
	    //if (getHistoryManager().isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
	  	  if(!parameters.containsKey("processInstanceId")){
	  		  throw new BpmException("No process instance id provided");
	  	  }	      
	      return (List<HistoricVariableInstance>) getDbSqlSession().
	    		  selectList("selectPreviousHistoricVariables", parameters);	     
	    //}
	  }
  
  public long findHistoricVariableInstanceCountByQueryCriteria(HistoricVariableInstanceQueryImpl historicProcessVariableQuery) {
    return (Long) getDbSqlSession().selectOne("selectHistoricVariableInstanceCountByQueryCriteria", historicProcessVariableQuery);
  }

  @SuppressWarnings("unchecked")
  public List<HistoricVariableInstance> findHistoricVariableInstancesByQueryCriteria(HistoricVariableInstanceQueryImpl historicProcessVariableQuery, Page page) {
    return getDbSqlSession().selectList("selectHistoricVariableInstanceByQueryCriteria", historicProcessVariableQuery, page);
  }

  public HistoricVariableInstanceEntity findHistoricVariableInstanceByVariableInstanceId(String variableInstanceId) {
    return (HistoricVariableInstanceEntity) getDbSqlSession().selectOne("selectHistoricVariableInstanceByVariableInstanceId", variableInstanceId);
  }

  public void deleteHistoricVariableInstancesByTaskId(String taskId) {
    if (getHistoryManager().isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricVariableInstanceQueryImpl historicProcessVariableQuery = 
        (HistoricVariableInstanceQueryImpl) new HistoricVariableInstanceQueryImpl().taskId(taskId);
      List<HistoricVariableInstance> historicProcessVariables = historicProcessVariableQuery.list();
      for(HistoricVariableInstance historicProcessVariable : historicProcessVariables) {
        ((HistoricVariableInstanceEntity) historicProcessVariable).delete();
      }
    }
  }
  
  public List<HistoricVariableInstance> findHistoricVariableInstancesByTaskId(String taskId) {
	      HistoricVariableInstanceQueryImpl historicProcessVariableQuery = 
	        (HistoricVariableInstanceQueryImpl) new HistoricVariableInstanceQueryImpl().taskId(taskId);
	     return historicProcessVariableQuery.list();
	  }
}
