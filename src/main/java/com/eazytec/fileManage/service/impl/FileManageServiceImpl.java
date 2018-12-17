package com.eazytec.fileManage.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.fileManage.dao.FileManageDao;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.NoticeDocument;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("fileManageService")
@WebService(serviceName="FileManageService",endpointInterface = "com.eazytec.fileManage.FileManageService")
public class FileManageServiceImpl extends GenericManagerImpl<FileManage, String> implements FileManageService{
	
	@Autowired
	private FileManageDao fileManageDao;
    
	
	/**
	  * Description:保存数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-14  上午9:45:23
	  */
	@Override
	public FileManage saveFileManage(FileManage fileManage) {
		return fileManageDao.saveFileManage(fileManage);
	}

    
	 /**
	  * Description:根据外键fileId查找数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-15  下午13:53:13
	  */
	@Override
	public List<FileManage> getEvidence(String fileId) {
		return fileManageDao.getEvidence(fileId);
	}
	
	@Override
	public List<FileManage> getEvidenceids(String fileId) {
		return fileManageDao.getEvidenceids(fileId);
	}


	@Override
	public FileManage getEvidence1(String id) {
		return fileManageDao.getDetail(id);
	}
	
	/**
	  * Description:保存数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-14  上午9:45:23
	  */
	@Override
	public String saveFileManage1(List<FileManage> fileManage) {
		String message = "";
		try{
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			for(int i=0;i<fileManage.size();i++){
				fileManageDao.saveFileManage1(fileManage.get(i));
				System.out.println("**********保存证据采集记录："+i+"**********" + currentTime.toGMTString());
			}
			message="true";
		}catch (Exception e){
			e.printStackTrace();
			message="false";
		}
		return message;
	}


	@Override
	public FileManage getFileManageById(String id) {

	        return fileManageDao.getFileManageById(id);
	    }




}
