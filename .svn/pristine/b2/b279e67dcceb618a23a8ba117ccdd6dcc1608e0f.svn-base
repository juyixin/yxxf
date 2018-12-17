package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Ramachandran
 * 
 */
@Entity
@Table(name = "DMS_FILEIMPORTFORM")
public class FileImportForm implements Serializable {
	private static final long serialVersionUID = 4453338766237619444L;
	
	private String path;
	private String name = "";
	private String updatedBy;
	private String resourcePath;
	private String createdBy;
	private Date createdTime;
	private Date modifiedTime;
	private String id;
	private String mimeType;
	
	public FileImportForm(){
		
	}
	

	public FileImportForm(String id,String name,String path){
		this.id = id;
		this.name = name;
		this.resourcePath = path;
	}
	
	@Column(name = "MODIFIED_TIME")
	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date lastModified) {
		this.modifiedTime = lastModified;
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
	
	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "RESOURCE_PATH")
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("path=").append(path);
		sb.append(", name=").append(name);
		sb.append(", mimeType=").append(mimeType);
		sb.append(", resourcePath=").append(resourcePath);
		sb.append(", updatedBy=").append(updatedBy);
		sb.append(", createdBy=").append(createdBy);
		sb.append(", createdTime=").append(createdTime==null?null:createdTime.getTime());
		sb.append(", modifiedTime=").append(modifiedTime==null?null:modifiedTime.getTime());
		sb.append(", id=").append(id);
		sb.append("}");
		return sb.toString();
	}
	
}

