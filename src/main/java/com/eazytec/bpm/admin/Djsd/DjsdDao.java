
package com.eazytec.bpm.admin.Djsd;

import java.util.*;
import com.eazytec.model.Djsd;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;


import com.eazytec.dao.GenericDao;



public interface DjsdDao extends GenericDao<Djsd,java.lang.String>{

	Djsd getDjsdByName(String name);

	void removeDjsd(String name);

	//List<Djsd> getDjsds();

	 Djsd saveDjsd(Djsd noticeplat);

	 Djsd getDjsdById(String id);

	List<Djsd> getNoticeListByUserid();
	
	List<Djsd> getNoticeListByUserid1(String isActive);

	List<NoticeUserPlat> showNoticeListByUserid(String userid);

	List<User> getUserList();

	List<Djsd> getReadNoticeListByUserid(String userid);

	void deleteAllNoticeByIds(String string);
	public List<User> getallUserList(List<List<StringBuffer>> list);

	 List<User> getallQyList();
	 
			void removeNotice(String jyxId);
			
			
			int getAllDjsdCount(PageBean<Djsd> pageBean);

			List<Djsd> getAllDjsd(PageBean<Djsd> pageBean);
			
			 Djsd getDjsdByIdsffb(String id);
			 
			 List<Djsd> showNoticeListBySffb();

}
