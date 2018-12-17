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
 * @author mathi
 * 
 */

@Entity
@Table(name = "DMS_USER_PERMISSION")
public class DMSUserPermission {
	
	private String id;
	
	private DocumentForm documentForm;
	
	private Folder folder; 
	
	private String userName;
	
	private boolean canAccess;
	
	private boolean canRead;
	
	private boolean canCreate;
	
	private boolean canEdit;
	
	private boolean canDelete;
	
	private boolean canPrint;
	
	private boolean canDownload;
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public DMSUserPermission() {
    }
	
	public DMSUserPermission(boolean canRead, boolean canCreate,
			boolean canEdit, boolean canDelete, boolean canPrint,
			boolean canDownload) {
		this.canRead = canRead;
		this.canCreate = canCreate;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
		this.canPrint = canPrint;
		this.canDownload = canDownload;
	}


	public DMSUserPermission(boolean canAccess) {
		this.canAccess = canAccess;
	}
	
	@Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setDocumentForm(DocumentForm documentForm) {
		this.documentForm = documentForm;
	}
	
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "DOCUMENT_FORM")
	public DocumentForm getDocumentForm() {
		return documentForm;
	}
    
	/**
	 * @return the folder
	 */
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "FOLDER")
	public Folder getFolder() {
		return folder;
	}

	/**
	 * @param folder the folder to set
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the canAccess
	 */
	@Column(name = "CAN_ACCESS")
	public boolean isCanAccess() {
		return canAccess;
	}

	/**
	 * @param canAccess the canAccess to set
	 */
	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
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

	@Column(name = "CAN_DOWNLOAD")
	public boolean getCanDownload() {
		return canDownload;
	}

	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}
}
