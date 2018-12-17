package com.eazytec.bpm.admin.Djsd;

import java.util.*;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Djsd;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




@Repository("djsdDao")
public class DjsdDaoImpl extends GenericDaoHibernate<Djsd,java.lang.String> implements DjsdDao{

	public DjsdDaoImpl(){
		super(Djsd.class);
	}
	@Transactional
	@Override
	public Djsd getDjsdByName(String name) {
		List<Djsd> djsds = getSession().createCriteria(Djsd.class)
				.add(Restrictions.eq("name", name)).list();
		if (djsds.isEmpty()) {
			return null;
		} else {
			return (Djsd) djsds.get(0);
		}
	}
	@Transactional
	@Override
	public void removeDjsd(String name) {
		log.info("removing Djsd"+name);
		Djsd djsd = getDjsdByName(name);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(djsd);
	}
	
	@Transactional
	@Override
	public Djsd saveDjsd(Djsd djsd) {
		if (log.isDebugEnabled()) {
			log.debug("djsd's id: " + djsd.getId());
		}
		getSession().saveOrUpdate(djsd);
		// necessary to throw a DataIntegrityViolation and catch it in
		// DepartmentManager
		getSession().flush();
		return djsd;
	}
	
	@Transactional
	@Override
	public Djsd getDjsdById(String id) {
		List djsds = getSession().createCriteria(Djsd.class)
				.add(Restrictions.eq("id", id)).list();
		if (djsds.isEmpty()) {
			return null;
		} else {
			return (Djsd) djsds.get(0);
		}
	}

	@Transactional
	@Override
	public List<Djsd> getNoticeListByUserid() {
		// TODO Auto-generated method stub
		String newquery="";
		newquery="select ID as id, TITLE as title,CREATETIME as createtime,CREATEPERSON as createperson,SFFB as sffb from ETEC_YXXF_DJSD  order by createtime desc";
		List<Djsd> djsds = getSession().createSQLQuery(newquery)
				.addScalar("id",StandardBasicTypes.STRING)
				.addScalar("title",StandardBasicTypes.STRING)
				.addScalar("createtime",StandardBasicTypes.DATE)
				.addScalar("createperson",StandardBasicTypes.STRING)
				.addScalar("sffb",StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(Djsd.class)).list();
		if (djsds.isEmpty()) {
			return null;
		} else {
			return djsds;
		}
	}
	
	@Transactional
	@Override
	public List<Djsd> getNoticeListByUserid1(String isActive) {
		// TODO Auto-generated method stub
		String newquery="";
		newquery="select ID as id, TITLE as title,CREATETIME as createtime,CREATEPERSON as createperson,SFFB as sffb from ETEC_YXXF_DJSD  where SFFB ='"+isActive+"'order by createtime desc";
		List<Djsd> djsds = getSession().createSQLQuery(newquery)
				.addScalar("id",StandardBasicTypes.STRING)
				.addScalar("title",StandardBasicTypes.STRING)
				.addScalar("createtime",StandardBasicTypes.DATE)
				.addScalar("createperson",StandardBasicTypes.STRING)
				.addScalar("sffb",StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(Djsd.class)).list();
		if (djsds.isEmpty()) {
			return null;
		} else {
			return djsds;
		}
	}

	@Transactional
	@Override
	public List<NoticeUserPlat> showNoticeListByUserid(String userId) {
		// TODO Auto-generated method stub
		String newquery="";
		newquery="select * from etec_notice_user_plat where userid='"+userId+"' and is_read='N' and  rownum<=5 order by createtime desc";
		List<NoticeUserPlat> djsds = getSession().createSQLQuery(newquery).addEntity(NoticeUserPlat.class).list();
		if (djsds.isEmpty()) {
			return null;
		} else {
			return djsds;
		}
	}

	@Transactional
	@Override
	public List<User> getallUserList(List<List<StringBuffer>> list) {
		List<User> usersList = new ArrayList<User>();
		for(int i = 0 ;i<list.size();i++){
			String sql = "";
			sql = "select * from etec_user where id in "+list.get(i).get(0)+"";
			List<User> lists = getSession().createSQLQuery(sql).addEntity(User.class).list();
			usersList.addAll(lists);
		}

		if (usersList.isEmpty()) {
			return null;
		} else {
			return usersList;
		}

	}

	@Transactional
	@Override
	public List<User> getallQyList() {
		String newquery="";
		newquery="select * from etec_user where id in (select user_id from etec_user_role where role_id  in ('ROLE_COMPANY','ROLE_FINANCER'))";
		List<User> usersList = getSession().createSQLQuery(newquery).addEntity(User.class).list();
		if (usersList.isEmpty()) {
			return null;
		} else {
			return usersList;
		}
	}

