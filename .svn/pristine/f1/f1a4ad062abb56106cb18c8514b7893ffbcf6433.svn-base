package com.eazytec.petitionLetter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "yxxf_xfsjxxb")
public class PetitionLetterEvent {
	
	//ID
	private String id;
	//事件名称
	private String eventName;
	//事件内容
	private String eventDetail;
	//备注
	private String bz;
	//信访人ID
	private String personId;
	//创建时间
	private String createTime;
	//最后修改时间
	private String lastModifyTime;
	//有效标示
	private String isActive;
	
	
	@Column(name = "IS_ACTIVE", unique = false, nullable = true, insertable = true, updatable = true, length = 6)
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
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
	
	@Column(name = "SJMC", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	@Column(name = "SJNR", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getEventDetail() {
		return eventDetail;
	}
	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}
	
	@Column(name = "BZ", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	@Column(name = "XFR_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
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

}
