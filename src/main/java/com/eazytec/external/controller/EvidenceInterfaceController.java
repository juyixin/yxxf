package com.eazytec.external.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.base.Constant;
import com.eazytec.crm.model.Record;
import com.eazytec.evidence.model.Evidence;
import com.eazytec.evidence.model.EvidenceApp;
import com.eazytec.evidence.service.EvidenceService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;
import com.eazytec.webapp.controller.BaseFormController;

@Controller("extEvidenceInterfaceController")
public class EvidenceInterfaceController extends BaseFormController{
	
	private static final Logger log = LoggerFactory.getLogger(EvidenceInterfaceController.class);
	
	@Autowired
	private FileManageService fileManageService;
	
	
	
	/**
     * Description:证据上传
     * 作者 : 蒋晨 
     * 时间 : 2017-2-10 下午1:38:35
	 * @throws IOException 
     */
	@RequestMapping(value = "evdienceFile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		//List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		/*// 从请求头中获取文件名
		String fileName = ExternalUtils.getFileName(request);
		// 获取文件名后缀
		String ext = FilenameUtils.getExtension(fileName);*/
		InputStream  file = request.getInputStream();
		String type = ExternalUtils.getFileName(request);
		//String type = request.getParameter("type");
		try {
			if(file != null&&type != null){
				
			
				/*Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm:ss");*/
				String timeStamp = DateUtil.convertDateToDefalutDateTimeString(new Date());
				String fileN = timeStamp + RandomStringUtils.randomNumeric(6);
		    	String fileName =  (fileN+ "证据采集"+"."+type).trim();
//		    	/String fileName1 = fileName;
		    	//InputStream is = new FileInputStream(file);
		    	// 获得字节数组，用来家计算文件大小
				byte[] data = IOUtils.toByteArray(file);
				
				int pos = fileName.lastIndexOf(".");
				String extension = fileName.substring(pos+1);
				fileName = fileName.substring(0, pos);
				fileName = fileName.replace(" ", "_");
	   	        byte[] encodedFileName = fileName.getBytes("UTF-8");
	   	        fileName= encodedFileName.toString();
	   	        if(fileName.startsWith("[")){
	   	            fileName = fileName.substring(1);
	   	         }
				fileName = fileName+"."+extension;
				// 文件输出路径，文件加上时间重命名，可根据项目自行修改
				//String filePath = "F:\\workspace\\yxxf\\src\\main\\webapp\\resources\\app\\" ;
				//String filePath = request.getRealPath("/resources/fileManage")+"\\"+fileName;
				String filePath1 = getServletContext().getRealPath("/resources/fileManage") + "/"+fileName;
				String filePath = "/resources/fileManage"+ "/"+fileName;
				File file1 = new File(filePath1);
				
				//输出到文件
				FileUtils.copyInputStreamToFile(new ByteArrayInputStream(data), file1);
		        //map.put("fileName", fileName1);
				map.put("filePath", filePath);
			}else{
				return ExternalUtils.error(map, "具体请求参数为空");	
			}
		
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ExternalUtils.error(map, "上传出错");
		}
		return ExternalUtils.success(map);
	}

}
