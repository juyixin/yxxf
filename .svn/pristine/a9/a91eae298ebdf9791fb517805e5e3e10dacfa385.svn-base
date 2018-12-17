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
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;
import com.eazytec.model.User;
import com.eazytec.sendDocuments.model.AppName;
import com.eazytec.service.UserService;
import com.eazytec.util.PageBean;
import com.eazytec.vacate.model.Vacate;
import com.eazytec.vacate.service.VacateService;

@Controller("extVacateController")
public class VacateController {
	
	private static final Logger log = LoggerFactory.getLogger(VacateController.class);
	
	@Autowired
	private  VacateService  vacateService;
	
	@Autowired
	private FileManageService fileManageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private DepartmentService departmentService;
	
	
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-4-17 上午8:51:35
     */
	@RequestMapping(value = "saveVacate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveVacate(Vacate send, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = ExternalUtils.getUser(request);
		try {
			FileManage fileManage = new FileManage();
			Vacate sd = new Vacate();
			List<FileManage> fileList = new ArrayList<FileManage>();
			//图片路劲
			String filePathPhotos = "";
			
			String[] fileVideos = null;
			String j = "";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			send.setCreateTime(sdf1.format(new Date()));
			send.setOriginator(user);
			send.setState("SHZT1");
			sd = vacateService.saveVacateInfo(send);
			if(sd.getId()!=null){
    				if(send.getDocument()!=null&&send.getDocument()!=""){
    					filePathPhotos = send.getDocument();		
    					fileVideos = filePathPhotos.split(";");
    					for(String s : fileVideos){
    						fileManage = new FileManage();
    						j=s.substring(s.lastIndexOf(".")+1);
    						fileManage.setFileType(j);
    						/*String fName = s.trim();  
	    				    String fileName = fName.substring(fName.lastIndexOf("/")+1); */
    						String fileN = "请假" + RandomStringUtils.randomNumeric(6);
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
    						return ExternalUtils.error(map, "保存图片失败");
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
	@RequestMapping(value = "vacateList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> vacateList(Integer pageNo, Integer pageSize,String name, String userId, String type, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		/*InetAddress addr = InetAddress.getLocalHost();
		String ip ="";
		ip=addr.getHostAddress().toString();//获得本机IP
*/		try {
	        //List<Vacate> list = VacateService.getVacate();
			PageBean<Vacate> pageBean = new PageBean<Vacate>(pageNo, pageSize);
			pageBean = vacateService.getRecords(pageBean, name,userId,type);
			//System.out.println(pageBean.getTotalrecords());
			int totla = pageBean.getTotalrecords();
			int current= pageNo;
			List<Vacate> records = pageBean.getResult();
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			User u = new User();
			for (Vacate person : records) {
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
		     	m.put("startTime", person.getStartTime());
				m.put("endTime", person.getEndTime());
				m.put("vacateDay", person.getVacateDay());
				m.put("vacateCause", person.getVacateCause());
				DataDictionary dcl = new DataDictionary();
				dcl = vacateService.getNameList(person.getVacateType());
		     	m.put("vacateType", dcl.getName());
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
	@RequestMapping(value = "vacateDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> vacateDetail(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Vacate s = vacateService.getDetail(id);
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
		     	map.put("startTime", s.getStartTime());
				map.put("endTime", s.getEndTime());
				map.put("vacateDay", s.getVacateDay());
				map.put("vacateCause", s.getVacateCause());
				DataDictionary dcl = new DataDictionary();
				dcl = vacateService.getNameList(s.getVacateType());
		     	map.put("vacateType", dcl.getName());
				map.put("opinion", s.getOpinion());
				
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
	
	
	/**
	 * Description:初始化字典数据
	 * 作者 : 蒋晨   
	 * 时间 : 2017-3-17  下午8:40:10
	 */
	@RequestMapping(value = "dicList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> dicList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DataDictionary> dclAll= new ArrayList<DataDictionary>();
			dclAll = dataDictionaryService.getWgtlAll();
			for(DataDictionary d : dclAll){
				List<DataDictionary> dclList = dataDictionaryService.getWgtlList(d.getCode());
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				for (DataDictionary record : dclList) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("code", record.getCode());
					m.put("name", record.getName());
					datas.add(m);
			   }
				map.put(d.getCode(), datas);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取数据字典列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	
	/**
	 * Description:组织架构树
	 * 作者 : 蒋晨   
	 * 时间 : 2017-4-19  上午8:40:10
	 */
	@RequestMapping(value = "orgTreeList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> orgTreeList(String code,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Department> departmentList = vacateService.getDepartment(code);
					List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for(Department d : departmentList){
				/*	List<User> userList= new ArrayList<User>();
				userList = vacateService.getUser(d.getId());
				for(User user : userList){*/
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("code", d.getId());
					m.put("name", d.getViewName());
					datas.add(m);
				//}
				//map.put(d.getViewName(), datas);
			}
			map.put("datas", datas);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取部门架构树列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	 * Description:组织架构树
	 * 作者 : 蒋晨   
	 * 时间 : 2017-4-19  上午8:40:10
	 */
	@RequestMapping(value = "perTreeList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> perTreeList(String code,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				List<User> userList= new ArrayList<User>();
				userList = vacateService.getUser(code);
				//Department d = departmentService.getDepartmentById(code);
				for(User user : userList){
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("code", user.getId());
					m.put("name", user.getFirstName());
					datas.add(m);
				}
				map.put("datas", datas);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取人员列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	 * Description:审批
	 * 作者 : 蒋晨   
	 * 时间 : 2017-4-19  下午13:22:10
	 */
	@RequestMapping(value = "approveVacate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> approveVacate(String id , String opinion,String state ,String type,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String meaasge = vacateService.approverVacate(id, opinion, state,type);
			if(meaasge.equals("true")){
				map.put("id", id);
			}else{
				return ExternalUtils.error(map, "审批失败");
				
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "审批出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	 * Description:请假统计
	 * 作者 : 蒋晨   
	 * 时间 : 2017-4-19  下午13:22:10
	 */
	@RequestMapping(value = "calculateVacate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> calculateVacate(Integer pageNo, Integer pageSize,String startTime,String endTime,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PageBean<Vacate> pageBean = new PageBean<Vacate>(pageNo, pageSize);
			pageBean = vacateService.getRecord(pageBean, startTime,endTime);
			
			int totla = pageBean.getTotalrecords();
			int current= pageNo;
	
			List<Vacate> records = pageBean.getResult();

			//int count = 0;
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			if(!records.isEmpty()&&records.size()!=0){
				for(Vacate d : records){
					
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("code", d.getRecipientName());
					m.put("name", d.getName());
					m.put("count", d.getCount());
					m.put("total", d.getTotal());
					datas.add(m);
				}
			}
			map.put("totla", totla);
			map.put("current", current);
			map.put("datas", datas);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "统计出错");
		}
		return ExternalUtils.success(map);
		
	}
	
	
	/**
	 * Description:请假统计里查看某人请假详情
	 * 作者 : 蒋晨   
	 * 时间 : 2017-4-19  下午13:22:10
	 */
	@RequestMapping(value = "checkVacate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkVacate(String id ,String startTime,String endTime,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			    List<Vacate> vacateist = vacateService.getVacateList(id,startTime,endTime);
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				if(vacateist.size()!=0&&!vacateist.isEmpty()){
				for(Vacate v : vacateist){
					Map<String, Object> m = new HashMap<String, Object>();
					DataDictionary dcl = new DataDictionary();
					dcl = vacateService.getNameList(v.getVacateType());
			     	m.put("vacateType", dcl.getName());
					m.put("startTime", v.getStartTime());
					m.put("endTime", v.getEndTime());
					m.put("vacateDay", v.getVacateDay());
					datas.add(m);
				}
			}
			map.put("datas", datas);
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取列表失败");
		}
		return ExternalUtils.success(map);
	}

	
	/**
	 * Description:请假统计里查看某人请假详情
	 * 作者 : 蒋晨   
	 * 时间 : 2017-4-19  下午13:22:10
	 */
	@RequestMapping(value = "checkingV", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkingV(String id ,String startTime,String endTime,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			    List<Vacate> vacateist = vacateService.getVacateList1(id,startTime,endTime);
				
			      System.out.println(vacateist);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取列表失败");
		}
		return ExternalUtils.success(map);
	}

	
}
