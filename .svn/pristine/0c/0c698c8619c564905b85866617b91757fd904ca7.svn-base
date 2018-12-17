package com.eazytec.bpm.opinion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.opinion.dao.UserOpinionDao;
import com.eazytec.bpm.opinion.service.UserOpinionService;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Opinion;
import com.eazytec.model.UserOpinion;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("userOpinionService")
public class UserOpinionServiceImpl extends GenericManagerImpl<UserOpinion, String> implements UserOpinionService {
	
	private UserOpinionDao userOpinionDao;

	@Autowired
	public void setUserOpinionDao(UserOpinionDao userOpinionDao) {
		this.userOpinionDao = userOpinionDao;
	}


	public UserOpinion saveUserOpinion(UserOpinion userOpinion) {
	    	return userOpinionDao.saveUserOpinion(userOpinion);
	    }
	
	@Override
	public boolean deleteUserOpinion(List<String> userOpinionIds) {
				boolean isDelete = false;
				for(String useropinion : userOpinionIds){
					isDelete = userOpinionDao.deleteUserOpinion(useropinion);
		        }		
				return isDelete;
	}
	
	  public UserOpinion getUserOpinionById(String id) {
		  	return userOpinionDao.getUserOpinionById(id);
	  }
	  
	  public List<UserOpinion> getUserOpinion(String opinionWord,String userId) throws EazyBpmException{
	    	return userOpinionDao.getUserOpinion(opinionWord,userId);
	    }
	  
	 /**
	 * {@inheritDoc}
	 */
	  public List<String> getUserOpinionsByUser(String userId)throws EazyBpmException{
		  return userOpinionDao.getUserOpinionsByUser(userId);
	  }
	  
	  public List<Opinion> getOpinion(String taskId) throws EazyBpmException{
	    	return userOpinionDao.getOpinion(taskId);
	    }
	  
	  public Opinion saveOpinion(Opinion opinion){
	    	return userOpinionDao.saveOpinion(opinion);
	    }
	  
	  public boolean deleteOpinion(String opinionId) throws EazyBpmException{
		  return userOpinionDao.deleteOpinion(opinionId);
	  }

}
