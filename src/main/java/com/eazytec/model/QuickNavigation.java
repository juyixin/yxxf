package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class used to represent quick navigation items for home page quick
 * navigation menus
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "PORTAL_QUICK_NAVIGATION")
public class QuickNavigation extends BaseObject {
	private static final long serialVersionUID = 4087961050960305923L;
	private String id;
	private String name;
	private int sortOrder;
	private String iconPath;
	private String quickNavigationUrl;
	private boolean status;
	private String menuType;
	private String menuLink;
	private String menuId;
	private String rootNodeId;
	private String rootNodeName;
	private String urlType;
	private String processName;
	private String listViewName;

	@Column(name = "PROCESS_NAME")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Transient
	public String getMenuType() {
		return menuType;
	}

	@Transient
	public String getMenuLink() {
		return menuLink;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
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

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STATUS", nullable = false)
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Column(name = "SORT_ORDER")
	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "ICON_PATH")
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@Column(name = "quickNavigationUrl")
	public String getQuickNavigationUrl() {
		return quickNavigationUrl;
	}

	public void setQuickNavigationUrl(String quickNavigationUrl) {
		this.quickNavigationUrl = quickNavigationUrl;
	}

	/**
	 * @return the urlType
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QuickNavigation)) {
			return false;
		}

		final QuickNavigation quickNav = (QuickNavigation) o;

		return !(id != null ? !id.equals(quickNav.id) : quickNav.id != null);

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

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	@Column(name = "MENU_ID")
	public String getMenuId() {
		return menuId;
	}

	@Column(name = "ROOT_NODE_ID")
	public String getRootNodeId() {
		return rootNodeId;
	}

	public void setRootNodeId(String rootNodeId) {
		this.rootNodeId = rootNodeId;
	}

	@Column(name = "ROOT_NODE_NAME")
	public String getRootNodeName() {
		return rootNodeName;
	}

	public void setRootNodeName(String rootNodeName) {
		this.rootNodeName = rootNodeName;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	@Column(name = "LIST_VIEW_NAME")
	public String getListViewName() {
		return listViewName;
	}

}
