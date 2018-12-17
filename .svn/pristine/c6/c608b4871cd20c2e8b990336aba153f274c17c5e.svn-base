package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class GeneralProcessControllerTest {
	
	
	@Test
	public void evideceTest() throws Exception {
		String url = "saveGeneralProcess";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("itemName", "流程三");
		paramMap.put("itemDescription", "流程SNAAAA从方法反反复复·流程一流程一流程一流程一流程一流程一");
		paramMap.put("remark", "");
		paramMap.put("approver", "jc");
		//paramMap.put("opinion", "jc,admin");
		paramMap.put("copyPeople", "jc,admin");
		paramMap.put("document", "/resources/fileManage/B@535da356.xls");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTes3() throws Exception{
		String url = "generalProcessList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "2");
		paramMap.put("userId", "jc");
		paramMap.put("type", "1");
	    //paramMap.put("state", "SHZT2");
		paramMap.put("name", "流程一");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTest1() throws Exception{
		String url = "generalProcessDetail";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402837585b852b3c015b853798460001");
	    //paramMap.put("name", "文件四");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}

}
