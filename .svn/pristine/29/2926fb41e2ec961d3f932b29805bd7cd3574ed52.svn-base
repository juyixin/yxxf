package com.eazytec.bpm.admin.sysConfig.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.sysConfig.dao.SysConfigDao;
import com.eazytec.bpm.admin.sysConfig.service.SysConfigManager;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.SysConfig;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("sysConfigManager")
public class SysConfigManagerImpl extends GenericManagerImpl<SysConfig, Long>
		implements SysConfigManager {

	private SysConfigDao sysConfigDao;

	public SysConfigDao getSysConfigDao() {
		return sysConfigDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeSysConfig(String id) {
		log.debug("removing group: " + id);
		sysConfigDao.removeSysConfig(id);
	}
	
	
	
	
	@Override
	public SysConfig saveSysConfig(SysConfig sysConfig) {
		// TODO Auto-generated method stub
		return sysConfigDao.saveSysConfig(sysConfig);
	}
	
	public SysConfig getSysConfigByKey(String key) throws Exception {
		return  sysConfigDao.getSysConfigByKey(key);
	}
	
	@Override
	@Autowired
	public void setSysConfigDao(SysConfigDao sysConfigdao) {
		this.dao = sysConfigdao;
		this.sysConfigDao = sysConfigdao;
	}

	/*
	 * @Override public SysConfig getSysConfig(String sysConfigId) { // TODO
	 * Auto-generated method stub return null; }
	 */

	/**
	 * {@inheritDoc}
	 */
	public SysConfig getId(String id) {
		return sysConfigDao.getId(id);
	}

	
	/**
	 * {@inheritDoc checkListViewName}
	 */
	public boolean checkSysConfigKey(String checkname)throws EazyBpmException{
		return sysConfigDao.checkSysConfigKey(checkname);
		
	}
	

	
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<SysConfig> getAllSysConfig() {
//		log.info("getting events");
		try {
			return sysConfigDao.getAllSysConfig();
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException(
					"problem in getting events" + e.getMessage(), e);
		}
	}
	
	
	
	
	 /**
     * {@inheritDoc}
     */
	public void deleteSelectedSysConfig(List<String> ids) throws Exception{
    	try{
    		log.info("List of ids==="+ids);
    		List<SysConfig> sysConfigs = sysConfigDao.getSysConfigById(ids);
    		for(SysConfig sysConfig:sysConfigs){
    			removeSysConfig(sysConfig.getId());
    		}
	    }catch (Exception e) {
    		throw new BpmException("error.sysconfig.delete"+e.getMessage(),e);
    	}
    }
    

}