package com.eazytec.external.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class EvidenceControllerTest {
	
	@Test
	public void evideceTest() throws Exception {
		String url = "evdienceFile";
		File file1 = new File("D:/Q.xls");
		InputStream file = new FileInputStream(file1);
        String type = "xls";
        /*Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", "jpg");
		String params = TestUtils.buildParams(paramMap, "UTF-8");*/
		TestUtils.getFilePost(url, file, type, "697c58e2");
	}
	
	

}
