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

package org.activiti.engine.impl.el;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.Condition;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.util.DateUtil;


/**
 * {@link Condition} that resolves an UEL expression at runtime.  
 * 
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class UelExpressionCondition implements Condition {
  
  protected Expression expression;
  
  protected Expression previousExpression;
  
  public UelExpressionCondition(Expression expression) {
    this.expression = expression;
  }
  public UelExpressionCondition() {}

  public boolean evaluate(DelegateExecution execution) {
	  Object result = "false" ;
	  previousExpression = expression;
		expression = constructExpression(expression);
		if (expression != null) {
			result = expression.getValue(execution);
			if (result == null) {
				throw new ActivitiException("condition expression returns null");
			}
			if (!(result instanceof Boolean)) {
				throw new ActivitiException("condition expression returns non-Boolean: " + result+ " (" + result.getClass().getName() + ")");
			}
		}
		expression = previousExpression;
    return (Boolean) result;
  }
  
  /**
   * Re-constructing Condtion expression for replacing the 
   * condition in sequence flow with system values
   * @param expression
   * @return
   */
  public Expression constructExpression(Expression expression) {
	  String expressionText = expression.getExpressionText();
		ExpressionManager expressionManager = new ExpressionManager();
		String finalExpression = null;
		//String regexForDefaultValue = "\\|(.*?)\\|";
		if( expressionText.indexOf("|") != -1 ) {
			// replace all "||" symbol present in expression with "OR"
			String regex = "\\|\\|";
			String expWithoutOR = expressionText.replaceAll(regex, "OR");
			StringBuffer exp = new StringBuffer();
		    exp = replaceWithDefaultValues(expWithoutOR);
				String regexOr = "(OR)";
				String finalexp = exp.toString();
				finalExpression = finalexp.replaceAll(regexOr, "||");
		}
		if(finalExpression != null) {
			return expressionManager.createExpression(finalExpression);
		} else {
			return expression;
		}
}
  /**
   * Replacing condition expression with system values
   * @param expression
   * @return
   */
  public StringBuffer replaceWithDefaultValues(String expression) {
		Map<String, Object> globalAttributes = new HashMap<String, Object>();
		globalAttributes = CommonUtil.getGlobalAttributes();
		StringBuffer exp = new StringBuffer();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
			String[] expArrays = expression.split("\\|");
			for(String expArray : expArrays ) {
				if(globalAttributes.containsKey(expArray)) {
					exp.append(appendExpression("userid",expArray,globalAttributes));
					exp.append(appendExpression("username",expArray,globalAttributes));
					exp.append(appendExpression("deptid",expArray,globalAttributes));
					exp.append(appendExpression("telephone",expArray,globalAttributes));
					exp.append(appendExpression("roleid",expArray,globalAttributes));
					exp.append(appendExpression("groupid",expArray,globalAttributes));
					exp.append(appendExpression("apppath",expArray,globalAttributes));
					exp.append(appendExpression("deptname",expArray,globalAttributes));
				} else {
					if(expArray.equalsIgnoreCase("time")) {
						exp.append(DateUtil.getTimeNow(date));
					} else if(expArray.equalsIgnoreCase("day")) {
						exp.append(cal.get(Calendar.DAY_OF_MONTH));
					} else if(expArray.equalsIgnoreCase("month")) {
						exp.append(cal.get(Calendar.MONTH));
					} else if(expArray.equalsIgnoreCase("year")) {
						exp.append(cal.get(Calendar.YEAR));
					} else if(expArray.equalsIgnoreCase("date")) {
						exp.append(cal.get(Calendar.DATE));
					} else if(expArray.equalsIgnoreCase("datetime")) {
						exp.append(DateUtil.getCurrentDateTime());
					} else {
						exp.append(expArray);
					}
				}			
			}
			return exp;
		} 
  		

  private String appendExpression(String exppressions,String expArray,Map<String,Object> globalAttributes){
	  if(expArray.equalsIgnoreCase(exppressions)) {
		  return (String) globalAttributes.get(exppressions);
	  }else{
		  return "";
	  }
  }
  
}
