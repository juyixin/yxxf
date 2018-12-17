package com.eazytec.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.crm.dao.CustomDao;
import com.eazytec.crm.model.Custom;
import com.eazytec.crm.service.CustomService;
import com.eazytec.util.PageBean;


@Service("customService")
public class CustomServiceImpl implements CustomService{
	@Autowired
	private CustomDao customDao;
	
	public List<Custom> getCustoms(String num,String name){
		return customDao.getAllCustomList(num,name);
	}
	
	public PageBean<Custom> getCustoms(PageBean<Custom> pageBean){
		int totalcustoms =  customDao.getAllCustomCount(pageBean);
		pageBean.setTotalrecords(totalcustoms);
		List<Custom> result =  customDao.getAllCustom(pageBean);
		pageBean.setResult(result);
		return pageBean;
	}
	
	public Custom saveCustom(Custom custom){
		return customDao.saveCustom(custom);
	}
	public String removeCustoms(List<String> customIds){
		List<Custom> customList=customDao.getCustomsByIds(customIds);
		
		for (Custom custom : customList) {
			customDao.deleteCustom(custom);
		}
		return null;
	}
	public Custom getCustomById(String id){
		return customDao.get(id);
	}

}
