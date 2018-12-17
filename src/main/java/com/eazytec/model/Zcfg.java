package com.eazytec.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import com.eazytec.fileManage.model.FileManage;
import com.eazytec.util.DateUtil;



@Entity
@Table(name = "YXXF_ZCFG")
public class Zcfg extends BaseObject{
	private static final long serialVersionUID = 5454155825314635342L;
	
	private java.lang.String id;

	private java.lang.String sffb;
	
	

	private java.lang.String title;


	
	private java.lang.String content;


	
	private java.lang.String createperson;


	
	private java.lang.String createdept;


	
	private java.lang.String formId;


	
	private java.lang.String toperson;


	
	private java.lang.String dataType;


	
	private java.util.Date createtime;

	private String createtimeByString;

	
	private java.lang.String comments;
	
	private java.lang.Long dataYear;

	private List<MultipartFile> files;

	private List<MultipartFile> myfiles;
	
	private List<FileManage> documents;
	
	private java.lang.String isRead;

	//columns END
	


	//123
	public Zcfg(){
	}

	public Zcfg(
		java.lang.String id
	){
		this.id = id;
	}

	

	public void setId(java.lang.String value) {
		this.id = value;
	}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
	public java.lang.String getId() {
		return this.id;
	}
	
	@Column(name = "TITLE", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public java.lang.String getTitle() {
		return this.title;
	}
	
	public void setTitle(java.lang.String value) {
		this.title = value;
	}
	@Column(name = "SFFB", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public java.lang.String getSffb() {
		return sffb;
	}

	public void setSffb(java.lang.String sffb) {
		this.sffb = sffb;
	}
	
	@Column(name = "CONTENT", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
	public java.lang.String getContent() {
		return this.content;
	}
	
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	
	@Column(name = "CREATEPERSON", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public java.lang.String getCreateperson() {
		return this.createperson;
	}
	
	public void setCreateperson(java.lang.String value) {
		this.createperson = value;
	}
	
	@Column(name = "CREATEDEPT", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public java.lang.String getCreatedept() {
		return this.createdept;
	}
	
	public void setCreatedept(java.lang.String value) {
		this.createdept = value;
	}
	
	@Column(name = "TOPERSON", unique = false, nullable = true, insertable = true, updatable = true, length = 400)
	public java.lang.String getToperson() {
		return this.toperson;
	}
	
	public void setToperson(java.lang.String value) {
		this.toperson = value;
	}
	
	@Column(name = "DATA_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getDataType() {
		return this.dataType;
	}
	
	public void setDataType(java.lang.String value) {
		this.dataType = value;
	}
	
	@Transient
	public String getCreatetimeByString() {
		//return DateConvertUtils.format(getCreatetime(), FORMAT_CREATETIME);
		return DateUtil.convertDateToString(createtime);
	}
	public void setCreatetimeByString(String createtimeByString) {
		//setCreatetime(DateConvertUtils.parse(value, FORMAT_CREATETIME,java.util.Date.class));
		this.createtimeByString =createtimeByString;
	}
	
	@Column(name = "CREATETIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public java.util.Date getCreatetime() {
		return this.createtime;
	}
	
	public void setCreatetime(java.util.Date value) {
		this.createtime = value;
	}
	
	@Column(name = "COMMENTS", unique = false, nullable = true, insertable = true, updatable = true, length = 400)
	public java.lang.String getComments() {
		return this.comments;
	}
	
	public void setComments(java.lang.String value) {
		this.comments = value;
	}


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Title",getTitle())
			.append("Content",getContent())
			.append("Createperson",getCreateperson())
			.append("Createdept",getCreatedept())
			.append("FormId",getFormId())
			.append("Toperson",getToperson())
			.append("DataType",getDataType())
			.append("Createtime",getCreatetime())
			.append("Comments",getComments())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof NoticePlat == false) return false;
		if(this == obj) return true;
		NoticePlat other = (NoticePlat)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
	
	
	@Column(name = "DATA_YEAR")
	public java.lang.Long getDataYear() {
		return dataYear;
	}

	public void setDataYear(java.lang.Long dataYear) {
		this.dataYear = dataYear;
	}

	@Column(name = "FORMID", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public java.lang.String getFormId() {
		return formId;
	}

	public void setFormId(java.lang.String formId) {
		this.formId = formId;
	}

	@Transient
	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	@Transient
	public  List<MultipartFile> getMyfiles(){
		return myfiles;
	}

	public void setMyfiles(List<MultipartFile> myfiles) {
		this.myfiles = myfiles;
	}

	@Transient
	public List<FileManage> getDocuments() {
		return documents;
	}

	public void setDocuments(List<FileManage> documents) {
		this.documents = documents;
	}

	@Transient
	public java.lang.String getIsRead() {
		return isRead;
	}

	public void setIsRead(java.lang.String isRead) {
		this.isRead = isRead;
	}

}
