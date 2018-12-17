package com.eazytec.bpm.admin.Djsd;

import java.util.*;
import com.eazytec.model.Djsd;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.util.DateUtil;
import com.eazytec.util.PageBean;
import javax.jws.WebService;
import com.eazytec.util.DateUtil;
import javax.jws.WebService;
import org.springframework.web.multipart.MultipartFile;

public interface DjsdService{

	public Djsd getDjsdByName(String name);

    public Djsd saveDjsd(Djsd djsd) throws Exception;

	public Djsd getDjsdById(String id);

	public List<Djsd> getNoticeListByUserid();
	
	public List<Djsd> getNoticeListByUserid1(String isActive);

	public Djsd saveOrUpdateDocumentForm(Djsd noticeForm, List<MultipartFile> files, String userName,
			String path, List<String[]> deleteArray);

	public Djsd  mysaveOrUpdateDocumentForm(Djsd noticeForm, List<MultipartFile> files,List<MultipartFile> myfiles, String userName,
											   String path, List<String[]> deleteArray);


	public List<NoticeUserPlat> showNoticeListByUserid(String userid);

	public List<Djsd> getReadNoticeListByUserid(String userid);

	public void deleteAllNoticeByIds(List<String> noticeIdList);
	
	public void removeNotice(List<String> jyxIds);
	
	
	Djsd getDjsdByIdS(String id);
	
	PageBean<Djsd> getDjsd(PageBean<Djsd> pageBean);
	
	public Djsd getDjsdByIdsffb(String id);
	
	public List<Djsd> showNoticeListBySffb();

}
