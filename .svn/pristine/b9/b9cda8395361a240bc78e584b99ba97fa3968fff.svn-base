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
import com.eazytec.util.DateUtil;

import java.util.*;

import com.eazytec.model.BaseObject;
import com.eazytec.util.DateUtil;


/**
 * @author easybpm
 *
 */


@Entity
@Table(name = "NOTICE_USER_PLAT")
public class NoticeUserPlat extends BaseObject{
	private static final long serialVersionUID = 5454155825314635342L;
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息11
	//columns START
	
	private java.lang.String id;


	
	private java.lang.String parentid;


	
	private java.lang.String userid;


	
	private java.lang.String isRead;


	
	private java.lang.String title;
	
	private java.util.Date createtime;

	private String createtimeByString;


	//columns END
	


	//123
	public NoticeUserPlat(){
	}

	public NoticeUserPlat(
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
	
	@Column(name = "PARENTID", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public java.lang.String getParentid() {
		return this.parentid;
	}
	
	public void setParentid(java.lang.String value) {
		this.parentid = value;
	}
	
	@Column(name = "USERID", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public java.lang.String getUserid() {
		return this.userid;
	}
	
	public void setUserid(java.lang.String value) {
		this.userid = value;
	}
	
	@Column(name = "IS_READ", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public java.lang.String getIsRead() {
		return this.isRead;
	}
	
	public void setIsRead(java.lang.String value) {
		this.isRead = value;
	}
	
	@Column(name = "TITLE", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public java.lang.String getTitle() {
		return this.title;
	}
	
	public void setTitle(java.lang.String value) {
		this.title = value;
	}


	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Parentid",getParentid())
			.append("Userid",getUserid())
			.append("IsRead",getIsRead())
			.append("Title",getTitle())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof NoticeUserPlat == false) return false;
		if(this == obj) return true;
		NoticeUserPlat other = (NoticeUserPlat)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}

	@Column(name = "CREATETIME")
	public java.util.Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
}

