package com.eazytec.external.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.evidence.model.Evidence;
import com.eazytec.evidence.model.EvidenceApp;
import com.eazytec.evidence.service.EvidenceService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.util.DateUtil;

@Controller("extEvidenceDetailController")
public class EvidenceDetailController {
	
	private static final Logger log = LoggerFactory.getLogger(EvidenceDetailController.class);
	

	@Autowired
	private EvidenceService evidenceService;
	@Autowired
	private FileManageService fileManageService;
	
	   /**
		* Description:具体证据详情上传
		* 作者 : 蒋晨 
		* 时间 : 2017-2-14 下午3:51:35
		*/
		@RequestMapping(value = "evidence", method = RequestMethod.POST)
		public @ResponseBody Map<String, Object> save(EvidenceApp evidenceApp, HttpServletRequest request, HttpServletResponse response) {
			Map<String, Object> map = new HashMap<String, Object>();
			Evidence newEvidence = new Evidence();
			//EvidenceApp evidence = new EvidenceApp();
			FileManage fileManage = new FileManage();
			String evidenceId = "";
			List<FileManage> fileList = new ArrayList<FileManage>();
			//照片路劲
			String filePathPhotos = "";
			//视频路劲
			String filePathVideos = "";
			//音频路劲
			String filePathVoices = "";
			String[] fileVideos = null;
			String j = "";
			try {
	            if(evidenceApp.getName()!=null){
	            	newEvidence.setEventName(evidenceApp.getName());
	            	newEvidence.setSfzCode(evidenceApp.getPartycert());
	            	newEvidence.setConcernName(evidenceApp.getPartyname());
	            	newEvidence.setHandlerName(evidenceApp.getHandlerName());
	            	newEvidence.setHandlerId(evidenceApp.getHandlerId());
	            	newEvidence.setHandlerPhone(evidenceApp.getHandlerPhone());
	            	newEvidence.setCreateDate(evidenceApp.getTime());
	            	newEvidence.setConcernSex(evidenceApp.getPartySex());
	    			evidenceId = evidenceService.saveEvidence(newEvidence);
	    			if(evidenceId!=null){
	    				/*String reg = ".*\\\\(.*)";
	    				String c ="";*/
	    				if(evidenceApp.getVideos()!=null&&evidenceApp.getVideos()!=""){
	    					filePathVideos = evidenceApp.getVideos();		
	    					fileVideos = filePathVideos.split(";");
	    					for(String s : fileVideos){
	    						fileManage = new FileManage();
	    						/*c = s.replaceAll(reg,"$1");
	    						int dot = c.lastIndexOf('.');   
	    				        if ((dot >-1) && (dot < (c.length() - 1))) {   
	    				            j = c.substring(dot + 1);   
	    				        } */ 
	    						j=s.substring(s.lastIndexOf(".")+1);
	    						fileManage.setFileType("video"+"\\"+j);
	    						String fileN = evidenceApp.getName() + RandomStringUtils.randomNumeric(6);
	    				    	String fileName =  (fileN+"."+j).trim(); 
	    						fileManage.setFileName(fileName);
	    						fileManage.setFileId(evidenceId);
	    						fileManage.setFilePath(s);
	    						fileManage.setCreateName(evidenceApp.getHandlerName());
	    						Date date = new Date();
	    						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    						fileManage.setCreateDate(sdf.format(date));
	    						fileList.add(fileManage);
	    					}
	    				}
	    				if(evidenceApp.getVoices()!=null&&evidenceApp.getVoices()!=""){
	    					filePathVoices = evidenceApp.getVoices();		
	    					fileVideos = filePathVoices.split(";");
	    					for(String s : fileVideos){
	    						fileManage = new FileManage();
	    						/*c = s.replaceAll(reg,"$1");
	    						int dot = c.lastIndexOf('.');   
	    				        if ((dot >-1) && (dot < (c.length() - 1))) {   
	    				            j = c.substring(dot + 1);   
	    				        }*/ 
	    						j=s.substring(s.lastIndexOf(".")+1);
	    						fileManage.setFileType("voices"+"\\"+j);
	    				        String fileN = evidenceApp.getName() + RandomStringUtils.randomNumeric(6);
	    				    	String fileName =  (fileN+"."+j).trim(); ;  
	    						fileManage.setFileName(fileName);
	    						fileManage.setFileId(evidenceId);
	    						fileManage.setFilePath(s);
	    						fileManage.setCreateName(evidenceApp.getHandlerName());
	    						Date date = new Date();
	    						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    						fileManage.setCreateDate(sdf.format(date));
	    						fileList.add(fileManage);
	    					}
	    				}
	    				if(evidenceApp.getPhotos()!=null&&evidenceApp.getPhotos()!=""){
	    					filePathPhotos = evidenceApp.getPhotos();		
	    					fileVideos = filePathPhotos.split(";");
	    					for(String s : fileVideos){
	    						fileManage = new FileManage();
	    						/*c = s.replaceAll(reg,"$1");
	    				        int dot = c.lastIndexOf('.');   
	    				        if ((dot >-1) && (dot < (c.length() - 1))) {   
	    				            j = c.substring(dot + 1);   
	    				        }   */  
	    						j=s.substring(s.lastIndexOf(".")+1);
	    						fileManage.setFileType("image"+"\\"+j);
	    						/*String fName = s.trim();  
		    				    String fileName = fName.substring(fName.lastIndexOf("/")+1); */
	    						String fileN = evidenceApp.getName() + RandomStringUtils.randomNumeric(6);
	    				    	String fileName =  (fileN+"."+j).trim(); 
	    						fileManage.setFileName(fileName);
	    						fileManage.setFileId(evidenceId);
	    						fileManage.setFilePath(s);
	    						fileManage.setCreateName(evidenceApp.getHandlerName());
	    						Date date = new Date();
	    						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    						fileManage.setCreateDate(sdf.format(date));
	    						fileList.add(fileManage);
	    					}
	    				}
	    				String message = fileManageService.saveFileManage1(fileList);
	    				if(message.equals("true")){
	    					map.put("success", "保存成功");
	    				}else{
	    					return ExternalUtils.error(map, "保存失败");
	    				}
	    			}
	            }else{
	            	return ExternalUtils.error(map, "具体请求参数为空");
	            }	
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return ExternalUtils.error(map, "获取Record出错");
			}
			return ExternalUtils.success(map);
		}
		

}
