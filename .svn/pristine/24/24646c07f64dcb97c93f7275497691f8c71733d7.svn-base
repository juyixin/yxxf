package com.eazytec.bpm.admin.sysConfig.service;

import java.util.List;

import com.eazytec.bpm.admin.sysConfig.dao.SysConfigDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.SysConfig;
import com.eazytec.service.GenericManager;

public interface SysConfigManager extends GenericManager<SysConfig, Long> {

	SysConfig saveSysConfig(SysConfig sysConfig);

	void setSysConfigDao(SysConfigDao sysConfigdao);

	SysConfig getId(String sysConfigId);

	public List<SysConfig> getAllSysConfig();

	boolean checkSysConfigKey(String checkname)throws EazyBpmException;
	
	SysConfig getSysConfigByKey(String key) throws Exception;
	
	public void deleteSelectedSysConfig(List<String> ids) throws Exception;
}