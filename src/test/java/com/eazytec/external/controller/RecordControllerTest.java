package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class RecordControllerTest {
	
	
	//================================================================================================================================================================
	@Test
	public void saveTest() throws Exception{
		String url = "record";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("num", "123");
		paramMap.put("note", "备注");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "697c58e2");
	}
	//================================================================================================================================================================
	
	
	
	
	//================================================================================================================================================================
	@Test
	public void editTest() throws Exception{
		String url = "record/edit";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402881e65a512cd6015a512dc6350000");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.get(url, params, "efd9d1b8");
	}
	
	@Test
	public void editTest2() throws Exception{
		String url = "record/402881e65a512cd6015a512dc6350000";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.get(url, params, "efd9d1b8");
	}
	
	//================================================================================================================================================================
	
	
	
	
	
	
	//================================================================================================================================================================
	
	@Test
	public void listTest() throws Exception{
		String url = "records";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "15");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.get(url, params, "697c58e2");
	}
	
	@Test
	public void listTest2() throws Exception{
		String url = "records/1/15";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.get(url, params, "697c58e2");
	}
	//================================================================================================================================================================
	
	
	
	
	
	//================================================================================================================================================================
	@Test
	public void updateTest() throws Exception{
		String url = "record/update";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402881e65a53ec93015a566295380000");
		paramMap.put("num", "123411");
		paramMap.put("note", "备注4");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "efd9d1b8");
	}
	
	
	@Test
	public void updateTest2() throws Exception{
		String url = "record/402881e65a53ec93015a566295380000";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("num", "12341");
		paramMap.put("note", "备注4");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.put(url, params, "efd9d1b8");
	}
	//================================================================================================================================================================
	
	
	
	
	
	
	//================================================================================================================================================================
	@Test
	public void deleteTest() throws Exception{
		String url = "record/delete";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402881e65a51427a015a5142ee9c0000");
		paramMap.put("num", "12341");
		paramMap.put("note", "备注4");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "efd9d1b8");
	}
	
	
	@Test
	public void deleteTest2() throws Exception{
		String url = "record/402881e65a51427a015a5142ee9c0000";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("num", "12341");
		paramMap.put("note", "备注4");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.delete(url, params, "efd9d1b8");
	}
	//================================================================================================================================================================
}