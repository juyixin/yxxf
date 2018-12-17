package com.eazytec.link.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.activiti.engine.identity.Picture;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "link")
public class Link {
	//ID
	private String id;
	//标题
	private String title;
	//地址
	private String url;
	//预留字段1
	private String extStr1;
	//预留字段2
	private String extStr2;
	//预留字段3
	private String extStr3;
	//创建时间
	private String createDate;
	//修改时间
	private String modifyDate;
	//是否启用停用
	private String isActive;
	//图片地址
	protected String pictureUrl;
	//文件名字
	private String fileName;
	
	protected ByteArrayEntity pictureByteArray;
	
	private String modifyFileImg;
	
	private String modifyFileUrl;
	
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
	
	@Column(name = "TITLE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "URL", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "EXTSTR1", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getExtStr1() {
		return extStr1;
	}
	public void setExtStr1(String extStr1) {
		this.extStr1 = extStr1;
	}
	
	@Column(name = "EXTSTR2", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getExtStr2() {
		return extStr2;
	}
	public void setExtStr2(String extStr2) {
		this.extStr2 = extStr2;
	}
	
	@Column(name = "EXTSTR3", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getExtStr3() {
		return extStr3;
	}
	public void setExtStr3(String extStr3) {
		this.extStr3 = extStr3;
	}
	
	@Column(name = "CREATE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "MODIFY_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@Column(name = "IS_ACTIVE", unique = false, nullable = true, insertable = true, updatable = true, length = 6)
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@Column(name = "PICTURE_URL", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	@Column(name = "FILE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	@Transient
	@JsonIgnore
	public Picture getPicture() {
		if (pictureUrl == null) {
			return null;
		}
		ByteArrayEntity pictureByteArray = getPictureByteArray();
		Picture picture = null;
		if (pictureByteArray != null) {
			picture = new Picture(pictureByteArray.getBytes(), pictureByteArray.getName());
		}
		return picture;
	}

	public void setPicture(Picture picture) {
		if (pictureUrl != null) {
			Context.getCommandContext().getByteArrayEntityManager().deleteByteArrayById(pictureUrl);
		}
		if (picture != null) {
			pictureByteArray = new ByteArrayEntity(picture.getMimeType(), picture.getBytes());
			Context.getCommandContext().getDbSqlSession().insert(pictureByteArray);
			pictureUrl = pictureByteArray.getId();
		} else {
			pictureUrl = null;
			pictureByteArray = null;
		}
	}
	
	@Transient
	private ByteArrayEntity getPictureByteArray() {
		/*
		 * if (pictureByteArrayId!=null && pictureByteArray==null) {
		 * pictureByteArray = Context .getCommandContext() .getDbSqlSession()
		 * .selectById(ByteArrayEntity.class, pictureByteArrayId); }
		 */
		return pictureByteArray;
	}
	
	@Transient
	public String getModifyFileImg() {
		return modifyFileImg;
	}
	public void setModifyFileImg(String modifyFileImg) {
		this.modifyFileImg = modifyFileImg;
	}
	
	
	@Transient
	public String getModifyFileUrl() {
		return modifyFileUrl;
	}
	public void setModifyFileUrl(String modifyFileUrl) {
		this.modifyFileUrl = modifyFileUrl;
	}

}
