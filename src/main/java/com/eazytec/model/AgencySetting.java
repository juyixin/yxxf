package com.eazytec.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USER_AGENCY_SETTING")
public class AgencySetting {
	
	private String id;
	private boolean enabled = true;
	private String agent;
	private Date startDate;
	private Date endDate;
	private String notificationMessage;
	private boolean isSms = false;;
	private boolean isMail = false;;
	private boolean isInternalMessage = false;;
	private String userId;
	private String processId;
	private String agentFullName;
	
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
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getNotificationMessage() {
		return notificationMessage;
	}
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "USERID", nullable = false, length = 100)
	public String getUserId() {
		return userId;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setAgentFullName(String agentFullName) {
		this.agentFullName = agentFullName;
	}
	public String getAgentFullName() {
		return agentFullName;
	}
	
	 @Column(name = "IS_SMS")
	public boolean getIsSms() {
		return isSms;
	}
	public void setIsSms(boolean isSms) {
		this.isSms = isSms;
	}
	
	@Column(name = "IS_MAIL")
	public boolean getIsMail() {
		return isMail;
	}
	public void setIsMail(boolean isMail) {
		this.isMail = isMail;
	}
	
	@Column(name = "IS_INTERNAL_MESSAGE")
	public boolean getIsInternalMessage() {
		return isInternalMessage;
	}
	public void setIsInternalMessage(boolean isInternalMessage) {
		this.isInternalMessage = isInternalMessage;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	

}
