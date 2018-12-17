package com.eazytec.bpm.oa.schedule.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.oa.schedule.dao.ScheduleDao;
import com.eazytec.bpm.oa.schedule.service.ScheduleService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Schedule;
import com.eazytec.service.impl.GenericManagerImpl;

/**
 * Implementation of ScheduleService interface.
 *
 * @author mathi
 */

@Service("scheduleService")
public class ScheduleServiceImpl extends GenericManagerImpl<Schedule, String> implements ScheduleService {
	private ScheduleDao scheduleDao;

	@Autowired
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.dao = scheduleDao;
		this.scheduleDao = scheduleDao;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public Schedule getEvent(String id) {
		log.info("getting event for id "+id);
		try {
			return dao.get(id);
		} catch (ObjectRetrievalFailureException e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting event"+e.getMessage(),e);
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting event"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEvents() {
//		log.info("getting events");
		try {
			return dao.getAllDistinct();
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting events"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public Schedule saveEvent(Schedule schedule) {
		log.info("saving event "+schedule.getEventName());
		try {
			if(schedule.getVersion() == 0) {
				schedule.setCreatedBy(CommonUtil.getLoggedInUserId());
				schedule.setUpdatedBy(CommonUtil.getLoggedInUserId());
				schedule.setCreatedTime(new Date());
				schedule.setUpdatedTime(new Date());
			} else { 
				schedule.setUpdatedBy(CommonUtil.getLoggedInUserId());
				schedule.setUpdatedTime(new Date());
			}
			return dao.save(schedule);
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in saving this event"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsByOwner(String userId) {
//		log.info("getting events");
		try {
			return scheduleDao.getEventsByOwner(userId);
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting events"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsForReminder() {
//		log.info("getting events for reminder");
		try {
			return scheduleDao.getEventsForReminder();
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting events"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
    public void deleteEvents(List<String> eventIds){
    	log.info("deleting events");
    	try {
	    	for (String eventId : eventIds) {
	    		scheduleDao.remove(eventId);
			}
    	} catch (Exception e) {
    		throw new BpmException(e.getMessage(),e);
		}
    }
    
    /**
	 * {@inheritDoc}
	 * 
	 */
    public void deleteEvents(String eventIds){
    	log.info("deleting events");
    	try {
    		if(eventIds != null && !eventIds.isEmpty()){
    			if (eventIds.contains(",")) {
    				String[] ids = eventIds.split(",");
    				for(String eventId :ids){
    					scheduleDao.remove(eventId);
    				}
    			} else {
    				scheduleDao.remove(eventIds);
    			}
    		}
    	} catch (Exception e) {
    		throw new BpmException(e.getMessage(),e);
		}
    }
    
    /**
	 * {@inheritDoc}
	 * 
	 */
    public void deleteEvent(String eventId){
    	log.info("deleting event==="+eventId);
		scheduleDao.remove(eventId);
    }
    
    /**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getAllEvents() {
//		log.info("getting events");
		try {
			return scheduleDao.getAllEvents();
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting events"+e.getMessage(),e);
		}
	}
	
    /**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsByOwnerId(String userId) {
//		log.info("getting events of "+userId);
		try {
			return scheduleDao.getEventsByOwnerId(userId);
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting events"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsByOwnerId(String userId, String startDate, String endDate) {
//		log.info("getting events of "+userId);
		try {
			return scheduleDao.getEventsByOwnerId(userId, startDate, endDate);
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException("problem in getting events"+e.getMessage(),e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getSchedulesGridData(boolean isAdmin) {
		log.info("getting schedule grid datas ");
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		Set<Schedule> scheduleSet = new HashSet<Schedule>();
		try{
			if (isAdmin) {
				scheduleList=getEvents();
	       	}else{
	       		scheduleList=getEventsByOwner(CommonUtil.getLoggedInUserId());
	       	}
			if(scheduleList !=null && scheduleList.size() > 0){
				for(Schedule event:scheduleList){
					event.setAssignedUser(CommonUtil.getCommaSeparatedUser(event.getUsers()));
				}
				scheduleSet = new HashSet<Schedule>(scheduleList);
			}
		} catch (Exception e) {
			throw new BpmException("problem in getting grid datas"+e.getMessage(),e);
		}
        return new ArrayList<Schedule>(scheduleSet);
	}
	
	/**
	 * get events json string for fullcalender
	 * 
	 * @param events
	 * 
	 * @return
	 * 
	 */
	public JSONArray getEventsJsonArray (List<Schedule> events)throws Exception,BpmException{
		log.info("get json string from events list");
		List<JSONObject> eventList = new ArrayList<JSONObject>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startDate = new GregorianCalendar();
		Calendar endDate = Calendar.getInstance();
		for (Schedule event : new HashSet<Schedule>(events)) {
			startDate.setTime(formatter.parse(event.getStartDate().toString()));
			endDate.setTime(formatter.parse(event.getEndDate().toString()));
			if(event.getEventType().equals("once")){
				eventList.addAll(getOnceEvents(event));
			}else if(event.getEventType().equals("daily")){
				eventList.addAll(getDailyEvents(event, startDate, endDate));
			}else if (event.getEventType().equals("weekly")) {
				eventList.addAll(getWeeklyEvents(event, startDate, endDate));
			}else if (event.getEventType().equals("monthly")) {
				eventList.addAll(getMonthlyEvents(event, startDate, endDate));
			}
    	}
		JSONArray eventArray = new JSONArray(eventList);
		return eventArray;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<JSONObject> getOnceEvents(Schedule event){
		log.info("getting once events ");
		List<JSONObject> eventList = new ArrayList<JSONObject>();
		try{
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = formatter.format(event.getStartDate());
			String endDate = formatter.format(event.getEndDate());
			String start = startDate + " " + event.getStartTime();
			String end = endDate + " " + event.getEndTime();
			eventList.add(getEventObject(event.getId(), event.getEventName(), start, end));
		} catch (Exception e) {
			throw new BpmException("problem in getting once events"+e.getMessage(),e);
		}
        return eventList;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<JSONObject> getDailyEvents(Schedule event, Calendar eventStart, Calendar eventEnd){
		log.info("getting daily events ");
		List<JSONObject> eventList = new ArrayList<JSONObject>();
		try{
		    while ((eventStart.get(Calendar.YEAR) <= eventEnd.get(Calendar.YEAR)) && 
		    		( ( (eventStart.get(Calendar.YEAR) == eventEnd.get(Calendar.YEAR)) && ( (eventStart.get(Calendar.MONTH) < eventEnd.get(Calendar.MONTH)) || ( (eventStart.get(Calendar.MONTH) == eventEnd.get(Calendar.MONTH))) && (eventStart.get(Calendar.DATE) <= eventEnd.get(Calendar.DATE)) ) ) ) 
		    		|| ( (eventStart.get(Calendar.YEAR) < eventEnd.get(Calendar.YEAR) ) ) )
		    		 {
		    	eventList.add(getCalendarEvent(event, eventStart));
		        eventStart.add(Calendar.DATE, event.getRepeatEvery());
		    }
		} catch (Exception e) {
			throw new BpmException("problem in getting daily events"+e.getMessage(),e);
		}
	    return eventList;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<JSONObject> getWeeklyEvents(Schedule event, Calendar eventStart, Calendar eventEnd){
		log.info("getting weekly events ");
		List<JSONObject> eventList = new ArrayList<JSONObject>();
		try{
		    while ((eventStart.get(Calendar.YEAR) <= eventEnd.get(Calendar.YEAR)) && 
		    		( ( (eventStart.get(Calendar.YEAR) == eventEnd.get(Calendar.YEAR)) && ( (eventStart.get(Calendar.MONTH) < eventEnd.get(Calendar.MONTH)) || ( (eventStart.get(Calendar.MONTH) == eventEnd.get(Calendar.MONTH))) && (eventStart.get(Calendar.DATE) <= eventEnd.get(Calendar.DATE)) ) ) ) 
		    		|| ( (eventStart.get(Calendar.YEAR) < eventEnd.get(Calendar.YEAR) ) ) ) {
	    		for(int i=1; i<=7; i++){
	    			if(( ( (eventStart.get(Calendar.YEAR) == eventEnd.get(Calendar.YEAR)) && ( (eventStart.get(Calendar.MONTH) < eventEnd.get(Calendar.MONTH)) || ( (eventStart.get(Calendar.MONTH) == eventEnd.get(Calendar.MONTH))) && (eventStart.get(Calendar.DATE) <= eventEnd.get(Calendar.DATE)) ) ) )) {
			    		if(event.getDays().isSunday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.SUNDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
			    		if(event.getDays().isMonday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.MONDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
			    		if(event.getDays().isTuesday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.TUESDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
			    		if(event.getDays().isWednesday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.WEDNESDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
			    		if(event.getDays().isThursday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.THURSDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
			    		if(event.getDays().isFriday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.FRIDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
			    		if(event.getDays().isSaturday() && eventStart.get(Calendar.DAY_OF_WEEK) == Constants.SATURDAY_OF_WEEK){
			    			eventList.add(getCalendarEvent(event, eventStart));
			    		}
	    			}
		    		eventStart.add(Calendar.DATE, 1);
	    		}
		        eventStart.add(Calendar.DATE, (event.getRepeatEvery() * 7)-7);
		    }
		} catch (Exception e) {
			throw new BpmException("problem in getting weekly events"+e.getMessage(),e);
		}
	    return eventList;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<JSONObject> getMonthlyEvents(Schedule event, Calendar eventStart, Calendar eventEnd){
		log.info("getting monthly events ");
		List<JSONObject> eventList = new ArrayList<JSONObject>();
		try {
		    while ((eventStart.get(Calendar.YEAR) <= eventEnd.get(Calendar.YEAR)) && 
		    		( ( (eventStart.get(Calendar.YEAR) == eventEnd.get(Calendar.YEAR)) && ( (eventStart.get(Calendar.MONTH) < eventEnd.get(Calendar.MONTH)) || ( (eventStart.get(Calendar.MONTH) == eventEnd.get(Calendar.MONTH))) && (eventStart.get(Calendar.DATE) <= eventEnd.get(Calendar.DATE)) ) ) ) 
		    		|| ( (eventStart.get(Calendar.YEAR) < eventEnd.get(Calendar.YEAR) ) ) ) {
		    	eventList.add(getCalendarEvent(event, eventStart));
		        eventStart.add(Calendar.MONTH, event.getRepeatEvery());
		    }
		} catch (Exception e) {
			throw new BpmException("problem in getting weekly events"+e.getMessage(),e);
		}
	    return eventList;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public JSONObject getEventObject(String id, String title, String start, String end){
		log.info("get event object ");
		JSONObject eventObject=new JSONObject();
		eventObject.put("id",id);
		eventObject.put("title",title);
		eventObject.put("start",start);
		eventObject.put("end",end);
		eventObject.put("allDay", false);
        return eventObject;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public Schedule getEventForEdit(String eventId)throws BpmException{
		return scheduleDao.getEventForEdit(eventId);
	}
	
	/**
	 * create json object for render events in calendar
	 * 
	 * @param event
	 * @param eventStart
	 * @param eventStartDate
	 * @param eventStartMonth
	 * 
	 * return {@link JSONObject}
	 * 
	 */
	
	public JSONObject getCalendarEvent(Schedule event,Calendar eventStart){
		log.info("getting json event object");
		int eventStartDate = eventStart.get(Calendar.DATE);
    	int eventStartMonth = eventStart.get(Calendar.MONTH)+1;
		String startMonth = "01";
    	String startDate = "01";
    	if(eventStartMonth <= 9){
    		startMonth = "0"+eventStartMonth;
    	}else{
    		startMonth = eventStartMonth+"";
    	}
    	if(eventStartDate <= 9){
    		startDate = "0"+eventStartDate;
    	}else{
    		startDate = eventStartDate+"";
    	}
    	String start = eventStart.get(Calendar.YEAR)+"-"+startMonth+"-"+startDate+" "+event.getStartTime();
    	String end = eventStart.get(Calendar.YEAR)+"-"+startMonth+"-"+startDate+" "+event.getEndTime();
    	JSONObject eventObject = getEventObject(event.getId(), event.getEventName(), start, end);
    	return eventObject;
	}
}
