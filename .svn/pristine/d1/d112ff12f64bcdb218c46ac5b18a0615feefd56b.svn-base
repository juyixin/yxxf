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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.external.util.ExternalUtils;


@Controller("extLeaveController")
public class LeaveController {
	private static final Logger log = LoggerFactory.getLogger(LeaveController.class);

	@Autowired
	private JdbcTemplate jt;

	@RequestMapping(value = "leaves", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String sql = "select * from leaves order by createdtime asc";
			List<Map<String, Object>> list = jt.queryForList(sql);
			
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> leaveMap : list) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", leaveMap.get("id"));
				m.put("reason", leaveMap.get("reason"));
				m.put("createTime", leaveMap.get("CREATEDTIME").toString());
				datas.add(m);
			}
			map.put("datas", datas);
			
			return ExternalUtils.success(map);
		} catch (Exception e) {
			log.error("获取Leave列表出错。", e);
			return ExternalUtils.error(map, "获取Leave列表出错。");
		}
	}
}
