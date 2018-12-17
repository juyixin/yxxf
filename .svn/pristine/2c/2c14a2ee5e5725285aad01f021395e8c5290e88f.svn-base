package com.eazytec.bpm.license.dao.impl;

import org.springframework.stereotype.Repository;

import com.eazytec.model.License;
import com.eazytec.bpm.license.dao.LicenseDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.BpmException;


@Repository("licenseDao")
public class LicenseDaoImpl extends GenericDaoHibernate<License, String> implements LicenseDao {
	public LicenseDaoImpl() {
		super(License.class);
	}
	
	@Override
	public License save(License license) throws BpmException{
		getSession().saveOrUpdate(license);
		return license;
	}
	
}
