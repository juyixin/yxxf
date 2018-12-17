package com.eazytec.bpm.admin.NoticeUserPlat;

import java.util.*;

import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.util.DateUtil;



import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("noticeuserplatService")
public class NoticeUserPlatServiceImpl extends GenericManagerImpl<NoticeUserPlat,java.lang.String> implements NoticeUserPlatService{

	private NoticeUserPlatDao noticeuserplatDao;

	@Autowired
	public NoticeUserPlatServiceImpl(NoticeUserPlatDao noticeuserplatDao) {
		super(noticeuserplatDao);
		this.noticeuserplatDao = noticeuserplatDao;
	}

	@Autowired
	public void setNoticeUserPlatDao(NoticeUserPlatDao noticeuserplatDao) {
		this.dao = noticeuserplatDao;
		this.noticeuserplatDao = noticeuserplatDao;
	}

	public List<NoticeUserPlat> getNoticeUserPlats(NoticeUserPlat noticeuserplat) {
		return dao.getAll();
	}

	public NoticeUserPlat getNoticeUserPlatByName(String name) {
		return noticeuserplatDao.getNoticeUserPlatByName(name);
	}

	
	public NoticeUserPlat saveNoticeUserPlat(NoticeUserPlat noticeuserplat) throws Exception {

		return noticeuserplatDao.saveNoticeUserPlat(noticeuserplat);
		
	}

	public NoticeUserPlat getNoticeUserPlatById(String id) {
        return noticeuserplatDao.getNoticeUserPlatById(id);
    }

	@Override
	public void updateIsRead(String userid, String noticeid) {
		// TODO Auto-generated method stub
		noticeuserplatDao.updateIsRead(userid,noticeid);
	}

	


}
