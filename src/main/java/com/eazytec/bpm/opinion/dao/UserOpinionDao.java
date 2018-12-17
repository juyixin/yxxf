package com.eazytec.bpm.opinion.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Opinion;
import com.eazytec.model.UserOpinion;


public interface UserOpinionDao extends GenericDao<UserOpinion, String> {

	public UserOpinion getUserOpinionById(String id) throws EazyBpmException;
	
	public UserOpinion saveUserOpinion(UserOpinion userOpinion) throws EazyBpmException;
	
	public boolean deleteUserOpinion(String userOpinionId) throws EazyBpmException;

  	public List<UserOpinion> getUserOpinion(String opinionWord,String userId) throws EazyBpmException;
  	
  	
  	List<Opinion> getOpinion(String taskId) throws EazyBpmException;
  	
  	Opinion saveOpinion(Opinion opinion);
  	
  	/**
  	 * To get the user option words by log in user
  	 * @param userId
  	 * @return
  	 * @throws EazyBpmException
  	 */
    List<String> getUserOpinionsByUser(String userId)throws EazyBpmException;
    
    boolean deleteOpinion(String opinionId) throws EazyBpmException;

}