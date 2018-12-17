package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eazytec.util.DateUtil;


@Entity
@Table(name = "OPINION_MESSAGE")
public class Opinion implements Serializable {
	private static final long serialVersionUID = 4453338766237619444L;
	private String id;
	private String taskId;
	private String userId;
	private String message;
	private String processInstanceId;
	private String submitStr;
	private String taskName;
	private String userFullName;

	@Column(name = "USER_FULLNAME")
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	
	@Column(name = "TASK_NAME")
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Transient
	public String getSubmitStr() {
		submitStr = DateUtil.convertDateToString(this.submittedOn);
		return submitStr;
	}
	public void setSubmitStr(String submitStr) {
		this.submitStr = submitStr;
	}
	@Column(name = "PROCESS_INS_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	@Column(name = "SUBMIT_ON")
	public Date getSubmittedOn() {
		return submittedOn;
	}
	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	private Date submittedOn;
	
	
	public String getTaskId() {
		return taskId;
	}
	@Column(name = "TASK_ID")
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "MESSAGE" , length=8000)  @Lob 
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
		return id;
	}
	
   	public void setId(String id) {
		this.id = id;
	}
   	
	
	
}
	