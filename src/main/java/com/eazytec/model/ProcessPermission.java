package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RE_PROCESS_PERMISSION")
public class ProcessPermission {
	private static final long serialVersionUID = 3644859655330969141L;
	private String id;
	private String roleType;
	private String roles;
	private String users;
	private String groups;
	private String department;


	
	public ProcessPermission(String processName, String userPermission,String rolePermission, String groupPermission,String departmentPermission) {
		this.roles = rolePermission;
		this.users= userPermission;
		this.groups = groupPermission;
		this.department = departmentPermission;
		this.id = processName;
	
	}
	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
	
	@Id
	@Column(name = "ID" , nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	@Column(name = "ROLE_TYPE",length = 100)
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}	
}
