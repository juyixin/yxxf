package com.eazytec.webapp.controller;

import java.io.FileInputStream;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.NoticeDocument.NoticeDocumentService;
import com.eazytec.bpm.admin.NoticePlat.NoticePlatService;
import com.eazytec.bpm.admin.NoticeUserPlat.NoticeUserPlatService;
import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.Document;
import com.eazytec.model.DocumentForm;
import com.eazytec.model.Folder;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.PermissionDTO;
import com.eazytec.model.User;
import com.eazytec.util.PropertyReader;
import com.eazytec.webapp.controller.BaseFormController;
import com.thoughtworks.xstream.io.binary.Token.Value;

/**
 * <p>The controller for department related operations like its CRUD, list, grid view etc on screen</p>
 *
 * @author mathi
 */
@Controller
public class NoticePlatController extends BaseFormController {
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    public VelocityEngine velocityEngine;
    
    public NoticePlatService noticeplatService;
    
    public NoticeDocumentService noticedocumentService;
    
    public NoticeUserPlatService  noticeuserplatService;

    
    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    @Autowired
	private DataDictionaryService dicService;
    
    @Autowired
	public void setNoticeUserPlatService(NoticeUserPlatService noticeuserplatService) {
        this.noticeuserplatService = noticeuserplatService;
    }
    
    @Autowired
	public void setNoticePlatService(NoticePlatService noticeplatService) {
        this.noticeplatService = noticeplatService;
    }
    
