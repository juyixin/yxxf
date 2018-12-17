package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = "ADMINLOGS")
public class AdminLogs extends BaseObject {
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ProcessLogs.class.getName());

	//private String id;
	private String userId;
	private String logDate;
	private String logType;
	private String logger;
	private String level;
	private String message;
	private String ip;
	
	
	public AdminLogs() {

	}

//	@Lob
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Column(name = "ID")
//	public String getId() {
//		return id;
//	}

	@Column(name = "USER_ID", length = 255)
	public String getUserId() {
		return userId;
	}

	@Id
	@Column(name = "LOG_DATE", length = 255,unique=false,nullable = true)
	public String getLogDate() {
		return logDate;
	}

	@Column(name = "LOG_TYPE", length = 255)
	public String getLogType() {
		return logType;
	}

	@Column(name = "LOGGER", length = 255)
	public String getLogger() {
		return logger;
	}

	@Column(name = "LEVELS", length = 255)
	public String getLevel() {
		return level;
	}

	@Lob
	@Column(name = "MESSAGE", length = 255)
	public String getMessage() {
		return message;
	}
	
	@Column(name = "IP", length = 255)
	public String getIp() {
		return ip;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

}
