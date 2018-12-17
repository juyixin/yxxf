package org.activiti.engine.impl.task;

import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;

import com.eazytec.bpm.engine.TaskRole;

public class OperatingFunctionImpl implements OperatingFunction {

	private Task task;
	private boolean submit;
	private boolean save;
	private boolean returnOperation;
	private boolean recall;
	private boolean withdraw;
	private boolean transfer;
	private boolean urge;
	private boolean add;
	private boolean confluentSignature;
	private boolean circulatePerusal;
	private boolean jump;
	private boolean transactorReplacement;
	private boolean terminate;
	private boolean suspend;
	private boolean print;

	private List<CustomOperatingFunction> customOperations;
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public OperatingFunctionImpl() {
    }    

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public boolean isSubmit() {
		return submit;
	}

	public void setSubmit(boolean submit) {
		this.submit = submit;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public boolean isReturnOperation() {
		return returnOperation;
	}

	public void setReturnOperation(boolean returnOperation) {
		this.returnOperation = returnOperation;
	}

	public boolean isRecall() {
		return recall;
	}

	public void setRecall(boolean recall) {
		this.recall = recall;
	}

	public boolean isWithdraw() {
		return withdraw;
	}

	public void setWithdraw(boolean withdraw) {
		this.withdraw = withdraw;
	}

	public boolean isTransfer() {
		return transfer;
	}

	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}

	public boolean isUrge() {
		return urge;
	}

	public void setUrge(boolean urge) {
		this.urge = urge;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isConfluentSignature() {
		return confluentSignature;
	}

	public void setConfluentSignature(boolean confluentSignature) {
		this.confluentSignature = confluentSignature;
	}

	public boolean isCirculatePerusal() {
		return circulatePerusal;
	}

	public void setCirculatePerusal(boolean circulatePerusal) {
		this.circulatePerusal = circulatePerusal;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public boolean isTransactorReplacement() {
		return transactorReplacement;
	}

	public void setTransactorReplacement(boolean transactorReplacement) {
		this.transactorReplacement = transactorReplacement;
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

	public boolean isSuspend() {
		return suspend;
	}

	public void setSuspend(boolean suspend) {
		this.suspend = suspend;
	}

	public List<CustomOperatingFunction> getCustomOperations() {
		return customOperations;
	}

	public void setCustomOperations(List<CustomOperatingFunction> customOperations) {
		this.customOperations = customOperations;
	}
	
	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}
}
