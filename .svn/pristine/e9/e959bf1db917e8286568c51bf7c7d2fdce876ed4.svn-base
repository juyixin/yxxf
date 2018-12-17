package com.eazytec.util;

import org.activiti.engine.impl.util.ClockUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;

import org.springframework.context.i18n.LocaleContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 *
 * @author madan
 */
public final class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);
    private static final String TIME_PATTERN = "HH:mm";

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private DateUtil() {
    }

    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        String defaultDatePattern;
        try {
        	defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
                    .getString("date.time.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = Constants.STD_DATE_FORMAT;
        }

        return defaultDatePattern;
    }

    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " HH:mm:ss.S";
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static String getDate(Date aDate) {

        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException when String doesn't match the expected format
     * @see java.text.SimpleDateFormat
     */
    public static Date convertStringToDate(String aMask, String strDate)
            throws ParseException {

        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask, Locale.UK);
        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }
    
    public static Date convertJsStringToDate(String aMask, String strDate)throws ParseException {

		SimpleDateFormat df;
		Date date;
		df = new SimpleDateFormat(aMask);
		if (log.isDebugEnabled()) {
		    log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}
		
		try {
		    date = df.parse(strDate);
		} catch (ParseException pe) {
		    //log.error("ParseException: " + pe);
		    throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		
		return (date);
		}
    

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException when String doesn't match the expected format
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {

        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime("yyyy-MM-dd HH:mm:ss", aDate);
    }
    
    public static String convertDateToStringFormat(Date aDate,String format) {
        return getDateTime(format, aDate);
    }
    
    /**
     * get the date string with server time zone
     * 
     * @param date
     * @return
     */
    public static String convertDateToEazytecFormat(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat(Constants.EAZYTEC_DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(CommonUtil.getEazytecTimeZone()));
        String eazytecDate = sdf.format(date);
        return eazytecDate;
    }
    
    /**
     * get the date string with server time zone
     * 
     * @param date
     * @return
     */
    public static Date convertDateStringToEazytecFormat(String strDate){
    	try {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		System.out.println("before parse date----"+df.parse(strDate));
            Date date = df.parse(strDate);
            df.setTimeZone(TimeZone.getTimeZone(CommonUtil.getEazytecTimeZone()));
            System.out.println("format date"+df.format(date)+"-----------"+date.toString());
	        return date;
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
    }
    
    
    /**
     * Get date object and convert into date string (in format yyyyMMddHHmmss)
     * @param aDate
     * @return
     */
    public static String convertDateToDefalutDateTimeString(Date aDate) {
    	 Locale locale = LocaleContextHolder.getLocale();
    	String defaultDateTimePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
                .getString("date.time.format.default");
        return getDateTime(defaultDateTimePattern, aDate);
    }
    
    
    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(final String strDate) throws ParseException {
        return convertStringToDate(getDatePattern(), strDate);
    }
    
    /**
     * To Convert the Date for Format : EEE MMM dd HH:mm:ss zzz yyyy to Standard Date time Format : MM/dd/yyyy HH:mm:ss string
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static String convertDefaultDateToSTDDateFormat(String dateStr) throws ParseException{
    	Date dateTime; 
    	try {
			dateTime = convertStringToDate("EEE MMM dd HH:mm:ss zzz yyyy", dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new ParseException(e.getMessage(), e.getErrorOffset());
		}
    	return convertDateToString(dateTime);
    }
    
    public static String getCurrentDateTime(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(ClockUtil.getCurrentTime());
        
    }
    
    /**
	 * The number of days still to the specified date, found as from current date, -ve if the date is in past
	 * @param calDate the date target from current date
	 * @return the no of days more, +ve if in future, -ve if in past
	 * @author madan
	 */
	public static int getDaysFromNow(GregorianCalendar calDate){
		Calendar now = Calendar.getInstance();		
		return daysDiffBetweenCals(calDate, now);
	}
	
	
	/**
	 * Finds the no of days between the two calendar dates, -ve if first is before second, else +ve
	 * @param calDate1
	 * @param calDate2
	 * @return the no of days, +ve or -ve
	 * @author madan
	 */
	public static int daysDiffBetweenCals(Calendar calDate1, Calendar calDate2){
		Calendar now = Calendar.getInstance();
		long millisecDiff = (calDate1.getTimeInMillis()-calDate2.getTimeInMillis());
		return (int)(millisecDiff/(1000 * 60 * 60 * 24));
	}
	
	/**
     * To Convert the Date for Format : EEE MMM dd HH:mm:ss zzz yyyy to Standard Date time Format : MM/dd/yyyy HH:mm:ss string
	 * @param dateTime
	 * @return
	 */
	public static String convertDateToSTDString(String dateStr) {
		String standardDate = null;
		try {
			standardDate = DateUtil.convertDefaultDateToSTDDateFormat(dateStr);
		} catch (ParseException e) {
			log.info("Date cannot be parsed for : "+dateStr);
		}
		return standardDate;
	}
}
