package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;



public class LetterPersonControllerTest {
	
	
	@Test
	public void listTest() throws Exception{
		String url = "letterPerson";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "2");
		paramMap.put("name", "张三");
		paramMap.put("sfzCode", "");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.get(url, params, "697c58e2");
	}
	
	
	@Test
	public void eventTest() throws Exception{
		String url = "eventList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("personId", "4028365d5a731998015a731ec6590000");

		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.get(url, params, "697c58e2");
	}
	
	

}
