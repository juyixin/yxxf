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

package org.activiti.engine.form;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;

import com.eazytec.bpm.engine.TaskRole;


/**
 * Represents a single property on a form.
 * 
 * @author Tom Baeyens
 */
public interface FormProperty {
  
  /** The key used to submit the property in {@link FormService#submitStartFormData(String, java.util.Map)} 
   * or {@link FormService#submitTaskFormData(String, java.util.Map)} */
  String getId();
  
  /** The display label */
  String getName();
  
  /** Type of the property. */
  FormType getType();

  /** Optional value that should be used to display in this property */
  String getValue();
  
  /** Optional value that should be used to display in this property */
  String getDefValue();
  
  /** Is this property read to be displayed in the form and made accessible with the methods 
   * {@link FormService#getStartFormData(String)} and {@link FormService#getTaskFormData(String)}. */
  boolean isReadable();

  /** Is this property expected when a user submits the form? */
  boolean isWritable();

  /** Is this property a required input field */
  boolean isRequired();
  
  /** Is this property a required input field */
  boolean isReadOnly();
  
  /** Type of the property. */
  String getSubType();
  
  /**Event handler property*/
  String getOnClick();
  
  String getOnChange();
  
  String getOnBlur();
 
  String getOnFocus();
  
  String getHelpText();
  
  String getMaxLength();
  
  String getMask();
  
  String getLabel();
  
  String getSize();
  
  void setValue(String value);//temporary, will be removed when we do RT form tables
  
  boolean isDefault();
  
  Map<TaskRole, FormFieldPermission> getFormFieldPermissions();
}
