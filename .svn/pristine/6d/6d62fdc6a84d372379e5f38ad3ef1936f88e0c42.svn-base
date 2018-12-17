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
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.alxxgl.service.AllxService;
import com.eazytec.alxxgl.service.AlxxbDocumentService;
import com.eazytec.alxxgl.service.AlxxbService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.util.PageBean;

@Controller("extallxController")
public class AllxController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(AllxController.class);
	@Autowired
	private AllxService allxService;
	@Autowired
	private AlxxbService alxxbService;
	@Autowired
	private AlxxbDocumentService alxxbDocumentService;

	//案例分类查询接口：根据此接口能查询出所有的案例分类
	@RequestMapping(value = "listAxxl/list", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> listAllx(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<Allx> record = allxService.getAllxBydepartment(id);
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < record.size(); i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", record.get(i).getId());
				m.put("lxmc", record.get(i).getLxmc());// 标题
				m.put("createdTime", record.get(i).getCreatedTime());//创建时间
				m.put("lxdm", record.get(i).getLxdm());// 内容
				datas.add(m);
				
			}
			map.put("datas", datas);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取案例出错");
		}
		return ExternalUtils.success(map);
	}

	/**
	 * 根据ID获取（传统）
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	
	//d.查询案例详情接口：根据案例ID查询案例详情。
	@RequestMapping(value = "editalxxb/edit", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editalxxb(String id, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Alxxb record = alxxbService.getAllxByIds(id);
			if (record == null) {
				return ExternalUtils.error(map, "目标不存在");
			}
			if(record.getContent()==null){
				record.setContent("");
			}
				map.put("id", record.getId());
				map.put("allx", record.getAllx());// 案例类型
				map.put("content", record.getContent()); //案例详情
				map.put("name", record.getName());// 案例名称
				map.put("dsr", record.getDsr());// 当事人
				map.put("createdTime", record.getCreatedTimeByString());//创建时间
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取案例出错");
		}
		return ExternalUtils.success(map);
	}

	
	
	//分页查询案例接口：根据此接口能查询出所有的案例分类
	@RequestMapping(value = "listAlxxb/list", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> listAxxl(Integer pageNo, Integer pageSize, String name, String allx,
			String dsr, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<Alxxb>  list=alxxbService.getlistsize(name, allx, dsr);
			PageBean<Alxxb> pageBean = new PageBean<Alxxb>(pageNo, pageSize);

			pageBean = alxxbService.getRecords(pageBean, name, allx, dsr);

			List<Alxxb> records = pageBean.getResult();
			int totla=0;
			int current=0;
			// 此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < records.size(); i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", records.get(i).getId());
				m.put("allx", records.get(i).getAllx());// 案例类型
				m.put("name", records.get(i).getName());// 案例名称
				m.put("dsr", records.get(i).getDsr());// 当事人
				m.put("createdTime", records.get(i).getCreatedTimeByString());//创建时间
				datas.add(m);
			}
			totla=list.size();
			current=pageNo;
			map.put("totla", totla);
			map.put("current", current);
			map.put("datas", datas);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取案例列表出错");
		}
		return ExternalUtils.success(map);
	}

	
	@RequestMapping(value = "editallxdocumentfile/file", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editnoticefile(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<AlxxbDocument> record = alxxbDocumentService.getDocumentsByIds(id);
			if (record == null) {
				return ExternalUtils.error(map, "目标不存在");
			}
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i=0;i<record.size();i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				String path = record.get(i).getPath();
				String fName = "";
				fName = path.substring(path.lastIndexOf("/")+1); 
				m.put("path", Constants.IP_CONFIG+":"+request.getLocalPort()+"/"+"resources/allxb/"+fName);//下载地址
				m.put("name", record.get(i).getName());//名字
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
