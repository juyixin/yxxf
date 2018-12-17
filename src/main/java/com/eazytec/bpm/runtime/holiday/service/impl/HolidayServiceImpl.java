package com.eazytec.bpm.runtime.holiday.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.runtime.holiday.dao.HolidayDao;
import com.eazytec.bpm.runtime.holiday.service.HolidayService;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Holiday;
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.DateUtil;

@Service("holidayService")
public class HolidayServiceImpl extends GenericManagerImpl<Holiday, String> implements HolidayService {

	
	private HolidayDao holidayDao;
	
	@Autowired
	public void setHolidayDao(HolidayDao holidayDao) {
		this.holidayDao = holidayDao;
	}

	public Holiday saveOrUpdate(Holiday holiday) {
		holidayDao.saveOrUpdate(holiday);
		return holiday;
	}
	
	public List<Date> getHolidayByDates(List<Date> dates){
		return holidayDao.getHolidayByDates(dates);
	}
	
	public Map<String,Object> getAllHolidaysByYear(int year){
		List<Holiday>  holidayDateObj=holidayDao.getAllHolidaysByYear(year);
		Map<String,Object> holidays = new HashMap<String, Object>();
		for(Holiday holidayObj:holidayDateObj){
			String[] holidaysWithTimeArray=DateUtil.convertDateToString(holidayObj.getHoliday()).split(" ");
			String[] holidayArray=holidaysWithTimeArray[0].split("-");
			int holidayMonth=Integer.parseInt(holidayArray[1])-1;
			/*if(holidayMonth<10){
				System.out.println("============"+holidayArray[0]+"-"+holidayArray[1]+"-"+holidayArray[2]+"============"+holidayArray[0]+"-0"+holidayMonth+"-"+holidayArray[2]);
				holidays.add(holidayArray[0]+"-0"+holidayMonth+"-"+holidayArray[2]);
			}else{
				System.out.println("============"+holidayArray[0]+"-"+holidayArray[1]+"-"+holidayArray[2]+"============"+holidayArray[0]+"-"+holidayMonth+"-"+holidayArray[2]);
				holidays.add(holidayArray[0]+"-"+holidayMonth+"-"+holidayArray[2]);
			}*/
			holidays.put(holidayArray[0]+"-"+holidayMonth+"-"+Integer.parseInt(holidayArray[2]),holidayObj.getDescription());
			holidays.put("weekEnds", holidayObj.getWeekEnd());
		}
		
		return holidays;
	}
	
	public Map<String,Object> saveOrUpdate(int currentYear,Map<String,Object> selectedDateList,String selectedWeekends) throws EazyBpmException {
		Map<String,Object> selectedDateForThisYear=new HashMap<String, Object>();
		holidayDao.deletePreviousHolidays(currentYear);
		Set<String> KeyDates=selectedDateList.keySet();
		for(String KeyDate:KeyDates){
			String selectedDateCopy=KeyDate;
			String[] selectedSplitedDate=selectedDateCopy.split("-");
			int selectedYear=Integer.parseInt(selectedSplitedDate[0]);
			int selectedMonth=Integer.parseInt(selectedSplitedDate[1])+1;
			int selectedDate=Integer.parseInt(selectedSplitedDate[2]);
			
			try {
				if(selectedYear==currentYear){
					selectedDateForThisYear.put(KeyDate, selectedDateList.get(KeyDate));
					Date selectedDateObj=DateUtil.convertStringToDate("yyyy-MM-dd", selectedYear+"-"+selectedMonth+"-"+selectedDate);
					Holiday holidayObj=null;
					if(selectedWeekends!="" && selectedWeekends!=null){
						holidayObj=new Holiday(selectedDateObj, String.valueOf(selectedDateList.get(KeyDate)), currentYear,selectedWeekends);	
					}else{
						holidayObj=new Holiday(selectedDateObj, String.valueOf(selectedDateList.get(KeyDate)), currentYear,null);
					}
					holidayDao.saveOrUpdate(holidayObj);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedDateForThisYear;
	}
	
	public Date getHolidayByDate(String date){
		return holidayDao.getHolidayByDate(date);
	}
}
