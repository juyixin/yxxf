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

import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.el.StartProcessVariableScope;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;


/**
 * @author Tom Baeyens
 */
public class FormPropertyHandler {

  protected String id;
  protected String name;
  protected AbstractFormType type;
  protected MetaTable table;
  protected MetaTableColumns column;
  protected boolean isReadable;
  protected boolean isReadOnly;
  protected boolean isWritable;
  protected boolean isRequired;
  private boolean isSubFormElement;
  protected String variableName;
  protected Expression variableExpression;
  protected Expression defaultExpression; 
  protected String subType;
  protected String onClick;
  protected String onChange;
  protected String onBlur;
  protected String onFocus;
  protected String value;
  protected String defValue;
  protected String helpText;
  protected String maxLength;
  protected String mask;
  protected String label;
  protected String size;
  protected Map<String, Boolean>creatorPermissions;
  protected Map<String, Boolean>readerPermissions;
  protected Map<String, Boolean>organizerPermissions;
  protected Map<String, Boolean>coordinatorPermissions;
  protected Map<String, Boolean>processedUserPermissions;
  protected Map<String, Boolean>wfAdminPermissions;
  protected boolean isDefault = false;


public FormProperty createFormProperty(ExecutionEntity execution) {
    FormPropertyImpl formProperty = new FormPropertyImpl(this);
    Object modelValue = null;
    if (execution!=null) {
      if (variableName != null || variableExpression == null) {
        final String varName = variableName != null ? variableName : name;
        if (execution.hasVariable(varName)) {
          modelValue = execution.getVariable(varName);
        } else if (defaultExpression != null) {
          modelValue = defaultExpression.getValue(execution);
        }
      } else {
        modelValue = variableExpression.getValue(execution);
      }
    } else {
      // Execution is null, the form-property is used in a start-form. Default value
      // should be available (ACT-1028) even though no execution is available.
      if (defaultExpression != null) {
        modelValue = defaultExpression.getValue(StartProcessVariableScope.getSharedInstance());
      }
    }

    if (modelValue instanceof String) {
      formProperty.setValue((String) modelValue);
    } else if (type != null) {
      String formValue = type.convertModelValueToFormValue(modelValue);
      formProperty.setValue(formValue);
    } else if (modelValue != null) {
      formProperty.setValue(modelValue.toString());
    }
    
    return formProperty;
  }

  public void submitFormProperty(ExecutionEntity execution, Map<String, String> properties) {
//    if (!isWritable && properties.containsKey(name)) {
//      throw new ActivitiException("form property '"+name+"' is not writable");
//    }
//    
//    if (isRequired && !properties.containsKey(name) && defaultExpression == null) {
//      throw new ActivitiException("form property '"+name+"' is required");
//    }
    
    Object modelValue = null;
    if (properties.containsKey(name)) {
      final String propertyValue = properties.remove(name);
      if (type != null) {
        modelValue = type.convertFormValueToModelValue(propertyValue);
      } else {
        modelValue = propertyValue;
      }
    } else if (defaultExpression != null) {
      final Object expressionValue = defaultExpression.getValue(execution);
      if (type != null && expressionValue != null) {
        modelValue = type.convertFormValueToModelValue(expressionValue.toString());
      } else if (expressionValue != null) {
        modelValue = expressionValue.toString();
      } else if (isRequired) {
        throw new ActivitiException("form property '"+name+"' is required");
      }
    }
    
    if (modelValue != null) {
      if (variableName != null) {
        execution.setVariable(variableName, modelValue);
      } else if (variableExpression != null) {
        variableExpression.setValue(modelValue, execution);
      } else {
        execution.setVariable(name, modelValue);
      }
    }
  }
  
  public void updateFormProperty(ExecutionEntity execution, Map<String, String> properties) {	    
	    Object modelValue = null;
	    if (properties.containsKey(name)) {
	      final String propertyValue = properties.remove(name);
	      if (type != null) {
	        modelValue = type.convertFormValueToModelValue(propertyValue);
	      } else {
	        modelValue = propertyValue;
	      }
	    } else if (defaultExpression != null) {
	      final Object expressionValue = defaultExpression.getValue(execution);
	      if (type != null && expressionValue != null) {
	        modelValue = type.convertFormValueToModelValue(expressionValue.toString());
	      } else if (expressionValue != null) {
	        modelValue = expressionValue.toString();
	      } else if (isRequired) {
	        throw new ActivitiException("form property '"+name+"' is required");
	      }
	    }
	    
	    if (modelValue != null) {
	      if (variableName != null) {
	        execution.setVariable(variableName, modelValue);
	      } else if (variableExpression != null) {
	        variableExpression.setValue(modelValue, execution);
	      } else {
	        execution.setVariable(name, modelValue);
	      }
	    }
	  }

