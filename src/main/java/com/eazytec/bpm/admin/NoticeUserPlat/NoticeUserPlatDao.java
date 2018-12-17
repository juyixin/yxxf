
package com.eazytec.bpm.admin.NoticeUserPlat;

import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.util.DateUtil;


/**
 * @author easybpm
 *
 */

import com.eazytec.dao.GenericDao;



public interface NoticeUserPlatDao extends GenericDao<NoticeUserPlat,java.lang.String>{

	NoticeUserPlat getNoticeUserPlatByName(String name);

	void removeNoticeUserPlat(String name);

	//List<NoticeUserPlat> getNoticeUserPlats();

	 NoticeUserPlat saveNoticeUserPlat(NoticeUserPlat noticeuserplat);

	 NoticeUserPlat getNoticeUserPlatById(String id);

	void updateIsRead(String userid, String noticeid);
	
	void deleteNoticeUserPlatById(String id);

}
