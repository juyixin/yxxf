package com.eazytec.evidence.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.evidence.dao.EvidenceDao;
import com.eazytec.evidence.model.Evidence;
import com.eazytec.link.model.Link;


@Repository("evidenceDao")
public class EvidenceDaoImpl extends GenericDaoHibernate<Evidence,String> implements EvidenceDao{
	
	public EvidenceDaoImpl(){
		super(Evidence.class);
	}
    
	
	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	@Override
	public String saveEvidenceInfo(Evidence evidence) {
		getSession().save(evidence);
		String id = evidence.getId();
		getSession().flush();
		return id;
	}

    
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-15 上午8:45:23
	 */
	@Override
	public List<Evidence> getEvidence() {
		String sql = "select * from etec_evidence";
		List<Evidence> evidenceList = getSession().createSQLQuery(sql).addEntity(Evidence.class).list();
		return evidenceList;
	}
	
	
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-15 上午8:45:23
	 */
	@Override
	public List<Evidence> getEvidence1(String name) {
		String sql = "select * from etec_evidence where event_Name like '%"+ name +"%'"+"or concern_Name like '%"+ name +"%'"+"or handler_Name like '%"+ name +"%'";
		List<Evidence> evidenceList = getSession().createSQLQuery(sql).addEntity(Evidence.class).list();
		return evidenceList;
	}

    
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-15 上午8:45:23
	 */
	@Override
	public Evidence getDetail(String id) {
		String sql = "select * from etec_evidence where id ='"+id+"'";
		Evidence evidence = (Evidence) getSession().createSQLQuery(sql).addEntity(Evidence.class).setMaxResults(1).uniqueResult();
		return evidence;
	}


	@Override
	public void updateEvidence(Evidence evidence) {
		getSession().update(evidence);
		getSession().flush();
	}


	@Override
	public void deleteEvidence(String id) {
		Query query = getSession().createQuery("delete from FileManage as fileManage where fileManage.id = '"+id+"'");
        query.executeUpdate();
		
	}

}
