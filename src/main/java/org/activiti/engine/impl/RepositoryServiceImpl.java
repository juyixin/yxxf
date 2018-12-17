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

package org.activiti.engine.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cmd.ActivateProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.ActivateProcessInstanceCmd;
import org.activiti.engine.impl.cmd.AddEditorSourceExtraForModelCmd;
import org.activiti.engine.impl.cmd.AddEditorSourceForModelCmd;
import org.activiti.engine.impl.cmd.AddIdentityLinkForProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.ClearProcessDefinitionsFromCacheCmd;
import org.activiti.engine.impl.cmd.CreateModelCmd;
import org.activiti.engine.impl.cmd.DeleteCustomOperatingFunction;
import org.activiti.engine.impl.cmd.DeleteDeploymentCmd;
import org.activiti.engine.impl.cmd.DeleteIdentityLinkForProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.DeleteModelCmd;
import org.activiti.engine.impl.cmd.DeleteProcessDefinitionCacheCmd;
import org.activiti.engine.impl.cmd.DeleteProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.DeployCmd;
import org.activiti.engine.impl.cmd.DescribeEntityCmd;
import org.activiti.engine.impl.cmd.GetAllCustomOperatingFunctionCmd;
import org.activiti.engine.impl.cmd.GetCustomOperatingByIdCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramLayoutCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessModelCmd;
import org.activiti.engine.impl.cmd.GetDeploymentResourceCmd;
import org.activiti.engine.impl.cmd.GetDeploymentResourceEntityCmd;
import org.activiti.engine.impl.cmd.GetDeploymentResourceNamesCmd;
import org.activiti.engine.impl.cmd.GetDeploymentResourcesMapCmd;
import org.activiti.engine.impl.cmd.GetIdentityLinksForProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.GetModelCmd;
import org.activiti.engine.impl.cmd.GetModelEditorSourceCmd;
import org.activiti.engine.impl.cmd.GetModelEditorSourceExtraCmd;
import org.activiti.engine.impl.cmd.GetProcessDefinitionByKeyCmd;
import org.activiti.engine.impl.cmd.GetProcessDefinitionByUserCmd;
import org.activiti.engine.impl.cmd.SaveCustomOperatingFunctionCmd;
import org.activiti.engine.impl.cmd.SaveModelCmd;
import org.activiti.engine.impl.cmd.SuspendProcessDefinitionCmd;
import org.activiti.engine.impl.cmd.SuspendProcessInstanceCmd;
import org.activiti.engine.impl.cmd.UpdateProcessDefinitionVersionCmd;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.repository.DeploymentBuilderImpl;
import org.activiti.engine.impl.task.CustomOperatingFunction;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.DiagramLayout;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.IdentityLink;

import com.eazytec.exceptions.BpmException;


/**
 * @author Tom Baeyens
 * @author Falko Menge
 * @author Joram Barrez
 * @author Revathi
 * @author madan
 */
public class RepositoryServiceImpl extends ServiceImpl implements RepositoryService {

  public DeploymentBuilder createDeployment() {
    return new DeploymentBuilderImpl(this);
  }

  public Deployment deploy(DeploymentBuilderImpl deploymentBuilder) {
    return commandExecutor.execute(new DeployCmd<Deployment>(deploymentBuilder));
  }

