package com.eazytec.bpm.license.dao;

import com.eazytec.dao.GenericDao;
import com.eazytec.model.License;
import com.eazytec.exceptions.BpmException;

/**
 * License Data Access Object interface.
 *
 * @author ideas2it
 */
public interface LicenseDao extends GenericDao<License, String> {
	/**
	 * Save license information
	 * 
	 * @author ideas2it
	 */
	License save(License license) throws BpmException;
}
