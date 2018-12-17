package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import com.eazytec.util.DateUtil;

/**
 * @author Vinoth
 * 
 */
@Entity
@Table(name = "DMS_DOCUMENTFORM")
public class DocumentForm implements Serializable {
	private static final long serialVersionUID = 4453338766237619444L;

	private String name = "";
	private String content = "";
	private String createdBy;
	private String updatedBy;
	private Date createdTime;
	private Date modifiedTime;
	private int permissions;
	private String id;
	private Set<Document> documents = new HashSet<Document>();
	private List<MultipartFile> files;
	private Folder folder;
	private Set<DMSUserPermission> dmsUserPermission= new HashSet<DMSUserPermission>();
	private Set<DMSRolePermission> dmsRolePermission= new HashSet<DMSRolePermission>();
	private Set<DMSGroupPermission> dmsGroupPermission= new HashSet<DMSGroupPermission>();
	private PermissionDTO permissionDTO;
	
	public DocumentForm(String id,String name, String createdBy, String updatedBy,
			Date createdTime) {
		this.name = name;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdTime = createdTime;
		this.id = id;
	}

	public DocumentForm(){
		
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CONTENT", length=8000)  @Lob
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "PERMISSION", nullable = false, length = 50)
	public int getPermissions() {
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "DOCUMENTFORM_ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "documentForm",cascade=javax.persistence.CascadeType.PERSIST)
	@OneToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    @JoinColumn(name="DOCUMENTFORM_ID")
	public Set<Document> getDocuments() {
		return documents;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "MODIFIED_TIME")
	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "UPDATEDBY_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	@Transient
	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	
	@Transient
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}


	@ManyToOne
    @JoinColumn(name="FOLDER_ID")
	public Folder getFolder() {
		return folder;
	}

	/**
	 * @return the dmsUserPermission
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "documentForm",cascade=CascadeType.ALL)
	public Set<DMSUserPermission> getDmsUserPermission() {
		return dmsUserPermission;
	}

	/**
	 * @param dmsUserPermission the dmsUserPermission to set
	 */
	public void setDmsUserPermission(Set<DMSUserPermission> dmsUserPermission) {
		this.dmsUserPermission = dmsUserPermission;
	}

	/**
	 * @return the dmsRolePermission
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "documentForm",cascade=CascadeType.ALL)
	public Set<DMSRolePermission> getDmsRolePermission() {
		return dmsRolePermission;
	}

	/**
	 * @param dmsRolePermission the dmsRolePermission to set
	 */
	public void setDmsRolePermission(Set<DMSRolePermission> dmsRolePermission) {
		this.dmsRolePermission = dmsRolePermission;
	}

	/**
	 * @return the dmsGroupPermission
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "documentForm",cascade=CascadeType.ALL)
	public Set<DMSGroupPermission> getDmsGroupPermission() {
		return dmsGroupPermission;
	}

	/**
	 * @param dmsGroupPermission the dmsGroupPermission to set
	 */
	public void setDmsGroupPermission(Set<DMSGroupPermission> dmsGroupPermission) {
		this.dmsGroupPermission = dmsGroupPermission;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("name=").append(name);
		sb.append(", createdBy=").append(createdBy);
		sb.append(", permissions=").append(permissions);
		sb.append(", createdTime=").append(
				createdTime == null ? null : createdTime.getTime());
		sb.append(", modifiedTime=").append(
				modifiedTime == null ? null : modifiedTime.getTime());
		sb.append(", id=").append(id);
		sb.append("}");
		return sb.toString();
	}
	
	 /**
     * Adds a user permission
     *
     * @param dmsUserPermission
     */
    public void addDMSUserPermission(DMSUserPermission dmsUserPermission) {
        getDmsUserPermission().add(dmsUserPermission);
    }
    
    /**
     * Adds a role permission
     *
     * @param dmsRolePermission
     */
    public void addDMSRolePermission(DMSRolePermission dmsRolePermission) {
        getDmsRolePermission().add(dmsRolePermission);
    }
    
    /**
     * Adds a group permission
     *
     * @param dmsGroupPermission
     */
    public void addDMSGroupPermission(DMSGroupPermission dmsGroupPermission) {
        getDmsGroupPermission().add(dmsGroupPermission);
    }

	public void setPermissionDTO(PermissionDTO permissionDTO) {
		this.permissionDTO = permissionDTO;
	}

	@Transient
	public PermissionDTO getPermissionDTO() {
		return permissionDTO;
	}
}
