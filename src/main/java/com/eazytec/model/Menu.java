package com.eazytec.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.activiti.engine.impl.db.PersistentObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

/**
 * This class is used to represent available menus in the database.
 *
 * @author madan
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "MENU")
@NamedQueries({ @NamedQuery(name = "findMenuByName", query = "select menu from Menu menu where menu.name = :name ") })
@XmlRootElement
public class Menu extends BaseObject implements Serializable, GrantedAuthority, PersistentObject {
	private static final long serialVersionUID = 3690197650654049848L;
	private String id;
	private String name;
	private String description;
	private String menuType;
	private boolean isParent;
	private String urlType;
	private String menuUrl;
	private Menu parentMenu;
	private String indexPage;
	private Set<Role> roles = new HashSet<Role>();
	private boolean active;
	private int menuOrder;
	private boolean isGlobal;
	private String helpText;
	private String iconPath1;
	private String iconPath2;
	private boolean isSystemDefined = false;
	private String listViewName;
	private String processName;
	private String listViewParam;
	private String reportName;
	private String reportParam;
	// private Set<Module> modules = new HashSet<Module>();

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Menu() {
	}

	/**
	 * Create a new instance and set the name.
	 *
	 * @param name
	 *            name of the menu.
	 */
	public Menu(final String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
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
	public String getAuthority() {
		return getName();
	}

	@Column(name = "NAME", length = 40)
	public String getName() {
		return this.name;
	}

	@Column(name = "DESCRIPTION", length = 64)
	public String getDescription() {
		return this.description;
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

	public void setType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "ICON_PATH1")
	@JsonIgnore
	public String getIconPath1() {
		return iconPath1;
	}

	public void setIconPath1(String iconPath1) {
		this.iconPath1 = iconPath1;
	}

	@Column(name = "ICON_PATH2")
	@JsonIgnore
	public String getIconPath2() {
		return iconPath2;
	}

	public void setIconPath2(String iconPath2) {
		this.iconPath2 = iconPath2;
	}

	@Transient
	@JsonIgnore
	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("name", name);
		persistentState.put("menuType", menuType);
		return persistentState;
	}

	@Column(name = "MENU_TYPE", length = 64)
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "IS_PARENT", nullable = false)
	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	@ManyToOne
	@JoinColumn(name = "PARENT_MENU")
	@JsonIgnore
	public Menu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	@Column(name = "INDEX_PAGE")
	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
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

	/**
	 * @return the urlType
	 */
	@Column(name = "URL_TYPE")
	public String getUrlType() {
		return urlType;
	}

	/**
	 * @param urlType
	 *            the urlType to set
	 */
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	@Column(name = "MENU_URL")
	public String getMenuUrl() {
		return menuUrl;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "IS_ACTIVE", length = 40)
	public boolean isActive() {
		return active;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	@Column(name = "MENU_ORDER", length = 40)
	public int getMenuOrder() {
		return menuOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Menu)) {
			return false;
		}

		final Menu menu = (Menu) o;

		return !(id != null ? !id.equals(menu.id) : menu.id != null);

	}

	public void setIsGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

	@Column(name = "IS_GLOBAL", nullable = false)
	public boolean getIsGlobal() {
		return isGlobal;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	@Column(name = "HELP_TEXT")
	public String getHelpText() {
		return helpText;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ROLE_MENU", joinColumns = { @JoinColumn(name = "MENU_ID") }, inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	@JsonIgnore
	public Set<Role> getRoles() {
		return roles;
	}

	public void setIsSystemDefined(boolean isSystemDefined) {
		this.isSystemDefined = isSystemDefined;
	}

	@Column(name = "IS_SYSTEMDEFINED")
	public boolean getIsSystemDefined() {
		return isSystemDefined;
	}

	@Column(name = "LIST_VIEW_NAME")
	public String getListViewName() {
		return listViewName;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	@Column(name = "PROCESS_NAME")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Column(name = "LIST_VIEW_PARAM")
	public String getListViewParam() {
		return listViewParam;
	}

	public void setListViewParam(String listViewParam) {
		this.listViewParam = listViewParam;
	}

	@Column(name = "REPORT_NAME")
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "REPORT_PARAM")
	public String getReportParam() {
		return reportParam;
	}

	public void setReportParam(String reportParam) {
		this.reportParam = reportParam;
	}

	/*
	 * @ManyToMany(fetch = FetchType.LAZY)
	 * 
	 * @JoinTable( name = "MENU_MODULE", joinColumns = { @JoinColumn(name =
	 * "MENU_ID") }, inverseJoinColumns = @JoinColumn(name = "MODULE_ID") )
	 * public Set<Module> getModules() { return modules; }
	 * 
	 * public void setModules(Set<Module> modules) { this.modules = modules; }
	 */

}