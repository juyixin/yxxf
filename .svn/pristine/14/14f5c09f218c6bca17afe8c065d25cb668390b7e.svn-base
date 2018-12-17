package com.eazytec.bpm.runtime.holiday.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.runtime.holiday.dao.HolidayDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Holiday;

@Repository("holidayDao")
public class HolidayDaoImpl extends GenericDaoHibernate<Holiday, String> implements HolidayDao {

	public HolidayDaoImpl() {
		super(Holiday.class);
	}
	
    /**
     * {@inheritDoc}
     */
    public Holiday saveOrUpdate(Holiday holiday) {
        if (log.isDebugEnabled()) {
            log.debug("holiday's id: " + holiday.getId());
        }
        getSession().saveOrUpdate(holiday);
        getSession().flush();
        return holiday;
    }
    
    public List<Date> getHolidayByDates(List<Date> dates){
    	List<Date> holidays = getSession().createQuery("select holiday.holiday from Holiday as holiday where holiday.holiday in (:list)").setParameterList("list", dates).list();
    	return holidays;
    }
    
    public List<Holiday> getAllHolidaysByYear(int year){
    	List<Holiday> holidays = getSession().createQuery("select holiday from Holiday as holiday where holiday.year="+year).list();
    	return holidays;
    }
    
    public void deletePreviousHolidays(int currentYear){
    	Query deletePreviousHolidays=getSession().createQuery("Delete from Holiday as holiday  where holiday.year="+currentYear);
    	deletePreviousHolidays.executeUpdate();
    }
    
    public Date getHolidayByDate(String date){
    	List<Date> holidays = getSession().createQuery("select holiday.holiday from Holiday as holiday where holiday.holiday='"+date+"'").list();
    	if(holidays.size() > 0){
    		return holidays.get(0);
    	}
    	return null;
    }
}
