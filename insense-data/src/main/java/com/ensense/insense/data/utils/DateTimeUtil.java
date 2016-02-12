package com.ensense.insense.data.utils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtil {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	  public static String getCurrentDateTime(){
		  Format formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		  Date today = Calendar.getInstance().getTime();
		  String date =formatter.format(today);
		 
		 return date;
	  }
	  
	  public static String getCurrentDateTimeyyyy_MM_dd_HH_mm_ss(){
		  Format formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		  Date today = Calendar.getInstance().getTime();
		  String date =formatter.format(today);
		 
		 return date;
	  }
	  
	  public static String getDateFormat(Date date){
		  String dateStr = "";
		  try {
			  Format formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			  dateStr =formatter.format(date);
			  
		  }catch(Exception e){
			  dateStr = "";
		  }
		 
		 return dateStr;
	  }

	  public static String getDateFormatyyyyMMdd(Date date){
		  String dateStr = "";
		  try {
			  Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			  dateStr =formatter.format(date);
			  
		  }catch(Exception e){
			  dateStr = "";
		  }
		 
		 return dateStr;
	  }
	public static Time getTimeInFormatHHSS(Date date) {
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		String stringTime = formatter.format(date);
		Time timeValue = null;
		try {
			Date t = (Date) formatter.parse(stringTime);
			timeValue = new Time(formatter.parse(stringTime).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return timeValue;
	}
	 /* public static void main(String[] args) {
		String date = DateTimeUtil.getDateFormat(new Date());
		System.out.println(date);
	}*/
	
	public static String formatDateString(String strDate, String format){
		
		if ( null == strDate || strDate.length() < 9 ){
			return "";
		}
		
		SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_TIME_FORMAT);
		SimpleDateFormat sdfDestination = new SimpleDateFormat(format);
		Date date = null;
		
		try {
			date = sdfSource.parse(strDate);
			strDate = sdfDestination.format(date);
		} catch (ParseException e) {
			strDate = "";
		}catch (Exception e){
			strDate = "";
		}
		return strDate;
	}
	
	public static String convertToString(Date date, String format)
	{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}catch(Exception e){
			return "";
		}
	}

	public static Date convertToDate(String dateInString, String format)
	{
		Date date = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(dateInString);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getCurrentDateTimeAsString(String format){
		Date date = new Date();
		Format formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

    public static String getCurrentDayName(){
      String dayNames[] = new DateFormatSymbols().getWeekdays();
      Calendar date2 = Calendar.getInstance();
      return dayNames[date2.get(Calendar.DAY_OF_WEEK)];
    }
    
    public static boolean isBeforeCurrentTime(Time time) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 19); // Your hour
    	cal.set(Calendar.MINUTE, 30); // Your Mintue
    	cal.set(Calendar.SECOND, 00); // Your second
    	Time currentTime = new Time(cal.getTime().getTime());
    	return time.before(currentTime);
    }
    
    public static boolean isBeforeCurrentDay(String day){
    	boolean pastDay = false; 
        List<String> weeks = Arrays.asList(new DateFormatSymbols().getWeekdays());
        Calendar date2 = Calendar.getInstance();
        int currentDayIndex = date2.get(Calendar.DAY_OF_WEEK);
        int givenDayIndex = weeks.indexOf(day);
        if(givenDayIndex < currentDayIndex) {
        	pastDay = true;
        }
        return pastDay;
      }
    
	public static Date getEndOfTheDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
	
	public static Date getStartOfTheDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}

	public static Date addSeconds(Date date, int seconds) {
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.SECOND, seconds);
		 return calendar.getTime();
	}
}
