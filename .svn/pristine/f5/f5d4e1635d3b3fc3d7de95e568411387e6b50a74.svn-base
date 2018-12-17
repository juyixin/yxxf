package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "FORM_HISTORY")
public class Forms implements Serializable {
	private static final long serialVersionUID = -3210517110491216770L;
	private String id;
	private String formName;
	private String tableName;
	private boolean publicForm = false;
	private MetaTable table;
	private String htmlSource;
	private String htmlSourceView;
	private String xmlSource;
	private String createdBy;
	private Date createdOn = new Date();
	private int revision;
	private int version;
	private boolean active = false;
	private boolean templateForm = false;
	private Set<Module> formModules = new HashSet<Module>();
	private boolean isDelete = false;
	private String englishName;
	private String isEdit;
	private String isSystemModule;
	private String printTemplate;
	private boolean isJspForm = false;
	private String createdByFullName;
	private String description;
	private String backgroungImgPath;
	private String isDefaultForm;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Forms() {
	}

	public Forms clone() {
		Forms metaFormObj = new Forms();
		metaFormObj.setFormName(this.formName);
		metaFormObj.setTableName(this.tableName);
		metaFormObj.setPublicForm(this.publicForm);
		metaFormObj.setTable(this.table);
		metaFormObj.setHtmlSource(this.htmlSource);
		metaFormObj.setHtmlSourceView(this.htmlSourceView);
		metaFormObj.setXmlSource(this.xmlSource);
		metaFormObj.setCreatedBy(this.createdBy);
		metaFormObj.setRevisionNext(this.revision);
		metaFormObj.setVersion(this.version);
		metaFormObj.setActive(this.active);
		metaFormObj.setTemplateForm(this.templateForm);
		metaFormObj.setFormModules(this.formModules);
		metaFormObj.setIsDelete(this.isDelete);
		metaFormObj.setEnglishName(this.englishName);
		metaFormObj.setIsEdit(this.isEdit);
		metaFormObj.setIsSystemModule(this.isSystemModule);
		metaFormObj.setPrintTemplate(this.printTemplate);
		metaFormObj.setCreatedOn(this.createdOn);
		metaFormObj.setDescription(this.description);
		metaFormObj.setBackgroungImgPath(this.backgroungImgPath);
		return metaFormObj;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "FORM_NAME", length = 100)
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	@Column(name = "TABLE_NAME", length = 100)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	/* @JoinColumn(name = "TABLE_ID", nullable = false) */
	@JoinColumn(name = "TABLE_ID")
	public MetaTable getTable() {
		return table;
	}

	public void setTable(MetaTable table) {
		this.table = table;
	}

	@Column(name = "HTML_SOURCE", length = 8000)
	@Lob
	public String getHtmlSource() {
		return htmlSource;
	}

	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}

	@Column(name = "HTML_SOURCE_VIEW", length = 8000)
	@Lob
	public String getHtmlSourceView() {
		return htmlSourceView;
	}

	public void setHtmlSourceView(String htmlSourceView) {
		this.htmlSourceView = htmlSourceView;
	}

	@Column(name = "XML_SOURCE", length = 8000)
	@Lob
	public String getXmlSource() {
		return xmlSource;
	}

	public void setXmlSource(String xmlSource) {
		this.xmlSource = xmlSource;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CREATED_BY", length = 50)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "REVISION", nullable = false, length = 50, columnDefinition = "int default 1")
	public int getRevisionNext() {
		return revision + 1;
	}

	public void setRevisionNext(int revision) {
		this.revision = revision + 1;
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

	@Column(name = "IS_ACTIVE", length = 40)
	public boolean isActive() {
		return active;
	}

	@Column(name = "IS_PUBLIC")
	public boolean isPublicForm() {
		return publicForm;
	}

	public void setPublicForm(boolean publicForm) {
		this.publicForm = publicForm;
	}

	@Column(name = "IS_TEMPLATE", length = 40)
	public boolean isTemplateForm() {
		return templateForm;
	}

	public void setTemplateForm(boolean templateForm) {
		this.templateForm = templateForm;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "HISTORY_MODULE_FORMS", joinColumns = { @JoinColumn(name = "FROM_ID") }, inverseJoinColumns = @JoinColumn(name = "MODULE_ID"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Module> getFormModules() {
		return formModules;
	}

	public void setFormModules(Set<Module> formModules) {
		this.formModules = formModules;
	}

	@Column(name = "IS_DELETE")
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "ENGLISH_NAME", length = 100)
	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public void addModule(Module module) {
		getFormModules().add(module);
	}

	@Transient
	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	@Transient
	public String getIsSystemModule() {
		return isSystemModule;
	}

	public void setIsSystemModule(String isSystemModule) {
		this.isSystemModule = isSystemModule;
	}

	@Column(name = "PRINT_TEMPLATE", length = 8000)
	@Lob
	public String getPrintTemplate() {
		return printTemplate;
	}

	public void setPrintTemplate(String printTemplate) {
		this.printTemplate = printTemplate;
	}

	@Column(name = "IS_JSP_FORM")
	public boolean getIsJspForm() {
		return isJspForm;
	}

	public void setIsJspForm(boolean isJspForm) {
		this.isJspForm = isJspForm;
	}

	/**
	 * Returns the full name.
	 *
	 * @return firstName + ' ' + lastName
	 */
	@Transient
	public String getCreatedByFullName() {
		return createdByFullName;
	}

	/**
	 * Returns the full name.
	 *
	 * @return firstName + ' ' + lastName
	 */
	@Transient
	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}

	@Column(name = "BG_IMG_PATH")
	public String getBackgroungImgPath() {
		return backgroungImgPath;
	}

	public void setBackgroungImgPath(String backgroungImgPath) {
		this.backgroungImgPath = backgroungImgPath;
	}

	@Column(name = "IS_DEFAULT")
	public String getIsDefaultForm() {
		return isDefaultForm;
	}

	public void setIsDefaultForm(String isDefaultForm) {
		this.isDefaultForm = isDefaultForm;
	}
}
