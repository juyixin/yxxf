package com.eazytec.external.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class EvidenceDetailControllerTest {
	
	@Test
	public void saveTest() throws Exception{
		String url = "evidence";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", "bb");
		paramMap.put("partyname", "ee");
		paramMap.put("partycert", "当事人身份证号");
		paramMap.put("partyphone", "联系电话");
		paramMap.put("partySex", "性别");
		paramMap.put("photos", "/resources/fileManage/B@6532293c.jpg;/resources/fileManage/B@4f189b47.mp4");
		paramMap.put("voices", "");
		paramMap.put("videos", "");
		paramMap.put("content", "床前明年电视剧发掘发掘舒服撒女爱家上班附加费建设单位我我外就好多钱小妹妹真不错阿尔法那那人叫你发了沙发沙发沙发GVG");
		paramMap.put("time", "2017-02-23 9:06:15");
		paramMap.put("handlerName", "小强");
		paramMap.put("handlerId", "xiaoqiang");
		paramMap.put("handlerPhone", "13961561382");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "697c58e2");
	}
	
	
	@Test
	public void txlrylist() throws Exception{
		String url = "txlrys";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "402837585b942df2015b943d50430009");
		String params = TestUtils.buildParams(paramMap, "UTF-8");
		TestUtils.post(url, params, "697c58e2");
	}
	
	

}
