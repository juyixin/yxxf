package com.eazytec.vacate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;

@Entity
@Table(name = "VACATE")
public class Vacate {
	
	//ID
	private String id;
	//请假类型
	private String vacateType;
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//请假天数
	private String vacateDay;
	//请假事由
	private String vacateCause;
	//审批人
	private String approver;
	//审批意见
	private String opinion;
	//抄送人
	private String copyPeople;
	//状态
	private String state;
	//发起人
	private User originator;
	//创建时间
	private String createTime;
	
	//文件信息，多个以;分割
	private String document;
    
	private String vacateTypeName;
	
    private String senderName;
	
	private String recipientName;
	
	private String copyPeopleName;
	
	private String stateName;
	
	private String senderCode;
	
	private String originatorName;
	
	private int count;
	
    private String searchStartTime;
	
	private String searchEndTime;
	
	private String name;
	
	private String total;
	
	
	
	
	@Transient
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(String searchStartTime) {
		this.searchStartTime = searchStartTime;
	}
	
	@Transient
	public String getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	@Transient
	public String getOriginatorName() {
		return originator.getFirstName();
	}

	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}
	
	
	@Transient
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	@Transient
	public String getSenderCode() {
		return originator.getId();
	}
	
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	
	@Transient
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@Transient
	public String getVacateTypeName() {
		return vacateTypeName;
	}
	public void setVacateTypeName(String vacateTypeName) {
		this.vacateTypeName = vacateTypeName;
	}
	
	@Transient
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	@Transient
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	
	@Transient
	public String getCopyPeopleName() {
		return copyPeopleName;
	}
	public void setCopyPeopleName(String copyPeopleName) {
		this.copyPeopleName = copyPeopleName;
	}
		
	@Transient
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
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
	
	@Column(name = "VACATE_TYPE")
	public String getVacateType() {
		return vacateType;
	}
	public void setVacateType(String vacateType) {
		this.vacateType = vacateType;
	}
	
	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "VACATE_DAY")
	public String getVacateDay() {
		return vacateDay;
	}
	public void setVacateDay(String vacateDay) {
		this.vacateDay = vacateDay;
	}
	
	@Column(name = "VACATE_CAUSE")
	public String getVacateCause() {
		return vacateCause;
	}
	public void setVacateCause(String vacateCause) {
		this.vacateCause = vacateCause;
	}
	
	@Column(name = "APPROVER")
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	
	@Column(name = "OPINION")
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	@Column(name = "COPY_PEOPLE")
	public String getCopyPeople() {
		return copyPeople;
	}
	public void setCopyPeople(String copyPeople) {
		this.copyPeople = copyPeople;
	}
	
	@Column(name = "STATE")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORIGINATOR")
	public User getOriginator() {
		return originator;
	}
	public void setOriginator(User originator) {
		this.originator = originator;
	}
	
	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
