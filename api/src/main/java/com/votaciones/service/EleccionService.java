package com.votaciones.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utilities.Time;
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
		// Validar que algunos datos no sean nulos
		validations.nonNullValidationResponse(eleccionDTO, "Error! La eleccion no puede ser nula");
		validations.nonNullValidationResponse(eleccionDTO.getCodigo(), "Error! El codigo no puede ser nulo");
		validations.nonNullValidationResponse(eleccionDTO.getNombre(), "Error! El nombre no puede ser nulo");
		validations.nonNullValidationResponse(eleccionDTO.getFechaInicio(), "Error! La fecha de inicio no puede ser nula");
		validations.nonNullValidationResponse(eleccionDTO.getFechaFin(), "Error! La fecha de cierre no puede ser nula");
		
		// Asignar valores por defecto a valores nulos
		eleccionDTO.setDescripcion(validations.selectDefaultValue(eleccionDTO.getDescripcion(), ""));
		eleccionDTO.setZonaHoraria(validations.selectDefaultValue(eleccionDTO.getZonaHoraria(), "UTC"));
		
		// Validar formatos
		validations.zoneIdValidationResponse(eleccionDTO.getZonaHoraria(), false, "Error! Formato incorrecto para la zona horaria");
		validations.isoLocalDateTimeValidationResponse(eleccionDTO.getFechaInicio(), false, "Error! Formato incorrecto para la fecha de inicio");
		validations.isoLocalDateTimeValidationResponse(eleccionDTO.getFechaFin(), false, "Error! Formato incorrecto para la fecha de cierre");
		
		// Convertir las fechas en una ZonaHoraria a las respectivas fechas en UTC
		eleccionDTO.setFechaInicio(Time.toUTC(eleccionDTO.getFechaInicio(), eleccionDTO.getZonaHoraria()));
		eleccionDTO.setFechaFin(Time.toUTC(eleccionDTO.getFechaFin(), eleccionDTO.getZonaHoraria()));
		
		Eleccion eleccion = modelMapper.map(eleccionDTO, Eleccion.class);
		eleccion.setCancelada(false);
		
		// Verificaciones de fechas
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		if(eleccion.getFechaInicio().isAfter(eleccion.getFechaFin()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha de cierre debe ser posterior a la fecha de inicio");
		if(fechaHoraActual.isAfter(eleccion.getFechaInicio()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha de inicio debe ser posterior a la fecha actual");
		
		if(eleccionRepository.existsById(eleccion.getCodigo()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe una eleccion con ese codigo");
		if(eleccionRepository.existsByNombre(eleccion.getNombre()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe una eleccion con ese nombre");
		
		Eleccion eleccionRespuesta = eleccionRepository.save(eleccion);
		EleccionDTO eleccionRespuestaDTO = modelMapper.map(eleccionRespuesta, EleccionDTO.class);
		eleccionRespuestaDTO.setZonaHoraria(eleccionDTO.getZonaHoraria());
		eleccionRespuestaDTO.setFechaInicio(Time.fromUTC(eleccionRespuestaDTO.getFechaInicio(), eleccionDTO.getZonaHoraria()));
		eleccionRespuestaDTO.setFechaFin(Time.fromUTC(eleccionRespuestaDTO.getFechaFin(), eleccionDTO.getZonaHoraria()));
		
		return eleccionRespuestaDTO;
	}
	
	public EleccionDTO editar(EleccionDTO eleccionDTO){
		// Validar que algunos datos no sean nulos
		validations.nonNullValidationResponse(eleccionDTO, "Error! La eleccion no puede ser nula");
		validations.nonNullValidationResponse(eleccionDTO.getCodigo(), "Error! El codigo no puede ser nulo");
		
		// Asignar valores por defecto a valores nulos
		eleccionDTO.setZonaHoraria(validations.selectDefaultValue(eleccionDTO.getZonaHoraria(), "UTC"));
		
		// Validar formatos
		validations.zoneIdValidationResponse(eleccionDTO.getZonaHoraria(), false, "Error! La zona horaria no existe o tiene formato incorrecto");
		validations.isoLocalDateTimeValidationResponse(eleccionDTO.getFechaInicio(), true, "Error! El formato de fecha de inicio es incorrecto");
		validations.isoLocalDateTimeValidationResponse(eleccionDTO.getFechaFin(), true, "Error! El formatato de fecha de cierre incorrecto");
		
		// Convertir las fechas en una ZonaHoraria a las respectivas fechas en UTC 
		eleccionDTO.setFechaInicio(Time.toUTC(eleccionDTO.getFechaInicio(), eleccionDTO.getZonaHoraria()));
		eleccionDTO.setFechaFin(Time.toUTC(eleccionDTO.getFechaFin(), eleccionDTO.getZonaHoraria()));
		
		Optional<Eleccion> optionalEleccion = eleccionRepository.findById(eleccionDTO.getCodigo());
		if(!optionalEleccion.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
		
		Eleccion eleccionPasado = optionalEleccion.get();
		Eleccion eleccion = modelMapper.map(eleccionDTO, Eleccion.class);
		
		if(eleccion.getNombre() != null && !eleccion.getNombre().equals(eleccionPasado.getNombre())){
			if(eleccionRepository.existsByNombre(eleccion.getNombre()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe una eleccion con ese nombre");
		}
		
		// Verificaciones de fechas
		if(eleccionPasado.getCancelada() && (eleccionDTO.getFechaInicio() != null || eleccionDTO.getFechaFin() != null))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion fue cancelada y no puede modificar las fechas");
		
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		
		if(fechaHoraActual.plusSeconds(10).isAfter(eleccionPasado.getFechaFin())){
			if(eleccion.getFechaInicio() != null || eleccionDTO.getFechaFin() != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Las fechas de la eleccion no se pueden modificar cuando la eleccion finalizo o esta a punto de finalizar");
		}
		else if(fechaHoraActual.plusSeconds(10).isAfter(eleccionPasado.getFechaInicio())) {
			if(eleccion.getFechaInicio() != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No puede modificar la fecha de inicio cuando las elecciones empezaron o estan a punto de empezar");
			if(eleccion.getFechaFin() != null){
				if(fechaHoraActual.plusSeconds(10).isAfter(eleccion.getFechaFin()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha de cierre debe ser almenos de 10 segundos mas tarde de la fecha y hora actual");
			}
		}
		else{
			if(eleccion.getFechaInicio() != null && eleccion.getFechaFin() != null){
				if(eleccion.getFechaInicio().isAfter(eleccion.getFechaFin()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha de cierre debe ser posterior a la de inicio");
				if(fechaHoraActual.isAfter(eleccion.getFechaInicio()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha y hora de inicio tiene que ser posterior a la actual");
			}
			else if(eleccion.getFechaInicio() != null){
				if(fechaHoraActual.isAfter(eleccion.getFechaInicio()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha y hora de inicio no debe ser antes de la fecha y hora actual");
				if(eleccion.getFechaInicio().isAfter(eleccionPasado.getFechaFin()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha y hora de inicio no debe ser posterior a la fecha y hora de cierre");
			}
			else if(eleccion.getFechaFin() != null){
				if(!eleccion.getFechaFin().isAfter(eleccionPasado.getFechaInicio()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La fecha y hora de cierre debe ser posterior a la fecha y hora de inicio");
			}
		}
		
		// Modificar los datos en la entidad que se va guardar
		eleccionPasado.setNombre(validations.updateValue(eleccionPasado.getNombre(), eleccion.getNombre()));
		eleccionPasado.setDescripcion(validations.updateValue(eleccionPasado.getDescripcion(), eleccion.getDescripcion()));
		eleccionPasado.setFechaInicio(validations.updateValue(eleccionPasado.getFechaInicio(), eleccion.getFechaInicio()));
		eleccionPasado.setFechaFin(validations.updateValue(eleccionPasado.getFechaFin(), eleccion.getFechaFin()));
		
		Eleccion eleccionRespuesta = eleccionRepository.save(eleccionPasado);
		EleccionDTO eleccionRespuestaDTO = modelMapper.map(eleccionRespuesta, EleccionDTO.class);		
		return eleccionRespuestaDTO;
	}
	
	public boolean finalizar(String codigo){
		// Validar que algunos datos no sean nulos
		validations.nonNullValidationResponse(codigo, "Error! El codigo no puede ser nulo");
		
		Optional<Eleccion> optionalEleccion = eleccionRepository.findById(codigo);
		if(!optionalEleccion.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
			
		Eleccion eleccion = optionalEleccion.get();
		
		if(eleccion.getCancelada())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion esta cancelada");
		
		// Verificacion de fechas
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));	
		if(fechaHoraActual.isBefore(eleccion.getFechaInicio()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion no ha iniciado");
		if(fechaHoraActual.isAfter(eleccion.getFechaFin()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion ya habia finalizado");
		
		if(fechaHoraActual.plusSeconds(10).isBefore(eleccion.getFechaFin()))
		eleccion.setFechaFin(fechaHoraActual.plusSeconds(10));
		eleccionRepository.save(eleccion);		
		
		return true;
	}
	
	public boolean cancelar(String codigo){
		validations.nonNullValidationResponse(codigo, "Error! El codigo es nulo");
		
		Optional<Eleccion> optionalEleccion = eleccionRepository.findById(codigo);
		if(!optionalEleccion.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
		
		Eleccion eleccion = optionalEleccion.get();
		if(eleccion.getCancelada())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion ya habia sido cancelada");
		
		eleccion.setCancelada(true);
		eleccionRepository.save(eleccion);
		return true;
	}
	
	
	//public List<VotoDTO> resultadoEleccion(String codigo){}
	
	
	public List<EleccionDTO> listar(String filtro, String zonaHoraria){
		validations.nonNullValidationResponse(filtro, "Error! El filtro no puede ser nulo");
		validations.nonNullValidationResponse(zonaHoraria, "Error! La zona horaria no puede ser nula");
	
		validations.zoneIdValidationResponse(zonaHoraria, false, "Error! La zona horaria no existe o tiene un formato incorrecto");
		
		try{ ZoneId.of(zonaHoraria); }
		catch(Exception e){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La zona horaria no existe o tiene un formato incorrecto"); }
		
		List<Eleccion> elecciones;
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		if(filtro.equals("FINALIZADAS"))
		elecciones = eleccionRepository.findAllByCanceladaAndFechaFinBeforeOrderByFechaInicioAscFechaFinAsc(false, fechaHoraActual);	
		else if(filtro.equals("ABIERTAS"))
		elecciones = eleccionRepository.findAllByCanceladaAndFechaInicioBeforeAndFechaFinAfterOrderByFechaInicioAscFechaFinAsc(false, fechaHoraActual, fechaHoraActual);
		else if(filtro.equals("PENDIENTES"))
		elecciones = eleccionRepository.findAllByCanceladaAndFechaInicioAfterOrderByFechaInicioAscFechaFinAsc(false, fechaHoraActual);
		else if(filtro.equals("CANCELADAS"))
		elecciones = eleccionRepository.findAllByCanceladaOrderByFechaInicioAscFechaFinAsc(true);
		else
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El filtro no existe");
	
		List<EleccionDTO> eleccionesDTO = new LinkedList<EleccionDTO>();
		ListIterator<Eleccion> iterator = elecciones.listIterator();
		while(iterator.hasNext()){
			Eleccion eleccion = iterator.next();
			EleccionDTO eleccionDTO = modelMapper.map(eleccion, EleccionDTO.class);
			eleccionDTO.setZonaHoraria(zonaHoraria);
			eleccionDTO.setFechaInicio(Time.fromUTC(eleccionDTO.getFechaInicio(), zonaHoraria));
			eleccionDTO.setFechaFin(Time.fromUTC(eleccionDTO.getFechaFin(), zonaHoraria));
			eleccionesDTO.add(eleccionDTO);
		}
		
		return eleccionesDTO;
	}
}
