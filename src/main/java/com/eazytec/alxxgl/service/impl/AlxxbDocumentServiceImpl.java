package com.eazytec.alxxgl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.alxxgl.dao.AlxxbDocumentDao;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.alxxgl.service.AlxxbDocumentService;

@Service("alxxbDocumentService")
public class AlxxbDocumentServiceImpl  implements AlxxbDocumentService {
	@Autowired
	private AlxxbDocumentDao alxxbDocumentDao;

	@Override
	public List<AlxxbDocument> getDocumentsById(String id) {
		 
		return alxxbDocumentDao.getDocumentsById(id);
	}

	@Override
	public AlxxbDocument getDocumentById(String id) {
		return alxxbDocumentDao.getDocumentById(id);
	}

	@Override
	public void deleteDocumentById(String documentId) {
		alxxbDocumentDao.deleteDocumentById(documentId);
	}

	@Override
	public List<AlxxbDocument> getDocumentsByIds(String id) {
		// TODO Auto-generated method stub
		return alxxbDocumentDao.getDocumentsByIds(id);
	}
}
