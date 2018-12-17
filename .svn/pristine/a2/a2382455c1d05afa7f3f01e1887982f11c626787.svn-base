package com.eazytec.bpm.admin.sysConfig.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.SysConfig;

public interface SysConfigDao extends GenericDao<SysConfig, Long> {

	SysConfig saveSysConfig(SysConfig sysConfig);

	SysConfig getId(String id);

	List<SysConfig> getSysConfigById(List<String> id);

	void removeSysConfig(String name);

	boolean checkSysConfigKey(String checkName)throws EazyBpmException;
	
	SysConfig getSysConfigByKey(String key) throws Exception;
	
	public List<SysConfig> getAllSysConfig();

}