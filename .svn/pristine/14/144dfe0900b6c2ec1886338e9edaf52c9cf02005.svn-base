package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class LogonControllerTest {
	
	@Test
	public void loginTest() throws Exception{
		String url = "logon";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", "jc");
		paramMap.put("password", "111111");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		
		TestUtils.post(url, params, "99237b3b");
	}
}