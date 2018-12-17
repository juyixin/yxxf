package com.eazytec.external.controller;

import java.net.InetAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eazytec.bpm.admin.Zcfg.ZcfgService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.fileManage.model.FileManage;
import com.eazytec.fileManage.service.FileManageService;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.Zcfg;
import com.eazytec.util.PageBean;

import com.eazytec.Constants;

@Controller("extzcfgController")
public class ZcfgController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ZcfgController.class);
	
	@Autowired
	private ZcfgService zcfgService;
	@Autowired
	private FileManageService fileManageService;
	
	
	//分页列表
	@RequestMapping(value = "listzcfg/list", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> listzcfg(Integer pageNo, Integer pageSize,String searchtext,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<Zcfg> noticePlatList = zcfgService.getZcfgListByserach(searchtext);
			PageBean<Zcfg> pageBean = new PageBean<Zcfg>(pageNo, pageSize);
			int totla=0;
			int current=0;
			pageBean = zcfgService.getRecords(pageBean, searchtext);
			
			List<Zcfg> records = pageBean.getResult();
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i =0; i<records.size();i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", records.get(i).getId());
				m.put("title", records.get(i).getTitle());//标题
			//	m.put("content", records.get(i).getContent());//内容
				m.put("createperson", records.get(i).getCreateperson());//创建人
				m.put("createtime", records.get(i).getCreatetimeByString());
				datas.add(m);
			}
			totla=noticePlatList.size();
			current=pageNo;
			map.put("totla", totla);
			map.put("current", current);
			map.put("datas", datas);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取政策法规列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	
	/**
	 * 根据ID获取（传统）
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "editzcfg/edit", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editzcfg(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Zcfg record = zcfgService.getZcfgById(id);
			if(record == null){
				return ExternalUtils.error(map, "目标不存在");
			}
			map.put("id", record.getId());
			map.put("title", record.getTitle());//标题
			map.put("content", null==record.getContent()?"":record.getContent());//内容
			map.put("createperson", record.getCreateperson());//创建人
			map.put("createtime", record.getCreatetimeByString());//创建时间

			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取政策法规出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	
	@RequestMapping(value = "editzcfgfile/file", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editzcfgfile(String fileid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<FileManage> fileManages=fileManageService.getEvidenceids(fileid);
			if(fileManages == null){
				return ExternalUtils.error(map, "目标不存在");
			}
			InetAddress addr = InetAddress.getLocalHost();		
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i=0;i<fileManages.size();i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				String path = fileManages.get(i).getFilePath();
				String fName ="";
				fName = path.substring(path.lastIndexOf("/")+1); 
				m.put("filePath", Constants.IP_CONFIG+":"+request.getLocalPort()+"/"+"resources/zcfg/"+fName);//下载地址
				m.put("fileName", fileManages.get(i).getFileName());//名字
				datas.add(m);
			}
			map.put("datas", datas);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取文件出错");
		}
		return ExternalUtils.success(map);
	}
	

}
