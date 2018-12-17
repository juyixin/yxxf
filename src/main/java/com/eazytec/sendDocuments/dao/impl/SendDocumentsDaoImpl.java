package com.eazytec.sendDocuments.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.sendDocuments.dao.SendDocumentsDao;
import com.eazytec.sendDocuments.model.SendDocuments;
import com.eazytec.util.PageBean;

@Repository
public class SendDocumentsDaoImpl extends GenericDaoHibernate<SendDocuments, String> implements SendDocumentsDao{

	public SendDocumentsDaoImpl() {
		super(SendDocuments.class);
	}

	@Override
	public List<SendDocuments> getSendDocuments() {
		String sql = "select * from ETEC_SENT_DOCUMENTS order by send_Time desc";
		List<SendDocuments> list = getSession().createSQLQuery(sql).addEntity(SendDocuments.class).list();
		return list;
	}

	@Override
	public List<SendDocuments> searchSendDocuments(String name) {
		String sql = "select * from ETEC_SENT_DOCUMENTS where theme like '%"+name+"%' order by send_Time desc";
		List<SendDocuments> list = getSession().createSQLQuery(sql).addEntity(SendDocuments.class).list();
		return list;
	}

	@Override
	public SendDocuments saveSendDocumentsInfo(SendDocuments sendDocuments) {
		getSession().save(sendDocuments);
		getSession().flush();
		return sendDocuments;
	}

	@Override
	public SendDocuments getDetail(String id) {
		String sql = "select * from ETEC_SENT_DOCUMENTS where id='"+id+"'";
		SendDocuments send = (SendDocuments) getSession().createSQLQuery(sql).addEntity(SendDocuments.class).setMaxResults(0).uniqueResult();
		return send;
	}

	@Override
	public SendDocuments updateSendDocumentsInfo(SendDocuments sendDocuments) {
		getSession().update(sendDocuments);
		getSession().flush();
		return sendDocuments;
	}

	@Override
	public String deleteSendDocumentsInfo(List<String> isFalse) {
		
		return null;
	}

	@Override
	public List<SendDocuments> getSendDocuments1() {
		
		return null;
	}

	@Override
	public int getAllRecordCount(PageBean<SendDocuments> pageBean, String name,String userId, String type) {
		 String sql = "select count(*) from etec_sent_documents "; 
		 if(type.equals("1")){
			 sql = sql + "where sender ='"+userId+"'";
		 }else{
			 sql = sql + "where (copy_people like '%"+ userId +"%' or recipient like '%"+ userId +"%')";
		 }
		 if(StringUtils.isNotBlank(name)){
			 sql = sql + "and theme like '%" + name + "%'";  
			 
		 }
		 BigDecimal count = (BigDecimal) getSession().createSQLQuery(sql).uniqueResult();

		 int total = count.intValue();
		         
		 return total; 
	}

	@Override
	public List<SendDocuments> getAllRecord(PageBean<SendDocuments> pageBean, String name,String userId, String type) {
		String sql = "select * from etec_sent_documents "; 
		 if(type.equals("1")){
			 sql = sql + "where sender ='"+userId+"'";
		 }else{
			 sql = sql + "where ( copy_people like '%"+ userId +"%' or recipient like '%"+ userId +"%')";
		 }
		 if(StringUtils.isNotBlank(name)){
			 sql = sql + "and theme like '%" + name + "%'";   
		 }
		 sql = sql + "order by send_Time desc";
		 List<SendDocuments> list = getSession().createSQLQuery(sql).addEntity(SendDocuments.class).list();
		 return list;
	}
}
