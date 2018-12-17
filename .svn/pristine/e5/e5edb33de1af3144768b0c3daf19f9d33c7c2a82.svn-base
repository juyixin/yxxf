package com.eazytec.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "RE_LIST_VIEW_BUTTONS")
public class ListViewButtons implements Serializable {
	
	private static final long serialVersionUID = -2421745164018859449L;
	private String id;
	private String displayName;
	private String buttonMethod;
	private int orderBy = 0;
	private ListView listView;
	private String iconPath;
	private int version;
	private boolean active = false;
	private String tableName;
	private String columnName;
	private String redirectValue;
	private String listViewButtonRoles;
	private String listViewButtonUsers;
	private String listViewButtonUsersFullName;
	private String listViewButtonDeps;
	private String listViewButtonGroups;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public ListViewButtons() {
	}

	public ListViewButtons(List<Object> listViewButtons) {
		this.displayName = (String) listViewButtons.get(0);
		this.buttonMethod = (String) listViewButtons.get(1);

		if (listViewButtons.get(2) != "" && listViewButtons.get(2) != null) {
			this.orderBy = Integer.parseInt(listViewButtons.get(2).toString());
		}
	}

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

	@Column(name = "DISPLAY_NAME", length = 255)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "BUTTON_METHOD", length = 8000)
	@Lob
	public String getButtonMethod() {
		return buttonMethod;
	}

	public void setButtonMethod(String buttonMethod) {
		this.buttonMethod = buttonMethod;
	}

	@Column(name = "ORDER_BY", length = 10)
	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LIST_VIEW_ID", nullable = false)
	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@Column(name = "ICON_PATH")
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@Column(name = "VERSION", columnDefinition = "int default 1")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	@Column(name = "TABLE_NAME")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "COLUMN_NAME")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Column(name = "REDIRECT_VALUE")
	public String getRedirectValue() {
		return redirectValue;
	}

	public void setRedirectValue(String redirectValue) {
		this.redirectValue = redirectValue;
	}

	@Column(name = "LIST_VIEW_ROLES")
	public String getListViewButtonRoles() {
		return listViewButtonRoles;
	}

	public void setListViewButtonRoles(String listViewButtonRoles) {
		this.listViewButtonRoles = listViewButtonRoles;
	}

	@Column(name = "LIST_VIEW_USERS")
	public String getListViewButtonUsers() {
		return listViewButtonUsers;
	}

	public void setListViewButtonUsers(String listViewButtonUsers) {
		this.listViewButtonUsers = listViewButtonUsers;
	}

	@Column(name = "LIST_VIEW_USERS_NAME")
	public String getListViewButtonUsersFullName() {
		return listViewButtonUsersFullName;
	}

	public void setListViewButtonUsersFullName(String listViewButtonUsersFullName) {
		this.listViewButtonUsersFullName = listViewButtonUsersFullName;
	}

	@Column(name = "LIST_VIEW_DEPARTMENTS")
	public String getListViewButtonDeps() {
		return listViewButtonDeps;
	}

	public void setListViewButtonDeps(String listViewButtonDeps) {
		this.listViewButtonDeps = listViewButtonDeps;
	}

	@Column(name = "LIST_VIEW_GROUPS")
	public String getListViewButtonGroups() {
		return listViewButtonGroups;
	}

	public void setListViewButtonGroups(String listViewButtonGroups) {
		this.listViewButtonGroups = listViewButtonGroups;
	}

}
