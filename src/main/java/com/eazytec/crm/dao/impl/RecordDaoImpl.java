package com.eazytec.crm.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.crm.dao.RecordDao;
import com.eazytec.crm.model.Record;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.util.PageBean;

@Repository("recordDao")
public class RecordDaoImpl extends GenericDaoHibernate<Record, String> implements RecordDao {

	public RecordDaoImpl() {
		super(Record.class);
	}

	public List<Record> getAllRecordList(String num) {
		
		/**
		StringBuilder sb = new StringBuilder("from Record u where 1=1");

		if (StringUtils.isNotBlank(num)) {
			sb.append(" and u.num like :num");
		}

		sb.append(" order by u.createdTime desc");

		Query qry = getSession().createQuery(sb.toString());

		if (StringUtils.isNotBlank(num)) {
			qry.setParameter("num", "%" + num + "%");
		}
		return qry.list();
		
		以上为传统写法，比较啰嗦，推荐使用“Finder”辅助类改写
		
		**/
		
		//推荐使用Finder辅助类编写dao层代码
		
		Finder f = Finder.create("from Record u where 1=1");
		
		if (StringUtils.isNotBlank(num)) {
			f.append(" and u.num = :num");
			f.setParam("num",num );
		}
		return find(f);
	}

	public Record saveRecord(Record record) {
		return save(record);
	}

	public List<Record> getRecordsByIds(List<String> ids) {
		List<Record> records = getSession().createQuery("from Record as record where record.id in (:list)").setParameterList("list", ids).list();
		if (records.isEmpty()) {
			return null;
		} else {
			return records;
		}
	}

	public void deleteRecord(Record record) {
		getSession().delete(record);
	}

	public int getAllRecordCount(PageBean<Record> pageBean, String num) {
		
		/**
		
		StringBuilder sb = new StringBuilder("select count(*) from Record record where 1=1");
		if(StringUtils.isNotBlank(num)){
			sb.append( "and record.num like :num");
		}
		
		Query query = getSession().createQuery(sb.toString());
		
		if(StringUtils.isNotBlank(num)){
			query.setParameter("num", "%" + num + "%");
		}
		return ((Number) query.iterate().next()).intValue();
		以上为传统写法，比较啰嗦，推荐使用“Finder”辅助类改写
		
		**/
		
		//推荐使用Finder辅助类编写dao层代码
		
		Finder f = Finder.create("select count(*) from Record record where 1=1");
		
		if (StringUtils.isNotBlank(num)) {
			f.append(" and record.num like :num");
			f.setParam("num", "%" + num + "%");
		}
		return findCount(f);
		
	}

	public List<Record> getAllRecord(PageBean<Record> pageBean, String num, String orderName,String orderType){
		
		
		/**
		StringBuilder sb = new StringBuilder("from Record record where 1=1");
		if(StringUtils.isNotBlank(num)){
			sb.append( "and record.num like :num");
		}
		
		if(StringUtils.isNotBlank(orderName)){
			sb.append( "order by record."+ orderName + " " +orderType);
		}
		
		Query query = getSession().createQuery(sb.toString());

		if(StringUtils.isNotBlank(num)){
			query.setParameter("num", "%" + num + "%");
		}
		
		List<Record> records = query.setFirstResult(pageBean.getStartRow()).setMaxResults(pageBean.getPageSize()).list();
		return records;
		
		以上为传统写法，比较啰嗦，推荐使用“Finder”辅助类改写
		
		**/
		
		//推荐使用Finder辅助类编写dao层代码
		
		Finder f = Finder.create("from Record record where 1=1");
		
		if (StringUtils.isNotBlank(num)) {
			f.append(" and record.num like :num");
			f.setParam("num", "%" + num + "%");
		}
		
		if(StringUtils.isNotBlank(orderName)){
			f.append( "order by record."+ orderName + " " +orderType);
		}
		f.setFirstResult(pageBean.getStartRow());
		f.setMaxResults(pageBean.getPageSize());
		
		return find(f);
		
	}

}
