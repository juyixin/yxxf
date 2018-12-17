package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "FORM")
public class Form implements Serializable {

	private static final long serialVersionUID = -8988082829969528622L;
	private String id;
	private String formName;
	private String description;
	private String resourceName;
	private String xmlSource;
	private String xmlString;
	private String htmlSource;
	private String htmlString;
	private Integer version;
	private String createdBy;
	private Date createdOn = new Date();
	private boolean active;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Form() {
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	@Column(name = "FORM_NAME", length = 100)
	public String getFormName() {
		return formName;
	}

	@Column(name = "RESOURCE_NAME", length = 100)
	public String getResourceName() {
		return resourceName;
	}

	@Column(name = "XML_SOURCE", length = 8000)
	@Lob
	public String getXmlSource() {
		return xmlSource;
	}

	@Column(name = "HTML_SOURCE", length = 8000)
	@Lob
	public String getHtmlSource() {
		return htmlSource;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setXmlSource(String xmlSource) {
		this.xmlSource = xmlSource;
	}

	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}

	@Column(name = "VERSION", columnDefinition = "int default 1")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Transient
	public String getXmlString() {

		// byte[] xmlBlob = this.getXmlSource();
		String stringValue = this.getXmlSource();
		// if(xmlBlob != null && xmlBlob.length != 0) {
		if (stringValue != null && stringValue.length() != 0) {
			// String stringValue = new String(xmlBlob);
			xmlString = stringValue.replaceAll("&lt;", "<");
			xmlString = xmlString.replaceAll("&gt;", ">");
		}
		return xmlString;

	}

	@Transient
	public String getHtmlString() {

		// byte[] htmlBlob = this.getHtmlSource();
		String stringValue = this.getHtmlSource();
		if (stringValue != null && stringValue.length() != 0) {
			// String stringValue = new String(htmlBlob);
			htmlString = stringValue.replaceAll("&lt;", "<");
			htmlString = htmlString.replaceAll("&gt;", ">");
		}
		return htmlString;

	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedOn() {
		return createdOn;
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

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "IS_ACTIVE", length = 40)
	public boolean isActive() {
		return active;
	}

}