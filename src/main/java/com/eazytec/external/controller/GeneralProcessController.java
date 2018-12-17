package com.eazytec.external.controller;

import java.net.UnknownHostException;
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

import com.eazytec.Constants;
import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.generalProcess.model.GeneralProcess;
import com.eazytec.generalProcess.service.GeneralProcessService;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.User;
import com.eazytec.sendDocuments.model.AppName;
import com.eazytec.service.UserService;
import com.eazytec.util.PageBean;
import com.eazytec.vacate.model.Vacate;
import com.eazytec.vacate.service.VacateService;


@Controller("extGeneralProcessController")
public class GeneralProcessController {
	
private static final Logger log = LoggerFactory.getLogger(GeneralProcessController.class);
	
	@Autowired
	private  VacateService  vacateService;
	
	@Autowired
	private FileManageService fileManageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private  GeneralProcessService  generalProcessService;
	
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-4-17 上午8:51:35
     */
	@RequestMapping(value = "saveGeneralProcess", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGeneralProcess(GeneralProcess send, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = ExternalUtils.getUser(request);
		try {
			FileManage fileManage = new FileManage();
			GeneralProcess sd = new GeneralProcess();
			List<FileManage> fileList = new ArrayList<FileManage>();
			//文件路劲
			String filePathPhotos = "";
			
			String[] fileVideos = null;
			String j = "";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			send.setCreateTime(sdf1.format(new Date()));
			send.setOriginator(user);
			send.setState("SHZT1");
			sd = generalProcessService.saveGeneralProcessInfo(send);
			if(sd.getId()!=null){
    				if(send.getDocument()!=null&&send.getDocument()!=""){
    					filePathPhotos = send.getDocument();		
    					fileVideos = filePathPhotos.split(";");
    					for(String s : fileVideos){
    						fileManage = new FileManage();
    						j=s.substring(s.lastIndexOf(".")+1);
    						fileManage.setFileType(j);
    						String fileN = send.getItemName()+ RandomStringUtils.randomNumeric(6);
    				    	String fileName =  (fileN+"."+j).trim(); 
    						fileManage.setFileName(fileName);
    						fileManage.setFileId(sd.getId());
    						fileManage.setFilePath(s);
    						fileManage.setCreateName(send.getOriginator().getFirstName());
    						Date date = new Date();
    						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    						fileManage.setCreateDate(sdf.format(date));
    						fileList.add(fileManage);
    					}
    				}
				    
                     if(!fileList.isEmpty()&&fileList.size()!=0){
    					String message = fileManageService.saveFileManage1(fileList);
    					if(message.equals("true")){
    						map.put("id", sd.getId());
    					}else{
    						return ExternalUtils.error(map, "保存文件失败");
    					}
    				}else{
    					map.put("id", sd.getId());
    				}
		     }else{
				  return ExternalUtils.error(map, "保存失败");
		     }	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "保存失败");
		}
		return ExternalUtils.success(map);
	}
	
	
	
	/**
	  * Description:初始化数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-26 下午8:57:35
	 * @throws UnknownHostException 
	  */
	@RequestMapping(value = "generalProcessList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> generalProcessList(Integer pageNo, Integer pageSize,String name, String userId, String type, String state,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PageBean<GeneralProcess> pageBean = new PageBean<GeneralProcess>(pageNo, pageSize);
			pageBean = generalProcessService.getRecords(pageBean, name,userId,type,state);
			//System.out.println(pageBean.getTotalrecords());
			int totla = pageBean.getTotalrecords();
			int current= pageNo;
			List<GeneralProcess> records = pageBean.getResult();
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			User u = new User();
			for (GeneralProcess person : records) {
				Map<String, Object> m = new HashMap<String, Object>();
				u = userService.getUser(person.getApprover());
				m.put("approver", u.getFirstName());
				if(person.getCopyPeople()!=null){
					
					String [] people = person.getCopyPeople().split(",");
					String jc = "";
					if(people.length>1){
						for(String s : people){
							u = userService.getUser(s);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.trim();
						jc = jc.substring(0,jc.length()-1);
					}else{
						u = userService.getUser(person.getCopyPeople());
						jc = u.getFirstName();
					}
					
					m.put("copyPeople", jc);
				}else{
					m.put("copyPeople", person.getCopyPeople());
				}
				
				
				m.put("originator", person.getOriginator().getFirstName());
				m.put("id", person.getId());
				m.put("itemName", person.getItemName());
				m.put("itemDescription", person.getItemDescription());
				m.put("remark", person.getRemark());
				DataDictionary dcl = new DataDictionary();
				m.put("opinion", person.getOpinion());
				dcl = vacateService.getNameList(person.getState());
				m.put("state", dcl.getName());
				m.put("createTime", person.getCreateTime());
				datas.add(m);
			}
			
			map.put("totla", totla);
			map.put("current", current);
			map.put("datas", datas);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取列表失败");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	  * Description:查询详情
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-26 下午8:57:35
	  */
	@RequestMapping(value = "generalProcessDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> generalProcessDetail(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			GeneralProcess s = generalProcessService.getDetail(id);
			if(s != null){
				
				List<AppName> an = new ArrayList<AppName>();
				AppName a = new AppName();
				User u = new User();
				u = userService.getUser(s.getApprover());
				a.setCode(u.getId());
				a.setName(u.getFirstName());
				an.add(a);
				map.put("approver", an);
				if(s.getCopyPeople()!=null){
					
					String [] people = s.getCopyPeople().split(",");
					an = new ArrayList<AppName>();
					for(String c : people){
						u = userService.getUser(c);
						a = new AppName();
						a.setCode(u.getId());
						a.setName(u.getFirstName());
						an.add(a);
					}
					map.put("copyPeople", an);
				}else{
					map.put("copyPeople", s.getCopyPeople());
				}
				
				an = new ArrayList<AppName>();
				a = new AppName();
				a.setCode(s.getOriginator().getId());
				a.setName(s.getOriginator().getFirstName());
				an.add(a);
				map.put("originator", an);
				
				map.put("id", s.getId());
				map.put("itemName", s.getItemName());
				map.put("itemDescription", s.getItemDescription());
				map.put("remark", s.getRemark());
				DataDictionary dcl = new DataDictionary();
				map.put("opinion", s.getOpinion());
				dcl = vacateService.getNameList(s.getState());
				map.put("state", dcl.getName());
				map.put("createTime", s.getCreateTime());

				dcl = vacateService.getNameList(s.getState());
				an = new ArrayList<AppName>();
				a = new AppName();
				a.setCode(dcl.getCode());
				a.setName(dcl.getName());
				an.add(a);
				map.put("state", an);
				map.put("createTime", s.getCreateTime());
				
				List<FileManage> list = new ArrayList<FileManage>();
				list = fileManageService.getEvidence(id);
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				if(list.size()!=0&&!list.isEmpty()){
					for(FileManage f : list){
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("fileType", f.getFileType());
						m.put("filePath", Constants.IP_CONFIG+":"+request.getLocalPort()+f.getFilePath());
						//m.put("filePath", Constants.IP_CONFIG+":"+request.getLocalPort()+f.getFilePath());
						m.put("id", f.getId());
						m.put("fileName", f.getFileName());
						datas.add(m);
					}
				}
				map.put("datas", datas);
			}else{
				return ExternalUtils.error(map, "ID不存在");
			}	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取详情失败");
		}
		return ExternalUtils.success(map);
	}

}
