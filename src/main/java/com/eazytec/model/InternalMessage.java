package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * class that send internal messages on specific time
 * 
 * @author mathi
 *
 */

@Entity
@Table(name = "INTERNAL_MESSAGE")
public class InternalMessage extends BaseObject implements Serializable {

	private static final long serialVersionUID = 4453338766237619444L;
	private String id;
	private String fromAddress;
	private String fromUserId;
	private String toAddress;
	private String ccAddress;
	private String bccAddress;
	private String subject;
	private String body;
	private int messageNumber;
	private Date messageSendOn;
	private String status;
	private String statusMessage;
	
	public InternalMessage() {
		
	}

	public InternalMessage(String fromAddress, String fromUserId,
			String toAddress, String ccAddress, String bccAddress,
			String subject, String body, int messageNumber,
			Date messageSendOn, String status) {
		this.fromAddress = fromAddress;
		this.fromUserId = fromUserId;
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		this.bccAddress = bccAddress;
		this.subject = subject;
		this.body = body;
		this.messageNumber = messageNumber;
		this.messageSendOn = messageSendOn;
		this.status = status;
	}
	
	/**
	 * @return the id
	 */
	@Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the from
	 */
	@Column(name = "FROM_ADDRESS" , length=8000)  @Lob
	public String getFrom() {
		return fromAddress;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	/**
	 * @return the fromUserId
	 */
	@Column(name = "FROM_USER_ID" , length=8000)  @Lob
	public String getFromUserId() {
		return fromUserId;
	}
	/**
	 * @param fromUserId the fromUserId to set
	 */
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	/**
	 * @return the to
	 */
	@Column(name = "TO_ADDRESS" , length=8000)  @Lob
	public String getTo() {
		return toAddress;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String toAddress) {
		this.toAddress = toAddress;
	}
	/**
	 * @return the cc
	 */
	@Column(name = "CC_ADDRESS" , length=8000)  @Lob
	public String getCc() {
		return ccAddress;
	}
	/**
	 * @param cc the cc to set
	 */
	public void setCc(String ccAddress) {
		this.ccAddress = ccAddress;
	}
	/**
	 * @return the bcc
	 */
	@Column(name = "BCC_ADDRESS" , length=8000)  @Lob
	public String getBcc() {
		return bccAddress;
	}
	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(String bccAddress) {
		this.bccAddress = bccAddress;
	}
	/**
	 * @return the subject
	 */
	@Column(name = "SUBJECT" , length=8000)  @Lob
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the body
	 */
	@Column(name = "BODY" , length=8000)  @Lob
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the messageNumber
	 */
	@Column(name = "MESSAGE_NUMBER")
	public int getMessageNumber() {
		return messageNumber;
	}
	/**
	 * @param message number the message number to set
	 */
	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}
	/**
	 * @return the messageSendOn
	 */
	@Column(name = "MESSAGE_SEND_ON" )
	public Date getMessageSendOn() {
		return messageSendOn;
	}
	/**
	 * @param messageSendOn the messageSendOn to set
	 */
	public void setMessageSendOn(Date messageSendOn) {
		this.messageSendOn = messageSendOn;
	}
	/**
	 * @return the status
	 */
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the statusMessage
	 */
	@Column(name = "STATUS_MESSAGE" , length=8000)  @Lob
	public String getStatusMessage() {
		return statusMessage;
	}
	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .append(this.id)
        .toString();
	}
	@Override
	public boolean equals(Object o) {
		 if (this == o) {
	            return true;
	        }
	        if (!(o instanceof InternalMessage)) {
	            return false;
	        }

	        final InternalMessage internalMessage = (InternalMessage) o;

	        return !(id != null ? !id.equals(internalMessage.id) : internalMessage.id != null);
	}
	@Override
	public int hashCode() {
		 return (id != null ? id.hashCode() : 0);
	}
}
