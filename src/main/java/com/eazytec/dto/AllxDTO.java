package com.eazytec.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AllxDTO {

	private String id;
	private String name;
	private String superDepartment;
	private String departmentType;
	private String lxmc;
	private String lxdm;
	

	public AllxDTO(String id,String name,String superDepartment,String departmentType,String lxdm, String viewName) {
		this.id=id;
		this.name=name;
		this.lxdm=lxdm;
		this.superDepartment=superDepartment;
		this.departmentType=departmentType;
		this.lxmc = viewName;
	}
	public String getLxdm() {
		return lxdm;
	}
	public void setLxdm(String lxdm) {
		this.lxdm = lxdm;
	}
	public String getLxmc() {
		return lxmc;
	}
	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}	
	 
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSuperDepartment() {
		return superDepartment;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSuperDepartment(String superDepartment) {
		this.superDepartment = superDepartment;
	}
	public void setType(String departmentType) {
		this.departmentType = departmentType;
	}
	public String getDepartmentType() {
		return departmentType;
	}     
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(
				this.id).toString();
	}
}
