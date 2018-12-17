package com.eazytec.bpm.license.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.license.dao.LicenseDao;
import com.eazytec.bpm.license.service.LicenseService;
import com.eazytec.model.License;
import com.eazytec.service.impl.GenericManagerImpl;

/**
 * Implementation of license service 
 * 
 * @author ideas2it
 */
@Service("licenseService")
public class LicenseServiceImpl extends GenericManagerImpl<License, String>  implements LicenseService {

	private LicenseDao licenseDao;
	
	@Autowired
	public void setLicenseDao(LicenseDao licenseDao){
		this.licenseDao = licenseDao;
	}
	
	/**
	 * Implementation of service method
	 * 
	 * @param License object
	 * @author ideas2it
	 * @throws Exception
	 * @return license
	 */
	public License saveLicense(License license) throws Exception {
		return licenseDao.save(license);
	}
}
