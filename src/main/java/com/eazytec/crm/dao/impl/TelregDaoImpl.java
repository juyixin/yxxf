package com.eazytec.crm.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.crm.dao.TelregDao;
import com.eazytec.crm.model.Record;
import com.eazytec.crm.model.Telreg;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
@Repository("telregDao")
public class TelregDaoImpl extends GenericDaoHibernate<Telreg, String> implements TelregDao{
	
	public TelregDaoImpl() {
		super(Telreg.class);
	}

	@Override
	public List<Telreg> getAlltelregList(String num,String telnum) {
		StringBuilder sb = new StringBuilder("from Telreg u where 1=1");
	
		if(StringUtils.isNotBlank(num)){
			sb.append(" and u.num like :num");
		}
		if(StringUtils.isNotBlank(telnum)){
			sb.append(" and u.telnum like :telnum");
		}
			
		
		sb.append(" order by u.createdTime desc");
		
		Query qry = getSession().createQuery(sb.toString());
		
		
		if(StringUtils.isNotBlank(num)){
			qry.setParameter("num", "%"+num+"%" );
		}
		if(StringUtils.isNotBlank(telnum)){
			qry.setParameter("telnum", "%"+telnum+"%");
			
		}
		return qry.list();
	}

	
	public Telreg saveTelreg(Telreg telreg) {
		return save(telreg);
	}

	
	public List<Telreg> getTelregsByIds(List<String> ids) {
		List<Telreg> telregs = getSession().createQuery("from Telreg as telreg where telreg.id in (:list)").setParameterList("list", ids).list();
        if (telregs.isEmpty()) {
            return null;
        } else {
            return telregs;
        }
	}

	public void deleteTelreg(Telreg telreg) {
		getSession().delete(telreg);		
	}
}
