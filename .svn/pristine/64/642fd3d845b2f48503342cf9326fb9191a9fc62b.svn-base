package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYSTEMLOG")
/*@SecondaryTable(name="LOGINFO", pkJoinColumns={
	    @PrimaryKeyJoinColumn(name="LOG_TYPE", referencedColumnName="LOG_TYPE"),
	    @PrimaryKeyJoinColumn(name="NAME", referencedColumnName="NAME"),
	    @PrimaryKeyJoinColumn(name="SAVE_CYCLE", referencedColumnName="SAVE_CYCLE"),
	    @PrimaryKeyJoinColumn(name="CYCLE_VALUE", referencedColumnName="CYCLE_VALUE")
	})*/





public class SystemLog extends BaseObject {
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(SystemLog.class.getName());

	private String id;
	private String logType;
	private String moduleName;
	private String moduleId;
	private String name;
	private String link;
	private boolean status;
	private String saveCycle;
	private int cycleValue;
	private String description;
	private String account;
	private String user;
	private String department;
	private String ipAddr;
	private String time;
	private String result;
	private String deadLine;
	private String action;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public SystemLog() {
		// PropertyConfigurator.configure("log4j.properties");
		// log.debug("Sample debug message");
		// log.info("Sample info message");
		// log.error("Sample error message");
		// log.fatal("Sample fatal message");
	}

	public SystemLog(String logType, String user, String ipAddr, String time,
			String department, String result) {
		super();
		try {

			this.logType = logType;
			this.user = user;
			
			this.ipAddr = ipAddr;
			this.time = time;
			this.department = department;
			this.result = result;
			log.debug("Debug");
			log.info("Info");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	@Column(name = "LOG_TYPE", length = 255)
	public String getLogType() {
		return logType;
	}

	@Column(name = "MODULE_NAME", length = 255)
	public String getModuleName() {
		return moduleName;
	}
	
	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}
			
	@Column(name = "LINK", length = 255)
	public String getLink() {
		return link;
	}

	@Column(name = "STATUS")
	public boolean getStatus() {
		return status;
	}

	@Column(name = "SAVE_CYCLE", length = 255)
	public String getSaveCycle() {
		return saveCycle;
	}
	
	@Column(name = "CYCLE_VALUE", length = 255)
	public int getCycleValue() {
		return cycleValue;
	}

	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return description;
	}
	
	@Column(name = "ACCOUNT", length = 255)
	public String getAccount() {
		return account;
	}

	@Column(name = "USER_ID", length = 255)
	public String getUser() {
		return user;
	}

	@Column(name = "DEPARTMENT", length = 255)
	public String getDepartment() {
		return department;
	}

	@Column(name = "IP", length = 255)
	public String getIpAddr() {
		return ipAddr;
	}

	@Column(name = "TIME", length = 255)
	public String getTime() {
		return time;
	}

	@Column(name = "RESULT", length = 255)
	public String getResult() {
		return result;
	}

	@Column(name = "DEAD_LINE", length = 255)
	public String getDeadLine() {
		return deadLine;
	}
	
	@Column(name="ACTION", length = 255)
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setSaveCycle(String saveCycle) {
		this.saveCycle = saveCycle;
	}

	public void setCycleValue(int cycleValue) {
		this.cycleValue = cycleValue;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

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
