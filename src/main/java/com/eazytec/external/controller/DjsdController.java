package com.eazytec.external.controller;

import java.net.InetAddress;
import java.util.ArrayList;
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

import com.eazytec.Constants;
import com.eazytec.bpm.admin.NoticeDocument.NoticeDocumentService;
import com.eazytec.bpm.admin.Djsd.DjsdService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.model.Document;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.Djsd;
import com.eazytec.util.PageBean;

@Controller("extdjsdController")
public class DjsdController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DjsdController.class);
	
	@Autowired
	private DjsdService djsdService;
	
	@Autowired
	private NoticeDocumentService noticeDocumentService;
	
	
	//详情
	@RequestMapping(value = "djsd/edit", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editnotice(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Djsd record = djsdService.getDjsdById(id);
			if (record == null) {
				return ExternalUtils.error(map, "目标不存在");
			}
			
			map.put("id", record.getId());
			map.put("title", record.getTitle());// 标题
			map.put("content", record.getContent());// 内容
			map.put("createperson", record.getCreateperson());// 创建人
						
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取党建速递详情出错");
		}
		return ExternalUtils.success(map);
	}

	// 分页查询
	@RequestMapping(value = "listDjsd/list", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> listnotice(Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<Djsd> list = djsdService.showNoticeListBySffb();
			PageBean<Djsd> pageBean = new PageBean<Djsd>(pageNo, pageSize);

			pageBean = djsdService.getDjsd(pageBean);

			List<Djsd> records = pageBean.getResult();
			int totla=0;
			int current=0;
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (Djsd record : records) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", record.getId());
				m.put("title", record.getTitle());// 标题
			//	m.put("content", record.getContent());// 内容
				m.put("createperson", record.getCreateperson());// 创建人
				m.put("createtime", record.getCreatetimeByString());//创建时间
				datas.add(m);
			}
			
			totla=list.size();
			current=pageNo;
			map.put("totla", totla);
			map.put("current", current);
			map.put("datas", datas);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取党建速递列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	@RequestMapping(value = "editDjsd/file", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editnoticefile(String parentid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<NoticeDocument> noticeDocuments=noticeDocumentService.getDocumentsByParentids(parentid);
			if (noticeDocuments == null) {
				return ExternalUtils.error(map, "目标不存在");
			}
			InetAddress addr = InetAddress.getLocalHost();
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i=0;i<noticeDocuments.size();i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				String path = noticeDocuments.get(i).getPath();
				String fName = "";
				fName = path.substring(path.lastIndexOf("/")+1); 
				m.put("path", Constants.IP_CONFIG+":"+request.getLocalPort()+"/"+"resources/notice/"+fName);//下载地址
				m.put("name", noticeDocuments.get(i).getName());//名字
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
