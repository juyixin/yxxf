package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class TxlryControllerTest {
	@Test
	public void txlrylist() throws Exception{
		String url = "txlrys";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "4028366f5a3f681f015a3f69c56f0000");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "612e89dd");
	}
}
