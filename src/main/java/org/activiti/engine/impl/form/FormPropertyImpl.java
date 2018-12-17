/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;


/**
 * @author Tom Baeyens
 */
public class FormPropertyImpl implements FormProperty {
  
  protected String id;
  protected String name;
  protected FormType type;
  protected Map<TaskRole, FormFieldPermission> formFieldPermissions;
  protected MetaTable table;
  protected MetaTableColumns column;
  protected boolean isRequired;
  protected boolean isReadOnly;
  protected boolean isReadable;
  protected boolean isWritable;
  protected String subType;
  protected String value;
  protected String defValue;
  protected String onClick;
  protected String onChange;
  protected String onBlur;
  protected String onFocus;
  protected String helpText;
  protected String maxLength;
  protected String mask;
  protected String label;
  protected String size;
  protected boolean isDefault=false;

  public FormPropertyImpl(FormPropertyHandler formPropertyHandler) {
    this.id = formPropertyHandler.getId();
    this.name = formPropertyHandler.getName();
    this.type = formPropertyHandler.getType();
    this.isRequired = formPropertyHandler.isRequired();
    this.isReadable = formPropertyHandler.isReadable();
    this.isWritable = formPropertyHandler.isWritable();
    this.subType = formPropertyHandler.getSubType();
    this.onClick = formPropertyHandler.getOnClick();
    this.onChange = formPropertyHandler.getOnChange();
    this.onBlur = formPropertyHandler.getOnBlur();
    this.onFocus = formPropertyHandler.getOnFocus();
    this.value = formPropertyHandler.getValue();
    this.defValue = formPropertyHandler.getDefValue();
    this.helpText = formPropertyHandler.getHelpText();
    this.maxLength = formPropertyHandler.getMaxLength();
    this.mask=formPropertyHandler.getMask();
    this.label=formPropertyHandler.getLabel();
    this.size=formPropertyHandler.getSize();
    this.isReadOnly=formPropertyHandler.isReadOnly();    
    this.formFieldPermissions = setFormFieldPermissions(formPropertyHandler);
    this.table = formPropertyHandler.getTable();
    this.column = formPropertyHandler.getColumn();
    this.isDefault = formPropertyHandler.isDefault();
  }

  private Map<TaskRole, FormFieldPermission> setFormFieldPermissions(FormPropertyHandler formPropertyHandler){
	  Map<TaskRole, FormFieldPermission> formFieldPermissions = new  HashMap<TaskRole, FormFieldPermission>();
	    formFieldPermissions.put(TaskRole.CREATOR, new FormFieldPermissionImpl(TaskRole.CREATOR, formPropertyHandler.getCreatorPermissions()));
	    formFieldPermissions.put(TaskRole.COORDINATOR, new FormFieldPermissionImpl(TaskRole.COORDINATOR, formPropertyHandler.getCoordinatorPermissions()));
	    formFieldPermissions.put(TaskRole.ORGANIZER, new FormFieldPermissionImpl(TaskRole.ORGANIZER, formPropertyHandler.getOrganizerPermissions()));
	    formFieldPermissions.put(TaskRole.PROCESSED_USER, new FormFieldPermissionImpl(TaskRole.PROCESSED_USER, formPropertyHandler.getProcessedUserPermissions()));
	    formFieldPermissions.put(TaskRole.READER, new FormFieldPermissionImpl(TaskRole.READER, formPropertyHandler.getReaderPermissions()));
	    formFieldPermissions.put(TaskRole.WORKFLOW_ADMINISTRATOR, new FormFieldPermissionImpl(TaskRole.WORKFLOW_ADMINISTRATOR, formPropertyHandler.getWfAdminPermissions()));
        return formFieldPermissions;
  }
  
  public String getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public FormType getType() {
    return type;
  }
  
  public String getValue() {
    return value;
  }
  
  public boolean isRequired() {
    return isRequired;
  }
  
  public boolean isReadable() {
    return isReadable;
  }
  
  public void setValue(String value) {
    this.value = value;
  }

  public boolean isWritable() {
    return isWritable;
  }

  @Override
  public String getSubType() {
	  // TODO Auto-generated method stub
	  return subType;
  }
  
  public String getOnClick() {
	  // TODO Auto-generated method stub
	  return onClick;
  }
  
  public String getOnChange() {
	  // TODO Auto-generated method stub
	  return onChange;
  }
   
  public String getOnBlur() {
	  // TODO Auto-generated method stub
	  return onBlur;
  }

  public String getOnFocus() {
	  // TODO Auto-generated method stub
	  return onFocus;
  }  
  
  public String getHelpText() {
	  // TODO Auto-generated method stub
	  return helpText;
  }
   
  public String getMaxLength() {
	  // TODO Auto-generated method stub
	  return maxLength;
  }
  
  public String getMask() {
	  // TODO Auto-generated method stub
	  return mask;
  }
  
  public String getLabel() {
	  // TODO Auto-generated method stub
	  return label;
  }
  
  public String getSize() {
	  // TODO Auto-generated method stub
	  return size;
  }  

  public String getDefValue() {
	  // TODO Auto-generated method stub
	  return defValue;
  }

  @Override
  public boolean isReadOnly() {
	// TODO Auto-generated method stub
	return isReadOnly;
  }

public Map<TaskRole, FormFieldPermission> getFormFieldPermissions() {
	return formFieldPermissions;
}

public MetaTable getTable() {
	return table;
}

public MetaTableColumns getColumn() {
	return column;
}

public boolean isDefault() {
	return isDefault;
} 
 
}
