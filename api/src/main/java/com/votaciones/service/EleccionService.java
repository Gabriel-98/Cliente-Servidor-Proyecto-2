package com.votaciones.service;

import java.time.ZoneId;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utilities.validations;
import com.votaciones.dto.EleccionDTO;
import com.votaciones.entity.Eleccion;
import com.votaciones.repository.EleccionRepository;

@Service
public class EleccionService{
	
	@Autowired
	EleccionRepository eleccionRepository;
	
	@Autowired
	ModelMapper modelMapper;

	public EleccionDTO crear(EleccionDTO eleccionDTO){
		validations.nullValidationResponse(eleccionDTO, "Error! La eleccion no puede ser nula");
		validations.nullValidationResponse(eleccionDTO.getCodigo(), "Error! El codigo no puede ser nulo");
		validations.nullValidationResponse(eleccionDTO.getNombre(), "Error! El nombre no puede ser nulo");
		validations.nullValidationResponse(eleccionDTO.getFechaInicio(), "Error! La fecha de inicio no puede ser nula");
		validations.nullValidationResponse(eleccionDTO.getFechaFin(), "Error! La fecha de cierre no puede ser nula");
		
		validations.zoneIdValidationResponse(eleccionDTO.getZonaHoraria(), "Error! Formato incorrecto para la zona horaria");
		validations.isoLocalDateTimeValidationResponse(eleccionDTO.getFechaInicio(), "Error! Formato de fecha de inicio incorrecto");
		validations.isoLocalDateTimeValidationResponse(eleccionDTO.getFechaFin(), "Error! Formato de fecha de cierre incorrecto");
		
		if(eleccionRepository.existsById(eleccionDTO.getCodigo()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe una eleccion con ese codigo");
		
		if(eleccionDTO.getDescripcion() == null)
		eleccionDTO.setDescripcion("");
		if(eleccionDTO.getZonaHoraria() == null)
		eleccionDTO.setZonaHoraria("UTC");
		
		Eleccion eleccion = modelMapper.map(eleccionDTO, Eleccion.class);
		
		int seconds = ZoneId.of(eleccionDTO.getZonaHoraria()).getRules().getOffset(eleccion.getFechaInicio()).getTotalSeconds();
		eleccion.setFechaInicio(eleccion.getFechaInicio().plusSeconds(seconds));
		eleccion.setFechaFin(eleccion.getFechaFin().plusSeconds(seconds));
		
		Eleccion eleccionRespuesta = eleccionRepository.save(eleccion);
		eleccionRespuesta.setFechaInicio(eleccionRespuesta.getFechaInicio().minusSeconds(seconds));
		eleccionRespuesta.setFechaFin(eleccionRespuesta.getFechaFin().minusSeconds(seconds));
		EleccionDTO eleccionRespuestaDTO = modelMapper.map(eleccionRespuesta, EleccionDTO.class);

		return eleccionRespuestaDTO;
	}
	
	/*public List<EleccionDTO> listar(String filtro, String zonaHoraria){
		validations.nullValidationResponse(filtro, "Error! El filtro no puede ser nulo");
		validations.nullValidationResponse(zonaHoraria, "Error! La zona horaria no puede ser nula");
	
		
	}*/
}
