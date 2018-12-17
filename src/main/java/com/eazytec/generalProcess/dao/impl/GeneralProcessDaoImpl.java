package com.eazytec.generalProcess.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.generalProcess.dao.GeneralProcessDao;
import com.eazytec.generalProcess.model.GeneralProcess;
import com.eazytec.util.PageBean;


@Repository
public class GeneralProcessDaoImpl extends GenericDaoHibernate<GeneralProcess, String> implements GeneralProcessDao{
	
	public GeneralProcessDaoImpl() {
		super(GeneralProcess.class);
	}

	@Override
	public List<GeneralProcess> getGeneralProcess() {
		String sql = "select * from ETEC_GENERAL_PROCESS order by create_Time desc";
		List<GeneralProcess> list = getSession().createSQLQuery(sql).addEntity(GeneralProcess.class).list();
		return list;
	}

	@Override
	public List<GeneralProcess> searchGeneralProcess(String name,String state) {	
		String sql = "select * from ETEC_GENERAL_PROCESS "; 
		 
		 if(StringUtils.isNotBlank(state)){
			 sql = sql + "where state ='" + state + "'";  
			 if(StringUtils.isNotBlank(name)){
				 sql = sql + "and item_name like '%" + name + "%'";   
			 }
		 }else{
			 if(StringUtils.isNotBlank(name)){
				 sql = sql + "where item_name like '%" + name + "%'";   
			 }
		 }
		 
		 sql = sql + "order by create_Time desc";
		
		List<GeneralProcess> list = getSession().createSQLQuery(sql).addEntity(GeneralProcess.class).list();
		return list;
	}

	@Override
	public GeneralProcess saveGeneralProcessInfo(GeneralProcess GeneralProcess) {
		getSession().save(GeneralProcess);
		getSession().flush();
		return GeneralProcess;
	}

	@Override
	public GeneralProcess getDetail(String id) {
		String sql = "select * from ETEC_GENERAL_PROCESS where id='"+id+"'";
		GeneralProcess send = (GeneralProcess) getSession().createSQLQuery(sql).addEntity(GeneralProcess.class).setMaxResults(0).uniqueResult();
		return send;
	}

	@Override
	public GeneralProcess updateGeneralProcessInfo(GeneralProcess GeneralProcess) {
		getSession().update(GeneralProcess);
		getSession().flush();
		return GeneralProcess;
	}

	@Override
	public String deleteGeneralProcessInfo(List<String> isFalse) {
		
		return null;
	}

	@Override
	public List<GeneralProcess> getGeneralProcess1() {
		
		return null;
	}

	@Override
	public int getAllRecordCount(PageBean<GeneralProcess> pageBean, String name,String userId, String type,String state) {
		 String sql = "select count(*) from ETEC_GENERAL_PROCESS "; 
		 if(type.equals("1")){
			 sql = sql + "where originator ='"+userId+"'";
		 }else{
			 sql = sql + "where (copy_people like '%"+ userId +"%' or approver='"+userId+"')";
		 }
		 
		 
		 if(StringUtils.isNotBlank(state)){
			 sql = sql + "and state ='" + state + "'";  
			 
		 }
		 
		 if(StringUtils.isNotBlank(name)){
			 sql = sql + "and item_name like '%" + name + "%'";  
			 
		 }
		
		 
		 
		 BigDecimal count = (BigDecimal) getSession().createSQLQuery(sql).uniqueResult();

		 int total = count.intValue();
		         
		 return total; 
	}

	@Override
	public List<GeneralProcess> getAllRecord(PageBean<GeneralProcess> pageBean, String name,String userId, String type,String state) {
		String sql = "select * from ETEC_GENERAL_PROCESS "; 
		 if(type.equals("1")){
			 sql = sql + "where originator ='"+userId+"'";
		 }else{
			 sql = sql + "where (copy_people like '%"+ userId +"%' or approver='"+userId+"')";
		 }
		 
		 if(StringUtils.isNotBlank(state)){
			 sql = sql + "and state ='" + state + "'";  
			 
		 }
		 
		 if(StringUtils.isNotBlank(name)){
			 sql = sql + "and item_name like '%" + name + "%'";   
		 }
		 sql = sql + "order by create_Time desc";
		 List<GeneralProcess> list = getSession().createSQLQuery(sql).addEntity(GeneralProcess.class).list();
		 return list;
	}

}
