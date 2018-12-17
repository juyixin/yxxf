package com.eazytec.evidence.service.impl;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.evidence.dao.EvidenceDao;
import com.eazytec.evidence.model.Evidence;
import com.eazytec.evidence.service.EvidenceService;
import com.eazytec.link.dao.LinkDao;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("evidenceService")
@WebService(serviceName = "EvidenceService", endpointInterface = "com.eazytec.evidence.service")
public class EvidenceServiceImpl extends GenericManagerImpl<Evidence, String> implements EvidenceService{
   
	private  EvidenceDao  evidenceDao;
    
    @Autowired
   	public EvidenceServiceImpl(EvidenceDao  evidenceDao) {
   		super(evidenceDao);
   		this.evidenceDao = evidenceDao;
   	}
   	
   	@Autowired
   	public void setEvidenceDao(EvidenceDao evidenceDao) {
   		this.evidenceDao = evidenceDao;
   	}
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-15 上午8:38:35
     */
	@Override
	public List<Evidence> getEvidence() {
		return evidenceDao.getEvidence();
	}
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-15 上午8:38:35
     */
	@Override
	public List<Evidence> getEvidence1(String name) {
		return evidenceDao.getEvidence1(name);
	}
    
	
	/**
     * Description:查看详情
     * 作者 : 蒋晨 
     * 时间 : 2017-2-15 上午10:50:50
     */
	@Override
	public Evidence getDetail(String id) {
		return evidenceDao.getDetail(id);
	}
	
	/**
     * Description:保存证据详情
     * 作者 : 蒋晨 
     * 时间 : 2017-2-15 上午10:50:50
     */
	@Override
	public String saveEvidence(Evidence evidence) {
		return evidenceDao.saveEvidenceInfo(evidence);
	}

	
	@Override
	public void updateEvidence(Evidence evidence) {
		evidenceDao.updateEvidence(evidence);
	}
	
	@Override
	public void deleteEvidence(String id) {
		evidenceDao.deleteEvidence(id);
	}

}
