package com.eazytec.vacate.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
import com.eazytec.vacate.dao.VacateDao;
import com.eazytec.vacate.model.Vacate;

@Repository
public class VacateDaoImpl extends GenericDaoHibernate<Vacate, String> implements VacateDao{
	
	public VacateDaoImpl() {
		super(Vacate.class);
	}

	@Override
	public List<Vacate> getVacate(String startTime,String endTime) {
		String sql = "select ORIGINATOR,first_name,count(originator) as count ,sum(vacate_day) as total from"+ 
				"(select a.originator,b.first_name,a.vacate_day from etec_vacate a "
				+ " left join etec_user b on a.originator = b.id where  a.state='SHZT2'";
				
				 
				 if(StringUtils.isNotBlank(startTime)){
					 sql = sql + "and a.start_time >= '"+startTime+"'";  
					 
				 }
				 if(StringUtils.isNotBlank(endTime)){
					 sql = sql + "and a.end_time <= '"+endTime+"'";  
					 
				 }
				 sql = sql + ")group by originator,first_name order by originator desc";
				 
				 
				 

				 List<Object[]> list = getSession().createSQLQuery(sql).list();
				 List<Vacate> list1 = new ArrayList<Vacate>();
				 Iterator it = list.iterator();  
			        while(it.hasNext()){  
			        	Vacate  v = new Vacate();  
			            Object[]  obj = (Object[]) it.next();  
			            v.setName((String)obj[0]); 
			            v.setRecipientName((String)obj[1]);
			            v.setCount(Integer.valueOf(obj[2].toString()));  
			            v.setTotal((new BigDecimal(obj[3].toString())).toString()); 
			            list1.add(v);   
			        }  
			        
			        return list1;
	}

	@Override
	public List<Vacate> searchVacate(String type,String state) {
		String sql = "select * from ETEC_Vacate ";
		if(type!=null&&type!=""){
			sql = sql + "where vacate_Type='"+type+"'";
			if(state!=null&&state!=""){
				sql = sql + "and state='"+state+"'";
			}
		}else{
			if(state!=null&&state!=""){
				sql = sql + "where state='"+state+"'";
			}
		}
		
		sql = sql + "order by create_Time desc";
		List<Vacate> list = getSession().createSQLQuery(sql).addEntity(Vacate.class).list();
		return list;
	}

	@Override
	public Vacate saveVacateInfo(Vacate Vacate) {
		getSession().save(Vacate);
		getSession().flush();
		return Vacate;
	}

	@Override
	public Vacate getDetail(String id) {
		String sql = "select * from ETEC_Vacate where id='"+id+"'";
		Vacate send = (Vacate) getSession().createSQLQuery(sql).addEntity(Vacate.class).setMaxResults(0).uniqueResult();
		return send;
	}

	@Override
	public Vacate updateVacateInfo(Vacate Vacate) {
		getSession().update(Vacate);
		getSession().flush();
		return Vacate;
	}

	@Override
	public String deleteVacateInfo(List<String> isFalse) {
		
		return null;
	}

	@Override
	public List<Vacate> getVacate1() {
		
		return null;
	}

	@Override
	public int getAllRecordCounts(PageBean<Vacate> pageBean, String name,String userId, String type) {
		 String sql = "select count(*) from etec_Vacate "; 
		 if(type.equals("1")){
			 sql = sql + "where originator ='"+userId+"'";
		 }else{
			 sql = sql + "where (copy_people like '%"+ userId +"%' or approver='"+userId+"')";
		 }
		 if(StringUtils.isNotBlank(name)){
			 sql = sql + "and state = '" + name + "'";  
			 
		 }
		 BigDecimal count = (BigDecimal) getSession().createSQLQuery(sql).uniqueResult();

		 int total = count.intValue();
		         
		 return total; 
	}

	@Override
	public List<Vacate> getAllRecords(PageBean<Vacate> pageBean, String name,String userId, String type) {
		 String sql = "select * from etec_Vacate "; 
		 if(type.equals("1")){
			 sql = sql + "where originator ='"+userId+"'";
		 }else{
			 sql = sql + "where (copy_people like '%"+ userId +"%' or approver='"+userId+"')";
		 }
		 if(StringUtils.isNotBlank(name)){
			 sql = sql + "and state = '" + name + "'";   
		 }
		 sql = sql + "order by create_Time desc";
		 List<Vacate> list = getSession().createSQLQuery(sql).addEntity(Vacate.class).list();
		 return list;
	}
	
	
	
	@Override
	public int getAllRecordCount(PageBean<Vacate> pageBean, String startTime,String endTime) {
		 String sql = "select count(*) as total from ( select originator from etec_Vacate  where state ='SHZT2'"; 
		 
		 if(StringUtils.isNotBlank(startTime)){
			 sql = sql +"and start_time >= '"+startTime+"'";  
			 
		 }
		 if(StringUtils.isNotBlank(endTime)){
			 sql = sql + "and end_time <= '"+endTime+"'";  
			 
		 }
		 sql = sql + "group by originator )";
		 BigDecimal count = (BigDecimal) getSession().createSQLQuery(sql).uniqueResult();

		 int total = count.intValue();
		         
		 return total; 
	}

