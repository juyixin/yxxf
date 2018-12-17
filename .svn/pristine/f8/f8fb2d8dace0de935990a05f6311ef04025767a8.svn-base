package com.eazytec.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.activiti.engine.identity.Picture;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class represents the basic "user" object with authentication and user
 * management implementing Acegi Security's UserDetails interface.
 *
 * @author madan
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "USER")
@XmlRootElement
public class User extends BaseObject implements Serializable, UserDetails, PersistentObject, org.activiti.engine.identity.User {
	private static final long serialVersionUID = 3832626162173359411L;

	private String id;
	private String username; // required
	private String password; // required
	private String confirmPassword;
	private String emailPassword; // required
	private String passwordHint;
	private String firstName; // required
	private String lastName; // required
	private String englishName;
	private String employeeNumber;
	private String idCardNumber;
	private String sex;
	private String organizationName;
	private String position;
	private String partTimePosition;
	private String manager;
	private String secretary;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Group> groups = new HashSet<Group>();
	private Department department;
	private Department partTimeDepartment;
	private String email; // unique
	private String mobile;
	private String workPhone;
	private String homePhone;
	private String fax;
	private String website;
	private Address address = new Address();
	private String whereTheWork;
	private String comment;
	private String resposibilities;
	private String signature;
	private int version;
	private boolean enabled = false;
	private boolean accountExpired = false;
	private boolean accountLocked = false;
	private boolean credentialsExpired = false;
	private boolean sendEmailNotification = false;
	protected String pictureByteArrayId;
	protected ByteArrayEntity pictureByteArray;
	private Set<Group> groupByAdministrators;
	private Set<Group> groupByInterfacePeople;
	private Set<Department> departmentByAdministrators;
	private Set<Department> departmentByInterfacePeople;
	private Set<Schedule> eventUsers;
	private boolean canDelete = true;
	private String userRole;
	private String preferredSkin;
	private Date dateOfBirth;
	private String language;
	private String fullName;
	private String token;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public User() {

	}

	/**
	 * Create a new instance and set the username.
	 *
	 * @param username
	 *            login name for user.
	 */
	public User(final String username) {
		this.username = username;
	}

	/**
	 * Create a new instance and set the username.
	 *
	 * @param username
	 *            login name for user.
	 */
	public User(String id, String username) {
		this.id = id;
		this.username = username;
	}

