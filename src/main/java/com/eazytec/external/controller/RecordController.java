package com.eazytec.external.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.crm.model.Record;
import com.eazytec.crm.service.RecordService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;

@Controller("extRecordController")
public class RecordController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(RecordController.class);

	@Autowired
	private RecordService recordService;

	/**
	 * 保存
	 * 
	 * @param record
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> save(Record record, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = ExternalUtils.getUser(request);
			record.setCreatedBy(user);
			record.init();

			recordService.saveRecord(record);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "保存Record出错");
		}
		return ExternalUtils.success(map);
	}
	
	//=============================================================================================================================================================
	
	/**
	 * 根据ID获取（传统）
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record/edit", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> edit(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Record record = recordService.getRecordById(id);
			if(record == null){
				return ExternalUtils.error(map, "ID不存在");
			}
			
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			map.put("id", record.getId());
			map.put("num", record.getNum());
			map.put("note", record.getNote());
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取Record出错");
		}
		return ExternalUtils.success(map);
	}
	
	/**
	 * 根据ID获取（restful风格）
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record/{id}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> edit2(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Record record = recordService.getRecordById(id);
			if(record == null){
				return ExternalUtils.error(map, "ID不存在");
			}
			
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			map.put("id", record.getId());
			map.put("num", record.getNum());
			map.put("note", record.getNote());
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取Record出错");
		}
		return ExternalUtils.success(map);
	}
	//=============================================================================================================================================================
	
	
	
	
	
	
	
	//=============================================================================================================================================================
	
	/**
	 * 分页获取列表（传统）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "records", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list(Integer pageNo, Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			PageBean<Record> pageBean = new PageBean<Record>(pageNo, pageSize);
			
			pageBean = recordService.getRecords(pageBean, null, null, null);
			
			List<Record> records = pageBean.getResult();
			
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (Record record : records) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", record.getId());
				m.put("num", record.getNum());
				m.put("note", record.getNote());
				datas.add(m);
			}
			map.put("datas", datas);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取Record列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	 * 分页获取列表（restful风格）
	 * 推荐使用“传统方式”，方便移动端进行url构造
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "records/{pageNo}/{pageSize}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list2(@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize")Integer pageSize, 
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			PageBean<Record> pageBean = new PageBean<Record>(pageNo, pageSize);
			
			pageBean = recordService.getRecords(pageBean, null, null, null);
			
			List<Record> records = pageBean.getResult();
			
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (Record record : records) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", record.getId());
				m.put("num", record.getNum());
				m.put("note", record.getNote());
				datas.add(m);
			}
			map.put("datas", datas);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取Record列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	//=============================================================================================================================================================
	
	
	
	
	
	
	//=============================================================================================================================================================
	
	/**
	 * 根据ID更新（传统）
	 * 
	 * @param record
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record/update", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> update(Record record, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Record r = recordService.getRecordById(record.getId());
			if(r == null){
				return ExternalUtils.error(map, "ID不存在");
			}
			
			recordService.updateRecord(record);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "更新Record出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	 * 根据ID更新（restful风格）
	 * 
	 * @param record
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record/{id}", method = RequestMethod.PUT)
	public @ResponseBody Map<String, Object> update2(Record record, @PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Record r = recordService.getRecordById(id);
			if(r == null){
				return ExternalUtils.error(map, "ID不存在");
			}
			
			record.setId(id);
			recordService.updateRecord(record);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "更新Record出错");
		}
		return ExternalUtils.success(map);
	}

	//=============================================================================================================================================================
	
	
	
	
	
	
	
	
	
	//=============================================================================================================================================================
	/**
	 * 根据ID删除（传统）
	 * 
	 * @param id
	 * @param num
	 * @param note
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> delete(String id, String num, String note, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> ids = new ArrayList<String>();
			ids.add(id);
			recordService.removeRecords(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "删除Record出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	 * 根据ID删除（restful风格）
	 * 
	 * @param id
	 * @param num
	 * @param note
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "record/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> delete2(@PathVariable("id") String id, String num, String note, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> ids = new ArrayList<String>();
			ids.add(id);
			recordService.removeRecords(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "删除Record出错");
		}
		return ExternalUtils.success(map);
	}
	//=============================================================================================================================================================
}
