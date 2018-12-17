package com.eazytec.webapp.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.NoticeDocument.NoticeDocumentService;
import com.eazytec.bpm.admin.NoticePlat.NoticePlatService;
import com.eazytec.bpm.admin.NoticeUserPlat.NoticeUserPlatService;
import com.eazytec.bpm.admin.Zcfg.ZcfgService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.User;
import com.eazytec.model.Zcfg;

@Controller
public class ZcfgController  extends BaseFormController {
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    public VelocityEngine velocityEngine;
    
    public ZcfgService zcfgService;
    
    public FileManageService fileManageService;
    
    
    public NoticeUserPlatService  noticeuserplatService;

    
    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Autowired
	public void setNoticeUserPlatService(NoticeUserPlatService noticeuserplatService) {
        this.noticeuserplatService = noticeuserplatService;
    }
    
  
    @Autowired
    public void setZcfgService(ZcfgService zcfgService) {
		this.zcfgService = zcfgService;
	}

	
    
   
    @Autowired
	public void setFileManageService(FileManageService fileManageService) {
		this.fileManageService = fileManageService;
	}

	/**
     *
     */
    @Override
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
	
	/**
     *页面
     */
    @RequestMapping(value="bpm/admin/showZcfgLatList",method = RequestMethod.GET)
    public ModelAndView showZcfgList( ModelMap model) throws Exception {
        try { 
        	String userid=CommonUtil.getLoggedInUserId();
        	String userName=CommonUtil.getLoggedInUserName();
        	List<Zcfg> newList = new ArrayList<Zcfg>();
        	User user=CommonUtil.getLoggedInUser();
        	List<Zcfg> noticePlatList = zcfgService.getZcfgListByUserid();
        	 String[] fieldNames={"id","title","createperson","createtime","content"};
            String script=GridUtil.generateScriptZcfgGrid(CommonUtil.getMapListFromObjectListByFieldNames(noticePlatList,fieldNames,""), velocityEngine);
            model.addAttribute("script", script);  
        } catch (SearchException se) {
            model.addAttribute("searchError", se.getMessage());
            
           
        }
        return new ModelAndView("admin/zcfg", model);   	
    }
    
   
    
    
    
    
    @RequestMapping(value = "bpm/admin/addZcfgForm",method = RequestMethod.GET)
	public ModelAndView addNoticeForm(ModelMap model,HttpServletRequest request) {

		try{
			Zcfg zcfgForm=new Zcfg();
	    	model.addAttribute("zcfgForm",zcfgForm);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/zcfgadd", model);

	}
    
    
    
    @RequestMapping(value = "bpm/admin/saveZcfgForm",method = RequestMethod.POST)
	 public ModelAndView saveDocumentForm(@RequestParam("deleteArray") List<String[]> deleteArray,ModelMap model,@ModelAttribute("zcfgForm") Zcfg zcfgForm, BindingResult errors, HttpServletRequest request,
	            HttpServletResponse response) {
		
		try{
			String path=getServletContext().getRealPath("/resources") + "/zcfg/";

			User user=CommonUtil.getLoggedInUser();

			zcfgForm = zcfgService.mysaveOrUpdateZcfgForm(zcfgForm,zcfgForm.getFiles(),zcfgForm.getMyfiles(), user.getFullName(), path,deleteArray);
			//Getting permissions for delete while editing document form
			
			model.addAttribute("noticeForm",zcfgForm);

		} catch (Exception e) {
			 return new ModelAndView("admin/zcfgadd",model);
		}
		return new ModelAndView("redirect:/bpm/admin/showZcfgLatList");
   }
    
    
  //删除
  	@RequestMapping(value = "bpm/admin/zcfgremove", method = RequestMethod.GET)
  	public ModelAndView deleteAllChancesConfirmZcfgs(@RequestParam("hrjInfoIds") String hrjInfoIds, HttpServletRequest request) {
  		
  		 List<String> hrjInfoIdList = new ArrayList<String>();
  		 
  		 if (hrjInfoIds.contains(",")) {
  			  String[] ids = hrjInfoIds.split(",");
  			  for(String id :ids){
  				  hrjInfoIdList.add(id);
  			  }
  			} else {
  				hrjInfoIdList.add(hrjInfoIds);	
  	}

  			
  		zcfgService.removeZcfg(hrjInfoIdList);
  		
  		return new ModelAndView("redirect:/bpm/admin/showZcfgLatList");
  	}
  	
  	
  	@RequestMapping(value = "bpm/admin/editZcfgForm",method = RequestMethod.GET)
	public ModelAndView editZcfgForm(ModelMap model,HttpServletRequest request) {

		try{
			Zcfg zcfgForm=new Zcfg();
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				zcfgForm=zcfgService.getZcfgById(request.getParameter("id"));
				List<FileManage> documents=fileManageService.getEvidence(request.getParameter("id"));
				if(documents !=null){
					zcfgForm.setDocuments(documents);
				}
			}
			 
	    	model.addAttribute("zcfgForm",zcfgForm);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/zcfgadd", model);

	}
  	
  	
  	 /**
     *查看页面
     */
    @RequestMapping(value="bpm/admin/showZcfgckLatList",method = RequestMethod.GET)
    public ModelAndView showZcfgListchakan( ModelMap model) throws Exception {
        try { 
        	String userid=CommonUtil.getLoggedInUserId();
        	String userName=CommonUtil.getLoggedInUserName();
        	List<Zcfg> newList = new ArrayList<Zcfg>();
        	User user=CommonUtil.getLoggedInUser();
        	List<Zcfg> noticePlatList = zcfgService.getZcfgListByUserid();
        	 String[] fieldNames={"id","title","createperson","createtime","content"};
            String script=GridUtil.generateScriptZcfgckGrid(CommonUtil.getMapListFromObjectListByFieldNames(noticePlatList,fieldNames,""), velocityEngine);
            model.addAttribute("script", script);  
        } catch (SearchException se) {
            model.addAttribute("searchError", se.getMessage());
            
           
        }
        return new ModelAndView("admin/zcfgck", model);   	
    }
    
    
    @RequestMapping(value = "bpm/admin/editZcfgckForm",method = RequestMethod.GET)
	public ModelAndView editZcfgckForm(ModelMap model,HttpServletRequest request) {

		try{
			Zcfg zcfgForm=new Zcfg();
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				zcfgForm=zcfgService.getZcfgById(request.getParameter("id"));
				List<FileManage> documents=fileManageService.getEvidence(request.getParameter("id"));
				if(documents !=null){
					zcfgForm.setDocuments(documents);
				}
			}
			 
	    	model.addAttribute("zcfgForm",zcfgForm);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/zcfgckfrom", model);

	}
    
    @RequestMapping(value = "bpm/admin/downloadzcfgdy",method = RequestMethod.GET)
	public void downloadDocumentnotice(HttpServletResponse response,
			HttpServletRequest request) {
		//log.info("Preparing to download Document");
		String fileName = null;
		FileManage document = new FileManage();
		try {
			if (!StringUtil.isEmptyString(request.getParameter("id"))) {
				document = fileManageService.getFileManageById(request.getParameter("id"));
			}
			fileName = document.getFileName();
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
			OutputStream o;
			o = response.getOutputStream();
			InputStream is = new FileInputStream(document.getFilePath());
			IOUtils.copy(is, o);
			o.flush();
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  	

}
