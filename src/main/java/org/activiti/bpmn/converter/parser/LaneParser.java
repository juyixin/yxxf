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
package org.activiti.bpmn.converter.parser;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.Lane;
import org.activiti.bpmn.model.Process;

/**
 * @author Tijs Rademakers
 */
public class LaneParser implements BpmnXMLConstants {
  
  public void parse(XMLStreamReader xtr, Process activeProcess) throws Exception {
    Lane lane = new Lane();
    lane.setId(xtr.getAttributeValue(null, ATTRIBUTE_ID));
    lane.setName(xtr.getAttributeValue(null, ATTRIBUTE_NAME));
    lane.setParentProcess(activeProcess);
    activeProcess.getLanes().add(lane);
    
    while (xtr.hasNext()) {
      xtr.next();
      if (xtr.isStartElement() && ATTRIBUTE_FLOWNODE_REF.equalsIgnoreCase(xtr.getLocalName())) {
        lane.getFlowReferences().add(xtr.getElementText());
      } else if(xtr.isEndElement() && ELEMENT_LANE.equalsIgnoreCase(xtr.getLocalName())) {
        break;
      }
    }
  }
}
