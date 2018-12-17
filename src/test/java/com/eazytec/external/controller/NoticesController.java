package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class NoticesController {
	
	@Test
	public void saveTest() throws Exception{
		String url = "editnoticefile/file";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "4028365d5a82194c015a82a767970003");
		paramMap.put("parentid", "4028365d5a82194c015a82a7678b0002   ");
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "697c58e2");
	}

}
