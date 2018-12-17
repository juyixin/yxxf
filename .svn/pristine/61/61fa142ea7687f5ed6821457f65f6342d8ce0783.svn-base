package com.eazytec.bpm.admin.Zcfg;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.crm.model.Record;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Zcfg;
import com.eazytec.util.PageBean;

@Repository("zcfgDao")
public class ZcfgDaoImpl extends GenericDaoHibernate<Zcfg, java.lang.String> implements ZcfgDao {
	public ZcfgDaoImpl() {
		super(Zcfg.class);
	}

	@Transactional
	@Override
	public List<Zcfg> getZcfgListByUserid() {
		// TODO Auto-generated method stub
		String newquery = "";
		newquery = "select ID as id, TITLE as title,CREATETIME as createtime,CREATEPERSON as createperson,CONTENT as content from ETEC_YXXF_ZCFG";
		List<Zcfg> noticeplats = getSession().createSQLQuery(newquery).addScalar("id", StandardBasicTypes.STRING)
				.addScalar("title", StandardBasicTypes.STRING).addScalar("createtime", StandardBasicTypes.DATE)
				.addScalar("createperson", StandardBasicTypes.STRING).addScalar("content", StandardBasicTypes.STRING)
				.setResultTransformer(Transformers.aliasToBean(Zcfg.class)).list();
		if (noticeplats.isEmpty()) {
			return null;
		} else {
			return noticeplats;
		}
	}

	// 删除
	@Transactional
	@Override
	public void removeZcfg(String jyxId) {
		String hql = "delete from Zcfg t where t.id=?";
		Query query = getSession().createQuery(hql);
		query.setString(0, jyxId);
		query.executeUpdate();
	}

	@Transactional
	@Override
	public Zcfg saveZcfg(Zcfg zcfg) {
		if (log.isDebugEnabled()) {
			log.debug("zcfg's id: " + zcfg.getId());
		}
		getSession().saveOrUpdate(zcfg);
		// necessary to throw a DataIntegrityViolation and catch it in
		// DepartmentManager
		getSession().flush();
		return zcfg;
	}

	@Transactional
	@Override
	public Zcfg getZcfgById(String id) {
		List noticeplats = getSession().createCriteria(Zcfg.class).add(Restrictions.eq("id", id)).list();
		if (noticeplats.isEmpty()) {
			return null;
		} else {
			return (Zcfg) noticeplats.get(0);
		}
	}
	
	@Transactional
	@Override
	public int getAllZcfgCount(PageBean<Zcfg> pageBean, String searchtext) {
		Finder f = Finder.create("select count(*) from Zcfg zcfg where 1=1");

		if (StringUtils.isNotBlank(searchtext)) {
			f.append(" and zcfg.title like :title");
			f.setParam("title", "%" + searchtext + "%");
			
		}
		f.append(" order by createtime DESC");
		return findCount(f);
	}
	
	@Transactional
	@Override
	public List<Zcfg> getAllZcfg(PageBean<Zcfg> pageBean, String searchtext) {
		Finder f = Finder.create("from Zcfg zcfg where 1=1");

		if (StringUtils.isNotBlank(searchtext)) {
			f.append(" and zcfg.title like :title");
			f.setParam("title", "%" + searchtext + "%");
		}
		f.append(" order by createtime DESC");
		f.setFirstResult(pageBean.getStartRow());
		f.setMaxResults(pageBean.getPageSize());

		return find(f);

	}
	@Transactional
	@Override
	public List<Zcfg> getZcfgListByserach(String name) {
		Finder f = Finder.create("from Zcfg zcfg where 1=1");

		if (StringUtils.isNotBlank(name)) {
			f.append(" and zcfg.title like :title");
			f.setParam("title", "%" + name + "%");
		}
		f.append(" order by createtime DESC");

		return find(f);
	}
}
