package com.eazytec.bpm.admin.NoticeUserPlat;
import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.util.DateUtil;

/**
 * @author easybpm
 *
 */


import javax.jws.WebService;

public interface NoticeUserPlatService{

	public NoticeUserPlat getNoticeUserPlatByName(String name);

    public NoticeUserPlat saveNoticeUserPlat(NoticeUserPlat noticeuserplat) throws Exception;

	public NoticeUserPlat getNoticeUserPlatById(String id);

	public void updateIsRead(String userid, String noticeid);
	

}