	@Transactional
	@Override
	public List<User> getUserList() {
		// TODO Auto-generated method stub
		String newquery="";
		newquery="select * from etec_user where id not in (select user_id from etec_user_role where role_id  in ('ROLE_COMPANY','ROLE_FINANCER'))";
		List<User> usersList = getSession().createSQLQuery(newquery).addEntity(User.class).list();
		if (usersList.isEmpty()) {
			return null;
		} else {
			return usersList;
		}
	}

	@Transactional
	@Override
	public List<Djsd> getReadNoticeListByUserid(String userid) {
		// TODO Auto-generated method stub
//		String newquery="";
//		newquery="select * from etec_notice_plat where id in (select parentid from etec_notice_user_plat where userid='"+userid+"') and  toperson = '部门端'";
//		List<Djsd> djsds = getSession().createSQLQuery(newquery).addEntity(Djsd.class).list();
//		if (djsds.isEmpty()) {
//			return null;
//		} else {
//			return djsds;
//		}


		String sql = " select  a.id,a.formid as formId,a.title,a.createperson,a.data_type as dataType,a.data_year as dataYear,a.createtime,a.toperson as toperson, b.is_read as isRead  from  (select * from  etec_notice_plat where toperson = '部门端' ) a left join etec_notice_user_plat b on a.id = b.parentid and userid=? order by createtime desc";
		List<Djsd> list = getSession().createSQLQuery(sql)
				.addScalar("id", StandardBasicTypes.STRING)
				.addScalar("formId", StandardBasicTypes.STRING)
				.addScalar("title", StandardBasicTypes.STRING)
				.addScalar("createperson", StandardBasicTypes.STRING)
				.addScalar("dataType", StandardBasicTypes.STRING)
				.addScalar("dataYear", StandardBasicTypes.LONG)
				.addScalar("createtime", StandardBasicTypes.DATE)
				.addScalar("toperson",StandardBasicTypes.STRING)
				.addScalar("isRead", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(Djsd.class)).setParameter(0, userid).list();
		if (list.isEmpty()) {
			return null;
		} else {
			return list;
		}


	}

	@Transactional
	@Override
	public void deleteAllNoticeByIds(String defpIds) {
		String newsQuery = "delete from ETEC_NOTICE_PLAT where id in "+defpIds;
		Query query= getSession().createSQLQuery(newsQuery);
		query.executeUpdate();
		String newsQuery2 = "delete from etec_notice_user_plat where parentid in "+defpIds;
		Query query2= getSession().createSQLQuery(newsQuery2);
		query2.executeUpdate();
		getSession().flush();
	}

	//删除
    @Transactional
	@Override
	public void removeNotice(String jyxId) {
		String hql="delete from Djsd t where t.id=?";
		Query query=getSession().createQuery(hql);
		query.setString(0, jyxId);
		query.executeUpdate();
	}

    @Transactional
	@Override
    public int getAllDjsdCount(PageBean<Djsd> pageBean) {
		Finder f = Finder.create("select count(*) from Djsd djsd where 1=1 ");
//		f.append(" and djsd.sffb like :sffb");
//		f.setParam("sffb", "是");
//		f.append(" order by createtime DESC");
		
			f.append(" and djsd.sffb like :sffb");
			f.setParam("sffb", "%是%");
			
	
		f.append(" order by createtime DESC");
		return findCount(f);
		
	}

    @Transactional
	@Override
public List<Djsd> getAllDjsd(PageBean<Djsd> pageBean){
	Finder f = Finder.create("from Djsd djsd where 1=1");

	f.append(" and djsd.sffb like :sffb");
	f.setParam("sffb", "%是%");
	

f.append(" order by createtime DESC");
	f.setFirstResult(pageBean.getStartRow());
	f.setMaxResults(pageBean.getPageSize());
	
	return find(f);
	
}
    @Transactional
	@Override
	public Djsd getDjsdByIdsffb(String id) {
    	// TODO Auto-generated method stub
    			String newquery="";
    			newquery="select * from ETEC_YXXF_DJSD  t where (t.id='"+id+"' and t.sffb='是') order by createtime desc)";
    			List<Djsd> usersList = getSession().createSQLQuery(newquery).addEntity(Djsd.class).list();
    			if (usersList.isEmpty()) {
    				return null;
    			} else {
    				return (Djsd) usersList.get(0);
    			}
    		}
    
    @Transactional
	@Override
	public List<Djsd> showNoticeListBySffb() {
		String newquery="";
		newquery="select * from ETEC_YXXF_DJSD  t where  t.sffb='是' order by createtime desc";
		List<Djsd> usersList = getSession().createSQLQuery(newquery).addEntity(Djsd.class).list();
		if (usersList.isEmpty()) {
			return null;
		} else {
			return usersList;
		}
	}
    

}
