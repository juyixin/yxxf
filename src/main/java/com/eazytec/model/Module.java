package com.eazytec.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class is used to schedule the events.
 *
 * @author madan
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "MODULE")
public class Module extends BaseObject {

	private static final long serialVersionUID = -7056791312197504872L;
	private String id;
	private String name;
	private String description;
	private int moduleOrder;
	private Date createdTime;
	private Date updatedTime;
	private String createdBy;
	private String updatedBy;

	private boolean isEdit = false;
	private boolean isSystemModule = false;
	private boolean isPublic = false;

	private Set<User> administrators = new HashSet<User>();
	private Set<Department> departments = new HashSet<Department>();
	private Set<Role> roles = new HashSet<Role>();
	private Set<Group> groups = new HashSet<Group>();

	private Set<User> viewAdministrators = new HashSet<User>();
	private Set<Department> viewDepartments = new HashSet<Department>();
	private Set<Role> viewRoles = new HashSet<Role>();
	private Set<Group> viewGroups = new HashSet<Group>();
	private Set<MetaForm> forms = new HashSet<MetaForm>();
	private Set<Forms> historyForms = new HashSet<Forms>();

	private Set<MetaTable> tables = new HashSet<MetaTable>();
	private Set<ListView> listViews = new HashSet<ListView>();
	private Set<ModuleRolePrivilege> modleRoles = new HashSet<ModuleRolePrivilege>();
	
	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Module() {
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "modules")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<ListView> getListViews() {
		return new HashSet<ListView>(listViews);
	}

	public void setListViews(Set<ListView> listViews) {
		this.listViews = listViews;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "modules")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<MetaForm> getForms() {
		return new HashSet<MetaForm>(forms);
	}

	public void setForms(Set<MetaForm> forms) {
		this.forms = forms;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "formModules")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Forms> getHistoryForms() {
		return historyForms;
	}

	public void setHistoryForms(Set<Forms> historyForms) {
		this.historyForms = historyForms;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "modules")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<MetaTable> getTables() {
		return new HashSet<MetaTable>(tables);
	}

	public void setTables(Set<MetaTable> tables) {
		this.tables = tables;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "MODULE_ID")
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the eventName
	 */

	@Column(name = "NAME", unique = true)
	public String getName() {
		return name;
	}

	/**
	 * @param eventName
	 *            the eventName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "MODULE_ORDER", columnDefinition = "int default 1")
	public int getModuleOrder() {
		return moduleOrder;
	}

	public void setModuleOrder(int moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

	@Transient
	public Set<User> getAdministrators() {
		return administrators;
	}

	public void setAdministrators(Set<User> administrators) {
		this.administrators = administrators;
	}

	@Transient
	public Set<User> getViewAdministrators() {
		return viewAdministrators;
	}

	public void setViewAdministrators(Set<User> viewAdministrators) {
		this.viewAdministrators = viewAdministrators;
	}

	@Transient
	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Transient
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Transient
	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Module)) {
			return false;
		}

		final Module schedule = (Module) o;

		return !(id != null ? !id.equals(schedule.id) : schedule.id != null);

	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.id).toString();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<ModuleRolePrivilege> getModleRoles() {
		return modleRoles;
	}

	public void setModleRoles(Set<ModuleRolePrivilege> modleRoles) {
		this.modleRoles = modleRoles;
	}

	@Transient
	public Set<Department> getViewDepartments() {
		return viewDepartments;
	}

	public void setViewDepartments(Set<Department> viewDepartments) {
		this.viewDepartments = viewDepartments;
	}

	@Transient
	public Set<Role> getViewRoles() {
		return viewRoles;
	}

	public void setViewRoles(Set<Role> viewRoles) {
		this.viewRoles = viewRoles;
	}

	@Transient
	public Set<Group> getViewGroups() {
		return viewGroups;
	}

	public void setViewGroups(Set<Group> viewGroups) {
		this.viewGroups = viewGroups;
	}

	@Transient
	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	@Column(name = "IS_SYSTEM_MODULE")
	public boolean getIsSystemModule() {
		return isSystemModule;
	}

	public void setIsSystemModule(boolean isSystemModule) {
		this.isSystemModule = isSystemModule;
	}

	@Column(name = "IS_PUBLIC")
	public boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
