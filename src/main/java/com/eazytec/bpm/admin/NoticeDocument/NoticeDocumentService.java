package com.eazytec.bpm.admin.NoticeDocument;
import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeDocument;
import com.eazytec.util.DateUtil;


import javax.jws.WebService;

public interface NoticeDocumentService{

	public NoticeDocument getNoticeDocumentByName(String name);

    public NoticeDocument saveNoticeDocument(NoticeDocument noticedocument) throws Exception;

	public NoticeDocument getNoticeDocumentById(String id);

	public List<NoticeDocument> getDocumentsByParentid(String parentid);
	
	public List<NoticeDocument> getDocumentsByParentids(String parentid);
	

}
