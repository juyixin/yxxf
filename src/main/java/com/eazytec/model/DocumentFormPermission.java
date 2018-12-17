package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "DMS_DOC_FORM_PERMISSION")
public class DocumentFormPermission {
	
	private String roleName;
	
	private boolean canRead;
	
	private boolean canCreate;
	
	private boolean canEdit;
	
	private boolean canDelete;
	
	private boolean canPrint;
	
	private DocumentForm documentForm;
	
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

	@Column(name = "CAN_READ")
	public boolean getCanRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	
	@Column(name = "CAN_CREATE")
	public boolean getCanCreate() {
		return canCreate;
	}

	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}
	@Column(name = "CAN_EDIT")
	public boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	@Column(name = "CAN_DELETE")
	public boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	@Column(name = "CAN_PRINT")
	public boolean getCanPrint() {
		return canPrint;
	}

	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}

	public void setDocumentForm(DocumentForm documentForm) {
		this.documentForm = documentForm;
	}
	
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "DOCUMENTFORM_ID", nullable = false)
	public DocumentForm getDocumentForm() {
		return documentForm;
	}

	
}
