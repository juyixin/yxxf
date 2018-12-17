package com.eazytec.alxxgl.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eazytec.alxxgl.dao.AlxxbDao;
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.alxxgl.service.AlxxbService;
import com.eazytec.bpm.common.util.PathUtils;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.crm.model.Contract;
import com.eazytec.crm.model.Record;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.util.PageBean;

@Service("alxxbService")
public class AlxxbServiceImpl implements AlxxbService{
	@Autowired
	private AlxxbDao alxxbDao;

	@Override
	public Alxxb getAlxxbById(String id) {
		return   alxxbDao.get(id);
	}

	@Override
	public Alxxb saveAlxxb(Alxxb alxxb) {
		return alxxbDao.saveAlxxb(alxxb);
	}

	@Override
	public String removeAlxxb(List<String> alxxbIds) {
	List<Alxxb> alxxbList=alxxbDao.getAlxxbByIds(alxxbIds);
		
		for (Alxxb alxxb : alxxbList) {
			alxxbDao.deleteAlxxb(alxxb);
		}
		return null;
	}

	@Override
	public Alxxb saveOrUpdateAlxxbForm(Alxxb alxxbForm, List<MultipartFile> files, String path) {
		Date date = new Date();
		try {
			if(StringUtil.isEmptyString(alxxbForm.getId())) {
				alxxbForm.setCreatedTime(date);
				alxxbForm.setLastModifyTime(date);
			} else { 
				alxxbForm.setLastModifyTime(date);
			}
			String	fileName = "";
			 String extension = "";
			String parentDir = path;
			//String parentDir = path+"test"+"/";
			if(files != null  && !files.isEmpty()){
				alxxbForm.setAlxxbDocument(new ArrayList<AlxxbDocument>());
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						AlxxbDocument document = new AlxxbDocument();
						 fileName = multipartFile.getOriginalFilename();
						 int pos = fileName.lastIndexOf(".");
						 extension = fileName.substring(pos+1);
						 fileName = fileName.substring(0, pos-1);
						 fileName = fileName.replace(" ", "_");
			   	     byte[] encodedFileName = fileName.getBytes("UTF-8");
			   	     fileName= encodedFileName.toString();
			   	     if(fileName.startsWith("[")){
			   	     fileName = fileName.substring(1);
			   	     }	
						String parentPath = parentDir+fileName+"."+extension;
						document.setPath(parentPath);
						document.setName(multipartFile.getOriginalFilename().replace(' ','_'));
						document.setCreatedTime(date);
						document.setModifiedTime(date);
						document.setMimeType(multipartFile.getContentType());
						alxxbForm.getAlxxbDocument().add(document);
						if(!StringUtil.isEmptyString(alxxbForm.getId())){
							Alxxb oldAlxxbForm = alxxbDao.get(alxxbForm.getId());   
							if(null != oldAlxxbForm.getAlxxbDocument() && oldAlxxbForm.getAlxxbDocument().size() >0){
								alxxbForm.getAlxxbDocument().addAll(oldAlxxbForm.getAlxxbDocument());
							}
						}
					}
				} 
			} else if(!StringUtil.isEmptyString(alxxbForm.getId())){
				Alxxb oldAlxxbForm = alxxbDao.get(alxxbForm.getId());   
				if(null != oldAlxxbForm.getAlxxbDocument() && oldAlxxbForm.getAlxxbDocument().size() >0){
					alxxbForm.getAlxxbDocument().addAll(oldAlxxbForm.getAlxxbDocument());
				}
			}
			
			alxxbForm =  alxxbDao.save(alxxbForm);
			
			String uploadDir = path;
	         // Create the directory if it doesn't exist
	         File dirPath = new File(uploadDir);
	         if (!dirPath.exists()) {
	             dirPath.mkdirs();
	         }
	         
			if(null != files && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
					      // retrieve the file data
		                InputStream stream = multipartFile.getInputStream();
		                // write the file to the file specified
		                OutputStream bos = new FileOutputStream(uploadDir + fileName+"."+extension);
		                int bytesRead;
		                byte[] buffer = new byte[8192];
		                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		                    bos.write(buffer, 0, bytesRead);
		                }
		                bos.close();
		                // close the stream
		                stream.close();
					}
				}
			}
		} catch (Exception e) {
			throw new BpmException("problem in saving document"+e);
		}
		return alxxbForm;
	}

	@Override
	public List<Alxxb> getAllxByAllx(String allx) {
		return alxxbDao.getAllxByAllx(allx);
	}
	
	@Override
	public List<Alxxb> getAllxByAllx1(String allx) {
		return alxxbDao.getAllxByAllx1(allx);
	}
	
	@Override
	public List<Alxxb> getAllxByAllx2(String id,String name) {
		return alxxbDao.getAllxByAllx2(id, name);
	}

	@Override
	public Alxxb getAllxByIds(String id) {
		return alxxbDao.getAllxByIds(id);
	}

	@Override
	public PageBean<Alxxb> getRecords(PageBean<Alxxb> pageBean, String name, String allx, String dsr) {
			int totalrecords =  alxxbDao.getAllRecordCount(pageBean, name,allx,dsr);
			pageBean.setTotalrecords(totalrecords);
			List<Alxxb> result =  alxxbDao.getAllRecord(pageBean, name, allx, dsr);
			pageBean.setResult(result);
			return pageBean;
		}

	@Override
	public List<Alxxb> getlistsize(String name, String allx, String dsr) {
		return alxxbDao.getlistsize(name,allx,dsr);
	}

	
	
	 
}