	public User(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	@Column(name = "USERNAME", nullable = false, length = 50, unique = true)
	public String getUsername() {
		return username;
	}

	@Column(name = "PASSWORD", nullable = false)
	@XmlTransient
	public String getPassword() {
		return password;
	}

	@Transient
	public String getConfirmPassword() {
		return confirmPassword;
	}

	@Column(name = "PASSWORD_HINT")
	@XmlTransient
	@JsonIgnore
	public String getPasswordHint() {
		return passwordHint;
	}

	@Column(name = "FIRST_NAME", length = 50)
	public String getFirstName() {
		return fullName;
	}

	@Column(name = "LAST_NAME", length = 50)
	public String getLastName() {
		return "";
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	@Column(name = "WORK_PHONE")
	public String getWorkPhone() {
		return workPhone;
	}

	@Column(name = "WEBSITE")
	public String getWebsite() {
		return website;
	}

	@Column(name = "PREFERREDSKIN")
	public String getPreferredSkin() {
		return preferredSkin;
	}

	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return language;
	}

	/**
	 * Returns the full name.
	 *
	 * @return firstName + ' ' + lastName
	 */
	@Column(name = "FULL_NAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "ADDRESS")
	@Embedded
	public Address getAddress() {
		return address;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * Convert user roles to LabelValue objects for convenience.
	 *
	 * @return a list of LabelValue objects with role information
	 */
	@Transient
	public List<LabelValue> getRoleList() {
		List<LabelValue> userRoles = new ArrayList<LabelValue>();

		if (this.roles != null) {
			for (Role role : roles) {
				// convert the user's roles to LabelValue Objects
				userRoles.add(new LabelValue(role.getName(), role.getId()));
			}
		}

		return userRoles;
	}

	/**
	 * Adds a role for the user
	 *
	 * @param role
	 *            the fully instantiated role
	 */
	public void addRole(Role role) {
		getRoles().add(role);
	}

	/**
	 * Remove a role for the user
	 *
	 * @param role
	 *            the fully instantiated role
	 */
	public void removeRole(Role role) {
		getRoles().remove(role);
	}

	/**
	 * @return GrantedAuthority[] an array of roles.
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Transient
	@JsonIgnore
	public Set<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
		authorities.addAll(roles);
		return authorities;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@XmlTransient
	@JsonIgnore
	@JoinTable(name = "MEMBERSHIP", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Group> getGroups() {
		return groups;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	@XmlTransient
	@JoinColumn(name = "DEPARTMENT")
	public Department getDepartment() {
		return department;
	}

	/**
	 * Convert user groups to LabelValue objects for convenience.
	 *
	 * @return a list of LabelValue objects with group information
	 */
	@Transient
	public List<LabelValue> getGroupList() {
		List<LabelValue> userGroups = new ArrayList<LabelValue>();

		if (this.groups != null) {
			for (Group group : groups) {
				// convert the user's groups to LabelValue Objects
				userGroups.add(new LabelValue(group.getName(), group.getId()));
			}
		}

		return userGroups;
	}

	/**
	 * Convert user departments to LabelValue objects for convenience.
	 *
	 * @return a list of LabelValue objects with department information
	 */
	@Transient
	public List<LabelValue> getDepartmentList() {
		List<LabelValue> userDepartments = new ArrayList<LabelValue>();

		if (this.department != null) {
			// convert the user's groups to LabelValue Objects
			userDepartments.add(new LabelValue(department.getName(), department.getId()));
		}

		return userDepartments;
	}

	/**
	 * Convert user part time departments to LabelValue objects for convenience.
	 *
	 * @return a list of LabelValue objects with department information
	 */
	@Transient
	public List<LabelValue> getPartTimeDepartmentList() {
		List<LabelValue> userPartTimeDepartment = new ArrayList<LabelValue>();

		if (this.partTimeDepartment != null) {
			// convert the user's groups to LabelValue Objects
			userPartTimeDepartment.add(new LabelValue(partTimeDepartment.getName(), partTimeDepartment.getId()));
		}

		return userPartTimeDepartment;
	}

	@Transient
	public List<String> getRoleIds() {
		List<String> roleNames = new ArrayList<String>();

		if (this.roles != null) {
			for (Role role : roles) {
				// convert the user's groups to LabelValue Objects
				roleNames.add(role.getId());
			}
		}

		return roleNames;
	}

	@Transient
	public List<String> getGroupNames() {
		List<String> groupNames = new ArrayList<String>();

		if (this.groups != null) {
			for (Group group : groups) {
				// convert the user's groups to LabelValue Objects
				groupNames.add(group.getName());
			}
		}

		return groupNames;
	}

	@Transient
	public List<String> getGroupIds() {
		List<String> groupNames = new ArrayList<String>();

		if (this.groups != null) {
			for (Group group : groups) {
				// convert the user's groups to LabelValue Objects
				groupNames.add(group.getId());
			}
		}

		return groupNames;
	}

	/**
	 * Adds a group for the user
	 *
	 * @param group
	 *            the fully instantiated group
	 */
	public void addGroup(Group group) {
		getGroups().add(group);
	}
	
	@Column(name = "IS_ENABLED")
	public boolean getEnabled() {
		return enabled;
	}

	@Transient
	public boolean isEnabled() {
		return enabled;
	}
	
	@Column(name = "IS_EXPIRED", nullable = false)
	public boolean getAccountExpired() {
		return accountExpired;
	}

	@Transient
	public boolean isAccountExpired() {
		return accountExpired;
	}

	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 * @return true if account is still active
	 */
	@Transient
	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}
	
	@Column(name = "IS_LOCKED", nullable = false)
	public boolean getAccountLocked() {
		return accountLocked;
	}

	@Transient
	public boolean isAccountLocked() {
		return accountLocked;
	}

	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 * @return false if account is locked
	 */
	@Transient
	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}
	
	@Column(name = "IS_CREDENTIAL_EXPIRED", nullable = false)
	public boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	@Transient
	public boolean isCredentialsExpired() {
		return getCredentialsExpired();
	}

	@Column(name = "PICTURE_ID")
	public String getPictureByteArrayId() {
		return pictureByteArrayId;
	}

	/**
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 * @return true if credentials haven't expired
	 */
	@Transient
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public void setFirstName(String firstName) {
		this.firstName = fullName;
	}

	public void setLastName(String lastName) {
		this.lastName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setPreferredSkin(String preferredSkin) {
		this.preferredSkin = preferredSkin;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setPictureByteArrayId(String pictureByteArrayId) {
		this.pictureByteArrayId = pictureByteArrayId;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@Column(name = "ID_CARD_NUMBER")
	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	@Column(name = "SEX")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "ORGANIZATION_NAME")
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "MANAGER")
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	@Column(name = "SECRETARY")
	public String getSecretary() {
		return secretary;
	}

	public void setSecretary(String secretary) {
		this.secretary = secretary;
	}

	@Column(name = "HOME_PHONE")
	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@Column(name = "FAX")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "WHERE_THE_WORK")
	public String getWhereTheWork() {
		return whereTheWork;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "administrators")
	@JsonIgnore
	@XmlTransient
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Group> getGroupByAdministrators() {
		return groupByAdministrators;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "interfacePeople")
	@JsonIgnore
	@XmlTransient
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Group> getGroupByInterfacePeople() {
		return groupByInterfacePeople;
	}

	public void setGroupByAdministrators(Set<Group> groupByAdministrators) {
		this.groupByAdministrators = groupByAdministrators;
	}

	public void setGroupByInterfacePeople(Set<Group> groupByInterfacePeople) {
		this.groupByInterfacePeople = groupByInterfacePeople;
	}

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "administrators")
	@JsonIgnore
	@XmlTransient
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Department> getDepartmentByAdministrators() {
		return departmentByAdministrators;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "interfacePeople")
	@JsonIgnore
	@XmlTransient
	@Transactional
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Department> getDepartmentByInterfacePeople() {
		return departmentByInterfacePeople;
	}

