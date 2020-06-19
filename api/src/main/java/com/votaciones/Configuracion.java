package com.votaciones;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.utilities.converters.StringToLocalDate;
import com.utilities.converters.StringToLocalDateTime;

@Configuration
public class Configuracion {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(new StringToLocalDateTime());
		//modelMapper.addConverter(new LocalDateTimeToString());
		modelMapper.addConverter(new StringToLocalDate());
		return modelMapper;
	}
}
