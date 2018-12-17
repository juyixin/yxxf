package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Vinoth
 * 
 */
@Entity
@Table(name = "DMS_FOLDER_PERMISSION")
public class FolderPermission {

	private String id;
	
	private String roleName="";
	
	private boolean canAccess=false;
	
	private Folder folder;
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
	public String getId() {
		return id;
	}

	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "CAN_ACCESS")
	public boolean getCanAccess() {
		return canAccess;
	}

	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "FOLDER_ID", nullable = false)
	public Folder getFolder() {
		return folder;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("roleName=").append(roleName);
		sb.append(", canAccess=").append(canAccess);
		sb.append(", id=").append(id);
		sb.append("}");
		return sb.toString();
	}
}
