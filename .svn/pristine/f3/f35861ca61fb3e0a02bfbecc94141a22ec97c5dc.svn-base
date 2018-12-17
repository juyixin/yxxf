package com.eazytec.alxxgl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
/**
 * @author Vinoth
 * 
 */
@Entity
@Table(name = "YXXF_FOLDER")
public class AlxxbFolder  implements Serializable {
	private static final long serialVersionUID = -2927429339076624036L;
	
	private String id;
	private String path;
	private String createdBy;
	private String modifiedBy;
	private Date createdTime;
	private Date modifiedTime;
	private int permissions;
	private boolean hasChildren;
	private Set<Alxxb> documentForms = new HashSet<Alxxb>();
	private String name;
	private String owner;
	 private String parentFolder;
	
	public AlxxbFolder(){
	}
	
	public AlxxbFolder(String id,String name,String owner){
		this.name=name;
		this.id=id;
		this.owner=owner;
	}
	
	@Column(name = "HAS_CHILDREN", nullable = false)
	public boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(name = "PERMISSION", nullable = false, length = 50, columnDefinition = "int default 1")
	public int getPermissions() {
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	@Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "FOLDER_ID")
	public String getId() {
		return id;
	}

	public void setId(String uuid) {
		this.id = uuid;
	}
	
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	@Column(name = "MODIFIED_TIME")
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public void setAlxxb(Set<Alxxb> documentForms) {
		this.documentForms = documentForms;
	}
	
	@OneToMany(mappedBy = "folder",cascade={CascadeType.ALL})
	public Set<Alxxb> getAlxxbs() {
		return documentForms;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	@Column(name = "OWNER")
	public String getOwner() {
		return owner;
	}
	
	 
	/*public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("path=").append(path);
		sb.append(", permissions=").append(permissions);
		sb.append(", createdTime=").append(createdTime==null?null:createdTime.getTime());
		sb.append(", hasChildren=").append(hasChildren);
		sb.append(", id=").append(id);
		sb.append("}");
		return sb.toString();
	}*/
	
	/**
	 * @return the parentFolderId
	 */
	@Column(name="PARENT_FOLDER")
	public String getParentFolder() {
		return parentFolder;
	}

	/**
	 * @param parentFolder the parentFolder to set
	 */
	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlxxbFolder)) {
            return false;
        }

        final AlxxbFolder folder = (AlxxbFolder) o;

        return !(id != null ? !id.equals(folder.getId()) : folder.getId() != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.id)
                .toString();
    }

}
