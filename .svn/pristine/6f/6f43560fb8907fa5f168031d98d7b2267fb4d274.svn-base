package com.eazytec.bpm.runtime.holiday.dao;

import java.util.Date;
import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Holiday;

public interface HolidayDao extends GenericDao<Holiday, String> {
	
	Holiday saveOrUpdate(Holiday holiday)throws EazyBpmException;
	
	List<Date> getHolidayByDates(List<Date> dates);
	
	List<Holiday> getAllHolidaysByYear(int year);
	
	void deletePreviousHolidays(int currentYear);
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	Date getHolidayByDate(String date);
}
