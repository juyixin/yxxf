package com.eazytec.petitionLetter.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.crm.model.Record;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.petitionLetter.dao.PetitionLetterPersonDao;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.util.PageBean;

@Repository("petitionLetterPersonDao")
public class PetitionLetterPersonDaoImpl extends GenericDaoHibernate<PetitionLetterPerson,String> implements PetitionLetterPersonDao{
       
	public PetitionLetterPersonDaoImpl(){
		super(PetitionLetterPerson.class);
	}
    
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	@Override
	public List<PetitionLetterPerson> getPerson() {
		String sql = "select * from etec_yxxf_xfrxxb order by create_Time desc";
		List<PetitionLetterPerson> personList = getSession().createSQLQuery(sql).addEntity(PetitionLetterPerson.class).list();
		return personList;
	}
    
	
	/**
     * Description:查询数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午1:57:57
     */
	@Override
	public List<PetitionLetterPerson> searchPerson(String name) {
		String sql = "select * from etec_yxxf_xfrxxb where XM like '%"+ name +"%' order by create_Time desc";
		List<PetitionLetterPerson> personList = getSession().createSQLQuery(sql).addEntity(PetitionLetterPerson.class).list();
		return personList;
	}
		
	
	/**
     * Description:保存数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-23 下午8:00:01
     */
	@Override
	public void savePerson(PetitionLetterPerson person) {
		getSession().save(person);
		getSession().flush();
	}


	/**
     * Description:校验身份证的唯一性
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 上午9:06:01
     */
	@Override
	public int getCount(String sfzCode) {
		int count = 0;
		String sql = "select * from etec_yxxf_xfrxxb where SFZ_CODE ='"+sfzCode+"'";
		List<PetitionLetterPerson> listCount = getSession().createSQLQuery(sql).addEntity(PetitionLetterPerson.class).list();
		if(!listCount.isEmpty()){
			count = 1;
		}else{
			count = 0;
		}
		return count;
	}


	/**
     * Description:查看详情
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 上午9:58:38
     */
	@Override
	public PetitionLetterPerson getDetail(String id) {
		String sql = "select * from etec_yxxf_xfrxxb where id ='"+id+"'";
		PetitionLetterPerson  person  = (PetitionLetterPerson)getSession().createSQLQuery(sql).addEntity(PetitionLetterPerson.class).setMaxResults(1).uniqueResult();
		return person;
	}
	
	/**
     * Description:更新数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-24 下午8:00:01
     */
	@Override
	public void updatePerson(PetitionLetterPerson person) {
		getSession().update(person);
		getSession().flush();
		
	}


	/**
     * Description:删除数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-25 上午9:53:13
     */
	@Override
	public void deletePersonInfo(String id) {
		String sql = "delete from etec_yxxf_xfrxxb where id= ?";   
	    Query query = getSession().createSQLQuery(sql);   
	    query.setParameter(0, id); 
	    query.executeUpdate();
	}
	
	
	 @Override
     public int getAllRecordCount(PageBean<PetitionLetterPerson> pageBean, String name,String sfzCode) {
		
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
		
		Finder f = Finder.create("select count(*) from PetitionLetterPerson person ");
		
		if (StringUtils.isNotBlank(name)) {
			f.append(" where person.xm like :name");
			f.setParam("name", "%" + name + "%");
			if(StringUtils.isNotBlank(sfzCode)){
				f.append(" and person.sfzhm like :sfzCode");
				f.setParam("sfzCode", "%" + sfzCode + "%");
			}
		}else{
			if(StringUtils.isNotBlank(sfzCode)){
				f.append(" where person.sfzhm like :sfzCode");
				f.setParam("sfzCode", "%" + sfzCode + "%");
			}
		}
		return findCount(f);
		
	}



	@Override
	public List<PetitionLetterPerson> getAllRecord(PageBean<PetitionLetterPerson> pageBean, String name,
			String sfzCode) {

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
		
		Finder f = Finder.create("from PetitionLetterPerson person ");
		
		if (StringUtils.isNotBlank(name)) {
			f.append(" where person.xm like :name");
			f.setParam("name", "%" + name + "%");
			if(StringUtils.isNotBlank(sfzCode)){
				f.append(" and person.sfzhm like :sfzCode");
				f.setParam("sfzCode", "%" + sfzCode + "%");
			}
		}else{
			if(StringUtils.isNotBlank(sfzCode)){
				f.append("where person.sfzhm like :sfzCode");
				f.setParam("sfzCode", "%" + sfzCode + "%");
			}
		}
		
		f.append(" order by createTime desc");
		f.setFirstResult(pageBean.getStartRow());
		f.setMaxResults(pageBean.getPageSize());
		
		return find(f);
	}

}
