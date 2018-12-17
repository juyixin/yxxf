package com.eazytec.alxxgl.service;

import java.util.List;

import com.eazytec.alxxgl.model.AlxxbDocument;

public interface AlxxbDocumentService {

	List<AlxxbDocument> getDocumentsById(String id);

	AlxxbDocument getDocumentById(String id);

	void deleteDocumentById(String documentId);
	
	List<AlxxbDocument> getDocumentsByIds(String id);


}