  public void deleteDeployment(String deploymentId) {
    commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, false));
  }

  public void deleteDeploymentCascade(String deploymentId) {
    commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, true));
  }
  
  public void deleteDeployment(String deploymentId, boolean cascade) {
    commandExecutor.execute(new DeleteDeploymentCmd(deploymentId, cascade));
  }

  public ProcessDefinitionQuery createProcessDefinitionQuery() {
    return new ProcessDefinitionQueryImpl(commandExecutor);
  }
  
  public List<Map<String, Object>> createProcessDescribeQuery(List<ProcessDefinition> processDefinitions) {
		 return commandExecutor.execute(new DescribeEntityCmd(processDefinitions));
  }

  @SuppressWarnings("unchecked")
  public List<String> getDeploymentResourceNames(String deploymentId) {
    return commandExecutor.execute(new GetDeploymentResourceNamesCmd(deploymentId));
  }

  public InputStream getResourceAsStream(String deploymentId, String resourceName) {
    return commandExecutor.execute(new GetDeploymentResourceCmd(deploymentId, resourceName));
  }
  
  public String getSvgString(String deploymentId, String resourceName) {
	  ResourceEntity resource = commandExecutor.execute(new GetDeploymentResourceEntityCmd(deploymentId, resourceName)); 
	  return new String(resource.getSvgString());
  }
  
  
  public ResourceEntity getResourceAsEntity(String deploymentId, String resourceName) {
	    return commandExecutor.execute(new GetDeploymentResourceEntityCmd(deploymentId, resourceName));
  }
  
  public Map<String,Map<String,InputStream>> getResourceListAsStream(List<String> deploymentIds){
	  return commandExecutor.execute(new GetDeploymentResourcesMapCmd(deploymentIds));
  }
  
  public DeploymentQuery createDeploymentQuery() {
    return new DeploymentQueryImpl(commandExecutor);
  }

  public ProcessDefinition getProcessDefinition(String processDefinitionId) {
    return commandExecutor.execute(new GetDeploymentProcessDefinitionCmd(processDefinitionId));
  }
  
  public ReadOnlyProcessDefinition getDeployedProcessDefinition(String processDefinitionId) {
    return commandExecutor.execute(new GetDeploymentProcessDefinitionCmd(processDefinitionId));
  }

  public void suspendProcessDefinitionById(String processDefinitionId) {
    commandExecutor.execute(new SuspendProcessDefinitionCmd(processDefinitionId, null, false, null));
  }
  
  public void suspendProcessDefinitionById(String processDefinitionId, boolean suspendProcessInstances, Date suspensionDate) {
    commandExecutor.execute(new SuspendProcessDefinitionCmd(processDefinitionId, null, suspendProcessInstances, suspensionDate));
  }
  
  public void suspendProcessDefinitionByKey(String processDefinitionKey) {
    commandExecutor.execute(new SuspendProcessDefinitionCmd(null, processDefinitionKey, false, null));
  }

  public void suspendProcessDefinitionByKey(String processDefinitionKey, boolean suspendProcessInstances, Date suspensionDate) {
    commandExecutor.execute(new SuspendProcessDefinitionCmd(null, processDefinitionKey, suspendProcessInstances, suspensionDate));
  }
  
  public void suspendProcessDefinitionByIds(boolean suspendProcessInstances, Date suspensionDate,List<String> processDefinitionIds) {
	    commandExecutor.execute(new SuspendProcessDefinitionCmd(suspendProcessInstances, suspensionDate,processDefinitionIds));
  }
  
  public void suspendProcessDefinitionByIds(List<String> processDefinitionIds) {
	    commandExecutor.execute(new SuspendProcessDefinitionCmd(false,null, processDefinitionIds));
	  }
  
  public void activateProcessDefinitionById(String processDefinitionId) {
    commandExecutor.execute(new ActivateProcessDefinitionCmd(processDefinitionId, null, false, null));
  }
  
  public void activateProcessDefinitionById(String processDefinitionId, boolean activateProcessInstances, Date activationDate) {
    commandExecutor.execute(new ActivateProcessDefinitionCmd(processDefinitionId, null, activateProcessInstances, activationDate));
  }

  public void activateProcessDefinitionByKey(String processDefinitionKey) {
    commandExecutor.execute(new ActivateProcessDefinitionCmd(null, processDefinitionKey, false, null));
  }
  
  public void activateProcessDefinitionByKey(String processDefinitionKey, boolean activateProcessInstances, Date activationDate) {
    commandExecutor.execute(new ActivateProcessDefinitionCmd(null, processDefinitionKey, activateProcessInstances, activationDate));
  }
  
  public void activateProcessDefinitionByIds(List<String> processDefinitionIds) {
	    commandExecutor.execute(new ActivateProcessDefinitionCmd(false, null,processDefinitionIds));
  }
	  
  public void activateProcessDefinitionByIds(boolean activateProcessInstances, Date activationDate,List<String> processDefinitionIds) {
	    commandExecutor.execute(new ActivateProcessDefinitionCmd(activateProcessInstances, activationDate,processDefinitionIds));
  }
  
  public void activateProcessDefinitionVersionById(String processDefinitionId){
	  commandExecutor.execute(new UpdateProcessDefinitionVersionCmd(processDefinitionId,true));
  }
  
  public void inActivateProcessDefinitionVersionById(String processDefinitionId) {
	    commandExecutor.execute(new UpdateProcessDefinitionVersionCmd(processDefinitionId, false));
  }

  public InputStream getProcessModel(String processDefinitionId) {
    return commandExecutor.execute(new GetDeploymentProcessModelCmd(processDefinitionId));
  }

  public InputStream getProcessDiagram(String processDefinitionId) {
    return commandExecutor.execute(new GetDeploymentProcessDiagramCmd(processDefinitionId));
  }

  public DiagramLayout getProcessDiagramLayout(String processDefinitionId) {
    return commandExecutor.execute(new GetDeploymentProcessDiagramLayoutCmd(processDefinitionId));
  }
  
  public Model newModel() {
    return commandExecutor.execute(new CreateModelCmd());
  }

  public void saveModel(Model model) {
    commandExecutor.execute(new SaveModelCmd((ModelEntity) model));
  }

  public void deleteModel(String modelId) {
    commandExecutor.execute(new DeleteModelCmd(modelId));
  }
  
  public void addModelEditorSource(String modelId, byte[] bytes) {
    commandExecutor.execute(new AddEditorSourceForModelCmd(modelId, bytes));
  }
  
  public void addModelEditorSourceExtra(String modelId, byte[] bytes) {
    commandExecutor.execute(new AddEditorSourceExtraForModelCmd(modelId, bytes));
  }
  
  public ModelQuery createModelQuery() {
    return new ModelQueryImpl(commandExecutor);
  }
  
  public Model getModel(String modelId) {
    return commandExecutor.execute(new GetModelCmd(modelId));
  }
  
  public byte[] getModelEditorSource(String modelId) {
    return commandExecutor.execute(new GetModelEditorSourceCmd(modelId));
  }
  
  public byte[] getModelEditorSourceExtra(String modelId) {
    return commandExecutor.execute(new GetModelEditorSourceExtraCmd(modelId));
  }
  
  public void addCandidateStarterUser(String processDefinitionId, String userId) {
    commandExecutor.execute(new AddIdentityLinkForProcessDefinitionCmd(processDefinitionId, userId, null));
  }
  
  public void addCandidateStarterGroup(String processDefinitionId, String groupId) {
    commandExecutor.execute(new AddIdentityLinkForProcessDefinitionCmd(processDefinitionId, null, groupId));
  }
  
  public void deleteCandidateStarterGroup(String processDefinitionId, String groupId) {
    commandExecutor.execute(new DeleteIdentityLinkForProcessDefinitionCmd(processDefinitionId, null, groupId));
  }

  public void deleteCandidateStarterUser(String processDefinitionId, String userId) {
    commandExecutor.execute(new DeleteIdentityLinkForProcessDefinitionCmd(processDefinitionId, userId, null));
  }

  public List<IdentityLink> getIdentityLinksForProcessDefinition(String processDefinitionId) {
    return commandExecutor.execute(new GetIdentityLinksForProcessDefinitionCmd(processDefinitionId));
  }
  
  public List<ProcessDefinition> getProcessDefinitionByUser(String user) {
	    return commandExecutor.execute(new GetProcessDefinitionByUserCmd(user));
  }

  public List<ProcessDefinition> getProcessDefinitionByKey(String key) {
	    return commandExecutor.execute(new GetProcessDefinitionByKeyCmd(key));
  }
  public List<ProcessDefinition> getProcessDefinitionByKeys(List<String>ids) {
	    return commandExecutor.execute(new GetProcessDefinitionByKeyCmd(null,ids));
}
  public void deleteProcessDefinitionFromCache(String processDefinitionId)throws BpmException {
	    commandExecutor.execute(new DeleteProcessDefinitionCacheCmd(processDefinitionId));
  }
  public void deleteProcessDefinitionsFromCache(List<ProcessDefinition>processDefinitions)throws BpmException {
	    commandExecutor.execute(new DeleteProcessDefinitionCacheCmd(processDefinitions));
  }
  public void deleteProcessDefinitionByDefinitionId(String processDefinitionId, String deleteReason, boolean cascade)throws BpmException {
	    commandExecutor.execute(new DeleteProcessDefinitionCmd(processDefinitionId, deleteReason, cascade));
  }
  public void deleteProcessDefinitions(List<ProcessDefinition>processDefinitions, String deleteReason, boolean cascade)throws BpmException {
	    commandExecutor.execute(new DeleteProcessDefinitionCmd(processDefinitions, deleteReason, cascade));
  }
  public void suspendProcessInstanceById(String processInstnaceId){
	  commandExecutor.execute(new SuspendProcessInstanceCmd(processInstnaceId));	  
  }
  public void activateProcessInstanceById(String processInstanceId){
	  commandExecutor.execute(new ActivateProcessInstanceCmd(processInstanceId));
  }
  
  public void clearProcessDefinitionsFromCache(){
	  commandExecutor.execute(new ClearProcessDefinitionsFromCacheCmd());
  } 
  
  public List<CustomOperatingFunction> getAllCustomOperatingFunction(){
	  return commandExecutor.execute(new GetAllCustomOperatingFunctionCmd());
  }
  
  public CustomOperatingFunction saveCustomOperatingFunction(CustomOperatingFunction customOperatingFunction,boolean isCustomOperatingUpdate){
	  return commandExecutor.execute(new SaveCustomOperatingFunctionCmd(customOperatingFunction,isCustomOperatingUpdate));
  }
  
  public CustomOperatingFunction getCustomOperatingById(String customOperatingFunctionId){
	  return commandExecutor.execute(new GetCustomOperatingByIdCmd(customOperatingFunctionId));
  }
  
  public void deleteCustomFunctionById(CustomOperatingFunction customOperatingObj){
	  commandExecutor.execute(new DeleteCustomOperatingFunction(customOperatingObj));
  }
  
}
