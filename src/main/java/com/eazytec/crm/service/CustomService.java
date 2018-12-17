package com.eazytec.crm.service;

import java.util.List;

import com.eazytec.crm.model.Custom;
import com.eazytec.util.PageBean;


public interface CustomService {
	List<Custom> getCustoms(String num,String name);
	PageBean<Custom> getCustoms(PageBean<Custom> pageBean);

	Custom saveCustom(Custom custom);

	String removeCustoms(List<String> customIdList);

	Custom getCustomById(String id);

	}
