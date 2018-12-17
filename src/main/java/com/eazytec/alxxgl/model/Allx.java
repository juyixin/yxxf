package com.eazytec.alxxgl.model;

import javax.persistence.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import com.eazytec.model.BaseObject;
import com.eazytec.util.DateUtil;

/**
 * @author easybpm
 * 
 */

@Entity
@Table(name = "yxxf_allx")
public class Allx extends BaseObject{
	private static final long serialVersionUID = 5454155825314635342L;

	private java.lang.String id;
	private java.lang.String lxdm;
	private java.lang.String lxmc;
	private java.lang.String sjlxdm;
	private java.lang.String bz;
	private java.util.Date createdTime;
	private java.util.Date lastModifyTime;
	private java.lang.String isActive;
	
	private String name;
	private String superDepartment;
	private String departmentType;
	private String viewName;
	// 123
	public Allx() {
	}

	public Allx(java.lang.String id) {
		this.id = id;
	}

	
	@Column(name = "NAME" )
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SUPER_DEPARTMENT")
	public String getSuperDepartment() {
		return superDepartment;
	}

	public void setSuperDepartment(String superDepartment) {
		this.superDepartment = superDepartment;
	}
	@Column(name = "DEPARTMENT_TYPE")
	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	@Column(name = "VIEW_NAME")
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String value) {
		this.id = value;
	}
	
	@Column(name = "LXDM", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getLxdm() {
		return this.lxdm;
	}

	public void setLxdm(java.lang.String value) {
		this.lxdm = value;
	}
	
	@Column(name = "LXMC", unique = false, nullable = true, insertable = true, updatable = true, length = 300)
	public java.lang.String getLxmc() {
		return this.lxmc;
	}

	public void setLxmc(java.lang.String value) {
		this.lxmc = value;
	}
	 
	@Column(name = "SJLXDM", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getSjlxdm() {
		return this.sjlxdm;
	}

	public void setSjlxdm(java.lang.String value) {
		this.sjlxdm = value;
	}
	
	 
	@Column(name = "BZ", unique = false, nullable = true, insertable = true, updatable = true, length = 300)
	public java.lang.String getBz() {
		return this.bz;
	}

	public void setBz(java.lang.String value) {
		this.bz = value;
	}
		
	@Column(name = "CREATED_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 11)
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(java.util.Date value) {
		this.createdTime = value;
	}


	@Column(name = "LAST_MODIFY_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 11)
	public java.util.Date getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(java.util.Date value) {
		this.lastModifyTime = value;
	}

	@Column(name = "IS_ACTIVE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public java.lang.String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(java.lang.String value) {
		this.isActive = value;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", getId()).append("Lxdm",getLxdm())
				.append("Lxmc", getLxmc())
				.append("Sjlxdm", getSjlxdm())
				.append("Bz", getBz())
				.append("superDepartment", getSuperDepartment())
				.append("CreatedTime", getCreatedTime())
				.append("LastModifyTime", getLastModifyTime())
				.append("IsActive", getIsActive()).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Allx == false)
			return false;
		if (this == obj)
			return true;
		Allx other = (Allx) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}
	@Transient
	public String getCreatedTimebyString() {
			return DateUtil.convertDateToString(createdTime);
		}
	

}
