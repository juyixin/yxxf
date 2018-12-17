package com.eazytec.fileManage.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.fileManage.model.FileManage;


public interface FileManageDao extends GenericDao<FileManage,String>{
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	public FileManage saveFileManage(FileManage fileManage);
	
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	public void saveFileManage1(FileManage fileManage);
	
	
	/**
	  * Description:根据外键fileId查找数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-15  下午13:53:13
	  */
	public List<FileManage> getEvidence(String  fileId);
	
	/**
	  * Description:根据主键Id查找数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-15  下午13:53:13
	  */
	public FileManage getDetail(String  id);
	
	FileManage getFileManageById(String id);

	void deleteFileManageById(String id);
	
	
	public List<FileManage> getEvidenceids(String  fileId);

}
