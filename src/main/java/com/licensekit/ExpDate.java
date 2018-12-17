package com.licensekit;
import java.util.StringTokenizer;
import java.text.DecimalFormat;

public class ExpDate
{
   public static final int BASE_YEAR = 1999;
   private int day;
   private int month;
   private int year;
   
   public ExpDate()
   {
   }
   
   public ExpDate (int aYear, int aMonth, int aDay) throws NumberFormatException
   {
      this();
      
      if ((aYear == 0) && (aMonth == 0) && (aDay == 0))
         return;
      
      setValues(aYear, aMonth, aDay);
   }
   
   public ExpDate (String yearMonthDayString) throws NumberFormatException
   {
      this();
      
      if (yearMonthDayString.equals("-"))
         return;
      
      StringTokenizer st = new StringTokenizer(yearMonthDayString);
      
      setValues(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
   }
   
   private void setValues (int aYear, int aMonth, int aDay) throws NumberFormatException
   {
      if ((aYear < BASE_YEAR) || (aYear > BASE_YEAR + 31))
         throw new NumberFormatException("illegal year");
      if ((aMonth < 1) || (aMonth > 12))
         throw new NumberFormatException("illegal month");
      if ((aDay < 1) || (aDay  > 31))
         throw new NumberFormatException("illegal day");
      
      year = aYear;
      month = aMonth;
      day = aDay;
   }
   
   public String toString ()
   {
      if (isDefined()) {
         DecimalFormat df = new DecimalFormat("0");
         DecimalFormat df2 = new DecimalFormat("00");
         return df.format(year) + " " + df2.format(month) + " " + df2.format(day);
      }
      return "-";
   }
   
   public boolean isDefined ()
   {
      return ((day != 0) && (month != 0) && (year != 0));
   }
   
   public int year ()
   {
      return year;
   }
   
   public int month ()
   {
      return month;
   }
   
   public int day ()
   {
      return day;
   }
}
