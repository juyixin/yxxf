package com.eazytec.evidence.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "evidence")
public class Evidence {
	//ID
	private String id;
	//事件名称
	private String eventName;
	//当事人身份证号码
	private String sfzCode;
	//当事人身份证姓名
	private String concernName;
	//当事人性别
	private String concernSex;
	//处理人姓名
	private String handlerName;
	//处理人ID
	private String handlerId;
	//处理人联系方式
	private String handlerPhone;
	//创建时间
	private String createDate;
	//修改时间
	private String modifyDate;
	//文字内容
	private String content;
	
	
	//文件路径
	private String filePath;
	//文件ID
	private String fileId;
	//文件名
	private String fileName;
	//文件类型
	private String fileType;
	//创建人
	private String createName;
	//描述
	private String description;
	
	private String imgUrl;
	
	@Transient
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	@Transient
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Transient
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@Transient
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Transient
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Transient
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	
	@Transient
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	@Column(name = "CONTENT", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "CONCERN_SEX", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	public String getConcernSex() {
		return concernSex;
	}
	public void setConcernSex(String concernSex) {
		this.concernSex = concernSex;
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
	@Column(name = "EVENT_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	@Column(name = "SFZ_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getSfzCode() {
		return sfzCode;
	}
	public void setSfzCode(String sfzCode) {
		this.sfzCode = sfzCode;
	}
	@Column(name = "CONCERN_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getConcernName() {
		return concernName;
	}
	public void setConcernName(String concernName) {
		this.concernName = concernName;
	}
	@Column(name = "HANDLER_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	@Column(name = "HANDLER_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}
	@Column(name = "HANDLER_PHONE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getHandlerPhone() {
		return handlerPhone;
	}
	public void setHandlerPhone(String handlerPhone) {
		this.handlerPhone = handlerPhone;
	}
	@Column(name = "CREATE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Column(name = "MODIFY_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
    
}
