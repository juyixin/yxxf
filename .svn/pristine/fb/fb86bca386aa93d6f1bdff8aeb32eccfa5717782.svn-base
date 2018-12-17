/**
 * File Name	:ReportDaoImpl.java
 * @author		:L.Parameswaram
 */

package com.eazytec.bpm.admin.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.Query;

import com.eazytec.bpm.admin.report.dao.ReportDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Report;


/**
 * <p>This class interacts with Hibernate session to save/delete,
 * retrieve Report objects.</p>
 */

@Repository("reportDao")
public class ReportDaoImpl extends GenericDaoHibernate<Report, String> implements ReportDao {
	
	/**
     * Constructor that sets the entity to Report.class.
     */
	public ReportDaoImpl() {
        super(Report.class);
    }
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean checkReportNameExists(String reportName) throws EazyBpmException {
		
		Query qry = getSession()
				.createQuery(
						"select report from Report report where lower(report.name) = '"+reportName+"' ");
		@SuppressWarnings("unchecked")
		List<Report> reports = qry.list();

		return (reports != null && !reports.isEmpty()) ? true:false;
	}
	
	@Override
	public Report save(Report report) throws EazyBpmException {
		if (log.isDebugEnabled()) {
            log.debug("Report id: " + report.getId());
        }
        getSession().saveOrUpdate(report);
        getSession().flush();
        return report;
	}
	
	/**
     * To get All report values from the database
     */
	
	@Override
	public List<Report> getReports() {
	
		Query qry = getSession().createQuery("from Report report");
		@SuppressWarnings("unchecked")
		List<Report> reports = (List<Report>) qry.list();
		if(reports.isEmpty()){
			return null;
		}	else	{
			return reports;
		}
	} 
    
    public Report getReportById( String id ) {
		
    	Report report = (Report) getSession().createQuery("from Report as Report where Report.id ='"+id+"'").list().get(0);
    	return report;
    	
    	
    }
       
    
    @Override
	public boolean deleteReport(Report report) throws Exception{
    	try {
    		getSession().delete(report);
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
		
	}


    public List<LabelValue> getAllReports()throws EazyBpmException {
    	List<LabelValue> reports = getSession().createQuery("select new com.eazytec.model.LabelValue(report.name as name,report.id as id)from Report as report where report.status = 'active' order by report.name").list();
		
		if(reports.isEmpty()){
			return null;
		}	else	{
			return reports;
		}
    	/*List<LabelValue> reports = getSession().createQuery(
				"select new com.eazytec.model.LabelValue(report.report as report,report.id as id) from Report as report")
		.list();
        if (reports.isEmpty()) {
            return null;
        } else {
            return reports;
        }*/
    }


	@Override
	public String getUrlByName(String name) throws EazyBpmException {
		String reportUrl = (String) getSession().createQuery("select report.reportUrl as reportUrl from Report as report where report.name ='"+name+"'").list().get(0);
    	
		return reportUrl;
	}
	
	public Report getReportByName(String name) throws EazyBpmException {
		
		Report report = (Report) getSession().createQuery("from Report as Report where Report.name ='"+name+"'").list().get(0);
    	return report;
		
	}

}
