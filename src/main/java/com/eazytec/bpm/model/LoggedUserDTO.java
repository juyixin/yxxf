package com.eazytec.bpm.model;

import java.io.Serializable;

import com.eazytec.webapp.controller.BaseFormController;

public class LoggedUserDTO extends BaseFormController implements Serializable {
	
	/**
	 * 
	 */
	LoggedUserDTO(){
		
	}
	public LoggedUserDTO(String userId,String ipAddress){
		this.ipAddress=ipAddress;
		this.userId=userId;
	}
	private static final long serialVersionUID = -6612710125791653819L;
	private String userId;
	private String ipAddress;
	public String getUserId() {
		return userId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Override
	public String toString() {
		return "[userId=" + userId + ", ipAddress=" + ipAddress
				+ "]";
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
        if (!(o instanceof LoggedUserDTO)) {
            return false;
        }

        final LoggedUserDTO user = (LoggedUserDTO) o;

        return !(userId != null ? !userId.equals(user.getUserId()) : user.getUserId() != null);

	}
	@Override
	public int hashCode() {
		return (userId != null ? userId.hashCode() : 0);
	}
	

}
