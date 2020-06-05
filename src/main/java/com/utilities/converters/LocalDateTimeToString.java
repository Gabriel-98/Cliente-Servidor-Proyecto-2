package com.utilities.converters;

import java.time.LocalDateTime;

import org.modelmapper.AbstractConverter;

public class LocalDateTimeToString extends AbstractConverter<LocalDateTime,String> {

	@Override
	protected String convert(LocalDateTime source){
		return source.toString();
	}
}
