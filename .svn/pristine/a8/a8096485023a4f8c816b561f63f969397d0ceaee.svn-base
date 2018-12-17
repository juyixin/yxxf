package com.eazytec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.activiti.engine.impl.db.HasRevision;

@Entity
@Table(name = "ACT_RE_PROCDEF")
@XmlRootElement
public class Process implements Serializable, HasRevision {
    private static final long serialVersionUID = 3644859655330969141L;
    private String id;
    private int revision;
    private String category;
    private String name;
    private String key;
    private String deploymentId;
    private String resourceName;
    private String diagramResourceName;
    private Integer version;
    private Integer suspensionState;
    private String description;
    private String classificationId;
    private boolean hasStartFormKey = false;
    private boolean isActiveVersion = false;
    private boolean isSystemDefined = false;
    private String modifiedUser;

    @Column(name = "MODIFIED_USER", length = 100)
    public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	@Column(name = "IS_SYSTEM_DEFINED_")
    public boolean getIsSystemDefined() {
		return isSystemDefined;
	}

	public void setIsSystemDefined(boolean isSystemDefined) {
		this.isSystemDefined = isSystemDefined;
	}


	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Process() {
    }

    @Id
    @Column(name = "ID_")
    public String getId() {
        return id;
    }

    @Transient
    public int getRevision() {
        return revision;
    }

    @Column(name = "RESOURCE_NAME_", length = 100)
    public String getResourceName() {
        return resourceName;
    }   

    public void setId(String id) {
        this.id = id;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }    

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }   

    @Column(name = "REV_", nullable = false)
    public int getRevisionNext() {
        return revision + 1;
    }

    public void setRevisionNext(int revision) {
        this.revision = revision + 1;
    } 

    @Column(name = "VERSION_", columnDefinition = "int default 1")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }    

    @Column(name = "DESCRIPTION_",length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    
    
    @Column(name = "CATEGORY_", length = 100)
    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "NAME_", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "KEY_", length = 100)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "DEPLOYMENT_ID_", length = 100)
	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	@Column(name = "DGRM_RESOURCE_NAME_", length = 100)
	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	@Column(name = "SUSPENSION_STATE_", columnDefinition = "int")
	public Integer getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(Integer suspensionState) {
		this.suspensionState = suspensionState;
	}

	@Column(name = "CLASSIFICATION_ID_", length = 100)
	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	@Column(name = "HAS_START_FORM_KEY_")
	public boolean isHasStartFormKey() {
		return hasStartFormKey;
	}

	public void setHasStartFormKey(boolean hasStartFormKey) {
		this.hasStartFormKey = hasStartFormKey;
	}

	@Column(name = "IS_ACTIVE_VERSION_")
	public boolean getIsActiveVersion() {
		return isActiveVersion;
	}

	public void setIsActiveVersion(boolean isActiveVersion) {
		this.isActiveVersion = isActiveVersion;
	}

}