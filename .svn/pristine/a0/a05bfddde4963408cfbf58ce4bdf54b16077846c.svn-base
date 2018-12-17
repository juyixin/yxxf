package com.eazytec.alxxgl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import com.eazytec.model.BaseObject;
import com.eazytec.model.DMSGroupPermission;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DMSUserPermission;
import com.eazytec.model.Document;
import com.eazytec.model.Folder;
import com.eazytec.model.PermissionDTO;
import com.eazytec.util.DateUtil;

/**
 * @author easybpm
 * 
 */

@Entity
@Table(name = "yxxf_alxxb")
public class Alxxb  extends BaseObject{
	private static final long serialVersionUID = 1L;
	private String id;
	private String dsr;	
	private String allx;
	private String bz;
	private List<MultipartFile> files;
	private Folder folder;
	private Date createdTime;
	private Date lastModifyTime;
	private String isActive;
	private List<AlxxbDocument> documents = new ArrayList<AlxxbDocument>();
														 
	private String name ;
	private String content  ;
	
	public Alxxb(String id,String name, String dsr, Date createdTime) {
		this.name = name;
		this.dsr = dsr;
		this.createdTime = createdTime;
		this.id = id;
	}
	public Alxxb(){
		
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
	
	@OneToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    @JoinColumn(name="DOCUMENTFORM_ID")
	public List<AlxxbDocument> getAlxxbDocument() {
		return documents;
	}
	public void setAlxxbDocument(List<AlxxbDocument> documents) {
		this.documents = documents;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DSR")
	public String getDsr() {
		return dsr;
	}
	public void setDsr(String dsr) {
		this.dsr = dsr;
	}
	
	@Column(name = "ALLX")
	public String getAllx() {
		return allx;
	}
	public void setAllx(String allx) {
		this.allx = allx;
	}
	
	@Column(name = "CONTENT" , length=8000)  @Lob
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "BZ")
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	@Transient
	public List<MultipartFile> getFiles() {
		return files;
	}
	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
	@ManyToOne
    @JoinColumn(name="FOLDER_ID")
	public Folder getFolder() {
		return folder;
	}
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	
	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	@Column(name = "LAST_MODIFY_TIME")
	public Date getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	@Column(name = "IS_ACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@Transient
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("name=").append(name);
		sb.append(", allx=").append(allx);
		sb.append(", dsr=").append(dsr);
		sb.append(", createdTime=").append(
				createdTime == null ? null : createdTime.getTime());
		sb.append(", lastmodifyTime=").append(
				lastModifyTime == null ? null : lastModifyTime.getTime());
		sb.append(", id=").append(id);
		sb.append("}");
		return sb.toString();
	}
	@Override
	public boolean equals(Object o) {
		return false;
	}
	@Override
	public int hashCode() {
		return 0;
	}
	
 
	
}
