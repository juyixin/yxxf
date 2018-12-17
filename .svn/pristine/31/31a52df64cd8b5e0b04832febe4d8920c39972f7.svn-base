package com.eazytec.sendDocuments.model;

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
@Table(name = "SENT_DOCUMENTS")
public class SendDocuments {
	//ID
	private String id;
	//主题
	private String theme;
	//内容
	private String content;
	//发送时间
	private String sendTime;
	//收件人
	private String recipient;
	//抄送人
	private String copyPeople;
	//发送人
	private User sender;
	//文件信息，多个以;分割
	private String document;
	
	private String senderName;
	
	private String recipientName;
	
	private String copyPeopleName;
	
	

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
	
	@Column(name = "THEME")
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "SEND_TIME")
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	@Column(name = "RECIPIENT")
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	@Column(name = "COPY_PEOPLE")
	public String getCopyPeople() {
		return copyPeople;
	}
	public void setCopyPeople(String copyPeople) {
		this.copyPeople = copyPeople;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SENDER")
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}

}
