package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class VacateControllerTest {
	
	@Test
	public void listTest1() throws Exception{
		String url = "vacateDetail";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402837585b84346c015b8438a2cb0000");
	    //paramMap.put("name", "文件四");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTest() throws Exception{
		String url = "dicList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	@Test
	public void listTest2() throws Exception{
		String url = "orgTreeList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void evideceTest() throws Exception {
		String url = "saveVacate";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("vacateType", "QJLX1");
		paramMap.put("startTime", "2017-03-30 9:06:15");
		paramMap.put("endTime", "2017-04-15 13:06:43");
		paramMap.put("vacateDay", "1");
		paramMap.put("vacateCause", "生病了");
		paramMap.put("approver", "jc");
		//paramMap.put("opinion", "jc,admin");
		//paramMap.put("copyPeople", "jc,admin");
		paramMap.put("document", "");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	@Test
	public void listTes3() throws Exception{
		String url = "vacateList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "2");
		paramMap.put("userId", "jc");
		paramMap.put("type", "1");
	    //paramMap.put("name", "文件四");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTest7() throws Exception{
		String url = "approveVacate";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402837585b843b03015b843eb9ae0000");
		paramMap.put("opinion", "");
	    paramMap.put("state", "SHZT2");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTest9() throws Exception{
		String url = "calculateVacate";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "2");
		paramMap.put("startTime", "2017-03-01");
		paramMap.put("endTime", "2017-04-20");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTest12() throws Exception{
		String url = "checkVacate";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "jc");
		paramMap.put("startTime", "2017-03-01");
		paramMap.put("endTime", "2017-04-20");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}

}
