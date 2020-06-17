package com.utilities.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.AbstractConverter;

public class StringToLocalDateTime extends AbstractConverter<String,LocalDateTime> {

	@Override
	protected LocalDateTime convert(String source){
		if(source == null)
		return null;
		return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
