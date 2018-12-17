/*
 *========================================
 * File:      LicenseValidator.java
 * Project:   Control Activa
 *
 * Author:    Silambarasan
 * Revision:  1.0
 *----------------------------------------
 * Copyright 2013 Ideas2IT
 *========================================
 */
package com.eazytec.util;

import java.util.GregorianCalendar;


import com.licensekit.LicenseHandler;
import com.licensekit.LicenseHandlerExitDelegate;
import com.eazytec.Constants;
import com.eazytec.util.DateUtil;

/**
 * 
 * When the licensing functionalities are packed by third party ( at present under com/licensekit ),
 * here we do the validation calls and radaptive specific manipulations to and fro the licensekit
 * 
 * @author Silambarasan
 *
 */
public class LicenseValidator {	
	
	/**
	 * It simply initiates the LiceseHandler class we got from jlicense kit library and checks 
	 * if the license is valid. The initiation is as per the license kit norms but with radaptive
	 * spec params
	 * 
	 * @param licenseKey which is to be validated
	 * @return license valid or not - boolean
	 * @author Silambarasan
	 * @throws Exception 
	 */
	public static boolean checkLicenseHandlerForValidity(String licenseKey) throws Exception{	
		//System.out.println("license key ceck---------"+licenseKey);
		// The following section initiates the LiceseHandler class as specified by the license kit library provider
		LicenseHandler.setExitDelegate(new LicenseHandlerExitDelegate() {			
			public void exitApplication() {			
			}
		});
		LicenseHandler.setAppName(Constants.APP_NAME);
		LicenseHandler.setModuleNames(null);
		
		// Just an int value, the current version of app, enables us to reject licenses burnt with lower version numbers		 
		LicenseHandler.setRequiredVersion(20);
		LicenseHandler.setUIEnabled(false); // It will enable UI alerts to clients in form of applets.
		LicenseHandler.setDemoModeEnabled(false);
		// This calls the license kit packed codes from the provider. We just need the result, true or false alone
		return LicenseHandler.checkLicenseString(licenseKey);		
	}
	
	/**
	 * It manipulates the no of days from now after which the license key will expire
	 * @param licenseKey
	 * @return the no of days
	 * @throws RttException when the license is not valid
	 * @author Silambarasan
	 */
	public static int getDaysToExpire(String licenseKey)throws Exception{
		try{
			GregorianCalendar expDate = LicenseHandler.getLeastExpiredDate(licenseKey);
			return DateUtil.getDaysFromNow(expDate);
		}catch (Exception e) {
			throw new Exception("Problem validating license key. Check if license is valid", e);
		}		
	}
	
	/**
	 * Checks if the license is close to expiry and warning to the user needed
	 * @param licenseKey
	 * @return 
	 * @throws RttException
	 */
	public static boolean warningNeeded(String licenseKey)throws Exception{
		try{
			int daysToExpire = LicenseValidator.getDaysToExpire(licenseKey);
			// The no of days before which we should start giving warning. This is configurable through sys config
			int daysToWarn = Integer.valueOf(PropertyReader.getInstance().getPropertyFromFile("Integer", "license.remind.before.days"));
			return daysToExpire<=daysToWarn;
		}catch (Exception e) {
			throw new Exception("Problem validating license key. Contact Administrator", e);
		}		
	}
	
	/**
	 * For dev instances, license check need not happen, and hence check if app is in dev mode
	 * @return whether license exception allowed or not
	 */
	public static boolean isDevLicenseExceptionAllowed() throws Exception{
		String appMode = PropertyReader.getInstance().getPropertyFromFile("String","deployment.mode");
		System.out.println("appl mode============"+appMode);
		return appMode.equalsIgnoreCase(Constants.APP_MODE);
	}
	
	/**
	 * For some instances only license check should happen and not for all it
	 * should  happen, like core newgen etc
	 * @return whether license exception allowed or not
	 */
	public static boolean isLicenseApplicable() throws Exception{
		String appName = PropertyReader.getInstance().getPropertyFromFile("String","application.name");		
		System.out.println("app name====="+appName+"===lic==="+isDevLicenseExceptionAllowed());
		return (appName!=null && (appName.equalsIgnoreCase(Constants.APP_NAME)) && isDevLicenseExceptionAllowed());
	}
	
}
