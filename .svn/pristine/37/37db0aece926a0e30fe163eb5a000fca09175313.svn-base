package com.eazytec.bpm.admin.NoticeDocument;

import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeDocument;
import com.eazytec.util.DateUtil;


/**
 * @author easybpm
 *
 */

import com.eazytec.dao.GenericDao;



public interface NoticeDocumentDao extends GenericDao<NoticeDocument,java.lang.String>{

	NoticeDocument getNoticeDocumentByName(String name);

	void removeNoticeDocument(String name);

	//List<NoticeDocument> getNoticeDocuments();

	 NoticeDocument saveNoticeDocument(NoticeDocument noticedocument);

	 NoticeDocument getNoticeDocumentById(String id);

	List<NoticeDocument> getDocumentsByParentid(String parentid);
	
	List<NoticeDocument> getDocumentsByParentids(String parentid);

	void deleteDocumentsById(String id);

}
