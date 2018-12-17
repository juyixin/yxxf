package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class LinkController {
	
	@Test
	public void loginTest() throws Exception{
		String url = "linkList";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		
		TestUtils.post(url, params, "697c58e2");
	}

}
