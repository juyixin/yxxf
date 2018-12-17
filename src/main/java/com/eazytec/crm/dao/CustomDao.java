package com.eazytec.crm.dao;

import java.util.List;

import com.eazytec.crm.model.Custom;
import com.eazytec.dao.GenericDao;
import com.eazytec.util.PageBean;

public interface CustomDao extends GenericDao<Custom, String> {

	List<Custom> getAllCustomList(String num,String name);

	Custom saveCustom(Custom custom);

	List<Custom> getCustomsByIds(List<String> customIds);
	void deleteCustom(Custom custom);
	
	int getAllCustomCount(PageBean<Custom> pageBean);

	List<Custom> getAllCustom(PageBean<Custom> pageBean);

}
