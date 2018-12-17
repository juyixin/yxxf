package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.evidence.model.Evidence;
import com.eazytec.evidence.service.EvidenceService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.link.model.Link;
import com.eazytec.model.Document;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;
import com.eazytec.webapp.util.ValidationUtil;

@Controller
public class EvidenceController  extends BaseFormController{
	

	public VelocityEngine velocityEngine;
	
	private EvidenceService evidenceService;
	
	@Autowired
	private FileManageService fileManageService;
	
	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	@Autowired
	public void setEvidenceService(EvidenceService evidenceService) {
		this.evidenceService = evidenceService;
	}
    
	
	protected final Log log = LogFactory.getLog(getClass());
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-14 下午7:52:35
     */
	@RequestMapping(value = "bpm/admin/evidenceList", method = RequestMethod.GET)
	public ModelAndView evidenceList(HttpServletRequest request, ModelMap model) {
        String script = null;
        String name = request.getParameter("searchText");
          try{
        	  List<Evidence> evidenceList = new ArrayList<Evidence>();
        	  if(name!=null&&name!=""){
          		  name=java.net.URLDecoder.decode(name, "utf-8");
          		  evidenceList = evidenceService.getEvidence1(name);
          	  }else{ 
          		  evidenceList = evidenceService.getEvidence();
          	  }
			String[] fieldNames = { "id","eventName", "concernName", "handlerName", "handlerPhone", "createDate"};
			script = GridUtil.generateScriptForEvidenceGrid(CommonUtil.getMapListFromObjectListByFieldNames(evidenceList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("evidence/evidenceList", model);
	}
	
	
    /**
     * Description:打开查看详情页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-15 上午10:37:35
     */
	@RequestMapping(value = "bpm/admin/evidenceDetail", method = RequestMethod.GET)
	public ModelAndView evidenceDetail(String id, HttpServletRequest request, ModelMap model) {
		//String id = request.getParameter("id");
		Evidence evidence = new Evidence();
		evidence = evidenceService.getDetail(id);
		List<FileManage> filePathList = fileManageService.getEvidence(id);
		model.addAttribute("filePathList", filePathList);
		model.addAttribute("evidence", evidence);
		return new ModelAndView("evidence/evidenceDetail", model);
	}
	
	
	/**
     * Description:打开查看详情页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-15 上午10:37:35
     */
	@RequestMapping(value = "bpm/admin/modifyEvidence", method = RequestMethod.GET)
	public ModelAndView modifyEvidence(String id,HttpServletRequest request, ModelMap model) {
		//String id = request.getParameter("id");
		Evidence evidence = new Evidence();
		evidence = evidenceService.getDetail(id);
		List<FileManage> filePathList = fileManageService.getEvidence(id);
		
		String[] fieldNames = { "id","filePath", "fileId", "fileName", "fileType", "description", "createName", "createDate"};
		String script = generateScriptForEvidenceGrid(CommonUtil.getMapListFromObjectListByFieldNames(filePathList, fieldNames, ""), velocityEngine);
		model.addAttribute("script", script);	
		
		model.addAttribute("evidence", evidence);
		return new ModelAndView("evidence/modifyEvidence", model);
	}
	
	public static String generateScriptForEvidenceGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','文件路径','文件ID','文件名','文件类型','描述','创建人','创建时间']";
		context.put("title", "Evidences");
		context.put("gridId", "EVIDENCE_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "filePath", "30", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "fileId", "30", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "fileName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "fileType", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createName", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createDate", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
     * Description:修改信息
     * 作者 : 蒋晨 
     * 时间 : 2017-4-13 上午13:47:35
     */
	@RequestMapping(value = "bpm/admin/saveEvidence", method = RequestMethod.POST)
	public ModelAndView saveEvidence(HttpServletRequest request, @ModelAttribute("evidence")Evidence evidence, ModelMap model,BindingResult errors) {
		User user = CommonUtil.getLoggedInUser();
		try {
			List<FileManage> fileList = new ArrayList<FileManage>();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			evidence.setModifyDate(sdf.format(date));
			evidenceService.updateEvidence(evidence);
			List<MultipartFile> file = new ArrayList<MultipartFile>();
			if(evidence.getImgUrl().equals("1")){
				
				MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
				file = mhs.getFiles("files");
			}

			String path="";
			if(file.size() != 0 && !file.isEmpty()) {
				for (MultipartFile multipartFile : file){
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						FileManage  fileManage = new FileManage();
						
						 String fileName = multipartFile.getOriginalFilename();
						 String fileName1 = fileName;
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
						 String uploadDir = getServletContext().getRealPath("/resources/fileManage") + "/";
				            File dirPath = new File(uploadDir);

				            if (!dirPath.exists()) {
				                dirPath.mkdirs();
				            }
				            
				            InputStream stream = multipartFile.getInputStream();
				            OutputStream bos = new FileOutputStream(uploadDir + fileName);
				            int bytesRead;
				            byte[] buffer = new byte[8192];

				            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				                bos.write(buffer, 0, bytesRead);
				            }

				            bos.close();
				            stream.close();
				            String link1 = "/resources/fileManage" + "/";
				            path = link1 + fileName+";"+fileName1;
				            String [] array = path.split(";");
				            String array0 = array[0];
				            String array1 = array[1];
				            fileManage.setFileId(evidence.getId());
				            fileManage.setFileName(array1);
				            fileManage.setFilePath(array0);
				            String type = array1.substring(array1.lastIndexOf(".")+1);
				           /* if(type.equals("jpg")||type.equals("png")||type.equals("gif")){
				            	fileManage.setFileType("image"+"\\"+type);
				            }else if(type.equals("mp4")||type.equals("avi")||type.equals("mkv")){
				            	fileManage.setFileType("video"+"\\"+type);
				            }else if(type.equals("mp3")||type.equals("wma")||type.equals("mav")){
				            	fileManage.setFileType("voice"+"\\"+type);
				            }*/
				            fileManage.setFileType(type);
				            fileManage.setCreateName(user.getId());
				            fileManage.setCreateDate(sdf.format(date));
				            fileList.add(fileManage);
					}
				}
			}
				
			String message = fileManageService.saveFileManage1(fileList);
			if(!message.equals("true")){
				saveError(request, "上传文件失败");
				return evidenceDetail(evidence.getId(),request, model);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return evidenceList(request, model);
	}
	
	
	/**
     * Description:删除文件
     * 作者 : 蒋晨 
     * 时间 : 2017-4-13 上午13:47:35
     */
	@RequestMapping(value="bpm/admin/deleteEvidence",method = RequestMethod.GET)
	public @ResponseBody ModelAndView deleteEvidence(String id, String evidenceId,HttpServletRequest request, ModelMap model){
    	Locale locale = request.getLocale();
		if(!StringUtil.isEmptyString(id)){
			evidenceService.deleteEvidence(id);
		}
		saveMessage(request, getText("success.document.delete",locale));
		return evidenceDetail(evidenceId,request,model);	
	}
	
	
	/**
     * Description:下载文件
     * 作者 : 蒋晨 
     * 时间 : 2017-2-16 上午11:06:06
     */
	@RequestMapping(value="bpm/admin/downloadEvidence",method = RequestMethod.GET)
	public void downloadEvidence(HttpServletResponse response,
			HttpServletRequest request) {
		
		String id = request.getParameter("id");
		String filePath = "";
		String fileId = "";
		String fileName = "";
		FileManage fileManage = new FileManage();
		try {
			fileManage = fileManageService.getEvidence1(id);
			fileName = fileManage.getFileName();
			filePath = fileManage.getFilePath();
			fileId = fileManage.getFileId();
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
            
			//response.setContentType(document.getMimeType());
			response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
			OutputStream o;
			o = response.getOutputStream();
			InputStream is = new FileInputStream(filePath + "/"
							+ fileId + "/"
							+ fileName);
			IOUtils.copy(is, o);
			o.flush();
			o.close();
		
		} catch (Exception e) {
			log.error(e.getMessage(),e);

		}
	}
	
	/**
	 * method to get user profile image path
	 *
	 * @param user
	 * @param file
	 * @param remoteUser
	 * @param contextPath
	 * @return pictureByteArrayId
	 * @throws IOException
	 * @throws InvalidNameException 
	 */
	private String getUserProfileImagePath(CommonsMultipartFile file,String remoteUser, String contextPath)throws IOException, InvalidNameException{
		String path="";
		 if (file!=null){
			 String fileName = file.getOriginalFilename();
			 String fileName1 = fileName;
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
   	     if(!ValidationUtil.validateImage(fileName)){
				 throw new InvalidNameException("Invalid FileName");
			 }
			 
            String uploadDir = getServletContext().getRealPath("/resources/fileManage") + "/";
            File dirPath = new File(uploadDir);

            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            
            InputStream stream = file.getInputStream();
            OutputStream bos = new FileOutputStream(uploadDir + fileName);
            int bytesRead;
            byte[] buffer = new byte[8192];

            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.close();
            stream.close();
            String link1 = "/resources/fileManage" + "/";
            path = link1 + fileName+";"+fileName1;
        }
		return path;
	}
	

}
