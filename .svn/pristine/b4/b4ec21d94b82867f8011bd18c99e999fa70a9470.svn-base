package com.eazytec.bpm.opinion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.opinion.dao.UserOpinionDao;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Opinion;
import com.eazytec.model.UserOpinion;


@Repository("userOpinionDao")
public class UserOpinionDaoImpl extends GenericDaoHibernate<UserOpinion, String> implements UserOpinionDao{
	
	public UserOpinionDaoImpl() {
		super(UserOpinion.class);
	}
	
	@Override
	public UserOpinion getUserOpinionById(String id) {
    	UserOpinion userOpinion = (UserOpinion) getSession().createQuery("from UserOpinion as userOpinion where userOpinion.id ='"+id+"'").list().get(0);
        return userOpinion;
        }
	
	@Override
	public UserOpinion saveUserOpinion(UserOpinion userOpinion){
		getSession().saveOrUpdate(userOpinion);
		getSession().flush();
		return userOpinion;
	}
	
	@Override
	public boolean deleteUserOpinion(String userOpinionId){
		Query qury = getSession().createQuery("delete from UserOpinion as userOpinion where userOpinion.id = '"+userOpinionId+"'");
		qury.executeUpdate();
		getSession().flush();
		return true;
	}

	@SuppressWarnings("unchecked")
  	public List<UserOpinion> getUserOpinion(String opinionWord,String userId) throws EazyBpmException{
    	List<UserOpinion> userOpinionList = (List<UserOpinion>) getSession().createQuery("from UserOpinion  AS userOpinion WHERE userOpinion.userId ='"+userId+"' AND userOpinion.word LIKE '"+opinionWord+"%'").list();
    	return userOpinionList;
    }
	
	@SuppressWarnings("unchecked")
  	public List<Opinion> getOpinion(String processInsId) throws EazyBpmException{
    	List<Opinion> opinionList = (List<Opinion>) getSession().createQuery("from Opinion  AS opinion WHERE opinion.processInstanceId ='"+processInsId+"' order by opinion.submittedOn desc").list();
    	return opinionList;
    }
	
	public Opinion saveOpinion(Opinion opinion){
		getSession().saveOrUpdate(opinion);
		getSession().flush();
		return opinion;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getUserOpinionsByUser(String userId)throws EazyBpmException{
		List<String> userOpinionList = (List<String>) getSession().createQuery("select userOpinion.word from UserOpinion  AS userOpinion WHERE userOpinion.userId ='"+userId+"' OR userOpinion.userId IS NULL OR userOpinion.userId='' ORDER BY userOpinion.word asc").list();
    	return userOpinionList;
	}
	
	public boolean deleteOpinion(String opinionId) throws EazyBpmException{
		Query query = getSession().createQuery("delete from Opinion WHERE id='"+opinionId+"'");
		int result = query.executeUpdate();
		return result != 0  ? true : false;
	}
}

 