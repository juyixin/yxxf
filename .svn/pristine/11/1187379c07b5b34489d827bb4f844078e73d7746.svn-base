package com.eazytec.petitionLetter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "yxxf_xfrxxb")
public class PetitionLetterPerson {
	
	//ID
	private String id;
	//姓名
	private String xm;
	//性别
	private String xb;
	//个人照片
	private String photo;
	//出生日期
	private String csrq;
	//电话
	private String dh;
	//身份证号码
	private String sfzhm;
	//创建时间
	private String createTime;
	//修改时间
	private String lastModifyTime;
	//有效标示
	private String isActive;
	//地址
	private String address;
	//图片地址是否存在
	private String imgExists;
	
	
    private String modifyFileImg;
	
	private String modifyFileUrl;
	
	
	/*
	 * 扩展字段
	 */
	
	//事件ID
	private String eventId;
	//事件名称
	private String eventName;
	//事件内容
	private String eventDetail;
	//备注
	private String bz;
	//信访人ID
	private String personId;
	
	
	@Transient
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	@Transient
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	@Transient
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	@Transient
	public String getEventDetail() {
		return eventDetail;
	}
	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}
	@Transient
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
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
	@Transient
	public String getImgExists() {
		return imgExists;
	}
	public void setImgExists(String imgExists) {
		this.imgExists = imgExists;
	}
	@Column(name = "ADDRESS", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	
	@Column(name = "XM", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	
	@Column(name = "SEX", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	
	@Column(name = "PHOTO", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@Column(name = "CSRQ", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	
	@Column(name = "TEL", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getDh() {
		return dh;
	}
	public void setDh(String dh) {
		this.dh = dh;
	}
	
	@Column(name = "SFZ_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public String getSfzhm() {
		return sfzhm;
	}
	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}
	
	@Column(name = "CREATE_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "LAST_MODIFY_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public String getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	@Column(name = "IS_ACTIVE", unique = false, nullable = true, insertable = true, updatable = true, length = 6)
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
}
