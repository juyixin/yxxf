package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * @author Vinoth
 * 
 */
@Entity
@Table(name = "DMS_DOCUMENT_PERMISSION")
public class DocumentPermission {
	
	private String roleName;
	
	private boolean canDownload;
	
	private boolean canEdit;
	
	private Document document;
	
	private String id;
	
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

	@Column(name = "CAN_DOWNLOAD")
	public boolean getCanDownload() {
		return canDownload;
	}

	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	@Column(name = "CAN_EDIT")
	public boolean getCanEdit() {
		return canEdit;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	@ManyToOne
    @JoinColumn(name="DOCUMENT_ID",
                insertable=false, updatable=false,
                nullable=true)
	public Document getDocument() {
		return document;
	}



}