    @Autowired
	public void setNoticeDocumentService(NoticeDocumentService noticedocumentService) {
        this.noticedocumentService = noticedocumentService;
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
    @RequestMapping(value="bpm/admin/showNoticePLatList",method = RequestMethod.GET)
    public ModelAndView showNoticePLatList( ModelMap model,HttpServletRequest request) throws Exception {
    	String home = request.getParameter("JC");
        try { 
        	String userid=CommonUtil.getLoggedInUserId();
        	String userName=CommonUtil.getLoggedInUserName();
        	List<NoticePlat> newList = new ArrayList<NoticePlat>();
        	User user=CommonUtil.getLoggedInUser();
        	List<NoticePlat> noticePlatList = new ArrayList<NoticePlat>();
        	if(home!=null&&home!=""){
        		String isActive = "是";
        		noticePlatList = noticeplatService.getNoticeListByUserid1(isActive);
        	}else{
        		noticePlatList = noticeplatService.getNoticeListByUserid();
        	}
        	 String[] fieldNames={"id","title","createperson","createtime","sffb"};
            String script=GridUtil.generateScriptForNoticePlatGrid(CommonUtil.getMapListFromObjectListByFieldNames(noticePlatList,fieldNames,""), velocityEngine);
            model.addAttribute("script", script);  
        } catch (SearchException se) {
            model.addAttribute("searchError", se.getMessage());
            
           
        }
        return new ModelAndView("admin/showNoticePLatList", model);   	
    }

    
	@RequestMapping(value = "bpm/admin/addNoticeForm",method = RequestMethod.GET)
	public ModelAndView addNoticeForm(ModelMap model,HttpServletRequest request) {

		try{
			NoticePlat noticeForm=new NoticePlat();
	    	model.addAttribute("noticeForm",noticeForm);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/noticeForm", model);

	}
	
	@RequestMapping(value = "bpm/admin/editNoticeForm",method = RequestMethod.GET)
	public ModelAndView editNoticeForm(ModelMap model,HttpServletRequest request) {

		try{
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
			
			model.addAttribute("dicList", dicList);
			NoticePlat noticeForm=new NoticePlat();
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				System.out.println(request.getParameter("id"));
				noticeForm=noticeplatService.getNoticePlatById(request.getParameter("id"));
				List<NoticeDocument> documents=noticedocumentService.getDocumentsByParentid(request.getParameter("id"));
				if(documents !=null){
					noticeForm.setDocuments(documents);
				}
			}
			 
	    	model.addAttribute("noticeForm",noticeForm);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/noticeForm", model);

	}
	
	@RequestMapping(value = "bpm/admin/editNoticeForm1",method = RequestMethod.GET)
	public ModelAndView editNoticeForm1(ModelMap model,HttpServletRequest request) {

		try{
			List<DataDictionary> dicList = dicService.getDataDictionaryByParentCode("SFYX");
			
			model.addAttribute("dicList", dicList);
			NoticePlat noticeForm=new NoticePlat();
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				System.out.println(request.getParameter("id"));
				noticeForm=noticeplatService.getNoticePlatById(request.getParameter("id"));
				List<NoticeDocument> documents=noticedocumentService.getDocumentsByParentid(request.getParameter("id"));
				if(documents !=null){
					noticeForm.setDocuments(documents);
				}
			}
			 
	    	model.addAttribute("noticeForm",noticeForm);
	    	//model.addAttribute("readOnly","readOnly");
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/noticeForm2", model);

	}
	
	
	@RequestMapping(value = "bpm/admin/showReadOnlyForm",method = RequestMethod.GET)
	public ModelAndView showReadOnlyForm(@RequestParam("noticeId") String noticeid,ModelMap model,HttpServletRequest request) {

		try{
				String userid=CommonUtil.getLoggedInUserId();
				NoticePlat noticeForm=new NoticePlat();
				noticeuserplatService.updateIsRead(userid,noticeid);
				noticeForm=noticeplatService.getNoticePlatById(noticeid);
				List<NoticeDocument> documents=noticedocumentService.getDocumentsByParentid(noticeid);
				if(documents !=null){
					noticeForm.setDocuments(documents);
				}
				model.addAttribute("noticeForm",noticeForm);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/noticeReadOnlyForm", model);

	}
	
	
	
	
	@RequestMapping(value = "bpm/admin/saveNoticeForm",method = RequestMethod.POST)
	 public ModelAndView saveDocumentForm(@RequestParam("deleteArray") List<String[]> deleteArray,ModelMap model,@ModelAttribute("noticeForm") NoticePlat noticeForm, BindingResult errors, HttpServletRequest request,
	            HttpServletResponse response) {
		
		try{
 			String path=getServletContext().getRealPath("/resources") + "/notice/";

 			User user=CommonUtil.getLoggedInUser();

			noticeForm = noticeplatService.mysaveOrUpdateDocumentForm(noticeForm,noticeForm.getFiles(),noticeForm.getMyfiles(), user.getFullName(), path,deleteArray);
			//Getting permissions for delete while editing document form
			
			model.addAttribute("noticeForm",noticeForm);

		} catch (Exception e) {
			 return new ModelAndView("admin/noticeForm",model);
		}
		return new ModelAndView("redirect:/bpm/admin/showNoticePLatList");
    }
    
	
	/**
	 * download Document Form page.
	 * 
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/downloadNoticeDoc",method = RequestMethod.GET)
	public void downloadDocumentnotice(HttpServletResponse response,
			HttpServletRequest request) {
		//log.info("Preparing to download Document");
		String fileName = null;
		NoticeDocument document = new NoticeDocument();
		try {
			if (!StringUtil.isEmptyString(request.getParameter("id"))) {
				document = noticedocumentService.getNoticeDocumentById(request
						.getParameter("id"));
			}
			fileName = document.getName();
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
			response.setContentType(document.getMimeType());
			response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
			OutputStream o;
			o = response.getOutputStream();
			InputStream is = new FileInputStream(document.getPath());
			IOUtils.copy(is, o);
			o.flush();
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	//删除
	@RequestMapping(value = "bpm/admin/noticeremove", method = RequestMethod.GET)
	public ModelAndView deleteSelectedHrjInfosNotice(@RequestParam("hrjInfoIds") String hrjInfoIds, HttpServletRequest request) {
		
		 List<String> hrjInfoIdList = new ArrayList<String>();
		 
		 if (hrjInfoIds.contains(",")) {
			  String[] ids = hrjInfoIds.split(",");
			  for(String id :ids){
				  hrjInfoIdList.add(id);
			  }
			} else {
				hrjInfoIdList.add(hrjInfoIds);	
	}

			
		 noticeplatService.removeNotice(hrjInfoIdList);
		return new ModelAndView("redirect:/bpm/admin/showNoticePLatList");
	}
	
}
