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
import com.eazytec.crm.model.Record;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.User;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.sendDocuments.model.AppName;
import com.eazytec.sendDocuments.model.SendDocuments;
import com.eazytec.sendDocuments.service.SendDocumentsService;
import com.eazytec.service.UserService;
import com.eazytec.util.PageBean;

@Controller("extSendDocumentsController")
public class SendDocumentsController {
	
	private static final Logger log = LoggerFactory.getLogger(SendDocumentsController.class);
	
	@Autowired
	private  SendDocumentsService  sendDocumentsService;
	
	@Autowired
	private FileManageService fileManageService;
	
	@Autowired
	private UserService userService;
	
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-4-17 上午8:51:35
     */
	@RequestMapping(value = "saveSendDocuments", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveSendDocuments(SendDocuments send, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = ExternalUtils.getUser(request);
		try {
			FileManage fileManage = new FileManage();
			SendDocuments sd = new SendDocuments();
			List<FileManage> fileList = new ArrayList<FileManage>();
			//文件路径
			String filePathPhotos = "";
			String[] fileVideos = null;
			String j = "";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			send.setSendTime(sdf1.format(new Date()));
			send.setSender(user);
			sd = sendDocumentsService.saveSendDocumentsInfo(send);
			if(sd.getId()!=null){
				
    				if(send.getDocument()!=null&&send.getDocument()!=""){
    					filePathPhotos = send.getDocument();		
    					fileVideos = filePathPhotos.split(";");
    					for(String s : fileVideos){
    						fileManage = new FileManage();
    						/*c = s.replaceAll(reg,"$1");
    				        int dot = c.lastIndexOf('.');   
    				        if ((dot >-1) && (dot < (c.length() - 1))) {   
    				            j = c.substring(dot + 1);   
    				        }   */  
    						j=s.substring(s.lastIndexOf(".")+1);
    						fileManage.setFileType(j);
    						/*String fName = s.trim();  
	    				    String fileName = fName.substring(fName.lastIndexOf("/")+1); */
    						String fileN = send.getTheme() + RandomStringUtils.randomNumeric(6);
    				    	String fileName =  (fileN+"."+j).trim(); 
    						fileManage.setFileName(fileName);
    						fileManage.setFileId(sd.getId());
    						fileManage.setFilePath(s);
    						fileManage.setCreateName(send.getSender().getFirstName());
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
    						return ExternalUtils.error(map, "保存附件失败");
    					}
    				}else{
    					map.put("id", sd.getId());
    				}
		     }else{
				  return ExternalUtils.error(map, "文件发送失败");
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
	@RequestMapping(value = "sendList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendList(Integer pageNo, Integer pageSize,String name, String userId, String type, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		/*InetAddress addr = InetAddress.getLocalHost();
		String ip ="";
		ip=addr.getHostAddress().toString();//获得本机IP
*/		try {
	        //List<SendDocuments> list = sendDocumentsService.getSendDocuments();
			PageBean<SendDocuments> pageBean = new PageBean<SendDocuments>(pageNo, pageSize);
			pageBean = sendDocumentsService.getRecords(pageBean, name,userId,type);
			//System.out.println(pageBean.getTotalrecords());
			int totla = pageBean.getTotalrecords();
			int current= pageNo;
			List<SendDocuments> records = pageBean.getResult();
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			User u = new User();
			for (SendDocuments person : records) {
				Map<String, Object> m = new HashMap<String, Object>();
				//u = userService.getUserById(person.getRecipient());
				//m.put("recipient", u.getFirstName());
				
                if(person.getRecipient()!=null){
					
					String [] people = person.getRecipient().split(",");
					String jc = "";
					if(people.length>1){
						for(String s : people){
							u = userService.getUser(s);
							jc= jc+u.getFirstName()+",";
						}
						jc = jc.trim();
						jc = jc.substring(0,jc.length()-1);
					}else{
						u = userService.getUser(person.getRecipient());
						//u = userService.getUserById(person.getRecipient());
						jc = u.getFirstName();
					}
					
					m.put("recipient", jc);
				}else{
					m.put("recipient", person.getCopyPeople());
				}

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
				m.put("id", person.getId());
				m.put("theme", person.getTheme());
				m.put("content", person.getContent());
				m.put("sendTime", person.getSendTime());
				m.put("sender", person.getSender().getFirstName());
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
	@RequestMapping(value = "sendDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendDetail(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			SendDocuments s = sendDocumentsService.getDetail(id);
			if(s != null){
				
				List<AppName> an = new ArrayList<AppName>();
				AppName a = new AppName();
				User u = new User();
				/*u = userService.getUserById(s.getRecipient());
				a.setCode(u.getId());
				a.setName(u.getFirstName());
				an.add(a);
				map.put("recipient", an);*/
				
                if(s.getRecipient()!=null){
					
					String [] people = s.getRecipient().split(",");
					an = new ArrayList<AppName>();
					for(String c : people){
						u = userService.getUser(c);
						a = new AppName();
						a.setCode(u.getId());
						a.setName(u.getFirstName());
						an.add(a);
					}
					map.put("recipient", an);
				}else{
					map.put("recipient", s.getRecipient());
				}


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
				map.put("id", s.getId());
				map.put("theme", s.getTheme());
				map.put("content", s.getContent());
				map.put("sendTime", s.getSendTime());
				an = new ArrayList<AppName>();
				a = new AppName();
				a.setCode(s.getSender().getId());
				a.setName(s.getSender().getFirstName());
				an.add(a);
				map.put("sender", an);
				
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
					map.put("datas", datas);
				}
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
