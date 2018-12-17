package com.eazytec.bpm.oa.schedule.dao;

import java.util.Date;
import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Schedule;
import com.eazytec.model.User;

/**
 * Interface to retrieve/persist schedule events to database.
 * 
 * @author mathi
 */
public interface ScheduleDao extends GenericDao<Schedule, String>{
	
	/**
	 * get events for particular user
	 * 
	 * @param eventOwner
	 * 
	 * @return
	 */
	public List<Schedule> getEventsByOwner(String userId);
	
	/**
	 * get events for reminder
	 * 
	 * @param eventOwner
	 * 
	 * @return
	 */
	public List<Schedule> getEventsForReminder();
	
	/**
	 * get all events
	 * 
	 * 
	 * @return
	 */
	public List<Schedule> getAllEvents();
	
	/**
	 * get events for particular user by user id
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	public List<Schedule> getEventsByOwnerId(String userId);
	
	/**
	 * get events for particular user by user id
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * 
	 * @return
	 */
	public List<Schedule> getEventsByOwnerId(String userId, String startDate, String endDate);
	
	/**
	 * To get the schedule object for edit
	 * @param id
	 * @return
	 * @throws BpmException
	 */
	Schedule getEventForEdit(String id)throws BpmException;
	
}
