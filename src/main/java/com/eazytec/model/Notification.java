package com.eazytec.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * This class is used to send Notification to user or group of mail.
 *
 * @author Ramachandran
 */
@Entity
@Table(name = "NOTIFICATION")
public class Notification implements Serializable {

	private static final long serialVersionUID = 4453338766237619444L;
	private String id;
	private String taskId;
	private String instantId;
	private String message;
	private Date messageSendOn;
	private String type;
	private String typeId;
	private String notificationType;
	private String status;
	private String subject;
	private String userId;
	private String statusMessage;
	
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "NOTIFICATION_ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	

	@Column(name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "INSTANT_ID")
	public String getInstantId() {
		return instantId;
	}

	public void setInstantId(String instantId) {
		this.instantId = instantId;
	}

	@Column(name = "MESSAGE" ,  length=8000)  @Lob
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Column(name = "MESSAGE_SEND_ON" )
	public Date getMessageSendOn() {
		return messageSendOn;
	}

	public void setMessageSendOn(Date messageSendOn) {
		this.messageSendOn = messageSendOn;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "TYPE_ID")
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	@Column(name = "NOTIFICATION_TYPE")
	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "SUBJECT")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Notification(){
		
	}
    
	/**
	 * @return the statusMessage
	 */
	@Column(name = "STATUS_MESSAGE",  length=8000)  @Lob
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}

