package com.eazytec.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DepartmentDTO {

	private String id;
	private String name;
	private String superDepartment;
	private String departmentType;
	private boolean isParent;
	private String viewName;
	

	public DepartmentDTO(String id,String name,String superDepartment,String departmentType,boolean isParent,String viewName) {
		this.id=id;
		this.name=name;
		this.superDepartment=superDepartment;
		this.departmentType=departmentType;
		this.isParent=isParent;
		this.viewName = viewName;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}	
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
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
