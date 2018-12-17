package com.eazytec.alxxgl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Vinoth
 * 
 */
@Entity
@Table(name = "YXXF_ALXXB_DOCUMENT")
public class AlxxbDocument implements Serializable {
	private static final long serialVersionUID = 4453338766237619444L;
	
	private String path;
	private String name = "";
	private String createdBy;
	private String documentformid;
	private Date createdTime;
	private Date modifiedTime;
	private String mimeType;
	private int permissions;
	private String id;
	private Alxxb alxxb;
	private byte[] file;

	@Column(name = "MODIFIED_TIME")
	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date lastModified) {
		this.modifiedTime = lastModified;
	}
	@Column(name = "DOCUMENTFORM_ID")
	public String getDocumentformid() {
		return documentformid;
	}

	public void setDocumentformid(String documentformid) {
		this.documentformid = documentformid;
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
	@Column(name = "MIME_TYPE")
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	/*@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}*/
	
	@Column(name = "PERMISSION", nullable = false, length = 50, columnDefinition = "int default 1")
	public int getPermissions() {
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "DOCUMENT_ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAlxxb(Alxxb alxxb) {
		this.alxxb = alxxb;
	}
/*	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "DOCUMENTFORM_ID", nullable = false)*/
	@ManyToOne
    @JoinColumn(name="DOCUMENTFORM_ID",
                insertable=false, updatable=false,
                nullable=true)
	public Alxxb getAlxxb() {
		return alxxb;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Transient
	public byte[] getFile() {
		return file;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("path=").append(path);
		sb.append(", name=").append(name);
		//sb.append(", description=").append(description);
		sb.append(", mimeType=").append(mimeType);
		sb.append(", createdBy=").append(createdBy);
		sb.append(", permissions=").append(permissions);
		sb.append(", createdTime=").append(createdTime==null?null:createdTime.getTime());
		sb.append(", modifiedTime=").append(modifiedTime==null?null:modifiedTime.getTime());
		sb.append(", id=").append(id);
		sb.append("}");
		return sb.toString();
	}
}
