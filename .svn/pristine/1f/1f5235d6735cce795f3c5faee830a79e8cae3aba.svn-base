package com.eazytec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "PORTAL_WIDGET")
@NamedQueries({ @NamedQuery(name = "findWidgetByName", query = "select w from Widget w where w.name = :name ") })
public class Widget extends BaseObject implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 3690197650654049850L;
	private String id;
	private String name;
	private String widgetUrl;
	private boolean isActive = false;
	private String classify;
	private String linkType;
	private String contentLink;
	private String listView;
	private String report;
	public String documentId;
	public String selectedListView;
	private boolean isPublic = true;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Widget() {
	}

	/**
	 * Create a new instance and set the name.
	 *
	 * @param name
	 *            name of the Widget.
	 */
	public Widget(final String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
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

	@Column(name = "NAME", length = 200, unique = true)
	public String getName() {
		return this.name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWidgetUrl(String widgetUrl) {
		this.widgetUrl = widgetUrl;
	}

	@Column(name = "WIDGETURL")
	public String getWidgetUrl() {
		return widgetUrl;
	}

	@Column(name = "ISACTIVE")
	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "ISPUBLIC")
	public boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Widget)) {
			return false;
		}

		final Widget widget = (Widget) o;

		return !(name != null ? !name.equals(widget.name) : widget.name != null);

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

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getClassify() {
		return classify;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setContentLink(String contentLink) {
		this.contentLink = contentLink;
	}

	public String getContentLink() {
		return contentLink;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public String getListView() {
		return listView;
	}

	public void setSelectedListView(String selectedListView) {
		this.selectedListView = selectedListView;
	}

	@Column(name = "SELECTEDLISTVIEW")
	public String getSelectedListView() {
		return selectedListView;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name = "DOCUMENTID")
	public String getDocumentId() {
		return documentId;
	}

}
