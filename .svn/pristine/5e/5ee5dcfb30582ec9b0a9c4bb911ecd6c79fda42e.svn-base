package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import com.eazytec.util.DateUtil;

/**
 * This class is used to represent available roles in the database.
 *
 * @author madan
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "ROLE")
@NamedQueries({ @NamedQuery(name = "findRoleByName", query = "select r from Role r where r.name = :name ") })
@XmlRootElement
public class Role extends BaseObject implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 3690197650654049848L;
	private String id;
	private String name;
	private String viewName;
	private String description;
	private String roleType;
	private String roleId; // Transient variable for id
	private int version;
	private Set<Menu> menus = new HashSet<Menu>();
	private Set<User> users = new HashSet<User>();
	private Department roleDepartment;
	private Date createdTime = new Date();
	private Set<Module> modules = new HashSet<Module>();
	private Integer orderNo;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Role() {
	}

	/**
	 * Create a new instance and set the name.
	 *
	 * @param name
	 *            name of the role.
	 */
	public Role(final String name) {
		this.name = name;
	}

	/**
	 * Create a new instance and set the name.
	 *
	 * @param id
	 *            id of the role
	 * @param name
	 *            name of the role.
	 */
	public Role(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Role(String id, String name, String roleType) {
		this.id = id;
		this.name = name;
		this.roleType = roleType;
	}

	@Id
	@Column(name = "ID", length = 70) // @GeneratedValue(generator="system-uuid")
	// @GenericGenerator(name="system-uuid", strategy = "uuid")
	@JsonIgnore
	public String getId() {
		return id;
	}

	/**
	 * @return the name property (getAuthority required by Acegi's
	 *         GrantedAuthority interface)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Transient
	@JsonIgnore
	public String getAuthority() {
		return getName();
	}

	@Column(name = "NAME", length = 200)
	public String getName() {
		return this.name;
	}
	
	@Column(name = "VIEW_NAME", length = 200)
	public String getViewName() {
		return viewName;
	}

	@Column(name = "DESCRIPTION", length = 250)
	@JsonIgnore
	public String getDescription() {
		return this.description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Role)) {
			return false;
		}

		final Role role = (Role) o;

		return !(name != null ? !name.equals(role.name) : role.name != null);

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

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
	@JsonIgnore
	public Set<Menu> getMenus() {
		return menus;
	}

	/**
	 * Adds a role for the user
	 *
	 * @param role
	 *            the fully instantiated role
	 */
	public void addMenu(Menu menu) {
		getMenus().add(menu);
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Column(name = "ROLE_TYPE")
	@JsonIgnore
	public String getRoleType() {
		return roleType;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void setRoleDepartment(Department roleDepartment) {
		this.roleDepartment = roleDepartment;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_DEPARTMENT")
	@JsonIgnore
	public Department getRoleDepartment() {
		return roleDepartment;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "CREATED_TIME")
	@JsonIgnore
	public Date getCreatedTime() {
		return createdTime;
	}

	@Transient
	@JsonIgnore
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}

	@JsonIgnore
	@Transient
	public String getRoleId() {
		roleId = this.id;
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Transient
	@JsonIgnore
	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	@Column(name = "ORDER_NO", length = 10)
	@JsonIgnore
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/*
	 * @ManyToMany(fetch = FetchType.LAZY)
	 * 
	 * @JoinTable( name = "ROLE_MODULE", joinColumns = { @JoinColumn(name =
	 * "ROLE_ID") }, inverseJoinColumns = @JoinColumn(name = "MODULE_ID") )
	 * public Set<Module> getModules() { return modules; }
	 * 
	 * public void setModules(Set<Module> modules) { this.modules = modules; }
	 */
}