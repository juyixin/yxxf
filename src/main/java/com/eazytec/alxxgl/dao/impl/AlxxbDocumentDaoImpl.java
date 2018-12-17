package com.eazytec.alxxgl.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.alxxgl.dao.AlxxbDao;
import com.eazytec.alxxgl.dao.AlxxbDocumentDao;
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.crm.model.Record;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Document;
import com.eazytec.model.NoticeUserPlat;

@Repository("alxxbDoucmentDao")
public class AlxxbDocumentDaoImpl extends GenericDaoHibernate<AlxxbDocument,String> implements AlxxbDocumentDao{

	public AlxxbDocumentDaoImpl() {
		super( AlxxbDocument.class);
	}

	@Override
	public List<AlxxbDocument> getDocumentsById(String id) {
		 List<AlxxbDocument> alxxbDocuments = getSession().createQuery("from AlxxbDocument as alxxbDocument where alxxbDocument.alxxb ='"+id+"'").list();
		if (alxxbDocuments.isEmpty()) {
			return null;
		} else {
			return alxxbDocuments;
		}
	}

	@Override
	public AlxxbDocument getDocumentById(String id) {
		return (AlxxbDocument) getSession().get(AlxxbDocument.class, id);
	}

	@Override
	public void deleteDocumentById(String documentId) {
		Query query = getSession().createQuery("delete from AlxxbDocument as alxxbDocument where alxxbDocument.id = '"+documentId+"'");
        query.executeUpdate();
	}

	@Override
	public List<AlxxbDocument> getDocumentsByIds(String id) {
		 List<AlxxbDocument> alxxbDocuments = getSession().createQuery("from AlxxbDocument as alxxbDocument where alxxbDocument.documentformid ='"+id+"' ").list();
			if (alxxbDocuments.isEmpty()) {
				return null;
			} else {
				return alxxbDocuments;
			}
		}

 
}