	@Override
	public List<Vacate> getAllRecord(PageBean<Vacate> pageBean, String startTime,String endTime) {
		String sql = "select ORIGINATOR,first_name,count(originator) as count ,sum(vacate_day) as total from"+ 
				"(select a.originator,b.first_name,a.vacate_day from etec_vacate a "
				+ " left join etec_user b on a.originator = b.id where  a.state='SHZT2'";
				
				 
				 if(StringUtils.isNotBlank(startTime)){
					 sql = sql + "and a.start_time >= '"+startTime+"'";  
					 
				 }
				 if(StringUtils.isNotBlank(endTime)){
					 sql = sql + "and a.end_time <= '"+endTime+"'";  
					 
				 }
				 sql = sql + ")group by originator,first_name order by originator desc";
				 
				 
				 

				 List<Object[]> list = getSession().createSQLQuery(sql).list();
				 List<Vacate> list1 = new ArrayList<Vacate>();
				 Iterator it = list.iterator();  
			        while(it.hasNext()){  
			        	Vacate  v = new Vacate();  
			            Object[]  obj = (Object[]) it.next();  
			            v.setRecipientName((String)obj[0]); 
			            v.setName((String)obj[1]);
			            v.setCount(Integer.valueOf(obj[2].toString()));  
			            v.setTotal((new BigDecimal(obj[3].toString())).toString()); 
			            list1.add(v);   
			        }  
		 
		/* List<Vacate> list = getSession().createSQLQuery(sql)
					.addScalar("name", StandardBasicTypes.STRING)
					.addScalar("count", StandardBasicTypes.INTEGER)
					.setResultTransformer(Transformers.aliasToBean(Vacate.class)).list();*/
		 
	/*	 List<Vacate> list = getSession().createSQLQuery(sql)
					.addEntity("originator", User.class)
					.addScalar("count", StandardBasicTypes.INTEGER)
					.setResultTransformer(Transformers.aliasToBean(Vacate.class)).list();*/
		
		 return list1;
	}

	@Override
	public List<Department> getDepartment(String id) {
		String sql = "select * from etec_department where super_department ='"+id+"'"; 
		List<Department> list = getSession().createSQLQuery(sql).addEntity(Department.class).list();
		return list;
	}
	
	@Override
	public List<User> getUser(String id) {
		String sql = "select * from etec_user where department ='"+id+"'"; 
		List<User> list = getSession().createSQLQuery(sql).addEntity(User.class).list();
		return list;
	}
	
	
	/**
	 * Description:查询字典code对应的字典名称
	 * 作者 : 蒋晨   
	 * 时间 : 2017-11-11 下午1:40:10
	 */
	public DataDictionary getNameList(String department){
		String sql = "select * from etec_data_dictionary where code ='"+department+"'";
		DataDictionary xc = (DataDictionary) getSession().createSQLQuery(sql).addEntity(DataDictionary.class).setMaxResults(1).uniqueResult();
		return xc;
	}

	@Override
	public void approverVacate(String id, String opinion,String state,String type) {
		String hql = "";
		if(type.equals("1")){
			hql="update Vacate link set link.state = '"+state+"', link.opinion ='"+opinion+"'"+"where link.id ='"+id+"'";
		}else{
		    hql="update GeneralProcess link set link.state = '"+state+"', link.opinion ='"+opinion+"'"+"where link.id ='"+id+"'";
		}
		getSession().createQuery(hql).executeUpdate();
		getSession().flush();
	}

	@Override
	public List<Vacate> getVacateList(String id,String startTime,String endTime) {
		String sql = "select * from etec_vacate where state='SHZT2' and originator ='"+id+"'";
		 if(StringUtils.isNotBlank(startTime)){
			 sql = sql + "and start_time >= '"+startTime+"'";  
			 
		 }
		 if(StringUtils.isNotBlank(endTime)){
			 sql = sql + "and end_time <= '"+endTime+"'";  
			 
		 }
		List<Vacate> list = getSession().createSQLQuery(sql).addEntity(Vacate.class).list();
		return list;
	}

	@Override
	public int getCount(String id, String startTime,String endTime) {
		String sql = "select count(*) from etec_vacate where state='SHZT2' and originator ='"+id+"'";
		 if(StringUtils.isNotBlank(startTime)){
			 sql = sql + "and start_time >= '"+startTime+"'";  
			 
		 }
		 if(StringUtils.isNotBlank(endTime)){
			 sql = sql + "and end_time <= '"+endTime+"'";  
			 
		 }
		BigDecimal count = (BigDecimal) getSession().createSQLQuery(sql).uniqueResult();
		int total = count.intValue();
		return total;
	}


	@Override
	public List<Vacate> getVacateList1(String id, String startTime, String endTime) {
		
		//List<Vacate> list3 = getSession().createSQLQuery("select v.originator ,count(v.originator) as count from Vacatve v where v.state='SHZT2' group by v.originator").addEntity(Vacate.class).list(); 
		String sql = "select ORIGINATOR,count(originator) as count from etec_vacate where state='SHZT2' group by originator";
		List<Object[]> list1 = getSession().createSQLQuery(sql).list();
		
		List<Vacate> list = getSession().createSQLQuery(sql)
				.addEntity("ORIGINATOR", User.class)
				.addScalar("count", StandardBasicTypes.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(Vacate.class)).list();
		
/*		Query query = getSession().createSQLQuery(sql);  
         
        List<Vacate> list = query.list();   
        
      //封装  
        List<Vacate>  list2 = new ArrayList();  
        Vacate v= null;  
        Iterator it = list.iterator();  
        while(it.hasNext()){  
            v = new Vacate();  
            Object[]  obj = (Object[]) it.next();  
            v.setOriginator((User) obj[0]);  
            v.setCount((Integer)obj[1]);  
            list2.add(v);   
        }  
		*/
		return list;

	}

}
