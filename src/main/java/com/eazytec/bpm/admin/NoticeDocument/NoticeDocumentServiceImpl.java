package com.eazytec.bpm.admin.NoticeDocument;

import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeDocument;
import com.eazytec.util.DateUtil;




import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eazytec.service.impl.GenericManagerImpl;

@Service("noticedocumentService")
public class NoticeDocumentServiceImpl extends GenericManagerImpl<NoticeDocument,java.lang.String> implements NoticeDocumentService{

	private NoticeDocumentDao noticedocumentDao;

	@Autowired
	public NoticeDocumentServiceImpl(NoticeDocumentDao noticedocumentDao) {
		super(noticedocumentDao);
		this.noticedocumentDao = noticedocumentDao;
	}

	@Autowired
	public void setNoticeDocumentDao(NoticeDocumentDao noticedocumentDao) {
		this.dao = noticedocumentDao;
		this.noticedocumentDao = noticedocumentDao;
	}

	public List<NoticeDocument> getNoticeDocuments(NoticeDocument noticedocument) {
		return dao.getAll();
	}

	public NoticeDocument getNoticeDocumentByName(String name) {
		return noticedocumentDao.getNoticeDocumentByName(name);
	}

	
	public NoticeDocument saveNoticeDocument(NoticeDocument noticedocument) throws Exception {

		return noticedocumentDao.saveNoticeDocument(noticedocument);
		
	}

	public NoticeDocument getNoticeDocumentById(String id) {
        return noticedocumentDao.getNoticeDocumentById(id);
    }

	@Override
	public List<NoticeDocument> getDocumentsByParentid(String parentid) {
		// TODO Auto-generated method stub
		return noticedocumentDao.getDocumentsByParentid(parentid);
	}
	
	@Override
	public List<NoticeDocument> getDocumentsByParentids(String parentid) {
		// TODO Auto-generated method stub
		return noticedocumentDao.getDocumentsByParentids(parentid);
	}

	


}
