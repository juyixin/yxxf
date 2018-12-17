/**
 * ========================
 * File Name  : Report.java
 * @author   : L.Parameswaran
 * ========================
 */
package com.eazytec.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "REPORT")

public class Report implements Serializable  {
	
	private static final long serialVersionUID = 4453338766237619444L;
	private String id;
	private String name;
	private String classification;
	private String reportUrl;
    private Set<Role> roles = new HashSet<Role>();
	private String description;
	private String status;
	private Set<Group> groups = new HashSet<Group>();
	private Set<Department> departments = new HashSet<Department>();;
	private String users;
	
	
	public Report() {}
	
	public Report( String id, String name) {
		
		this.id = id;
		this.name = name;
	}
	
	@Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	@Column(name = "reportName", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "classification")
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Column(name = "reportUrl")
	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ROLE_REPORT",
            joinColumns = { @JoinColumn(name = "REPORT_ID") },
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
public Set<Role> getRoles() {
	return roles;
}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@Column(name = "description",  length=8000)  @Lob
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_REPORT",
            joinColumns = { @JoinColumn(name = "REPORT_ID") },
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID")
    )
	public Set<Group> getGroups() {
		return groups;
	}

	
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "DEPARTMENT_REPORT",
            joinColumns = { @JoinColumn(name = "REPORT_ID") },
            inverseJoinColumns = @JoinColumn(name = "DEPARTMENT_ID")
    )
	public Set<Department> getDepartments() {
		return departments;
	}


	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	@Column(name = "users")
	public String getUsers() {
		return users;
	}

	
	public void setUsers(String users) {
		this.users = users;
	}

}
