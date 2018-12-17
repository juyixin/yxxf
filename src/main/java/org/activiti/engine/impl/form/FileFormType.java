package org.activiti.engine.impl.form;

import org.activiti.engine.form.AbstractFormType;



/**
 * @author madan 
 */
public class FileFormType extends AbstractFormType {

  public String getName() {
    return "file";
  }

  public String getMimeType() {
    return "text/plain";
  }

  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }

  public String convertModelValueToFormValue(Object modelValue) {
    return (String) modelValue;
  }

}
