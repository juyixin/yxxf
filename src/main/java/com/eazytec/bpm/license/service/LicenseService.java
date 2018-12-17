package com.eazytec.bpm.license.service;

import com.eazytec.model.License;

/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author ideas2it
 */
public interface LicenseService {
	
	/**
	 * to handle license object and save license information
	 * 
	 * @param license
	 * @return license object
	 * @throws Exception
	 */
	License saveLicense(License license) throws Exception;
		
}
