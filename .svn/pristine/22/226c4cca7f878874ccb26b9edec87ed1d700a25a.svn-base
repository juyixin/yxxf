package com.eazytec.model;
import javax.persistence.*;

import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import com.eazytec.model.BaseObject;
import com.eazytec.model.DocumentForm;
import com.eazytec.util.DateUtil;

import java.util.*;

import com.eazytec.model.BaseObject;
import com.eazytec.util.DateUtil;


/**
 * @author easybpm
 *
 */


@Entity
@Table(name = "NOTICE_DOCUMENT")
public class NoticeDocument extends BaseObject {
	private static final long serialVersionUID = 5454155825314635342L;
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息11
	//columns START
	
	private java.lang.String id;


	
	private java.lang.String createdBy;


	
	private java.util.Date createdTime;

	private String createdTimeByString;

	
	private java.lang.String mimeType;


	
	private java.lang.String name;


	
	private java.lang.String path;


	
	private java.lang.String parentid;


	//columns END
	


	//123
	public NoticeDocument(){
	}

	public NoticeDocument(
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
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, length = 255)
	public java.lang.String getId() {
		return this.id;
	}
	
	@Column(name = "CREATED_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(java.lang.String value) {
		this.createdBy = value;
	}
	
	@Transient
	public String getCreatedTimeByString() {
		//return DateConvertUtils.format(getCreatedTime(), FORMAT_CREATED_TIME);
		return DateUtil.convertDateToString(createdTime);
	}
	public void setCreatedTimeByString(String createdTimeByString) {
		//setCreatedTime(DateConvertUtils.parse(value, FORMAT_CREATED_TIME,java.util.Date.class));
		this.createdTimeByString =createdTimeByString;
	}
	
	@Column(name = "CREATED_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 11)
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	
	public void setCreatedTime(java.util.Date value) {
		this.createdTime = value;
	}
	
	@Column(name = "MIME_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getMimeType() {
		return this.mimeType;
	}
	
	public void setMimeType(java.lang.String value) {
		this.mimeType = value;
	}
	
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	@Column(name = "PATH", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getPath() {
		return this.path;
	}
	
	public void setPath(java.lang.String value) {
		this.path = value;
	}
	
	@Column(name = "PARENTID", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getParentid() {
		return this.parentid;
	}
	
	public void setParentid(java.lang.String value) {
		this.parentid = value;
	}


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("CreatedBy",getCreatedBy())
			.append("CreatedTime",getCreatedTime())
			.append("MimeType",getMimeType())
			.append("Name",getName())
			.append("Path",getPath())
			.append("Parentid",getParentid())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof NoticeDocument == false) return false;
		if(this == obj) return true;
		NoticeDocument other = (NoticeDocument)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}

}

