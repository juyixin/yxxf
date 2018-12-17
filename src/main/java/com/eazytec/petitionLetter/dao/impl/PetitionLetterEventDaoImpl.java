package com.eazytec.petitionLetter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.petitionLetter.dao.PetitionLetterEventDao;
import com.eazytec.petitionLetter.model.PetitionLetterEvent;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;

@Repository("petitionLetterEventDao")
public class PetitionLetterEventDaoImpl extends GenericDaoHibernate<PetitionLetterEvent,String> implements PetitionLetterEventDao{

	public PetitionLetterEventDaoImpl(){
		super(PetitionLetterEvent.class);
	}
    
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午10:51:51
     */
	@Override
	public List<PetitionLetterEvent> getEvent(String personId) {
		String sql = "select * from etec_yxxf_xfsjxxb where xfr_id ='"+personId+"' order by create_time desc";
		List<PetitionLetterEvent> listEvent = getSession().createSQLQuery(sql).addEntity(PetitionLetterEvent.class).list();
		return listEvent;
	}
    
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午10:51:51
     */
	@Override
	public List<PetitionLetterEvent> getEvent1(String personId) {
		String sql = "select ID as id,SJMC as eventName ,SJNR as eventDetail from etec_yxxf_xfsjxxb where xfr_id ='"+personId+"' order by create_time desc";
		List<PetitionLetterEvent> lit = new ArrayList();
		
		lit = getSession().createSQLQuery(sql)
				.addScalar("id", StandardBasicTypes.STRING)
				.addScalar("eventName", StandardBasicTypes.STRING)
				.addScalar("eventDetail", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(PetitionLetterEvent.class)).list();
		return lit;
	}
    
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午2:22:22
     */
	@Override
	public void saveEvent(PetitionLetterEvent event) {
		getSession().save(event);
		getSession().flush();
	}
	
	/**
     * Description:更新数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26 下午7:29:29
     */
	@Override
	public void updateEvent(PetitionLetterEvent event) {
		getSession().update(event);
		getSession().flush();
	}
    
	/**
     * Description:查看数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 下午2:22:22
     */
	@Override
	public PetitionLetterEvent getDetail(String id) {
		String sql = "select * from etec_yxxf_xfsjxxb where id ='"+id+"'"; 
		PetitionLetterEvent event = (PetitionLetterEvent)getSession().createSQLQuery(sql).addEntity(PetitionLetterEvent.class).setMaxResults(1).uniqueResult();
		return event;
	}

	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26 下午7:29:29
     */
	@Override
	public void deleteEvent(String id) {
		String sql = "delete from etec_yxxf_xfsjxxb where id= ?";   
	    Query query = getSession().createSQLQuery(sql);   
	    query.setParameter(0, id); 
	    query.executeUpdate();
	}

	/**
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-26  下午8:35:51
     */
	@Override
	public List<PetitionLetterEvent> searchEvent(String name) {
		String sql = "select * from etec_yxxf_xfsjxxb where SJMC like '%"+ name +"%' order by create_Time desc";
		List<PetitionLetterEvent> eventList = getSession().createSQLQuery(sql).addEntity(PetitionLetterEvent.class).list();
		return eventList;
	}  

}
