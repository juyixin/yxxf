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

import com.eazytec.bpm.engine.task.impl.TransactorIdentityLinkEntity;


/**
 * @author madan
 */
public class TransactorIdentityLinkEntityManager extends AbstractManager {

  public void deleteIdentityLink(TransactorIdentityLinkEntity identityLink) {
    getDbSqlSession().delete(identityLink);
  }
  
  @SuppressWarnings("unchecked")
  public List<TransactorIdentityLinkEntity> findIdentityLinksByTaskId(String taskId) {
    return getDbSqlSession().selectList("selectTransactorIdentityLinksByTask", taskId);
  }
  
  @SuppressWarnings("unchecked")
  public List<TransactorIdentityLinkEntity> findIdentityLinksByProcessDefinitionId(String processDefinitionId) {
    return getDbSqlSession().selectList("selectTransactorIdentityLinksByProcessDefinition", processDefinitionId);
  }

  @SuppressWarnings("unchecked")
  public List<TransactorIdentityLinkEntity> findIdentityLinkByTaskUserGroupAndType(String taskId, String userId, String groupId, String type) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("taskId", taskId);
    parameters.put("userId", userId);
    parameters.put("groupId", groupId);
    parameters.put("type", type);
    return getDbSqlSession().selectList("selectTransactorIdentityLinkByTaskUserGroupAndType", parameters);
  }
  
  @SuppressWarnings("unchecked")
  public List<TransactorIdentityLinkEntity> findIdentityLinkByProcessDefinitionUserAndGroup(String processDefinitionId, String userId, String groupId) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("processDefinitionId", processDefinitionId);
    parameters.put("userId", userId);
    parameters.put("groupId", groupId);
    return getDbSqlSession().selectList("selectTransactorIdentityLinkByProcessDefinitionUserAndGroup", parameters);
  }

  public void deleteIdentityLinksByTaskId(String taskId) {
    List<TransactorIdentityLinkEntity> identityLinks = findIdentityLinksByTaskId(taskId);
    for (TransactorIdentityLinkEntity identityLink: identityLinks) {
      deleteIdentityLink(identityLink);
    }
  }
  
  public void deleteIdentityLinksByProcDef(String processDefId) {
    getDbSqlSession().delete("deleteTransactorIdentityLinkByProcDef", processDefId);
  }
  
  public void updateIdentityLink(TransactorIdentityLinkEntity entity) {
	    getDbSqlSession().update(entity);
  }
  
}
