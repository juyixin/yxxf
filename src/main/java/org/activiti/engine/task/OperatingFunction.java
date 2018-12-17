package org.activiti.engine.task;

import java.util.List;

import org.activiti.engine.impl.task.CustomOperatingFunction;
import org.activiti.engine.task.Task;

import com.eazytec.bpm.engine.TaskRole;

 public interface OperatingFunction {
	 
	static final String SUBMIT="submit";
	static final String CREATE="create";
	static final String SAVE="save";
	static final String RETURN="returnoperation";
	static final String RECALL="recall";
	static final String WITHDRAW="withdraw";
	static final String TRANSFER="transfer";
	static final String URGE="urge";
	static final String ADD="add";
	static final String CONFLUENT_SIGNATURE="collaborativeoperation";//协办
	static final String CIRCULATE_PERUSAL="circulateperusal";//传阅
	static final String JUMP="jump";
	static final String TRANSACTOR_REPLACEMENT="transactorreplacement";//替换办理人
	static final String TERMINATE="terminate";
	static final String SUSPEND="suspend";
	static final String CUSTOM_FUNCTION="custom";
	static final String START_PROCESS  ="startProcess";
	static final String DEFAULT_TASK  ="defaultTask";
	static final String ADDORDER="addOrder";
	static final String REFERENCE = "reference";
	static final String PRINT="print";
	static final String ACTIVATE="activate";
	static final String DELETE="delete";
	static final String BULKREPLACE="bulkReplace";
	static final String UPDATE="update";
	static final String RETURN_SUBMIT="return submit";
	static final String BACK="Back";
	
	/** This is not an operating function, it is a function that will be done for next task**/
	static final String JOINT_CONDUCTION="jointConduction";
	static final String COMPELETE = "complete";
	
	Task getTask();

	void setTask(Task task);	

	boolean isSubmit();

	void setSubmit(boolean submit);

	boolean isPrint();
	
	void setPrint(boolean print);
	
	boolean isSave();

	void setSave(boolean save);

	boolean isReturnOperation();

	void setReturnOperation(boolean returnOperation);

	boolean isRecall();

	void setRecall(boolean recall);

	boolean isWithdraw();

	void setWithdraw(boolean withdraw);

	boolean isTransfer();

	void setTransfer(boolean transfer);

	boolean isUrge();

	void setUrge(boolean urge);

	boolean isAdd();

	void setAdd(boolean add);

	boolean isConfluentSignature();

	void setConfluentSignature(boolean confluentSignature);

	boolean isCirculatePerusal();

	void setCirculatePerusal(boolean circulatePerusal);

	boolean isJump();

	void setJump(boolean jump);

	boolean isTransactorReplacement();

	void setTransactorReplacement(boolean transactorReplacement);

	boolean isTerminate();

	void setTerminate(boolean terminate);

	boolean isSuspend();

	void setSuspend(boolean suspend);
	
	List<CustomOperatingFunction> getCustomOperations();

	void setCustomOperations(List<CustomOperatingFunction> customOperations);
	
}
