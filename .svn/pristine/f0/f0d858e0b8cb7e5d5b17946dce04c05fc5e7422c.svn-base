package com.eazytec.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RE_INSTANCE_AUDIT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OperatingFunctionAudit extends BaseObject{

	private String id;
	private String createdBy;
	private String taskId;
	private String processName;
	private String operationType;
	private String processId;
	private Date createdOn = new Date();
	private String processDefinitionId;
	
	public OperatingFunctionAudit(String createdBy,String taskId,String processName,String operationType,String processId, String processDefinitionId){
		this.createdBy = createdBy;
		this.taskId = taskId;
		this.processName = processName;
		this.operationType = operationType;
		this.processId=processId;
		this.processDefinitionId = processDefinitionId;
	}
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "PROCESSDEFINITIONID",length = 100)
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	
	@Column(name = "CREATEDBY",length = 100)
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "TASKID",length = 100)
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name = "PROCESSNAME",length = 100)
	public String getProcessName() {
		return processName;
	}
	
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	@Column(name = "OPERATION_TYPE",length = 100)
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	@Column(name = "CREATED_TIME")
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public OperatingFunctionAudit() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Column(name = "PROCESS_ID",length = 100)
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	
}
