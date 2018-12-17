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
package org.activiti.engine.impl.bpmn.behavior;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;


/**
 * @author Joram Barrez
 */
public class NoneEndEventActivityBehavior extends FlowNodeActivityBehavior {
  
	protected TaskDefinition taskDefinition;
	  public NoneEndEventActivityBehavior(TaskDefinition taskDefinition) {
	    this.taskDefinition = taskDefinition;
	  }
	  
  public void execute(ActivityExecution execution) throws Exception {
	handleAssignments(execution);
    execution.end();
  }
  
  
  @SuppressWarnings({ "unchecked"})
	protected void handleAssignments(ActivityExecution execution) {

	  	Set<String> groupReaders = new HashSet<String>();
	  	Set<String> userReaders = new HashSet<String>();
	  	//check is all stackholders
	    if(execution.getActivity() != null){
	        if(execution.getActivity().getProperty("readerOption") != null){
	        	if(execution.getActivity().getProperty("readerOption").equals("allstackholder")){
	    			List<HistoricIdentityLinkEntity> stackHoldersLinks = Context.getCommandContext().
	    			getHistoricTaskInstanceEntityManager().getHistoricIdentityLinksByProcessInstanceId(execution.getId());
	    			for(HistoricIdentityLinkEntity stackHoldersLink : stackHoldersLinks){
	    				if(stackHoldersLink.getGroupId() != null){
	    					groupReaders.add(stackHoldersLink.getGroupId());
	    				}else if(stackHoldersLink.getUserId() != null){
	    					userReaders.add(stackHoldersLink.getUserId());
	    				}
	    			}
	        	}
	        }
	    }
	  	
		if (!taskDefinition.getReaderGroupIdExpressions().isEmpty()) {
			for (Expression groupIdExpr : taskDefinition
					.getReaderGroupIdExpressions()) {
				Object value = groupIdExpr.getValue(execution);
				if (value instanceof String) {
					groupReaders.addAll(extractCandidates((String) value));
					taskDefinition.addReaderGroups(groupReaders, execution.getId());
				} else if (value instanceof Collection) {
					taskDefinition.addReaderGroups((Collection) value,
							execution.getId());
				} else {
					throw new ActivitiException(
							"Expression did not resolve to a string or collection of strings");
				}
			}
		}

		if (!taskDefinition.getReaderUserIdExpressions().isEmpty()) {
			for (Expression userIdExpr : taskDefinition
					.getReaderUserIdExpressions()) {
				Object value = userIdExpr.getValue(execution);
				if (value instanceof String) {
					userReaders.addAll(extractCandidates((String) value));
					taskDefinition.addReaderUsers(userReaders, execution.getId());
				} else if (value instanceof Collection) {
					userReaders.addAll((Collection)value);
				} else {
					throw new ActivitiException(
							"Expression did not resolve to a string or collection of strings");
				}
			}
		}
		if(!userReaders.isEmpty()){
			taskDefinition.addReaderUsers(userReaders, execution
					.getId());
		}
	}
	  
  /**
   * Extract a candidate list from a string. 
   * 
   * @param str
   * @return 
   */
  protected List<String> extractCandidates(String str) {
    return Arrays.asList(str.split("[\\s]*,[\\s]*"));
  }

}
