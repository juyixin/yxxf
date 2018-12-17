package org.activiti.engine.impl.form;

import java.util.Map;

import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;

import com.eazytec.bpm.engine.TaskRole;

public class FormFieldPermissionImpl implements FormFieldPermission {
//	public static final String IS_EDITABLE="isEditable";
//	public static final String IS_REQUIRED="isRequired";
//	public static final String IS_READ_ONLY="isReadOnly";
//	public static final String IS_HIDDEN="isHidden";
//	public static final String IS_NO_OUTPUT="isNoOutput";
	private FormProperty field;
	private Task task;
	private TaskRole taskRole;
	private boolean isEditable;
	private boolean isRequired;
	private boolean isReadOnly;
	private boolean isHidden;
	private boolean isNoOutput;
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public FormFieldPermissionImpl() {
    }
    
    public FormFieldPermissionImpl(TaskRole role, Map<String, Boolean>permissions) {
    	if(permissions!=null&&permissions.size()>0){
    		this.taskRole = role;
        	this.isEditable = permissions.get(FormFieldPermission.IS_EDITABLE);
        	this.isRequired = permissions.get(FormFieldPermission.IS_REQUIRED);
        	this.isReadOnly = permissions.get(FormFieldPermission.IS_READ_ONLY);
        	this.isHidden = permissions.get(FormFieldPermission.IS_HIDDEN);
        	this.isNoOutput = permissions.get(FormFieldPermission.IS_NO_OUTPUT);
    	}    	
    }
    
    public FormFieldPermissionImpl(FormProperty field, TaskRole role, Map<String, Boolean>permissions) {
    	this.field = field;
    	this.taskRole = role;
    	this.isEditable = permissions.get(FormFieldPermission.IS_EDITABLE);
    	this.isRequired = permissions.get(FormFieldPermission.IS_REQUIRED);
    	this.isReadOnly = permissions.get(FormFieldPermission.IS_READ_ONLY);
    	this.isHidden = permissions.get(FormFieldPermission.IS_HIDDEN);
    	this.isNoOutput = permissions.get(FormFieldPermission.IS_NO_OUTPUT);
    }

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public TaskRole getTaskRole() {
		return taskRole;
	}

	public void setTaskRole(TaskRole taskRole) {
		this.taskRole = taskRole;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isNoOutput() {
		return isNoOutput;
	}

	public void setNoOutput(boolean isNoOutput) {
		this.isNoOutput = isNoOutput;
	}

	public FormProperty getField() {
		return field;
	}

	public void setField(FormProperty field) {
		this.field = field;
	}
	
	
	
}
