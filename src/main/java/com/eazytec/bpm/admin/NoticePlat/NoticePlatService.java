package com.eazytec.bpm.admin.NoticePlat;

import java.util.*;

import com.eazytec.model.NoticePlat;
import com.eazytec.model.NoticeUserPlat;

import com.eazytec.util.DateUtil;
import com.eazytec.util.PageBean;

/**
 * @author easybpm
 *
 */


import javax.jws.WebService;

import com.eazytec.util.DateUtil;


/**
 * @author easybpm
 *
 */


import javax.jws.WebService;


import org.springframework.web.multipart.MultipartFile;

public interface NoticePlatService{

	public NoticePlat getNoticePlatByName(String name);

    public NoticePlat saveNoticePlat(NoticePlat noticeplat) throws Exception;

	public NoticePlat getNoticePlatById(String id);

	public List<NoticePlat> getNoticeListByUserid();
	
	public List<NoticePlat> getNoticeListByUserid1(String isActive);

	public NoticePlat saveOrUpdateDocumentForm(NoticePlat noticeForm, List<MultipartFile> files, String userName,
			String path, List<String[]> deleteArray);

	public NoticePlat  mysaveOrUpdateDocumentForm(NoticePlat noticeForm, List<MultipartFile> files,List<MultipartFile> myfiles, String userName,
											   String path, List<String[]> deleteArray);


	public List<NoticeUserPlat> showNoticeListByUserid(String userid);

	public List<NoticePlat> getReadNoticeListByUserid(String userid);

	public void deleteAllNoticeByIds(List<String> noticeIdList);
	
	public void removeNotice(List<String> jyxIds);
	
	
	NoticePlat getNoticePlatByIdS(String id);
	
	PageBean<NoticePlat> getNoticePlat(PageBean<NoticePlat> pageBean);
	
	public NoticePlat getNoticePlatByIdsffb(String id);
	
	public List<NoticePlat> showNoticeListBySffb();

}
