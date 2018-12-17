/**
 * File Name	:ReportDao.java
 * @author		:L.Parameswaram
 */

package com.eazytec.bpm.admin.report.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Report;

/* This contains all method declarations of a Report Class*/
public interface ReportDao extends GenericDao<Report, String>{
	
	/* To create a new report */
	@Transactional
	public Report save(Report report);

	/* To get all report values */
	@Transactional
	public List<Report> getReports();
	
	/* To get Report By Id*/
	@Transactional
	public Report getReportById( String id );
				
	/* To delete Report*/
	@Transactional
	public boolean deleteReport(Report report) throws Exception;
	
	/**
	 * Checks for a report name whether or not it is already existed.
	 * @param report name
	 *        report name to be checked
	 * @return
	 * 		boolean status about report name existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkReportNameExists(String reportName) throws EazyBpmException;
	
	public List<LabelValue> getAllReports()throws EazyBpmException;

	public String getUrlByName(String name)throws EazyBpmException;
	
	public Report getReportByName(String name) throws EazyBpmException;

}
