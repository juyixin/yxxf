package com.eazytec.bpm.oa.schedule.service;

import java.util.Calendar;
import java.util.List;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;

import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Schedule;

/**
 * Interface to provide service for scheduling the events 
 * 
 * @author mathi
 */

public interface ScheduleService {

	/**
	 * get the event by event id
	 * 
	 * @param id
	 * 
	 * @return {@link Schedule}
	 */
	public Schedule getEvent(String id);
	
	/**
	 * get all events
	 *  
	 * @param id
	 * 
	 * @return {@link List}
	 */
	public List<Schedule> getEvents();
	
	/**
	 * save event
	 * 
	 * @param schedule
	 * 
	 * @return {@link Schedule}
	 */
	public Schedule saveEvent(Schedule schedule);
		
	/**
	 * get events for particular user
	 * 
	 * @param userId
	 * 
	 * @return {@link List}
	 */
	public List<Schedule> getEventsByOwner(String userId);
	
	/**
	 * get events for reminder
	 * 
	 * @return {@link List}
	 */
	public List<Schedule> getEventsForReminder();
	
	/**
     * delete events from the database by their id
     *
     * @param eventIds
     */
    public void deleteEvents(List<String> eventIds);
    
    /**
     * delete events from the database by their id
     *
     * @param eventIds
     */
    public void deleteEvents(String eventIds);
    
    /**
     * delete event from the database by id
     *
     * @param eventId
     */
    public void deleteEvent(String eventId);
    
    /**
	 * get all events
	 * 
	 * @return {@link List}
	 */
	public List<Schedule> getAllEvents();
	
    /**
	 * get events for particular user by user id
	 * 
	 * @param userId
	 * 
	 * @return {@link List}
	 */
	public List<Schedule> getEventsByOwnerId(String userId);
	
	/**
	 * get events for particular user by user id
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * 
	 * @return {@link List}
	 */
	public List<Schedule> getEventsByOwnerId(String userId,String startDate,String endDate);
	
	/**
	 * get Schedule grid Data
	 * 
	 * @param isAdmin
	 * @return {@link List}
	 * 
	 */
	public List<Schedule> getSchedulesGridData(boolean isAdmin);
	
	/**
	 * get events json string for fullcalender
	 * 
	 * @param events
	 * 
	 * @return
	 * 
	 */
	public JSONArray getEventsJsonArray (List<Schedule> events)throws Exception,BpmException;
	
	/**
	 * get once events
	 * 
	 * @param event
	 * 
	 * @throws
	 * @return {@link List}
	 * 
	 */
	public List<JSONObject> getOnceEvents(Schedule event);
	
	/**
	 * get daily events
	 * 
	 * @param event
	 * 
	 * @throws
	 * @return {@link List}
	 */
	public List<JSONObject> getDailyEvents(Schedule event, Calendar eventStart, Calendar eventEnd);
	
	/**
	 * get weekly events
	 * 
	 * @param event
	 * 
	 * @throws
	 * @return {@link List}
	 */
	public List<JSONObject> getWeeklyEvents(Schedule event, Calendar eventStart, Calendar eventEnd);
	
	/**
	 * get monthly events
	 * 
	 * @param event
	 * @return {@link List}
	 */
	public List<JSONObject> getMonthlyEvents(Schedule event, Calendar eventStart, Calendar eventEnd);
	
	/**
	 * get event object
	 * 
	 * @param id
	 * @param title
	 * @param start
	 * @param end
	 * 
	 * @return {@link JSONObject}
	 */
	public JSONObject getEventObject(String id, String title, String start, String end);
	
	/**
	 * To get the schedule object for edit
	 * @param id
	 * @return
	 * @throws BpmException
	 */
	Schedule getEventForEdit(String id)throws BpmException;
}

