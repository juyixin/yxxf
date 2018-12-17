package com.eazytec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "MODULELOGS")
public class ModuleLogs implements Serializable {
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ProcessLogs.class.getName());

	private String userId;
	private String logDate;
	private String logType;
	private String logger;
	private String level;
	private String message;
	private String ip;
	
	
	public ModuleLogs() {

	}

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

	@Column(name = "ERROR_LEVEL", length = 255)
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
}
