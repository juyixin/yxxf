
package com.eazytec.bpm.admin.NoticeUserPlat;

import java.util.*;


import com.eazytec.model.BaseObject;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.util.DateUtil;

import org.hibernate.Query;

/**
 * @author easybpm
 *
 */


import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.hibernate.GenericDaoHibernate;


@Repository("noticeuserplatDao")
public class NoticeUserPlatDaoImpl extends GenericDaoHibernate<NoticeUserPlat,java.lang.String> implements NoticeUserPlatDao{

	public NoticeUserPlatDaoImpl(){
		super(NoticeUserPlat.class);
	}

	public NoticeUserPlat getNoticeUserPlatByName(String name) {
		List<NoticeUserPlat> noticeuserplats = getSession().createCriteria(NoticeUserPlat.class)
				.add(Restrictions.eq("name", name)).list();
		if (noticeuserplats.isEmpty()) {
			return null;
		} else {
			return (NoticeUserPlat) noticeuserplats.get(0);
		}
	}
	@Transactional
	@Override
	public void removeNoticeUserPlat(String name) {
		log.info("removing NoticeUserPlat"+name);
		NoticeUserPlat noticeuserplat = getNoticeUserPlatByName(name);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(noticeuserplat);
	}
	@Transactional
	@Override
	public NoticeUserPlat saveNoticeUserPlat(NoticeUserPlat noticeuserplat) {
		if (log.isDebugEnabled()) {
			log.debug("noticeuserplat's id: " + noticeuserplat.getId());
		}
		getSession().saveOrUpdate(noticeuserplat);
		// necessary to throw a DataIntegrityViolation and catch it in
		// DepartmentManager
		getSession().flush();
		return noticeuserplat;
	}
	@Transactional
	@Override
	public NoticeUserPlat getNoticeUserPlatById(String id) {
		List noticeuserplats = getSession().createCriteria(NoticeUserPlat.class)
				.add(Restrictions.eq("id", id)).list();
		if (noticeuserplats.isEmpty()) {
			return null;
		} else {
			return (NoticeUserPlat) noticeuserplats.get(0);
		}
	}

	@Transactional
	@Override
	public void updateIsRead(String userid, String noticeid) {
		// TODO Auto-generated method stub
		String newsQuery = "update  etec_notice_user_plat set is_read='Y' where userid='"+userid+"' and parentid='"+noticeid+"'";
		Query query= getSession().createSQLQuery(newsQuery);
		query.executeUpdate();
		getSession().flush();
	}
	
	@Transactional
	@Override
	public void deleteNoticeUserPlatById(String id) {
		// TODO Auto-generated method stub
		getSession().createSQLQuery("delete from etec_notice_user_plat where parentid = ?")
		.setParameter(0, id).executeUpdate();
	}



}
