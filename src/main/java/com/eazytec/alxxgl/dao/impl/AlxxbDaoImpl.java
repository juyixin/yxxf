package com.eazytec.alxxgl.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.alxxgl.dao.AlxxbDao;
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.crm.model.Record;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Txl;
import com.eazytec.model.Zcfg;
import com.eazytec.util.PageBean;

@Repository("alxxbDao")
public class AlxxbDaoImpl extends GenericDaoHibernate<Alxxb, String> implements AlxxbDao {

	public AlxxbDaoImpl() {
		super(Alxxb.class);
	}

	@Override
	public Alxxb saveAlxxb(Alxxb alxxb) {
		return save(alxxb);
	}

	@Override
	public List<Alxxb> getAlxxbByIds(List<String> ids) {
		List<Alxxb> alxxbs = getSession().createQuery("from Alxxb as alxxb where alxxb.id in (:list)")
				.setParameterList("list", ids).list();
		System.out.println(alxxbs);
		if (alxxbs.isEmpty()) {
			return null;
		} else {
			return alxxbs;
		}
	}

	@Override
	public void deleteAlxxb(Alxxb alxxb) {
		getSession().delete(alxxb);
	}

	@Override
	public List<Alxxb> getAllxByAllx(String allx) {
		List<Alxxb> alxxbs = getSession().createQuery("from Alxxb as alxxb where alxxb.allx ='" + allx + "'").list();

		if (alxxbs.isEmpty()) {
			return null;
		} else {
			return alxxbs;
		}
	}
	
	
	@Override
	public List<Alxxb> getAllxByAllx1(String allx) {
		List<Alxxb> alxxbs = getSession().createQuery("from Alxxb as alxxb where alxxb.name like'%" + allx + "%'").list();

		if (alxxbs.isEmpty()) {
			return null;
		} else {
			return alxxbs;
		}
	}
	
	
	
	@Override
	public List<Alxxb> getAllxByAllx2(String id, String name) {
		List<Alxxb> alxxbs = getSession().createQuery("from Alxxb as alxxb  where alxxb.allx ='" + id + "'and alxxb.name like'%" + name + "%'").list();

		if (alxxbs.isEmpty()) {
			return null;
		} else {
			return alxxbs;
		}
	}

	@Transactional
	@Override
	public Alxxb getAllxByIds(String id) {
		List alxxbs = getSession().createQuery("from Alxxb as alxxb where alxxb.id ='" + id + "' order by createdTime DESC").list();
		if (alxxbs.isEmpty()) {
			return null;
		} else {
			return (Alxxb) alxxbs.get(0);
			
		}
	}

	@Override
	public int getAllRecordCount(PageBean<Alxxb> pageBean, String name, String allx, String dsr) {
		Finder f = Finder.create("select count(*) from Alxxb alxxb where 1=1");

		if (StringUtils.isNotBlank(name)) {
			f.append(" and alxxb.name like :name");
			f.setParam("name", "%" + name + "%");
		}
		if (StringUtils.isNotBlank(allx)) {
			f.append(" and alxxb.allx like :allx");
			f.setParam("allx", "%" + allx + "%");
		}
		if (StringUtils.isNotBlank(dsr)) {
			f.append(" and alxxb.dsr like :dsr");
			f.setParam("dsr", "%" + dsr + "%");
		}
		f.append(" order by createdTime DESC");	
		return findCount(f);

	}

	@Override
	public List<Alxxb> getAllRecord(PageBean<Alxxb> pageBean, String name, String allx, String dsr) {
		Finder f = Finder.create("from Alxxb alxxb where 1=1");

		if (StringUtils.isNotBlank(name)) {
			f.append(" and alxxb.name like :name");
			f.setParam("name", "%" + name + "%");
		}
		if (StringUtils.isNotBlank(allx)) {
			f.append(" and alxxb.allx like :allx");
			f.setParam("allx", "%" + allx + "%");
		}
		if (StringUtils.isNotBlank(dsr)) {
			f.append(" and alxxb.dsr like :dsr");
			f.setParam("dsr", "%" + dsr + "%");
		}
		f.append(" order by createdTime DESC");	

		f.setFirstResult(pageBean.getStartRow());
		f.setMaxResults(pageBean.getPageSize());

		return find(f);

	}

	@Override
	public List<Alxxb> getlistsize(String name, String allx, String dsr) {
		Finder f = Finder.create("from Alxxb alxxb where 1=1");

		if (StringUtils.isNotBlank(name)) {
			f.append(" and alxxb.name like :name");
			f.setParam("name", "%" + name + "%");
		}
		if (StringUtils.isNotBlank(allx)) {
			f.append(" and alxxb.allx like :allx");
			f.setParam("allx", "%" + allx + "%");
		}
		if (StringUtils.isNotBlank(dsr)) {
			f.append(" and alxxb.dsr like :dsr");
			f.setParam("dsr", "%" + dsr + "%");
		}
		f.append(" order by createdTime DESC");	
		return find(f);
		}
	
	

}
