/**
 * ========================
 * File Name  : ReportServiceImpl.java
 * @author   : L.Parameswaran
 * ========================
 */

package com.eazytec.bpm.admin.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.report.dao.ReportDao;
import com.eazytec.bpm.admin.report.service.ReportService;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Report;

/* Report Service Implementation implements ReportService Methods*/

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	
	private ReportDao reportDao;
	
	@Autowired
	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
		
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report save(Report report)
			throws EazyBpmException {
		Report reportObj = reportDao.save(report);
		return reportObj;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean checkReportNameExists(String reportName) throws EazyBpmException {
		
		return reportDao.checkReportNameExists(reportName);
	}
	
	@Override
	public List<Report> getReports() {
		
		return reportDao.getReports();
			
	}
	
	@Override
	public Report getReportById( String id ) {
		
		return reportDao.getReportById(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteReport(List<Report> reportList) {
		boolean isDeleted = false;
		try{
			if(reportList != null && !reportList.isEmpty()) {
				for(Report report:reportList) {
					if(report != null) {
						isDeleted = reportDao.deleteReport(report);
					} else {
						isDeleted = false;
					}
				}
			}
			
		}catch(Exception ex){
			isDeleted = false;
		}
		return isDeleted;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllReports()throws EazyBpmException{
		return reportDao.getAllReports();
	}

	@Override
	public String getUrlByName(String name) throws EazyBpmException {
		// TODO Auto-generated method stub
		return reportDao.getUrlByName(name);
	}

	@Override
	public Report getReportByName(String name) throws EazyBpmException {
		// TODO Auto-generated method stub
		return reportDao.getReportByName(name);
	}
	
}
