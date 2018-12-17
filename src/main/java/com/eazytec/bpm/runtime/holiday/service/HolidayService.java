package com.eazytec.bpm.runtime.holiday.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Holiday;
import com.eazytec.service.GenericManager;

public interface HolidayService extends GenericManager<Holiday, String>  {
	
	/**
	 * To save or update holidays
	 * @param holiday
	 * @return
	 */
	Holiday saveOrUpdate(Holiday holiday);
	
	List<Date> getHolidayByDates(List<Date> dates);
	
	/**
	 * To get the holidays for selected year 
	 * @param year
	 * @return
	 */
	Map<String,Object> getAllHolidaysByYear(int year);
	
	/**
	 * To save the holidays for a year
	 * @param currentYear
	 * @param selectedDateList
	 * @param selectedWeekends
	 * @return
	 * @throws EazyBpmException
	 */
	Map<String,Object> saveOrUpdate(int currentYear,Map<String,Object> selectedDateList,String selectedWeekends)throws EazyBpmException;
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	Date getHolidayByDate(String date);
}
