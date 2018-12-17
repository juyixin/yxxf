package com.eazytec.bpm.admin.NoticeDocument;

import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.NoticePlat;
import com.eazytec.util.DateUtil;



import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.hibernate.GenericDaoHibernate;


@Repository("noticedocumentDao")
public class NoticeDocumentDaoImpl extends GenericDaoHibernate<NoticeDocument,java.lang.String> implements NoticeDocumentDao{

	public NoticeDocumentDaoImpl(){
		super(NoticeDocument.class);
	}
	@Transactional
	@Override
	public NoticeDocument getNoticeDocumentByName(String name) {
		List<NoticeDocument> noticedocuments = getSession().createCriteria(NoticeDocument.class)
				.add(Restrictions.eq("name", name)).list();
		if (noticedocuments.isEmpty()) {
			return null;
		} else {
			return (NoticeDocument) noticedocuments.get(0);
		}
	}
	@Transactional
	@Override
	public void removeNoticeDocument(String name) {
		log.info("removing NoticeDocument"+name);
		NoticeDocument noticedocument = getNoticeDocumentByName(name);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(noticedocument);
	}
	@Transactional
	@Override
	public NoticeDocument saveNoticeDocument(NoticeDocument noticedocument) {
		if (log.isDebugEnabled()) {
			log.debug("noticedocument's id: " + noticedocument.getId());
		}
		getSession().saveOrUpdate(noticedocument);
		// necessary to throw a DataIntegrityViolation and catch it in
		// DepartmentManager
		getSession().flush();
		return noticedocument;
	}
	@Transactional
	@Override
	public NoticeDocument getNoticeDocumentById(String id) {
		List noticedocuments = getSession().createCriteria(NoticeDocument.class)
				.add(Restrictions.eq("id", id)).list();
		if (noticedocuments.isEmpty()) {
			return null;
		} else {
			return (NoticeDocument) noticedocuments.get(0);
		}
	}

	@Transactional
	@Override
	public List<NoticeDocument> getDocumentsByParentid(String parentid) {
		// TODO Auto-generated method stub
		String sql="select * from ETEC_NOTICE_DOCUMENT t where t.parentid like '%"+parentid+"%'";
		List<NoticeDocument> noticedocuments = new ArrayList();
		try{
			noticedocuments = getSession().createSQLQuery(sql).addEntity(NoticeDocument.class).list();

		 }catch(Exception e){
			 e.printStackTrace();
		 }		 
		 return noticedocuments;
	}
	
	
	@Transactional
	@Override
	public List<NoticeDocument> getDocumentsByParentids(String parentid) {
		// TODO Auto-generated method stub
		String sql="select * from ETEC_NOTICE_DOCUMENT t where t.parentid ='"+parentid+"'";
		List<NoticeDocument> noticedocuments = new ArrayList();
		try{
			noticedocuments = getSession().createSQLQuery(sql).addEntity(NoticeDocument.class).list();

		 }catch(Exception e){
			 e.printStackTrace();
		 }		 
		 return noticedocuments;
	}

	@Transactional
	@Override
	public void deleteDocumentsById(String id) {
		// TODO Auto-generated method stub
		getSession().createSQLQuery("delete from etec_notice_document where id = ?")
		.setParameter(0, id).executeUpdate();
	}



}
