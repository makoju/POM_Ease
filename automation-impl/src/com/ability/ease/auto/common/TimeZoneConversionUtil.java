package com.ability.ease.auto.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneConversionUtil {
	
	/*public static  String convertToTimeZone(String strCurrentTime, String StrFromTimeZone) throws ParseException{
		SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a");
		Date newDate = formatter1.parse(strCurrentTime);//In our testing--  time will be picking  from dropdown of jsystem
		
		System.out.println("newDate :"+newDate);
		
		Date d = convertTimeZone(newDate, TimeZone.getTimeZone(StrFromTimeZone), null);
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
		
		String strTime = formatter.format(d.getTime());
		
		System.out.println("strTime :"+strTime);
		
		return strTime; 
	}*/
	
	public static  String convertToCSTTimeZone(String strCurrentTime, String strFromTimeZone) throws ParseException{
		
		if(strFromTimeZone.equalsIgnoreCase("eastern")){
			strFromTimeZone="EST";
		}
		else if(strFromTimeZone.equalsIgnoreCase("pacific")){
			strFromTimeZone="PST";
		}
		else if(strFromTimeZone.equalsIgnoreCase("Central")){
			strFromTimeZone="CST";
		}
		else if(strFromTimeZone.equalsIgnoreCase("Mountain")){
			strFromTimeZone="MST";
		}
		
		SimpleDateFormat formatter1 = new SimpleDateFormat("h:mm a");
		Date newDate = formatter1.parse(strCurrentTime);//take the time from dropdown
		
		System.out.println("newDate :"+newDate);
		
		Date d = convertTimeZone(newDate, TimeZone.getTimeZone(strFromTimeZone), TimeZone.getTimeZone("CST"));
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
		
		String strTime = formatter.format(d.getTime());
		
		System.out.println("strTime :"+strTime);
		
		return strTime; 
	}
	
	public static  String convertToCronTimeCSTTimeZone(String strCurrentTime, String strFromTimeZone) throws ParseException{
		
		if(strFromTimeZone.equalsIgnoreCase("eastern")){
			strFromTimeZone="EST";
		}
		else if(strFromTimeZone.equalsIgnoreCase("pacific")){
			strFromTimeZone="PST";
		}
		else if(strFromTimeZone.equalsIgnoreCase("Central")){
			strFromTimeZone="CST";
		}
		else if(strFromTimeZone.equalsIgnoreCase("Mountain")){
			strFromTimeZone="MST";
		}
		
		SimpleDateFormat formatter1 = new SimpleDateFormat("h:mm a");
		Date newDate = formatter1.parse(strCurrentTime);//take the time from dropdown
		
		System.out.println("newDate :"+newDate);
		
		Date d = convertTimeZone(newDate, TimeZone.getTimeZone(strFromTimeZone), TimeZone.getTimeZone("CST"));
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		
		String strTime = formatter.format(d.getTime());
		
		System.out.println("strTime :"+strTime);
		
		return strTime; 
	}
	
	
	
	public static Date convertTimeZone(Date date, TimeZone fromTimeZone, TimeZone toTimeZone)
	{
	    long fromTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, fromTimeZone);
	    long toTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, toTimeZone);

	    return new Date(date.getTime() + (toTimeZoneOffset - fromTimeZoneOffset));
	}
	
	
	private static long getTimeZoneUTCAndDSTOffset(Date date, TimeZone timeZone)
	{
	    long timeZoneDSTOffset = 0;
	    if(timeZone.inDaylightTime(date))
	    {
	        timeZoneDSTOffset = timeZone.getDSTSavings();
	    }

	    return timeZone.getRawOffset() + timeZoneDSTOffset;
	}




	

}
