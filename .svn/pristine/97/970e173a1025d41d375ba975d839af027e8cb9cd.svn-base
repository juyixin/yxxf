package org.activiti.engine.form;

import org.activiti.engine.task.Task;

import com.eazytec.bpm.engine.TaskRole;

 public interface FormFieldPermission {		
	
	 
	 
	 static final String IS_EDITABLE="isEditable";
	 static final String IS_REQUIRED="isRequired";
		static final String IS_READ_ONLY="isReadOnly";
		static final String IS_HIDDEN="isHidden";
		static final String IS_NO_OUTPUT="isNoOutput";
		static final String PERMISSION_FORMAT = IS_EDITABLE+IS_REQUIRED+IS_READ_ONLY+IS_HIDDEN+IS_NO_OUTPUT;
	Task getTask();

	void setTask(Task task);

	 TaskRole getTaskRole();
		

	 void setTaskRole(TaskRole taskRole);
		

	 boolean isEditable();
		

	 void setEditable(boolean isEditable);
		

	 boolean isRequired();
		

	 void setRequired(boolean isRequired);
		

	 boolean isReadOnly();
		

	 void setReadOnly(boolean isReadOnly);
		

	 boolean isHidden();
		

	 void setHidden(boolean isHidden);
		

	 boolean isNoOutput();
		

	 void setNoOutput(boolean isNoOutput);
		
	 FormProperty getField();

	 void setField(FormProperty field);

	
	
}
