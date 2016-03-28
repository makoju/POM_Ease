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
		
		Date d = convertFromAnyTimeZoneToCSTTimeZone(strCurrentTime, strFromTimeZone);
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
		
		String strTime = formatter.format(d.getTime());
		
		System.out.println("strTime :"+strTime);
		
		return strTime; 
	}
	
	public static Date convertFromAnyTimeZoneToCSTTimeZone(String strCurrentTime, String strFromTimeZone) throws ParseException{
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
		
		return convertTimeZone(newDate, TimeZone.getTimeZone(strFromTimeZone), TimeZone.getTimeZone("CST"));
	}
	
	public static  String convertToCronTimeCSTTimeZone(String strCurrentTime, String strFromTimeZone) throws ParseException{
		Date d = convertFromAnyTimeZoneToCSTTimeZone(strCurrentTime, strFromTimeZone);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		String strTime = formatter.format(d.getTime());
		
		System.out.println("strTime :"+strTime);
		
		return generateCronExpression(String.valueOf(d.getSeconds()), String.valueOf(d.getMinutes()), String.valueOf(d.getHours()), "*", "*", "?");
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

	/**
	 * Generate a CRON expression is a string comprising 6 or 7 fields separated by white space.
	 *
	 * @param seconds    mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param minutes    mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param hours      mandatory = yes. allowed values = {@code 0-23   * / , -}
	 * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
	 * @param month      mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
	 * @param dayOfWeek  mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
	 * @param year       mandatory = no. allowed values = {@code 1970–2099    * / , -}
	 * @return a CRON Formatted String.
	 */
	private static String generateCronExpression(final String seconds, final String minutes, final String hours,
	                                             final String dayOfMonth,
	                                             final String month, final String dayOfWeek)
	{
	  return String.format("%1$s %2$s %3$s %4$s %5$s %6$s", seconds, minutes, hours, dayOfMonth, month, dayOfWeek);
	}

}
