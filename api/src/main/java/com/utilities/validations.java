package com.utilities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class validations {
	
	public static void nullValidationResponse(Object o, String message){
		if(o == null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}
	
	public static <T extends Comparable<T>> void updatableValidationResponse(T o1, T o2, String message){
		if(o1.compareTo(o2) == 0)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}
	
	public static void zoneIdValidationResponse(String s, String message) {
		try{ ZoneId.of(s); }
		catch(Exception e){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message); }
	}
	
	public static void isoLocalDateTimeValidationResponse(String s, String message){
		try{ LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
		catch(Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message); }
	}
}
