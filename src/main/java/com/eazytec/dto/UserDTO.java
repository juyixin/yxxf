package com.eazytec.dto;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO {

	private String id;
	private String username; // required
	private String fullName; // required
	private String employeeNumber;
	private String department;
	private String email; // required; unique
	private boolean enabled;
	private String firstName;
	private String lastName = "";
	private boolean sendEmailNotification;
	
	public UserDTO(){
	}
	
	public UserDTO(String id, String username, String firstName,String lastName,
			String department, String email,String employeeNumber, 
			boolean enabled) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = "";
		this.employeeNumber = employeeNumber;
		this.department = department;
		this.email = email;
		this.enabled = enabled;
	}
	public UserDTO(String id, String username, String firstName,String lastName,
			String department, String email,String employeeNumber, 
			boolean enabled,boolean sendEmailNotification) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = "";
		this.employeeNumber = employeeNumber;
		this.department = department;
		this.email = email;
		this.enabled = enabled;
		this.sendEmailNotification=sendEmailNotification;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return firstName + ' ' + lastName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setSendEmailNotification(boolean sendEmailNotification) {
		this.sendEmailNotification = sendEmailNotification;
	}
	
	public boolean isSendEmailNotification() {
		return sendEmailNotification;
	}

}
