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

import org.activiti.engine.impl.db.PersistentObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import com.eazytec.util.DateUtil;

/**
 * This class is used to represent available groups in the database.
 *
 * @author madan
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "GROUP")
@NamedQueries({ @NamedQuery(name = "findGroupByName", query = "select r from Group r where r.name = :name ") })
public class Group extends BaseObject implements Serializable, GrantedAuthority, PersistentObject, org.activiti.engine.identity.Group {
	private static final long serialVersionUID = 3690197650654049848L;
	private String id;
	private String name;
	private String viewName;
	private String description;
	private int revision;
	private String groupType;
	private String superGroup;
	private boolean isParent;
	private String personIncharge;// single select
	private String leader;// single select
	private Set<User> administrators = new HashSet<User>();
	private Set<User> interfacePeople = new HashSet<User>();
	private String remark;
	private Date createdTime = new Date();
	private int version;
	private Set<User> users = new HashSet<User>();
	private String groupId; // Transient variable for id
	private Set<Module> modules = new HashSet<Module>();
	private String groupRole;

	private Integer orderNo;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Group() {
	}

	/**
	 * Create a new instance and set the name.
	 *
	 * @param name
	 *            name of the group.
	 */
	public Group(final String name) {
		this.name = name;
	}

	/**
	 * Create a new instance and set the name and id.
	 *
	 * @param id
	 *            id of the group.
	 * @param name
	 *            name of the group.
	 */
	public Group(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	/**
	 * @return the name property (getAuthority required by Acegi's
	 *         GrantedAuthority interface)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
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

	@Column(name = "GROUP_TYPE")
	public String getType() {
		return groupType;
	}

	@Transient
	public int getRevision() {
		return revision;
	}

	@Column(name = "SUPER_GROUP")
	public String getSuperGroup() {
		return superGroup;
	}

	@Column(name = "PERSON_INCHARGE")
	public String getPersonIncharge() {
		return personIncharge;
	}

	@Column(name = "LEADER")
	public String getLeader() {
		return leader;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "GROUP_ADMINISTRATORS", joinColumns = { @JoinColumn(name = "GROUP_ID") }, inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<User> getAdministrators() {
		return administrators;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "GROUP_INTERFACE_PEOPLE", joinColumns = { @JoinColumn(name = "GROUP_ID") }, inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<User> getInterfacePeople() {
		return interfacePeople;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}

	@ManyToMany(mappedBy = "groups")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<User> getUsers() {
		return users;
	}

	@Column(name = "VIEW_NAME")
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void setSuperGroup(String superGroup) {
		this.superGroup = superGroup;
	}

	public void setPersonIncharge(String persionIncharge) {
		this.personIncharge = persionIncharge;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public void setAdministrators(Set<User> administrators) {
		this.administrators = administrators;
	}

	public void setInterfacePeople(Set<User> interfacePeople) {
		this.interfacePeople = interfacePeople;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public void setType(String groupType) {
		this.groupType = groupType;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Transient
	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("name", name);
		persistentState.put("groupType", groupType);
		return persistentState;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Group)) {
			return false;
		}

		final Group group = (Group) o;

		return !(name != null ? !name.equals(group.name) : group.name != null);

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

	@Column(name = "IS_PARENT", nullable = false)
	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	@Transient
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}

	@Transient
	public String getGroupId() {
		groupId = this.id;
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/* @ManyToMany(fetch = FetchType.LAZY,mappedBy = "groups") */
	@Transient
	public Set<Module> getModules() {
		return modules;
	}

	@Column(name = "ORDER_NO", length = 10)
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	@Column(name = "GROUP_ROLE", length = 250)
	public String getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}
}