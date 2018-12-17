package com.eazytec.bpm.oa.schedule.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.oa.schedule.dao.ScheduleDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Schedule;
import com.eazytec.model.User;

/**
 * Implementation of ScheduleDao interface.
 *
 * @author mathi
 */

@Repository("scheduleDao")
public class ScheduleDaoImpl extends GenericDaoHibernate<Schedule, String> implements ScheduleDao {

	public String dataBaseSchema;

	public String getDataBaseSchema() {
		return dataBaseSchema;
	}

	public void setDataBaseSchema(String dataBaseSchema) {
		this.dataBaseSchema = dataBaseSchema;
	}
	
	/**
	 * Constructor to create a Generics-based version using Schedule as the entity
	 */
	public ScheduleDaoImpl() {
		super(Schedule.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsByOwner(String userId) {
		/*List<Schedule> schedules = getSession().createCriteria(Schedule.class)
		.add(Restrictions.eq("eventOwner", eventOwner)).list();*/
		List<Schedule> schedules = (List<Schedule>) getSession().createQuery("select schedule from Schedule as schedule join schedule.users as user where user.id = '"+userId+"' order by schedule.createdTime  desc").list();
		if (schedules.isEmpty()) {
			return null;
		} else {
			return schedules;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsForReminder() {
		List<Schedule> schedules = getSession().createCriteria(Schedule.class)
		.add(Restrictions.le("endDate", new Date())).list();
		if (schedules.isEmpty()) {
			return null;
		} else {
			return schedules;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getAllEvents() {
		@SuppressWarnings("unchecked")
		List<Schedule> schedules = (List<Schedule>)getSession().createQuery("select new com.eazytec.model.Schedule(schedule.id as id,schedule.eventName as eventName,schedule.description as description,schedule.location as location,schedule.eventType as eventType,schedule.startDate as startDate,schedule.endDate as endDate,schedule.startTime as startTime,schedule.endTime as endTime,schedule.remindBefore as remindBefore,schedule.remindTimeType as remindTimeType,schedule.repeatEvery as repeatEvery,schedule.days.sunday as sunday, schedule.days.monday as monday, schedule.days.tuesday as tuesday, schedule.days.wednesday as wednesday, schedule.days.thursday as thursday, schedule.days.friday as friday, schedule.days.saturday as saturday, user.id as assignedUser) from Schedule as schedule join schedule.users as user").list();
		if (schedules.isEmpty()) {
			return null;
		} else {
			return schedules;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<Schedule> getEventsByOwnerId(String userId) {
		@SuppressWarnings("unchecked")
		List<Schedule> schedules = (List<Schedule>)getSession().createQuery("select new com.eazytec.model.Schedule(schedule.id as id,schedule.eventName as eventName,schedule.description as description,schedule.location as location,schedule.eventType as eventType,schedule.startDate as startDate,schedule.endDate as endDate,schedule.startTime as startTime,schedule.endTime as endTime,schedule.remindBefore as remindBefore,schedule.remindTimeType as remindTimeType,schedule.repeatEvery as repeatEvery, schedule.days.sunday as sunday, schedule.days.monday as monday, schedule.days.tuesday as tuesday, schedule.days.wednesday as wednesday, schedule.days.thursday as thursday, schedule.days.friday as friday, schedule.days.saturday as saturday, user.id as assignedUser) from Schedule as schedule join schedule.users as user where user.id = '"+userId+"'").list();
		if (schedules.isEmpty()) {
			return null;
		} else {
			return schedules;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> getEventsByOwnerId(String userId,String startDate, String endDate) {
		List<Schedule> schedules = new ArrayList<Schedule>();
		if(dataBaseSchema.equalsIgnoreCase("oracle")) {
			 schedules = (List<Schedule>)getSession().createQuery("select new com.eazytec.model.Schedule(schedule.id as id,schedule.eventName as eventName,schedule.description as description,schedule.location as location,schedule.eventType as eventType,schedule.startDate as startDate,schedule.endDate as endDate,schedule.startTime as startTime,schedule.endTime as endTime,schedule.remindBefore as remindBefore,schedule.remindTimeType as remindTimeType,schedule.repeatEvery as repeatEvery, schedule.days.sunday as sunday, schedule.days.monday as monday, schedule.days.tuesday as tuesday, schedule.days.wednesday as wednesday, schedule.days.thursday as thursday, schedule.days.friday as friday, schedule.days.saturday as saturday, user.id as assignedUser) from Schedule as schedule join schedule.users as user where (schedule.startDate<=TO_DATE('"+endDate+"','yyyy-MM-dd') and  schedule.endDate >= TO_DATE('"+startDate+"','yyyy-MM-dd')) and user.id = '"+userId+"'").list(); 
		 } else {
			 schedules = (List<Schedule>)getSession().createQuery("select new com.eazytec.model.Schedule(schedule.id as id,schedule.eventName as eventName,schedule.description as description,schedule.location as location,schedule.eventType as eventType,schedule.startDate as startDate,schedule.endDate as endDate,schedule.startTime as startTime,schedule.endTime as endTime,schedule.remindBefore as remindBefore,schedule.remindTimeType as remindTimeType,schedule.repeatEvery as repeatEvery, schedule.days.sunday as sunday, schedule.days.monday as monday, schedule.days.tuesday as tuesday, schedule.days.wednesday as wednesday, schedule.days.thursday as thursday, schedule.days.friday as friday, schedule.days.saturday as saturday, user.id as assignedUser) from Schedule as schedule join schedule.users as user where (schedule.startDate<='"+endDate+"' and  schedule.endDate >='"+startDate+"') and user.id = '"+userId+"'").list();
		 }
//		log.info("size of list=="+schedules.size());
		if (schedules.isEmpty()) {
			return null;
		} else {
			return schedules;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public Schedule getEventForEdit(String eventId)throws BpmException{
		@SuppressWarnings("unchecked")
		//List<Schedule> schedules = (List<Schedule>)getSession().createQuery("select new com.eazytec.model.Schedule(schedule.id as id,schedule.eventName as eventName,schedule.description as description,schedule.location as location,schedule.eventType as eventType,schedule.startDate as startDate,schedule.endDate as endDate,schedule.startTime as startTime,schedule.endTime as endTime,schedule.remindBefore as remindBefore,schedule.remindTimeType as remindTimeType,schedule.repeatEvery as repeatEvery, schedule.days.sunday as sunday, schedule.days.monday as monday, schedule.days.tuesday as tuesday, schedule.days.wednesday as wednesday, schedule.days.thursday as thursday, schedule.days.friday as friday, schedule.days.saturday as saturday,schedule.userNames as userNames,schedule.createdBy as createdBy,schedule.updatedBy as updatedBy,schedule.createdTime as createdTime,schedule.updatedTime as updatedTime) from Schedule as schedule where schedule.id='"+eventId+"'").list();
		List<Schedule> schedules = (List<Schedule>)getSession().createQuery(" from Schedule as schedule where schedule.id='"+eventId+"'").list();
		if (schedules.isEmpty()) {
			return null;
		} else {
			return schedules.get(0);
		}
	}
	
	
}
