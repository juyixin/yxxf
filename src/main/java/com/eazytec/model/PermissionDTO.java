package com.eazytec.model;


public class PermissionDTO {

	private boolean canRead = false;

	private boolean canCreate = false;

	private boolean canEdit = false;

	private boolean canDelete = false;

	private boolean canPrint = false;

	private boolean canDownload = false;
	
	private boolean isPublic = false; // this permission is for documents. will be true until any permission for each document is given

	
	public PermissionDTO(boolean canRead, boolean canCreate, boolean canEdit,
			boolean canDelete, boolean canPrint, boolean canDownload) {
		super();
		this.canRead = canRead;
		this.canCreate = canCreate;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
		this.canPrint = canPrint;
		this.canDownload = canDownload;
	}
	public PermissionDTO(){

	}

	public boolean getCanRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	
	public boolean getCanCreate() {
		return canCreate;
	}

	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}

	public boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public boolean getCanPrint() {
		return canPrint;
	}

	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}

	public boolean getCanDownload() {
		return canDownload;
	}

	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	@Override
	public String toString() {
		return "PermissionDTO [canCreate=" + canCreate + ", canDelete="
				+ canDelete + ", canDownload=" + canDownload + ", canEdit="
				+ canEdit + ", canPrint=" + canPrint + ", canRead=" + canRead
				+ "]";
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

}
