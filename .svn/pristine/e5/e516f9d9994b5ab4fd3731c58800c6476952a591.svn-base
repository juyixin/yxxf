package com.eazytec.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.apache.commons.io.IOUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.util.DateUtil;


/**
 * <p>Util for template specific blocks like merging for vm templates, context etc</p>
 * 
 * @author madan
 */
public class FileUtil {
	public static final String PROCESS_TYPE_TASK = "Task";
	public static final String PROCESS_TYPE_START = "StartNode";
	public static Map<String, byte[]>getFileUploadMap(HttpServletRequest request) throws IOException{
		try{
			 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;	
			  MultiValueMap<String, MultipartFile> map = multipartRequest.getMultiFileMap();
			   
			   Map<String, byte[]>files = new HashMap<String, byte[]>();
			   
			   for (String fileUploadElement : map.keySet()) {
				List<MultipartFile>mfs = map.get(fileUploadElement);					
				files.put(fileUploadElement, mfs.get(0).getBytes());
			   }
			   return files;
		}catch(Exception e){
			return null;
			
		}
		
	}
	
	public static Map<String, String> uploadFile(HttpServletRequest request, String uploadDir) throws IOException{
		try{			
			 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;	
			  MultiValueMap<String, MultipartFile> fileInList = multipartRequest.getMultiFileMap();
			  Map<String, String>filePathMap = new HashMap<String, String>();
			// Create the directory if it doesn't exist
		         File dirPath = new File(uploadDir);
		         if (!dirPath.exists()) {
		             dirPath.mkdirs();
		         }
		         for (String fileUploadElement : fileInList.keySet()) {		        	 
		        	 List<MultipartFile> files =fileInList.get(fileUploadElement);
		        	 for (MultipartFile multipartFile : files) {
			                // retrieve the file data
			                InputStream stream = multipartFile.getInputStream();
			                // write the file to the file specified
			                String uploadFilePathName = uploadDir + multipartFile.getOriginalFilename();
			                filePathMap.put(fileUploadElement, uploadFilePathName);
			                OutputStream bos = new FileOutputStream(uploadFilePathName);
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
		         return filePathMap;
		}catch(Exception e){
			throw new EazyBpmException("Problem uploading file(s)");			
		}
		
	}	
	
	public static Map<String, String> uploadFileForTask(HttpServletRequest request, String uploadDir, String taskId, String proInsId, String path, RuntimeService runtimeService, String ProcessType) throws IOException{
		try{			
			 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;	
			  MultiValueMap<String, MultipartFile> fileInList = multipartRequest.getMultiFileMap();
			  Map<String, String>filePathMap = new HashMap<String, String>();
			// Create the directory if it doesn't exist
		         File dirPath = new File(uploadDir);
		         if (!dirPath.exists()) {
		             dirPath.mkdirs();
		         }
		         int attachmentCount = 1;
		         Date date = new Date();
	             String dateTime = DateUtil.convertDateToDefalutDateTimeString(date);
		         for (String fileUploadElement : fileInList.keySet()) {		        	 
		        	 List<MultipartFile> files =fileInList.get(fileUploadElement);
		        	 for (MultipartFile multipartFile : files) {
			                // retrieve the file data
			                InputStream stream = multipartFile.getInputStream();
			                if(runtimeService!= null){
			                	runtimeService.createAttachmentOfTask(multipartFile.getContentType(), taskId, proInsId, multipartFile.getOriginalFilename(),null, null,  uploadDir,attachmentCount,dateTime);
			                }
			               
			                // write the file to the file specified
			                String uploadFilePathName = uploadDir + attachmentCount+"_"+dateTime+"_"+multipartFile.getOriginalFilename();
			                attachmentCount++;
			                filePathMap.put(fileUploadElement, uploadFilePathName);
			                OutputStream bos = new FileOutputStream(uploadFilePathName);
			               
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
		         return filePathMap;
		}catch(Exception e){
			throw new EazyBpmException("Problem uploading file(s)");			
		}
	}	
	
	public static String writeTempFileToViewFile(String filePath,String fileName,String fileType,String repositaryCachePdf,String tmpFilePath) throws Exception{
		return null;
		/**
		UUID uuid= UUID.randomUUID();
		File pdfCache = new File(repositaryCachePdf + File.separator + "pdf" +  File.separator + uuid + ".pdf");
		try{
			File f=new File(repositaryCachePdf+"/tmp/");
			if(!f.exists()){
				f.mkdirs();
			}
			File tmp = File.createTempFile("bpm", "." + FileUtils.getFileExtension(fileName),f);
			InputStream is = new FileInputStream(filePath);
			FileUtils.copy(is, tmp);
			is.close();
			
			if(fileType.equals(MimeTypeConfig.MIME_PDF)){
				tmpFilePath = tmpFilePath +"/tmp/"+tmp.getName();
			}else if(DocConverter.validImageMagick.contains(fileType)){
				tmpFilePath = tmpFilePath +"/tmp/"+tmp.getName();
			}else if(fileType.equals(MimeTypeConfig.MIME_XML)){
				tmpFilePath = tmpFilePath +"/tmp/"+tmp.getName();
			}else{
				if (DocConverter.getInstance().convertibleToPdf(fileType)) {
					  if (DocConverter.validOpenOffice.contains(fileType)) {
						  tmpFilePath = tmpFilePath +"/pdf/"+uuid+".pdf";
						  DocConverter.getInstance().doc2pdf(tmp, fileType, pdfCache);
						  tmp.delete();
						}else 	if (fileType.equals(MimeTypeConfig.MIME_ZIP)) {
							// This is an internal conversion and does not need 3er party software
							 tmpFilePath = tmpFilePath +"/pdf/"+uuid+".pdf";
							 DocConverter.getInstance().zip2pdf(tmp, pdfCache);
							 tmp.delete();
						} else{
							tmpFilePath = "/resources/conversion_not_avail.pdf";
							//throw new NotImplementedException("Conversion from '" + fileType + "' to PDF not available");
						}
				}else{
					tmpFilePath = "/resources/conversion_problem.pdf";
				}
			}
			return tmpFilePath;
		}catch(Exception e){
			throw new Exception("Error while preparing file to view "+e.getMessage(),e);
		}
		**/
	}
	
	public static String converFileContentIntoString(File file) throws IOException{
		Reader input = new FileReader(file);
		StringWriter output = new StringWriter();
		try {
		  IOUtils.copy(input, output);
		} finally {
		  input.close();
		}
		return output.toString();
	}
}
