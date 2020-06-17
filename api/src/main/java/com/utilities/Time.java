package com.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Time {
	
	// return seconds of difference between UTC y zoneId
	public static int totalSeconds(ZoneId zone, LocalDateTime dateTime){
		return zone.getRules().getOffset(dateTime).getTotalSeconds();
	}
	
	public static int totalSeconds(String zone, String dateTime){
		return totalSeconds(ZoneId.of(zone), LocalDateTime.parse(dateTime));
	}
	
	public static LocalDateTime toUTC(LocalDateTime dateTime, ZoneId sourceZone){
		if(dateTime == null || sourceZone == null)
		return null;
		int difSeconds = totalSeconds(sourceZone, dateTime);
		return dateTime.minusSeconds(difSeconds);
	}
	
	public static String toUTC(String dateTime, String sourceZone){
		if(dateTime == null || sourceZone == null)
		return null;
		LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return toUTC(localDateTime, ZoneId.of(sourceZone)).toString();
	}
	
	public static LocalDateTime fromUTC(LocalDateTime dateTime, ZoneId targetZone){
		if(dateTime == null || targetZone == null)
		return null;
		int difSeconds = totalSeconds(targetZone, dateTime);
		return dateTime.plusSeconds(difSeconds);
	}
	
	public static String fromUTC(String dateTime, String targetZone){
		if(dateTime == null || targetZone == null)
		return null;
		LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return fromUTC(localDateTime, ZoneId.of(targetZone)).toString();
	}
	
	public static LocalDateTime toTimeZone(LocalDateTime dateTime, ZoneId sourceZone, ZoneId targetZone){
		if(dateTime == null || sourceZone == null || targetZone == null)
		return null;
		LocalDateTime dateTimeUTC = toUTC(dateTime, sourceZone);
		return fromUTC(dateTimeUTC, targetZone);
	}
	
	public static String toTimeZone(String dateTime, String sourceZone, String targetZone){
		if(dateTime == null || sourceZone == null || targetZone == null)
		return null;
		LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return toTimeZone(localDateTime, ZoneId.of(sourceZone), ZoneId.of(targetZone)).toString();
	}
	
	public static int differenceInYears(LocalDateTime ldt1, LocalDateTime ldt2){
		// years(ldt1) - years(ldt2)
		int ans = 0, sign = 1;
		if(ldt2.isAfter(ldt1)){
			LocalDateTime temp = ldt1;
			ldt1 = ldt2;	ldt2 = temp;
			sign = -1;
		}		
		ans = ldt1.getYear() - ldt2.getYear();
		if(ldt1.getMonthValue() < ldt2.getMonthValue())
		ans--;
		else if(ldt1.getMonthValue() == ldt2.getMonthValue()){
			if(ldt1.getDayOfMonth() < ldt2.getDayOfMonth())
			ans--;
		}
		return ans * sign;
	}
	
	public static int differenceInYears(String dateTime1, String dateTime2) {
		LocalDateTime ldt1 = LocalDateTime.parse(dateTime1, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime ldt2 = LocalDateTime.parse(dateTime2, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return differenceInYears(ldt1, ldt2);
	}
}
