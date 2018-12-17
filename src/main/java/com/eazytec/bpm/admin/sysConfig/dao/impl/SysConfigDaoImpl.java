package com.eazytec.bpm.admin.sysConfig.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.sysConfig.dao.SysConfigDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.SysConfig;

/**
 * 
 * @author Sangeetha.G
 * 
 */
@Repository("sysConfigDao")
public class SysConfigDaoImpl extends GenericDaoHibernate<SysConfig, Long>
		implements SysConfigDao {

	public SysConfigDaoImpl() {
		super(SysConfig.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public SysConfig addNewsysConfig(SysConfig sysConfig)
			throws EazyBpmException {
		if (log.isDebugEnabled()) {
			// log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(sysConfig);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return sysConfig;
	}

	@Override
	public SysConfig saveSysConfig(SysConfig sysConfig) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			// log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(sysConfig);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return sysConfig;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SysConfig getSysConfigByKey(String key) throws Exception {
		List sysconfig = getSession().createCriteria(SysConfig.class)
				.add(Restrictions.eq("selectKey", key)).list();
		if (sysconfig.isEmpty()) {
			return null;
		} else {
			return (SysConfig) sysconfig.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SysConfig getId(String id) {
		List sysconfig = getSession().createCriteria(SysConfig.class)
				.add(Restrictions.eq("id", id)).list();
		if (sysconfig.isEmpty()) {
			return null;
		} else {
			return (SysConfig) sysconfig.get(0);
		}
	}
	
		
	
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<SysConfig> getSysConfigById(List<String> id) {
    	List<SysConfig> sysConfigs = getSession().createQuery("from SysConfig as sysConfig where sysConfig.id in (:list)").setParameterList("list", id).list();
    	if (sysConfigs.isEmpty()) {
            return null;
        } else {
            return sysConfigs;
        }
    }	
    
    
    

    /**
     * {@inheritDoc}
     */
	public void removeSysConfig(String name) {
        SysConfig sysConfig = getId(name);
        Session session = getSessionFactory().getCurrentSession();
        session.delete(sysConfig);
    }
	
	
	
	
	/**
	 * {@inheritDoc checkListViewName}
	 */
	public boolean checkSysConfigKey(String checkName)throws EazyBpmException{
		@SuppressWarnings("unchecked")
		List<SysConfig> sysConfig = getSession().createCriteria(SysConfig.class).add(Restrictions.eq("selectKey", checkName)).list();
		if (sysConfig.isEmpty()) {
			return false;
		}else{
			return true;
		}
	}


    

	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<SysConfig> getAllSysConfig() {

		Query qry = getSession().createQuery("from SysConfig sysconfig");
		@SuppressWarnings("unchecked")
		List<SysConfig> sysconfigs = (List<SysConfig>) qry.list();

		if (sysconfigs.isEmpty()) {
			return null;
		} else {
			return sysconfigs;
		}
	}

}
