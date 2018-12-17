package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.activiti.engine.impl.db.PersistentObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import com.eazytec.util.DateUtil;

/**
 * This class is used to represent available departments in the database.
 *
 * @author madan
 */
@XmlRootElement
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "DEPARTMENT")
@NamedQueries({ @NamedQuery(name = "findDepartmentByName", query = "select r from Department r where r.name = :name ") })
public class Department extends BaseObject implements Serializable, GrantedAuthority, PersistentObject {
	private static final long serialVersionUID = 3690197650654049848L;
	private String id;
	private String name;
	private String viewName;
	private String description;
	private String departmentType;
	private boolean isParent;
	private String superDepartment;
	private String departmentOwner;
	private String userAction;
	private int version;
	private Date createdTime = new Date();
	private Set<User> administrators = new HashSet<User>();
	private Set<User> interfacePeople = new HashSet<User>();
	private String departmentId; // Transient variable for id

	private Set<Module> modules = new HashSet<Module>();
	private String departmentRole;
	private Integer orderNo;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Department() {
	}

	/**
	 * Create a new instance and set the name.
	 *
	 * @param name
	 *            name of the department.
	 */
	public Department(final String name) {
		this.name = name;
	}

	public Department(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@Column(name = "ID", length = 70)
	// @GeneratedValue(strategy = GenerationType.AUTO)
	public String getId() {
		return id;
	}

	/**
	 * @return the name property (getAuthority required by Acegi's
	 *         GrantedAuthority interface)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@JsonIgnore
	@Transient
	public String getAuthority() {
		return getName();
	}

	@Column(name = "NAME", length = 200)
	public String getName() {
		return this.name;
	}

	@Column(name = "DESCRIPTION", length = 250)
	public String getDescription() {
		return this.description;
	}

	@Column(name = "VIEW_NAME")
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String departmentType) {
		this.departmentType = departmentType;
	}

	@Transient
	@JsonIgnore
	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("name", name);
		persistentState.put("departmentType", departmentType);
		return persistentState;
	}

	@Column(name = "DEPARTMENT_TYPE", length = 64)
	public String getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}

	@Column(name = "IS_PARENT", nullable = false)
	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	@Column(name = "SUPER_DEPARTMENT")
	public String getSuperDepartment() {
		return superDepartment;
	}

	public void setSuperDepartment(String superDepartment) {
		this.superDepartment = superDepartment;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Department)) {
			return false;
		}

		final Department department = (Department) o;

		return !(name != null ? !name.equals(department.name) : department.name != null);

	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.name).toString();
	}

	public void setDepartmentOwner(String departmentOwner) {
		this.departmentOwner = departmentOwner;
	}

	@Column(name = "DEPARTMENT_OWNER")
	public String getDepartmentOwner() {
		return departmentOwner;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}

	
	@XmlTransient
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "DEPARTMENT_ADMINISTRATORS", joinColumns = { @JoinColumn(name = "DEPARTMENT_ID") }, inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<User> getAdministrators() {
		return administrators;
	}

	@XmlTransient
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "DEP_INTERFACE_PEOPLE", joinColumns = { @JoinColumn(name = "DEPARTMENT_ID") }, inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<User> getInterfacePeople() {
		return interfacePeople;
	}

	public void setAdministrators(Set<User> administrators) {
		this.administrators = administrators;
	}

	public void setInterfacePeople(Set<User> interfacePeople) {
		this.interfacePeople = interfacePeople;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

	@Column(name = "USER_ACTION")
	public String getUserAction() {
		return userAction;
	}

	@Transient
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}

	@Transient
	public String getDepartmentId() {
		departmentId = this.id;
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "ORDER_NO", length = 10)
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	@Transient
	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	@Column(name = "DEPARTMENT_ROLE", length = 250)
	public String getDepartmentRole() {
		return departmentRole;
	}

	public void setDepartmentRole(String departmentRole) {
		this.departmentRole = departmentRole;
	}

}