	public void setDepartmentByAdministrators(Set<Department> departmentByAdministrators) {
		this.departmentByAdministrators = departmentByAdministrators;
	}

	public void setDepartmentByInterfacePeople(Set<Department> departmentByInterfacePeople) {
		this.departmentByInterfacePeople = departmentByInterfacePeople;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	@JsonIgnore
	@XmlTransient
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Schedule> getEventUsers() {
		return eventUsers;
	}

	public void setEventUsers(Set<Schedule> eventUsers) {
		this.eventUsers = eventUsers;
	}

	public void setWhereTheWork(String whereTheWork) {
		this.whereTheWork = whereTheWork;
	}

	@Column(name = "USER_COMMENT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "RESPONSIBILITIES")
	public String getResposibilities() {
		return resposibilities;
	}

	public void setResposibilities(String resposibilities) {
		this.resposibilities = resposibilities;
	}

	@Transient
	@JsonIgnore
	public Picture getPicture() {
		if (pictureByteArrayId == null) {
			return null;
		}
		ByteArrayEntity pictureByteArray = getPictureByteArray();
		Picture picture = null;
		if (pictureByteArray != null) {
			picture = new Picture(pictureByteArray.getBytes(), pictureByteArray.getName());
		}
		return picture;
	}

	public void setPicture(Picture picture) {
		if (pictureByteArrayId != null) {
			Context.getCommandContext().getByteArrayEntityManager().deleteByteArrayById(pictureByteArrayId);
		}
		if (picture != null) {
			pictureByteArray = new ByteArrayEntity(picture.getMimeType(), picture.getBytes());
			Context.getCommandContext().getDbSqlSession().insert(pictureByteArray);
			pictureByteArrayId = pictureByteArray.getId();
		} else {
			pictureByteArrayId = null;
			pictureByteArray = null;
		}
	}

	@Transient
	private ByteArrayEntity getPictureByteArray() {
		/*
		 * if (pictureByteArrayId!=null && pictureByteArray==null) {
		 * pictureByteArray = Context .getCommandContext() .getDbSqlSession()
		 * .selectById(ByteArrayEntity.class, pictureByteArrayId); }
		 */
		return pictureByteArray;
	}

	@Transient
	@JsonIgnore
	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("firstName", firstName);
		persistentState.put("lastName", lastName);
		persistentState.put("email", email);
		persistentState.put("password", password);
		persistentState.put("pictureByteArrayId", pictureByteArrayId);
		return persistentState;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}

		final User user = (User) o;

		return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);

	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (username != null ? username.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.username).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSendEmailNotification(boolean sendEmailNotification) {
		this.sendEmailNotification = sendEmailNotification;
	}
	
	@Column(name = "IS_SEND_EMAIL_NOTIFICATION")
	public boolean getSendEmailNotification() {
		return sendEmailNotification;
	}
	
	@Transient
	public boolean isSendEmailNotification() {
		return sendEmailNotification;
	}

	public void setPartTimePosition(String partTimePosition) {
		this.partTimePosition = partTimePosition;
	}

	public String getPartTimePosition() {
		return partTimePosition;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setPartTimeDepartment(Department partTimeDepartment) {
		this.partTimeDepartment = partTimeDepartment;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	@XmlTransient
	@JoinColumn(name = "PART_TIME_DEPARTMENT")
	public Department getPartTimeDepartment() {
		return partTimeDepartment;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	@Column(name = "CAN_DELETE")
	public boolean getCanDelete() {
		return canDelete;
	}

	@Column(name = "SIGNATURE")
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "EMAIL_PASSWORD")
	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	@Column(name = "USER_ROLE", length = 250)
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}