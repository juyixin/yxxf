package com.eazytec.crm.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eazytec.model.BaseObject;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;

@Entity
@Table(name = "crm_record")
public class Record extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String id;
	private String num;
	private String type;
	private Date startTime;
	private Date endTime;
	private String note;
	private User createdBy;
	private Date createdTime;
	
	public Record() {

	}

	@Id
	@GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "created_by")
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	@Transient
	public String getStartTimeByString() {
		return DateUtil.convertDateToString(startTime);
	}
	
	@Transient
	public String getEndTimeByString() {
		return DateUtil.convertDateToString(endTime);
	}
	
	@Transient
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public void init() {
		if(getCreatedTime() == null){
			setCreatedTime(new Date());
		}
		
	}

}
