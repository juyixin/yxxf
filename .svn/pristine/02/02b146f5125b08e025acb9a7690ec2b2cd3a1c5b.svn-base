/**
 * ========================
 * File Name  : ReportService.java
 * @author   : L.Parameswaran
 * ========================
 */

package com.eazytec.bpm.admin.report.service;

import java.util.List;





import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Report;
import com.eazytec.model.LabelValue;



public interface ReportService {
	
	/* To create a new report */
	public Report save(Report report) throws EazyBpmException;
	 
	 /* To retrieve all Report Details */
	List<Report> getReports();
	
	public Report getReportById( String id ) throws EazyBpmException;
	
	
	public boolean deleteReport(List<Report> reportList);
	
	
	/**
	 * Checks for a report Name whether or not it is already existed.
	 * @param reportName
	 *        report Name to be checked
	 * @return
	 * 		boolean status about reportName existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkReportNameExists(String reportName) throws EazyBpmException;
	
	public List<LabelValue> getAllReports()throws EazyBpmException;
	
	public String getUrlByName(String name) throws EazyBpmException;
	
	public Report getReportByName(String name) throws EazyBpmException;
	
}
