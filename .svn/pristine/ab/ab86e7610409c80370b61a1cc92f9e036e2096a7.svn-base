package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.link.model.Link;
import com.eazytec.link.service.LinkService;
import com.eazytec.model.User;
import com.eazytec.webapp.util.ValidationUtil;

@Controller
public class LinkController extends BaseFormController{
	
	public VelocityEngine velocityEngine;
	
	private LinkService linkService;
	
	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	
	@Autowired
	public void setLinkService(LinkService linkService) {
		this.linkService = linkService;
	}
	
	
	protected final Log log = LogFactory.getLog(getClass());
	
	
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@RequestMapping(value = "bpm/admin/linkList", method = RequestMethod.GET)
	public ModelAndView linkList(HttpServletRequest request, ModelMap model) {
        String script = null;
        // Locale locale = request.getLocale();
          try{
			List<Link> linkList = linkService.getLink();
			String[] fieldNames = { "id","title", "url", "createDate", "isActive"};
			script = GridUtil.generateScriptForLinkGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("link/linkList", model);
	}
	
	/**
     * Description:打开新增友情链接页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午3:15:25
     */
	@RequestMapping(value = "bpm/admin/newLink", method = RequestMethod.GET)
	public ModelAndView newLink(HttpServletRequest request, ModelMap model) {
		String id = request.getParameter("id");
		Link link = new Link();
		model.addAttribute("link", link);
		return new ModelAndView("link/newLink", model);
	}
	
	
	/**
     * Description:打开修改友情链接页面
     * 作者 : 蒋晨 
     * 时间 : 2017-2-7 上午10:32:23
     */
	@RequestMapping(value = "bpm/admin/modifyLink", method = RequestMethod.GET)
	public ModelAndView modifyLink(HttpServletRequest request, ModelMap model) {
		String id = request.getParameter("id");
		Link link = new Link();
		if(id!=null){
			String readOnly = "readOnly";
			model.addAttribute("readOnly", readOnly);
			link = linkService.getDetail(id);
			model.addAttribute("url", link.getPictureUrl());
		}
		model.addAttribute("link", link);
		return new ModelAndView("link/modifyLink", model);
	}
	
	
	/*
	/**
     * Description:上传图片
     * 作者 : 蒋晨 
     * 时间 : 2017-2-7 下午15:12:13
     *
	@RequestMapping(value = "bpm/admin/uploadImg", method = RequestMethod.GET)
	@ResponseBody
	public void uploadImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
        JSONObject jsonResult = new JSONObject();
        String remoteUser = request.getRemoteUser();
		String contextPath = request.getContextPath();
        Link link = new Link();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		String url = "";
		String message="";
		if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
			url = getUserProfileImagePath(link,file, remoteUser, contextPath);
			if(url != null && !url.isEmpty()){
				message = "true";
			}
		} 
		
	    
	    try {
			out = response.getWriter();
			if(message.equals("true")){
                jsonResult.put("code", "1");
                jsonResult.put("msg", "上传成功");   
                jsonResult.put("url", url);
            }else{
            	 jsonResult.put("code", "-1");
                 jsonResult.put("msg", "上传失败");
            } 
		}catch (IOException e) {
            jsonResult.put("code", "-1");
            jsonResult.put("msg", e.getMessage());
        } finally {
            if (out != null) {
                out.write(jsonResult.toString());
                out.flush();
                out.close();
            }
        }
	    
	}*/
	
	
	
	/**
     * Description:保存友情链接信息
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午3:42:57
     */
	@RequestMapping(value = "bpm/Link/saveLinkList", method = RequestMethod.POST)
	public ModelAndView saveLinkList(HttpServletRequest request,@ModelAttribute("link") Link link,ModelMap model, BindingResult errors) throws Exception {
         
		try {
			String remoteUser = request.getRemoteUser();
			String contextPath = request.getContextPath();
			if (link.getId().equals(Constants.EMPTY_STRING)) {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				link.setCreateDate(sdf.format(date));
				String a = link.getModifyFileImg();
				if(a.equals("1")){
					
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
					if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
						String path = getUserProfileImagePath(link,file, remoteUser, contextPath);
						String [] array = path.split(";");
						String array0 = array[0];
						String array1 = array[1];
						link.setPictureUrl(array0);
						link.setFileName(array1);
					} 
					//link.setIsActive("1");
				}
				linkService.saveLinkInfo(link);
			}else{
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String a = link.getModifyFileImg();
				if(a.equals("1")){
					
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
					if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
						String path = getUserProfileImagePath(link,file, remoteUser, contextPath);
						String [] array = path.split(";");
						String array0 = array[0];
						String array1 = array[1];
						link.setPictureUrl(array0);
						link.setFileName(array1);;
					} 
				}else{
					link.setPictureUrl(link.getModifyFileUrl());
				}
				/*String isActive = link.getIsActive();
				String title = link.getTitle();
				String url = link.getUrl();*/
				link.setModifyDate(sdf.format(date));
				linkService.updateLinkInfo(link);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		    return linkList(request, model);
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
	private String getUserProfileImagePath(Link link, CommonsMultipartFile file,String remoteUser, String contextPath)throws IOException, InvalidNameException{
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
			 
            String uploadDir = getServletContext().getRealPath("/resources/link") + "/";
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
            String link1 = contextPath + "/resources/link" + "/";
            return link1 + fileName+";"+fileName1;
        }else if (link.getPictureUrl().isEmpty()){
        	 return "/images/profile/default.png";
        }else {
       	 return link.getPictureUrl();
        }
	}
	
	
	
	
	/**
     * Description:改变状态
     * 作者 : 蒋晨 
     * 时间 : 2017-2-6 下午1:46:46
     */
	@RequestMapping(value = "bpm/admin/changeStatus", method = RequestMethod.GET)
	public ModelAndView changeStatus(HttpServletRequest request, ModelMap model) {
		
		//String state = request.getParameter("state");
		String id = request.getParameter("id");
		String state="2";
		linkService.updateStatus(id,state);
		return linkList(request, model);
		
	}
	
	/**
     * Description:改变状态
     * 作者 : 蒋晨 
     * 时间 : 2017-2-6 下午1:46:46
     */
	@RequestMapping(value = "bpm/admin/changeStatus1", method = RequestMethod.GET)
	public ModelAndView changeStatus1(HttpServletRequest request, ModelMap model) {
		
		//String state = request.getParameter("state");
		String id = request.getParameter("id");
		String state="1";
		linkService.updateStatus(id,state);
		return linkList(request, model);
		
	}

	/**
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-7 上午9:32:07
     */
	@RequestMapping(value = "bpm/admin/searchLink", method = RequestMethod.GET)
	public ModelAndView searchLink(HttpServletRequest request, ModelMap model) {
        String script = null;
        String name = request.getParameter("searchText");
          try{
        	if(name!=null&&name!=""){
        		name=java.net.URLDecoder.decode(name, "utf-8");
        	}
			List<Link> linkList = linkService.searchLink(name);
			String[] fieldNames = { "id","title", "url", "createDate", "isActive"};
			script = GridUtil.generateScriptForLinkGrid(CommonUtil.getMapListFromObjectListByFieldNames(linkList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
          } catch (Exception e) {
              log.error(e.getMessage(), e);
    	}
		    return new ModelAndView("link/linkList", model);
	}
	
	
	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-7 上午9:53:13
     */
	@RequestMapping(value = "bpm/admin/deleteLinkList", method = RequestMethod.GET)
	@ResponseBody
	public void deleteUnitUnion(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = null;
        JSONObject jsonResult = new JSONObject();
        String [] array = id.split(",");
        List<String> titleIdList= new ArrayList<String>();
        Collections.addAll(titleIdList, array);
	    String id2 = "";
	    String id1 = "";
	    List<String> isFalse = new ArrayList<String>();
	    for(String s : titleIdList){
	    	
	    	if(s.indexOf("[")!=-1){
	    		id2 = s.replace("[", "").trim();
	    		id1 = id2.replace("\"", "").trim();
	    		if(id1.indexOf("]")!=-1){
	    			id1 = id1.replace("]", "").trim();
	    		}
	    	}else if(s.indexOf("]")!=-1){
	    		id2 = s.replace("]", "").trim();
	    		id1 = id2.replace("\"", "").trim();
	    	}else{
	    		id1 = s.replace("\"", "").trim();
	    	}
	    	isFalse.add(id1);
	    }
	    String message="";
	    if(isFalse!=null&&isFalse.size()!=0){
			message = linkService.deleteLinkInfo(isFalse);
	    }
	    try {
			out = response.getWriter();
			if(message.equals("true")){
                jsonResult.put("code", "1");
                jsonResult.put("msg", "删除成功");   
            }else{
            	 jsonResult.put("code", "-1");
                 jsonResult.put("msg", "删除失败");
            } 
		}catch (IOException e) {
            jsonResult.put("code", "-1");
            jsonResult.put("msg", e.getMessage());
        } finally {
            if (out != null) {
                out.write(jsonResult.toString());
                out.flush();
                out.close();
            }
        }
	    
	}
}
