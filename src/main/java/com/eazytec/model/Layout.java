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
 * This class used to represents the available layout in database for home
 * screen
 * 
 * @author revathi
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "PORTAL_LAYOUT")
public class Layout extends BaseObject {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int noOfWidget;
	private boolean status;
	private String columnWidget;
	private String menuType;
	private String menuLink;
	private String assignedTo;

	/**
	 * constuctor
	 */
	public Layout() {
	}

	/**
	 * constuctor
	 * 
	 * @param id
	 * @param name
	 * @param noOfWidget
	 * @param status
	 * @param columnWidget
	 * @param menuType
	 * @param menuLink
	 * @param assignedTo
	 */
	public Layout(String name, int noOfWidget, boolean status, String columnWidget, String menuType, String menuLink, String assignedTo) {
		super();
		this.name = name;
		this.noOfWidget = noOfWidget;
		this.status = status;
		this.columnWidget = columnWidget;
		this.menuType = menuType;
		this.menuLink = menuLink;
		this.assignedTo = assignedTo;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	@Transient
	public String getMenuType() {
		return menuType;
	}

	@Transient
	public String getMenuLink() {
		return menuLink;
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

	@Column(name = "NO_OF_WIDGET")
	public int getNoOfWidget() {
		return noOfWidget;
	}

	public void setNoOfWidget(int noOfWidget) {
		this.noOfWidget = noOfWidget;
	}

	@Column(name = "STATUS")
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Layout)) {
			return false;
		}

		final Layout schedule = (Layout) o;

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

	public void setColumnWidget(String columnWidget) {
		this.columnWidget = columnWidget;
	}

	public String getColumnWidget() {
		return columnWidget;
	}

	/**
	 * @return the assignedTo
	 */
	@Column(name = "Assigned_TO")
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo
	 *            the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

}
