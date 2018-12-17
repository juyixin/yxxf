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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.repository.Deployment;


/**
 * @author Tom Baeyens
 */
public class DeploymentEntity implements Serializable, Deployment, PersistentObject {

  private static final long serialVersionUID = 1L;
  
  protected String id;
  protected String name;
  protected String category;
  protected String description;
  protected Map<String, ResourceEntity> resources;
  protected Date deploymentTime;
  protected boolean validatingSchema = true;
  protected boolean isNew;
  protected String classificationId;
  protected boolean isSystemDefined;
  protected String modifiedUser;
  protected Integer orderNo;

public String getModifiedUser() {
	return modifiedUser;
}

public void setModifiedUser(String modifiedUser) {
	this.modifiedUser = modifiedUser;
}
public boolean isSystemDefined() {
	return isSystemDefined;
}

public void setIsSystemDefined(boolean isSystemDefined) {
	this.isSystemDefined = isSystemDefined;
}

public Integer getOrderNo() {
	return orderNo;
}

public void setOrderNo(Integer orderNo) {
	this.orderNo = orderNo;
}



/**
   * Will only be used during actual deployment to pass deployed artifacts (eg process definitions).
   * Will be null otherwise.
   */
  protected Map<Class<?>, List<Object>> deployedArtifacts;
  
  public ResourceEntity getResource(String resourceName) {
    return getResources().get(resourceName);
  }

  public void addResource(ResourceEntity resource) {
    if (resources==null) {
      resources = new HashMap<String, ResourceEntity>();
    }
    resources.put(resource.getName(), resource);
  }

  // lazy loading /////////////////////////////////////////////////////////////
  public Map<String, ResourceEntity> getResources() {
    if (resources==null && id!=null) {
      List<ResourceEntity> resourcesList = Context
        .getCommandContext()
        .getResourceEntityManager()
        .findResourcesByDeploymentId(id);
      resources = new HashMap<String, ResourceEntity>();
      for (ResourceEntity resource: resourcesList) {
        resources.put(resource.getName(), resource);
      }
    }
    return resources;
  }

  public Object getPersistentState() {
    // properties of this entity are immutable
    // so always the same value is returned
    // so never will an update be issued for a DeploymentEntity
    return DeploymentEntity.class;
  }
  
  // Deployed artifacts manipulation //////////////////////////////////////////
  
  public void addDeployedArtifact(Object deployedArtifact) {
    if (deployedArtifacts == null) {
      deployedArtifacts = new HashMap<Class<?>, List<Object>>();
    }
    
    Class<?> clazz = deployedArtifact.getClass();
    List<Object> artifacts = deployedArtifacts.get(clazz);
    if (artifacts == null) {
      artifacts = new ArrayList<Object>();
      deployedArtifacts.put(clazz, artifacts);
    }
    
    artifacts.add(deployedArtifact);
  }
  
  @SuppressWarnings("unchecked")
  public <T> List<T> getDeployedArtifacts(Class<T> clazz) {
    return (List<T>) deployedArtifacts.get(clazz);
  }

  // getters and setters //////////////////////////////////////////////////////

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setResources(Map<String, ResourceEntity> resources) {
    this.resources = resources;
  }
  
  public Date getDeploymentTime() {
    return deploymentTime;
  }
  
  public void setDeploymentTime(Date deploymentTime) {
    this.deploymentTime = deploymentTime;
  }

  public boolean isValidatingSchema() {
    return validatingSchema;
  }
  
  public void setValidatingSchema(boolean validatingSchema) {
    this.validatingSchema = validatingSchema;
  }

  public boolean isNew() {
    return isNew;
  }
  
  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}
  

public String getClassificationId() {
	return classificationId;
}

public void setClassificationId(String classificationId) {
	this.classificationId = classificationId;
}
  
}
