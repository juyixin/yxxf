package com.eazytec.evidence.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.evidence.model.Evidence;

public interface EvidenceDao extends GenericDao<Evidence,String>{
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	public String saveEvidenceInfo(Evidence evidence);
	
	
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-15 上午8:45:23
	 */
	public List<Evidence> getEvidence();
	
	
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-15 上午8:45:23
	 */
	public List<Evidence> getEvidence1(String name);
	
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-15 上午8:45:23
	 */
	public Evidence getDetail(String id);
	
	public void updateEvidence(Evidence evidence);
  
	public void deleteEvidence(String id);

}
