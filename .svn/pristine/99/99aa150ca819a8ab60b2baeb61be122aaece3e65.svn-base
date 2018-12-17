package com.eazytec.external.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class sendDocumentsControllerTest {
	
	@Test
	public void evideceTest() throws Exception {
		String url = "saveSendDocuments";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("theme", "文件五");
		paramMap.put("content", "文件五");
		paramMap.put("recipient", "jc");
		paramMap.put("copyPeople", "jc,admin");
		paramMap.put("photos", "/resources/fileManage/B@41ce8806.jpg;/resources/fileManage/B@3f4be0e8.png");
		paramMap.put("voices", "");
		paramMap.put("videos", "/resources/fileManage/B@489e2b3b.mp4");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	
	@Test
	public void listTest() throws Exception{
		String url = "sendList";
		
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
	public void listTest1() throws Exception{
		String url = "sendDetail";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402837585b7aaf0d015b7abc28d60000");
	//paramMap.put("name", "文件四");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
	
	

}
