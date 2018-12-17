
package com.eazytec.bpm.admin.NoticePlat;

import java.util.*;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;


import com.eazytec.dao.GenericDao;



public interface NoticePlatDao extends GenericDao<NoticePlat,java.lang.String>{

	NoticePlat getNoticePlatByName(String name);

	void removeNoticePlat(String name);

	//List<NoticePlat> getNoticePlats();

	 NoticePlat saveNoticePlat(NoticePlat noticeplat);

	 NoticePlat getNoticePlatById(String id);

	List<NoticePlat> getNoticeListByUserid();
	
	List<NoticePlat> getNoticeListByUserid1(String isActive);

	List<NoticeUserPlat> showNoticeListByUserid(String userid);

	List<User> getUserList();

	List<NoticePlat> getReadNoticeListByUserid(String userid);

	void deleteAllNoticeByIds(String string);
	public List<User> getallUserList(List<List<StringBuffer>> list);

	 List<User> getallQyList();
	 
			void removeNotice(String jyxId);
			
			
			int getAllNoticePlatCount(PageBean<NoticePlat> pageBean);

			List<NoticePlat> getAllNoticePlat(PageBean<NoticePlat> pageBean);
			
			 NoticePlat getNoticePlatByIdsffb(String id);
			 
			 List<NoticePlat> showNoticeListBySffb();

}
