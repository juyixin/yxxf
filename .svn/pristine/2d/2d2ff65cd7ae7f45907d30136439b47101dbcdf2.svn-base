package com.eazytec.bpm.admin.Zcfg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eazytec.bpm.admin.NoticeDocument.NoticeDocumentDao;
import com.eazytec.bpm.admin.NoticePlat.NoticePlatDao;
import com.eazytec.bpm.admin.NoticeUserPlat.NoticeUserPlatDao;
import com.eazytec.bpm.common.util.PathUtils;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.crm.model.Record;
import com.eazytec.exceptions.BpmException;
import com.eazytec.fileManage.dao.FileManageDao;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.User;
import com.eazytec.model.Zcfg;
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.PageBean;
import com.eazytec.webapp.socket.ClientSend;

@Service("zcfgService")
public class ZcfgServiceImpl extends GenericManagerImpl<Zcfg,java.lang.String> implements ZcfgService{
	
	private ZcfgDao zcfgDao;
	
	private FileManageDao fileManageDao;
    
	private NoticeUserPlatDao  noticeuserplatDao;

	private ClientSend clientSend;

	@Autowired
	public void setZcfgDao(ZcfgDao zcfgDao) {
		this.zcfgDao = zcfgDao;
	}

	@Autowired
	public void setFileManageDao(FileManageDao fileManageDao) {
		this.fileManageDao = fileManageDao;
	}

	@Autowired
	public void setNoticeuserplatDao(NoticeUserPlatDao noticeuserplatDao) {
		this.noticeuserplatDao = noticeuserplatDao;
	}

	@Autowired
	public void setClientSend(ClientSend clientSend) {
		this.clientSend = clientSend;
	}

	@Autowired
	public ZcfgServiceImpl(ZcfgDao zcfgDao) {
		super(zcfgDao);
		this.zcfgDao = zcfgDao;
	}

	@Override
	public List<Zcfg> getZcfgListByUserid() {
		// TODO Auto-generated method stub
		return zcfgDao.getZcfgListByUserid();
	}

	@Override
	public Zcfg mysaveOrUpdateZcfgForm(Zcfg zcfgForm, List<MultipartFile> files, List<MultipartFile> myfiles,
			String userName, String path, List<String[]> deleteArray) {
		Date date = new Date();
		Calendar calendar=Calendar.getInstance();
		List<NoticeDocument> noticeDocuments=new ArrayList<NoticeDocument>();
		String fileName = "";
		try {
			if(StringUtil.isEmptyString(zcfgForm.getId())) {
				zcfgForm.setId(null);
			}
			zcfgForm.setCreateperson(userName);
			zcfgForm.setCreatetime(date);
			zcfgForm.setDataYear((long) calendar.get(Calendar.YEAR));
			if(!deleteArray.isEmpty()){
				for(int i=0;i<deleteArray.size();i++){
					fileManageDao.deleteFileManageById(deleteArray.get(i)[0]);
				}

			}
			zcfgForm =  zcfgDao.saveZcfg(zcfgForm);

			String parentDir = path;

			//保存上传文件进入数据库
			if(files != null  && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						FileManage document = new FileManage();
						 fileName = multipartFile.getOriginalFilename();
						 int pos = fileName.lastIndexOf(".");
						 String extension = fileName.substring(pos+1);
						 fileName = fileName.substring(0, pos-1);
						 fileName = fileName.replace(" ", "_");
			   	     byte[] encodedFileName = fileName.getBytes("UTF-8");
			   	     fileName= encodedFileName.toString();
			   	     if(fileName.startsWith("[")){
			   	     fileName = fileName.substring(1);
			   	     }
						 fileName = fileName+"."+extension;
						String parentPath = parentDir+fileName;
						//String name = PathUtils.escape(fileName);
						document.setId(null);
						document.setCreateName(zcfgForm.getCreateperson());
						document.setFilePath(parentPath);
						document.setFileName(multipartFile.getOriginalFilename().replace(" ", "_"));
						if(zcfgForm.getId()!=null){
							document.setFileId(zcfgForm.getId());
						}
						fileManageDao.saveFileManage(document);
					}
				}
			}
			List<User> userList=new ArrayList<User>();
			String uploadDir = path;
			// Create the directory if it doesn't exist
			File dirPath = new File(uploadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			//上传文件到服务器
			if(null != files && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						// retrieve the file data
						InputStream stream = multipartFile.getInputStream();
						// write the file to the file specified
						OutputStream bos = new FileOutputStream(uploadDir + fileName);
						int bytesRead;
						byte[] buffer = new byte[8192];
						while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
							bos.write(buffer, 0, bytesRead);
						}
						bos.close();
						// close the stream
						stream.close();
						clientSend.sendFile(new File(uploadDir + fileName));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new BpmException("problem in saving document"+e);
		}
		return zcfgForm;
	}


		@Override
		public void removeZcfg(List<String> jyxIds) {
			// TODO Auto-generated method stub
			for(int i=1;i<jyxIds.size();i++){
					zcfgDao.removeZcfg(jyxIds.get(i));
		}
	}

		@Override
		public Zcfg saveZcfg(Zcfg zcfg) throws Exception {
				return zcfgDao.saveZcfg(zcfg);
				
			}

		@Override
		
		public Zcfg getZcfgById(String id) {
		        return zcfgDao.getZcfgById(id);
		    }

		@Override
		public PageBean<Zcfg> getRecords(PageBean<Zcfg> pageBean, String searchtext) {
				int totalrecords =  zcfgDao.getAllZcfgCount(pageBean, searchtext);
				pageBean.setTotalrecords(totalrecords);
				List<Zcfg> result =  zcfgDao.getAllZcfg(pageBean, searchtext);
				pageBean.setResult(result);
				return pageBean;
			}

		@Override
		public List<Zcfg> getZcfgListByserach(String name) {
			// TODO Auto-generated method stub
			return zcfgDao.getZcfgListByserach(name);
		}

}
