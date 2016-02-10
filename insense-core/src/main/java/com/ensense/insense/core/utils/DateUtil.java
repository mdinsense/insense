package com.ensense.insense.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil
{
	public static final String DATE_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss";
	public static final String DATE_PICKER_FORMAT = "MM/dd/yyyy";
	public static final String TIME_FORMAT= "HH:mm:ss";
	public static final String UI_DATE_PICKER_FORMAT = "yyyy/MM/dd HH:mm";
	public static final String WS_DATE_PICKER_FORMAT = "yyyy/MM/dd";
    public static final String[] DAYS = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	
	public static String convertToString(Date date, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
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
	
	public static String covertHMS(long millis)   {
		String hms = "";

		try {
			hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		} catch(Exception exp){
		}
		return hms;
	}
}
