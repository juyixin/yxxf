package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.eazytec.util.DateUtil;

/*
 * @author yanghaiyun
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "MESSAGE")
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8588131355861674851L;
	
	public static final int VALID = 1;//未删除，有效的
	public static final int INVALID = 0;//已删除，无效的

	private String id;
	private String fromUser; // 发送人
	private String toUser;// 接收人
	private String messageBody; // 消息内容
	private String messageName; // 消息主题
	private Integer fromStatus; // 发送人消息状态
	private Integer toStatus; // 接收人消息状态
	private Boolean isDraft; // 是否草稿（1是0否）
	private Date messageDate; // 消息发送日期
	private Boolean isRead;// 是否已读（1已读0未读）
	//private List<MessageFile> messageFiles = new ArrayList<MessageFile>();
	private Set<MessageFile> messageFiles;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "FROM_USER")
	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	@Column(name = "TO_USER")
	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	@Column(name = "MESSAGE_BODY")
	@Lob
	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	@Column(name = "MESSAGE_NAME")
	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	@Column(name = "FROM_STATUS")
	public Integer getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(Integer fromStatus) {
		this.fromStatus = fromStatus;
	}

	@Column(name = "TO_STATUS")
	public Integer getToStatus() {
		return toStatus;
	}

	public void setToStatus(Integer toStatus) {
		this.toStatus = toStatus;
	}

	@Column(name = "IS_DRAFT")
	public Boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	@Column(name = "MESSAGE_DATE")
	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	
	@Transient
	public String getMessageDateByString() {
		return DateUtil.convertDateToString(messageDate);
	}

	@Column(name = "IS_READ")
	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	/**
	@ElementCollection(fetch=FetchType.EAGER, targetClass=MessageFile.class) //指定元素中集合的类型  
    @CollectionTable(name="MESSAGE_FILE", joinColumns=@JoinColumn(name="MESSAGE_ID")) //指定集合生成的表  
    @OrderColumn(name="PRIORITY") //指定排序列的名称  
	public List<MessageFile> getMessageFiles() {
		return messageFiles;
	}

	public void setMessageFiles(List<MessageFile> messageFiles) {
		this.messageFiles = messageFiles;
	}
	**/
	
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "message")
	public Set<MessageFile> getMessageFiles() {
		return messageFiles;
	}

	public void setMessageFiles(Set<MessageFile> messageFiles) {
		this.messageFiles = messageFiles;
	}

	public void addToFiles(MessageFile file) {
		Set<MessageFile> set = getMessageFiles();
		if (set == null) {
			set = new HashSet<MessageFile>();
			setMessageFiles(set);
		}
		set.add(file);
	}

	public void init() {
		if(getIsRead() == null){
			setIsRead(false);
		}
		if(getToStatus() == null){
			setToStatus(VALID);
		}
		if(getFromStatus() == null){
			setFromStatus(VALID);
		}
		if(getMessageDate() == null){
			setMessageDate(new Date());
		}
	}
}
