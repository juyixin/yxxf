package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYSTEMLOGS")
public class SystemLogs extends BaseObject {
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(SystemLogs.class.getName());

	//private int id;
	//private String id;
	private String userId;
	private String logDate;
	private String logger;
	private String level;
	private String message;
	private String ip;
	private String logType;
	
	public SystemLogs(){
		
	}

	public SystemLogs(String userId,String logDate,String logger,String level,String message) {
		this.userId = userId;
		this.logDate = logDate;
		this.logger = logger;
		this.level = level;
		this.message = message;
	}
	
	public SystemLogs(String userId,String logDate){
		this.userId = userId;
		this.logDate = logDate;
	}
	
	/**
	 * @return the id
	 */
	//@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	/*public int getId() {
		return id;
	}*/

	/**
	 * @param id the id to set
	 */
	/*public void setId(int id) {
		this.id = id;
	}*/

	@Id
	@Column(name = "USER_ID", length = 255,unique=false,nullable=true)
	public String getUserId() {
		return userId;
	}

	@Column(name = "LOG_DATE", length = 255)
	public String getLogDate() {
		return logDate;
	}

	@Column(name = "LOGGER", length = 255)
	public String getLogger() {
		return logger;
	}

	@Column(name = "ERROR_LEVEL", length = 255)
	public String getLevel() {
		return level;
	}

	
	@Column(name = "LOG_TYPE", length = 255)
	public String getLogType() {
		return logType;
	}
	
	@Lob
	@Column(name = "MESSAGE", length = 255)
	public String getMessage() {
		return message;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "IP", length = 255)
	public String getIp() {
		return ip;
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
