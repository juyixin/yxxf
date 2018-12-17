

package org.activiti.engine.impl.task;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.engine.task.OperatingFunction;

import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.common.util.StringUtil;


/**
 * @author Tom Baeyens
 */
public class OperatingFunctionHandler {
	public static final String OPERATING_FUNCTION_TAG = "operatingFunction";
	public static final String OPERATING_FUNCTION_ROLE_ATTR = "role";
	
	Map<TaskRole, OperatingFunction>operatingFunctions;
  
  
	public OperatingFunctionHandler(Element activityElement){
		this.parseConfiguration(activityElement);
	}
  
	public void parseConfiguration(Element activityElement) {
		createOperatingFunctions(activityElement);
	}
  
	private void createOperatingFunctions(Element taskElement){
		List<Element>operatingFunctionEls = taskElement.elements(OPERATING_FUNCTION_TAG);
		operatingFunctions = new HashMap<TaskRole, OperatingFunction>();
		for (Element operatingFunctionEl : operatingFunctionEls) {
			createOperatingFunction(operatingFunctionEl);
		}
	}
  
	private void createOperatingFunction(Element operatingFunctionEl){
		String taskRoleName = operatingFunctionEl.attribute(OPERATING_FUNCTION_ROLE_ATTR);
		TaskRole taskRole = TaskRole.GET_TASK_ROLE.getTaskRoleByName(taskRoleName);
		if(taskRole!=null){
			operatingFunctions.put(taskRole, setOperatingFunction(operatingFunctionEl));
		}
	}
  
  	private OperatingFunction setOperatingFunction(Element operatingFunctionEl){
  		OperatingFunction operatingFunction = new OperatingFunctionImpl();	  
  		operatingFunction.setSubmit(getSubmit(operatingFunctionEl));
  		operatingFunction.setSave(getSave(operatingFunctionEl));
  		operatingFunction.setReturnOperation(getReturnOperation(operatingFunctionEl));
  		operatingFunction.setRecall(getRecall(operatingFunctionEl));
  		operatingFunction.setWithdraw(getWithdraw(operatingFunctionEl));
  		operatingFunction.setTransfer(getTransfer(operatingFunctionEl));
  		operatingFunction.setUrge(getUrge(operatingFunctionEl));
  		operatingFunction.setAdd(getAdd(operatingFunctionEl));
  		operatingFunction.setConfluentSignature(getConfluentSignature(operatingFunctionEl));
  		operatingFunction.setCirculatePerusal(getCirculatePerusal(operatingFunctionEl));
  		operatingFunction.setJump(getJump(operatingFunctionEl));
  		operatingFunction.setTransactorReplacement(getTransactorReplacement(operatingFunctionEl));
  		operatingFunction.setTerminate(getTerminate(operatingFunctionEl));
  		operatingFunction.setSuspend(getSuspend(operatingFunctionEl));
  		operatingFunction.setCustomOperations(getCustomOperations(operatingFunctionEl));
  		operatingFunction.setPrint(getPrint(operatingFunctionEl));
  		
  		return operatingFunction;
  	}
  
  	private boolean getSubmit(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.SUBMIT);
	}

	

	private boolean getSave(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.SAVE);
	}



	private boolean getReturnOperation(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.RETURN);
	}



	private boolean getRecall(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.RECALL);
	}


	private boolean getWithdraw(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.WITHDRAW);
	}



	private boolean getTransfer(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.TRANSFER);
	}


	private boolean getUrge(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.URGE);
	}

	private boolean getAdd(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.ADD);
	}
	

	private boolean getConfluentSignature(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.CONFLUENT_SIGNATURE);
	}

	

	private boolean getCirculatePerusal(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.CIRCULATE_PERUSAL);
	}

	

	private boolean getJump(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.JUMP);
	}

	
	private boolean getTransactorReplacement(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.TRANSACTOR_REPLACEMENT);
	}

	

	private boolean getTerminate(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.TERMINATE);
	}

	
	private boolean getSuspend(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.SUSPEND);
	}	

	public List<CustomOperatingFunction> getCustomOperations(Element operatingFunctionEl) {
		List<CustomOperatingFunction> customOpFunctions = new ArrayList<CustomOperatingFunction>();
		List<String>customFunctionIds = new ArrayList<String>();
		
		List<Element>customFunctionEls = operatingFunctionEl.elements(OperatingFunction.CUSTOM_FUNCTION);
		for (Element customFunctionEl : customFunctionEls) {
			String customOperationId = customFunctionEl.getText();
			if(!StringUtil.isEmptyString(customOperationId)){
				customFunctionIds.add(customOperationId);
			}			
		}
		if(customFunctionIds != null && customFunctionIds.size() > 0){
			List<CustomOperatingFunction> customFunctions=Context.getCommandContext().getCustomOperatingFunctionManager().findOperatingFunctionsByIds(customFunctionIds);
			for (CustomOperatingFunction customFunction : customFunctions) {
				try {
					customFunction.setJsFunction(URLDecoder.decode(customFunction.getJsFunction(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block					
				}
				customOpFunctions.add(customFunction);
			}
		}
		return customOpFunctions;
	}

	private boolean getOperatingFunctionValue(Element operatingFunctionEl, String operatingFunctionType){
		Element operatingFunctionTypeEl = operatingFunctionEl.element(operatingFunctionType);
		if(operatingFunctionTypeEl!=null){
			return StringUtil.checkAndParseBooleanAttribute(operatingFunctionTypeEl.getText());
		}else{
			return false;
		}
	}

	public Map<TaskRole, OperatingFunction> getOperatingFunctions() {
		return operatingFunctions;
	}
	
	public void setOperatingFunctions(
			Map<TaskRole, OperatingFunction> operatingFunctions) {
		this.operatingFunctions = operatingFunctions;
	}
	
	private boolean getPrint(Element operatingFunctionEl) {
		return getOperatingFunctionValue(operatingFunctionEl, OperatingFunction.PRINT);
	}
	
}