  // getters and setters //////////////////////////////////////////////////////
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public AbstractFormType getType() {
    return type;
  }
  
  public void setType(AbstractFormType type) {
    this.type = type;
  }
  
  public boolean isReadable() {
    return isReadable;
  }
  
  public void setReadable(boolean isReadable) {
    this.isReadable = isReadable;
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

  public String getVariableName() {
    return variableName;
  }
  
  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }
  
  public Expression getVariableExpression() {
    return variableExpression;
  }
  
  public void setVariableExpression(Expression variableExpression) {
    this.variableExpression = variableExpression;
  }
  
  public Expression getDefaultExpression() {
    return defaultExpression;
  }
  
  public void setDefaultExpression(Expression defaultExpression) {
    this.defaultExpression = defaultExpression;
  }
  
  public boolean isWritable() {
    return isWritable;
  }

  public void setWritable(boolean isWritable) {
    this.isWritable = isWritable;
  }

  public String getSubType() {
	  return subType;
  }
	
  public void setSubType(String subType) {
	this.subType = subType;
  }
	
  public String getOnClick() {
	return onClick;
  }
	
  public void setOnClick(String onClick) {
	this.onClick = onClick;
  }
	
  public String getOnChange() {
	return onChange;
  }
	
  public void setOnChange(String onChange) {
	this.onChange = onChange;
  }
	
  public String getOnBlur() {
	return onBlur;
  }
	
  public void setOnBlur(String onBlur) {
	this.onBlur = onBlur;
  }
  
  

public String getOnFocus() {
	return onFocus;
}

public void setOnFocus(String onFocus) {
	this.onFocus = onFocus;
}

public String getValue() {
	return value;
}

public void setValue(String value) {
	this.value = value;
}

public String getDefValue() {
	return defValue;
}

public void setDefValue(String defValue) {
	this.defValue = defValue;
}

public String getHelpText() {
	return helpText;
}

public void setHelpText(String helpText) {
	this.helpText = helpText;
}

public String getMaxLength() {
	return maxLength;
}

public void setMaxLength(String maxLength) {
	this.maxLength = maxLength;
}

public String getMask() {
	return mask;
}

public void setMask(String mask) {
	this.mask = mask;
}

public String getLabel() {
	return label;
}

public void setLabel(String label) {
	this.label = label;
}

public String getSize() {
	return size;
}

public void setSize(String size) {
	this.size = size;
}

public Map<String, Boolean> getCreatorPermissions() {
	return creatorPermissions;
}

public void setCreatorPermissions(Map<String, Boolean> creatorPermissions) {
	this.creatorPermissions = creatorPermissions;
}

public Map<String, Boolean> getReaderPermissions() {
	return readerPermissions;
}

public void setReaderPermissions(Map<String, Boolean> readerPermissions) {
	this.readerPermissions = readerPermissions;
}

public Map<String, Boolean> getOrganizerPermissions() {
	return organizerPermissions;
}

public void setOrganizerPermissions(Map<String, Boolean> organizerPermissions) {
	this.organizerPermissions = organizerPermissions;
}

public Map<String, Boolean> getCoordinatorPermissions() {
	return coordinatorPermissions;
}

public void setCoordinatorPermissions(
		Map<String, Boolean> coordinatorPermissions) {
	this.coordinatorPermissions = coordinatorPermissions;
}

public Map<String, Boolean> getProcessedUserPermissions() {
	return processedUserPermissions;
}

public void setProcessedUserPermissions(
		Map<String, Boolean> processedUserPermissions) {
	this.processedUserPermissions = processedUserPermissions;
}

public Map<String, Boolean> getWfAdminPermissions() {
	return wfAdminPermissions;
}

public void setWfAdminPermissions(Map<String, Boolean> wfAdminPermissions) {
	this.wfAdminPermissions = wfAdminPermissions;
}

public MetaTable getTable() {
	return table;
}

public void setTable(MetaTable table) {
	this.table = table;
}

public MetaTableColumns getColumn() {
	return column;
}

public void setColumn(MetaTableColumns column) {
	this.column = column;
}

public boolean isDefault() {
	return isDefault;
}

public void setDefault(boolean isDefault) {
	this.isDefault = isDefault;
}

public boolean isSubFormElement() {
	return isSubFormElement;
}

public void setSubFormElement(boolean isSubFormElement) {
	this.isSubFormElement = isSubFormElement;
}

}
