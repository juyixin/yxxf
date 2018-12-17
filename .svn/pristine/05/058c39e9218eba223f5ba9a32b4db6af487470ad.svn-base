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
package org.activiti.engine.impl.cmd;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;


/**
 * @author Revathi
 */
public class GetDeploymentResourcesMapCmd implements Command<Map<String,Map<String,InputStream>>>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected List<String> deploymentIds;
  
  public GetDeploymentResourcesMapCmd(List<String> deploymentIds) {
    this.deploymentIds = deploymentIds;
  }

  public Map<String,Map<String,InputStream>> execute(CommandContext commandContext) {
	  Map<String,Map<String,InputStream>> inputStreamMap = new HashMap<String,Map<String,InputStream>>();
    if (deploymentIds== null) {
      throw new ActivitiException("deploymentIds is null");
    }
   List<ResourceEntity> resources = commandContext
      .getResourceEntityManager()
      .findResourcesByDeploymentIds(deploymentIds);
    if(resources == null) {
      throw new ActivitiException("no resources found for '" + deploymentIds + "'");
    }
    for (ResourceEntity resource : resources){
    	Map<String,InputStream> msp = new HashMap<String,InputStream>();
    	msp.put(resource.getName(), new ByteArrayInputStream(resource.getBytes()));
    	inputStreamMap.put(resource.getDeploymentId(),msp );
    }
    return inputStreamMap;
  }





}
