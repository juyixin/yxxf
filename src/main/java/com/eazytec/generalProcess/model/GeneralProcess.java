package com.eazytec.generalProcess.model;

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

import com.eazytec.model.User;

@Entity
@Table(name = "GENERAL_PROCESS")
public class GeneralProcess {
	
	    //ID
		private String id;
		//事项名称
		private String itemName;
		//事项描述
		private String itemDescription;
		//备注
		private String remark;
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
		
		private String document;
		
		
		private String senderName;
			
	    private String recipientName;
			
		private String copyPeopleName;
			
		private String stateName;
			
			
			
		@Transient
		public String getStateName() {
			return stateName;
		}
		
		public void setStateName(String stateName) {
		    this.stateName = stateName;
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
		
		@Column(name = "ITEM_NAME")
		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		@Column(name = "ITEM_DESCRIPTION")
		public String getItemDescription() {
			return itemDescription;
		}

		public void setItemDescription(String itemDescription) {
			this.itemDescription = itemDescription;
		}

		@Column(name = "REMARK")
		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
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

		
		@Transient
		public String getDocument() {
			return document;
		}

		public void setDocument(String document) {
			this.document = document;
		}
		

}
