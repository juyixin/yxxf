package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "RE_TABLE")
public class MetaTable implements Serializable {

	private static final long serialVersionUID = -4914623522785986992L;
	private String id;
	private String description;
	private String tableName;
	private String chineseName;
	private String createdBy;
	private Set<MetaTableColumns> metaTableColumns = new HashSet<MetaTableColumns>();
	private Set<MetaTableRelation> metaTableRelation = new HashSet<MetaTableRelation>();
	private Set<Module> modules = new HashSet<Module>();
	private boolean isAutoFormCreate = false;
	private Date createdOn = new Date();

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "TABLE_NAME", length = 100, unique = true)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "CHINESE_NAME", length = 100)
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setIsAutoFormCreate(boolean isAutoFormCreate) {
		this.isAutoFormCreate = isAutoFormCreate;
	}

	@Column(name = "IS_AUTOFORMCREATE")
	public boolean getIsAutoFormCreate() {
		return isAutoFormCreate;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "CREATED_BY", length = 50)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "table")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("orderNum asc")
	public Set<MetaTableColumns> getMetaTableColumns() {
		return metaTableColumns;
	}

	public void setMetaTableColumns(Set<MetaTableColumns> metaTableColumns) {
		this.metaTableColumns = metaTableColumns;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "table")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<MetaTableRelation> getMetaTableRelation() {
		return metaTableRelation;
	}

	public void setMetaTableRelation(Set<MetaTableRelation> metaTableRelation) {
		this.metaTableRelation = metaTableRelation;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "MODULE_TABLES", joinColumns = { @JoinColumn(name = "TABLE_ID") }, inverseJoinColumns = @JoinColumn(name = "MODULE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	/**
	 * Adds a Module for the table
	 *
	 * @param Module
	 */
	public void addModule(Module module) {
		getModules().add(module);
	}

	/**
	 * Remove a role for the user
	 *
	 * @param role
	 *            the fully instantiated role
	 */
	public void removeModule(Module module) {
		getModules().remove(module);
	}
}
