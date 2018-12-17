package com.eazytec.fileManage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Description:文件管理公共类
 * 作者 : 蒋晨 
 * 时间 : 2017-2-8 下午1:38:35
 */
@Entity
@Table(name = "public_file_manage")
public class FileManage {
	//ID
	private String id;
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
	//创建时间
	private String createDate;
	//修改时间
	private String modifyDate;
	
	
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
	@Column(name = "FILE_PATH", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Column(name = "FILE_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	@Column(name = "FILE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Column(name = "FILE_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	@Column(name = "CREATE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
