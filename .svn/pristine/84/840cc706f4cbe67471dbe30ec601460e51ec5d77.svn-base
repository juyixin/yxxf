/*
 *========================================
 * File:      CalendarUtil.java
 * Project:   Control Activa
 *
 * Author:    Silambarasan
 * Revision:  1.0
 *----------------------------------------
 * Copyright 2013 Ideas2IT
 *========================================
 */

package com.eazytec.common.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class CalendarUtil {

	/*public static CalendarEntry createCalendarEntry(List entryList,Timezone timezone) throws RttException{
		CalendarEntry returnedCalendar = null;
		try{
			
			CalendarService myService = new CalendarService("exampleCo-exampleApp-1");
			String username = PropertyReader.getInstance().getPropertyFromFile("CRCALENDAR",
											"CALENDAR_ACCOUNT");
			String password = PropertyReader.getInstance().getPropertyFromFile("CRCALENDAR",
											"CALENDAR_PASSWORD");
			myService.setUserCredentials(username, password);
			URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
			StopWatch sw = new StopWatch();
			sw.start();
			CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
			for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			  CalendarEntry entry = resultFeed.getEntries().get(i);
			  String calTitle = entry.getTitle().getPlainText();
			  if(calTitle.equalsIgnoreCase("Change Request Calendar")){
				  returnedCalendar = resultFeed.getEntries().get(i);
				  returnedCalendar.setTimeZone(new TimeZoneProperty(timezone.getId()));
				  returnedCalendar = returnedCalendar.update();
				  break;
			  }
			}
			
			 Delete all the cr calendar event entries if the database is reset 
			String calId = returnedCalendar.getId().substring(returnedCalendar.getId().lastIndexOf("/")+1);
			if(entryList != null && entryList.size() == 0){
				URL postURL = new URL("http://www.google.com/calendar/feeds/"+calId+"/private/full");
				CalendarQuery  myQuery = new CalendarQuery(postURL);
			    myQuery.setMaxResults(100000);
			    CalendarEventFeed entryFeed = myService.query(myQuery, CalendarEventFeed.class);
				if(entryFeed != null && entryFeed.getEntries().size() > 0){
					for (int j = 0; j < entryFeed.getEntries().size(); j++) {
						CalendarEventEntry entry = entryFeed.getEntries().get(j);
						entry.delete();
					}
				}
			}
			sw.stop();

		}catch(Exception e){
			e.printStackTrace();
			throw new RttException(e.getMessage());
		}
		return returnedCalendar;
	}*/
	
	/*public static CalendarEntry createScheduleCalendarEntry(List entryList,Timezone timezone) throws RttException{
		CalendarEntry returnedCalendar = null;
		try{
			
			CalendarService myService = new CalendarService("exampleCo-exampleApp-1");
			String username = PropertyReader.getInstance().getPropertyFromFile("CRCALENDAR",
									"CALENDAR_ACCOUNT");
			String password = PropertyReader.getInstance().getPropertyFromFile("CRCALENDAR",
									"CALENDAR_PASSWORD");
			myService.setUserCredentials(username, password);
			URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
			CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
			logger.info("Your calendars:"+resultFeed.getEntries().size());			
			for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			  CalendarEntry entry = resultFeed.getEntries().get(i);
			  String calTitle = entry.getTitle().getPlainText();
			  if(calTitle.equalsIgnoreCase("Oncall Schedule Calendar")){
				  returnedCalendar = resultFeed.getEntries().get(i);
				  returnedCalendar.setTimeZone(new TimeZoneProperty(timezone.getId()));
				  returnedCalendar = returnedCalendar.update();
			  }
			}
			if(returnedCalendar == null){
				CalendarEntry calendar = new CalendarEntry();
				calendar.setCanEdit(true);
				calendar.setTitle(new PlainTextConstruct("Oncall Schedule Calendar"));
				calendar.setSummary(new PlainTextConstruct("This calendar contains the Oncall Schedule data details."));
				calendar.setTimeZone(new TimeZoneProperty(timezone.getId()));
				calendar.setColor(new ColorProperty("#2952A3"));
//					 Insert the calendar
				URL postUrl = new URL("http://www.google.com/calendar/feeds/default/owncalendars/full");
				returnedCalendar = myService.insert(postUrl, calendar);
				
			}
			String calId = returnedCalendar.getId().substring(returnedCalendar.getId().lastIndexOf("/")+1);
			logger.info("------calId------->"+calId);
			
			
			URL postURL = new URL("http://www.google.com/calendar/feeds/"+calId+"/private/full");
			CalendarQuery  myQuery = new CalendarQuery(postURL);
		    myQuery.setMaxResults(100000);
		    CalendarEventFeed entryFeed = myService.query(myQuery, CalendarEventFeed.class);
			if(entryFeed.getEntries().size() > 0){
				for (int j = 0; j < entryFeed.getEntries().size(); j++) {
					CalendarEventEntry entry = entryFeed.getEntries().get(j);
					entry.delete();
				}
			}
			for(int i=0;i < entryList.size();i++){
				CalendarEventEntry myEvent = new CalendarEventEntry();
				RadaptiveDTO onCallScheduleObj = (RadaptiveDTO) entryList.get(i);
				String assignee = onCallScheduleObj.getValue("assignee").toString();
				myEvent.setTitle(new PlainTextConstruct(assignee));
				myEvent.setContent(new PlainTextConstruct(onCallScheduleObj.getValue("assignedGroup").toString()));
				String fromtime = onCallScheduleObj.getValue("fromtime").toString();
				fromtime = fromtime.substring(0, fromtime.indexOf("."));
				String totime = onCallScheduleObj.getValue("totime").toString();
				totime = totime.substring(0, totime.indexOf("."));
				DateTime startTime = DateTime.parseDateTime(fromtime);
				DateTime endTime = DateTime.parseDateTime(totime);
				When eventTimes = new When();
				eventTimes.setStartTime(startTime);
				eventTimes.setEndTime(endTime);
				myEvent.addTime(eventTimes);
				CalendarEventEntry insertedEntry = myService.insert(postURL, myEvent);
			}		

		}catch(Exception e){
			e.printStackTrace();
			throw new RttException(e.getMessage());
		}
		return returnedCalendar;
	}*/
	
	/**
	 * Finds the no of days between the two calendar dates, -ve if first is before second, else +ve
	 * @param calDate1
	 * @param calDate2
	 * @return the no of days, +ve or -ve
	 * @author madan
	 */
	public static int daysDiffBetweenCals(Calendar calDate1, Calendar calDate2){
		Calendar now = Calendar.getInstance();
		System.out.println("cal now dates====="+calDate1.getTime().getDate()+"==cal date 2======"+calDate2.getTime().getDate());
		long millisecDiff = (calDate1.getTimeInMillis()-calDate2.getTimeInMillis());
		return (int)(millisecDiff/(1000 * 60 * 60 * 24));
	}
	
	/**
	 * The number of days still to the specified date, found as from current date, -ve if the date is in past
	 * @param calDate the date target from current date
	 * @return the no of days more, +ve if in future, -ve if in past
	 * @author madan
	 */
	public static int getDaysFromNow(GregorianCalendar calDate){
		Calendar now = Calendar.getInstance();
		System.out.println("date format in now======yeear=="+now.getTime().getYear()+"==mon=="+now.getTime().getMonth()+"==day=="+now.getTime().getDay()+now.getTime().getHours());
		return daysDiffBetweenCals(calDate, now);
	}

}
