package com.utilities.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.modelmapper.AbstractConverter;

public class StringToLocalDate extends AbstractConverter<String,LocalDate> {

	@Override
	protected LocalDate convert(String source){
		if(source == null)
		return null;
		return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
	}
}
