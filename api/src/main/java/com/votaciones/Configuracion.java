package com.votaciones;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.utilities.converters.ByteVectorToString;
import com.utilities.converters.LocalDateTimeToString;
import com.utilities.converters.StringToByteVector;
import com.utilities.converters.StringToLocalDateTime;

@Configuration
public class Configuracion {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(new ByteVectorToString());
		modelMapper.addConverter(new StringToByteVector());
		modelMapper.addConverter(new StringToLocalDateTime());
		//modelMapper.addConverter(new LocalDateTimeToString());
		return modelMapper;
	}
}
