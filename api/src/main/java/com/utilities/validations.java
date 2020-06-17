package com.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class validations {
	
	public static void nonNullValidationResponse(Object o, String message){
		if(o == null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}
	
	public static void nonUpdatableValidationResponse(Object o1, Object o2, String message){
		if(!o1.equals(o2))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}
	
	public static void zoneIdValidationResponse(String zone, boolean allowNull, String message) {
		// allowNull: permite a null ser un formato valido
		try{
			if(allowNull){
				if(zone != null)
				ZoneId.of(zone);
			}
			else
			ZoneId.of(zone);
		}
		catch(Exception e){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message); }
	}
	
	public static void isoLocalDateTimeValidationResponse(String dateTime, boolean permitNull, String message){
		try{
			if(permitNull){
				if(dateTime != null)
				LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}
			else
			LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}
		catch(Exception e){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message); }
	}
	
	public static <T> T selectDefaultValue(T currentValue, T defaultValue){
		if(currentValue == null)
		return defaultValue;
		return currentValue;
	}
	
	public static <T> T updateValue(T currentValue, T newValue){
		if(newValue == null)
		return currentValue;
		return newValue;
	}
	
	public static void onlyNumbers(String text, String message){
		for(int i=0; i<text.length(); i++) {
			if(!('0' <= text.charAt(i) && text.charAt(i) <= '9'))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
	}
}
